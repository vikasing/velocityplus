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
 * $Id: ISourceModel.java,v 1.1 2004/02/19 17:27:54 cell Exp $
 */

package net.sf.wdte.core.model;

/**
 * 
 */
public interface ISourceModel {

	/**
	 * Returns the top-level elements of the source model.
	 * 
	 * @return an array of the top-level elements
	 */
	ISourceReference[] getElements();

	/**
	 * Returns the elements that are direct children of the given element.
	 * 
	 * @param element the element for which to return the child elements
	 * @return an array of child elements, or an empty array if the given
	 *         element has no children
	 */
	ISourceReference[] getChildren(ISourceReference element);

	/**
	 * Returns the direct parent element of the specified element.
	 * 
	 * @param element the element for which the parent should be returned
	 * @return the parent element, or <tt>null</tt> if the specified element
	 *         does not have a parent (meaning it is a top-level element)
	 */
	ISourceReference getParent(ISourceReference element);

}
