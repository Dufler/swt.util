package com.dufler.swt.utils.input;

import org.eclipse.swt.widgets.Composite;

import com.dufler.swt.utils.decoration.Decorator;

public class TextForPercentage extends TextField<Double> {
	
public static final String ERROR_MESSAGE = "Il valore inserito non \u00E8 corretto. Esempio valido: 99.99";
	
	private static final String regex = "^100|^100\\.0|^\\d{1,2}((\\.|,)\\d{1,3})?";
	
	private Double value;

	public TextForPercentage(Composite parent) {
		super(parent, DEFAULT_STYLE, regex, 6);
		setMessage("... \u0025");
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
		this.value = value;
		if (value != null) {
			String valore = value.toString();
			setText(valore);
		} else {
			setText("");
		}
		resetDirty();
	}
	
	public String getFormattedText() {
		Double v = getValue();
		if (v == null)
			v = 0.0;
		return Decorator.getPercentageValue(v);
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
