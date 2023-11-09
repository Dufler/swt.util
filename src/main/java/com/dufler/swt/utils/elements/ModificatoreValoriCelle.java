package com.dufler.swt.utils.elements;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;

/**
 * Classe che permette di modificare con semplicità i valori degli elementi all'interno di una tabella.
 * @author Damiano
 *
 * @param <T> Il tipo degli oggetti gestiti dalla tabella.
 */
public abstract class ModificatoreValoriCelle<T> extends EditingSupport {
	
	protected final Tabella<T, ?> tabella;
	protected final CellEditor editor;
	
	protected int index;
	
	protected boolean dirty;

	public ModificatoreValoriCelle(Tabella<T, ?> tabella) {
		super(tabella);
		
		this.tabella = tabella;
		this.editor = new TextCellEditor(tabella.getTable());
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	protected void setColumnIndex(int columnIndex) {
		index = columnIndex;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object getValue(Object element) {
		Object value;
		if (element != null) {
			T elemento = (T) element;
			value = getValore(elemento, index);
		} else {
			value = null;
		}
		return value;
	}
	
	protected abstract Object getValore(T elemento, int indiceColonna);

	@SuppressWarnings("unchecked")
	@Override
	protected void setValue(Object element, Object value) {
		if (element != null && value != null) {
			T elemento = (T) element;
			boolean update = setValore(elemento, value, index);
			if (update) {
				tabella.refresh();
				dirty = true;
			}
		}
	}
	
	@Override
	protected void initializeCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
		Object value = getValue(cell.getElement());
		if (value != null)
			cellEditor.setValue(value);
	}
	
	protected abstract boolean setValore(T elemento, Object valore, int indiceColonna);

}
