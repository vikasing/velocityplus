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
 * $Id: TextDoubleClickStrategy.java,v 1.1 2004/02/10 12:39:57 cell Exp $
 */

package net.sf.wdte.ui.text;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

/**
 * @author Igor Malinin
 */
public class TextDoubleClickStrategy implements ITextDoubleClickStrategy {

	/*
	 * @see org.eclipse.jface.text.ITextDoubleClickStrategy#doubleClicked(ITextViewer)
	 */
	@Override
	public void doubleClicked(ITextViewer viewer) {
		int offset = viewer.getSelectedRange().x;
		if (offset < 0) { return; }

		selectWord(viewer, viewer.getDocument(), offset);
	}

	protected void selectWord(ITextViewer textViewer, IDocument document,
			int offset) {
		try {
			int start = offset;
			while (start >= 0) {
				char c = document.getChar(start);

				if (!Character.isUnicodeIdentifierPart(c)) {
					break;
				}

				--start;
			}

			int length = document.getLength();

			int end = offset;
			while (end < length) {
				char c = document.getChar(end);

				if (!Character.isUnicodeIdentifierPart(c)) {
					break;
				}

				++end;
			}

			if (start == end) {
				textViewer.setSelectedRange(start, 0);
			} else {
				textViewer.setSelectedRange(start + 1, end - start - 1);
			}
		} catch (BadLocationException x) {
		}
	}
}
