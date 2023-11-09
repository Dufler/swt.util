package com.dufler.swt.utils.task;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.dufler.swt.utils.dialog.DialogErrore;
import com.dufler.swt.utils.dialog.DialogMessaggio;

import lombok.extern.slf4j.Slf4j;

/**
 * Questa classe genera una finestra con una progress bar.<br>
 * Esempio di utilizzo:<br><code>
 * Processo processo = new Processo();<br>
 * DialogProgresso dialog = new DialogProgresso("Il titolo");<br>
 * dialog.esegui(processo);<br></code>
 * @author Damiano
 *
 */
@Slf4j
public class DialogProgresso extends ProgressMonitorDialog {
	
	private static final String CANCEL_LABEL = "Annulla";
	public static final String TITOLO_DEFAULT = "Operazione in corso";
	private static int LABEL_DLUS = 21;
	private static int BAR_DLUS = 9;
	
	private final String titolo;
	
	/**
	 * Costruttore di default.
	 * @param title Il titolo della dialog.
	 */
	public DialogProgresso(String title) {
		super(getMyShell());
		titolo = title;
		enableCancelButton = false;
	}
	
	/**
	 * Costruttore che permette di specificare se sia possibile interrompere il processo.
	 * @param title Il titolo della dialog.
	 * @param canCancelOperation indica se è possibile per l'utente annullare il processo o meno.
	 */
	public DialogProgresso(String title, boolean canCancelOperation) {
		super(getMyShell());
		titolo = title;
		enableCancelButton = canCancelOperation;
	}
	
	private static Shell getMyShell() {
		Thread ct = Thread.currentThread();
		Display d = Display.findDisplay(ct);
		Shell s = d != null ? d.getActiveShell() : createNewDisplayAndShell();
		return s;
	}
	
	private static Shell createNewDisplayAndShell() {
		Display display = new Display();
		Shell shell = new Shell(display);
		return shell;
	}
	
	@Override
    protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(titolo);
    }
	
	@Override
	protected void createCancelButton(Composite parent) {
		cancel = createButton(parent, IDialogConstants.CANCEL_ID, CANCEL_LABEL, true);
		if (arrowCursor == null) {
			arrowCursor = new Cursor(cancel.getDisplay(), SWT.CURSOR_ARROW);
		}
		cancel.setCursor(arrowCursor);
		setOperationCancelButtonEnabled(enableCancelButton);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage(TITOLO_DEFAULT, false);
		createMessageArea(parent);
		// Only set for backwards compatibility
		taskLabel = messageLabel;
		// progress indicator
		progressIndicator = new ProgressIndicator(parent);
		GridData gd = new GridData();
		gd.heightHint = convertVerticalDLUsToPixels(BAR_DLUS);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		progressIndicator.setLayoutData(gd);
		// label showing current task
		subTaskLabel = new Label(parent, SWT.LEFT | SWT.WRAP);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = convertVerticalDLUsToPixels(LABEL_DLUS);
		gd.horizontalSpan = 2;
		subTaskLabel.setLayoutData(gd);
		subTaskLabel.setFont(parent.getFont());
		return parent;
	}
	
	private void setMessage(String messageString, boolean force) {
		// must not set null text in a label
		message = messageString == null ? "" : messageString; //$NON-NLS-1$
		if (messageLabel == null || messageLabel.isDisposed()) {
			return;
		}
		if (force || messageLabel.isVisible()) {
			messageLabel.setToolTipText(message);
			messageLabel.setText(shortenText(message, messageLabel));
		}
	}
	
	/**
	 * Metodo di convenienza per eseguire un processo.<br>
	 * Il thread verrà separato per non inficiare la UI e non potrà essere cancellato.<br>
	 * E' possibile specificare tali opzioni con l'altro metodo esegui.
	 * @param runnable Il processo da eseguire.
	 * @return l'esito dell'operazione.
	 */
	public boolean esegui(Processo runnable) {
		boolean eseguito = esegui(runnable, true);
		return eseguito;
	}
	
	/**
	 * Metodo specifico per eseguire un processo.<br>
	 * E' possibile specificare se separare il thread oppure no e se è possibile per l'utente interromperlo.
	 * @param runnable Il processo da eseguire.
	 * @param stessoThread va eseguito nello stesso thread?
	 * @param annullabile è annullabile? 
	 * @param visualizzazione delle dialog interne
	 * @return l'esito dell'operazione.
	 */
	public boolean esegui(Processo runnable, boolean stessoThread) {
		return esegui(runnable, stessoThread, true);
	}
	
	public boolean esegui(Processo runnable, boolean stessoThread, boolean showDialog) {
		boolean eseguito = true;
		try {
			run(stessoThread, enableCancelButton, runnable.getRunnable());
		} catch (InvocationTargetException e) {
			eseguito = false;
			if (showDialog) {
				DialogErrore.openError("Errore nell'esecuzione del task", e.getTargetException().getMessage(),	e.getTargetException());
			}
			log.error(e.getMessage(),e);
		} catch (InterruptedException e) {
			if (showDialog) {
				DialogMessaggio.openError("Procedura interrotta", "La procedura \u00E8 stata interrotta.");
			}
			log.error(e.getMessage(),e);
		} catch (Exception e) {
			if (showDialog) {
				StringBuilder messaggio = new StringBuilder("La procedura ha riscontrato un errore imprevisto: \r\n"); 
				messaggio.append(e.getLocalizedMessage());
				messaggio.append("\r\n");
				for (StackTraceElement elemente : e.getStackTrace()) {
					messaggio.append(elemente.toString());
					messaggio.append("\r\n");
				}
				DialogMessaggio.openError("Procedura interrotta", messaggio.toString());
			}
			log.error(e.getMessage(),e);
		}
		return eseguito;
	}

}
