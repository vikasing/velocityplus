/*
 * Created on 29.12.2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.googlecode.veloeclipse.editor;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator2;

public class VelocityMarkerResolutionGenerator implements IMarkerResolutionGenerator2
{

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.IMarkerResolutionGenerator#getResolutions(org.eclipse.core.resources.IMarker)
     */
    @Override
	public IMarkerResolution[] getResolutions(IMarker marker)
    {
        IMarkerResolution resolution = new VelocityResolution();
        return new IMarkerResolution[] { resolution };
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.IMarkerResolutionGenerator2#hasResolutions(org.eclipse.core.resources.IMarker)
     */
    @Override
	public boolean hasResolutions(IMarker marker)
    {
        // TODO Auto-generated method stub
        return true;
    }

    private final class VelocityResolution implements IMarkerResolution
    {

        @Override
		public String getLabel()
        {
            // TODO Auto-generated method stub
            return "not implemented";
        }

        @Override
		public void run(IMarker marker)
        {
            // runIt((IAction) null);
        }
    }
}