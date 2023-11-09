package com.dufler.swt.utils.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.graphics.Image;

import com.dufler.swt.utils.composite.GruppoDiElementi;
import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.validation.ValidationHandler;

/**
 * Questa classe viene estesa da tutte le Dialog che devono inserire o modificare entities.
 * La classe prevede che debbano essere implementati gli specifici metodi per la validazione di tali entities.
 * @author Damiano
 *
 * @param <T> Il tipo d'oggetto che deve essere manipolato
 */
public abstract class DialogModel<T> extends DialogSempliceConValidazione {
	
	public static final String TITLE_ERROR_IN_MODEL_VALIDATION = "Errore nella validazione";
	
	/**
	 * Indica se la dialog viene aperta in modalità "modifica" oppure "nuovo inserimento".
	 */
	protected final boolean modify;
	
	/**
	 * Il valore del tipo che questa dialog gestisce.<br>
	 * Se viene passato un valore non nullo al costruttore si andrà in modifica sull'elemento altrimenti verrà creato un nuovo oggetto.
	 */
	protected final T valore;
	
	/**
	 * Instanzia una dialog.
	 * Se lo specifico oggetto passato è <code>null</code> allora la dialog si apre in modalità inserimento 
	 * altrimenti in modifica preimpostando i valori contenuti nell'oggetto dentro i campi.
	 * @param title Il titolo della dialog.
	 * @param value Il model da mostrare, null se si vuole creare un elemento nuovo.
	 */
	public DialogModel(String title, T value) {
		this(title, Immagine.IMPOSTAZIONI_16X16.getImage(), value, true);
	}
	
	/**
	 * Instanzia una dialog.
	 * Se lo specifico oggetto passato è <code>null</code> allora la dialog si apre in modalità inserimento 
	 * altrimenti in modifica preimpostando i valori contenuti nell'oggetto dentro i campi.
	 * E' possibile andare a specificare un'immagine per l'icona in alto a sinistra.
	 * @param title Il titolo della dialog.
	 * @param image L'icona da mostrare accanto al titolo.
	 * @param value Il model da mostrare, null se si vuole creare un elemento nuovo.
	 */
	public DialogModel(String title, Image image, T value, boolean abilitaComponenti) {
		super(title, image, abilitaComponenti);		
		if (value == null) {
			modify = false;
			valore = createNewModel();
		} else {
			modify = true;
			valore = value;
		}
	}
	
	@Override
	protected void checkElementiGrafici() {
		if (modify) {
			loadModel();
			lockNonUpdatableElements();
		} else {
			prefillModel();
		}
	}
	
	protected void lockNonUpdatableElements() {
		for (ValidationHandler child : children) {
			if (child instanceof GruppoDiElementi) {
				GruppoDiElementi gruppo = (GruppoDiElementi) child;
				gruppo.lockNonUpdatableElements();
			}
		}
	}
	
	@Override
	protected boolean validation() {
		validate();
		if (valid && abilitaComponenti) {
			copyDataToModel();
			List<String> errors = validateModel();
			if (errors == null || errors.isEmpty()) {
				if (modify)
					valid = updateModel();
				else
					valid = insertModel();
			} else {
				valid = false;
				showValidationError(errors);
			}
		}
		return valid;
	}
	
	/**
	 * Carica i dati contenuti nel model passato al costruttore
	 */
	public abstract void loadModel();
	
	/**
	 * Carica i dati da impostare come default sul nuovo model.
	 */
	public abstract void prefillModel();
	
	/**
	 * Risporta le modifiche fatte dall'utente sull'UI al model.
	 */
	public abstract void copyDataToModel();
	
	/**
	 * Valida i model e restituisce una lista di stringhe, eventualmente vuota, contenente gli errori riscontrati.
	 * @return la lista di stringhe con gli errori.
	 */
	public abstract List<String> validateModel();
	
	/**
	 * Valida il model come <code>ValidEntity</code> e lo aggiorna sul DB.
	 * @return true se è stato aggiornato, false altrimenti.
	 */
	public abstract boolean updateModel();
	
	/**
	 * Valida il model come <code>ValidEntity</code> e lo inserisce sul DB.
	 * @return true se è stato inserito, false altrimenti.
	 */
	public abstract boolean insertModel();
	
	/**
	 * Crea un nuovo model e lo restituisce.
	 * @return una nuova instanza, solitamente vuota.
	 */
	public abstract T createNewModel();
	
	
	public T getValore() {
		return valore;
	}
	
	public T apri() {
		T selectedValue = null;
		validate();
		int returnCode = open();
		if (returnCode == Dialog.OK) 
			selectedValue = getValore();
		return selectedValue;
	}
	
	protected void showValidationError(List<String> errors) {
		StringBuilder errorMessage = new StringBuilder("Errori riscontrati nella validazione dei dati:");
		for (String error : errors) {
			errorMessage.append("\r\n");
			errorMessage.append(error);
		}
		mostraErrore(TITLE_ERROR_IN_MODEL_VALIDATION, errorMessage.toString());
	}

}
