/*
 * Copyright (c) 2004 Christopher Lenz and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Christopher Lenz - initial API and implementation
 * 
 * $Id: StructuredTextEditor.java,v 1.2 2004/02/20 17:14:05 cell Exp $
 */

package net.sf.wdte.ui.editor;

import net.sf.wdte.core.model.ISourceModel;
import net.sf.wdte.core.model.ISourceReference;
import net.sf.wdte.ui.text.IReconcilingParticipant;
import net.sf.wdte.ui.views.outline.ModelBasedOutlinePage;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IUpdate;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * Abstract base class for editors that keep a source model synchronized with
 * the textual contants being edited.
 */
public abstract class StructuredTextEditor extends TextEditor
	implements IReconcilingParticipant {

	// Inner Classes -----------------------------------------------------------

    /**
     * Listens to changes to the selection in the outline page, and changes the
     * selection and highlight range in the editor accordingly.
     */
    private class OutlineSelectionChangedListener
        implements ISelectionChangedListener {

        /*
         * @see ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
         */
        @Override
		public void selectionChanged(SelectionChangedEvent event) {
            IStructuredSelection selection =
                (IStructuredSelection) event.getSelection();
            if (selection.isEmpty()) {
                resetHighlightRange();
            } else {
                ISourceReference element = (ISourceReference)
					selection.getFirstElement();
                highlightElement(element, true);
            }
        }

    }

	// Instance Variables ------------------------------------------------------

	/**
	 * The associated outline page.
	 */
	private IContentOutlinePage outlinePage;

	/**
	 * Listens to changes in the outline page's selection to update the editor
	 * selection and highlight range.
	 */
	private ISelectionChangedListener outlinePageSelectionListener;

	// TextEditor Implementation -----------------------------------------------

	/*
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IContentOutlinePage.class)) {
			if (outlinePage == null) {
				outlinePage = createOutlinePage();
                outlinePageSelectionListener =
                    new OutlineSelectionChangedListener();
                outlinePage.addSelectionChangedListener(
                outlinePageSelectionListener);
			}
			return outlinePage;
		}
		return super.getAdapter(adapter);
	}

	/*
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#handleCursorPositionChanged()
	 */
	@Override
	protected void handleCursorPositionChanged() {
		super.handleCursorPositionChanged();
		highlightElement(computeHighlightRangeSourceReference(), false);
		synchronizeOutlinePageSelection();
	}

	// IReconcilingParticipant Implementation ----------------------------------

	/* 
	 * @see IReconcilingParticipant#reconciled()
	 */
	@Override
	public void reconciled() {
		Shell shell = getSite().getShell();
		if ((shell != null) && !shell.isDisposed()) {
			shell.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (outlinePage instanceof IUpdate) {
						((IUpdate) outlinePage).update();
					}
					synchronizeOutlinePageSelection();
				}
			});
		}
	}

	// Public Methods ----------------------------------------------------------

	/**
	 * Computes and returns the source reference that includes the caret and
	 * serves as provider for the outline page selection and the editor range
	 * indication.
	 * 
	 * @return the computed source reference
	 */
	public ISourceReference computeHighlightRangeSourceReference() {
		ISourceViewer sourceViewer = getSourceViewer();
		if (sourceViewer == null) {
			return null;
		}
		StyledText styledText = sourceViewer.getTextWidget();
		if ((styledText == null) || styledText.isDisposed()) {
			return null;
		}
		int offset = sourceViewer.getVisibleRegion().getOffset();
		int caret = offset + styledText.getCaretOffset();

		return getElementAt(caret);
	}

	/**
	 * Returns the source model element at the specified offset.
	 * 
	 * @param offset the offset into the document
	 * @return the element at the given offset, or <tt>null</tt> if no model is
	 *         available or there is no element at the offset
	 */
	public ISourceReference getElementAt(int offset) {
		ISourceReference retVal = null;
		ISourceModel model = getSourceModel();
		if (model != null) {
			ISourceReference elements[] = model.getElements();
			retVal = getElementAt(model, elements, offset);
		}
		return retVal;
	}

	/**
	 * Returns the structure source model corresponding to the document
	 * currently being edited.
	 * 
	 * Concrete implementations must implement this method to return the model
	 * appropriate to the content being edited.
	 * 
	 * @return the source model
	 */
	public abstract ISourceModel getSourceModel();

	/**
	 * Informs the editor that its outliner has been closed.
	 * 
	 * TODO There must be a more elegant way to get notified when the outline 
	 *      page was closed. Otherwise move this method into an interface
	 */
	public void outlinePageClosed() {
		if (outlinePage != null) {
			outlinePage.removeSelectionChangedListener(
					outlinePageSelectionListener);
			outlinePage = null;
			resetHighlightRange();
		}
	}

	/**
	 * Synchronizes the outliner selection with the given element position in 
	 * the editor.
	 * 
	 * @param element the java element to select
	 */
	public void synchronizeOutlinePage(ISourceReference element) {
		if (outlinePage != null) {
			outlinePage.removeSelectionChangedListener(
				outlinePageSelectionListener);
			if (outlinePage instanceof ModelBasedOutlinePage) {
				((ModelBasedOutlinePage) outlinePage).select(element);
			}
			outlinePage.addSelectionChangedListener(
				outlinePageSelectionListener);
		}
	}

	/**
	 * Synchronizes the outliner selection with the currently highlighted source
	 * reference.
	 */
	public void synchronizeOutlinePage() {
		ISourceReference element = computeHighlightRangeSourceReference();
		synchronizeOutlinePage(element);
	}

	// Protected Methods -------------------------------------------------------

	protected abstract IContentOutlinePage createOutlinePage();

	/**
	 * Highlights the given element.
	 * 
	 * @param element the element that should be highlighted
	 * @param moveCursor whether the cursor should be moved to the element
	 */
	protected final void highlightElement(ISourceReference element,
			boolean moveCursor) {
		if (element != null) {
			IRegion highlightRegion = element.getSourceRegion();
			setHighlightRange(highlightRegion.getOffset(),
				highlightRegion.getLength(), moveCursor);
		} else {
			resetHighlightRange();
		}
	}

	/**
	 * Returns whether the outline page is currently linked with the editor,
	 * meaning that its selection should automatically be updated to reflect the
	 * current cursor position.
	 * 
	 * @return <tt>true</tt> if the outline page is linked with the editor,
	 *         <tt>false</tt> otherwise
	 */
	protected abstract boolean isOutlineLinkedWithEditor();

	// Private Methods ---------------------------------------------------------

	/**
	 * Recursively searches the specified list of elements managed by the given
	 * model for the element that covers the specified offfset with minimal 
	 * padding.
	 * 
	 * @param model the source model
	 * @param elements the current list of elements
	 * @param offset the offset into the document
	 * @return the model element at the specified offset, or <tt>null</tt> if
	 *         no element could be found
	 */
	private static ISourceReference getElementAt(
			ISourceModel model, ISourceReference elements[], int offset) {
		ISourceReference retVal = null;
		for (int i = 0; i < elements.length; i++) {
			ISourceReference element = elements[i];
			IRegion region = element.getSourceRegion();
			if ((offset > region.getOffset())
			 && (offset < (region.getOffset() + region.getLength()))) {
				ISourceReference[] children = model.getChildren(element);
				if (children.length > 0) {
					retVal = getElementAt(model, children, offset);
					if (retVal != null) {
						break;
					}
				}
				if (retVal == null) {
					retVal = element;
				}
			}
		}
		return retVal;
	}

	private void synchronizeOutlinePageSelection() {
		IPreferenceStore store = getPreferenceStore();
		if (store != null) {
			boolean linkWithEditor = isOutlineLinkedWithEditor();
			if (linkWithEditor) {
				synchronizeOutlinePage(computeHighlightRangeSourceReference());
			}
		}
	}

}
