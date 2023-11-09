package com.dufler.swt.utils.input;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.validation.ParentValidationHandler;
import com.dufler.swt.utils.validation.ValidationHandler;

public abstract class TextField<T> extends Text implements InputElement, ValidationHandler {
	
	public static final String DEFAULT_ERROR_MESSAGE = "Il valore inserito non \u00E8 corretto.";
	public static final String DEFAULT_MAX_LENGTH_MESSAGE = "Il valore inserito \u00E8 troppo lungo.";
	public static final String DEAFULT_REQUIRED_MESSAGE = "L'inserimento di un valore \u00E8 obbligatorio.";
	
	public static final int DEFAULT_STYLE = SWT.BORDER;
	public static final int DEFAULT_DESCRIPTION_STYLE = SWT.BORDER | SWT.MULTI | SWT.WRAP;
	
	protected ControlDecoration wrongDecoration;
	protected ControlDecoration requiredDecoration;
	
	protected boolean required;
	protected boolean dirty;
	protected boolean valid;
	
	protected String regex;
	protected Integer maxLength;
	
	private ParentValidationHandler successor;

	/**
	 * Costruttore che permette di specificare lo stile e la regex con cui validare il contenuto.
	 * @param parent il composite su cui attaccare il campo di testo.
	 * @param style lo stile per il campo di testo.
	 * @param regex la regex con cui validare il contenuto, passando <code>null</code> non verrà validato.
	 */
	public TextField(Composite parent, int style, String regex, Integer maxLength) {
		super(parent, style);
		if (parent instanceof ParentValidationHandler) {
			successor = (ParentValidationHandler) parent;
			successor.addChild(this);
		} else {
			successor = null;
		}
		this.regex = regex;
		this.maxLength = maxLength;
		this.required = true;
		addDecoration();
		addListener();
	}
	
	@Override
	public void checkSubclass() {
		//DO NOTHING!
	}
	
	public abstract T getValue();
	
	public abstract void setValue(T value);
	
	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(boolean required) {
		this.required = required;
		validate();
	}
	
	@Override
	public boolean isDirty() {
		return dirty;
	}
	
	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		validate();
	}
	
	protected void resetDirty() {
		dirty = false;
	}
	
	protected void showErrorDecoration() {
		setDefaultErrorMessage();
		wrongDecoration.show();
	}
	
	protected void showMaxLenghtDecoration() {
		setDefaultMaxLengthMessage();
		wrongDecoration.show();
	}
	
	/**
	 * Imposta manualmente la validazione a false e mostra il messaggio d'errore specificato.
	 * @param errorMessage il messaggio da mostrare sul tooltip.
	 */
	public void showErrorDecoration(String errorMessage) {
		valid = false;
		wrongDecoration.setDescriptionText(errorMessage);
		wrongDecoration.show();
	}
	
	protected void showRequiredDecoration() {
		setDefaultRequiredMessage();
		requiredDecoration.show();
	}
	
	/**
	 * Imposta manualmente la validazione a false, l'obbligatorietà a true e mostra il messaggio d'errore specificato.
	 * @param requiredMessage il messaggio da mostrare sul tooltip.
	 */
	public void showRequiredDecoration(String requiredMessage) {
		valid = false;
		required = true;
		requiredDecoration.setDescriptionText(requiredMessage);
		requiredDecoration.show();
	}
	
	private void hideDecorations() {
		wrongDecoration.hide();
		requiredDecoration.hide();
	}
	
	private void addDecoration() {
		wrongDecoration = new ControlDecoration(this, SWT.RIGHT);
		Image imageWrong = Immagine.RED_MARK_12X20.getImage();
		wrongDecoration.setImage(imageWrong);
		requiredDecoration = new ControlDecoration(this, SWT.RIGHT);
		Image imageRequired = Immagine.YELLOW_MARK_12X20.getImage();
		requiredDecoration.setImage(imageRequired);
		hideDecorations();
	}
	
	protected abstract void setDefaultRequiredMessage();
	
	protected abstract void setDefaultErrorMessage();
	
	protected void setDefaultMaxLengthMessage() {
		wrongDecoration.setDescriptionText(DEFAULT_MAX_LENGTH_MESSAGE);
	}
	
	private void addListener() {
		addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
				dirty = true;			
			}		
		});
		//Nel caso in cui qualcuno prema invio non faccio propagare il comando oltre se la text field ha il focus.
		addListener(SWT.Traverse, event -> {
			if (event.character == SWT.CR || event.character == SWT.LF)
				event.doit = false;
		});
	}

	@Override
	public void setParent(ParentValidationHandler parent) {
		successor = parent;
	}
	
	protected void setRegex(String regularExpression) {
		regex = regularExpression;
	}

	@Override
	public boolean validate() {
		hideDecorations();
		valid = true;
		String valore = getText();
		//Se non è stato inserito un valore controllo se è obbligatorio
		if (valore.isEmpty()) {
			if (required) {
				valid = false;
				showRequiredDecoration();
			}
		} else { //Altrimenti se è stato inserito controllo che sia valido
			//Check sulla regex
			if (regex != null) {
				valid = valore.matches(regex);
				if (!valid)
					showErrorDecoration();
			}
			//Check sulla lunghezza massima
			if (valid && maxLength != null) {
				valid = valore.length() <= maxLength;
				if (!valid)
					showMaxLenghtDecoration();
			}
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
	public boolean isValid() {
		return valid;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		setEditable(enabled);
	}
	
	@Override
	public void resetValue() {
		setText("");
		dirty = false;
		validate();
	}

}
