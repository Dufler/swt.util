package com.dufler.swt.utils.elements;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
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

/**
 * Questa classe viene estesa per realizzare tabelle con semplici funzionalità.
 * @author Damiano Bellucci - damiano.bellucci@gmail.com
 *
 */
public abstract class Tabella<T, C extends CriteriFiltraggio> extends TableViewer implements ITabella<T, C> {
	
	/**
	 * Contiene i possibili stili applicabili alla tabella.
	 * @author Damiano
	 *
	 */
	public enum StileTabella {
		
		STILE_SEMPLICE(SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI),
		STILE_SELEZIONE_SINGOLA(SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE),
		STILE_TABELLE_GRANDI(SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.MULTI),
		STILE_TABELLE_GRANDI_SELEZIONE_SINGOLA(SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.SINGLE);
		
		private final int style;
		
		private StileTabella(int style) {
			this.style = style;
		}
		
		public int getStyle() {
			return style;
		}
		
	}
	
	public static final int STILE_SEMPLICE = SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI;
	public static final int STILE_SELEZIONE_SINGOLA = SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE;
	public static final int STILE_TABELLE_GRANDI = SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.MULTI;
	public static final int STILE_TABELLE_GRANDI_SELEZIONE_SINGOLA = SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.SINGLE;
	
	protected static final String TAB = "\t";
	protected static final String NEW_LINE = "\n"; 
	
	protected List<T> elementi;
	
	protected boolean dirty;
	protected Table table;
	protected Menu menuPopup;
	protected MenuItem copy;
	protected MenuItem selectAll;
	
	protected Ordinatore<T> sorter;
	
	protected FiltroTabella<T, C> filter;
	protected CriteriFiltraggio criteri;
	
	protected final Set<ModificatoreValoriCelle<T>> editors;
	
	protected final HashMap<Integer, TableViewerColumn> mappaColonne;
	
	protected final int preferredHeight;

	public Tabella(Composite parent, int style) {
		super(parent, style);
		this.mappaColonne = new HashMap<Integer, TableViewerColumn>();
		this.editors = new HashSet<>();
		this.preferredHeight = getPreferredHeight();
		this.table = getTable();
		impostaTabella(table);
		this.menuPopup = new Menu(table);
		this.table.setMenu(menuPopup);
		setContentProvider(ArrayContentProvider.getInstance());
		aggiungiListenerBase();
		aggiungiMenuBase();
		aggiungiListenerMenuAComparsa();
		setOrdinamento();
		setFiltro();
		aggiungiListener();
		aggiungiMenu(menuPopup);
		this.dirty = false;
		elementi = new LinkedList<>();
		ColumnViewerToolTipSupport.enableFor(this);
		
		if (style == STILE_TABELLE_GRANDI || style == STILE_TABELLE_GRANDI_SELEZIONE_SINGOLA)
			setUseHashlookup(true);
		
		if (style == STILE_TABELLE_GRANDI || style == STILE_SEMPLICE)
			aggiungiListenerSelezionaTutti();
		
		aggiungiColonne();
		
		aggiornaContenuto();
	}
	
	protected int getPreferredHeight() {
		return SWT.DEFAULT;
	}
	
	protected void aggiungiListenerMenuAComparsa() {
		menuPopup.addMenuListener(new MenuListener() {

			@Override
			public void menuHidden(MenuEvent e) {
				//DO NOTHING!
			}

			@Override
			public void menuShown(MenuEvent e) {
				mostraVociSpecificheMenu(menuPopup);
			}
		});
		
	}

	/**
	 * Questo metodo va sovrascritto dalle tabelle con un menù a comparsa con voci specifiche in base all'elemento selezionato.
	 * @param menu
	 */
	protected void mostraVociSpecificheMenu(Menu menu) {
		//DO NOTHING!		
	}

	protected void aggiungiListenerSelezionaTutti() {
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'a')) {
					selectAll();
                }
			}
		});
	}
	
	protected void selectAll() {
		this.doSetSelection(this.doGetItems());
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
	 * Gli stili preimpostati per la tabella sono: BORDER, FULL_SELECTION, MULTI.
	 * @param parent
	 */
	public Tabella(Composite parent) {
		this(parent, STILE_SEMPLICE);
	}
	
	/**
	 * metodo da estendere dove andare a definire le colonne da mostrare.<br>
	 * Viene richiamato nel costruttore.
	 */
	protected abstract void aggiungiColonne();

	
	@Override
	public final void aggiornaContenuto() {
		Collection<T> contenuto = elaboraContenutoInAutonomia();
		if (contenuto != null)
			setElementi(contenuto);
		refresh();
	}
	
	/**
	 * Metodo da estendere per tutte le tabelle in grado di gestire in autonomia il proprio contenuto.
	 */
	protected Collection<T> elaboraContenutoInAutonomia() {
		return null;
	}

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
	
	public void aggiungiColonnaVuota() {
		ColumnLabelProvider provider = new ColumnLabelProvider() {
			public String getText(Object element) {
				return null;
			}
		};
		TableViewerColumn colonna = new TableViewerColumn(this, SWT.NONE);
		TableColumn tblclmn = colonna.getColumn();
		tblclmn.setToolTipText(DO_NOT_SHOW);
		tblclmn.setWidth(0);
		tblclmn.setText("");
		tblclmn.setResizable(false);
		tblclmn.setMoveable(false);
		colonna.setLabelProvider(provider);
	}
	
	/**
	 * Aggiunge una colonna separatrice alla tabella con larghezza fissa.
	 */
	public void aggiungiColonnaSeparazione() {
		ColumnLabelProvider provider = new ColumnLabelProvider() {
			public String getText(Object element) {
				return "    ";
			}
		};
		TableViewerColumn colonna = new TableViewerColumn(this, SWT.NONE);
		TableColumn tblclmn = colonna.getColumn();
		tblclmn.setToolTipText(DO_NOT_SHOW);
		tblclmn.setWidth(50);
		tblclmn.setText("");
		tblclmn.setResizable(false);
		colonna.setLabelProvider(provider);
	}
	
	/**
	 * Aggiunge una colonna alla tabella, vanno specificati il nome, la larghezza di default e il provider del contenuto, se ce l'ha.
	 * @param nome
	 * @param larghezza
	 * @param provider
	 */
	public void aggiungiColonna(String nome, int larghezza, int indiceColonna) {
		aggiungiColonna(nome, larghezza, creaEtichettatore(), indiceColonna, SWT.NONE);
	}
	
	public void aggiungiColonna(String nome, int larghezza, int indiceColonna, int style) {
		aggiungiColonna(nome, larghezza, creaEtichettatore(), indiceColonna, SWT.NONE);
	}
	
	public void aggiungiColonna(String nome, int larghezza, Etichettatore<T> provider, int indiceColonna) {
		aggiungiColonna(nome, larghezza, provider, indiceColonna, SWT.NONE);
	}
	
	/**
	 * Aggiunge una colonna alla tabella, vanno specificati lo stile, il nome, la larghezza di default e il provider del contenuto, se ce l'ha.
	 * @param nome
	 * @param larghezza
	 * @param provider
	 */
	public void aggiungiColonna(String nome, int larghezza, Etichettatore<T> provider, int indiceColonna, int style) {
		
		//Controllo sui parametri
		if (nome == null)
			nome = "";
		if (larghezza < 0)
			larghezza = 0;
		
		TableViewerColumn colonna = new TableViewerColumn(this, style);
		mappaColonne.put(indiceColonna, colonna);
		
		TableColumn tblclmn = colonna.getColumn();
		tblclmn.setWidth(larghezza);
		tblclmn.setText(nome);
		
		//Etichettatore<T> provider = creaEtichettatore();
		if (provider == null) {
			colonna.setLabelProvider(new ColumnLabelProvider());
		} else {
			provider.setColumnIndex(indiceColonna);
			colonna.setLabelProvider(provider);
		}
		
		ModificatoreValoriCelle<T> editor = creaModificatore();
		if (editor != null) {
			editor.setColumnIndex(indiceColonna);
			colonna.setEditingSupport(editor);
			editors.add(editor);
		}
		
		if (sorter != null)
			tblclmn.addSelectionListener(new ListenerColonna<T>(indiceColonna, tblclmn, getTable(), sorter, this));
	}

	/**
	 * Nel corpo di questo metodo vanno aggiunti Listener particolari per raffinare il comportamento della tabella
	 */
	protected abstract void aggiungiListener();
	
	protected abstract void aggiungiMenu(Menu menu);
	
	/**
	 * Aggiunge i listener base della tabella.
	 */
	protected void aggiungiListenerBase() {
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'c')) {
					copiaSelezione();
                }
			}
		});
	}

	/**
	 * Aggiunge il menù base della tabella.
	 */
	protected void aggiungiMenuBase() {
		aggiungiVoceMenuCopia();
		aggiungiVoceMenuSelezionaTutto();
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
		
	protected void aggiungiVoceMenuSelezionaTutto() {
		selectAll = new MenuItem(menuPopup, SWT.PUSH);
		selectAll.setText("Seleziona tutto");
		selectAll.setImage(Immagine.SPUNTAVERDE_16X16.getImage());
		selectAll.addListener(SWT.Selection, new Listener() {
	    	public void handleEvent(Event event) {
	    		int selectionIndex = table.getSelectionIndex();
	    		if (selectionIndex != -1) {
	    			selectAll();
	    		}
	    	}
	    });
	}
	
	
	/**
	 * Restituisce una stringa con il testo selezionato.
	 */
	protected String getSelectedText() {
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
		for (TableItem item : table.getSelection()) {
			for (int index = 0; index < columns; index++) {
				textData.append(item.getText(index));
				textData.append(TAB);
			}
			textData.setLength(textData.length() - 1);
			textData.append(NEW_LINE);
		}
		textData.setLength(textData.length() - 1);
		return textData.toString();
	}
	
	/**
	 * Restituisce una stringa con tutto il testo della tabella.
	 * @return
	 */
	protected String getTableText() {
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
			for (int index = 0; index < columns; index++) {
				textData.append(item.getText(index));
				textData.append(TAB);
			}
			textData.setLength(textData.length() - 1);
			textData.append(NEW_LINE);
		}
		textData.setLength(textData.length() - 1);
		return textData.toString();
	}

	public void copiaSelezione() {
		String textData = getTableText();
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
		System.out.println("Testo ricco copiato.");
	}
	
	/**
	 * Sistema la larghezza delle colonne in base al contenuto.
	 */
	@Override
	public final void aggiustaLarghezzaColonne() {
//		for (TableColumn column: table.getColumns()) {
//			//Controllo sul tipo di colonna, se è stata marchiata come colonna vuota non vado a ridimensionarla.
//			if (column.getToolTipText() != null && column.getToolTipText().equals(DO_NOT_SHOW))
//				continue;
//			column.pack();
//		}
		refresh();
		AggiustaLarghezzaColonne runnable = new AggiustaLarghezzaColonne(table);
		table.getDisplay().asyncExec(runnable);
	}
	
	/**
	 * Imposta il contenuto della tabella con quello passato come argomento.
	 * La larghezza delle colonne viene ricalcolata in base al nuovo contenuto.
	 * @param items una collection di elementi del tipo specifico della tabella.
	 */
	@Override
	public final void setElementi(Collection<T> items) {
		elementi.clear();
		if (items != null)
			elementi.addAll(items);
		setInput(items);
		aggiustaLarghezzaColonne();
	}
	
	/**
	 * Imposta il contenuto della tabella con quello passato come argomento.
	 * La larghezza delle colonne viene ricalcolata in base al nuovo contenuto.
	 * @param items un array di elementi del tipo specifico della tabella.
	 */
	@Override
	public final void setElementi(T[] items) {
		elementi.clear();
		for (T item : items)
			elementi.add(item);
		setInput(items);
		aggiustaLarghezzaColonne();
	}
	
	/**
	 * Restituisce una lista contenente gli elementi della tabella.
	 * @return una lista tipata con gli oggetti contenuti nella tabella.
	 */
//	@SuppressWarnings("unchecked")
	public List<T> getElementi() {
//		List<T> lista = new LinkedList<>();
//		TableItem[] items = table.getItems();
//		for (TableItem item : items) {
//			T object = (T) item.getData();
//			lista.add(object);
//		}
//		return lista;
		return elementi;
	}
	
	@SuppressWarnings("unchecked")
	public T getRigaSelezionata() {
		T item = null;
		int selectedIndex = table.getSelectionIndex();
		if (selectedIndex != -1) {
			TableItem selectedItem = table.getItem(selectedIndex);
			item = (T) selectedItem.getData();
		}
		return item;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getRigheSelezionate() {
		List<T> items = new LinkedList<>();
		int[] selectedIndicies = table.getSelectionIndices();
		for (int index : selectedIndicies) {
			TableItem selectedItem = table.getItem(index);
			items.add((T) selectedItem.getData());
		}
		return items;
	}
	
	/**
	 * Indica se qualcuno ha modificato i dati presenti nella tabella.<br>
	 * Ogni tabella a cui interessa questa informazione deve gestirsi in autonomia questo stato.
	 * @return
	 */
	public boolean isDirty() {
		boolean editorDirty = false;
		for (ModificatoreValoriCelle<T> editor : editors) {
			if (editor.isDirty()) {
				editorDirty = true;
				break;
			}
		}
		return dirty || editorDirty;
	}
	
	/**
	 * Abilita o disabilita tutte le voci del menu da click destro.
	 * @param permesso
	 */
	public void abilitaMenu(boolean permesso) {
		menuPopup.setEnabled(permesso);		
	}
	
	public void mostraMessaggio(String titolo, String messaggio) {
		PopupDialog dialog = new PopupDialog(table.getShell(), PopupDialog.HOVER_SHELLSTYLE, false, false, false, false, false, titolo, messaggio);
		dialog.open();
	}
	
	/**
	 * Nel corpo di questo metodo va implementato il filtraggio degli elementi in tabella.
	 * Usualmente viene abbinato al viewer un oggetto <code>ViewerFilter</code>
	 */
	protected final void setFiltro() {
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
