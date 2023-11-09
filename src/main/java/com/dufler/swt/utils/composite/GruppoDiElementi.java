package com.dufler.swt.utils.composite;

import org.eclipse.swt.widgets.Control;

public interface GruppoDiElementi {
	
	/**
	 * Indica se il gruppo di elementi è stato compilato dall'utente.
	 * @return
	 */
	public boolean isDirty();
	
	/**
	 * Indica se il gruppo di elementi è stato compilato dall'utente.
	 * @return
	 */
	public boolean isRequired();
	
	/**
	 * Aggiunge un elemento che non può essere modificato.
	 * @param control
	 */
	public void addNonUpdatableElement(Control control);
	
	/**
	 * Imposta come disabilitati tutti i controlli non aggiornabili.
	 */
	public void lockNonUpdatableElements();
	
	/**
	 * Resetta tutti gli elementi di input.
	 */
	public void resetValues();
	
	/**
	 * Metodo ridichiarato per poterlo usare senza cast espliciti.
	 * @param enabled
	 */
	public void setEnabled(boolean enabled);

}
