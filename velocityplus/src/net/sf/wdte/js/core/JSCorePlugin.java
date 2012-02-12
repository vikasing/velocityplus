/*
 * $RCSfile: JSCorePlugin.java,v $
 *
 * Copyright 2002
 * CH-1700 Fribourg, Switzerland
 * All rights reserved.
 *
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSCorePlugin.java,v $
 * Revision 1.2  2004/05/22 16:14:37  l950637
 * Adapt for Eclipse 3.0M9
 *
 * Revision 1.1  2004/02/05 03:10:08  agfitzp
 * Initial Submission
 *
 * Revision 1.3  2003/12/10 20:19:16  agfitzp
 * 3.0 port
 *
 * Revision 1.2  2003/06/21 03:48:51  agfitzp
 * fixed global variables as functions bug
 * fixed length calculation of instance variables
 * Automatic outlining is now a preference
 *
 * Revision 1.1  2003/05/28 15:17:12  agfitzp
 * net.sf.wdte.js.core 0.0.1 code base
 *
 *========================================================================
*/

package net.sf.wdte.js.core;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Plugin;


/**
 * The main plugin class to be used in the desktop.
 */
public class JSCorePlugin extends Plugin
{
   //The shared instance.
   private static JSCorePlugin plugin;

   //Resource bundle.
   private ResourceBundle resourceBundle;
   
   private boolean defaultsInitialized = false;
   
   /**
    * current func list
    */
   private List currentFunctions = new LinkedList();
     
   /**
    * The constructor.
    * @param descriptor
    */
   public JSCorePlugin() {
      plugin = this;

      try {
         resourceBundle = ResourceBundle.getBundle("net.sf.wdte.js.core.JSCorePluginResources");
      } catch(MissingResourceException x) {
         resourceBundle = null;
      }
   }

   /**
    * Returns the shared instance.
    * @return
    */
   public static JSCorePlugin getDefault() {
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
    * Returns the string from the plugin's resource bundle, or 'key' if not found.
    * @param key
    * 
    * @return
    */
   public static String getResourceString(String key) {
      ResourceBundle bundle = JSCorePlugin.getDefault().getResourceBundle();

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
	 * Returns the currentFunctions.
	 * @return List
	 */
	public List getCurrentFunctions() {
		return currentFunctions;
	}

	/**
	 * Sets the currentFunctions.
	 * @param currentFunctions The currentFunctions to set
	 */
	public void setCurrentFunctions(List currentFunctions) {
		this.currentFunctions = currentFunctions;
	}
}
