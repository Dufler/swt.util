package com.dufler.swt.utils.decoration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Classe che implementa un label vuoto atto ad ospitare icone di validazione o similari per avere spazio vuoto vicino a componenti.
 * @author Damiano Bellucci - damiano.bellucci@gmail.com
 *
 */
public class SpacerLabel extends Label {
	
	public static final String SHORT = " ";
	public static final String MEDIUM = "	";
	public static final String LARGE = "		";
	
	/**
	 * Costruttore semplice da usare per dare lo spazio agli elementi di validazione.
	 * @param parent
	 */
	public SpacerLabel(Composite parent) {
		this(parent, SWT.NONE, SHORT);
	}
	
	/**
	 * Costruttore che permette di specificare la stringa da mostrare.<br>
	 * @param parent
	 * @param spacing
	 */
	public SpacerLabel(Composite parent, String spacing) {
		this(parent, SWT.NONE, spacing);
	}

	/**
	 * Costruttore che permette di specificare stili SWT da applicare e la stringa da mostrare.<br>
	 * Usualmente vengono usati spazi, la visibilità è impostata a <code>false</code>.
	 * @param parent
	 * @param style
	 * @param spacing
	 */
	public SpacerLabel(Composite parent, int style, String spacing) {
		super(parent, style);
		setText(spacing);
		setVisible(false);
	}
	
	public void checkSubclass() {
		//DO NOTHING!
	}

}
