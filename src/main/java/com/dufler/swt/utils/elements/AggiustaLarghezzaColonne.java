package com.dufler.swt.utils.elements;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Runnable che aggiusta la larghezza delle colonne.
 * @author Damiano
 *
 */
public class AggiustaLarghezzaColonne implements Runnable {
	
	private final Table table;
	
	public AggiustaLarghezzaColonne(Table table) {
		this.table = table;
	}

	@Override
	public void run() {
		for (TableColumn column : table.getColumns()) {
			// Controllo sul tipo di colonna, se è stata marchiata come colonna vuota non
			// vado a ridimensionarla.
			if (column.getToolTipText() != null && column.getToolTipText().equals(ITabella.DO_NOT_SHOW))
				continue;
			column.pack();
		}
	}

}
