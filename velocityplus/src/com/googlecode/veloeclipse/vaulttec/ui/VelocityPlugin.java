package com.googlecode.veloeclipse.vaulttec.ui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import net.sf.wdte.js.ui.editors.JSImages;
import net.sf.wdte.js.ui.model.JSModelAdapterFactory;
import net.sf.wdte.js.ui.preferences.PreferenceNames;

import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.internal.Utilities;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.googlecode.veloeclipse.editor.compare.CompareDialog;
import com.googlecode.veloeclipse.preferences.GeneralPreferencePage;
import com.googlecode.veloeclipse.vaulttec.ui.editor.VelocityEditorEnvironment;

/**
 * The main plugin class to be used in the desktop.
 */
public class VelocityPlugin extends AbstractUIPlugin
{
    

    /*
     * Note, we use to try and dynamically load these dtds.. However, the DTD parser
     * that we use from http://www.wutka.com/dtdparser.html pukes when parsing STRICT40
     * and LOOSE40.  So the only DTD we load now is STRICTX, and we store the dtd locally
     * instead of going onto the web.  It is loaded in VelocityEditor.    
     * @see VelocityEditor.getHTMLElement(String)
     */

    /**
     * The id of the Velocity plugin (value
     * <code>"com.googlecode.veloeclipse.vaulttec.ui"</code>).
     */
    public static final String    PLUGIN_ID       = "com.googlecode.veloeclipse.vaulttec.ui";
    /** The shared instance. */
    private static VelocityPlugin fPlugin;
    private static final String   RESOURCE_NAME   = PLUGIN_ID + ".messages";
    private ResourceBundle        fResourceBundle;
	private List currentFunctions;


    public boolean isAutoCompletionEnabled()
    {
        return getDefault().getPreferenceStore().getBoolean(GeneralPreferencePage.P_Completion);
    }

    public boolean isUppercaseEnabled()
    {
        return getDefault().getPreferenceStore().getBoolean(GeneralPreferencePage.P_CASE);
    }
   
    /*
    public boolean noShowEditorPreviewTab()
    {
        return getDefault().getPreferenceStore().getBoolean(GeneralPreferencePage.P_PREVIEW);
    }
     */
    
    public VelocityPlugin()
    {
        fPlugin = this;
      
        try
        {
            fResourceBundle = ResourceBundle.getBundle(RESOURCE_NAME);
        }
        catch (Exception e)
        {
            log(e);
            fResourceBundle = null;
        }
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeDefaultPluginPreferences()
     */
    @Override
	protected void initializeDefaultPluginPreferences()
    {
        IPreferenceStore aStore = getPreferenceStore();
        aStore.setDefault(IPreferencesConstants.EDITOR_SHOW_SEGMENTS, false);
        aStore.setDefault(IPreferencesConstants.VELOCITY_COUNTER_NAME, "velocityCount");
        aStore.setDefault(IPreferencesConstants.VELOCITY_USER_DIRECTIVES, "");
        aStore.setDefault(IPreferencesConstants.P_AUTO_OUTLINE, true);        
        VelocityColorProvider.initializeDefaults(aStore);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    /**
     * Sets default preference values. These values will be used until some
     * preferences are actually set using the preference dialog.
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeDefaultPreferences(org.eclipse.jface.preference.IPreferenceStore)
     */
    /* protected void initializeDefaultPreferences(IPreferenceStore store)
     {
    	store.setDefault(PreferenceNames.P_AUTO_OUTLINE, true);
 		// TODO Use PreferenceConverter for color/string conversion
 		store.setDefault(PreferenceNames.P_COMMENT_COLOR, "63,127,95");
 		store.setDefault(PreferenceNames.P_STRING_COLOR, "42,0,255");
 		store.setDefault(PreferenceNames.P_KEYWORD_COLOR, "127,0,85");
 		store.setDefault(PreferenceNames.P_DEFAULT_COLOR, "0,0,0");
     }*/
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent anEvent)
    {
        String prop = anEvent.getProperty();
        VelocityEditorEnvironment.getColorProvider().handlePreferenceStoreChanged(anEvent);
        if (prop.equals(IPreferencesConstants.VELOCITY_USER_DIRECTIVES) || prop.equals(IPreferencesConstants.LIBRARY_LIST))
        {
            VelocityEditorEnvironment.createVelocityParser();
        }
    }

    /**
     * Returns Velocity user directives.
     */
    public static List getVelocityUserDirectives()
    {
        IPreferenceStore store = getDefault().getPreferenceStore();
        String directives = store.getString(IPreferencesConstants.VELOCITY_USER_DIRECTIVES);
        StringTokenizer st = new StringTokenizer(directives, ",\n\r");
        ArrayList list = new ArrayList();
        while (st.hasMoreElements())
        {
            list.add(st.nextElement());
        }
        return list;
    }

    /**
     * Returns the shared instance.
     */
    public static VelocityPlugin getDefault()
    {
        return fPlugin;
    }

    /**
     * Returns the workspace instance.
     */
    public static IWorkspace getWorkspace()
    {
        return ResourcesPlugin.getWorkspace();
    }

    /**
     * Returns the plugin's resource bundle,
     */
    public ResourceBundle getResourceBundle()
    {
        return fResourceBundle;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static Shell getActiveWorkbenchShell()
    {
        IWorkbenchWindow window = getActiveWorkbenchWindow();
        return ((window != null) ? window.getShell() : null);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static IWorkbenchWindow getActiveWorkbenchWindow()
    {
        return getDefault().getWorkbench().getActiveWorkbenchWindow();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String getPluginId()
    {
        return getDefault().getBundle().getSymbolicName();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    // public static IPath getInstallLocation()
    // {
    // return new Path(getInstallURL().getFile());
    // }
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static URL getInstallURL()
    {
        return getDefault().getBundle().getEntry("/");
    }

    public static String getInstallPath()
    {
        String loc = "";
        URL url = VelocityPlugin.getDefault().getBundle().getEntry("/");
        try
        {
            loc = FileLocator.resolve(url).toString();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return loc;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param aStatus
     *            DOCUMENT ME!
     */
    public static void log(IStatus aStatus)
    {
        getDefault().getLog().log(aStatus);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param aThrowable
     *            DOCUMENT ME!
     */
    public static void log(Throwable aThrowable)
    {
        log(new Status(IStatus.ERROR, getPluginId(), IStatus.OK, getMessage("VelocityPlugin.internal_error"), aThrowable));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param aMessage
     *            DOCUMENT ME!
     */
    public static void logErrorMessage(String aMessage)
    {
        log(new Status(IStatus.ERROR, getPluginId(), IStatus.OK, aMessage, null));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param aMessage
     *            DOCUMENT ME!
     * @param aStatus
     *            DOCUMENT ME!
     */
    public static void logErrorStatus(String aMessage, IStatus aStatus)
    {
        if (aStatus == null)
        {
            logErrorMessage(aMessage);
        } else
        {
            MultiStatus multi = new MultiStatus(getPluginId(), IStatus.OK, aMessage, null);
            multi.add(aStatus);
            log(multi);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static boolean isDebug()
    {
        return getDefault().isDebugging();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param anOption
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static boolean isDebug(String anOption)
    {
        boolean debug;
        if (isDebug())
        {
            String value = Platform.getDebugOption(anOption);
            debug = (((value != null) && value.equalsIgnoreCase("true")) ? true : false);
        } else
        {
            debug = false;
        }
        return debug;
    }

    /**
     * Returns the string from the plugin's resource bundle, or 'aKey' if not
     * found.
     */
    public static String getMessage(String aKey)
    {
        String bundleString;
        ResourceBundle bundle = getDefault().getResourceBundle();
        if (bundle != null)
        {
            try
            {
                bundleString = bundle.getString(aKey);
            }
            catch (MissingResourceException e)
            {
                log(e);
                bundleString = "!" + aKey + "!";
            }
        } else
        {
            bundleString = "!" + aKey + "!";
        }
        return bundleString;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param aKey
     *            DOCUMENT ME!
     * @param anArg
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static String getFormattedMessage(String aKey, String anArg)
    {
        return getFormattedMessage(aKey, new String[] { anArg });
    }

    /**
     * DOCUMENT ME!
     * 
     * @param aKey
     *            DOCUMENT ME!
     * @param anArgs
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static String getFormattedMessage(String aKey, Object[] anArgs)
    {
        return MessageFormat.format(getMessage(aKey), anArgs);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param key
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static String getResourceString(String key)
    {
        ResourceBundle bundle = getDefault().getResourceBundle();
        try
        {
            return bundle.getString(key);
        }
        catch (MissingResourceException e)
        {
            return key;
        }
    }

    public int openCompareDialog(final CompareEditorInput input, String error)
    {
        int i = 0;
        if (compareResultOK(input))
        {
            CompareDialog dialog = new CompareDialog(VelocityPlugin.getActiveWorkbenchShell(), input, error);
            i = dialog.open();
        }
        return i;
    }

    private boolean compareResultOK(CompareEditorInput input)
    {
        final Shell shell = VelocityPlugin.getActiveWorkbenchShell();
        try
        {
            // run operation in separate thread and make it canceable
            PlatformUI.getWorkbench().getProgressService().run(true, true, input);
            String message = input.getMessage();
            if (message != null)
            {
                MessageDialog.openError(shell, Utilities.getString("CompareUIPlugin.compareFailed"), message); //$NON-NLS-1$
                return false;
            }
            if (input.getCompareResult() == null)
            {
                MessageDialog.openInformation(shell, Utilities.getString("CompareUIPlugin.dialogTitle"), Utilities.getString("CompareUIPlugin.noDifferences")); //$NON-NLS-2$ //$NON-NLS-1$
                return false;
            }
            return true;
        }
        catch (InterruptedException x)
        {
            // cancelled by user
        }
        catch (InvocationTargetException x)
        {
            MessageDialog.openError(shell, Utilities.getString("CompareUIPlugin.compareFailed"), x.getTargetException().getMessage()); //$NON-NLS-1$
        }
        return false;
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
	 * @see AbstractUIPlugin#initializeImageRegistry(ImageRegistry)
	 */
	//@Override
	//protected void initializeImageRegistry(ImageRegistry reg) {
		//JSImages.initializeRegistry(reg);
	//}

}