package com.dufler.swt.utils.elements;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Etichettatore per le tabelle.
 * @author Damiano Bellucci - damiano.bellucci@gmail.com
 *
 * @param <T>
 */
public abstract class Etichettatore<T> extends /*ColumnLabelProvider*/ StyledCellLabelProvider implements IColorProvider {
	
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
	
	protected static final int DEFAULT_BACKGROUND_COLOR = SWT.COLOR_LIST_BACKGROUND;
	protected static final int DEFAULT_FOREGROUND_COLOR = SWT.COLOR_BLACK;

	protected int index;
	
	protected final SimpleDateFormat sdf;
	protected final DecimalFormat formatInteri;
	protected final DecimalFormat formatEuro;
	protected final DecimalFormat formatDecimali;
	protected final DecimalFormat formatPercentuali;
	protected final DecimalFormat formatPesi;
	protected final DecimalFormat formatVolumi;
	
	/**
	 * Costruttore di default che usa i formati standard per rappresentare date, valori in euro e percentuali.
	 */
	public Etichettatore() {
		this(DEFAULT_DATE_FORMAT, DEFAULT_EURO_FORMAT, DEFAULT_DECIMAL_FORMAT, DEFAULT_PERCENTAGE_FORMAT);
	}
	
	/**
	 * Costruttore che permette di specificare i pattern da usare per rappresentare date, valori in euro e percentuali.
	 * @param dateFormat il pattern da applicare alle date.
	 * @param euroFormat il pattern da applicare ai valori in euro.
	 * @param percentageFormat il pattern da applicare ai valori percentuali.
	 */
	public Etichettatore(String dateFormat, String euroFormat, String decimalFormat, String percentageFormat) {
		sdf = new SimpleDateFormat(dateFormat);
		formatInteri = new DecimalFormat(DEFAULT_INTEGER_FORMAT);
		formatEuro = new DecimalFormat(euroFormat);
		formatDecimali = new DecimalFormat(decimalFormat);
		formatPercentuali = new DecimalFormat(percentageFormat);
		formatPesi = new DecimalFormat(DEFAULT_PESO_FORMAT);
		formatVolumi = new DecimalFormat(DEFAULT_VOLUME_FORMAT);
	}
	
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		cell.setText(getText(element));
		Image image = getImage(element);
		cell.setImage(image);
		cell.setBackground(getBackground(element));
		cell.setForeground(getForeground(element));
		//cell.setFont(getFont(element));
	}
	
	protected void setColumnIndex(int columnIndex) {
		index = columnIndex;
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public Color getBackground(Object element) {
		T oggetto = (T) element;
    	int colorCode = getCodiceColoreSfondo(oggetto);
    	Color color = Display.getCurrent().getSystemColor(colorCode);
    	return color;
    }

	@SuppressWarnings("unchecked")
    @Override
    public Color getForeground(Object element) {
    	T oggetto = (T) element;
    	int colorCode = getCodiceColoreScritte(oggetto);
    	Color color = Display.getCurrent().getSystemColor(colorCode);
    	return color;
    }
	
	@SuppressWarnings("unchecked")
	//@Override
	public String getText(Object element) {
		T oggetto = (T) element;
		String testo = getTesto(oggetto, index);
		return testo;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getToolTipText(Object element) {
		T oggetto = (T) element;
		String testo = getTestoTooltip(oggetto, index);
		return testo;
	}
	
	@SuppressWarnings("unchecked")
	//@Override
	public Image getImage(Object element) {
		T oggetto = (T) element;
		Image icona = getIcona(oggetto, index);
		return icona;
	}
	
	/**
	 * Metodo da estendere se si desidera colorare la scritta nelle celle in base al contenuto.
	 * @param oggetto l'oggetto rappresentato dalla riga nella tabella.
	 * @return l'intero che rappresenta il colore come codificato dentro SWT.
	 */
	public int getCodiceColoreScritte(T oggetto) {
		return DEFAULT_FOREGROUND_COLOR;
	}
	
	/**
	 * Metodo da estendere se si desidera colorare la sfondo delle celle in base al contenuto.
	 * @param oggetto l'oggetto rappresentato dalla riga nella tabella.
	 * @return l'intero che rappresenta il colore come codificato dentro SWT.
	 */
	public int getCodiceColoreSfondo(T oggetto) {
		return DEFAULT_BACKGROUND_COLOR;
	}
	
	public abstract String getTesto(T oggetto, int colonna);
	
	public abstract String getTestoTooltip(T oggetto, int colonna);
	
	public abstract Image getIcona(T oggetto, int colonna);
	
	public int getToolTipTimeDisplayed(Object object) {
		return 10000;
	}
	
	public int getToolTipDisplayDelayTime(Object object) {
		return 450;
	}
}
