/*
 * Copyright (c) 2003-2004 Christopher Lenz and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Christopher Lenz - initial API and implementation
 * 
 * $Id: SourceReference.java,v 1.1 2004/02/19 17:27:54 cell Exp $
 */

package net.sf.wdte.core.model;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

/**
 * Default implementation of {@link ISourceReference} based on
 * {@link IDocument}.
 */
public class SourceReference implements ISourceReference {

	// Instance Variables ------------------------------------------------------

	/** The associated document. */
	private IDocument document;

	/** The region in the document that maps to the source reference. */
	private IRegion sourceRegion;

	// Constructors ------------------------------------------------------------

	/**
	 * Constructor.
	 * 
	 * @param document The document that contains the source reference
	 */
	public SourceReference(IDocument document) {
		this(document, 0, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param document The document that contains the source reference
	 */
	public SourceReference(IDocument document, int offset) {
		this(document, offset, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param document The document that contains the source reference
	 */
	public SourceReference(IDocument document, int offset, int length) {
		this.document = document;
		this.sourceRegion = new Region(offset, length);
	}

	// ISourceReference Implementation -----------------------------------------

	/*
	 * @see ISourceReference#getSource()
	 */
	@Override
	public String getSource() {
		try {
			return document.get(sourceRegion.getOffset(),
				sourceRegion.getLength());
		} catch (BadLocationException e) {
			throw new IllegalStateException(
				"Model not synchronized with document"); //$NON-NLS-1$
		}
	}

	/*
	 * @see ISourceReference#getSourceRegion()
	 */
	@Override
	public IRegion getSourceRegion() {
		return sourceRegion;
	}

	// Public Methods ----------------------------------------------------------

	/**
	 * Sets the source region covered by the element.
	 * 
	 * @param offset the offset of the region
	 * @param length the length of the region
	 */
	public final void setSourceRegion(int offset, int length) {
		sourceRegion = new Region(offset, length);
	}

	/**
	 * Sets the source region covered by the element.
	 * 
	 * @param region the source region to set
	 */
	public final void setSourceRegion(IRegion region) {
		setSourceRegion(region.getOffset(), region.getLength());
	}

	// Protected Methods -------------------------------------------------------

	/**
	 * Returns the underlying document.
	 * 
	 * @return the underlying document
	 */
	protected final IDocument getDocument() {
		return document;
	}

}
