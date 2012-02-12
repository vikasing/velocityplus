/*
 * Copyright (c) 2002-2004 Adrian Dinu and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Adrian Dinu - initial implementation
 *     Alex Fitzpatrick - additional images
 *     Christopher Lenz - migration to use the plugin's image registry
 * 
 * $Id: JSImages.java,v 1.4 2004/05/22 16:14:22 l950637 Exp $
 */

package net.sf.wdte.js.ui.editors;

import java.net.URL;

import net.sf.wdte.js.ui.JSUIPlugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * Convenience class for storing references to image descriptors used by the JS
 * editor.
 */
public class JSImages {

	public static final String ICON_VAR =
		"global_variable.gif"; //$NON-NLS-1$
	public static final String ICON_FUNCTION =
		"func.gif"; //$NON-NLS-1$
	public static final String ICON_CLASS =
		"class_obj.gif"; //$NON-NLS-1$
	public static final String ICON_DYNAMIC_CLASS =
		"dyn_class_obj.gif"; //$NON-NLS-1$
	public static final String ICON_CLASS_METHOD =
		"class_method.gif"; //$NON-NLS-1$
	public static final String ICON_INSTANCE_METHOD =
		"instance_method.gif"; //$NON-NLS-1$
	public static final String ICON_CLASS_VAR =
		"class_variable.gif"; //$NON-NLS-1$
	public static final String ICON_INSTANCE_VAR =
		"instance_variable.gif"; //$NON-NLS-1$

	/**
	 * Returns the image for the specified key, or <tt>null</tt> if no image for
	 * that key is found.
	 * 
	 * @param key the key under which the image was registered
	 * @return the image, or <tt>null</tt> if none
	 */
	public static Image get(String key) {
		return JSUIPlugin.getDefault().getImageRegistry().get(key);
	}

	/**
	 * Returns the image descriptor for the specified key, or <tt>null</tt> if
	 * no image for that key is found.
	 * 
	 * @param key the key under which the image was registered
	 * @return the image descriptor, or <tt>null</tt> if none
	 */
	public static ImageDescriptor getDescriptor(String key) {
		return JSUIPlugin.getDefault().getImageRegistry().getDescriptor(key);
	}

	/**
	 * Initializes the given image registry with all images provided through
	 * this class.
	 * 
	 * @param reg the registry to initialize
	 */
	public static void initializeRegistry(ImageRegistry reg) {
		reg.put(ICON_VAR, createImageDescriptor(ICON_VAR));
		reg.put(ICON_FUNCTION, createImageDescriptor(ICON_FUNCTION));
		reg.put(ICON_CLASS, createImageDescriptor(ICON_CLASS));
		reg.put(ICON_DYNAMIC_CLASS,	createImageDescriptor(ICON_DYNAMIC_CLASS));
		reg.put(ICON_CLASS_METHOD, createImageDescriptor(ICON_CLASS_METHOD));
		reg.put(ICON_INSTANCE_METHOD,
				createImageDescriptor(ICON_INSTANCE_METHOD));
		reg.put(ICON_CLASS_VAR,	createImageDescriptor(ICON_CLASS_VAR));
		reg.put(ICON_INSTANCE_VAR, createImageDescriptor(ICON_INSTANCE_VAR));
	}

	/**
	 * Utility method to create an <code>ImageDescriptor</code> from a path to a
	 * file.
	 * 
	 * @param path the full path to the image file
	 * @return the image descriptor
	 */
	private static ImageDescriptor createImageDescriptor(String path) {
		try {
			URL url = JSUIPlugin.getDefault().getBundle()
				.getEntry("/icons/" + path); //$NON-NLS-1$
			return ImageDescriptor.createFromURL(url);
		} catch (IllegalStateException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}
}
