/*
 * Copyright (c) 2004 Christopher Lenz and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Christopher Lenz - initial version based on the Eclipse class of the same
 *                        name in org.eclipse.jdt.internal.ui.viewsupport
 * 
 * $Id: ImageImageDescriptor.java,v 1.1 2004/02/19 08:38:58 cell Exp $
 */

package net.sf.wdte.ui.views.util;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * <tt>ImageDescriptor</tt> implementation that wraps an existing {@link Image}.
 */
public class ImageImageDescriptor extends ImageDescriptor {

	// Instance Variables ------------------------------------------------------

	/** The wrapped image. */
	private Image image;

	// Constructors ------------------------------------------------------------

	/**
	 * Constructor.
	 * 
	 * @param image the image to wrap
	 */
	public ImageImageDescriptor(Image image) {
		this.image= image;
	}

	// ImageDescriptor Implementation ------------------------------------------

	/*
	 * @see ImageDescriptor#getImageData()
	 */
	@Override
	public ImageData getImageData() {
		return image.getImageData();
	}

	/*
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof ImageImageDescriptor) {
			ImageImageDescriptor other = (ImageImageDescriptor) o;
			if (image.equals(other.image)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return image.hashCode();
	}

}
