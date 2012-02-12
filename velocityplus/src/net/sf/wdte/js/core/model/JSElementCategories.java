/*
 * Created on May 20, 2003
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSElementCategories.java,v $
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
 *========================================================================
 */
package net.sf.wdte.js.core.model;

/**
 * @author fitzpata
 *
 */
public interface JSElementCategories
{
	static final int CLASS = 1;
	static final int FUNCTION = 2;
	static final int VARIABLE = 3;
	static final int CLASS_VARIABLE = 4;
	static final int INSTANCE_VARIABLE = 5;
	static final int CLASS_METHOD = 6;
	static final int INSTANCE_METHOD = 7;
}
