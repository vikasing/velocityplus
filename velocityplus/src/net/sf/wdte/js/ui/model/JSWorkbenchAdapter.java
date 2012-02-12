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
 * $Id: JSWorkbenchAdapter.java,v 1.1 2004/02/27 18:29:12 cell Exp $
 */

package net.sf.wdte.js.ui.model;

import net.sf.wdte.js.core.model.JSClassElement;
import net.sf.wdte.js.core.model.JSClassMethodElement;
import net.sf.wdte.js.core.model.JSClassVariableElement;
import net.sf.wdte.js.core.model.JSElement;
import net.sf.wdte.js.core.model.JSElementList;
import net.sf.wdte.js.core.model.JSFunctionElement;
import net.sf.wdte.js.core.model.JSGlobalVariableElement;
import net.sf.wdte.js.core.model.JSInstanceMethodElement;
import net.sf.wdte.js.core.model.JSInstanceVariableElement;
import net.sf.wdte.js.ui.editors.JSImages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.WorkbenchAdapter;

/**
 * Adapter that provides a visual representation of JavaScript model elements.
 */
public class JSWorkbenchAdapter extends WorkbenchAdapter {

	// IWorkbenchAdapter Implementation ----------------------------------------

	/* 
	 * @see IWorkbenchAdapter#getChildren(Object)
	 */
	@Override
	public Object[] getChildren(Object o) {
		if (o instanceof JSElementList) {
			return ((JSElementList) o).getChildren(null);
		} else if (o instanceof JSElement) {
			return ((JSElement) o).getChildren(null);
		}
		return super.getChildren(o);
	}

	/* 
	 * @see IWorkbenchAdapter#getImageDescriptor(Object)
	 */
	@Override
	public ImageDescriptor getImageDescriptor(Object o) {
		if (o instanceof JSClassElement) {
			return JSImages.getDescriptor(JSImages.ICON_CLASS);
		} else if (o instanceof JSGlobalVariableElement) {
			return JSImages.getDescriptor(JSImages.ICON_VAR);
		} else if (o instanceof JSFunctionElement) {
			return JSImages.getDescriptor(JSImages.ICON_FUNCTION);
		} else if (o instanceof JSClassMethodElement) {
			return JSImages.getDescriptor(JSImages.ICON_CLASS_METHOD);
		} else if (o instanceof JSClassVariableElement) {
			return JSImages.getDescriptor(JSImages.ICON_CLASS_VAR);
		} else if (o instanceof JSInstanceMethodElement) {
			return JSImages.getDescriptor(JSImages.ICON_INSTANCE_METHOD);
		} else if (o instanceof JSInstanceVariableElement) {
			return JSImages.getDescriptor(JSImages.ICON_INSTANCE_VAR);
		}
		return super.getImageDescriptor(o);
	}

	/* 
	 * @see IWorkbenchAdapter#getLabel(Object)
	 */
	@Override
	public String getLabel(Object o) {
		if (o instanceof JSElement) {
			return ((JSElement) o).getName();
		}
		return super.getLabel(o);
	}

	/* 
	 * @see IWorkbenchAdapter#getParent(Object)
	 */
	@Override
	public Object getParent(Object o) {
		if (o instanceof JSElement) {
			return ((JSElement) o).getParent(null);
		}
		return super.getParent(o);
	}

}
