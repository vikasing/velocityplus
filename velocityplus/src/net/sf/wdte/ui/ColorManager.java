/*
 * Copyright (c) 2002-2004 Widespace, OU and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 * 
 * Contributors:
 * 	   Igor Malinin - initial contribution
 * 
 * $Id: ColorManager.java,v 1.1 2004/02/10 12:39:26 cell Exp $
 */

package net.sf.wdte.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Color Manager.
 * 
 * @author Igor Malinin
 */
public class ColorManager {
	protected Map colors = new HashMap( 10 );

	public void bindColor( String key, RGB rgb ) {
		Object value = colors.get( key );
		if ( value != null ) {
			throw new UnsupportedOperationException();
		}

		Color color = new Color( Display.getCurrent(), rgb );

		colors.put( key, color );
	}

	public void unbindColor( String key ) {
		Color color = (Color) colors.remove( key );
		if ( color != null ) {
			color.dispose();
		}
	}

	public Color getColor( String key ) {
		return (Color) colors.get( key );
	}

	public void dispose() {
		Iterator i = colors.values().iterator();
		while ( i.hasNext() ) {
			((Color) i.next()).dispose();
		}
	}
}
