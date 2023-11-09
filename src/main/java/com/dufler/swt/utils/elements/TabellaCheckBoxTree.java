package com.dufler.swt.utils.elements;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.dufler.swt.utils.elements.table.filter.CriteriFiltraggio;

public abstract class TabellaCheckBoxTree<T, C extends CriteriFiltraggio> extends CheckboxTreeViewer {
	
	protected Tree tree;
//	protected Ordinatore<T> sorter;

	public TabellaCheckBoxTree(Composite parent) {
		super(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		setAutoExpandLevel(CheckboxTreeViewer.ALL_LEVELS);
		setContentProvider(creaVisualizzatore());
		
		tree = getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
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
	
//	/**
//	 * Nel corpo di questo metodo va implementato l'ordinamento della tabella.
//	 * Usualmente viene abbinato al viewer un oggetto <code>ViewerComparator</code>
//	 * La classe <code>Ordinatore</code> nello stesso package fornisce un template da poter riciclare.
//	 */
//	protected final void setOrdinamento() {
//		sorter = creaOrdinatore();
//		if (sorter != null) {
//			setComparator(sorter);
//		} else {
//			setComparator(new ViewerComparator());
//		}
//	}
	
	protected abstract VisualizzatoreAlbero<T> creaVisualizzatore();
	
	protected abstract Etichettatore<T> creaEtichettatore();
	
//	protected abstract Ordinatore<T> creaOrdinatore();
	
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
	
	public void aggiungiColonna(String nome, int larghezza, Etichettatore<T> provider, ModificatoreValoriCelle<T> modificatore, int indiceColonna, int style) {
		
		//Controllo sui parametri
		if (nome == null)
			nome = "";
		if (larghezza < 0)
			larghezza = 0;
		
		TreeViewerColumn colonna = new TreeViewerColumn(this, style);
		
		TreeColumn tblclmn = colonna.getColumn();
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
		
//		if (sorter != null)
//			tblclmn.addSelectionListener(new ListenerColonnaCheckBox<T>(indiceColonna, tblclmn, getTable(), sorter, this));
	}

}
