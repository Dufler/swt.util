package com.dufler.swt.utils.input;

/**
 * Questa interfaccia rappresenta un elemento di input per l'utente (es. campo di testo, combo...)
 * @author Damiano
 *
 */
public interface InputElement {
	
	/**
	 * Questo metodo restituisce lo stato di validazione dell'oggetto.
	 * @return lo stato di validazione.
	 */
	public boolean isValid();
	
	/**
	 * Questo metodo specifica se l'element di input è richiesto per la validazione oppure se è opzionale.
	 * @return 
	 */
	public boolean isRequired();
	
	/**
	 * Questo metodo permette di specificare se l'elemento di input è opzionale o meno
	 * @param required
	 */
	public void setRequired(boolean required);
	
	/**
	 * Questo metodo permette di capire se l'utente ha interagito con l'elemento di input.
	 * @return
	 */
	public boolean isDirty();
	
	/**
	 * Questo metodo permette di impostare l'elemento di input come "sporco, già usato" anche se l'utente potrebbe non avervi interagito.
	 * Va usato con estrema cautela.
	 */
	public void setDirty(boolean dirty);
	
	/**
	 * Permette di ripristinare lo stato dell'elemento di input al valore di default, anche dirty viene annullato.
	 */
	public void resetValue();
	
	/**
	 * Metodo ridichiarato per poterlo usare senza cast espliciti.
	 * @param enabled
	 */
	public void setEnabled(boolean enabled);

}
