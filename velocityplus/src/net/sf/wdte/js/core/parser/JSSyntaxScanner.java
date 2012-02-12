/*
 * Created on May 16, 2003
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSSyntaxScanner.java,v $
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

/**
 * Scanner for detecting syntactic elements: comments, strings, classes, functions
 *
 * @author fitzpata
 *
 */
public class JSSyntaxScanner extends BufferedRuleBasedScanner {
	public final static String JS_DEFAULT = "__js_default";
	public final static String JS_COMMENT = "__js_comment";
	public final static String JS_FUNCTION = "__js_function";
	public final static String JS_STRING = "__js_string";
	public final static String JS_MEMBER = "__js_member";
	public final static String JS_PERIOD = "__js_period";

	public final static IToken TOKEN_STRING = new Token(JS_STRING);
	public final static IToken TOKEN_COMMENT = new Token(JS_COMMENT);
	public final static IToken TOKEN_DEFAULT = new Token(JS_DEFAULT);
	public final static IToken TOKEN_FUNCTION = new Token(JS_FUNCTION);
	public final static IToken TOKEN_MEMBER = new Token(JS_MEMBER);
	public final static IToken TOKEN_PERIOD = new Token(JS_PERIOD);

	/**
	 * String for detecting member declarations.
	 */
	private static String memberToken=  ".prototype";

	/**
	 * Creates a new JSSyntaxScanner object.
	 */
	public JSSyntaxScanner() {
		List rules = new ArrayList();

		rules.add(new MultiLineRule("/*", "*/", TOKEN_COMMENT));
		rules.add(new SingleLineRule("//", "", TOKEN_COMMENT));
		rules.add(new SingleLineRule("\"", "\"", TOKEN_STRING, '\\'));
		rules.add(new SingleLineRule("'", "'", TOKEN_STRING, '\\'));
		
		rules.add(new WhitespaceRule(new JSWhitespaceDetector()));
		
		rules.add(new MultiLineRule("function(", ")", TOKEN_FUNCTION));
		rules.add(new MultiLineRule("function ", ")", TOKEN_FUNCTION));

		rules.add(new WordRule(new JSWordDetector(), TOKEN_DEFAULT));
		rules.add(new PredicateWordRule(new JSReferenceDetector(), memberToken, TOKEN_MEMBER));
		
		setRuleList(rules);
	}


	/**
	 * set the rule list
	 * @param rules
	 */
	private void setRuleList(List rules)
	{
		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}
}