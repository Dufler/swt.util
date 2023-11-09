package com.dufler.swt.utils.input;

import java.util.HashMap;

import org.eclipse.jface.viewers.ComboViewer;

public class ComboBoxViewer<T> extends ComboViewer {
	
	private HashMap<String, T> map;

	public ComboBoxViewer(ComboBox<T> list) {
		super(list);
		map = new HashMap<String, T>();
	}
	
	public T getData(String key) {
		return map.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public void setData(String key, Object value) {
		try {
			T valore = (T) value;
			map.put(key, valore);
		} catch(ClassCastException e) {
			//DO NOTHING!
		}
	}	

}
