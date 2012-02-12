/*
 * Copyright (c) 2002-2004 Widespace, OU and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 * 
 * Contributors:
 *     Igor Malinin - initial contribution
 * 
 * $Id: ViewNode.java,v 1.2 2004/02/29 17:54:37 l950637 Exp $
 */

package net.sf.wdte.ui.text.rules;

/**
 * @author Igor Malinin
 */
public class ViewNode extends FlatNode {

	/** Inner view of the document */
	public InnerDocumentView view;

	public ViewNode(String type) {
		super(type);
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ViewNode[" + type + ", " + offset + ", " + length + "]";
	}
}
