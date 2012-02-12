package com.googlecode.veloeclipse.vaulttec.ui.editor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.AbstractTreeViewer;

import com.googlecode.veloeclipse.vaulttec.ui.VelocityPlugin;
import com.googlecode.veloeclipse.vaulttec.ui.VelocityPluginImages;

/**
 * DOCUMENT ME!
 * 
 * @version $Revision: 7 $
 * @author <a href="mailto:akmal.sarhan@gmail.com">Akmal Sarhan </a>
 */
public class CollapseAllAction extends Action
{

    private AbstractTreeViewer fViewer;

    public CollapseAllAction(AbstractTreeViewer aViewer)
    {
        fViewer = aViewer;
        setText(VelocityPlugin.getMessage("VelocityEditor.CollapseAllAction.label"));
        setToolTipText(VelocityPlugin.getMessage("VelocityEditor.CollapseAllAction.tooltip"));
        VelocityPluginImages.setLocalImageDescriptors(this, "collapseall.gif");
    }

    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    @Override
	public void run()
    {
        fViewer.collapseAll();
    }
}
