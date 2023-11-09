package com.dufler.swt.utils.elements;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.dialog.DialogMessaggio;

/**
 * Questa classe contiene i componenti base di una toolbar, viene estesa e raffinata.
 * @author Damiano
 * @param <U>
 * @param <T>
 *
 */
public abstract class ToolbarCRUD<T extends ITabellaCRUD<U> & ITabella<U, ?>, U> extends ToolbarSemplice<T, U> {
	
	protected ToolItem nuovo;
	protected ToolItem modifica;
	protected ToolItem elimina;
	protected ToolItem refresh;
	protected ToolItem dropDown;
	protected ToolItem duplica;
	
	protected Composite compositeSx;
	protected Composite compositeDx;

	public ToolbarCRUD(Composite parent) {
		super(parent);
	}
	
	/**
	 * Metodo da implementare, eseguirà l'azione corrispondente all'inserimento di un elemento.
	 */
	protected void nuovoElemento() {
		tabella.apriDialog(null);
	}
	
	/**
	 * Metodo da implementare, eseguirà l'azione corrispondente alla modifica di un elemento.
	 */
	protected void modificaElemento() {
		U elemento = tabella.getRigaSelezionata();
		if (elemento != null) {
			tabella.apriDialog(elemento);
		} else {
			mostraMessaggioSelezione();
		}
	}
	
	/**
	 * Metodo da implementare, eseguirà l'azione corrispondente all'eliminazione di un elemento.
	 */
	protected void eliminaElemento() {
		if (tabella instanceof TabellaCRUD<?, ?>) {
			U elemento = tabella.getRigaSelezionata();
			if (elemento != null) {
				tabella.apriDialogElimina(elemento);
			} else {
				mostraMessaggioSelezione();
			}
		}
	}
	
	protected void refreshTabella() {
		tabella.aggiornaContenuto();
	}
	
	protected void aggiungiSeparatore() {
		new ToolItem(toolbar, SWT.SEPARATOR);
	}
		
	/**
	 * Metodo di convenienza per aggiungere un tasto "Copia" senza testo, solo icona.
	 */
	protected void aggiungiTastoDuplica() {
		aggiungiTastoDuplica(false);
	}
	
	/**
	 * Metodo di convenienza per aggiungere il tasto "Copia" specificando se deve esserci il testo o meno,
	 * @param testo indica se dovrà essere mostrato o meno il testo.
	 */
	protected void aggiungiTastoDuplica(boolean testo) {
		duplica = new ToolItem(toolbar, SWT.NONE);
		duplica.setImage(Immagine.COPIA_16X16.getImage());
		duplica.setDisabledImage(Immagine.COPIA_ANNULLATA_16X16.getImage());
		duplica.setText(testo ? "Clona" : "");
		duplica.setToolTipText("Clona");
		duplica.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				duplicaElemento();
			}
		});
	}
	
	/**
	 * Metodo da implementare, eseguirà l'azione corrispondente alla copia di un elemento.
	 */
	protected void duplicaElemento() {
		U elemento = tabella.getRigaSelezionata();
		if (elemento != null) {
			tabella.duplicaElemento(elemento);
		} else {
			mostraMessaggioSelezione();
		}
	}
	
	/**
	 * Metodo di convenienza per aggiungere un tasto "Nuovo" senza testo, solo icona.
	 */
	protected void aggiungiTastoNuovo() {
		aggiungiTastoNuovo(false);
	}
	
	/**
	 * Metodo di convenienza per aggiungere il tasto "Nuovo" specificando se deve esserci il testo o meno,
	 * @param testo indica se dovrà essere mostrato o meno il testo.
	 */
	protected void aggiungiTastoNuovo(boolean testo) {
		nuovo = new ToolItem(toolbar, SWT.NONE);
		nuovo.setImage(Immagine.CROCEVERDE_16X16.getImage());
		nuovo.setDisabledImage(Immagine.CROCEVERDE_ANNULLATA_16X16.getImage());
		nuovo.setText(testo ? "Nuovo" : "");
		nuovo.setToolTipText("Nuovo");
		nuovo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				nuovoElemento();
			}
		});
	}
	
	protected void aggiungiTastoModifica() {
		aggiungiTastoModifica(false);
	}
	
	protected void aggiungiTastoModifica(boolean testo) {
		modifica = new ToolItem(toolbar, SWT.NONE);
		modifica.setImage(Immagine.MATITA_16X16.getImage());
		modifica.setDisabledImage(Immagine.MATITA_ANNULLATA_16X16.getImage());
		modifica.setText(testo ? "Modifica" : "");
		modifica.setToolTipText("Modifica");
		modifica.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				modificaElemento();
			}
		});
	}
	
	protected void aggiungiTastoElimina() {
		aggiungiTastoElimina(false);
	}
	
	protected void aggiungiTastoElimina(boolean testo) {
		elimina = new ToolItem(toolbar, SWT.NONE);
		elimina.setImage(Immagine.CESTINO_16X16.getImage());
		elimina.setDisabledImage(Immagine.CESTINO_ANNULLATO_16X16.getImage());
		elimina.setText(testo ? "Elimina" : "");
		elimina.setToolTipText("Elimina");
		elimina.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				eliminaElemento();
			}
		});
	}
	
	protected void aggiungiTastoRefresh() {
		aggiungiTastoRefresh(false);
	}
	
	protected void aggiungiTastoRefresh(boolean testo) {
		refresh = new ToolItem(toolbar, SWT.NONE);
		refresh.setImage(Immagine.REFRESH_16X16.getImage());
		refresh.setText(testo ? "Ricarica" : "");
		refresh.setToolTipText("Ricarica");
		refresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshTabella();
			}
		});
	} 
	
	protected void aggiungiTastoDropDown(String testo, Menu menu) {
		dropDown = new ToolItem(toolbar, SWT.DROP_DOWN);
		dropDown.setText(testo);
		dropDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Rectangle rect = dropDown.getBounds ();
				Point pt = new Point (rect.x, rect.y + rect.height);
				pt = toolbar.toDisplay (pt);
				menu.setLocation (pt.x, pt.y);
				menu.setVisible (true);
			}
		});
	}
	
	@Override
	public void abilita(boolean abilita) {
		//Inutile andare avanti se la toolbar non è stata ancora costruita.
		//Eseguo i null check perchè potrebbero non essere usati come tasti.
		if (toolbarCostruita) {
			boolean permesso = isAbilitato();
			if (nuovo != null) 
				nuovo.setEnabled(abilita && permesso);
			if (modifica != null)
				modifica.setEnabled(abilita && permesso);
			if (dropDown != null)
				dropDown.setEnabled(abilita && permesso);
			//permesso a parte per la cancellazione
			boolean permessoDelete = isAbilitatoDelete();
			if (elimina != null)
				elimina.setEnabled(abilita && permessoDelete);
		}
	}
	
	protected void mostraMessaggioSelezione() {
		DialogMessaggio.openInformation("Selezione Necessaria", "\u00C8 necessario selezionare un elemento!");
	}

	protected abstract boolean isAbilitatoDelete();

}
