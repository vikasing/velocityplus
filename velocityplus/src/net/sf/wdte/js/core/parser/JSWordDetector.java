/*
 * Created on May 13, 2003
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSWordDetector.java,v $
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
package net.sf.wdte.js.core.parser;

/**
 * @author fitzpata
 *
 */

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * A JavaScript aware word detector.
 * JavaScript tokens are almost identical to Java so this
 * class is borrowed from org.eclipse.jdt.internal.ui.text.JavaWordDetector.
 */
public class JSWordDetector implements IWordDetector {

	/**
	 * @see IWordDetector#isWordStart
	 * JavaScript tokens are almost identical to Java so for now
	 * we can just borrow this behavior.
	 */
	@Override
	public boolean isWordStart(char c) {
		return Character.isJavaIdentifierStart(c);
	}
	
	/**
	 * @see IWordDetector#isWordPart
	 * JavaScript tokens are almost identical to Java so for now
	 * we can just borrow this behavior.
	 */
	@Override
	public boolean isWordPart(char c) {
		return Character.isJavaIdentifierPart(c);
	}
}
