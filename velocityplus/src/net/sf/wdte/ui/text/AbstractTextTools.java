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
 * $Id: AbstractTextTools.java,v 1.1 2004/02/10 12:39:57 cell Exp $
 */

package net.sf.wdte.ui.text;

import java.util.HashMap;
import java.util.Map;

import net.sf.wdte.ui.ColorManager;
import net.sf.wdte.ui.preferences.ITextStylePreferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

/**
 * @author Igor Malinin
 */
public class AbstractTextTools {

	/** The preference store */
	protected IPreferenceStore store;

	/** The color manager */
	protected ColorManager colorManager;

	private Map tokens;

	private String[] properties;

	private String[] foregroundPropertyNames;

	private String[] backgroundPropertyNames;

	private String[] stylePropertyNames;

	private IPropertyChangeListener listener;

	/**
	 * Creates a new text tools collection.
	 */
	public AbstractTextTools(IPreferenceStore store, String[] properties) {
		this.store = store;
		this.properties = properties;

		colorManager = new ColorManager();

		tokens = new HashMap();

		int length = properties.length;

		foregroundPropertyNames = new String[length];
		backgroundPropertyNames = new String[length];
		stylePropertyNames = new String[length];

		for (int i = 0; i < length; i++) {
			String property = properties[i];

			String foreground = property
					+ ITextStylePreferences.SUFFIX_FOREGROUND;
			String background = property
					+ ITextStylePreferences.SUFFIX_BACKGROUND;
			String style = property + ITextStylePreferences.SUFFIX_STYLE;

			foregroundPropertyNames[i] = foreground;
			backgroundPropertyNames[i] = background;
			stylePropertyNames[i] = style;

			RGB rgb;

			rgb = getColor(store, foreground);
			if (rgb != null) {
				colorManager.bindColor(foreground, rgb);
			}

			rgb = getColor(store, background);
			if (rgb != null) {
				colorManager.bindColor(background, rgb);
			}

			tokens.put(property, new Token(new TextAttribute(colorManager
					.getColor(foreground), colorManager.getColor(background),
					getStyle(store, style))));
		}

		listener = new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				adaptToPreferenceChange(event);
			}
		};

		store.addPropertyChangeListener(listener);
	}

	/**
	 * Disposes all the individual tools of this tools collection.
	 */
	public void dispose() {
		if (store != null) {
			store.removePropertyChangeListener(listener);

			store = null;
			listener = null;
		}

		if (colorManager != null) {
			colorManager.dispose();

			colorManager = null;
		}

		tokens = null;

		properties = null;
		foregroundPropertyNames = null;
		backgroundPropertyNames = null;
		stylePropertyNames = null;
	}

	/**
	 * Returns the color manager which is used to manage any XML-specific
	 * colors needed for such things like syntax highlighting.
	 * 
	 * @return the color manager to be used for XML text viewers
	 */
	public ColorManager getColorManager() {
		return colorManager;
	}

	protected Map getTokens() {
		return tokens;
	}

	protected Token getToken(String key) {
		int index = indexOf(key);
		if (index < 0) {
			return null;
		}

		return (Token) tokens.get(properties[index]);
	}

	/**
	 * Determines whether the preference change encoded by the given event
	 * changes the behavior of one its contained components.
	 * 
	 * @param event
	 *            the event to be investigated
	 * @return <code>true</code> if event causes a behavioral change
	 */
	public boolean affectsBehavior(PropertyChangeEvent event) {
		return (indexOf(event.getProperty()) >= 0);
	}

	/**
	 * Adapts the behavior of the contained components to the change encoded in
	 * the given event.
	 * 
	 * @param event
	 *            the event to whch to adapt
	 */
	protected void adaptToPreferenceChange(PropertyChangeEvent event) {
		String property = event.getProperty();

		Token token = getToken(property);
		if (token != null) {
			if (property.endsWith(ITextStylePreferences.SUFFIX_FOREGROUND)
					|| property
							.endsWith(ITextStylePreferences.SUFFIX_BACKGROUND)) {
				adaptToColorChange(token, event);
			} else if (property.endsWith(ITextStylePreferences.SUFFIX_STYLE)) {
				adaptToStyleChange(token, event);
			}
		}
	}

	private void adaptToColorChange(Token token, PropertyChangeEvent event) {
		RGB rgb = getColor(event.getNewValue());

		String property = event.getProperty();

		colorManager.unbindColor(property);
		if (rgb != null) {
			colorManager.bindColor(property, rgb);
		}

		Object data = token.getData();
		if (data instanceof TextAttribute) {
			TextAttribute old = (TextAttribute) data;

			int i = indexOf(property);

			token.setData(new TextAttribute(colorManager
					.getColor(foregroundPropertyNames[i]), colorManager
					.getColor(backgroundPropertyNames[i]), old.getStyle()));
		}
	}

	private void adaptToStyleChange(Token token, PropertyChangeEvent event) {
		int style = getStyle((String) event.getNewValue());

		Object data = token.getData();
		if (data instanceof TextAttribute) {
			TextAttribute old = (TextAttribute) data;
			if (old.getStyle() != style) {
				token.setData(new TextAttribute(old.getForeground(), old
						.getBackground(), style));
			}
		}
	}

	private int indexOf(String property) {
		if (property != null) {
			int length = properties.length;

			for (int i = 0; i < length; i++) {
				if (property.equals(properties[i])
						|| property.equals(foregroundPropertyNames[i])
						|| property.equals(backgroundPropertyNames[i])
						|| property.equals(stylePropertyNames[i])) {
					return i;
				}
			}
		}

		return -1;
	}

	private RGB getColor(IPreferenceStore store, String key) {
		return getColor(store.getString(key));
	}

	private RGB getColor(Object value) {
		if (value instanceof RGB) {
			return (RGB) value;
		}

		String str = (String) value;
		if (str.length() > 0) {
			return StringConverter.asRGB(str);
		}

		return null;
	}

	private int getStyle(IPreferenceStore store, String key) {
		return getStyle(store.getString(key));
	}

	private int getStyle(String value) {
		if (value.indexOf(ITextStylePreferences.STYLE_BOLD) >= 0) {
			return SWT.BOLD;
		}

		return SWT.NORMAL;
	}
}
