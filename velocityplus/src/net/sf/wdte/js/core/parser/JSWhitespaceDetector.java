/*
 * $RCSfile: JSWhitespaceDetector.java,v $
 *
 * Copyright 2002
 * CH-1700 Fribourg, Switzerland
 * All rights reserved.
 *
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSWhitespaceDetector.java,v $
 * Revision 1.1  2004/02/26 02:25:42  agfitzp
 * renamed packages to match xml & css
 *
 * Revision 1.1  2004/02/05 03:10:08  agfitzp
 * Initial Submission
 *
 * Revision 1.1.2.1  2003/12/12 21:37:24  agfitzp
 * Experimental work for Classes view
 *
 * Revision 1.1  2003/05/28 15:17:12  agfitzp
 * net.sf.wdte.js.core 0.0.1 code base
 *
 *========================================================================
*/

package net.sf.wdte.js.core.parser;

import org.eclipse.jface.text.rules.IWhitespaceDetector;


/**
 * 
 *
 * @author $Author: agfitzp $, $Date: 2004/02/26 02:25:42 $
 *
 * @version $Revision: 1.1 $
 */
public class JSWhitespaceDetector implements IWhitespaceDetector
{
   /**
    *
    *
    * @param c 
    *
    * @return 
    */
   @Override
public boolean isWhitespace(char c)
   {
      return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
   }
}