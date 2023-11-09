package com.dufler.swt.utils.validation;

/**
 * Applicazione parziale del pattern "Chain of responsability"<br>
 * Il gruppo di elementi di input si valida in autonomia dopo l'immissione di dati da parte dell'utente e richiama il metodo passando l'esito della propria validazione.<br>
 * Gli elementi che implementano questa interfaccia raffinano questo metodo.<br>
 * Es. dialog di modifica abilitano/disabilitano il bottone "Ok"
 * Es. le wizard page richiamano il metodo setPageComplete(boolean complete) 
 * @author Damiano
 *
 */
public interface ValidationHandler {
	
	/**
	 * Specifica chi è il padre del componente.
	 * Il padre dovrà essere avvisato in fase di validazione attraverso il metodo forwardValidation()
	 * @param parent
	 */
	public void setParent(ParentValidationHandler parent);
	
	/**
	 * Restituisce lo stato di validazione del componente.
	 * @return
	 */
	public boolean isValid();
	
	/**
	 * Scatena la validazione e restituisce l'esito.
	 * L'implementazione di questo metodo potrebbe essere:
	 * <code>
	 * - eseguo i controlli
	 * boolean valid = ...
	 * forwardValidation();
	 * return valid;
	 * </code>
	 * @return l'esito della validazione
	 */
	public boolean validate();
	
	/**
	 * Reinvia la richiesta di validazione al suo superiore passando la propria parte di risultato.
	 * L'implementazione di questo metodo potrebbe essere:
	 * <code>
	 * if (successor != null) {
	 * 	successor.validate();
	 * }
	 * </code>
	 * @param valid
	 */
	public void forwardValidation();

}
