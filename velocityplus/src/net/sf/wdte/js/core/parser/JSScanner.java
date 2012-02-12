/*
 * $RCSfile: JSScanner.java,v $
 *
 * Copyright 2002
 * CH-1700 Fribourg, Switzerland
 * All rights reserved.
 *
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSScanner.java,v $
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.graphics.Color;

/**
 * 
 *
 * @author $Author: agfitzp $, $Date: 2004/02/26 02:25:42 $
 *
 * @version $Revision: 1.1 $
 */
public class JSScanner extends RuleBasedScanner
{
   /**
    * Creates a new JSScanner object.
    *
    * @param manager 
    */
   public JSScanner(Color aColor)
   {
      List rules = new ArrayList();
      IToken procInstr = new Token(new TextAttribute(aColor));

      // Add generic whitespace rule.
      rules.add(new WhitespaceRule(new JSWhitespaceDetector()));

      IRule[] result = new IRule[rules.size()];
      rules.toArray(result);
      setRules(result);
   }

}