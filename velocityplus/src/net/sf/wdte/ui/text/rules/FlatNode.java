/*
 * Copyright (c) 2003-2004 Widespace, OU and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 * 
 * Contributors:
 *     Igor Malinin - initial contribution
 * 
 * $Id: FlatNode.java,v 1.2 2004/02/29 17:54:37 l950637 Exp $
 */

package net.sf.wdte.ui.text.rules;

/**
 * @author Igor Malinin
 */
public class FlatNode {

	/** Content-type of the node */
	public final String type;

	/** Flat offset of the node */
	public int offset;

	/** Flat length of the node */
	public int length;

	public FlatNode(String type) {
		this.type = type;
	}

	/**
	 * Checks whether the given offset is inside of this position's text range.
	 * 
	 * @param offset
	 *            the offset to check
	 * @return <code>true</code> if offset is inside of this position
	 */
	public boolean includes(int offset) {
		return (this.offset <= offset && offset < this.offset + length);
	}

	/**
	 * Checks whether the intersection of the given text range and the text
	 * range represented by this position is empty or not.
	 * 
	 * @param offset
	 *            the offset of the range to check
	 * @param length
	 *            the length of the range to check
	 * @return <code>true</code> if intersection is not empty
	 */
	public boolean overlapsWith(int offset, int length) {
		int end = offset + length;
		int thisEnd = this.offset + this.length;

		if (length > 0) {
			if (this.length > 0) {
				return (this.offset < end && offset < thisEnd);
			}
			return (offset <= this.offset && this.offset < end);
		}

		if (this.length > 0) {
			return (this.offset <= offset && offset < thisEnd);
		}

		return (this.offset == offset);
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FlatNode[" + type + ", " + offset + ", " + length + "]";
	}
}
