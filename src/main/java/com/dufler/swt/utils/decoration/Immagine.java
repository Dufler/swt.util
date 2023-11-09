package com.dufler.swt.utils.decoration;

import java.io.InputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

public enum Immagine {
	
	COPIA_16X16("src/main/resources/icons/copy_16x16.png"),
	COPIA_ANNULLATA_16X16("src/main/resources/icons/copyAnnullata_16x16.png"),
	
	CODICE_16X16("src/main/resources/icons/codice_16x16.png"),
	CAP_16X16("src/main/resources/icons/cap_16x16.png"),
	IMPORT_16X16("src/main/resources/icons/import_16x16.png"),
	EXPORT_16X16("src/main/resources/icons/export_16x16.png"),
	LISTINO_16X16("src/main/resources/icons/listino_16x16.png"),
	SALVADANAIO_16X16("src/main/resources/icons/salvadanaio_16x16.png"),
	IMPOSTAZIONI_16X16("src/main/resources/icons/impostazioni_16x16.png"),
	CALCOLO_16X16("src/main/resources/icons/calcolo_16x16.png"),
	WIZARD_16X16("src/main/resources/icons/wizard_16x16.png"),
	WIZARD_ANNULLATO_16X16("src/main/resources/icons/wizardAnnullato_16x16.png"),
	TRASPORTI_16X16("src/main/resources/icons/trasporti_16x16.png"),
	TELEFONO_16X16("src/main/resources/icons/telefono_16x16.png"),
	MONETE_16X16("src/main/resources/icons/monete_16x16.png"),
	MAIL_16X16("src/main/resources/icons/mail_16x16.png"),
	LISTA_16X16("src/main/resources/icons/lista_16x16.png"),
	INFO_16X16("src/main/resources/icons/info_16x16.png"),
	FULMINE_16X16("src/main/resources/icons/fulmine_16x16.png"),
	FULMINE_CON_FRECCIA_16X16("src/main/resources/icons/fulmineConFreccia_16x16.png"),
	FULMINE_ANNULLATO_16X16("src/main/resources/icons/fulmineCancella_16x16.png"),
	
	CARICO_LAVORAZIONE("src/main/resources/icons/caricoLavorazione_16x16.png"),
	CARICO_CHIUSO("src/main/resources/icons/caricoChiuso_16x16.png"),
	
	SCARICHI_16X16("src/main/resources/icons/scarichi_16x16.png"),
	SCATOLA_16X16("src/main/resources/icons/scatola_16x16.png"),
	SCATOLA_ANNULLATA_16X16("src/main/resources/icons/scatolaAnnullata_16x16.png"),
	
	UOMOSCRIVANIA_16X16("src/main/resources/icons/uomoScrivania_16x16.png"),
	TIMER_16X16("src/main/resources/icons/timer_16x16.png"),
	REFRESH_16X16("src/main/resources/icons/refresh_16x16.png"),
	CESTINO_16X16("src/main/resources/icons/cestino_16x16.png"),
	CESTINO_ANNULLATO_16X16("src/main/resources/icons/cestinoAnnullato_16x16.png"),
	MATITA_16X16("src/main/resources/icons/matita_16x16.png"),
	MATITA_ANNULLATA_16X16("src/main/resources/icons/matitaAnnullata_16x16.png"),
	LENTE_16X16("src/main/resources/icons/lente_16x16.png"),
	LENTE_ANNULLATA_16X16("src/main/resources/icons/lenteAnnullata_16x16.png"),
	REPORT_16X16("src/main/resources/icons/report_16x16.png"),
	
	SACCO_SOLDI_16X16("src/main/resources/icons/saccoSoldi_16x16.png"),
	SOLDISU_16X32("src/main/resources/icons/soldiSu_16x32.png"),
	SOLDIGIU_16X32("src/main/resources/icons/soldiGiu_16x32.png"),
	SOLDIGIUGIALLO_16X32("src/main/resources/icons/soldiGiuGiallo_16x32.png"),
	
	SPUNTAVERDE_16X16("src/main/resources/icons/spuntaVerde_16x16.png"),
	SPUNTAVERDE_16X50("src/main/resources/icons/spuntaVerde_16x50.png"),
	CROCEROSSA_16X16("src/main/resources/icons/croceRossa_16x16.png"),
	CROCEROSSA_16X50("src/main/resources/icons/croceRossa_16x50.png"),
	
	CROCEVERDE_16X16("src/main/resources/icons/croceVerde_16x16.png"),
	CROCEVERDE_ANNULLATA_16X16("src/main/resources/icons/croceVerdeAnnullata_16x16.png"),
	CROCIVERDI_16X16("src/main/resources/icons/crociVerdi_16x16.png"),
	CROCIVERDI_ANNULLATE_16X16("src/main/resources/icons/crociVerdiAnnullate_16x16.png"),
	
	EXCEL_16X16("src/main/resources/icons/excel_16x16.png"),
	EXCEL_ANNULLATO_16X16("src/main/resources/icons/excelAnnullato_16x16.png"),
	
	PDF_16X16("src/main/resources/icons/pdf_16x16.png"),
	PDF_ANNULLATO_16X16("src/main/resources/icons/pdfAnnullato_16x16.png"),
	
	FRECCIAVERDESU_16X16("src/main/resources/icons/frecciaVerdeSu_16x16.png"),
	FRECCIABLUSU_16X16("src/main/resources/icons/frecciaBluSu_16x16.png"),
	FRECCIAGIALLAGIU_16X16("src/main/resources/icons/frecciaGiallaGiu_16x16.png"),
	FRECCIAROSSAGIU_16X16("src/main/resources/icons/frecciaRossaGiu_16x16.png"),
	FRECCEROSSEGIU_16X16("src/main/resources/icons/frecceRosseGiu_16x16.png"),
	
	RED_QUESTION_MARK_12X20("src/main/resources/icons/redQuestionMark_12x20.png"),
	RED_MARK_12X20("src/main/resources/icons/redMark_12x20.png"),
	ORANGE_MARK_12X20("src/main/resources/icons/orangeMark_12x20.png"),
	YELLOW_MARK_12X20("src/main/resources/icons/yellowMark_12x20.png"),
	
	UTENTE_16X16("src/main/resources/icons/user_16x16.png"),
	
	STAMPANTE_16x16("src/main/resources/icons/print/printer.png"),
	STAMPANTE_AGGIUNGI_16x16("src/main/resources/icons/print/printer_add.png"),
	STAMPANTE_ELIMINA_16x16("src/main/resources/icons/print/printer_delete.png"),
	STAMPANTE_VUOTA_16x16("src/main/resources/icons/print/printer_empty.png"),
	STAMPANTE_ERRORE_16x16("src/main/resources/icons/print/printer_error.png"),
	
	TRASPARENTE_16X16("src/main/resources/icons/trasparente_16x16.png"),
	
	LOADING_256X256("src/main/resources/icons/loading_256x256.png");
	
	private final String path;
	private final Image image;
	private final Image imageWithLeftPadding;
	
	private Immagine(String path) {
		this.path = path;
		InputStream imageStream = Immagine.class.getResourceAsStream(path);
		InputStream paddingStream = Immagine.class.getResourceAsStream("src/main/resources/icons/trasparente_16x16.png");
		if (imageStream != null && paddingStream != null) {
			image = new Image(Display.getCurrent(), imageStream);
			Image padding = new Image(Display.getCurrent(), paddingStream);
			imageWithLeftPadding = combineImages(padding, image);
			//pulisco
			padding.dispose();
		} else {
			image = null;
			imageWithLeftPadding = null;
		}
		
	}
	
	public String getPath() {
		return path;
	}
	
	public Image getImage() {
		return image;
	}
	
	public Image getImageWithLeftPadding() {
		return imageWithLeftPadding;
	}

	private static Image combineImages(Image... images) {
		Image combinedImages;
		if (images.length > 0) {
				int h = 0;
				int w = 0;
				int d = 0;
				PaletteData pd = null;
				for (Image image : images) {
					ImageData data = image.getImageData();
					// prendo la profondità maggiore
					if (d < data.depth)
						d = data.depth;
					// Palette data
					if (pd == null)
						pd = data.palette;
					// Prendo l'altezza maggiore
					if (h < data.height) {
						h = data.height;
					}
					// Combino le lunghezze
					w += data.width;
				}
				// Creo la base
				ImageData combinedImagesData = new ImageData(w, h, d, pd);
				// Copio le immagini dentro
				int cw = 0;
				for (Image image : images) {
					ImageData data = image.getImageData();
					for (int x = data.x; x < data.width; x++) {
						for (int y = data.y; y < data.height; y++) {
							combinedImagesData.setPixel(x + cw, y, data.getPixel(x, y));
						}
					}
					cw += data.width;
				}
				combinedImages = new Image(Display.getCurrent(), combinedImagesData);
		} else {
			combinedImages = null;
		}
		return combinedImages;
	}

}
