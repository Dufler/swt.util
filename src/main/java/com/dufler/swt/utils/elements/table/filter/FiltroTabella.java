package com.dufler.swt.utils.elements.table.filter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public abstract class FiltroTabella<T, U extends CriteriFiltraggio> extends ViewerFilter {
	
	private Set<T> toExclude = new HashSet<T>();
	protected U criteri;

	@SuppressWarnings("unchecked")
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		T item = (T) element;
		boolean select = criteri != null ? checkElemento(item) : true;
		if(toExclude.contains(item)) {
			select = true;
		}
		return select;
	}
	
	public U getCriteri() {
		return criteri;
	}

	public void setCriteri(U criteri) {
		this.criteri = criteri;
	}

	public final void resetCriteri() {
		criteri = null;
	}

	protected abstract boolean checkElemento(T item);
	
	public void exclude(T item) {
		toExclude.add(item);
	}	
	
	public void include(T item) {
		toExclude.remove(item);
	}
	
	protected boolean checkStringValue(String criteri, String value) {
		criteri = criteri != null ? criteri.toUpperCase() : "";
		value = value != null ? value.toUpperCase() : "";
		return value.contains(criteri);
	}
	
	protected boolean checkIntValue(Integer criteri, Integer value) {
		boolean check;
		if (criteri == null) {
			check = true;
		} else if (value == null) {
			check = false;
		} else {
			check = criteri == value;
		}
		return check;
	}
	
	protected boolean checkDateAfter(Date date, Date start) {
		boolean check;
		if (start == null) {
			check = true;
		} else if (date == null) {
			check = false;
		} else {
			check = date.after(start);
		}
		return check;
	}
	
	protected boolean checkDateBefore(Date date, Date end) {
		boolean check;
		if (end == null) {
			check = true;
		} else if (date == null) {
			check = false;
		} else {
			check = date.before(end);
		}
		return check;
	}
	
	protected boolean checkDateBetween(Date date, Date start, Date end) {
		boolean after = checkDateAfter(date, start);
		boolean before = checkDateBefore(date, end);
		return after && before;
	}

}
