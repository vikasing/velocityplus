/*
 * Created on May 15, 2003
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSClassElement.java,v $
 * Revision 1.1  2004/02/26 02:25:42  agfitzp
 * renamed packages to match xml & css
 *
 * Revision 1.1  2004/02/05 03:10:08  agfitzp
 * Initial Submission
 *
 * Revision 1.1.2.1  2003/12/12 21:37:24  agfitzp
 * Experimental work for Classes view
 *
 * Revision 1.2  2003/05/30 20:53:09  agfitzp
 * 0.0.2 : Outlining is now done as the user types. Some other bug fixes.
 *
 *
 *========================================================================
*/
package net.sf.wdte.js.core.model;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;

/**
 * @author fitzpata
 *
 */
public class JSClassElement extends JSElement
{
	protected HashMap childrenByName;
	protected boolean isPrototype = false;

	/**
	 * @param aName
	 * @param offset
	 * @param length
	 */
	public JSClassElement(IFile aFile, String aName, int offset, int length)
	{
		super(aFile, aName, offset, length);
		childrenByName = new HashMap();
	}

	public void addChildElement(JSElement anElement)
	{
		String elementName = anElement.getName();
		if(!childrenByName.containsKey(elementName))
		{
			this.children.add(anElement);
			this.childrenByName.put(elementName, anElement);
			anElement.setParent(this);
		}
	}

	@Override
	public int category()
	{
		return CLASS;	
	}

	/**
	 * @return
	 */
	public boolean isPrototype()
	{
		return isPrototype;
	}

	/**
	 * @param b
	 */
	public void setPrototype(boolean b)
	{
		isPrototype = b;
	}

}
