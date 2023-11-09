package com.dufler.swt.utils.composite.common;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.dufler.swt.utils.composite.GruppoSemplice;
import com.dufler.swt.utils.decoration.SpacerLabel;
import com.dufler.swt.utils.input.Bottone;
import com.dufler.swt.utils.input.DateField;
import com.dufler.swt.utils.validation.ParentValidationHandler;

public class CompositeDate extends GruppoSemplice {
	
	private Bottone btnIndifferente;
	private DateField da;
	private DateField a;
	
	private boolean opzionale;

	public CompositeDate(ParentValidationHandler parentValidator, Composite parent, boolean opzionale) {
		super(parentValidator, parent);
		this.opzionale = opzionale;
		if (!opzionale) {
			btnIndifferente.dispose();
			layout();
		}
	}

	@Override
	public void aggiungiElementiGrafici() {
		setLayout(new GridLayout(7, false));
		
		btnIndifferente = new Bottone(this, SWT.CHECK);
		btnIndifferente.setText("Indifferente");
		btnIndifferente.setRequired(false);
		btnIndifferente.setSelection(true);
		addChild(btnIndifferente);
		
		Label lblDa = new Label(this, SWT.NONE);
		lblDa.setText("Da: ");
		
		da = new DateField(this);
		da.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (opzionale) {
					btnIndifferente.setSelection(false);
				}
			}
		});
		addChild(da);
		
		new SpacerLabel(this);
		
		Label lblA = new Label(this, SWT.NONE);
		lblA.setText("A: ");
		
		a = new DateField(this);
		a.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (opzionale) {
					btnIndifferente.setSelection(false);
				}
			}
		});
		addChild(a);
		
		new SpacerLabel(this);
		new Label(this, SWT.NONE);
	}
	
	@Override
	public boolean validate() {
		super.validate();
		if (valid) {
			boolean indifferente = btnIndifferente != null && !btnIndifferente.isDisposed() ? btnIndifferente.getSelection() : true;
			boolean campiPronti = da != null && a != null;
			Date dataDa = campiPronti ? da.getValue() : null;
			Date dataA = campiPronti ? a.getValue() : null;
			boolean data = campiPronti && dataDa != null && dataA != null ? (dataDa.compareTo(dataA) < 0) : true;
			valid = opzionale ? (indifferente || data) : (indifferente && data);
		}
		forwardValidation();
		return valid;
	}
	
	public Date getDaSoloGiorno() {
		return da.getSimpleStartValue();
	}
	
	public Date getDa() {
		return da.getValue();
	}
	
	public void setDa(Date value) {
		da.setValue(value);
	}
	
	public void setDa(Calendar value) {
		da.setValue(value);
	}
	
	public Date getASoloGiorno() {
		return a.getSimpleEndValue();
	}
	
	public Date getA() {
		return a.getValue();
	}
	
	public void setA(Date value) {
		a.setValue(value);
	}
	
	public void setA(Calendar value) {
		a.setValue(value);
	}
	
	@Override
	public void resetValues() {
		super.resetValues();
		btnIndifferente.setSelection(true); //Oltre al solito mi assicuro che il tasto indifferente sia selezionato perchè di default è così.
	}
	
	public boolean getIndifferente() {
		boolean indifferente = false;
		if (opzionale)
			indifferente = btnIndifferente.getSelection();
		return indifferente;
	}

	public void setInizioEFineMeseCorrente() {
		GregorianCalendar today = new GregorianCalendar();
		//Inizio mese
		today.set(Calendar.DAY_OF_MONTH, 1);
		Date inizioMese = today.getTime();
		da.setValue(inizioMese);
		//Fine mese
		int daysInMonth = getDayInMonth(today.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, daysInMonth);
		Date fineMese = today.getTime();
		a.setValue(fineMese);
	}
	
	public void setInizioEFineMesePrecedente() {
		GregorianCalendar today = new GregorianCalendar();
		//Inizio mese
		today.set(Calendar.DAY_OF_MONTH, 1);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		int mese = today.get(Calendar.MONTH) - 1;
		//Controllo che non si stia fatturando Gennaio, in quel caso sposto a dicembre dell'anno precedente.
		if (mese < 0) {
			mese = 11;
			today.set(Calendar.YEAR, today.get(Calendar.YEAR) - 1);
		}
		today.set(Calendar.MONTH, mese);
		Date inizioMese = today.getTime();
		da.setValue(inizioMese);
		//Fine mese
		int daysInMonth = getDayInMonth(today.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, daysInMonth);
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);
		today.set(Calendar.MILLISECOND, 0);
		Date fineMese = today.getTime();
		a.setValue(fineMese);
	}
	
	private int getDayInMonth(int month) {
		int days;
		switch (month) {
			case Calendar.FEBRUARY : days = 28; break;
			case Calendar.APRIL : days = 30; break;
			case Calendar.JUNE : days = 30; break;
			case Calendar.SEPTEMBER : days = 30; break;
			case Calendar.NOVEMBER : days = 30; break;
			default : days = 31;
		}
		return days;
	}

}
