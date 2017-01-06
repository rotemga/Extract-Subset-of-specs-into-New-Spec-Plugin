package tau.smlab.syntech.extractspecsintonewspec;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import tau.smlab.syntech.spectra.Decl;
import tau.smlab.syntech.spectra.LTLAsm;
import tau.smlab.syntech.spectra.LTLGar;
import tau.smlab.syntech.spectra.Model;
import tau.smlab.syntech.spectragameinput.SpectraInputProvider;

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
	
	public boolean find (String projectName, String fileName) {
	  IProject project = getProject(projectName);
	  fileName += ".spectra";
	  boolean fileExists = project.getFile(fileName).exists();
	  boolean check = new File(projectName, fileName).exists();
	   System.out.println(projectName+ " " + fileName);
	  System.out.println(fileExists);
	  System.out.println(check);
	  
    return fileExists;
	   
	}

	public boolean find(IFile specFile, String entityKind, String entityName)
	{
		Model m = SpectraInputProvider.getSpectraModel(specFile);

		for(Decl decl : m.getElements()) {
			if (entityKind.equals(Constants.ASSUMPTION) && decl instanceof LTLAsm)
			{
				LTLAsm asm = (LTLAsm)decl;
				if (asm.getName() != null && asm.getName().equals(entityName))
				{	
					if(asm.getJustice() != null) {
						type = Constants.JUSTICE;
					}
					else if(asm.getSafety() != null) {
						type = Constants.SAFETY;
					}
					else { // INI
						type = Constants.INI;
					}
					
					INode inode = NodeModelUtils.getNode(asm.getTemporalExpr());
					content = inode.getText() + Constants.SEMICOLON;
					eobject = asm;
					return true;
				}
			}
			else if (entityKind.equals(Constants.GUARANTEE) && decl instanceof LTLGar)
			{
				LTLGar gar = (LTLGar)decl;
				if (gar.getName() != null && gar.getName().equals(entityName))
				{
					
					if(gar.getJustice() != null) {
						type = Constants.JUSTICE;
					}
					else if(gar.getSafety() != null) {
						type = Constants.SAFETY;
					}
					else { // INI
						type = Constants.INI;
					}
					
					INode inode = NodeModelUtils.getNode(gar.getTemporalExpr());
					content = inode.getText() + Constants.SEMICOLON;		
					eobject = gar;
					return true;
				}
			}		    	

		}
		return false;
	}
	
  public IProject getProject(String projectName){
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    for (IProject p: projects) {
      if (projectName.equals(p.getName()))
        return p;
    }
    return null;
    }

}
