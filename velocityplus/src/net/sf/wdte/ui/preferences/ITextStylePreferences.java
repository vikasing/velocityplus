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
 * $Id: ITextStylePreferences.java,v 1.1 2004/02/10 12:40:56 cell Exp $
 */

package net.sf.wdte.ui.preferences;


/**
 * 
 * 
 * @author Igor Malinin
 */
public interface ITextStylePreferences {
	public static final String SUFFIX_FOREGROUND = "_foreground"; //$NON-NLS-1$
	public static final String SUFFIX_BACKGROUND = "_background"; //$NON-NLS-1$
	public static final String SUFFIX_STYLE      = "_style";      //$NON-NLS-1$

	public static final String STYLE_NORMAL = "normal"; //$NON-NLS-1$
	public static final String STYLE_BOLD   = "bold";   //$NON-NLS-1$
}
