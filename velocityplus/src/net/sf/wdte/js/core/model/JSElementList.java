/*
 * $RCSfile: JSElementList.java,v $
 *
 * Copyright 2002
 * CH-1700 Fribourg, Switzerland
 * All rights reserved.
 *
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSElementList.java,v $
 * Revision 1.2  2004/02/27 18:28:10  cell
 * Make model elements platform objects so they are automatically adapted
 *
 * Revision 1.1  2004/02/26 02:25:42  agfitzp
 * renamed packages to match xml & css
 *
 * Revision 1.1  2004/02/05 03:10:08  agfitzp
 * Initial Submission
 *
 * Revision 1.1.2.1  2003/12/12 21:37:24  agfitzp
 * Experimental work for Classes view
 *
 * Revision 1.1  2003/05/30 20:53:08  agfitzp
 * 0.0.2 : Outlining is now done as the user types. Some other bug fixes.
 *
 * Revision 1.1  2003/05/28 15:17:11  agfitzp
 * net.sf.wdte.js.core 0.0.1 code base
 *
 *========================================================================
*/

package net.sf.wdte.js.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.PlatformObject;

/**
 * DOCUMENT ME!
 * 
 * @author Addi 
 */
public class JSElementList extends PlatformObject
{
	protected List children = new ArrayList();

	/**
	 * Creates a new adaptable list with the given children.
	 */
	public JSElementList()
	{
	}

	/**
	 * Creates a new adaptable list with the given children.
	 * @param newChildren
	 */
	public JSElementList(JSElement[] newChildren)
	{
		for (int i = 0; i < newChildren.length; i++)
		{
			children.add(newChildren[i]);
		}
	}

	/**
	 * Creates a new adaptable list with the given children.
	 * @param newChildren
	 */
	public JSElementList(List newChildren)
	{
		for (int i = 0; i < newChildren.size(); i++)
		{
			children.add(newChildren.get(i));
		}
	}

	/**
	 * Adds all the adaptable objects in the given enumeration to this list. Returns this list.
	 * @param iterator
	 * 
	 * @return
	 */
	public JSElementList add(Iterator iterator)
	{
		while (iterator.hasNext())
		{
			add((JSElement) iterator.next());
		}

		return this;
	}

	/**
	 * Adds the given adaptable object to this list. Returns this list.
	 * @param adaptable
	 * 
	 * @return
	 */
	public JSElementList add(JSElement anElement)
	{
		children.add(anElement);

		return this;
	}

	/**
	 * Returns the elements in this list.
	 * @return
	 */
	public Object[] getChildren()
	{
		return children.toArray();
	}

	/**
	 *
	 *
	 * @param o 
	 *
	 * @return 
	 */
	public Object[] getChildren(Object o)
	{
		return children.toArray();
	}

	/**
	 *
	 *
	 * @param object 
	 *
	 * @return 
	 */
	public String getLabel(Object object)
	{
		return object == null ? "" : object.toString();
	}

	/**
	 *
	 *
	 * @param object 
	 *
	 * @return 
	 */
	public Object getParent(Object object)
	{
		return null;
	}

	/**
	 * Removes the given adaptable object from this list.
	 * @param adaptable
	 */
	public void remove(JSElement anElement)
	{
		children.remove(anElement);
	}

	/**
	 * Returns the number of items in the list
	 * @return
	 */
	public int size()
	{
		return children.size();
	}

	public JSElement findEquivilent(JSElement anElement)
	{
		for(int i = 0; i < size();i++)
		{
			JSElement aCandidate = (JSElement) children.get(i);
			if(anElement.equals(aCandidate))
			{
				return aCandidate;
			}
		}
		
		
		return null;
	}

	public JSElement get(int index)
	{
		if(index >= size())
		{
			return null;
		}
		return (JSElement) children.get(index);
	}

	/**
	 * @return
	 */
	public IAdaptable asAdaptable() {
		// TODO Auto-generated method stub
		return null;
	}
}
