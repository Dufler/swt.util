package com.dufler.swt.utils.validation;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.dufler.swt.utils.input.InputElement;

public class Validator {
	
	public boolean validate(Composite container) {
		boolean valid = true;
		Control[] children = container.getChildren();
		for (Control child : children) {
			if (child instanceof InputElement) {
				InputElement inputControl = (InputElement) child;
				if (!inputControl.isValid()) {
					valid = false;
					break;
				}
			}
		}
		return valid;
	}
	
	public boolean isDirty(Composite container) {
		boolean dirty = false;
		Control[] children = container.getChildren();
		for (Control child : children) {
			if (child instanceof InputElement) {
				InputElement inputControl = (InputElement) child;
				if (inputControl.isDirty()) {
					dirty = true;
					break;
				}
			}
		}
		return dirty;
	}

}
