package com.dufler.swt.utils.input;

import org.eclipse.swt.widgets.Composite;

public class TextForInteger extends TextField<Integer> {
	
	public static final String ERROR_MESSAGE = "Il valore inserito non \u00E8 corretto. Esempio valido: 12345";
	
	private static final String regexWithoutDecimal = "^\\d+";
	
	private Integer value;

	/**
	 * Costruttore di default.<br>
	 * La lunghezza massima dell'input è 11 caratteri.
	 * @param parent
	 */
	public TextForInteger(Composite parent) {
		super(parent, DEFAULT_STYLE, regexWithoutDecimal, 11);
	}

	@Override
	public Integer getValue() {
		try {
			String valore = getText();
			value = Integer.valueOf(valore);
		} catch (NumberFormatException e) {
			value = null;
		}
		return value;
	}

	@Override
	public void setValue(Integer value) {
		this.value = value;
		if (value != null) {
			String valore = value.toString();
			setText(valore);
		} else {
			setText("");
		}
		resetDirty();
	}
	
	@Override
	protected void setDefaultRequiredMessage() {
		requiredDecoration.setDescriptionText(TextField.DEAFULT_REQUIRED_MESSAGE);		
	}

	@Override
	protected void setDefaultErrorMessage() {
		wrongDecoration.setDescriptionText(ERROR_MESSAGE);
	}
	
}
