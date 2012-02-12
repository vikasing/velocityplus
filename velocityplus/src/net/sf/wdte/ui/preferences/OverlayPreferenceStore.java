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
 * $Id: OverlayPreferenceStore.java,v 1.2 2004/02/17 12:29:25 cell Exp $
 */

package net.sf.wdte.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;


/**
 * An overlaying preference store.
 */
public class OverlayPreferenceStore implements IPreferenceStore {
	private class PropertyListener implements IPropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			PreferenceDescriptor key = findOverlayKey(event.getProperty());
			if (key != null) {
				propagateProperty(parent, key, store);
			}
		}
	}

	IPreferenceStore parent;
	IPreferenceStore store;

	private PreferenceDescriptor[] keys;

	private PropertyListener fPropertyListener;

	public OverlayPreferenceStore(
		IPreferenceStore parent, PreferenceDescriptor[] overlayKeys
	) {
		this.parent = parent;
		this.keys = overlayKeys;

		store = new PreferenceStore();
	}

	PreferenceDescriptor findOverlayKey(String key) {
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].key.equals(key)) {
				return keys[i];
			}
		}

		return null;
	}

	private boolean covers(String key) {
		return (findOverlayKey(key) != null);
	}

	void propagateProperty(
		IPreferenceStore orgin, PreferenceDescriptor key,
		IPreferenceStore target
	) {
		if (orgin.isDefault(key.key)) {
			if (!target.isDefault(key.key)) {
				target.setToDefault(key.key);
			}

			return;
		}

		PreferenceDescriptor.Type d = key.type;
		if (PreferenceDescriptor.BOOLEAN == d) {
			boolean originValue = orgin.getBoolean(key.key);
			boolean targetValue = target.getBoolean(key.key);
			if (targetValue != originValue) {
				target.setValue(key.key, originValue);
			}
		} else if (PreferenceDescriptor.DOUBLE == d) {
			double originValue = orgin.getDouble(key.key);
			double targetValue = target.getDouble(key.key);
			if (targetValue != originValue) {
				target.setValue(key.key, originValue);
			}
		} else if (PreferenceDescriptor.FLOAT == d) {
			float originValue = orgin.getFloat(key.key);
			float targetValue = target.getFloat(key.key);
			if (targetValue != originValue) {
				target.setValue(key.key, originValue);
			}
		} else if (PreferenceDescriptor.INT == d) {
			int originValue = orgin.getInt(key.key);
			int targetValue = target.getInt(key.key);
			if (targetValue != originValue) {
				target.setValue(key.key, originValue);
			}
		} else if (PreferenceDescriptor.LONG == d) {
			long originValue = orgin.getLong(key.key);
			long targetValue = target.getLong(key.key);
			if (targetValue != originValue) {
				target.setValue(key.key, originValue);
			}
		} else if (PreferenceDescriptor.STRING == d) {
			String originValue = orgin.getString(key.key);
			String targetValue = target.getString(key.key);
			if (targetValue != null && originValue != null
					&& !targetValue.equals(originValue)) {
				target.setValue(key.key, originValue);
			}
		}
	}

	public void propagate() {
		for (int i = 0; i < keys.length; i++) {
			propagateProperty(store, keys[i], parent);
		}
	}

	private void loadProperty(
		IPreferenceStore orgin, PreferenceDescriptor key,
		IPreferenceStore target, boolean forceInitialization
	) {
		PreferenceDescriptor.Type d = key.type;
		if (PreferenceDescriptor.BOOLEAN == d) {
			if (forceInitialization) {
				target.setValue(key.key, true);
			}
			target.setValue(key.key, orgin.getBoolean(key.key));
			target.setDefault(key.key, orgin.getDefaultBoolean(key.key));
		} else if (PreferenceDescriptor.DOUBLE == d) {
			if (forceInitialization) {
				target.setValue(key.key, 1.0D);
			}
			target.setValue(key.key, orgin.getDouble(key.key));
			target.setDefault(key.key, orgin.getDefaultDouble(key.key));
		} else if (PreferenceDescriptor.FLOAT == d) {
			if (forceInitialization) {
				target.setValue(key.key, 1.0F);
			}
			target.setValue(key.key, orgin.getFloat(key.key));
			target.setDefault(key.key, orgin.getDefaultFloat(key.key));
		} else if (PreferenceDescriptor.INT == d) {
			if (forceInitialization) {
				target.setValue(key.key, 1);
			}
			target.setValue(key.key, orgin.getInt(key.key));
			target.setDefault(key.key, orgin.getDefaultInt(key.key));
		} else if (PreferenceDescriptor.LONG == d) {
			if (forceInitialization) {
				target.setValue(key.key, 1L);
			}
			target.setValue(key.key, orgin.getLong(key.key));
			target.setDefault(key.key, orgin.getDefaultLong(key.key));
		} else if (PreferenceDescriptor.STRING == d) {
			if (forceInitialization) {
				target.setValue(key.key, "1"); //$NON-NLS-1$
			}
			target.setValue(key.key, orgin.getString(key.key));
			target.setDefault(key.key, orgin.getDefaultString(key.key));
		}
	}

	public void load() {
		for (int i = 0; i < keys.length; i++) {
			loadProperty(parent, keys[i], store, true);
		}
	}

	public void loadDefaults() {
		for (int i = 0; i < keys.length; i++) {
			setToDefault(keys[i].key);
		}
	}

	public void start() {
		if (fPropertyListener == null) {
			fPropertyListener = new PropertyListener();
			parent.addPropertyChangeListener(fPropertyListener);
		}
	}

	public void stop() {
		if (fPropertyListener != null) {
			parent.removePropertyChangeListener(fPropertyListener);
			fPropertyListener = null;
		}
	}

	/*
	 * @see IPreferenceStore#addPropertyChangeListener(IPropertyChangeListener)
	 */
	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		store.addPropertyChangeListener(listener);
	}

	/*
	 * @see IPreferenceStore#removePropertyChangeListener(IPropertyChangeListener)
	 */
	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		store.removePropertyChangeListener(listener);
	}

	/*
	 * @see IPreferenceStore#firePropertyChangeEvent(String, Object, Object)
	 */
	@Override
	public void firePropertyChangeEvent(
		String name, Object oldValue, Object newValue
	) {
		store.firePropertyChangeEvent(name, oldValue, newValue);
	}

	/*
	 * @see IPreferenceStore#contains(String)
	 */
	@Override
	public boolean contains(String name) {
		return store.contains(name);
	}

	/*
	 * @see IPreferenceStore#getBoolean(String)
	 */
	@Override
	public boolean getBoolean(String name) {
		return store.getBoolean(name);
	}

	/*
	 * @see IPreferenceStore#getDefaultBoolean(String)
	 */
	@Override
	public boolean getDefaultBoolean(String name) {
		return store.getDefaultBoolean(name);
	}

	/*
	 * @see IPreferenceStore#getDefaultDouble(String)
	 */
	@Override
	public double getDefaultDouble(String name) {
		return store.getDefaultDouble(name);
	}

	/*
	 * @see IPreferenceStore#getDefaultFloat(String)
	 */
	@Override
	public float getDefaultFloat(String name) {
		return store.getDefaultFloat(name);
	}

	/*
	 * @see IPreferenceStore#getDefaultInt(String)
	 */
	@Override
	public int getDefaultInt(String name) {
		return store.getDefaultInt(name);
	}

	/*
	 * @see IPreferenceStore#getDefaultLong(String)
	 */
	@Override
	public long getDefaultLong(String name) {
		return store.getDefaultLong(name);
	}

	/*
	 * @see IPreferenceStore#getDefaultString(String)
	 */
	@Override
	public String getDefaultString(String name) {
		return store.getDefaultString(name);
	}

	/*
	 * @see IPreferenceStore#getDouble(String)
	 */
	@Override
	public double getDouble(String name) {
		return store.getDouble(name);
	}

	/*
	 * @see IPreferenceStore#getFloat(String)
	 */
	@Override
	public float getFloat(String name) {
		return store.getFloat(name);
	}

	/*
	 * @see IPreferenceStore#getInt(String)
	 */
	@Override
	public int getInt(String name) {
		return store.getInt(name);
	}

	/*
	 * @see IPreferenceStore#getLong(String)
	 */
	@Override
	public long getLong(String name) {
		return store.getLong(name);
	}

	/*
	 * @see IPreferenceStore#getString(String)
	 */
	@Override
	public String getString(String name) {
		return store.getString(name);
	}

	/*
	 * @see IPreferenceStore#isDefault(String)
	 */
	@Override
	public boolean isDefault(String name) {
		return store.isDefault(name);
	}

	/*
	 * @see IPreferenceStore#needsSaving()
	 */
	@Override
	public boolean needsSaving() {
		return store.needsSaving();
	}

	/*
	 * @see IPreferenceStore#putValue(String, String)
	 */
	@Override
	public void putValue(String name, String value) {
		if (covers(name)) {
			store.putValue(name, value);
		}
	}

	/*
	 * @see IPreferenceStore#setDefault(String, double)
	 */
	@Override
	public void setDefault(String name, double value) {
		if (covers(name)) {
			store.setDefault(name, value);
		}
	}

	/*
	 * @see IPreferenceStore#setDefault(String, float)
	 */
	@Override
	public void setDefault(String name, float value) {
		if (covers(name)) {
			store.setDefault(name, value);
		}
	}

	/*
	 * @see IPreferenceStore#setDefault(String, int)
	 */
	@Override
	public void setDefault(String name, int value) {
		if (covers(name)) {
			store.setDefault(name, value);
		}
	}

	/*
	 * @see IPreferenceStore#setDefault(String, long)
	 */
	@Override
	public void setDefault(String name, long value) {
		if (covers(name)) {
			store.setDefault(name, value);
		}
	}

	/*
	 * @see IPreferenceStore#setDefault(String, String)
	 */
	@Override
	public void setDefault(String name, String value) {
		if (covers(name)) {
			store.setDefault(name, value);
		}
	}

	/*
	 * @see IPreferenceStore#setDefault(String, boolean)
	 */
	@Override
	public void setDefault(String name, boolean value) {
		if (covers(name)) {
			store.setDefault(name, value);
		}
	}

	/*
	 * @see IPreferenceStore#setToDefault(String)
	 */
	@Override
	public void setToDefault(String name) {
		store.setToDefault(name);
	}

	/*
	 * @see IPreferenceStore#setValue(String, double)
	 */
	@Override
	public void setValue(String name, double value) {
		if (covers(name)) {
			store.setValue(name, value);
		}
	}

	/*
	 * @see IPreferenceStore#setValue(String, float)
	 */
	@Override
	public void setValue(String name, float value) {
		if (covers(name)) {
			store.setValue(name, value);
		}
	}

	/*
	 * @see IPreferenceStore#setValue(String, int)
	 */
	@Override
	public void setValue(String name, int value) {
		if (covers(name)) {
			store.setValue(name, value);
		}
	}

	/*
	 * @see IPreferenceStore#setValue(String, long)
	 */
	@Override
	public void setValue(String name, long value) {
		if (covers(name)) {
			store.setValue(name, value);
		}
	}

	/*
	 * @see IPreferenceStore#setValue(String, String)
	 */
	@Override
	public void setValue(String name, String value) {
		if (covers(name)) {
			store.setValue(name, value);
		}
	}

	/*
	 * @see IPreferenceStore#setValue(String, boolean)
	 */
	@Override
	public void setValue(String name, boolean value) {
		if (covers(name)) {
			store.setValue(name, value);
		}
	}
}
