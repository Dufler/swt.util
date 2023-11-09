package com.dufler.swt.utils.wizard;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

public abstract class WizardConRisultati extends WizardConFinale {
	
	protected final Set<PaginaWizardRisultati> pagineConRisultati;
	
	public WizardConRisultati(String title, boolean canGoBack) {
		super(title, canGoBack);
		pagineConRisultati = new HashSet<>();
	}
	
	@Override
	public void addPage(IWizardPage page) {
		if (page instanceof PaginaWizardRisultati) {
			PaginaWizardRisultati pagina = (PaginaWizardRisultati) page;
			pagineConRisultati.add(pagina);
		}
		super.addPage(page);
	}

	protected void aggiungiListener(Button nextButton) {
		nextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (PaginaWizardRisultati pagina : pagineConRisultati) {
					if (getContainer().getCurrentPage().equals(pagina)) {
						pagina.mostraRisultato();
					}
				}
			}
		});
	}
	
	@Override
	public boolean canFinish() {
		IWizardPage current = getContainer().getCurrentPage();
		boolean finish = checkLastPage(current) && current.isPageComplete();
		return finish;
	}
	
	protected boolean checkLastPage(IWizardPage current) {
		boolean check = false;
		if (current instanceof PaginaWizardRisultati) {
			PaginaWizardRisultati pagina = (PaginaWizardRisultati) current;
			check = pagina.isLastPage();
		}
		return check;
	}

}
