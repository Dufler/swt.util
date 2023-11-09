package com.dufler.swt.utils.elements.tree;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

/**
 * Classe astratta da implementare per lo specifico albero che gestisce i tipi T.
 * @author Damiano Bellucci - damiano.bellucci@gmail.com
 *
 * @param <T> il tipo da gestire.
 */
public abstract class AlberoEtichettatore extends LabelProvider implements IStyledLabelProvider {
	
	protected static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	protected static final String HIGH_PRECISION_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	
	protected static final String DEFAULT_EURO_FORMAT = "#,##0.000 \u20AC";
	protected static final String HIGH_PRECISION_EURO_FORMAT = "#,##0.00000 \u20AC";
	
	protected static final String DEFAULT_INTEGER_FORMAT = "#";
	
	protected static final String DEFAULT_DECIMAL_FORMAT = "#,##0.0";
	protected static final String HIGH_PRECISION_DECIMAL_FORMAT = "#,##0.000";
	
	protected static final String DEFAULT_PESO_FORMAT = "#,##0.0 Kg";
	protected static final String DEFAULT_VOLUME_FORMAT = "#,##0.000 mc";
	
	protected static final String DEFAULT_PERCENTAGE_FORMAT = "### \u0025";
	protected static final String HIGH_PRECISION_PERCENTAGE_FORMAT = "##0.00 \u0025";
	
	private final int columnIndex;
	
	protected final SimpleDateFormat sdf;
	protected final DecimalFormat formatInteri;
	protected final DecimalFormat formatEuro;
	protected final DecimalFormat formatDecimali;
	protected final DecimalFormat formatPercentuali;
	protected final DecimalFormat formatPesi;
	protected final DecimalFormat formatVolumi;
	
	public AlberoEtichettatore(int columnIndex) {
		this(columnIndex, DEFAULT_DATE_FORMAT, DEFAULT_EURO_FORMAT, DEFAULT_DECIMAL_FORMAT, DEFAULT_PERCENTAGE_FORMAT);
	}
	
	public AlberoEtichettatore(int columnIndex, String dateFormat, String euroFormat, String decimalFormat, String percentageFormat) {
		this.columnIndex = columnIndex;
		
		sdf = new SimpleDateFormat(dateFormat);
		formatInteri = new DecimalFormat(DEFAULT_INTEGER_FORMAT);
		formatEuro = new DecimalFormat(euroFormat);
		formatDecimali = new DecimalFormat(decimalFormat);
		formatPercentuali = new DecimalFormat(percentageFormat);
		formatPesi = new DecimalFormat(DEFAULT_PESO_FORMAT);
		formatVolumi = new DecimalFormat(DEFAULT_VOLUME_FORMAT);
	}

	@Override
	public StyledString getStyledText(Object element) {
		StyledString testoConStile = new StyledString();
		String testo =  getTestoConStile(element, columnIndex);
		testoConStile.append(testo != null ? testo : "");
		return testoConStile;
	}
	
	/**
	 * Metodo astratto da implementare che restituisce il testo che descrive la proprieta' dell'oggetto nella specifica colonna.
	 * @param oggetto l'oggetto da descrivere.
	 * @param indiceColonna la colonna da riempire.
	 * @return una stringa con stile.
	 */
	protected abstract String getTestoConStile(Object oggetto, int indiceColonna);
	
	@Override
    public Image getImage(Object element) {
		return getImmagine(element, columnIndex);
	}
	
	/**
	 * Metodo astratto da implementare che restituisce l'immagine da piazzare nella cella individuata dell'oggetto e dalla colonna. 
	 * @param oggetto l'oggetto da descrivere.
	 * @param indiceColonna la colonna da riempire.
	 * @return un immagine che verrà posizionata nella cella.
	 */
	protected abstract Image getImmagine(Object oggetto, int indiceColonna);

}
