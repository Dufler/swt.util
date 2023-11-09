package com.dufler.swt.utils.elements;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.dialog.DialogApribile;
import com.dufler.swt.utils.dialog.DialogMessaggio;
import com.dufler.swt.utils.elements.table.filter.CriteriFiltraggio;

public abstract class TabellaCheckBoxCRUD<T, C extends CriteriFiltraggio> extends TabellaCheckBox<T, C> implements ITabella<T, C>, ITabellaCRUD<T> {
	
	protected final boolean deleteAvaible;
	protected MenuItem delete;
	protected MenuItem insert;
	protected MenuItem modify;
	
	public TabellaCheckBoxCRUD(Composite parent) {
		this(parent, STILE_SELEZIONE_MULTIPLA, true, true);
	}

	public TabellaCheckBoxCRUD(Composite parent, int style) {
		this(parent, style, true, true);
	}
	
	public TabellaCheckBoxCRUD(Composite parent, int style, boolean deleteAvaible, boolean apriConDoppioClick) {
		super(parent, style);
		
		this.deleteAvaible = deleteAvaible && isPermessoDelete();
		
		if (!deleteAvaible && delete != null) {
			delete.dispose();
		}
		
		if (apriConDoppioClick)
			aggiungiListenerAperturaDoppioClick();
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
	
	@Override
	protected void aggiungiMenuBase() {
		//Aggiungo le voci base
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
	
	protected abstract boolean isPermesso();

	@Override
	public void apriDialog(T elemento) {
		DialogApribile dialog = creaDialog(elemento);
		int result = dialog != null ? dialog.open() : -1;
		if (result == Dialog.OK) {
			aggiornaContenuto(); //FIXME - Forse potrei mettere un refresh() ? Le tabelle il cui contenuto è gestito esternamente non vengono refreshate correttamente.
			refresh();
			dirty = true;
		}
	}
	
	protected abstract DialogApribile creaDialog(T elemento);
	
	/**
	 * Abilita i comandi del menù a popup 
	 * @param abilita true se vanno abilitati, false se vanno disabilitati.
	 */
	public void abilitaComandiMenu(boolean abilita) {
		boolean permessiCRU = isPermesso();
		copy.setEnabled(abilita);
		if (insert != null) insert.setEnabled(abilita && permessiCRU);
		if (modify != null) modify.setEnabled(abilita && permessiCRU);
		if (delete != null) delete.setEnabled(abilita && permessiCRU);
	}
	
	@Override
	public void apriDialogElimina(T elemento) {
		if (deleteAvaible) {
			boolean scelta = DialogMessaggio.openConfirm("Eliminazione", "Sei sicuro di volerlo eliminare?");
			if (scelta) {
				boolean esito = eliminaElemento(elemento);
				if (!esito) {
					DialogMessaggio.openWarning("Eliminazione non riuscita", "Impossibile eliminare l'elemento selezionato. Se pensi che questo sia un bug contattare il CED.");
				} else {
					aggiornaContenuto();
					dirty = true;
				}
			}
		} else {
			DialogMessaggio.openInformation("Eliminazione non disponibile", "L'eliminazione degli elementi non \u00E8 disponibile.");
		}
	}
	
	protected abstract boolean eliminaElemento(T elemento);
	
	protected abstract boolean isPermessoDelete();

}
