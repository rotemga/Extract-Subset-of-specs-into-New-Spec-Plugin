package tau.smlab.syntech.extractspecsintonewspec.action;
//import static tau.smlab.syntech.extractspecsintonewspec.Activator.PLUGIN_NAME;

import static tau.smlab.syntech.extractspecsintonewspec.Activator.PLUGIN_NAME;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import tau.smlab.syntech.extractspecsintonewspec.Constants;
import tau.smlab.syntech.extractspecsintonewspec.Searcher;
import tau.smlab.syntech.extractspecsintonewspec.dialogs.ExtractSpecDialog;
import tau.smlab.syntech.extractspecsintonewspec.dialogs.ExtractSpecResultDialog;



public class ExtractSpecsIntoNewSpecAction {
  private IFile specFile;
  private IWorkbench workbench;
  private IWorkbenchWindow window;
  private IWorkbenchPage activePage;
  private Shell shell;
  
  
  
  
  public void run(String action) {    
    if (!setSpecFileAndEnvironmentFields()) {
      return;
    }

    if (!savePage()) {
      return;
    }
    

    if (action.equals(ActionsID.CREATE)) {
      boolean tryAgain = true;

      while (tryAgain) {
        ExtractSpecDialog searchDialog = new ExtractSpecDialog(shell);
        searchDialog.open();
        
        if (searchDialog.isCancelPressed())
        {
          break;
        }
        ///////////////
        String entityKindRepresentation = (searchDialog.getProjectName().equals("Assumption") ? Constants.ASSUMPTION : Constants.GUARANTEE);
        Searcher searcher = new Searcher();
       // boolean isEntityFound = searcher.find(specFile, entityKindRepresentation, searchDialog.getFileName());
        boolean isFileFound = searcher.find(searchDialog.getProjectName(), searchDialog.getFileName());

        if (!isFileFound) {
          String specFileName = specFile.getName();
          int extensionInd = specFileName.indexOf(".");
          ExtractSpecResultDialog searchResultDialog = new ExtractSpecResultDialog(shell, specFileName.substring(0, extensionInd), searchDialog.getProjectName(), searchDialog.getFileName(), searcher.getType(), searcher.getContent());
          searchResultDialog.open();
          
          if (searchResultDialog.isSearchAgainPressed())
          {
            tryAgain = true;
          }
          else
          {
            tryAgain = false;
          }
          
          if (searchResultDialog.isMarkersButtonPressed())
          {
            EObject eobject = searcher.getEObject();
            INode node = NodeModelUtils.getNode(eobject);
            IMarker marker;
            try {
              marker = specFile.createMarker("tau.smlab.syntech.entitiesfinder.ui.marker");
              marker.setAttribute(IMarker.MESSAGE, "Search result");
              marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
              marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
              marker.setAttribute(IMarker.LOCATION, node.getStartLine());
              marker.setAttribute(IMarker.CHAR_START, node.getOffset());
              marker.setAttribute(IMarker.CHAR_END, node.getEndOffset());           
            } catch (CoreException e) {
            }
          }
          
        } else { // isFileFound
          MessageDialog fileAlreadyExistDialog = new MessageDialog(shell, PLUGIN_NAME, null, 
              "The file " + searchDialog.getFileName() + " is already exists in the project'" + searchDialog.getProjectName() + ". Please try again.",
              MessageDialog.INFORMATION, new String[] {"Try Again", "Close"}, 0);
          
          int result = fileAlreadyExistDialog.open();
          if (result == 0) {
            tryAgain = true;
          } else if (result == 1) {
            tryAgain = false;
          }
        }
      }

    } else {
      System.err.println("Unhandled action id: " + action);
    }

  }
  

  /**
   * @return true if setting succeeded, false otherwise.
   */
  private boolean setSpecFileAndEnvironmentFields() {
    workbench = PlatformUI.getWorkbench();
    window = workbench.getActiveWorkbenchWindow();
    shell = window.getShell();
    activePage = window.getActivePage();
    List<IFile> selectedFiles = new ArrayList<>();

    if (window == null) {
      return false;
    }

    ISelectionService selectionService = window.getSelectionService();
    if (selectionService == null) {
      return false;
    }

    ISelection selection = selectionService.getSelection();
    if (selection == null) {
      return false;
    }

    // selected on text view (the code window)
    if (selection instanceof TextSelection) {
      IEditorPart editor = activePage.getActiveEditor();
      IFile original = ((FileEditorInput) editor.getEditorInput()).getFile();
      specFile = original;
    }

    // selected on explorer view
    else if (selection instanceof IStructuredSelection) {
      IStructuredSelection structSelection = (IStructuredSelection) selection;
      List<?> selected = structSelection.toList();
      for (Object object : selected) {
        if (object instanceof IFile) {
          IFile file = (IFile) object;
          selectedFiles.add(file);
        }
      }
      if (selectedFiles == null || selectedFiles.size() == 0 || selectedFiles.size() >= 2) {
        MessageDialog.openInformation(shell, PLUGIN_NAME, "Please select only one .spectra file.");
        return false;
      } else {
        specFile = selectedFiles.get(0);
      }
    }

    return true;
  }
  
  /**
   * If the page is unsaved, ask user if he wants to save it first
   * @return false if the user has chosen to abort
   */
  private boolean savePage() {
    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

    // check if file is saved
    IEditorPart editorPart = page.getActiveEditor();
    if (editorPart != null && editorPart.isDirty()) {
      boolean isYes = MessageDialog.openQuestion(shell, PLUGIN_NAME,
          "The file is not saved. Select 'Yes' to save and 'No' to abort.");
      if (isYes) {
        editorPart.doSave(null);
      } else {
        return false;
      }
    }
    return true;
  }
  
  
}
