/*
 * Copyright (c) 2003-2004 Christopher Lenz and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Christopher Lenz - initial API
 * 
 * $Id: ISourceReference.java,v 1.1 2004/02/19 17:27:54 cell Exp $
 */

package net.sf.wdte.core.model;

import org.eclipse.jface.text.IRegion;

/**
 * Common protocol for model elements that have associated source code.
 */
public interface ISourceReference {

	/**
	 * Returns the source code associated with this model element.
	 * 
	 * <p>
	 *   This method extracts the substring from the source buffer containing
	 *   this source element. This corresponds to the source regione that would
	 *   be returned by {@link ISourceReference#getSourceRegion()}</code>.
	 * </p>
	 * 
	 * @return The source code, or <code>null</code> if this element has no 
	 *         associated source code
	 */
	String getSource();

	/**
	 * Returns the source range associated with this element.
	 * 
	 * @return The source region, or <code>null</code> if this element has no 
	 *         associated source code
	 */
	IRegion getSourceRegion();

}
