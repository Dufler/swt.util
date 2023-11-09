package com.dufler.swt.utils.dialog;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Questa Dialog permette all'utente di selezionare una cartella nel suo computer.
 * L'utilizzo tipico è il seguente:
 * 
 * 	<code>
 * 	DialogSelezioneCartella dialog = new DialogSelezioneCartella();
 *	String path = dialog.open();
 *	if (path != null && !path.isEmpty()) {
 *		//Do Something
 *	}
 *	</code>
 * @author Damiano Bellucci - damiano.bellucci@gmail.com
 *
 */
public class DialogSelezioneCartella extends DirectoryDialog {

	public DialogSelezioneCartella() {
		super(Display.getDefault().getActiveShell());
	}
	
	@Override
	protected void checkSubclass() {
		//DO NOTHING!
	}

}
