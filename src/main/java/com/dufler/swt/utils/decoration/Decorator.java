package com.dufler.swt.utils.decoration;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Decorator {
	
//	public static final String LISTINO_16X16 = "/it/ltc/logica/gui/resources/listino_16x16.png";
//	public static final String IMPOSTAZIONI_16X16 = "/it/ltc/logica/gui/resources/impostazioni_16x16.png";
//	public static final String CALCOLO_16X16 = "/it/ltc/logica/gui/resources/calcolo_16x16.png";
//	public static final String WIZARD_16X16 = "/it/ltc/logica/gui/resources/wizard_16x16.png";
//	public static final String TRASPORTI_16X16 = "/it/ltc/logica/gui/resources/trasporti_16x16.png";
//	
//	public static final String RED_MARK_12X20 = "/it/ltc/logica/gui/resources/red_mark_12x20.png";
//	public static final String ORANGE_MARK_12X20 = "/it/ltc/logica/gui/resources/orange_mark_12x20.png";
//	public static final String YELLOW_MARK_12X20 = "/it/ltc/logica/gui/resources/yellow_mark_12x20.png";
//	
//	public static final String LOADING_256X256 = "/it/ltc/logica/gui/resources/loading_256x256.png";
//	
//	private static final HashMap<String, Image> immaginiCaricate = new HashMap<String, Image>();
	
	private static DecimalFormat dfInteri;
	private static DecimalFormat dfDecimali;
	private static DecimalFormat dfEuro2Decimali;
	private static DecimalFormat dfEuro3Decimali;
	private static DecimalFormat dfPercentage;
	
//	public static Image combineImages(List<Immagine> images) {
//		Image combinedImages;
//		if (!images.isEmpty()) {
//			if (images.size() == 1)
//				System.out.println("Combino!");
//			//Recupero la chiave
//			String key = "";
//			for (Immagine i : images) {
//				key += i.getPath();
//			}
//			if (immaginiCaricate.containsKey(key)) {
//				combinedImages = immaginiCaricate.get(key);
//			} else {
//				int h = 0;
//				int w = 0;
//				int d = 0;
//				PaletteData pd = null;
//				for (Immagine i : images) {
//					Image image = i.getImage();
//					ImageData data = image.getImageData();
//					//prendo la profondità maggiore
//					if (d < data.depth)
//						d = data.depth;
//					//Palette data
//					if (pd == null)
//						pd = data.palette;
//					//Prendo l'altezza maggiore
//					if (h < data.height) {
//						h = data.height;
//					}
//					//Combino le lunghezze
//					w += data.width;
//				}
//				//Creo la base
//				ImageData combinedImagesData = new ImageData(w, h, d, pd);
//				//Copio le immagini dentro
//				int cw = 0;
//				for (Immagine i : images) {
//					Image image = i.getImage();
//					ImageData data = image.getImageData();
//					for (int x = data.x; x < data.width; x++) {
//						for (int y = data.y; y < data.height; y++) {
//							combinedImagesData.setPixel(x + cw, y, data.getPixel(x, y));
//						}
//					}
//					cw += data.width;
//				}
//				combinedImages = new Image(Display.getCurrent(), combinedImagesData);
//				immaginiCaricate.put(key, combinedImages);
//			}
//		} else {
//			combinedImages = null;
//		}
//		return combinedImages;
//	}
	
//	public static Image getImage(String immagine) {
//		if (!immaginiCaricate.containsKey(immagine)) {
//			loadImage(immagine);
//		}
//		Image image = immaginiCaricate.get(immagine);
//		return image;
//	}
//
//	private static void loadImage(String immagine) {
//		try {
//			InputStream imageStream = Decorator.class.getResourceAsStream(immagine);
//			Image image = new Image(Display.getCurrent(), imageStream);
//			immaginiCaricate.put(immagine, image);
//		} catch (Exception e) {
//			System.out.println("L'immagine richiesta non è stata trovata.");
//		}
//	}
	
	/**
	 * Restituisce il valore formattato in euro del double passato come argomento.
	 * @param value Il valore in euro da formattare
	 * @return Una stringa formattata
	 */
	public static String getEuroValue(double value) {
		DecimalFormat df = getEuroFormatter();
		return df.format(value);
	}
	
	/**
	 * Restituisce il valore formattato in euro con 3 decimali del double passato come argomento.
	 * @param value Il valore in euro da formattare
	 * @return Una stringa formattata
	 */
	public static String getEuroValue3Decimals(double value) {
		DecimalFormat df = getEuroFormatter3Decimals();
		return df.format(value);
	}
	
	/**
	 * Restituisce il valore formattato come percentuale del double passato come argomento.
	 * @param value Il valore monetario da formattare
	 * @param currency Il tipo di denaro
	 * @return Una stringa formattata
	 */
	public static String getPercentageValue(double value) {
		DecimalFormat df = getPercentageFormatter();
		return df.format(value);
	}
	
	/**
	 * Restituisce il valore formattato come intero del double passato come argomento.
	 * @param value Il valore monetario da formattare
	 * @param currency Il tipo di denaro
	 * @return Una stringa formattata
	 */
	public static String getIntegerValue(double value) {
		DecimalFormat df = getIntegerFormatter();
		return df.format(value);
	}
	
	/**
	 * Restituisce il valore formattato come decimale del double passato come argomento.
	 * @param value Il valore monetario da formattare
	 * @param currency Il tipo di denaro
	 * @return Una stringa formattata
	 */
	public static String getDoubleWithDecimalValue(double value) {
		DecimalFormat df = getDecimalFormatter();
		return df.format(value);
	}
	
	private static DecimalFormat getIntegerFormatter() {
		if (dfInteri == null) {
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALY);
			dfInteri = (DecimalFormat) nf;
			dfInteri.applyPattern("###,##0");
		}
		return dfInteri;
	}
	
	private static DecimalFormat getDecimalFormatter() {
		if (dfDecimali == null) {
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALY);
			dfDecimali = (DecimalFormat) nf;
			dfDecimali.applyPattern("###,##0.0##");
		}
		return dfDecimali;
	}
	
	private static DecimalFormat getEuroFormatter() {
		if (dfEuro2Decimali == null) {
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALY);
			dfEuro2Decimali = (DecimalFormat) nf;
			dfEuro2Decimali.applyPattern("\u20AC ###,##0.00");
		}
		return dfEuro2Decimali;
	}
	
	private static DecimalFormat getEuroFormatter3Decimals() {
		if (dfEuro3Decimali == null) {
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALY);
			dfEuro3Decimali = (DecimalFormat) nf;
			dfEuro3Decimali.applyPattern("\u20AC ###,##0.000");
		}
		return dfEuro3Decimali;
	}
	
	private static DecimalFormat getPercentageFormatter() {
		if (dfPercentage == null) {
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALY);
			dfPercentage = (DecimalFormat) nf;
			dfPercentage.applyPattern("##0.00 %");
		}
		return dfPercentage;
	}

}
