package com.dufler.swt.utils.task;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.dufler.swt.utils.dialog.DialogMessaggio;

/**
 * Classe astratta da estendere nei punti di codice dove vanno eseguite operazioni lente.
 * @author Damiano
 *
 */
public abstract class Processo {
	
	protected final String titolo;
	protected final Integer operazioni;
	private IProgressMonitor monitor;
	private final IRunnableWithProgress runnable;
	
	private Exception exception;
	
	public Exception getException() {
		return exception;
	}
	
	/**
	 * Costruisce un processo per cui andranno specificate le operazioni da eseguire.
	 * Se viene indicato un numero di operazioni nullo o minore di 0 allora vienesupposto che non si sappia quante sono.
	 * @param titolo Il messaggio da mostrare nella schermata di caricamento
	 * @param operazioni Il numero di operazioni da eseguire
	 */
	public Processo(String title, Integer operations) {
		titolo = title;
		if (operations == null || operations <= 0)
			operations = IProgressMonitor.UNKNOWN;
		operazioni = operations;
		runnable = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor m) throws InvocationTargetException, InterruptedException {
				monitor = m;
				if (monitor != null)
					monitor.beginTask(titolo, operazioni);
				try {
					eseguiOperazioni();
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				}
				if (monitor != null)
					monitor.done();
			}
			
		};
	}
	
	public void aumentaProgresso(int progresso) {
		if (monitor != null)
			monitor.worked(progresso);
	}
	
	/**
	 * Questo metodo va implementato, qui vanno inserite le operazioni da eseguire.
	 * E' possibile e doveroso chiamare all'interno il metodo <code>monitor.worked(1)</code> ogniqualvolta viene eseguita una operazione
	 * @param monitor Il monitor che indica l'avanzamento di processo.
	 */
	public abstract void eseguiOperazioni() throws Exception;
	
	/**
	 * Metodo di convenienza, non va invocato mai a meno che non sia l'unico modo per eseguire il processo senza l'utilizzo di una dialog con progress bar.
	 */
	public void eseguiOperazioniSenzaProgresso() {
		eseguiOperazioniSenzaProgresso(true);
	}
	
	public void eseguiOperazioniSenzaProgresso(boolean showErrorDialog) {
		try {
			eseguiOperazioni();
		} catch (Exception e) {
			e.printStackTrace();
			exception = e;
			if (showErrorDialog) {
				showErrorDialog();
			}			
		}
	}
	
	protected void showErrorDialog() {
		StringBuilder messaggio = new StringBuilder();
		messaggio.append("La procedura ha riscontrato un errore imprevisto: ");
		messaggio.append(exception.getLocalizedMessage());
		for (StackTraceElement elemente : exception.getStackTrace()) {
			messaggio.append("\r\n");
			messaggio.append(elemente.toString());
		}
		DialogMessaggio.openError("Procedura interrotta", messaggio.toString());
	}

	protected IRunnableWithProgress getRunnable() {
		return runnable;
	}

}
