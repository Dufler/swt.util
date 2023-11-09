package com.dufler.swt.utils.dialog;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class DialogErrore extends ErrorDialog {
	
	public static final String DEFAULT_PLUGIN_ID = "it.ltc.logica.gui";
	
	private static final String mailTo = "damiano.bellucci@ltc-logistics.it";
	private static final String subject = "Logica%20-%20Bug%20Report";
	private static final String cc = "support@ltc-logistics.it";
	
	private static final String LABEL_SEND_REPORT_BUTTON = "Invia segnalazione bug";
	private Button sendReportButton;
	
	private final IStatus status;

	public DialogErrore(Shell parent, String dialogTitle, String message, IStatus status, int displayMask) {
		super(parent, dialogTitle, message, status, displayMask);
		this.status = status;
		this.message = message;
	}
	
	public static int openError(String dialogTitle,	String message, Throwable t) {
		MultiStatus status = createMultiStatus(t.getLocalizedMessage(), t);
		return openError(dialogTitle, message, status, IStatus.OK | IStatus.INFO | IStatus.WARNING | IStatus.ERROR);
	}
	
	public static int openError(String title, String message, IStatus status, int displayMask) {
		DialogErrore dialog = new DialogErrore(null, title, message, status, displayMask);
		return dialog.open();
	}
	
	private static MultiStatus createMultiStatus(String msg, Throwable t) {

        List<Status> childStatuses = new ArrayList<>();
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

        for (StackTraceElement stackTrace: stackTraces) {
            Status status = new Status(IStatus.ERROR, DEFAULT_PLUGIN_ID, stackTrace.toString());
            childStatuses.add(status);
        }

        MultiStatus ms = new MultiStatus(DEFAULT_PLUGIN_ID, IStatus.ERROR, childStatuses.toArray(new Status[] {}), "" /*t.toString()*/, t);
        return ms;
    }
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,	true);
		createDetailsButton(parent);
		createSendReportButton(parent);
	}

	private void createSendReportButton(Composite parent) {
		if (shouldShowDetailsButton()) {
			sendReportButton = createButton(parent, IDialogConstants.HELP_ID, LABEL_SEND_REPORT_BUTTON, false);
			sendReportButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					inviaSegnalazioneErrore();
				}
			});
		}
	}
	
	private void inviaSegnalazioneErrore() {
		String message = status.getMessage();
		for (IStatus s :status.getChildren()) {
			//CR - Carriage return = %0D
			//LF - Line feed = %0A
			message += "%0D" + "%0A" + s.getMessage();
		}
		System.out.println(message);
		String body = escapeHTML(message);
		final String mailURIStr = String.format("mailto:%s?subject=%s&cc=%s&body=%s", mailTo, subject, cc, body);
		try {
			URI mailURI = new URI(mailURIStr);
			Desktop.getDesktop().mail(mailURI);
		} catch (URISyntaxException e) {
			String errorMessage = "\r\nImpossibile inviare la segnalazione agli indirizzi specificati: \r\n" + e.getMessage();
			errorMessage += "\r\nCarattere errato: '" + mailURIStr.charAt(e.getIndex()) + "'";
			DialogMessaggio.openError("Errore nell'invio della segnalazione", errorMessage);
		} catch (IOException e) {
			DialogMessaggio.openError("Errore nell'invio della segnalazione", "Impossibile inviare la segnalazione: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static String escapeHTML(String s) {
	    StringBuilder out = new StringBuilder(Math.max(16, s.length()));
	    for (int i = 0; i < s.length(); i++) {
	        char c = s.charAt(i);
	        switch (c) {
		        case ' ' : out.append("%20"); break;
		        case '"' : out.append("%22"); break;
		        case '(' : out.append("%28"); break;
		        case ')' : out.append("%29"); break;
		        case ':' : out.append("%3A"); break;
		        case '=' : out.append("%3D"); break;
		        default : out.append(c);
	        }
	    }
	    return out.toString();
	}

}
