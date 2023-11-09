package com.dufler.swt.utils.dialog;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.dufler.swt.utils.validation.ParentValidationHandler;
import com.dufler.swt.utils.validation.ValidationHandler;

public abstract class DialogSempliceConValidazione extends DialogSemplice implements ParentValidationHandler {
	
	protected static final String TITLE_CONFIRM_CANCEL = "Conferma chiusura";
	protected static final String MESSAGE_CONFIRM_CANCEL = "Ci sono dei dati non salvati, sei sicuro di voler chiudere?";
	
	protected boolean valid;
	protected final Set<ValidationHandler> children;
	
	public DialogSempliceConValidazione(String title, Image image, boolean abilitaComponenti) {
		super(title, image, abilitaComponenti);
		children = new HashSet<ValidationHandler>();
	}

	@Override
	public void setParent(ParentValidationHandler parent) {
		//DO NOTHING!
	}

	@Override
	public boolean isValid() {
		return valid;
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
		enableOkButton(valid);
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
	protected void okPressed() {
		if (isDirty()) {
			if (validation())
				super.okPressed();
		} else {
			super.okPressed();
		}
	}
	
	@Override
	protected void cancelPressed() {
		if (isDirty()) {
			boolean chiudi = mostraMessaggio(TITLE_CONFIRM_CANCEL, MESSAGE_CONFIRM_CANCEL);
			if (chiudi)
				super.cancelPressed();
		} else {
			super.cancelPressed();
		}
	}
	
	@Override
	public void aggiungiAltriBottoni(Composite parent) {
		validate();
	}
	
	/**
	 * Specifica se l'utente ha fatto cambiamenti sui valori.
	 * @return
	 */
	public abstract boolean isDirty();
	
	/**
	 * Metodo da estendere per definire come deve essere eseguita la validazione.
	 * @return
	 */
	protected abstract boolean validation();

}
