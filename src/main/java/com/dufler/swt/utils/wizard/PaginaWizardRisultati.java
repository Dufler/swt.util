package com.dufler.swt.utils.wizard;

public abstract class PaginaWizardRisultati extends PaginaWizard {
	
	protected PaginaWizardRisultati(String title, String description, boolean lastPage) {
		super(title, description, lastPage);
	}
	
	public abstract void mostraRisultato();
	
	@Override
	public void copyDataToModel() {
		//DO NOTHING! (Di solito, può essere esteso per risolvere casi specifici)		
	}

}
