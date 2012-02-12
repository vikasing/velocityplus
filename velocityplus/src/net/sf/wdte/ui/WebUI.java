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
 * $Id: WebUI.java,v 1.2 2004/05/22 16:14:47 l950637 Exp $
 */

package net.sf.wdte.ui;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The web development tools UI plugin.
 */
public class WebUI extends AbstractUIPlugin {

	// Constants ---------------------------------------------------------------

	public static final String ICON_OVERLAY_ERROR =
		"full/ovr16/error_co.gif"; //$NON-NLS-1$
	public static final String ICON_OVERLAY_WARNING =
		"full/ovr16/warning_co.gif"; //$NON-NLS-1$

	// Instance Variables ------------------------------------------------------

	/** The shared instance. */
	private static WebUI plugin;

	// Constructors ------------------------------------------------------------

	/**
	 * The constructor.
	 */
	public WebUI() {
		plugin = this;
	}

	// Public Methods ----------------------------------------------------------

	/**
	 * Returns the shared instance.
	 */
	public static WebUI getDefault() {
		return plugin;
	}

	// AbstractUIPlugin Implementation -----------------------------------------

	/*
	 * @see AbstractUIPlugin#initializeImageRegistry(ImageRegistry)
	 */
	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		reg.put(ICON_OVERLAY_ERROR, getImageDescriptor(ICON_OVERLAY_ERROR));
		reg.put(ICON_OVERLAY_WARNING, getImageDescriptor(ICON_OVERLAY_WARNING));
	}

	// Private Methods ---------------------------------------------------------

	/**
	 * Returns an image descriptor for the image corresponding to the specified
	 * key (which is the name of the image file).
	 * 
	 * @param key The key of the image
	 * @return The descriptor for the requested image, or <code>null</code> if 
	 *         the image could not be found
	 */
	private ImageDescriptor getImageDescriptor(String key) {
		try {
			URL url = getBundle().getEntry("/icons/" + key); //$NON-NLS-1$
			return ImageDescriptor.createFromURL(url);
		} catch (IllegalStateException e) {
			return null;
		}
	}
}
