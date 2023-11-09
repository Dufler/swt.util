package com.dufler.swt.utils.validation;

public interface ParentValidationHandler extends ValidationHandler {
	
	/**
	 * Aggiunge alla validazione un componente figlio.
	 * @param child
	 */
	public void addChild(ValidationHandler child);
	
	/**
	 * Rimuove dalla validazione un componente figlio.
	 * @param child
	 */
	public void removeChild(ValidationHandler child);

}
