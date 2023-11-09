package com.dufler.swt.utils.decoration;

import org.eclipse.swt.SWT;

/**
 * Enum che elenca i colori SWT.
 * @author Damiano
 *
 */
public enum Colore {
	
	BIANCO(SWT.COLOR_WHITE),
	BLU(SWT.COLOR_BLUE),
	BLU_SCURO(SWT.COLOR_DARK_BLUE),
	CIANO(SWT.COLOR_CYAN),
	CIANO_SCURO(SWT.COLOR_DARK_CYAN),
	GIALLO(SWT.COLOR_YELLOW),
	GIALLO_SCURO(SWT.COLOR_DARK_YELLOW),
	GRIGIO(SWT.COLOR_GRAY),
	GRIGIO_SCURO(SWT.COLOR_DARK_GRAY),
	MAGENTA(SWT.COLOR_MAGENTA),
	MAGENTA_SCURO(SWT.COLOR_DARK_MAGENTA),
	NERO(SWT.COLOR_BLACK),
	ROSSO(SWT.COLOR_RED),
	ROSSO_SCURO(SWT.COLOR_DARK_RED),
	TRASPARENTE(SWT.COLOR_TRANSPARENT),
	VERDE(SWT.COLOR_GREEN),
	VERDE_SCURO(SWT.COLOR_DARK_GREEN);
	
	private final int codiceSWT;
	
	private Colore(int codiceSWT) {
		this.codiceSWT = codiceSWT;
		//int c = SWT.COLOR_
	}
	
	public int getCodiceSWT() {
		return codiceSWT;
	}

}
