package com.dufler.swt.utils.dialog;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

/**
 * Questa Dialog permette all'utente di selezionare un file nel suo computer.
 * L'utilizzo tipico è il seguente:
 * 
 * 	<code>
 * 	DialogSelezioneFile dialog = new DialogSelezioneFile();
 *	String path = dialog.open();
 *	if (path != null && !path.isEmpty()) {
 *		//Do Something
 *	}
 *	</code>
 * @author Damiano Bellucci - damiano.bellucci@gmail.com
 *
 */
public abstract class DialogSelezioneFile extends FileDialog {
	
	public enum Extension {
		
		PDF("*.pdf", "pdf"),
		CSV("*.csv", "csv"),
		XML("*.xml", "xml"),
		XLS("*.xls", "Excel legacy"),
		XLSX("*.xlsx", "Excel");
		
		private final String extension;
		private final String name;
		
		private Extension(String extension, String name) {
			this.extension = extension;
			this.name = name;
		}
		
		public String getExtension() {
			return extension;
		}
		
		public String getName() {
			return name;
		}
		
	}

	public DialogSelezioneFile(int style) {
		this(style, new Extension[0]);
	}
	
	public DialogSelezioneFile(int style, Extension... extensions) {
		super(Display.getDefault().getActiveShell(), style);
		setFileExtension(extensions);
	}
	
	protected void setFileExtension(Extension[] extension) {
		//Se hanno specificato degli elementi per il filtro procedo ad impostarli
		if (extension != null && extension.length > 0) {
			//Istanzio gli array
			String[] filterExtension = new String[extension.length];
			String[] filterNames = new String[extension.length];		
			//Valorizzo i filtri delle estensioni
			for (int index = 0; index < extension.length; index++) {
				filterExtension[index] = extension[index].getExtension();
				filterNames[index] = extension[index].getName();
			}
			//Imposto i valori nella dialog
			setFilterExtensions(filterExtension);
			setFilterNames(filterNames);
		}		
	}
	
	@Override
	protected void checkSubclass() {
		//DO NOTHING!
	}

}
