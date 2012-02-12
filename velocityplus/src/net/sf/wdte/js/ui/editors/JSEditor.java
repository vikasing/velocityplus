/*
 * $RCSfile: JSEditor.java,v $
 *
 * Copyright 2002
 * CH-1700 Fribourg, Switzerland
 * All rights reserved.
 *
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSEditor.java,v $
 * Revision 1.4  2004/02/27 17:12:48  cell
 * Use the new outline page
 *
 * Revision 1.3  2004/02/26 02:25:57  agfitzp
 * renamed packages to match xml & css
 *
 * Revision 1.2  2004/02/14 18:36:30  ayashi
 * More UI-Core refactoring... still doesn't work though...
 *
 * Revision 1.1  2004/02/05 03:13:28  agfitzp
 * Initial submission, outline view is broken due to refactoring
 *
 * Revision 1.5.2.1  2003/12/12 21:37:24  agfitzp
 * Experimental work for Classes view
 *
 * Revision 1.5  2003/08/14 15:14:15  agfitzp
 * Removed thread hack from automatic update
 *
 * Revision 1.4  2003/07/04 17:26:56  agfitzp
 * New hack, update in a new thread only if we're not already in the middle of updating
 *
 * Revision 1.3  2003/06/21 03:48:51  agfitzp
 * fixed global variables as functions bug
 * fixed length calculation of instance variables
 * Automatic outlining is now a preference
 *
 * Revision 1.2  2003/05/28 20:47:58  agfitzp
 * Outline the document, not the file.
 *
 * Revision 1.1  2003/05/28 15:17:12  agfitzp
 * net.sf.wdte.js.ui 0.0.1 code base
 *
 *========================================================================
*/

package net.sf.wdte.js.ui.editors;

import net.sf.wdte.js.core.model.JSElement;
import net.sf.wdte.js.ui.internal.outline.JSOutlinePage;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * DOCUMENT ME!
 * 
 * @version $Revision: 1.4 $
 * @author $Author: cell $, $Date: 2004/02/27 17:12:48 $
 */
public class JSEditor extends TextEditor implements ISelectionChangedListener
{
	protected JSColorManager colorManager = new JSColorManager();
	protected JSOutlinePage outlinePage;
	protected JSConfiguration configuration;
	
	protected boolean updating = false;

	/**
	 * Constructor for SampleEditor.
	 */
	public JSEditor()
	{
		super();
		configuration = new JSConfiguration(colorManager);
		
		setSourceViewerConfiguration(configuration);
		setDocumentProvider(new JSDocumentProvider());
	}

	@Override
	public void doSave(IProgressMonitor monitor)
	{
		super.doSave(monitor);

		if (outlinePage != null)
		{
			outlinePage.update();
		}
	}

	/**
	 *
	 */
	@Override
	public void dispose()
	{
		colorManager.dispose();
		super.dispose();
	}

	/*
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#getAdapter(Class)
	 */
	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IContentOutlinePage.class)) {
			outlinePage = new JSOutlinePage(this);
			outlinePage.addSelectionChangedListener(this);
			return outlinePage;
		}
		return super.getAdapter(key);
	}

	/**
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event)
	{
		if (null != event)
		{
			if (event.getSelection() instanceof IStructuredSelection)
			{
				IStructuredSelection sel = (IStructuredSelection) event.getSelection();
				if (null != sel)
				{
					JSElement fe = (JSElement) sel.getFirstElement();
					if (null != fe)
					{
						selectAndReveal(fe.getStart(), fe.getLength());
					}
				}
			}
		}
	}

	/**
	 * Updates all content dependent actions.
	 * 
	 * This might be a hack: We're trapping this update to ensure that the 
	 * outline is always up to date.
	 */
	@Override
	protected void updateContentDependentActions()
	{
		super.updateContentDependentActions();
		
		if(!updating)
		{
			if (configuration.getAutomaticOutliningPreference())
			{
				if (outlinePage != null)
				{
					updating = true;
	
					outlinePage.update();
					updating = false;
				}
			}
		}
	}
}