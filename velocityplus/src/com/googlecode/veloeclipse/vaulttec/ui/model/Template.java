package com.googlecode.veloeclipse.vaulttec.ui.model;

import java.util.Iterator;
import java.util.Vector;

/**
 * DOCUMENT ME!
 * 
 * @version $Revision: 7 $
 * @author <a href="mailto:akmal.sarhan@gmail.com">Akmal Sarhan </a>
 */
public class Template extends AbstractTreeNode implements IBlock
{

    private String fName;
    private Vector fDirectives = new Vector();

    public Template(String aName)
    {
        super(null, -1, -1);
        fName = aName;
    }

    /**
     * @see com.googlecode.veloeclipse.vaulttec.ui.model.IBlock#addDirective(com.googlecode.veloeclipse.vaulttec.ui.model.Directive)
     */
    @Override
	public void addDirective(Directive aDirective)
    {
        fDirectives.add(aDirective);
    }

    /**
     * @see com.googlecode.veloeclipse.vaulttec.ui.model.ITreeNode#getName()
     */
    @Override
	public String getName()
    {
        return fName;
    }

    /**
     * @see com.googlecode.veloeclipse.vaulttec.ui.model.ITreeNode#hasChildren()
     */
    @Override
	public boolean hasChildren()
    {
        return !fDirectives.isEmpty();
    }

    /**
     * @see com.googlecode.veloeclipse.vaulttec.ui.model.ITreeNode#getChildren()
     */
    @Override
	public Object[] getChildren()
    {
        return fDirectives.toArray();
    }

    /**
     * @see com.googlecode.veloeclipse.vaulttec.ui.model.ITreeNode#accept(com.googlecode.veloeclipse.vaulttec.ui.model.ITreeVisitor)
     */
    @Override
	public boolean accept(ITreeVisitor aVisitor)
    {
        boolean more = true;
        // Visit all directives of this template
        Iterator iter = fDirectives.iterator();
        while (more && iter.hasNext())
        {
            more = ((ITreeNode) iter.next()).accept(aVisitor);
        }
        // Finally visit this template
        if (more)
        {
            more = aVisitor.visit(this);
        }
        return more;
    }

    /**
     * @see com.googlecode.veloeclipse.vaulttec.ui.model.ITreeNodeInfo#getUniqueID()
     */
    @Override
	public String getUniqueID()
    {
        return getName();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
	public String toString()
    {
        return getUniqueID() + " [" + getStartLine() + ":" + getEndLine() + "] with directive(s) " + fDirectives;
    }
}
