package com.dufler.swt.utils.composite;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.dufler.swt.utils.input.InputElement;
import com.dufler.swt.utils.validation.ParentValidationHandler;
import com.dufler.swt.utils.validation.ValidationHandler;

/**
 * Oggetto simile ad un form, raccoglie elementi di input.
 * @author Damiano
 *
 */
public abstract class GruppoSemplice extends Composite implements ParentValidationHandler, GruppoDiElementi {
	
	protected boolean valid;
	protected boolean dirty;
	protected boolean required;
	protected ParentValidationHandler successor;
	protected final LinkedList<ValidationHandler> children;
	protected final Set<Control> nonUpdatableElements;
	
	/**
	 * Costruttore di default.
	 * @param parent l'oggetto che ospiterà il gruppo.
	 */
	public GruppoSemplice(ParentValidationHandler parentValidator, Composite parent) {
		this(parentValidator, parent, SWT.NONE);
	}

	/**
	 * Costruttore che permette di specificare lo stile SWT da applicare al gruppo.
	 * @param parent l'oggetto che ospiterà il gruppo.
	 * @param style lo stile SWT da applicare
	 */
	public GruppoSemplice(ParentValidationHandler parentValidator, Composite parent, int style) {
		super(parent, style);
		if (parentValidator != null) {
			successor = parentValidator;
			successor.addChild(this);
		} else {
			successor = null;
		}
		required = true;
		children = new LinkedList<ValidationHandler>();
		nonUpdatableElements = new HashSet<>();
		aggiungiElementiGrafici();
	}
	
	/**
	 * Questo metodo va implementato, viene richiamato per aggiungere gli elementi di input e grafici.
	 */
	public abstract void aggiungiElementiGrafici();
	
	public boolean isDirty() {
		dirty = false;
		for (Control child : getChildren()) {
			if (child instanceof InputElement) {
				InputElement i = (InputElement) child;
				dirty = i.isDirty();
			} else if (child instanceof GruppoDiElementi) {
				GruppoDiElementi g = (GruppoDiElementi) child;
				dirty = g.isDirty();
			}
			//Basta che uno sia "dirty" per terminare la verifica.
			if (dirty)
				break;
		}
		return dirty;
	}
	
	public boolean isRequired() {
		return required;
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public boolean validate() {
		valid = true;
		//Se è required oppure è stato toccato vado a validarlo
		if (required || isDirty()) for (ValidationHandler child : children) {
			valid = child.isValid();
			if (!valid)
				break;
		}
		valid = validazioneSpecifica(valid);
		forwardValidation();
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
		if (successor != null)
			successor.validate();
	}

	@Override
	public void setParent(ParentValidationHandler parent) {
		successor = parent;
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
	public void addNonUpdatableElement(Control control) {
		nonUpdatableElements.add(control);
	}
	
	/**
	 * Abilita o disabilita tutti gli elementi di input.
	 * @param enable true -> abilita, false-> disabilita
	 */
	@Override
	public void setEnabled(boolean enable) {
		for (Control child : getChildren()) {
			if (child instanceof InputElement) {
				if (!enable || !nonUpdatableElements.contains(child)) {
					if (child instanceof Text) {
						Text t = (Text) child;
						t.setEditable(enable);
					} else {
						child.setEnabled(enable);	
					}
				}					
			} else if (child instanceof GruppoDiElementi) {
				((GruppoDiElementi) child).setEnabled(enable);
			}
		}
	}
	
	/**
	 * Imposta come disabilitati tutti i controlli non aggiornabili.
	 */
	@Override
	public void lockNonUpdatableElements() {
		for (Control element : nonUpdatableElements) {
			if (element instanceof Text) {
				Text t = (Text) element;
				t.setEditable(false);
			} else {
				element.setEnabled(false);	
			}
		}			
	}
	
	@Override
	protected void checkSubclass() {
		//DO NOTHING!
	}
	
	/**
	 * Resetta tutti gli elementi di input.
	 */
	@Override
	public void resetValues() {
		for (Control child : getChildren()) {
			if (child instanceof InputElement)
				((InputElement) child).resetValue();
			else if (child instanceof GruppoDiElementi)
				((GruppoDiElementi) child).resetValues();
		}
	}

}
