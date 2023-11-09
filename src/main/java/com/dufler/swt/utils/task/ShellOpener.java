package com.dufler.swt.utils.task;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ShellOpener implements Runnable {

	Shell shell;
	
	public void run() {
//		Display display = PlatformUI.createDisplay();
//		WorkbenchAdvisor advisor = new WorkbenchAdvisor() {
//			
//			@Override
//			public String getInitialWindowPerspectiveId() {
//				return null;
//			}
//		};
//		int returnCode = PlatformUI.createAndRunWorkbench(display, advisor);
//		if (returnCode == PlatformUI.RETURN_OK)
			shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }
	
	public Shell getShell() {
		return shell;
	}

}
