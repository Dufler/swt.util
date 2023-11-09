package com.dufler.swt.utils.input;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class ListItems<T> extends List implements InputElement {

	private HashMap<String, T> values;
	
	private boolean required;
	
	public ListItems(Composite parent, int style) {
		super(parent, style);
		values = new HashMap<String, T>();
		required = false;
	}
	
	public void addItem(String key, T value) {
		values.put(key, value);
		add(key);
	}
	
	public void setItems(HashMap<String, T> newValues) {
		values = newValues;
		Set<String> keys = values.keySet();
		String[] items = keys.toArray(new String[0]);
		setItems(items);
	}
	
	public T getSelectedValue() {
		T value = null;
		int selectionIndex = getSelectionIndex();
		if (selectionIndex != -1) {
			String selectedValue = getItem(selectionIndex);
			value = values.get(selectedValue);
		}
		return value;
	}
	
	public void checkSubclass() {
		//DO NOTHING!
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void setDirty(boolean dirty) {
		//TODO
	}

	@Override
	public void resetValue() {
		// TODO Auto-generated method stub
		
	}

}
