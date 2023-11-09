package com.dufler.swt.utils.input;

import org.eclipse.swt.widgets.Composite;

/**
 * Questa classe viene usata per campi di testo che contengono descrizioni o note.<br>
 * Vengono impostatati automaticamente come non richiesti, multilinea e testo con "a capo" automatico.
 * @author Damiano Bellucci - damiano.bellucci@gmail.com
 *
 */
public class TextForDescription extends TextField<String> {
	
	/**
	 * Costruttore di default, imposta la lunghezza massima del campo a 250 caratteri.
	 * @param parent il composite padre.
	 */
	public TextForDescription(Composite parent) {
		this(parent, 250);
	}
	
	/**
	 * Costruttore che permette di specificare la lunghezza massima del contenuto del campo.
	 * @param parent il composite padre.
	 * @param maxLenght la lunghezza massima del campo.
	 */
	public TextForDescription(Composite parent, int maxLenght) {
		super(parent, DEFAULT_DESCRIPTION_STYLE, null, maxLenght);
		setRequired(false);
	}

	@Override
	public String getValue() {
		return getText();
	}

	@Override
	public void setValue(String value) {
		setText(value);
	}
	
	@Override
	public void setText(String value) {
		super.setText(value != null ? value : "");
		resetDirty();
	}

	@Override
	protected void setDefaultRequiredMessage() {
		//DO NOTHING! (non è mai obligatorio)
	}

	@Override
	protected void setDefaultErrorMessage() {
		//DO NOTHING! (Niente regex)
	}

}
