package com.dufler.swt.utils.composite;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.dufler.swt.utils.input.InputElement;
import com.dufler.swt.utils.validation.ParentValidationHandler;
import com.dufler.swt.utils.validation.ValidationHandler;

/**
 * Oggetto simile ad un form, raccoglie elementi di input.
 * E' possibile specificare un titolo.
 * @author Damiano
 *
 */
public abstract class Gruppo extends Group implements ParentValidationHandler, GruppoDiElementi {
	
	protected boolean valid;
	protected boolean dirty;
	protected boolean required;
	protected ParentValidationHandler successor;
	protected final Set<ValidationHandler> children;
	protected final Set<Control> nonUpdatableElements;
	
	/**
	 * Costruttore di default.
	 * @param parent l'oggetto che ospiterà il gruppo.
	 * @param title il titolo da dare all'insieme di elementi.
	 */
	public Gruppo(ParentValidationHandler parentValidator, Composite parent, String title) {
		this(parentValidator, parent, SWT.NONE, title, null);
	}
	
	/**
	 * Costruttore con setup.
	 * @param parentValidator
	 * @param parent
	 * @param title
	 */
	public Gruppo(ParentValidationHandler parentValidator, Composite parent, String title, Object setupInfo) {
		this(parentValidator, parent, SWT.NONE, title, setupInfo);
	}

	/**
	 * Costruttore che permette di specificare lo stile SWT da applicare al gruppo.
	 * @param parent l'oggetto che ospiterà il gruppo.
	 * @param title il titolo da dare all'insieme di elementi.
	 * @param style lo stile SWT da applicare
	 */
	public Gruppo(ParentValidationHandler parentValidator, Composite parent, int style, String title, Object setupInfo) {
		super(parent, style);
		setText(title);
		if (parentValidator != null) {
			successor = parentValidator;
			successor.addChild(this);
		} else {
			successor = null;
		}
		required = true;
		children = new HashSet<>();
		nonUpdatableElements = new HashSet<>();
		setup(setupInfo);
		aggiungiElementiGrafici();
	}
	
	/**
	 * Questo metodo va esteso per poter valorizzare variabili prima degli elementi grafici.
	 */
	public void setup(Object setupInfo) {
		//DO NOTHING!
	}
	
	/**
	 * Questo metodo va implementato, viene richiamato per aggiungere gli elementi di input e grafici.
	 * TODO: si protrebbe mettere un oggetto di configurazione (estendibile) come parametro in ingresso a questo metodo he contiene tutte le info necessarie alla configurazione (es. commessa) la maggior parte delle volte questo oggetto sarebbe null.
	 */
	public abstract void aggiungiElementiGrafici();
	
	public boolean isRequired() {
		return required;
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}
	
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
		forwardValidation();
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
//		super.setEnabled(enable);
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
