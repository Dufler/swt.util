package com.dufler.swt.utils.dialog;

import org.eclipse.swt.SWT;

/**
 * Dialog che permette di selezionare e aprire un file.<br>
 * E' possibile passare una lista di estensioni da usare come filtro.
 * @author Damiano
 *
 */
public class DialogAperturaFile extends DialogSelezioneFile {

	public DialogAperturaFile(Extension... extensions) {
		super(SWT.OPEN, extensions);
	}

}
