package com.dufler.swt.utils.elements.expand;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandItem;

public class EspandiElemento extends ExpandItem {

	public EspandiElemento(EspandiBarra parent, Composite composite, String text) {
		super(parent, SWT.NONE);
		setContenuto(composite);
		setText(text);
	}
	
	@Override
	protected void checkSubclass() {
		//DO NOTHING!
	}
	
	private void setContenuto(Composite composite) {
		setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		setControl(composite);
	}

}
