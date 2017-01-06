package tau.smlab.syntech.extractspecsintonewspec.dialogs;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ExtractSpecDialog extends TitleAreaDialog {

	private Combo ProjectNameCombo;
	private Text textFileName;

	private String projectNameString;
	private String fileName;
	private boolean isCancelPressed;
	
	public ExtractSpecDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Extract Subset of the Specification into a New File");
		setMessage("Please select a spectra file name and a project that the new file will be in", IMessageProvider.INFORMATION);
		// initially disable OK button
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		getButton(IDialogConstants.OK_ID).setText("Create");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		createProjectName(container);
		createFileName(container);

		return area;
	}

	private void createProjectName(Composite container) {
		Label lbtProjectName = new Label(container, SWT.NONE);
		lbtProjectName.setText("Project Name:");

		GridData dataProjectName = new GridData();
		dataProjectName.grabExcessHorizontalSpace = true;
		dataProjectName.horizontalAlignment = GridData.FILL;

		ProjectNameCombo = new Combo(container, SWT.BORDER);
		IProject[] projectsList = getProjects();
		for (IProject p : projectsList) {
		  ProjectNameCombo.add(p.getName());
		}

		ProjectNameCombo.setLayoutData(dataProjectName);

		// don't allow typing into the combo box
		// Note: instead you can change the construction this way, but the combo
		// will have a grey background
		// Combo entityKind = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		ProjectNameCombo.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				e.doit = false;
			}
		});

		ProjectNameCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
	}

	private void createFileName(Composite container) {
		Label lbtFileName = new Label(container, SWT.NONE);
		lbtFileName.setText("File name:");

		GridData dataFileName = new GridData();
		dataFileName.grabExcessHorizontalSpace = true;
		dataFileName.horizontalAlignment = GridData.FILL;
		textFileName = new Text(container, SWT.BORDER);
		textFileName.setLayoutData(dataFileName);

		textFileName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
	}
	


	private void validate() {
		if (isEmpty(textFileName.getText()) || isEmpty(ProjectNameCombo.getText())) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else if (textFileName.getText().contains(" ")) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			setErrorMessage("File name shouldn't contain spaces");
		} else {
			getButton(IDialogConstants.OK_ID).setEnabled(true);
		}
	}

	private boolean isEmpty(String text) {
		if (text == null) {
			return true;
		}
		return text.trim().isEmpty();
	} 
	
  public IProject[] getProjects(){
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    return projects;
    }
  
	@Override
	protected boolean isResizable() {
		return true;
	}

	// save content of the fields because they get disposed
	// as soon as the Dialog closes
	private void saveInput() {
		projectNameString = ProjectNameCombo.getText();
		fileName = textFileName.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	@Override
	protected void cancelPressed() {
		isCancelPressed = true;
		super.cancelPressed();
	}
	
	public String getProjectName() {
		return projectNameString;
	}

	public String getFileName() {
		return fileName;
	}
	
	public boolean isCancelPressed() {
		return isCancelPressed;
	}
}


