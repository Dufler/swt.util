package com.dufler.swt.utils.input;

import org.eclipse.swt.widgets.Composite;

import com.dufler.swt.utils.decoration.Decorator;

public class TextForMoney extends TextField<Double> {
	
	public static final String ERROR_MESSAGE = "Il valore inserito non \u00E8 corretto. Esempio valido: 12345.678";
	
	private static final String regex = "^\\d+((\\.|,)\\d{1,3})?";
	private static final String regex_5Decimali = "^\\d+((\\.|,)\\d{1,5})?";
	
	private Double value;

	public TextForMoney(Composite parent) {
		this(parent, false);
	}
	
	public TextForMoney(Composite parent, boolean precisioneEstrema) {
		super(parent, DEFAULT_STYLE, precisioneEstrema ? regex_5Decimali : regex, 13);
		setMessage("... \u20AC");
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
		return Decorator.getEuroValue3Decimals(v);
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
