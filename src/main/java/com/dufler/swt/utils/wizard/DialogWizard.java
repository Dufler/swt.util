package com.dufler.swt.utils.wizard;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.dialog.DialogApribile;

public class DialogWizard extends WizardDialog implements DialogApribile {
	
	public static final int WIZARD_DEFAULT= -1;
	public static final int WIZARD_LISTINO = 1;
	public static final int WIZARD_PREVENTIVO = 2;
	public static final int WIZARD_SPEDIZIONE = 3;	
	
	private final WizardConFinale wizard;
	
	private Button fine;
	private Button annulla;
	private Button aiuto;
	private Button avanti;
	private Button indietro;

	public DialogWizard(WizardConFinale newWizard) {
		this(newWizard, 0);
	}
	
	public DialogWizard(WizardConFinale newWizard, int wizardType) {
		super(Display.getDefault().getActiveShell(), newWizard);
		wizard = newWizard;
		switch(wizardType) {
			case WIZARD_LISTINO : decoraPerListini(); break;
			case WIZARD_PREVENTIVO : decoraPerPreventivi(); break;
			case WIZARD_SPEDIZIONE : decoraPerSpedizioni(); break;
			default : decoraDefault();
		}
	}
	
	private void aggiungiListener() {
		if (wizard instanceof WizardConRisultati) {
			WizardConRisultati wr = (WizardConRisultati) wizard;
			wr.aggiungiListener(avanti);
		}
	}
	
	private void setMinimizable(boolean blocca) {
		int mode = blocca ? SWT.APPLICATION_MODAL : SWT.MODELESS;
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.MIN | SWT.TITLE | SWT.BORDER | mode | SWT.RESIZE | getDefaultOrientation());
		//setShellStyle(getShellStyle() ^ SWT.MIN | SWT.MODELESS);
		setBlockOnOpen(blocca);
	}
	
	private void decoraDefault() {
		setDefaultImage(Immagine.WIZARD_16X16.getImage());
		setMinimizable(true);
	}
	
	private void decoraPerListini() {
		setDefaultImage(Immagine.LISTINO_16X16.getImage());
		setMinimizable(true);
	}
	
	private void decoraPerPreventivi() {
		setDefaultImage(Immagine.CALCOLO_16X16.getImage());
		setMinimizable(false);
	}
	
	private void decoraPerSpedizioni() {
		setDefaultImage(Immagine.TRASPORTI_16X16.getImage());
		setMinimizable(true);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		
		fine = getButton(IDialogConstants.FINISH_ID);
		if (fine != null)
			fine.setText("Fine");
		
		annulla = getButton(IDialogConstants.CANCEL_ID);
		if (annulla != null)
			annulla.setText("Annulla");
		
		aiuto = getButton(IDialogConstants.HELP_ID);
		if (aiuto != null)
			aiuto.setText("Aiuto");
		
		avanti = getButton(IDialogConstants.NEXT_ID);
		if (avanti != null)
			avanti.setText("Avanti");
		
		indietro = getButton(IDialogConstants.BACK_ID);
		if (indietro != null)
			indietro.setText("Indietro");
		
		aggiungiListener();
		
		
	}
	
	@Override
	protected void cancelPressed() {
		Display display = Display.getCurrent();
		Shell messageShell = display.getActiveShell();
		boolean scelta = MessageDialog.openConfirm(messageShell, "Conferma chiusura", "Sei sicuro di voler chiudere il wizard?");
		if (scelta) {
			super.cancelPressed();
		}
	}

	public Button getNextButton() {
		return getButton(IDialogConstants.NEXT_ID);
	}
	

}
