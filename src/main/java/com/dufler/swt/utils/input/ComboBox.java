package com.dufler.swt.utils.input;

import java.util.Collection;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.validation.ParentValidationHandler;
import com.dufler.swt.utils.validation.ValidationHandler;

public class ComboBox<T> extends Combo implements InputElement, ValidationHandler {
	
	public static final String MESSAGE_REQUIRED = "L'inserimento di un valore \u00E8 obbligatorio.";
	
	protected ComboViewer viewer;

	protected boolean required;
	protected boolean dirty;
	protected boolean valid;
	
	protected final ControlDecoration requiredDecoration;
	protected ParentValidationHandler successor;
	
	public ComboBox(Composite parent) {
		super(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
		required = true;
		dirty = false;
		requiredDecoration = new ControlDecoration(this, SWT.RIGHT);
		Image imageRequired = Immagine.YELLOW_MARK_12X20.getImage();
		requiredDecoration.setImage(imageRequired);
		requiredDecoration.setDescriptionText(MESSAGE_REQUIRED);
		hideDecorations();
		viewer = new ComboViewer(this);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setComparator(new ViewerComparator());
		
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
	}
	
	public ComboViewer getViewer() {
		return viewer;
	}
	
	public void addItem(String key, T value) {
		viewer.add(value);
	}
	
	public void setItems(Collection<T> list) {
		viewer.setInput(list);
	}
	
	public void setItems(T[] array) {
		viewer.setInput(array);
	}
	
	public boolean isSelected() {
		int index = getSelectionIndex();
		return index != -1;
	}
	
	@SuppressWarnings("unchecked")
	public T getSelectedValue() {
		int index = getSelectionIndex();
		T value = (T) viewer.getElementAt(index);
		return value;
	}
	
	public void checkSubclass() {
		//DO NOTHING!
	}
	
	private void showRequiredDecoration() {
		requiredDecoration.show();
	}
	
	private void hideDecorations() {
		requiredDecoration.hide();
	}
	
	@Override
	public boolean isValid() {
		return valid;
	}

	public void setSelectedValue(T value) {
		if (value != null) {
			StructuredSelection selection = new StructuredSelection(value);
			viewer.setSelection(selection);
		} else {
			StructuredSelection selection = new StructuredSelection();
			viewer.setSelection(selection);
		}
		notificaListenerSelezione();
		dirty = false;
		validate();
	}
	
	public void setSelectedItem(String value) {
		if (value != null) {
			Object valore = viewer.getData(value);
			StructuredSelection selection = new StructuredSelection(valore);
			viewer.setSelection(selection);
		}
		notificaListenerSelezione();
		dirty = false;
		validate();
	}
	
	protected void notificaListenerSelezione() {
//		Listener[] listeners = getListeners(SWT.Selection);
//		for (Listener listener : listeners) {
//			listener.
//		}
		notifyListeners(SWT.Selection, new Event());
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

	@Override
	public boolean validate() {
		valid = true;
		if (required && getSelectionIndex() == -1) {
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
	public void setParent(ParentValidationHandler parent) {
		successor = parent;	
	}

	@Override
	public void resetValue() {
		setSelectedValue(null);
		dirty = false;
	}

}
