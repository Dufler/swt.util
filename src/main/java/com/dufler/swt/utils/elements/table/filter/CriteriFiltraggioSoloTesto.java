package com.dufler.swt.utils.elements.table.filter;

public class CriteriFiltraggioSoloTesto extends CriteriFiltraggio {
	
	private final String testo;

	public CriteriFiltraggioSoloTesto(String testo) {
		this.testo = testo;
	}

	public String getTesto() {
		return testo;
	}

}
