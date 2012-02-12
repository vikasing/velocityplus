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
 * $Id: JSNameSorter.java,v 1.2 2004/02/27 18:29:51 cell Exp $
 */

package net.sf.wdte.js.ui.model;

import net.sf.wdte.js.core.model.JSClassElement;
import net.sf.wdte.js.core.model.JSClassMethodElement;
import net.sf.wdte.js.core.model.JSClassVariableElement;
import net.sf.wdte.js.core.model.JSFunctionElement;
import net.sf.wdte.js.core.model.JSGlobalVariableElement;
import net.sf.wdte.js.core.model.JSInstanceMethodElement;
import net.sf.wdte.js.core.model.JSInstanceVariableElement;

import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Name sorter.
 */
public class JSNameSorter extends ViewerSorter {

	// ViewerSorter Implementation ---------------------------------------------

	/*
	 * @see ViewerSorter#category(Object)
	 */
	@Override
	public int category(Object element) {
		if (element instanceof JSClassElement) {
			return JSElementCategories.CLASS;
		} else if (element instanceof JSFunctionElement) {
			return JSElementCategories.FUNCTION;
		} else if (element instanceof JSGlobalVariableElement) {
			return JSElementCategories.VARIABLE;
		} else if (element instanceof JSClassVariableElement) {
			return JSElementCategories.CLASS_VARIABLE;
		} else if (element instanceof JSInstanceVariableElement) {
			return JSElementCategories.INSTANCE_VARIABLE;
		} else if (element instanceof JSClassMethodElement) {
			return JSElementCategories.CLASS_METHOD;
		} else if (element instanceof JSInstanceMethodElement) {
			return JSElementCategories.INSTANCE_VARIABLE;
		}
		return super.category(element);
	}

}
