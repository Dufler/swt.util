package com.dufler.swt.utils.elements.tree;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.dufler.swt.utils.elements.ITabella;
import com.dufler.swt.utils.elements.Ordinatore;

public class ListenerColonnaTree<T> extends SelectionAdapter {
	
	private final int index;
	private final TreeColumn column;
	private final Ordinatore<T> sorter;
	private final Tree tree;
	private final ITabella<T, ?> viewer;
	
	public ListenerColonnaTree(int indiceColonna, TreeColumn colonna, Tree albero, Ordinatore<T> ordinatore, ITabella<T, ?> visionatore) {
		index = indiceColonna;
		column = colonna;
		tree = albero;
		sorter = ordinatore;
		viewer = visionatore;
	}
	
	@Override
    public void widgetSelected(SelectionEvent e) {
		sorter.setColumn(index);
		int direction = sorter.getDirection();
		tree.setSortDirection(direction);
		tree.setSortColumn(column);
		viewer.aggiustaLarghezzaColonne();
	}

}
