package com.dufler.swt.utils.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.dufler.swt.utils.decoration.Immagine;

/**
 * Classe estesa da dialog che servono a selezionare un'elemento
 * @author Damiano
 *
 * @param <T> Il tipo di elemento da selezionare.
 */
public abstract class DialogSelezione<T> extends DialogSemplice implements DialogApribile {
	
	public static final String OK_LABEL = "Ok";
	public static final String CANCEL_LABEL = "Annulla";
	
	protected T selectedValue;
	
	private Composite container;

	public DialogSelezione(String title) {
		super(title, Immagine.WIZARD_16X16.getImage(), true);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
				
		caricaModel();

		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, OK_LABEL, false);
		createButton(parent, IDialogConstants.CANCEL_ID, CANCEL_LABEL, true);
	}
	
	@Override
	protected void okPressed() {
		if (getSelezione() != null) {
			super.okPressed();
		}
	}
	
	@Override
	protected void checkElementiGrafici() {
		//DO NOTHING, di solito non serve.
	}
	
	/**
	 * Apre la dialog e restituisce il valore selezionato dall'utente.
	 * Viene restituito <code>null</code> se l'utente ha cliccato su <code>Annulla</code> o ha chiuso la Dialog.
	 * @return
	 */
	public T apri() {
		int returnCode = open();
		if (returnCode == Dialog.OK) {
			selectedValue = getSelezione();
		} else {
			selectedValue = null;
		}
		return selectedValue;
	}
	
	/**
	 * Carica le informazioni nella maschera.<br>
	 * Va sovrascritto dalle classi che hanno bisogno di questo comportamento.
	 */
	public void caricaModel() {}
	
	/**
	 * Recupera la selezione dell'utente.<br>
	 * Può essere sovrascritta per raffinare il comportamento.
	 * @return
	 */
	public T getSelezione() {
		return selectedValue;
	}

}
