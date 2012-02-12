/*
 * Copyright (c) 2002-2004 Adrian Dinu and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Adrian Dinu - initial implementation
 * 
 * $Id: JSUIPlugin.java,v 1.7 2004/05/22 16:14:22 l950637 Exp $
 */

package net.sf.wdte.js.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sf.wdte.js.ui.editors.JSImages;
import net.sf.wdte.js.ui.model.JSModelAdapterFactory;
import net.sf.wdte.js.ui.preferences.PreferenceNames;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class JSUIPlugin extends AbstractUIPlugin {

	/** The shared instance. */
	private static JSUIPlugin plugin;

	/** Resource bundle. */
	private ResourceBundle resourceBundle;

	/** The current func list. */
	private List currentFunctions = new LinkedList();

	/**
	 * The constructor.
	 * 
	 * @param descriptor the plugin descriptors
	 */
	public JSUIPlugin() {
		plugin = this;

		try {
			resourceBundle = ResourceBundle.getBundle(
				"net.sf.wdte.js.ui.jseditorPluginResources"); //$NON-NLS-1$
		} catch (MissingResourceException e) {
			resourceBundle = null;
		}
	}

	/**
	 * Returns the shared instance.
	 * @return
	 */
	public static JSUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 * @return
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 * 
	 * @param key
	 * 
	 * @return
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = JSUIPlugin.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		} catch(MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 * @return
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * Returns the list of current functions.
	 * 
	 * @return the current functions
	 */
	public List getCurrentFunctions() {
		return currentFunctions;
	}

	/**
	 * Sets the current list of functions.
	 * 
	 * @param currentFunctions The functions to set
	 */
	public void setCurrentFunctions(List currentFunctions) {
		this.currentFunctions = currentFunctions;
	}

	/*
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		JSModelAdapterFactory.register(Platform.getAdapterManager());
	}
	
	/* 
	 * @see AbstractUIPlugin#initializeDefaultPreferences(IPreferenceStore)
	 */
	@Override
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		store.setDefault(PreferenceNames.P_AUTO_OUTLINE, true);
		// TODO Use PreferenceConverter for color/string conversion
		store.setDefault(PreferenceNames.P_COMMENT_COLOR, "63,127,95");
		store.setDefault(PreferenceNames.P_STRING_COLOR, "42,0,255");
		store.setDefault(PreferenceNames.P_KEYWORD_COLOR, "127,0,85");
		store.setDefault(PreferenceNames.P_DEFAULT_COLOR, "0,0,0");
	}

	/* 
	 * @see AbstractUIPlugin#initializeImageRegistry(ImageRegistry)
	 */
	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		JSImages.initializeRegistry(reg);
	}
}
