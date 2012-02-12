/*
 * Copyright (c) 2003-2004 Alex Fitzpatrick and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Alex Fitzpatrick - initial implementation
 * 
 * $Id: JSClassesViewContentProvider.java,v 1.4 2004/02/27 18:31:05 cell Exp $
 */

package net.sf.wdte.js.ui.views;

import net.sf.wdte.js.core.model.JSElement;
import net.sf.wdte.js.core.parser.JSSyntaxModelFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider for the JavaScript classes view.
 */
class JSClassesViewContentProvider
	implements IStructuredContentProvider, ITreeContentProvider {
	private IWorkspace invisibleRoot;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object parent) {
		if (parent.equals(ResourcesPlugin.getWorkspace())) {
			if (invisibleRoot == null)
				invisibleRoot = ((IWorkspace) parent);
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object child) {
		if (child instanceof IProject) {
			return ((IProject) child).getWorkspace();
		}

		if (child instanceof JSElement) {
			return ((JSElement) child).getParent(child);
		}
		return null;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parent) {

		if (parent instanceof IWorkspace) {
			return invisibleRoot.getRoot().getProjects();
		}

		if (parent instanceof IProject) {
			return getClasses((IProject) parent);
		}

		if (parent instanceof JSElement) {
			return ((JSElement) parent).getChildren(parent);
		}

		return new Object[0];
	}
	/**
	 * @param project
	 * @return
	 */
	private Object[] getClasses(IProject project) {
		return JSSyntaxModelFactory
			.getInstance()
			.getContentOutline(project)
			.getChildren();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object parent) {
		return getChildren(parent).length > 0;
	}
}
