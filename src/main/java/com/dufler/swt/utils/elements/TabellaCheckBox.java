package com.dufler.swt.utils.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.elements.table.filter.CriteriFiltraggio;
import com.dufler.swt.utils.elements.table.filter.FiltroTabella;

public abstract class TabellaCheckBox<T, C extends CriteriFiltraggio> extends CheckboxTableViewer implements ITabella<T, C> {
	
	public static final String DO_NOT_SHOW = " - ";
	
	public static final int STILE_SELEZIONE_MULTIPLA = SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.CHECK;
	public static final int STILE_SELEZIONE_SINGOLA = SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE | SWT.CHECK;
	public static final int STILE_TABELLE_GRANDI_SELEZIONE_MULTIPLA = SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.MULTI | SWT.CHECK;
	public static final int STILE_TABELLE_GRANDI_SELEZIONE_SINGOLA = SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.SINGLE | SWT.CHECK;
	
	private static final String TAB = "\t";
	private static final String NEW_LINE = "\n"; 

	protected boolean dirty;
	protected Table table;
	protected MenuItem copy;
	protected MenuItem selectAll;
	protected Menu menuPopup;
	protected Ordinatore<T> sorter;
	
	protected FiltroTabella<T, C> filter;
	protected CriteriFiltraggio criteri;
	
	protected boolean selectAllItem;
	
	protected final int preferredHeight;
	protected final HashMap<Integer, TableViewerColumn> mappaColonne;
	
	public TabellaCheckBox(Composite parent) {
		this(parent, STILE_SELEZIONE_MULTIPLA);
	}
	
	public TabellaCheckBox(Composite parent, int style) {
		super(new Table(parent, style));
		this.preferredHeight = getPreferredHeight();
		mappaColonne = new HashMap<Integer, TableViewerColumn>();
		table = getTable();
		menuPopup = new Menu(table);
		table.setMenu(menuPopup);
		setContentProvider(ArrayContentProvider.getInstance());
		if (style == STILE_SELEZIONE_SINGOLA)
			aggiungiListenerSelezioneSingola();
		aggiungiListenerCopia();
		aggiungiMenuBase();
		setOrdinamento();
		setFiltro();
		aggiungiListener();
		aggiungiMenu(menuPopup);
		
		dirty = false;
		
		ColumnViewerToolTipSupport.enableFor(this);
		
		impostaTabella(table);
		
		aggiungiColonne();
		
		aggiornaContenuto();
	}
	
	protected int getPreferredHeight() {
		return SWT.DEFAULT;
	}
	
	/**
	 * Imposta i valori di default su come rappresentare la tabella.
	 */
	protected void impostaTabella(Table table) {
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData layout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		layout.heightHint = preferredHeight;
		table.setLayoutData(layout);
	}
	
	/**
	 * metodo da estendere dove andare a definire le colonne da mostrare.<br>
	 * Viene richiamato nel costruttore.
	 */
	protected abstract void aggiungiColonne();

	/**
	 * Metodo da estendere per tutte le tabelle in grado di gestire in autonomia il proprio contenuto.
	 */
	public abstract void aggiornaContenuto();

	/**
	 * Nel corpo di questo metodo va implementato l'ordinamento della tabella.
	 * Usualmente viene abbinato al viewer un oggetto <code>ViewerComparator</code>
	 * La classe <code>Ordinatore</code> nello stesso package fornisce un template da poter riciclare.
	 */
	protected final void setOrdinamento() {
		sorter = creaOrdinatore();
		if (sorter != null) {
			setComparator(sorter);
		} else {
			setComparator(new ViewerComparator());
		}
	}
	
	protected abstract Ordinatore<T> creaOrdinatore();
	
	protected abstract Etichettatore<T> creaEtichettatore();
	
	protected abstract ModificatoreValoriCelle<T> creaModificatore();
	
	/**
	 * Aggiunge una colonna alla tabella, vanno specificati il nome, la larghezza di default e il provider del contenuto, se ce l'ha.
	 * @param nome
	 * @param larghezza
	 * @param provider
	 */
	public void aggiungiColonna(String nome, int larghezza, int indiceColonna) {
		aggiungiColonna(nome, larghezza, creaEtichettatore(), indiceColonna);
	}
	
	/**
	 * Aggiunge una colonna alla tabella, vanno specificati il nome, la larghezza di default e il provider del contenuto, se ce l'ha.
	 * @param nome
	 * @param larghezza
	 * @param provider
	 */
	public void aggiungiColonna(String nome, int larghezza, Etichettatore<T> provider, int indiceColonna) {
		aggiungiColonna(nome, larghezza, provider, null, indiceColonna, SWT.NONE);
	}
	
	/**
	 * Aggiunge una colonna alla tabella, vanno specificati il nome, la larghezza di default e il provider del contenuto, se ce l'ha.
	 * @param nome
	 * @param larghezza
	 * @param provider
	 */
	public void aggiungiColonna(String nome, int larghezza, Etichettatore<T> provider, ModificatoreValoriCelle<T> modificatore, int indiceColonna) {
		aggiungiColonna(nome, larghezza, provider, modificatore, indiceColonna, SWT.NONE);
	}
	
	/**
	 * Aggiunge una colonna alla tabella, vanno specificati il nome, la larghezza di default e il provider del contenuto, se ce l'ha.
	 * @param nome
	 * @param larghezza
	 * @param provider
	 */
	public void aggiungiColonna(String nome, int larghezza, Etichettatore<T> provider, ModificatoreValoriCelle<T> modificatore, int indiceColonna, int style) {
		
		//Controllo sui parametri
		if (nome == null)
			nome = "";
		if (larghezza < 0)
			larghezza = 0;
		
		TableViewerColumn colonna = new TableViewerColumn(this, style);
		
		TableColumn tblclmn = colonna.getColumn();
		tblclmn.setWidth(larghezza);
		tblclmn.setText(nome);
		
		if (provider == null) {
			colonna.setLabelProvider(new ColumnLabelProvider());
		} else {
			provider.setColumnIndex(indiceColonna);
			colonna.setLabelProvider(provider);
		}
		
		if (modificatore != null) {
			modificatore.setColumnIndex(indiceColonna);
			colonna.setEditingSupport(modificatore);
		}
		
		if (sorter != null)
			tblclmn.addSelectionListener(new ListenerColonna<T>(indiceColonna, tblclmn, getTable(), sorter, this));
	}

	/**
	 * Nel corpo di questo metodo vanno aggiunti Listener particolari per raffinare il comportamento della tabella
	 */
	protected void aggiungiListener() {};
	
	/**
	 * Metodo da estendere nel caso in cui si voglia aggiungere menù specifici per la tabella checkbox.
	 */
	protected void aggiungiMenu(Menu menu) {}
	
	/**
	 * Aggiunge un listener che impedisce la selezione multipla.
	 */
	protected void aggiungiListenerSelezioneSingola() {
		addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(final CheckStateChangedEvent event) {
				setCheckedElements(new Object[] { event.getElement() });
			}
		});
	}
	
	protected void aggiungiListenerCopia() {
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'c')) {
					copiaSelezione();
                }
			}
		});
	}
	
	protected void aggiungiMenuBase() {
		aggiungiVoceMenuCopia();
		aggiungiMenuSelezione();
	}

	protected void aggiungiVoceMenuCopia() {
	    copy = new MenuItem(menuPopup, SWT.PUSH);
	    copy.setText("Copia tabella");
	    copy.setImage(Immagine.COPIA_16X16.getImage());
	    copy.addListener(SWT.Selection, new Listener() {
	    	public void handleEvent(Event event) {
	    		int selectionIndex = table.getSelectionIndex();
	    		if (selectionIndex != -1) {
	    			copiaSelezione();
	    		}
	    	}
	    });	
	}
	
	protected void aggiungiMenuSelezione() {
	    selectAll = new MenuItem(menuPopup, SWT.PUSH);
	    selectAll.setText("Seleziona Tutti");
	    selectAll.setImage(Immagine.SPUNTAVERDE_16X16.getImage());
	    selectAll.addListener(SWT.Selection, new Listener() {
	    	public void handleEvent(Event event) {
	    		if (selectAllItem) {
	    			deselezionaTutto();
	    		} else {
	    			selezionaTutto();
	    		}
	    	}
	    });	
	}
	
	public void deselezionaTutto() {
		setAllChecked(false);
		selectAll.setText("Seleziona Tutti");
		selectAll.setImage(Immagine.SPUNTAVERDE_16X16.getImage());
		selectAllItem = false;
		table.notifyListeners(SWT.Selection, new Event());
	}
	
	public void selezionaTutto() {
		setAllChecked(true);
		selectAll.setText("Deseleziona Tutti");
		selectAll.setImage(Immagine.CROCEROSSA_16X16.getImage());
		selectAllItem = true;
		table.notifyListeners(SWT.Selection, new Event());
	}
	
	private String getSelectedText() {
		StringBuilder textData = new StringBuilder();
		//Aggiungo i nomi delle colonne
		int columns = table.getColumnCount();
		for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
			textData.append(table.getColumn(columnIndex).getText());
			textData.append(TAB);
		}
		textData.setLength(textData.length() - 1);
		textData.append(NEW_LINE);
		//Aggiungo il contenuto selezionato
		for (TableItem item : table.getItems()) {
			if (item.getChecked()) {
				for (int index = 0; index < columns; index++) {
					textData.append(item.getText(index));
					textData.append(TAB);
				}
				textData.setLength(textData.length() - 1);
				textData.append(NEW_LINE);
			}			
		}
		textData.setLength(textData.length() - 1);
		return textData.toString();
	}

	@Override
	public void copiaSelezione() {
		String textData = getSelectedText();
		Clipboard clipboard = new Clipboard(Display.getDefault());
		TextTransfer textTransfer = TextTransfer.getInstance();
		Transfer[] transfers = new Transfer[] { textTransfer };
		Object[] data = new Object[] { textData };
		clipboard.setContents(data, transfers);
		clipboard.dispose();
		System.out.println("Testo copiato.");
	}
	
	protected void copiaSelezioneConRichText() {
		String textData = getSelectedText();
		Clipboard clipboard = new Clipboard(Display.getDefault());
		String rtfData = "{\\rtf1\\b\\i " + textData + "}";
		RTFTransfer rtfTransfer = RTFTransfer.getInstance();
		TextTransfer textTransfer = TextTransfer.getInstance();
		Transfer[] transfers = new Transfer[] { textTransfer, rtfTransfer };
		Object[] data = new Object[] { textData, rtfData };
		clipboard.setContents(data, transfers);
		clipboard.dispose();
		System.out.println("Testo rich copiato.");
	}
	
	public void aggiustaLarghezzaColonne() {
		refresh();
		AggiustaLarghezzaColonne runnable = new AggiustaLarghezzaColonne(table);
		table.getDisplay().asyncExec(runnable);
	}
	
	@Override
	public void setElementi(Collection<T> items) {
		setInput(items);
		aggiustaLarghezzaColonne();
	}
	
	@Override
	public void setElementi(T[] items) {
		setInput(items);
		aggiustaLarghezzaColonne();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getRigaSelezionata() {
		T item = null;
		Object[] selezione = getCheckedElements();
		if (selezione.length > 0) {
			item = (T) selezione[0];
		}
		return item;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getRigheSelezionate() {
		Object[] selezione = getCheckedElements();
		List<T> lista = new ArrayList<>();
		for (Object object : selezione) {
			T item = (T) object;
			lista.add(item);
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getElementiNonSelezionati() {
		TableItem[] children = getTable().getItems();
		List<T> lista = new ArrayList<>(children.length);
		for (TableItem item : children) {
			Object data = item.getData();
			if (data != null) {
				if (!item.getChecked()) {
					lista.add((T) data);
				}
			}
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getElementi() {
		TableItem[] children = getTable().getItems();
		List<T> lista = new ArrayList<>(children.length);
		for (TableItem item : children) {
			Object data = item.getData();
			if (data != null) {
				lista.add((T) data);
			}
		}
		return lista;
	}
	
	/**
	 * Indica se qualcuno ha modificato i dati presenti nella tabella.<br>
	 * Ogni tabella a cui interessa questa informazione deve gestirsi in autonomia questo stato.
	 * @return
	 */
	public boolean isDirty() {
		return dirty;
	}
	
	/**
	 * Nel corpo di questo metodo va implementato il filtraggio degli elementi in tabella.
	 * Usualmente viene abbinato al viewer un oggetto <code>ViewerFilter</code>
	 */
	protected void setFiltro() {
		filter = creaFiltro();
		if (filter != null)
			setFilters(filter);
	}
	
	/**
	 * Metodo astratto che deve essere implementato per restituire l'oggetto responsabile del filtraggio della tabella.
	 * @return
	 */
	protected abstract FiltroTabella<T, C> creaFiltro();
	
	/**
	 * Passa i criteri al filtro e aggiorna gli elementi mostrati nella tabella.
	 * @param criteri
	 */
	@Override
	public void filtra(C criteri) {
		if (filter != null) {
			filter.setCriteri(criteri);
			refresh();
			aggiustaLarghezzaColonne();
		}
	}
	
	/**
	 * Annnulla i criteri di filtraggio precedentemente impostati.
	 */
	@Override
	public void annullaFiltro() {
		if (filter != null) {
			filter.resetCriteri();
			refresh();
			aggiustaLarghezzaColonne();
		}
	}
	
}
