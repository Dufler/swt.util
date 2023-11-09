package com.dufler.swt.utils.elements;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.dialog.DialogApribile;
import com.dufler.swt.utils.dialog.DialogMessaggio;
import com.dufler.swt.utils.elements.table.filter.CriteriFiltraggio;

/**
 * Tabella che adempie alle funzioni di creazione, modifica ed eliminazione sugli elementi.
 * @author Damiano
 *
 * @param <T>
 */
public abstract class TabellaCRUD<T, C extends CriteriFiltraggio> extends Tabella<T, C> implements ITabella<T, C>, ITabellaCRUD<T> {
	
	protected final boolean deleteAvaible;
	protected MenuItem delete;
	protected MenuItem insert;
	protected MenuItem modify;
	protected MenuItem duplicate;
	
	private final Set<CrudTableListener> listeners;
	
	public TabellaCRUD(Composite parent) {
		this(parent, STILE_SEMPLICE, true, true);
	}

	public TabellaCRUD(Composite parent, int style) {
		this(parent, style, true, true);
	}
	
	public TabellaCRUD(Composite parent, int style, boolean deleteAvaible, boolean apriConDoppioClick) {
		super(parent, style);
		
		if (apriConDoppioClick)
			aggiungiListenerAperturaDoppioClick();
		
		this.deleteAvaible = deleteAvaible && isPermessoDelete();
		
		if (!deleteAvaible && delete != null) {
			delete.dispose();
		}
		
		listeners = new HashSet<>();
	}
	
	@Override
	protected void aggiungiMenuBase() {
		super.aggiungiMenuBase();
	    
	    //Aggiungo le voci di menù solo se l'utente ha i permessi richiesti
	    boolean permessiCRU = isPermesso();
	    if (permessiCRU) {
	    	aggiungiVoceMenuInserisci();
		    aggiungiVoceMenuModifica();		
	    }
	    
	    boolean permessoDelete = isPermessoDelete();
	    if (permessoDelete) {
	    	aggiungiVoceMenuElimina();
	    }
	    
	}
	
	/**
	 * Aggiunge un listener alla lista di quelli che devono essere notificati se il contenuto della tabella cambia a causa di un evento CRUD.
	 * @param listener
	 */
	public void addCrudListener(CrudTableListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Le tabelle il cui contenuto è gestito esternamente devono usare un listener CRUD per essere refreshate correttamente.
	 */
	protected void notificaListeners() {
		for (CrudTableListener listener : listeners) {
			listener.contentChanged();
		}
	}
	
	protected void aggiungiListenerAperturaDoppioClick() {		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				T elemento = getRigaSelezionata();
	    		if (elemento != null)
	    			apriDialog(elemento);
			}
		});
	}
	
	protected void aggiungiVoceMenuDuplica() {
		duplicate = new MenuItem(menuPopup, SWT.PUSH);
		duplicate.setText("Clona");
		duplicate.setImage(Immagine.COPIA_16X16.getImage());
		duplicate.addListener(SWT.Selection, new Listener() {
	    	public void handleEvent(Event event) {
	    		T elemento = getRigaSelezionata();
		   		if (elemento != null)
		   			duplicaElemento(elemento);
	    	}
	    });
	}
	
	protected void aggiungiVoceMenuInserisci() {
		insert = new MenuItem(menuPopup, SWT.PUSH);
	    insert.setText("Nuovo");
	    insert.setImage(Immagine.CROCEVERDE_16X16.getImage());
	    insert.addListener(SWT.Selection, new Listener() {
	    	public void handleEvent(Event event) {
	    		apriDialog(null);
	    	}
	    });
	}
	
	protected void aggiungiVoceMenuModifica() {
		 modify = new MenuItem(menuPopup, SWT.PUSH);
		 modify.setText("Modifica");
		 modify.setImage(Immagine.MATITA_16X16.getImage());
		 modify.addListener(SWT.Selection, new Listener() {
		  	public void handleEvent(Event event) {
		   		T elemento = getRigaSelezionata();
		   		if (elemento != null)
		   			apriDialog(elemento);
		   	}
		 });
	}
	
	protected void aggiungiVoceMenuElimina() {
		delete = new MenuItem(menuPopup, SWT.PUSH);
	    delete.setText("Elimina");
	    delete.setImage(Immagine.CESTINO_16X16.getImage());
	    delete.addListener(SWT.Selection, new Listener() {
	    	public void handleEvent(Event event) {
	    		T elemento = getRigaSelezionata();
	    		if (elemento != null)
	    			apriDialogElimina(elemento);
	    	}
	    });
	}
	
	/**
	 * Abilita i comandi del menù a popup 
	 * @param abilita true se vanno abilitati, false se vanno disabilitati.
	 */
	public void abilitaComandiMenu(boolean abilita) {
		boolean permessiCRU = isPermesso();
//		copy.setEnabled(abilita);
		if (insert != null) insert.setEnabled(abilita && permessiCRU);
		if (modify != null) modify.setEnabled(abilita && permessiCRU);
		if (delete != null) delete.setEnabled(abilita && permessiCRU);
		if (duplicate != null) duplicate.setEnabled(abilita && permessiCRU);
	}
	
	protected abstract boolean isPermesso();
		
	/**
	 * A seguito di una operazione CRUD imposta la tabella come "dirty", aggiorna il contenuto e invia una notifica ai listener. 
	 */
	protected void notificaCambiamentoContenuto() {
		aggiornaContenuto(); 
		refresh();
		dirty = true;
		notificaListeners();
	}

	@Override
	public void apriDialog(T elemento) {
		DialogApribile dialog = creaDialog(elemento);
		int result = dialog != null ? dialog.open() : -1;
		if (result == Dialog.OK) {
			notificaCambiamentoContenuto();
		}
	}
	
	protected abstract DialogApribile creaDialog(T elemento);
	
	@Override
	public void apriDialogElimina(T elemento) {
		if (deleteAvaible) {
			boolean scelta = DialogMessaggio.openConfirm("Eliminazione", "Sei sicuro di volerlo eliminare?");
			if (scelta) {
				boolean esito = eliminaElemento(elemento);
				if (!esito) {
					DialogMessaggio.openWarning("Eliminazione non riuscita", "Impossibile eliminare l'elemento selezionato. Se pensi che questo sia un bug contattare il CED.");
				} else {
					notificaCambiamentoContenuto();
				}
			}
		} else {
			DialogMessaggio.openInformation("Eliminazione non disponibile", "L'eliminazione degli elementi non \u00E8 disponibile.");
		}
	}
	
	@Override
	public void duplicaElemento(T elemento) {
		throw new UnsupportedOperationException("Non ancora implementato.");
	}
	
	protected abstract boolean eliminaElemento(T elemento);
	
	protected abstract boolean isPermessoDelete();
	
	@Override
	protected void aggiungiListener() {
		// Di solito non è necessario.	
	}

	@Override
	protected void aggiungiMenu(Menu menu) {
		// Di solito non è necessario.		
	}

}
