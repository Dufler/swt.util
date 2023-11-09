package com.dufler.swt.utils.input;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

public abstract class TextForSelection<T> extends TextField<T> {
	
	protected T value;
	
	/**
	 * Indica se è possibile effettuare modifiche di selezione. 
	 */
	protected boolean updatable;

	public TextForSelection(Composite parent, String message) {
		super(parent, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.READ_ONLY, null, null);
		updatable = true;
		setToolTipText(message);
		setMessage(message);
		super.setEditable(false);
		aggiungiListener();
	}
	
	private void aggiungiListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (updatable) {
					T value = apriDialogSelezione();
					if (value != null)
						setValue(value);
				}				
			}
		});
	}
	
	/**
	 * Non può mai venire abilitato per la scrittura.
	 */
	@Override
	public void setEnabled(boolean enabled) {
		updatable = enabled;
		super.setEnabled(false);
	}
	
	@Override
	public void setEditable(boolean editable) {
		updatable = editable;
		super.setEditable(false); //Non può mai essere editabile, il valore testuale varia solo in base alla selezione da doppio click.
	}
	
	/**
	 * Apre una dialog che permette di selezionare un valore.
	 */
	protected abstract T apriDialogSelezione();
	
	/**
	 * Mostra la selezione effettuata scrivendo qualcosa nella text box.
	 */
	protected abstract void mostraSelezione(T selection);
	
	/**
	 * Nasconde la selezione effettuata rimuovendo il testo dalla text box.
	 */
	protected void nascondiSelezione() {
		setText("");
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
		if (value != null) {
			mostraSelezione(value);
			notifyListeners(SWT.Selection, new Event()); //TEST
		} else {
			nascondiSelezione();
		}
	}

	@Override
	protected void setDefaultRequiredMessage() {
		requiredDecoration.setDescriptionText(TextField.DEAFULT_REQUIRED_MESSAGE);
	}

	@Override
	protected void setDefaultErrorMessage() {
		wrongDecoration.setDescriptionText(TextField.DEFAULT_ERROR_MESSAGE);
	}
	
	@Override
	public void resetValue() {
		setText("");
		dirty = false;
		value = null;
		validate();
	}

}
