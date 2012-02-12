/*
 * Copyright (c) 2002-2004 Adrian Dinu and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Adrian Dinu - initial implementation
 *     Alex Fitzpatrick - update outline while editing
 *     Christopher Lenz - use custom content and label providers
 * 
 * $Id: JSOutlinePage.java,v 1.2 2004/02/27 18:34:19 cell Exp $
 */

package net.sf.wdte.js.ui.internal.outline;

import net.sf.wdte.js.core.model.JSElementList;
import net.sf.wdte.js.core.parser.JSSyntaxModelFactory;
import net.sf.wdte.js.ui.model.JSNameSorter;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * Implements an outline page for JavaScript files.
 */
public class JSOutlinePage extends ContentOutlinePage {

	// Instance Variables ------------------------------------------------------

	/** The associated text editor. **/
	protected ITextEditor editor;

	// Constructors ------------------------------------------------------------

	/**
	 * Creates a new JSOutlinePage.
	 * 
	 * @param editor the associated text editor
	 */
	public JSOutlinePage(ITextEditor editor) {
		this.editor = editor;
	}

	// ContentOutlinePage Implementation ---------------------------------------

	/* 
	 * @see org.eclipse.ui.part.IPage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new WorkbenchContentProvider());
		viewer.setLabelProvider(new WorkbenchLabelProvider());
		// TODO Make sorting optional
		viewer.setSorter(new JSNameSorter());
	}

	// Public Methods ----------------------------------------------------------

	/**
	 * Forces the outline page to update its contents.
	 */
	public void update() {
		IDocument document = getDocument();
		JSSyntaxModelFactory factory = JSSyntaxModelFactory.getInstance();
		JSElementList model = factory.getContentOutline(document);
		if (model != null) {
			TreeViewer viewer = getTreeViewer();
			if (viewer != null) {
				Control control = viewer.getControl();
				if ((control != null) && !control.isDisposed()) {
					control.setRedraw(false);
					viewer.setInput(model);
					viewer.expandAll();
					control.setRedraw(true);
				}
			}
		}
	}

	// Private Methods ---------------------------------------------------------

	/**
	 * Returns the document that is open in the associated text editor.
	 * 
	 * @return the document being edited
	 */
	private IDocument getDocument() {
		IDocumentProvider provider = editor.getDocumentProvider();
		return provider.getDocument(editor.getEditorInput());
	}

}
