/*
 * $RCSfile: JSSyntaxModelFactory.java,v $
 * 
 * Copyright 2002 CH-1700 Fribourg, Switzerland All rights reserved.
 * 
 * ========================================================================
 * Modifications history
 * ========================================================================
 * $Log: JSSyntaxModelFactory.java,v $
 * Revision 1.2  2004/02/27 17:25:25  cell
 * Fix NPE for files without an extension
 *
 * Revision 1.1  2004/02/26 02:25:42  agfitzp
 * renamed packages to match xml & css
 *
 * Revision 1.1  2004/02/05 03:10:08  agfitzp
 * Initial Submission
 *
 * Revision 1.1.2.1  2003/12/12 21:37:24  agfitzp
 * Experimental work for Classes view
 * Revision 1.3 2003/05/30 20:53:08 agfitzp
 * 0.0.2 : Outlining is now done as the user types. Some other bug fixes.
 * 
 * Revision 1.2 2003/05/28 20:47:56 agfitzp Outline the document, not the file.
 * 
 * Revision 1.1 2003/05/28 15:17:11 agfitzp net.sf.wdte.js.core 0.0.1 code
 * base
 * 
 * ========================================================================
 */

package net.sf.wdte.js.core.parser;

import java.util.LinkedList;
import java.util.List;

import net.sf.wdte.js.core.model.JSElementList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;

/**
 * @author Addi
 */
public class JSSyntaxModelFactory {
	private static JSSyntaxModelFactory instance = new JSSyntaxModelFactory();

	/**
	 * Creates a new JSSyntaxModelFactory.
	 */
	private JSSyntaxModelFactory() {
	}
	/**
	 * @param adaptable
	 * 
	 * @return
	 */
	public JSElementList getContentOutline(IProject aProject) {
		return new JSElementList(getSyntacticElements(aProject));
	}

	/**
	 * @param adaptable
	 * 
	 * @return
	 */
	public JSElementList getContentOutline(IFile adaptable) {
		return new JSElementList(getSyntacticElements(adaptable));
	}

	/**
	 * @param document
	 * 
	 * @return
	 */
	public JSElementList getContentOutline(IDocument document) {
		return new JSElementList(getSyntacticElements(document));
	}

	/**
	 * Returns the singleton.
	 * 
	 * @return
	 */
	public static JSSyntaxModelFactory getInstance() {
		return instance;
	}

	/**
	 * @param file
	 * 
	 * @return
	 */
	private List getSyntacticElements(IProject aProject) {
		int i;
		JSParser aParser = new JSParser();
		Object[] jsFiles = getJSFilesFor(aProject);

		for (i = 0; i < jsFiles.length; i++) {
			aParser.parse((IFile) jsFiles[i]);
		}
		return aParser.getElementList();
	}

	/**
	 * @param project
	 * @return
	 */
	private Object[] getJSFilesFor(IProject project) {
		LinkedList files = new LinkedList();
		collectJSFiles(project, files);
		return files.toArray();
	}

	private void collectJSFiles(IContainer aContainer, LinkedList files) {
		try {
			int i;
			IResource[] members = aContainer.members();
			for (i = 0; i < members.length; i++) {
				IResource aResource = members[i];
				if (aResource.getType() == IResource.FILE) {
					IFile aFile = (IFile) aResource;
					String ext = aFile.getFileExtension();
					if ((ext != null) && ext.equals("js")) {
						files.add(aFile);
					}
				} else if (aResource.getType() == IResource.FOLDER) {
					collectJSFiles((IFolder) aResource, files);
				}
			}
		} catch (CoreException e) {
		}
	}
	/**
	 * @param file
	 * 
	 * @return
	 */

	private List getSyntacticElements(IFile file) {
		return (new JSParser()).parse(file);
	}

	/**
	 * @param file
	 * 
	 * @return
	 */
	private List getSyntacticElements(IDocument document) {
		return (new JSParser()).parse(document);
	}
}