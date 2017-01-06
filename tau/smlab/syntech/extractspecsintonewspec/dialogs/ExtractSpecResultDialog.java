package tau.smlab.syntech.extractspecsintonewspec.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ExtractSpecResultDialog extends TitleAreaDialog {

	private String modelName;
	private String projectName; // Assumption/Guarantee
	private String fileName;
	private String entityType; // INI/SAFETY/JUSTICE
	private String content;

	private boolean isSearchAgainPressed;
	private boolean isMarkersButtonPressed;
	
	/**
	 * 
	 * @param parentShell
	 * @param modelName
	 * @param projectName - Assumption/Guarantee
	 * @param fileName
	 * @param entityType - INI/SAFETY/JUSTICE
	 * @param content
	 */
	public ExtractSpecResultDialog(Shell parentShell, String modelName, String projectName, String fileName, String entityType, String content) {
		super(parentShell);
		this.modelName = modelName;
		this.projectName = projectName;
		this.fileName = fileName;
		this.entityType = entityType;
		this.content = content;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Open created file");
		setMessage("The file created", IMessageProvider.INFORMATION);
		getButton(IDialogConstants.CANCEL_ID).setText("Open");
	}

//	@Override
//	protected Control createDialogArea(Composite parent) {
//		Composite area = (Composite) super.createDialogArea(parent);
//		Composite container = new Composite(area, SWT.NONE);
//		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		GridLayout layout = new GridLayout(2, false);
//		container.setLayout(layout);
//
//		createGreyBox(container, "Model name:", modelName);
//		createGreyBox(container, "Entity kind:", projectName);
//		createGreyBox(container, "Entity name:", fileName);
//		createGreyBox(container, "Entity type:", entityType);
//		createGreyBox(container, "Content:", content);
//
//		createMarkersButton(container);
//		
//		return area;
//	}

	private void createOpenButton(Composite container) {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;

		Label label = new Label(container, SWT.NONE);
		label.setText("Show in spectra file:");
		
		Button button = new Button(container, SWT.BORDER);
		button.setText("Highlight");
		button.setLayoutData(gridData);
		
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				isMarkersButtonPressed = true;
				setReturnCode(OK);
				close();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
			}
			
		});
	}

//	private void createGreyBox(Composite container, String fieldName, String fieldValue) {
//		fieldValue = fieldValue.trim();
//		
//		Label label = new Label(container, SWT.NONE);
//		label.setText(fieldName);
//		
//		GridData gridData = new GridData();
//		gridData.grabExcessHorizontalSpace = true;
//		gridData.horizontalAlignment = GridData.FILL;
//		
//		Text text = new Text(container, SWT.BORDER | SWT.READ_ONLY);
//		text.setLayoutData(gridData);
//		text.setText(fieldValue);
//	}


	@Override
	protected boolean isResizable() {
		return true;
	}


	@Override
	protected void cancelPressed() {
		isSearchAgainPressed = true;
		super.cancelPressed();
	}
	
	
	public boolean isSearchAgainPressed() {
		return isSearchAgainPressed;
	}
	
	public boolean isMarkersButtonPressed() {
		return isMarkersButtonPressed;
	}
	
}