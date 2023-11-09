package com.dufler.swt.utils.input;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.validation.ParentValidationHandler;
import com.dufler.swt.utils.validation.ValidationHandler;

public class Bottone extends Button implements InputElement, ValidationHandler {
	
	public static final String MESSAGE_REQUIRED = "La scelta di un valore \u00E8 obbligatoria.";
	
	private boolean required;
	private boolean dirty;
	private boolean valid;
	
	private final ControlDecoration requiredDecoration;
	private ParentValidationHandler successor;

	public Bottone(Composite parent, int style) {
		super(parent, style);
		required = false;
		dirty = false;
		requiredDecoration = new ControlDecoration(this, SWT.RIGHT);
		Image imageRequired = Immagine.YELLOW_MARK_12X20.getImage();
		requiredDecoration.setImage(imageRequired);
		requiredDecoration.setDescriptionText(MESSAGE_REQUIRED);
		hideDecorations();
		
		if (parent instanceof ParentValidationHandler) {
			successor = (ParentValidationHandler) parent;
			successor.addChild(this);
		} else {
			successor = null;
		}
		
		addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
				dirty = true;				
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				dirty = true;				
			}
		});
		
		valid = true;
	}
	
	private void showRequiredDecoration() {
		requiredDecoration.show();
	}
	
	private void hideDecorations() {
		requiredDecoration.hide();
	}

	@Override
	public void setParent(ParentValidationHandler parent) {
		successor = parent;	
	}

	@Override
	public boolean validate() {
		valid = true;
		if (required && !getSelection()) {
			valid = false;
			showRequiredDecoration();
		}
		if (valid)
			hideDecorations();
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
	public void setSelection(boolean selected) {
		super.setSelection(selected);
		validate();
		dirty = false;
	}
	
	@Override
	protected void checkSubclass() {
		// DO NOTHING!
	}

	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		validate();
	}

	@Override
	public void resetValue() {
		dirty = false;
		valid = true; //Sul costruttore è così, ma non sono sicuro che vada bene.		
	}

}
