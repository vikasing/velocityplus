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
 * $Id: JSModelAdapterFactory.java,v 1.1 2004/02/27 18:29:11 cell Exp $
 */

package net.sf.wdte.js.ui.model;

import net.sf.wdte.js.core.model.JSElement;
import net.sf.wdte.js.core.model.JSElementList;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * Factory that creates adapters for JavaScript model objects.
 */
public class JSModelAdapterFactory implements IAdapterFactory {

	// Static Methods ----------------------------------------------------------

	/**
	 * Creates and registers this adapter factory with the given manager.
	 * 
	 * @param manager the adapter manager to register with
	 */
	public static void register(IAdapterManager manager) {
		JSModelAdapterFactory factory = new JSModelAdapterFactory();
		manager.registerAdapters(factory, JSElement.class);
		manager.registerAdapters(factory, JSElementList.class);
	}

	// IAdapterFactory Implementation ------------------------------------------

	/* 
	 * @see IAdapterFactory#getAdapter(Object, Class)
	 */
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IWorkbenchAdapter.class) {
			if ((adaptableObject instanceof JSElement)
			 || (adaptableObject instanceof JSElementList)) {
				return new JSWorkbenchAdapter();
			}
		}
		return null;
	}

	/* 
	 * @see IAdapterFactory#getAdapterList()
	 */
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IWorkbenchAdapter.class };
	}

}
