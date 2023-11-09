package com.dufler.swt.utils.wizard;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * Classe astratta che tutti i wizard devono estendere.
 * Va implementato il metodo <code>finisci()</code> dove devono essere eseguite tutte le operazioni di elaborazione dei dati.
 * @author Damiano Bellucci - damiano.bellucci@gmail.com
 *
 */
public abstract class WizardConFinale extends Wizard {
	
	private static final String DEFAULT_TITLE = "Wizard";
	
	protected final boolean tornaIndietro;
	
	public WizardConFinale(String title, boolean canGoBack) {
		tornaIndietro = canGoBack;
		setWindowTitle(title != null ? title : DEFAULT_TITLE);
	}
	
	@Override
	public boolean performFinish() {
		PaginaWizard ultimaPagina = (PaginaWizard) getContainer().getCurrentPage();
		ultimaPagina.copyDataToModel();
		return finisci();
	}
	
	/**
	 * Metodo astratto da implementare. Qui dentro vanno eseguite tutte le operazioni per il salvataggio/elaborazione dei dati.
	 * @return true se l'elaborazione è andata a buon fine, false altrimenti.
	 */
	public abstract boolean finisci();
	
	@Override
	public boolean performCancel() {
        return annulla();
    }
	
	/**
	 * Metodo astratto da implementare. Qui dentro vanno eseguite tutte le operazioni per l'annullamenteo dell'elaborazione dei dati.
	 */
	public abstract boolean annulla();
	
	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		IWizardPage previous = tornaIndietro ? super.getPreviousPage(page) : null;
		return previous;
	}

}
