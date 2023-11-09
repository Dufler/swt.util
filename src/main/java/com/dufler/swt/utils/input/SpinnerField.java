package com.dufler.swt.utils.input;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.validation.ParentValidationHandler;
import com.dufler.swt.utils.validation.ValidationHandler;

/**
 * Controllo che permette di selezionare un numero intero compreso in un range ben definito di valori.
 * @author Damiano
 *
 */
public class SpinnerField extends Spinner implements InputElement, ValidationHandler {
	
	public static final String DEFAULT_ERROR_MESSAGE = "Il valore inserito non \u00E8 corretto.";
	public static final String DEAFULT_REQUIRED_MESSAGE = "L'inserimento di un valore \u00E8 obbligatorio.";
	
	public static final int DEFAULT_DIGITS = 0;
	public static final int DEFAULT_INCREMENT = 1;
	public static final int DEFAULT_PAGE_INCREMENT = 1;
	
	protected Integer selectedValue;
	protected int minValue;
	protected int maxValue;
	
	protected boolean required;
	protected boolean dirty;
	protected boolean valid;
	
	protected ControlDecoration wrongDecoration;
	protected ControlDecoration requiredDecoration;
	
	private ParentValidationHandler successor;
	
	public SpinnerField(Composite parent, int minValue, int maxValue) {
		super(parent, SWT.HORIZONTAL);
		
		if (parent instanceof ParentValidationHandler) {
			successor = (ParentValidationHandler) parent;
			successor.addChild(this);
		} else {
			successor = null;
		}
		
		this.minValue = minValue;
		this.maxValue = maxValue;
		
		setValues(minValue, minValue, maxValue, DEFAULT_DIGITS, DEFAULT_INCREMENT, DEFAULT_PAGE_INCREMENT);
		
		addDecoration();
		addListener();
	}
	
	@Override
	public void checkSubclass() {
		//DO NOTHING!
	}
	
	public Integer getValue() {
		return selectedValue;
	}
	
	public void setValue(Integer value) {
		this.selectedValue = value;
		setSelection(value != null && value >= minValue && value <= maxValue ? value : minValue);
		resetDirty();
		validate();
	}

	@Override
	public void setParent(ParentValidationHandler parent) {
		successor = parent;
	}

	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public boolean validate() {
		hideDecorations();
		valid = true;
		selectedValue = dirty ? getSelection() : selectedValue;
		if (selectedValue == null) {
			if (required) {
				valid = false;
				showRequiredDecoration();
			}
		} else {
			valid = selectedValue >= minValue && selectedValue <= maxValue;
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

	@Override
	public void resetValue() {
		setValue(null);
	}
	
	private void addDecoration() {
		wrongDecoration = new ControlDecoration(this, SWT.RIGHT);
		Image imageWrong = Immagine.RED_MARK_12X20.getImage();
		wrongDecoration.setImage(imageWrong);
		wrongDecoration.setDescriptionText(DEFAULT_ERROR_MESSAGE);
		requiredDecoration = new ControlDecoration(this, SWT.RIGHT);
		Image imageRequired = Immagine.YELLOW_MARK_12X20.getImage();
		requiredDecoration.setImage(imageRequired);
		requiredDecoration.setDescriptionText(DEAFULT_REQUIRED_MESSAGE);
		hideDecorations();
	}
	
	protected void showErrorDecoration() {
		wrongDecoration.show();
	}
	
	protected void showRequiredDecoration() {
		requiredDecoration.show();
	}
	
	private void hideDecorations() {
		wrongDecoration.hide();
		requiredDecoration.hide();
	}
	
	private void addListener() {
		addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
				dirty = true;
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				validate();
				dirty = true;
			}
			
		});
	}

}
