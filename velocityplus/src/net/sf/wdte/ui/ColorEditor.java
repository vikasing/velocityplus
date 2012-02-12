/*
 * Copyright (c) 2002-2004 Widespace, OU  and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 * 
 * Contributors:
 *     Igor Malinin - initial contribution
 * 
 * $Id: ColorEditor.java,v 1.1 2004/02/10 12:39:26 cell Exp $
 */

package net.sf.wdte.ui;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * A "button" of a certain color determined by the color picker.
 */
public class ColorEditor {

	private Point extent;

	Button button;

	Image image;

	RGB rgb;

	Color color;

	public ColorEditor(Composite parent) {
		button = new Button(parent, SWT.PUSH);
		extent = computeImageSize(parent);
		image = new Image(parent.getDisplay(), extent.x, extent.y);

		GC gc = new GC(image);

		gc.setBackground(button.getBackground());
		gc.fillRectangle(0, 0, extent.x, extent.y);

		gc.dispose();

		button.setImage(image);

		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				ColorDialog colorDialog = new ColorDialog(button.getShell());
				colorDialog.setRGB(rgb);

				RGB newColor = colorDialog.open();
				if (newColor != null) {
					rgb = newColor;
					updateColorImage();
				}
			}
		});

		button.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent event) {
				if (image != null) {
					image.dispose();
					image = null;
				}

				if (color != null) {
					color.dispose();
					color = null;
				}
			}
		});
	}

	public RGB getColorValue() {
		return rgb;
	}

	public void setColorValue(RGB rgb) {
		this.rgb = rgb;

		updateColorImage();
	}

	public Button getButton() {
		return button;
	}

	protected void updateColorImage() {
		Display display = button.getDisplay();

		GC gc = new GC(image);

		gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.drawRectangle(0, 2, extent.x - 1, extent.y - 4);

		if (color != null) {
			color.dispose();
		}

		color = new Color(display, rgb);

		gc.setBackground(color);
		gc.fillRectangle(1, 3, extent.x - 2, extent.y - 5);

		gc.dispose();

		button.setImage(image);
	}

	protected Point computeImageSize(Control control) {
		Font f = JFaceResources.getFontRegistry().get(
				JFaceResources.DEFAULT_FONT);

		GC gc = new GC(control);
		gc.setFont(f);

		int height = gc.getFontMetrics().getHeight();

		gc.dispose();

		return new Point(height * 3 - 6, height);
	}
}
