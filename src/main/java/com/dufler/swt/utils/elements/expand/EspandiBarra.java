package com.dufler.swt.utils.elements.expand;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;

public class EspandiBarra extends ExpandBar {
	
	private final Composite parent;

	public EspandiBarra(Composite parent) {
		super(parent, SWT.V_SCROLL);
		
		this.parent = parent;
		
		addExpandListener(new ExpandListener() {

			@Override
			public void itemCollapsed(ExpandEvent e) {
				ridimensiona();		
			}

			@Override
			public void itemExpanded(ExpandEvent e) {
				ridimensiona();				
			}
			
		});
	}
	
	private void ridimensiona() {
		Display.getCurrent().asyncExec(new Runnable(){
            public void run()
            {
            	parent.layout();
            }
        });
	}
	
	@Override
	protected void checkSubclass() {
		//DO NOTHING!
	}

}
