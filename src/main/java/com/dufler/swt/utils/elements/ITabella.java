package com.dufler.swt.utils.elements;

import java.util.Collection;
import java.util.List;

import com.dufler.swt.utils.elements.table.filter.CriteriFiltraggio;

public interface ITabella<T, C extends CriteriFiltraggio> {
	
	public static final String DO_NOT_SHOW = " - ";
	
	void aggiornaContenuto();
	
	T getRigaSelezionata();
	
	List<T> getRigheSelezionate();
	
	List<T> getElementi();
	
	void setElementi(Collection<T> items);
	
	void setElementi(T[] items);
	
	void aggiustaLarghezzaColonne();
	
	void copiaSelezione();
	
	void filtra(C criteri);
	
	void annullaFiltro();

}
