package tau.smlab.syntech.extractspecsintonewspec;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.util.Files;

public class Searcher {
	
	private String type;
	private String content;
	private EObject eobject;
	
	public String getType() {
		return type;
	}

	public String getContent() {
		return content;
	}
	
	public EObject getEObject() {
		return eobject;
	}
	


  public boolean isDirectoryExists(String directoryPath) {
    //directoryPath = "C:\\Users\\User\\runtime-EclipseApplication\\CarSpec\\src";
    String workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
    directoryPath = workspacePath + "/" + directoryPath;
    System.out.println(workspacePath + "  is the workspace path");

    File f = new File(directoryPath);
    if (f.exists() && f.isDirectory()) {
      System.out.println(directoryPath + " directory exists");
      return true;
    }
    System.out.println(directoryPath + " directory does not exists");
    return false;

  }
	
	public boolean isFileExists (String filePath, String directoryName) {
	  //filePath = "C:\\Users\\User\\workspace\\iSynthExamples\\basics\\Elevator.spectra";
    String workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
    filePath = workspacePath + "/" + directoryName + "/" + filePath + ".spectra";
	   File f = new File(filePath);
	    if (f.exists() && f.isFile()) {
	      System.out.println(filePath + " file exists");
	      return true;
	    }
	    System.out.println(filePath + " file does not exists");
	    return false;
	}
	
	public void createSpectraFile (String fileName, String directoryName, String selectedText) {
	  
	  String workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
    String filePath = workspacePath + "/" + directoryName + "/" + fileName + ".spectra";
    File file = new File(filePath);
    if(!file.exists()){
         try {
            file.createNewFile();
            String data =Constants.MODULE + " " + fileName + "\n" + "\n";
            data += selectedText;
            Files.writeStringIntoFile(filePath, data);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	  
	}
	
}
