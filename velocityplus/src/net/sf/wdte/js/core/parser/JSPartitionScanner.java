/*
 * $RCSfile: JSPartitionScanner.java,v $
 *
 * Copyright 2002
 * CH-1700 Fribourg, Switzerland
 * All rights reserved.
 *
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSPartitionScanner.java,v $
 * Revision 1.1  2004/02/26 02:25:42  agfitzp
 * renamed packages to match xml & css
 *
 * Revision 1.1  2004/02/05 03:10:08  agfitzp
 * Initial Submission
 *
 * Revision 1.1.2.1  2003/12/12 21:37:24  agfitzp
 * Experimental work for Classes view
 *
 * Revision 1.3  2003/05/30 20:53:09  agfitzp
 * 0.0.2 : Outlining is now done as the user types. Some other bug fixes.
 *
 * Revision 1.2  2003/05/28 20:47:58  agfitzp
 * Outline the document, not the file.
 *
 * Revision 1.1  2003/05/28 15:17:12  agfitzp
 * net.sf.wdte.js.core 0.0.1 code base
 *
 *========================================================================
*/

package net.sf.wdte.js.core.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

/**
 * 
 *
 * @author $Author: agfitzp $, $Date: 2004/02/26 02:25:42 $
 *
 * @version $Revision: 1.1 $
 */
public class JSPartitionScanner extends RuleBasedPartitionScanner {
	public final static String JS_DEFAULT = "__js_default";
	public final static String JS_COMMENT = "__js_comment";
	public final static String JS_KEYWORD = "__js_keyword";
	public final static String JS_STRING = "__js_string";

	public final static IToken TOKEN_STRING = new Token(JS_STRING);
	public final static IToken TOKEN_COMMENT = new Token(JS_COMMENT);
	public final static IToken TOKEN_DEFAULT = new Token(JS_DEFAULT);
	public final static IToken TOKEN_KEYWORD = new Token(JS_KEYWORD);

	/**
	 * Array of keyword token strings.
	 */
	private static String[] keywordTokens= {
		"break", 
		"case", "catch", "continue", 
		"default", "do", 
		"else", 
		"for", "function",
		"goto", 
		"if", "in", 
		"new", 
		"return",
		"switch",
		"this", "throw", "try",
		"var", "void",
		"while", "with"
	};

	/**
	 * Array of constant token strings.
	 */
	private static String[] constantTokens= { "false", "null", "true" };


	/**
	 * Creates a new JSPartitionScanner object.
	 */
	public JSPartitionScanner() {
		List rules = new ArrayList();

		rules.add(new MultiLineRule("/*", "*/", TOKEN_COMMENT));
		rules.add(new SingleLineRule("//", "", TOKEN_COMMENT));
		rules.add(new SingleLineRule("\"", "\"", TOKEN_STRING, '\\'));
		rules.add(new SingleLineRule("'", "'", TOKEN_STRING, '\\'));
		
		PredicateWordRule keywordRule = new PredicateWordRule(new JSWordDetector(), TOKEN_DEFAULT, keywordTokens, TOKEN_KEYWORD);
		keywordRule.addWords(constantTokens, TOKEN_KEYWORD);
		rules.add(keywordRule);
		
		setRuleList(rules);
	}


	private void setRuleList(List rules)
	{
		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}