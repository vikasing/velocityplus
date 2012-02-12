/*
 * Copyright (c) 2004 Christopher Lenz and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Christopher Lenz - initial implementation
 * 
 * $Id: ModelBasedOutlinePage.java,v 1.1 2004/02/19 17:33:51 cell Exp $
 */

package net.sf.wdte.ui.views.outline;

import java.util.List;

import net.sf.wdte.core.model.ISourceModel;
import net.sf.wdte.core.model.ISourceReference;
import net.sf.wdte.ui.editor.StructuredTextEditor;
import net.sf.wdte.ui.internal.WebUIMessages;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.texteditor.IUpdate;
import org.eclipse.ui.texteditor.ResourceAction;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * 
 */
public class ModelBasedOutlinePage extends ContentOutlinePage
	implements IUpdate {

	// Inner Classes -----------------------------------------------------------

	public class ContentProvider implements ITreeContentProvider {

		/*
		 * ITreeContentProvider#getChildren(Object)
		 */
		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof ISourceReference) {
				return model.getChildren((ISourceReference) parentElement);
			}
			return new Object[0];
		}

		/*
		 * @see ITreeContentProvider#getParent(Object)
		 */
		@Override
		public Object getParent(Object element) {
			if (element instanceof ISourceReference) {
				return model.getParent((ISourceReference) element);
			}
			return null;
		}

		/*
		 * @see ITreeContentProvider#hasChildren(Object)
		 */
		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

		/*
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
		 */
		@Override
		public Object[] getElements(Object inputElement) {
			return model.getElements();
		}

		/* 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		@Override
		public void dispose() {
		}

		/*
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer, Object, Object)
		 */
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (oldInput != newInput) {
				if (newInput instanceof ISourceModel) {
					model = (ISourceModel) newInput;
				}	   
			}
		}

	}

	/**
	 * This action toggles whether this outline page links its selection
	 * to the active editor.
	 */
	private class ToggleLinkingAction extends ResourceAction {
		
		/**
		 * Constructs a new action.
		 */
		public ToggleLinkingAction() {
			super(WebUIMessages.getResourceBundle(),
				"OutlinePage.linkWithEditor."); //$NON-NLS-1$
			if ((preferenceStore != null)
			 && (linkWithEditorPreferenceKey != null)) {
				boolean checked = preferenceStore.getBoolean(
						linkWithEditorPreferenceKey);
				valueChanged(checked, false);
			} else {
				setEnabled(false);
			}
		}

		/*
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {
			if ((preferenceStore != null)
			 && (linkWithEditorPreferenceKey != null)) {
				valueChanged(isChecked(), true);
			}
		}
	
		// Private Methods -----------------------------------------------------

		/**
		 * Updates whether the outline page is linked to the active editor.
		 * 
		 * @param checked Whether linking is enabled
		 * @param store Whether the new state should be written back as a 
		 *        preference
		 */
		private void valueChanged(final boolean checked, boolean store) {
			setChecked(checked);
			BusyIndicator.showWhile(getTreeViewer().getControl().getDisplay(),
				new Runnable() {
					@Override
					public void run() {
						editor.synchronizeOutlinePage();
					}
				});
			if (store) {
				preferenceStore.setValue(
					linkWithEditorPreferenceKey, checked);
			}
		}

	}

	// Instance Variables ------------------------------------------------------

	/**
	 * The associated editor.
	 */
	private StructuredTextEditor editor;

	/**
	 * The structured source model.
	 */
	private ISourceModel model;

	/**
	 * The preference store.
	 */
	private IPreferenceStore preferenceStore;

	/**
	 * The preference key which specifies whether the outline page is linked to
	 * the active editor.
	 */
	private String linkWithEditorPreferenceKey;

	// Constructors ------------------------------------------------------------

	/**
	 * Constructor.
	 * 
	 * @param editor The associated structured text editor
	 */
	public ModelBasedOutlinePage(StructuredTextEditor editor) {
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
		viewer.setContentProvider(new ContentProvider());
	}

	/*
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	@Override
	public void dispose() {
		if (editor != null) {
			editor.outlinePageClosed();
			editor = null;
		}
		super.dispose();
	}

	/*
	 * @see org.eclipse.ui.part.Page#makeContributions(IMenuManager, IToolBarManager, IStatusLineManager)
	 */
	@Override
	public void makeContributions(IMenuManager menuManager,
		IToolBarManager toolBarManager, IStatusLineManager statusLineManager) {
		if (toolBarManager != null) {
			toolBarManager.add(new ToggleLinkingAction());
		}
		super.makeContributions(menuManager, toolBarManager, statusLineManager);
	}

	// IUpdate Implementation --------------------------------------------------

	/**
	 * @see IUpdate#update()
	 */
	@Override
	public void update() {
		ISourceModel model = editor.getSourceModel();
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

	// Public Methods ----------------------------------------------------------

	/**
	 * Selects a specific element in the outline page.
	 * 
	 * @param element the element to select
	 */
	public void select(ISourceReference element) {
		TreeViewer viewer = getTreeViewer();
		if (viewer != null) {
			ISelection selection = viewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection =
					(IStructuredSelection) selection;
				List elements = structuredSelection.toList();
				if (!elements.contains(element)) {
					if (element == null) {
						selection = StructuredSelection.EMPTY;
					} else {
						selection = new StructuredSelection(element);
					}
					viewer.setSelection(selection, true);
				}
			}
		}
	}

	// Protected Methods -------------------------------------------------------

	protected final StructuredTextEditor getEditor() {
		return editor;
	}

	protected final void setPreferenceStore(IPreferenceStore store) {
		preferenceStore = store;
	}

	protected final void setLinkWithEditorPreferenceKey(String key) {
		linkWithEditorPreferenceKey = key;
	}

}
