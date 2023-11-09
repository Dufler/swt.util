package com.dufler.swt.utils.elements.tree;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
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
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.dufler.swt.utils.decoration.Immagine;
import com.dufler.swt.utils.elements.ITabella;
import com.dufler.swt.utils.elements.Ordinatore;
import com.dufler.swt.utils.elements.table.filter.CriteriFiltraggio;
import com.dufler.swt.utils.elements.table.filter.FiltroTabella;

/**
 * Raffinamento della classe TreeViewer.
 * Va implementato il provider specifico per il tipo scelto.
 * @author Damiano Bellucci - damiano.bellucci@gmail.com
 *
 * @param <T> il tipo di oggetti contenuti nell'albero.
 */
public abstract class Albero<T, C extends CriteriFiltraggio> extends TreeViewer implements ITabella<T, C> {
	
	public static final int STILE_SELEZIONE_MULTIPLA = SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL;
	public static final int STILE_SELEZIONE_SINGOLA = SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL;
	
	public static final int DEFAULT_WIDTH = 100;
	
	protected static final String TAB = "\t";
	protected static final String NEW_LINE = "\n"; 
	
	protected final Tree tree;
	protected Menu menuPopup;
	protected MenuItem copy;
	
	protected Ordinatore<T> sorter;
	
	protected FiltroTabella<T, C> filter;
	protected CriteriFiltraggio criteri;
	
	protected List<T> elementi;
	
	public Albero(Composite parent) {
		this(parent, STILE_SELEZIONE_SINGOLA);
	}

	public Albero(Composite parent, int style) {
		super(parent, style);
		
		this.tree = getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		this.menuPopup = new Menu(tree);
		this.tree.setMenu(menuPopup);
		
		elementi = new LinkedList<>();
		
		aggiungiMenuBase();
		aggiungiListenerMenuAComparsa();
		aggiungiMenu(menuPopup);
		
		aggiungiListenerBase();
		setOrdinamento();
		
		impostaColonne();
		
		setContentProvider(creaProvider());
	}
	
	/**
	 * Metodo da estendere per tutte i tree in grado di gestire in autonomia il proprio contenuto.
	 */
	@Override
	public final void aggiornaContenuto() {
		List<T> contenuto = elaboraContenutoInAutonomia();
		if (contenuto != null)
			setElementi(contenuto);
		refresh();
	}
	
	/**
	 * Metodo da estendere per tutte le tabelle in grado di gestire in autonomia il proprio contenuto.
	 */
	protected List<T> elaboraContenutoInAutonomia() {
		return null;
	}
	
	/**
	 * Imposta le colonne che compongono la tabella.
	 */
	protected abstract void impostaColonne();
	
	@SuppressWarnings("unchecked")
	public List<T> getElementi() {
		Object input = getInput();
		return (List<T>) input;
	}
	
	/**
	 * Imposta l'insieme di elementi all'interno dell'albero.
	 * @param elementi
	 */
	@Override
	public void setElementi(Collection<T> items) {
		elementi.clear();
		if (items != null)
			elementi.addAll(items);
		setInput(items);
		aggiustaLarghezzaColonne();
	}
	
	/**
	 * Imposta l'insieme di elementi all'interno dell'albero.
	 * @param elementi
	 */
	@Override
	public void setElementi(T[] items) {
		elementi.clear();
		for (T item : items)
			elementi.add(item);
		setInput(items);
		aggiustaLarghezzaColonne();
	}
	
	/**
	 * Ridimensiona la larghezza delle colonne in automatico.
	 */
	@Override
	public void aggiustaLarghezzaColonne() {
		AggiustaLarghezzaColonne runnable = new AggiustaLarghezzaColonne();
		tree.getDisplay().asyncExec(runnable);
	}
	
	/**
	 * Runnable che aggiusta la larghezza delle colonne.
	 * @author Damiano
	 *
	 */
	private class AggiustaLarghezzaColonne implements Runnable {
		
		public void run() {
			for (TreeColumn column : tree.getColumns()) { 
				column.pack(); 
			}
		}
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
	
	protected abstract AlberoProvider<T> creaProvider();
	
	protected abstract AlberoEtichettatore creaEtichettatore(int indiceColonna);
	
	/**
	 * Aggiunge una colonna con il nome e l'etichettatore specificati lasciando il resto come default.
	 * @param columnName il nome della colonna.
	 * @param etichettatore l'etichettatore che si occupera' di riempire il contenuto delle celle nella colonna.
	 */
	protected void aggiungiColonna(String columnName, int indiceColonna) {
		aggiungiColonna(columnName, indiceColonna, DEFAULT_WIDTH, SWT.NONE);
	}
	
	protected void aggiungiColonna(String columnName, int indiceColonna, int width, int style) {
		TreeViewerColumn treeColumn = new TreeViewerColumn(this, style);
		TreeColumn column = treeColumn.getColumn();
		column.setText(columnName);
		column.setWidth(width);
		
		AlberoEtichettatore etichettatore = creaEtichettatore(indiceColonna);
		if (etichettatore == null) {
			treeColumn.setLabelProvider(new ColumnLabelProvider());
		} else {
			treeColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(etichettatore));
		}
		
		if (sorter != null)
			column.addSelectionListener(new ListenerColonnaTree<T>(indiceColonna, column, tree, sorter, this));
	}
	
	protected void aggiungiListenerBase() {
		
		TreeListener listener = new TreeListener() {

			@Override
			public void treeCollapsed(TreeEvent e) {
				aggiustaLarghezzaColonne();				
			}

			@Override
			public void treeExpanded(TreeEvent e) {
				aggiustaLarghezzaColonne();				
			}
			
		};
		tree.addTreeListener(listener);
		
		
		tree.addKeyListener(new KeyAdapter() {
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
	    copy = new MenuItem(menuPopup, SWT.PUSH);
	    copy.setText("Copia tabella");
	    copy.setImage(Immagine.COPIA_16X16.getImage());
	    copy.addListener(SWT.Selection, new Listener() {
	    	public void handleEvent(Event event) {
	    		copiaSelezione();
	    	}
	    });	
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
	
	/**
	 * Metodo da sovrascrivere per aggiungere voci di menù.
	 * @param menuPopup
	 */
	protected void aggiungiMenu(Menu menuPopup) {
		//DO NOTHING
	}
	
	/**
	 * Restituisce una stringa con tutto il testo della tabella.
	 * @return
	 */
	protected String getTreeText() {
		StringBuilder textData = new StringBuilder();
		//Aggiungo i nomi delle colonne
		textData.append(getColumnsHeader());
		//Aggiungo il contenuto selezionato
		textData.append(getContentAsString());
		return textData.toString();
	}
	
	protected String getColumnsHeader() {
		StringBuilder textData = new StringBuilder();
		int columns = tree.getColumnCount();
		for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
			textData.append(tree.getColumn(columnIndex).getText());
			textData.append(TAB);
		}
		textData.setLength(textData.length() - 1);
		textData.append(NEW_LINE);
		return textData.toString();
	}
	
	protected String getContentAsString() {
		StringBuilder textData = new StringBuilder();
		int columns = tree.getColumnCount();
		for (TreeItem item : tree.getItems()) {
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
	
	@Override
	public void copiaSelezione() {
		String textData = getTreeText();
		Clipboard clipboard = new Clipboard(Display.getDefault());
		TextTransfer textTransfer = TextTransfer.getInstance();
		Transfer[] transfers = new Transfer[] { textTransfer };
		Object[] data = new Object[] { textData };
		clipboard.setContents(data, transfers);
		clipboard.dispose();
		System.out.println("Testo copiato.");
	}
	
	protected void copiaSelezioneConRichText() {
		String textData = getTreeText();
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
	 * Metodo implementato solo per i tree in alternativa al <method>getRigaSelezionata()</method> perchè i figli potrebbe essere tipi diversi da T.
	 * @return l'oggetto (padre o figlio) selezionato dall'utente.
	 */
	public Object getElementoSelezionato() {
		TreeSelection selection = (TreeSelection) getSelection();
		Object item  = selection.getFirstElement();
		return item;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getRigaSelezionata() {		
		TreeSelection selection = (TreeSelection) getSelection();
		T item  = (T) selection.getFirstElement();
		return item;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getRigheSelezionate() {
		List<T> items = new LinkedList<>();
		TreeSelection selection = (TreeSelection) getSelection();
		for (Object selected : selection.toArray()) {
			T item = (T) selected;
			items.add(item);
		}
		return items;
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
