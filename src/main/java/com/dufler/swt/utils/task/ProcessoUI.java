package com.dufler.swt.utils.task;

import org.eclipse.e4.ui.di.UISynchronize;

/**
 * Classe da utilizzare per eseguire processi lunghi che influenzano la UI.
 * @author Damiano Bellucci - damiano.bellucci@gmail.com
 *
 */
public abstract class ProcessoUI extends Processo {
	
	private final boolean synchronize;
	private final UISynchronize synchronizer;

	public ProcessoUI(String title, Integer operations, boolean synchronize, UISynchronize synchronizer) {
		super(title, operations);
		this.synchronize = synchronize;
		this.synchronizer = synchronizer;
	}
	
	public void eseguiOperazioni() throws Exception {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				eseguiOperazioniSincronizzate();
			}
		};
		
		if (synchronize) {
			synchronizer.syncExec(r);
		} else {
			synchronizer.asyncExec(r);
		}
		
	}
	
	/**
	 * Questo metodo va esteso e riempito con le istruzioni da eseguire in maniera sincrona alla UI.
	 */
	public abstract void eseguiOperazioniSincronizzate() throws RuntimeException;
	
}
