package com.dufler.swt.utils.elements.table.template;

import org.eclipse.swt.graphics.Image;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.elements.Etichettatore;

public class TemplateEtichettatore extends Etichettatore<TemplateEntity> {

	public TemplateEtichettatore() {
		super();
	}

	@Override
	public String getTesto(TemplateEntity oggetto, int strategia) {
		String testo;
		switch (strategia) {
			case 0 : testo = oggetto.getFirstName(); break;
			case 1 : testo = oggetto.getLastName(); break;
			case 2 : testo = oggetto.getGender(); break;
			case 3 : testo = oggetto.isMarried() ? "Si" : "No"; break;
			default: testo = "";
		}
		return testo;
	}

	@Override
	public String getTestoTooltip(TemplateEntity oggetto, int colonna) {
		String tooltip = getTesto(oggetto, colonna);
		return tooltip;
	}

	@Override
	public Image getIcona(TemplateEntity oggetto, int colonna) {
		Image image = null;
		if (colonna == 3) {
			if (oggetto.isMarried())
				image = Immagine.SPUNTAVERDE_16X16.getImage();
			else
				image = Immagine.CROCEROSSA_16X16.getImage();
		}
		return image;
	}

}
