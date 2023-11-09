package com.dufler.swt.utils.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class DialogMessaggio extends IconAndMessageDialog implements DialogApribile {
	
	public static final String OK_LABEL = "Ok";
	public static final String CANCEL_LABEL = "Annulla";
	public static final String YES_LABEL = "Si";
	public static final String NO_LABEL = "No";


    public final static int NONE = 0;
    public final static int ERROR = 1;
    public final static int INFORMATION = 2;
    public final static int QUESTION = 3;
    public final static int WARNING = 4;
    public final static int CONFIRM = 5;
    public final static int QUESTION_WITH_CANCEL = 6;
    
    private String[] buttonLabels;
    private Button[] buttons;
    private int defaultButtonIndex;
    private String title;
    private Image titleImage;
    private Image image = null;
    private Control customArea;

    
	public DialogMessaggio(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell);
		init(dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, defaultIndex, dialogButtonLabels);
	}

	public DialogMessaggio(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, int defaultIndex, String... dialogButtonLabels) {
		super(parentShell);
		init(dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, defaultIndex, dialogButtonLabels);
	}

	private void init(String dialogTitle, Image dialogTitleImage, String dialogMessage, int dialogImageType,
			int defaultIndex, String... dialogButtonLabels) {
		this.title = dialogTitle;
        this.titleImage = dialogTitleImage;
        this.message = dialogMessage;

        switch (dialogImageType) {
        case ERROR: {
            this.image = getErrorImage();
            break;
        }
        case INFORMATION: {
            this.image = getInfoImage();
            break;
        }
        case QUESTION:
        case QUESTION_WITH_CANCEL:
        case CONFIRM: {
            this.image = getQuestionImage();
            break;
        }
        case WARNING: {
            this.image = getWarningImage();
            break;
        }
        }
        this.buttonLabels = dialogButtonLabels;
        this.defaultButtonIndex = defaultIndex;
	}

    @Override
	protected void buttonPressed(int buttonId) {
        setReturnCode(buttonId);
        close();
    }

    @Override
	protected void configureShell(Shell shell) {
        super.configureShell(shell);
        if (title != null) {
			shell.setText(title);
		}
        if (titleImage != null) {
			shell.setImage(titleImage);
		}
    }

    @Override
	protected void createButtonsForButtonBar(Composite parent) {
        buttons = new Button[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            String label = buttonLabels[i];
			Button button = createButton(parent, i, label, defaultButtonIndex == i);
            buttons[i] = button;
        }
    }

    protected Control createCustomArea(Composite parent) {
        return null;
    }

    @Override
	protected Control createDialogArea(Composite parent) {
        // create message area
        createMessageArea(parent);
        // create the top level composite for the dialog area
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        composite.setLayout(layout);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        composite.setLayoutData(data);
        // allow subclasses to add custom controls
        customArea = createCustomArea(composite);
        //If it is null create a dummy label for spacing purposes
        if (customArea == null) {
			customArea = new Label(composite, SWT.NULL);
		}
        return composite;
    }

    @Override
	protected Button getButton(int index) {
		if (buttons == null || index < 0 || index >= buttons.length) {
			return null;
		}
        return buttons[index];
    }

    protected int getMinimumMessageWidth() {
        return convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
    }

    
    @Override
	protected void handleShellCloseEvent() {
        //Sets a return code of SWT.DEFAULT since none of the dialog buttons
        // were pressed to close the dialog.
        super.handleShellCloseEvent();
        setReturnCode(SWT.DEFAULT);
    }

    @Override
	public int open() {
    	return super.open();
    }
    
    public static boolean open(int kind, Shell parent, String title, String message, int style) {
    	ApriDialog runnable = new ApriDialog(parent, title, message, kind, style);
    	Display.getDefault().asyncExec(runnable);
    	boolean result = runnable.getResult();
    	return result;
    }
    
    private static class ApriDialog implements Runnable {
    	
    	private final Shell parent;
    	private final String title;
    	private final String message;
    	private final int kind;
    	private int style;
    	private boolean result;
    	
    	public ApriDialog(Shell parent, String title, String message, int kind, int style) {
    		this.parent = parent;
    		this.title = title;
    		this.message = message;
    		this.kind = kind;
    		this.style = style;
    	}

		@Override
		public void run() {
			DialogMessaggio dialog = new DialogMessaggio(parent, title, null, message, kind, 0, getButtonLabels(kind));
	    	style &= SWT.SHEET;
	    	dialog.setShellStyle(dialog.getShellStyle() | style);
	    	result = dialog.open() == 0;
		}
		
		public boolean getResult() {
			return result;
		}
    	
    }

	public static boolean openSync(int kind, Shell parent, String title, String message, int style) {
		DialogMessaggio dialog = new DialogMessaggio(parent, title, null, message, kind, 0, getButtonLabels(kind));
		style &= SWT.SHEET;
		dialog.setShellStyle(dialog.getShellStyle() | style);
		return dialog.open() == 0;
	}
	
	public static DialogMessaggio getDialog(int kind, Shell parent, String title, String message, int style) {
		DialogMessaggio dialog = new DialogMessaggio(parent, title, null, message, kind, 0, getButtonLabels(kind));
		style &= SWT.SHEET;
		dialog.setShellStyle(dialog.getShellStyle() | style);
		return dialog;
	}
	
	static String[] getButtonLabels(int kind) {
		String[] dialogButtonLabels;
		switch (kind) {
		case ERROR:
		case INFORMATION:
		case WARNING: {
			dialogButtonLabels = new String[] { OK_LABEL };
			break;
		}
		case CONFIRM: {
			dialogButtonLabels = new String[] { OK_LABEL, CANCEL_LABEL };
			break;
		}
		case QUESTION: {
			dialogButtonLabels = new String[] { YES_LABEL, NO_LABEL };
			break;
		}
		case QUESTION_WITH_CANCEL: {
			dialogButtonLabels = new String[] { YES_LABEL, NO_LABEL, CANCEL_LABEL };
			break;
		}
		default: {
			throw new IllegalArgumentException("Illegal value for kind in MessageDialog.open()"); //$NON-NLS-1$
		}
		}
		return dialogButtonLabels;
	}
	
	private static Shell getNewShell() {
//		Display display = Display.getCurrent();
//		Shell shell = display.getActiveShell();
//		return shell;
		return null;
	}
	
	//
	
	public static DialogMessaggio getConfirm(String title, String message) {
        return getDialog(CONFIRM, getNewShell(), title, message, SWT.NONE);
    }

    public static DialogMessaggio getError(String title, String message) {
    	return getDialog(ERROR, getNewShell(), title, message, SWT.NONE);
    }

    public static DialogMessaggio getInformation(String title, String message) {
    	return getDialog(INFORMATION, getNewShell(), title, message, SWT.NONE);
    }
    
    public static DialogMessaggio getInformation(String title, String message, Shell shell) {
    	return getDialog(INFORMATION, shell, title, message, SWT.NONE);
    }

    public static DialogMessaggio getQuestion(String title, String message) {
    	return getDialog(QUESTION, getNewShell(), title, message, SWT.NONE);
    }

    public static DialogMessaggio getWarning(String title, String message) {
    	return getDialog(WARNING, getNewShell(), title, message, SWT.NONE);
    }
    
    //
	
    public static boolean openConfirm(String title, String message) {
        return openSync(CONFIRM, getNewShell(), title, message, SWT.NONE);
    }

    public static void openError(String title, String message) {
        open(ERROR, getNewShell(), title, message, SWT.NONE);
    }

    public static void openInformation(String title, String message) {
        open(INFORMATION, getNewShell(), title, message, SWT.NONE);
    }
    
    public static void openInformation(String title, String message, Shell shell) {
        open(INFORMATION, shell, title, message, SWT.NONE);
    }

    public static boolean openQuestion(String title, String message) {
        return openSync(QUESTION, getNewShell(), title, message, SWT.NONE);
    }

    public static void openWarning(String title, String message) {
        open(WARNING, getNewShell(), title, message, SWT.NONE);
    }
    
    @Override
	protected Button createButton(Composite parent, int id, String label,
            boolean defaultButton) {
        Button button = super.createButton(parent, id, label, defaultButton);
        //Be sure to set the focus if the custom area cannot so as not
        //to lose the defaultButton.
        if (defaultButton && !customShouldTakeFocus()) {
			button.setFocus();
		}
        return button;
    }

    protected boolean customShouldTakeFocus() {
        if (customArea instanceof Label) {
			return false;
		}
        if (customArea instanceof CLabel) {
			return (customArea.getStyle() & SWT.NO_FOCUS) > 0;
		}
        return true;
    }

    @Override
	public Image getImage() {
        return image;
    }

    protected String[] getButtonLabels() {
        return buttonLabels;
    }

    protected int getDefaultButtonIndex() {
        return defaultButtonIndex;
    }

	protected void setButtons(Button... buttons) {
        if (buttons == null) {
			throw new NullPointerException("The array of buttons cannot be null."); //$NON-NLS-1$
		}
        this.buttons = buttons;
    }

	protected void setButtonLabels(String... buttonLabels) {
        if (buttonLabels == null) {
			throw new NullPointerException("The array of button labels cannot be null."); //$NON-NLS-1$
		}
        this.buttonLabels = buttonLabels;
    }

}
