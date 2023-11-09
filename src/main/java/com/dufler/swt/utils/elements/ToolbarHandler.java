package com.dufler.swt.utils.elements;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

/**
 * Questa classe viene estesa da tutti i toolbar handler.<br>
 * Il comportamento predefinito associato al tasto consiste nell'abilitare la visione della perspective se si ha il permesso richiesto.
 * @author Damiano
 *
 */
public abstract class ToolbarHandler {
	
	protected static final String PERSPECTIVE_ID_USER = "it.ltc.logica.container.perspective.utente";

	@Execute
	public void execute(MApplication app, EPartService partService, EModelService modelService) {
		List<MPerspective> perspectives = modelService.findElements(app, null, MPerspective.class, null);
		for (MPerspective perspective : perspectives) {
			if (perspective.getElementId().equals(getPerspectiveID())) {
				operazioniAddizionaliPerAttivazionePerpespective();
				partService.switchPerspective(perspective);
				perspective.setVisible(true);
				break;
			}
		}
	}

	protected abstract String getPerspectiveID();
	
	/**
	 * Metodo da estendere se si vuole raffinare il comportamento per l'attivazione della perspective.
	 */
	protected void operazioniAddizionaliPerAttivazionePerpespective() {
		//DO NOTHING!
	}
	
	/**
	 * Restituisce l'ID della perspective da restituire.
	 * @return
	 */
	protected abstract String getOwnPerspectiveID();

}
