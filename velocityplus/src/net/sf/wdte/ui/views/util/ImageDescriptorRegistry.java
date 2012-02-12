/*
 * Copyright (c) 2004 Christopher Lenz and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Christopher Lenz - initial implementation based on the internal Eclipse
 *                        class of the same name, defined in multiple packages
 * 
 * $Id: ImageDescriptorRegistry.java,v 1.1 2004/02/19 08:38:58 cell Exp $
 */

package net.sf.wdte.ui.views.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Registry that keeps a table of image descriptors and the images created from
 * those descriptors.
 */
public class ImageDescriptorRegistry {

	/**
	 * Stores all registered image descriptors as keys, and the images created
	 * from them as values.
	 */
	private Map registry = new HashMap(10);

	// Constructors ------------------------------------------------------------

	/**
	 * Creates a new image descriptor registry for the current or default
	 * display, respectively.
	 */
	public ImageDescriptorRegistry() {
		this(Display.getCurrent() != null ?
				Display.getCurrent() : Display.getDefault());
	}

	/**
	 * Creates a new image descriptor registry for the given display. All images
	 * managed by this registry will be disposed when the display gets disposed.
	 * 
	 * @param display the display the images managed by this registry are
	 *        allocated for 
	 */
	public ImageDescriptorRegistry(Display display) {
		display.disposeExec(new Runnable() {
			@Override
			public void run() {
				dispose();
			}	
		});
	}
	
	/**
	 * Returns the image assiciated with the given image descriptor.
	 * 
	 * @param descriptor the image descriptor for which the registry manages an
	 *        image
	 * @return the image associated with the image descriptor or <tt>null</tt>
	 *         if the image descriptor can't create the requested image.
	 */
	public Image get(ImageDescriptor descriptor) {
		if (descriptor == null) {
			descriptor = ImageDescriptor.getMissingImageDescriptor();
		}
		Image result = (Image) registry.get(descriptor);
		if (result == null) {
			result = descriptor.createImage();
			if (result != null) {
				registry.put(descriptor, result);
			}
		}
		return result;
	}

	/**
	 * Disposes all images managed by this registry.
	 */	
	public void dispose() {
		for (Iterator i = registry.values().iterator(); i.hasNext(); ) {
			((Image) i.next()).dispose();
		}
		registry.clear();
	}
	
}
