package com.dufler.swt.utils.input;

import org.eclipse.swt.widgets.Composite;

public class TextForDouble extends TextField<Double> {
	
	public static final String ERROR_MESSAGE = "Il valore inserito non \u00E8 corretto. Esempio valido: 12345.67";
	
	private static final String regexWithDecimal = "^\\d+((\\.|,)\\d{1,N})?";
	private static final String regexWithoutDecimal = "^\\d+";
	
	private Double value;
	private int maxDecimal;
	private double roundingDouble;

	public TextForDouble(Composite parent) {
		this(parent, 3);
	}
	
	public TextForDouble(Composite parent, int maxDecimal) {
		super(parent, DEFAULT_STYLE, null, null);
		setMaxDecimal(maxDecimal);
	}
	
	@Override
	public String getText() {
		String text = super.getText();
		return text;
	}

	@Override
	public Double getValue() {
		try {
			String valore = getText();
			valore = valore.replace(',', '.');
			value = Double.valueOf(valore);
		} catch (NumberFormatException e) {
			value = null;
		}
		return value;
	}

	@Override
	public void setValue(Double value) {
		if (value != null) {
			this.value = (double) Math.round(value * roundingDouble) / roundingDouble;
			String valore = this.value.toString();
			if (maxDecimal == 0) {
				int index = valore.indexOf('.');
				valore = valore.substring(0, index);
			}
			setText(valore);
		} else {
			this.value = null;
			setText("");
		}
		resetDirty();
	}
	
	public void setMaxDecimal(int maxDecimal) {
		this.maxDecimal = maxDecimal;
		roundingDouble = Math.pow(10, maxDecimal);
		if (maxDecimal == 0)
			setRegex(regexWithoutDecimal);
		else
			setRegex(regexWithDecimal.replace("N", Integer.toString(maxDecimal)));
		validate();
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
