/*
 * Copyright (c) 2004 Christopher Lenz and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Christopher Lenz - initial API and implementation
 * 
 * $Id: BrowserPreview.java,v 1.2 2004/02/19 17:32:52 cell Exp $
 */

package net.sf.wdte.ui.views.preview;

import net.sf.wdte.ui.internal.WebUIMessages;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

/**
 * 
 */
public class BrowserPreview extends PageBookView {

	/* 
	 * @see PageBookView#createDefaultPage(org.eclipse.ui.part.PageBook)
	 */
	@Override
	protected IPage createDefaultPage(PageBook book) {
		MessagePage page = new MessagePage();
		page.createControl(book);
		page.setMessage(WebUIMessages.getString(
				"BrowserPreview.notAvailable")); //$NON-NLS-1$);
		return page;
	}

	/* 
	 * @see PageBookView#doCreatePage(org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	protected PageRec doCreatePage(IWorkbenchPart part) {
		// Try to get an outline page.
		Object adapter = part.getAdapter(IBrowserPreviewPage.class);
		if (adapter instanceof IBrowserPreviewPage) {
			IBrowserPreviewPage page = (IBrowserPreviewPage) adapter;
			if (page instanceof IPageBookViewPage) {
				initPage((IPageBookViewPage) page);
			}
			page.createControl(getPageBook());
			return new PageRec(part, page);
		}
		// There is no preview
		return null;
	}

	/* 
	 * @see PageBookView#doDestroyPage(IWorkbenchPart, PageBookView.PageRec)
	 */
	@Override
	protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord) {
		IBrowserPreviewPage page = (IBrowserPreviewPage) pageRecord.page;
		page.dispose();
		pageRecord.dispose();
	}

	/* 
	 * @see PageBookView#getBootstrapPart()
	 */
	@Override
	protected IWorkbenchPart getBootstrapPart() {
		IWorkbenchPage page = getSite().getPage();
		if (page != null) {
			return page.getActiveEditor();
		}
		return null;
	}

	/* 
	 * @see PageBookView#isImportant(IWorkbenchPart)
	 */
	@Override
	protected boolean isImportant(IWorkbenchPart part) {
		return (part instanceof IEditorPart);
	}

}
