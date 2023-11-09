package com.dufler.swt.utils.elements;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolItem;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.elements.table.filter.CriteriFiltraggio;
import com.dufler.swt.utils.input.InputElement;
import com.dufler.swt.utils.input.TextForString;

/**
 * Toolbar da abbinare ad una tabella CRUD con filtri.
 * @author Damiano
 *
 * @param <T> La tabella.
 * @param <U> La classe di oggetti da manipolare.
 * @param <V> La classe che rappresenta i criteri di filtraggio.
 */
public abstract class ToolbarCRUDConFiltro<T extends ITabellaCRUD<U> & ITabella<U, C>, U, C extends CriteriFiltraggio> extends ToolbarCRUD<T, U> {
	
	protected static final int DEFAULT_FILTER_WIDTH = 150;
	
	protected ToolItem filtra;
	protected ToolItem annullaFiltro;
	
	protected TextForString filterText;

	public ToolbarCRUDConFiltro(Composite parent) {
		super(parent);
	}
	
	@Override
	protected void aggiungiElementi(Composite composite) {
		setupFiltri(composite);
	}
	
	protected List<InputElement> trovaElementiFiltro(Composite parent) {
		List<InputElement> elements = new LinkedList<>();
		for (Control control : parent.getChildren()) {
			if (control instanceof InputElement) {
				InputElement element = (InputElement) control;
				elements.add(element);
			} else if (control instanceof Composite) {
				Composite composite = (Composite) control;
				elements.addAll(trovaElementiFiltro(composite));
			}
		}
		return elements;
	}
	
	/**
	 * Metodo da implementare dove si possono sfruttare i metodi già esistenti come <code>aggiungiTastoNuovo</code> per determinare quali tasti mostrare.
	 */
	protected abstract void setupFiltri(Composite composite);
	
	/**
	 * Metodo da implementare, eseguirà il filtraggio in base a quanto inserito nella casella di testo.
	 */
	protected final void filtra() {
		C criteri = getCriteriDiFiltraggio();
		tabella.filtra(criteri);
	}
	
	protected abstract C getCriteriDiFiltraggio();
	
	/**
	 * Metodo da implementare, annullerà il filtraggio.
	 */
	protected void annullaFiltro() {
		tabella.annullaFiltro();
		resetCampiFiltri();
	}
	
	protected final void resetCampiFiltri() {
		List<InputElement> elements = trovaElementiFiltro(this);
		for (InputElement element : elements) {
			element.resetValue();
		}
	}
	
	/**
	 * Metodo da sovrascrivere se si vuole una larghezza diversa per la barra di ricerca.
	 */
	protected int getFilterWidth() {
		return DEFAULT_FILTER_WIDTH;
	}
	
	protected void aggiungiCampoDiTestoGenericoPerFiltro(Composite composite) {
		filterText = new TextForString(composite);
		filterText.setToolTipText("Cerca...");
		filterText.setMessage("Cerca...");
		GridData layoutFiltro = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		layoutFiltro.widthHint = getFilterWidth();
		filterText.setLayoutData(layoutFiltro);
		filterText.setRequired(false);
		filterText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.CR || e.character == SWT.LF) {
					filtra();
				}
			}
		});
		filterText.addListener(SWT.Traverse, event -> {
			if (event.character == SWT.CR || event.character == SWT.LF)
				event.doit = false;
		});
	}
	
	protected void aggiungiTastoFiltra() {
		aggiungiTastoFiltra(false);
	}
	
	protected void aggiungiTastoFiltra(boolean testo) {
		filtra = new ToolItem(toolbar, SWT.NONE);
		filtra.setImage(Immagine.LENTE_16X16.getImage());
		filtra.setText(testo ? "Filtra" : "");
		filtra.setToolTipText("Filtra");
		filtra.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				filtra();
				//Imposto il focus sul campo di testo per prevenire scroll scomodi in alcuni casi.
				if (filterText != null)
					filterText.setFocus();
			}
		});
	}
	
	protected void aggiungiTastoAnnullaFiltra() {
		aggiungiTastoAnnullaFiltra(false);
	}
	
	protected void aggiungiTastoAnnullaFiltra(boolean testo) {
		annullaFiltro = new ToolItem(toolbar, SWT.NONE);
		annullaFiltro.setImage(Immagine.LENTE_ANNULLATA_16X16.getImage());
		annullaFiltro.setText(testo ? "Annulla filtro" : "");
		annullaFiltro.setToolTipText("Annulla filtro");
		annullaFiltro.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				annullaFiltro();
			}
		});
	}
	
	@Override
	public final void abilita(boolean abilita) {
		super.abilita(abilita);
		//Inutile andare avanti se la toolbar non è stata ancora costruita.
		if (toolbarCostruita) {
			if (filtra != null)
				filtra.setEnabled(abilita);
			if (annullaFiltro != null)
				annullaFiltro.setEnabled(abilita);
			if (filterText != null)
				filterText.setEnabled(abilita);
			abilitaAltriElementi(abilita);
		}
	}
	
	protected final void abilitaAltriElementi(boolean abilita) {
		List<InputElement> elements = trovaElementiFiltro(this);
		for (InputElement element : elements) {
			element.setEnabled(abilita);
		}
	}

}
