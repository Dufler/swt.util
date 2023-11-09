package com.dufler.swt.utils.elements;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.widgets.Composite;

import com.dufler.swt.utils.elements.table.filter.CriteriFiltraggio;
import com.dufler.swt.utils.elements.table.filter.FiltroTabella;

/**
 * Tabella con checkbox per gestire oggetti che possiede anche un filtro.
 * @author Damiano
 *
 * @param <T> Il tipo di oggetti da gestire
 * @param <U> L'oggetto che contiene i criteri di filtraggio, estende <code>CriteriFiltraggio</code>
 */
public abstract class TabellaCheckBoxConFiltro<T, C extends CriteriFiltraggio> extends TabellaCheckBox<T, C> implements ITabella<T, C>, ITabellaCRUD<T> {
	
	protected FiltroTabella<T, C> filter;
	protected C criteri;
	
	public TabellaCheckBoxConFiltro(Composite parent, int style) {
		super(parent, style);
	}

	public TabellaCheckBoxConFiltro(Composite parent) {
		super(parent);
	}

	/**
	 * Restituisce il filtro della tabella.
	 * @return
	 */
	public FiltroTabella<T, C> getFiltro() {
		return filter;
	}

	/**
	 * Nel corpo di questo metodo va implementato il filtraggio degli elementi in
	 * tabella. Usualmente viene abbinato al viewer un oggetto Vengono esclusi dal
	 * filtro gli elementi selezionati tramite check che rimarranno sempre
	 * selezionati <code>ViewerFilter</code>
	 */
	@Override
	protected final void setFiltro() {
		filter = creaFiltro();
		if (filter != null) {
			addCheckStateListener(new ICheckStateListener() {
				@SuppressWarnings("unchecked")
				@Override
				public void checkStateChanged(CheckStateChangedEvent event) {
					T el = (T) event.getElement();
					if (event.getChecked()) {
						filter.exclude(el);
					} else {
						filter.include(el);
					}
				}
			});
			addFilter(filter);
		}
	}
	
	/**
	 * Metodo astratto che deve essere implementato per restituire l'oggetto responsabile del filtraggio della tabella.
	 * @return
	 */
	protected abstract FiltroTabella<T, C> creaFiltro();
	
	/**
	 * Passa i criteri al filtro e aggiorna gli elementi mostrati nella tabella.
	 * @param criteri
	 */
	@Override
	public void filtra(C criteri) {
		if (filter != null) {
			filter.setCriteri(criteri);
			refresh();
			aggiustaLarghezzaColonne();
		}
	}
	
	/**
	 * Annnulla i criteri di filtraggio precedentemente impostati.
	 */
	@Override
	public void annullaFiltro() {
		if (filter != null) {
			filter.resetCriteri();
			refresh();
			aggiustaLarghezzaColonne();
		}
	}

}
