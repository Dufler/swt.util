package com.dufler.swt.utils.elements;

import org.eclipse.swt.widgets.Composite;

import com.dufler.swt.utils.elements.table.filter.CriteriFiltraggio;

/**
 * Tabella per gestire oggetti che possiede anche un filtro.
 * @author Damiano
 *
 * @param <T> Il tipo di oggetti da gestire
 * @param <U> L'oggetto che contiene i criteri di filtraggio, estende <code>CriteriFiltraggio</code>
 */
public abstract class TabellaCRUDConFiltro<T, C extends CriteriFiltraggio> extends TabellaCRUD<T, C> implements ITabella<T, C>, ITabellaCRUD<T> {
	
	public TabellaCRUDConFiltro(Composite parent) {
		this(parent, STILE_SEMPLICE, true, true);
	}
	
	public TabellaCRUDConFiltro(Composite parent, int style) {
		this(parent, style, true, true);
	}
	
	public TabellaCRUDConFiltro(Composite parent, int style, boolean cancellazionePossibile, boolean apriConDoppioClick) {
		super(parent, style, cancellazionePossibile, apriConDoppioClick);
	}

}
