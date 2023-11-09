package com.dufler.swt.utils.elements;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public abstract class FunzionePart {
	
	@Inject
	protected EPartService partService;
	
	private final String label;
	private final String tooltip;
	private final String funzionePartID;
	private final String partID;
	
	public FunzionePart(String label, String tooltip, String funzionePartID, String partID) {
		this.label = label;
		this.tooltip = tooltip;
		this.funzionePartID = funzionePartID;
		this.partID = partID;
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));
		Label l = new Label(parent, SWT.NONE);
		l.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				mostraPart();
			}
		});
		l.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		l.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
		l.setToolTipText(tooltip);
		l.setText(label);
		
		setVisibility();
	}
	
	protected void setVisibility() {
		boolean visible = isVisible();
		MPart partFunzione = partService.findPart(funzionePartID);
		if (partFunzione != null) {
			partFunzione.setVisible(visible);
		}
		MPart part = partService.findPart(partID);
		if (part != null) {
			part.setVisible(visible);
		}
	}
	
	protected void mostraPart() {
		MPart part = partService.findPart(partID);
		if (part != null) {
			System.out.println("Mostro la part: " + partID);
			part.setVisible(true);
			partService.showPart(part, PartState.VISIBLE);
		}
	}
	
	public abstract boolean isVisible();

}
