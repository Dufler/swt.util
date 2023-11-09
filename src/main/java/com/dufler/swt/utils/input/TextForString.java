package com.dufler.swt.utils.input;

import org.eclipse.swt.widgets.Composite;

public class TextForString extends TextField<String> {
	
	public static final String REGEX_CAP = "^\\d{5}";
	public static final String REGEX_PIVA = "^\\d{11}";
	public static final String REGEX_CF = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]";
	public static final String REGEX_PIVA_CF = "^(\\d{11}|[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z])";
	public static final String REGEX_TELEFONO = "^\\d+((\\.|-|\\s+)\\d+)*";
	public static final String REGEX_EMAIL = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?.)+[\\w](?:[\\w-]*[\\w])?";
	public static final String REGEX_BARCODE = "\\w{3,35}";
	public static final String REGEX_COLLO = "^\\d{9}";
	
	public TextForString(Composite parent) {
		this(parent, DEFAULT_STYLE, null, null);
	}

	/**
	 * Costruisce la textfield specificando una regex con cui validare il contenuto.
	 * @param parent il contenitore padre della textfield
	 * @param style lo stile da applicarvi (SWT.MULTI per multilinea)
	 * @param regex la regex con cui validare il contenuto immesso dall'utente.
	 */
	public TextForString(Composite parent, String regex) {
		this(parent, DEFAULT_STYLE, regex, null);
	}
	
	/**
	 * Costruisce la textfield specificando la lunghezza massima contenuto.
	 * @param parent il contenitore padre della textfield
	 * @param style lo stile da applicarvi (SWT.MULTI per multilinea)
	 * @param lunghezzaMassima la lunghezza massima per il contenuto immesso dall'utente.
	 */
	public TextForString(Composite parent, int lunghezzaMassima) {
		this(parent, DEFAULT_STYLE, null, lunghezzaMassima);
	}
	
	/**
	 * Costruttore che permette di specificare tutti i parametri possibili.
	 * @param parent
	 * @param style
	 * @param regex
	 * @param lunghezzaMassima
	 */
	public TextForString(Composite parent, int style, String regex, Integer lunghezzaMassima) {
		super(parent, style, regex, lunghezzaMassima);
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
		requiredDecoration.setDescriptionText(TextField.DEAFULT_REQUIRED_MESSAGE);		
	}

	@Override
	protected void setDefaultErrorMessage() {
		wrongDecoration.setDescriptionText(TextField.DEFAULT_ERROR_MESSAGE); //TODO - Cambiare il messaggio in base al tipo di regex scelta.
	}
	
}
