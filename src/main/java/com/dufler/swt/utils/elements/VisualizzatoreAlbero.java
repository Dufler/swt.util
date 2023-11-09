package com.dufler.swt.utils.elements;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;

@SuppressWarnings("unchecked")
public abstract class VisualizzatoreAlbero<T> implements ITreeContentProvider {
	
	@Override
	public Object[] getElements(Object inputElement) {
		Object[] result;
		if (inputElement == null) {
			result = null;
		} else if (inputElement instanceof List) {
			result = ((List<Object>) inputElement).toArray();
		} else if (inputElement.getClass().isArray()) {
			result = (Object[]) inputElement;
		} else {
			result = null;
		}
		return result;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		Object[] figli = parentElement != null ? getFigli((T) parentElement) : null;
		return figli;
	}
	
	protected abstract Object[] getFigli(T parent);

	@Override
	public Object getParent(Object element) {
		T parent = element != null ? getPadre((T) element) : null;
		return parent;
	}
	
	protected abstract T getPadre(T child);

	@Override
	public boolean hasChildren(Object element) {
		boolean figli = element != null ? haFigli((T) element) : null;
		return figli;
	}
	
	protected abstract boolean haFigli(T elemento);

}
