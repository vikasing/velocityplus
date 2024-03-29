/*
 * Created on 02.10.2003
 * 
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.googlecode.veloeclipse.preferences;

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
public class VelocityColorEditor
{

    private Point  fExtent;
    private Image  fImage;
    private RGB    fColorValue;
    private Color  fColor;
    private Button fButton;

    public VelocityColorEditor(Composite parent)
    {
        fButton = new Button(parent, SWT.PUSH);
        fExtent = computeImageSize(parent);
        fImage = new Image(parent.getDisplay(), fExtent.x, fExtent.y);
        GC gc = new GC(fImage);
        gc.setBackground(fButton.getBackground());
        gc.fillRectangle(0, 0, fExtent.x, fExtent.y);
        gc.dispose();
        fButton.setImage(fImage);
        fButton.addSelectionListener(new SelectionAdapter() {

            @Override
			public void widgetSelected(SelectionEvent event)
            {
                ColorDialog colorDialog = new ColorDialog(fButton.getShell());
                colorDialog.setRGB(fColorValue);
                RGB newColor = colorDialog.open();
                if (newColor != null)
                {
                    fColorValue = newColor;
                    updateColorImage();
                }
            }
        });
        fButton.addDisposeListener(new DisposeListener() {

            @Override
			public void widgetDisposed(DisposeEvent event)
            {
                if (fImage != null)
                {
                    fImage.dispose();
                    fImage = null;
                }
                if (fColor != null)
                {
                    fColor.dispose();
                    fColor = null;
                }
            }
        });
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public RGB getColorValue()
    {
        return fColorValue;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param rgb
     *            DOCUMENT ME!
     */
    public void setColorValue(RGB rgb)
    {
        fColorValue = rgb;
        updateColorImage();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public Button getButton()
    {
        return fButton;
    }

    protected void updateColorImage()
    {
        Display display = fButton.getDisplay();
        GC gc = new GC(fImage);
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawRectangle(0, 2, fExtent.x - 1, fExtent.y - 4);
        if (fColor != null)
        {
            fColor.dispose();
        }
        fColor = new Color(display, fColorValue);
        gc.setBackground(fColor);
        gc.fillRectangle(1, 3, fExtent.x - 2, fExtent.y - 5);
        gc.dispose();
        fButton.setImage(fImage);
    }

    protected Point computeImageSize(Control window)
    {
        GC gc = new GC(window);
        Font f = JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT);
        gc.setFont(f);
        int height = gc.getFontMetrics().getHeight();
        gc.dispose();
        Point p = new Point((height * 3) - 6, height);
        return p;
    }
}
