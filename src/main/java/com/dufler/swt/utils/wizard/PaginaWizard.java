package com.dufler.swt.utils.wizard;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.dufler.swt.utils.validation.ParentValidationHandler;
import com.dufler.swt.utils.validation.ValidationHandler;

public abstract class PaginaWizard extends WizardPage implements ParentValidationHandler {

	protected boolean valid;
	protected final boolean lastPage;
	
	protected Composite container;
	protected final Set<ValidationHandler> children;
	
	protected PaginaWizard(String title, String description) {
		this(title, description, false);
	}
	
	protected PaginaWizard(String title, String description, boolean lastPage) {
		super("");
		setTitle(title);
		setDescription(description);
		setPageComplete(false);
		children = new HashSet<ValidationHandler>();
		this.lastPage = lastPage;
	}
	
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));
		setControl(container);
		aggiungiElementiGrafici(container);
		validate();
	}
	
	/**
	 * Aggiunge gli elemnti grafici
	 * @param container
	 */
	public abstract void aggiungiElementiGrafici(Composite container);
	
	/**
	 * Metodo che viene richiamato quando la validazione va a buon fine, serve a copiare i dati immessi dall'utente nei model manipolati.
	 */
	public abstract void copyDataToModel();

	@Override
	public boolean isValid() {
		return valid;
	}
	
	public boolean isLastPage() {
		return lastPage;
	}

	@Override
	public final boolean validate() {
		valid = true;
		for (ValidationHandler child : children) {
			valid = child.isValid();
			if (!valid)
				break;
		}
		valid = validazioneSpecifica(valid);
		//E' stato spostato sotto, viene eseguito al momento del cambio pagina.
		//if (valid)
		//	copyDataToModel();
		setPageComplete(valid);
		return valid;
	}
	
	/**
	 * Metodo astratto da riscrevere nel caso di validazione specifiche.
	 * @return
	 */
	protected boolean validazioneSpecifica(boolean valid) {
		return valid;
	}

	@Override
	public void forwardValidation() {
		//DO NOTHING!		
	}
	
	@Override
	public void setParent(ParentValidationHandler parent) {
		//DO NOTHING!
		
	}

	@Override
	public void addChild(ValidationHandler child) {
		child.setParent(this);
		children.add(child);
	}
	
	@Override
	public void removeChild(ValidationHandler child) {
		child.setParent(null);
		children.remove(child);
	}
	
	@Override
	public IWizardPage getNextPage() {
		//Gli faccio copiare i dati se sono validi
		if (valid)
			copyDataToModel();
		return super.getNextPage();
	}
	
	@Override
	public boolean canFlipToNextPage() {
        return isPageComplete() && !lastPage;
    }

}
