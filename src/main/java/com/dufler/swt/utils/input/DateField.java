package com.dufler.swt.utils.input;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.validation.ParentValidationHandler;
import com.dufler.swt.utils.validation.ValidationHandler;

public class DateField extends DateTime implements InputElement, ValidationHandler {
	
	public static final String MESSAGE_REQUIRED = "L'inserimento di un valore \u00E8 obbligatorio.";
	public static final String MESSAGE_NOT_USED = "Il valore non \u00E8 stato inserito e non verr\u00E0 utilizzato.";
	
	//private Date data;
	private Calendar data;
	
	private boolean required;
	private boolean dirty;
	private boolean valid;
	
	private ParentValidationHandler successor;
	private final ControlDecoration requiredDecoration;
	private final ControlDecoration nonUsedDecoration;
	private final boolean showNonUsedDecoration;
	
	public DateField(Composite parent) {
		this(parent, true);
	}
	
	public DateField(Composite parent, boolean showNonUsed) {
		super(parent, SWT.DROP_DOWN | SWT.BORDER);
		required = true;
		dirty = false;
		
		Image imageRequired = Immagine.YELLOW_MARK_12X20.getImage();
		requiredDecoration = new ControlDecoration(this, SWT.RIGHT);
		requiredDecoration.setImage(imageRequired);
		requiredDecoration.setDescriptionText(MESSAGE_REQUIRED);
		
		Image imageNonUsed = Immagine.ORANGE_MARK_12X20.getImage();
		nonUsedDecoration = new ControlDecoration(this, SWT.RIGHT);
		nonUsedDecoration.setImage(imageNonUsed);
		nonUsedDecoration.setDescriptionText(MESSAGE_NOT_USED);
		showNonUsedDecoration = showNonUsed;
		
		hideDecorations();
		addDirtyListener();
		
		if (parent instanceof ParentValidationHandler) {
			successor = (ParentValidationHandler) parent;
			successor.addChild(this);
		} else {
			successor = null;
		}
		
		//valid = true;
		validate();
	}
	
	private void addDirtyListener() {
		addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dirty = true;	
				validate();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				dirty = true;				
			}});
	}
	
	public void checkSubclass() {
		//DO NOTHING!
	}
	
	private void showRequiredDecoration() {
		requiredDecoration.show();
	}
	
	private void showNonUsedDecoration() {
		nonUsedDecoration.show();
	}
	
	private void hideDecorations() {
		requiredDecoration.hide();
		nonUsedDecoration.hide();
	}

	@Override
	public boolean isValid() {
		return valid;
	}
	
	private void copyValues() {
		int year = data.get(Calendar.YEAR);
		setYear(year);
		int month = data.get(Calendar.MONTH);
		setMonth(month);
		int day = data.get(Calendar.DATE);
		setDay(day);
		int hours = data.get(Calendar.HOUR_OF_DAY);
		setHours(hours);
		int minutes = data.get(Calendar.MINUTE);
		setMinutes(minutes);
		int seconds = data.get(Calendar.SECOND);
		setSeconds(seconds);
	}
	
	public void setValue(Date date) {
		if (date != null) {
			dirty = true;
			data = new GregorianCalendar();
			data.setTime(date);
			copyValues();
		} else {
			data = null;
		}
		validate();
	}
	
	public void setValue(Calendar calendar) {
		if (calendar != null) {
			dirty = true;
			data = calendar;
			copyValues();
		} else {
			data = null;
		}
		validate();
	}
	
	public Date getValue() {
		return dirty && data != null ? data.getTime() : null; //Ho aggiunto la condizione di dirty affinchè restituisca un null se non è stato in qualche modo impostato
	}
	
	public Date getSimpleStartValue() {
		return dirty && data != null ? getSimpleCalendarStart().getTime() : null;
	}
	
	public Date getSimpleEndValue() {
		return dirty && data != null ? getSimpleCalendarEnd().getTime() : null;
	}
	
	private Calendar getCalendar() {
		int year = getYear();
		int month = getMonth();
		int day = getDay();
		int hours = getHours();
		int minutes = getMinutes();
		int seconds = getSeconds();
		Calendar c = Calendar.getInstance();
		c.set(year, month, day, hours, minutes, seconds);
		return c;
	}
	
	private Calendar getSimpleCalendarStart() {
		data.set(Calendar.HOUR_OF_DAY, 0);
		data.set(Calendar.MINUTE, 0);
		data.set(Calendar.SECOND, 0);
		data.set(Calendar.MILLISECOND, 0);
		return data;
	}
	
	private Calendar getSimpleCalendarEnd() {
		data.set(Calendar.HOUR_OF_DAY, 23);
		data.set(Calendar.MINUTE, 59);
		data.set(Calendar.SECOND, 59);
		data.set(Calendar.MILLISECOND, 0);
		return data;
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
	public void setParent(ParentValidationHandler parent) {
		successor = parent;		
	}

	@Override
	public boolean validate() {
		hideDecorations();
		valid = true;
		data = dirty ? getCalendar() : null;
		if (required) {
			if (data == null) {
				valid = false;
				showRequiredDecoration();
			}
		} else {
			if (data == null && showNonUsedDecoration)
				showNonUsedDecoration();
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
	public void resetValue() {
		Date defaultValue = null; //Prima era new Date();
		setValue(defaultValue);
		dirty = false;
	}

}
