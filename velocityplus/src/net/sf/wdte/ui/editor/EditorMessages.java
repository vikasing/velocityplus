/*
 * Copyright (c) 2002-2004 Widespace, OU and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 * 
 * Contributors:
 *     Igor Malinin - initial contribution
 * 
 * $Id: EditorMessages.java,v 1.1 2004/02/10 12:39:25 cell Exp $
 */

package net.sf.wdte.ui.editor;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Igor Malinin
 */
public class EditorMessages {
	private static ResourceBundle bundle = ResourceBundle
		.getBundle("net.sf.wdte.ui.editor.EditorMessages"); //$NON-NLS-1$

	private EditorMessages() {}

	public static String getString( String key ) {
		try {
			return bundle.getString( key );
		} catch ( MissingResourceException e ) {
			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}
