package com.dufler.swt.utils.dialog;

import org.eclipse.swt.SWT;

/**
 * Dialog che permette di salvare un file.<br>
 * E' possibile passare una lista di estensioni da usare come filtro.
 * @author Damiano
 *
 */
public class DialogSalvataggioFile extends DialogSelezioneFile {

	public DialogSalvataggioFile(Extension... extensions) {
		super(SWT.SAVE, extensions);
		setOverwrite(true); //chiede conferma per sovrascrivere.
		//setFilterPath("C:\\");
	}

}
