package com.dufler.swt.utils.elements.tree;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * Classe provider di informazioni necessarie a renderizzare un TreeViewer.
 * @author Damiano Bellucci - damiano.bellucci@gmail.com
 *
 * @param <T> Il tipo gestito.
 */
//@SuppressWarnings("unchecked")
public abstract class AlberoProvider<T> implements ITreeContentProvider {
	
	@Override
	public Object[] getElements(Object inputElement) {
		Collection<?> elements = (Collection<?>) inputElement;
		return elements.toArray();
	}

//	@Override
//	public Object[] getElements(Object inputElement) {
//		return (T[]) inputElement ;
//	}
//
//	@Override
//	public Object[] getChildren(Object parentElement) {
//		T padre = (T) parentElement;
//		return getFigli(padre);
//	}
//	
//	/**
//	 * Metodo astratto da implementare che restituisce un array contenente i figli del nodo padre passato come argomento.
//	 * @param padre il nodo padre.
//	 * @return un array con i nodi figli del padre.
//	 */
//	public abstract T[] getFigli(T padre);
//
//	@Override
//	public Object getParent(Object element) {
//		T figlio = (T) element;
//		return getPadre(figlio);
//	}
//	
//	/**
//	 * Metodo astratto da implementare che restituisce il padre del nodo passato come argomento. 
//	 * @param figlio il nodo figlio.
//	 * @return il nodo padre.
//	 */
//	public abstract T getPadre(T figlio);
//
//	@Override
//	public boolean hasChildren(Object element) {
//		T elemento = (T) element;
//		return haFigli(elemento);
//	}
//	
//	/**
//	 * Metodo astratto da implementare che restiuisce true se il nodo passato come argomento ha almeno un figlio, false altrimenti.
//	 * @param elemento l'elemento di cui si vuol sapere se ha figli.
//	 * @return true se ha almeno un figlio, false altrimenti.
//	 */
//	public abstract boolean haFigli(T elemento);

}
