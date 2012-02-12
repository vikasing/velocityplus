/*
 * Copyright (c) 2004 Christopher Lenz and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Christopher Lenz - initial version based on the internal Eclipse class
 *                        org.eclipse.ui.internal.ide.misc.OverlayIcon
 * 
 * $Id: OverlayImageDescriptor.java,v 1.1 2004/02/19 08:38:58 cell Exp $
 */

package net.sf.wdte.ui.views.util;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * An overlay image descriptor can add several overlay icons to a base image.
 */
public class OverlayImageDescriptor extends CompositeImageDescriptor {

	// Instance Variables ------------------------------------------------------

	/** Size of the resulting composite image. */
	private Point size = null;

	/** The base image. */
	private ImageDescriptor base;
	
	/** The overlay images. */
	private ImageDescriptor overlays[][];

	// Constructors ------------------------------------------------------------

	/**
	 * Creates the image descriptor. The size of the resulting image will be the
	 * same as the size of the base image.
	 * 
	 * @param base the descriptor of the base image
	 * @param overlays descriptors of the overlay images
	 */
	public OverlayImageDescriptor(ImageDescriptor base,
			ImageDescriptor[][] overlays) {
		this(base, overlays, null);
	}

	/**
	 * Creates the image descriptor.
	 * 
	 * @param base the descriptor of the base image
	 * @param overlays descriptors of the overlay images
	 * @param size the size of the composite image, or <tt>null</tt> to use the
	 *        size of the base image
	 */
	public OverlayImageDescriptor(ImageDescriptor base,
			ImageDescriptor[][] overlays, Point size) {
		this.base = base;
		this.overlays = overlays;
		if (size == null) {
			ImageData data = base.getImageData();
			size = new Point(data.width, data.height); 
		}
		this.size = size;
	}

	// CompositeImageDescriptor Implementation ---------------------------------

	/* 
	 * @see CompositeImageDescriptor#drawCompositeImage(int, int)
	 */
	@Override
	protected void drawCompositeImage(int width, int height) {
		ImageData bg;
		if ((base == null) || ((bg = base.getImageData()) == null)) {
			bg = DEFAULT_IMAGE_DATA;
		}
		drawImage(bg, 0, 0);
		if (overlays != null) {
			if (overlays.length > 0) {
				drawTopRight(overlays[0]);
			}
			if (overlays.length > 1) {
				drawBottomRight(overlays[1]);
			}
			if (overlays.length > 2) {
				drawBottomLeft(overlays[2]);
			}
			if (overlays.length > 3) {
				drawTopLeft(overlays[3]);
			}
		}
	}

	/* 
	 * @see CompositeImageDescriptor#getSize()
	 */
	@Override
	protected Point getSize() {
		return size;
	}

	// Private Methods ---------------------------------------------------------

	private void drawBottomLeft(ImageDescriptor[] descriptors) {
		if (descriptors == null) {
			return;
		}
		int length = descriptors.length;
		int x = 0;
		for (int i = 0; i < 3; i++) {
			if ((i < length) && (descriptors[i] != null)) {
				ImageData id = descriptors[i].getImageData();
				drawImage(id, x, getSize().y - id.height);
				x += id.width;
			}
		}
	}

	private void drawBottomRight(ImageDescriptor[] descriptors) {
		if (descriptors == null) {
			return;
		}
		int length = descriptors.length;
		int x = getSize().x;
		for (int i = 2; i >= 0; i--) {
			if (i < length && descriptors[i] != null) {
				ImageData id = descriptors[i].getImageData();
				x -= id.width;
				drawImage(id, x, getSize().y - id.height);
			}
		}
	}

	private void drawTopLeft(ImageDescriptor[] descriptors) {
		if (descriptors == null) {
			return;
		}
		int length = descriptors.length;
		int x = 0;
		for (int i = 0; i < 3; i++) {
			if (i < length && descriptors[i] != null) {
				ImageData id = descriptors[i].getImageData();
				drawImage(id, x, 0);
				x += id.width;
			}
		}
	}

	private void drawTopRight(ImageDescriptor[] descriptors) {
		if (descriptors == null) {
			return;
		}
		int length = descriptors.length;
		int x = getSize().x;
		for (int i = 2; i >= 0; i--) {
			if (i < length && descriptors[i] != null) {
				ImageData id = descriptors[i].getImageData();
				x -= id.width;
				drawImage(id, x, 0);
			}
		}
	}

}
