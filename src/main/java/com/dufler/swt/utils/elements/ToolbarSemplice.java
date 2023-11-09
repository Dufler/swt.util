package com.dufler.swt.utils.elements;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

public abstract class ToolbarSemplice<T extends ITabella<U, ?>, U> extends Composite {
	
	protected T tabella;
	
	protected ToolBar toolbar;
	
	protected Composite compositeSx;
	protected Composite compositeDx;
	
	protected boolean toolbarCostruita;

	public ToolbarSemplice(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(2, false));
		
		creaCompositeSx();
		
		aggiungiElementi(compositeSx);
		
		creaCompositeDx();
		
		setupTastiToolbar();
		
		toolbar.pack();
		
		toolbarCostruita = true;
		
		//Controllo sui permessi.
		boolean abilita = isAbilitato();
		abilita(abilita);
	}
	
	protected void creaCompositeSx() {
		compositeSx = new Composite(this, SWT.NONE);
		compositeSx.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeSx.setLayout(new GridLayout(1, false));
	}
	
	protected void creaCompositeDx() {
		compositeDx = new Composite(this, SWT.NONE);
		compositeDx.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeDx.setLayout(new GridLayout(1, false));
		
		toolbar = new ToolBar(compositeDx, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
	}
	
	protected abstract boolean isAbilitato();

	public void setTabella(T tabella) {
		this.tabella = tabella;
	}
	
	/**
	 * Da raffinare se si intende aggiungere elementi alla sinistra della toolbar.
	 */
	protected void aggiungiElementi(Composite composite) {
		//DO NOTHING!		
	}

	/**
	 * Metodo da implementare dove si possono sfruttare i metodi già esistenti come <code>aggiungiTastoNuovo</code> per determinare quali tasti mostrare.
	 */
	protected abstract void setupTastiToolbar();
	
	protected abstract void abilita(boolean abilita);
	
	@Override
	protected void checkSubclass() {
		//DO NOTHING!
	}

}
