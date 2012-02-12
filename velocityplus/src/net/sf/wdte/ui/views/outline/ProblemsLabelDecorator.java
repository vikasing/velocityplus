/*
 * Copyright (c) 2004 Christopher Lenz and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Christopher Lenz - initial implementation
 * 
 * $Id: ProblemsLabelDecorator.java,v 1.2 2004/02/28 15:39:40 cell Exp $
 */

package net.sf.wdte.ui.views.outline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.wdte.core.model.ISourceReference;
import net.sf.wdte.ui.WebUI;
import net.sf.wdte.ui.views.util.ImageDescriptorRegistry;
import net.sf.wdte.ui.views.util.ImageImageDescriptor;
import net.sf.wdte.ui.views.util.OverlayImageDescriptor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Label decorator for the outline page that adds error and warning overlay
 * icons to elements in the outline. The information is retrieved from the 
 * annotation model corresponding to the input of the associated text editor.
 */
public class ProblemsLabelDecorator extends LabelProvider
	implements ILabelDecorator {

	// Constants ---------------------------------------------------------------

	private static final String ANNOTATION_TYPE_ERROR =
		"org.eclipse.ui.workbench.texteditor.error"; //$NON-NLS-1$

	private static final String ANNOTATION_TYPE_WARNING =
		"org.eclipse.ui.workbench.texteditor.warning"; //$NON-NLS-1$

	// Instance Variables ------------------------------------------------------

	/** The associated text editor if the decorator is used in the outline. */
	private ITextEditor editor;

	/** Registry of icons and overlay icons. */
	private ImageDescriptorRegistry registry = new ImageDescriptorRegistry();

	// Constructors ------------------------------------------------------------

	/**
	 * Constructor.
	 * 
	 * @param editor the associated text editor
	 */
	public ProblemsLabelDecorator(ITextEditor editor) {
		this.editor = editor;
	}

	// ILabelDecorator Implementation ------------------------------------------

	/* 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		registry.dispose();
	}

	/* 
	 * @see ILabelDecorator#decorateImage(Image, Object)
	 */
	@Override
	public Image decorateImage(Image image, Object element) {
		Annotation annotations[] =
			getAssociatedAnnotations((ISourceReference) element);
		ImageDescriptor overlay = null;
		for (int i = 0; i < annotations.length; i++) {
			if (isError(annotations[i])) {
				overlay = WebUI.getDefault().getImageRegistry().getDescriptor(
						WebUI.ICON_OVERLAY_ERROR);
			} else if (isWarning(annotations[i])) {
				overlay = WebUI.getDefault().getImageRegistry().getDescriptor(
						WebUI.ICON_OVERLAY_WARNING);
			}
		}
		if (overlay != null) {
			ImageDescriptor base = new ImageImageDescriptor(image);
			return registry.get(new OverlayImageDescriptor(base,
					new ImageDescriptor[][] { null, null, { overlay }, null },
					null));
		} else {
		}
		return image;
	}

	/* 
	 * @see ILabelDecorator#decorateText(String, Object)
	 */
	@Override
	public String decorateText(String text, Object element) {
		return text;
	}

	// Private Methods ---------------------------------------------------------

	/**
	 * Returns all annotations associated with the given model element.
	 * 
	 * @param element the model element for which annotations should be
	 *        collected
	 * @return an array containing all annotations for the given element, or an
	 *         empty array if no annotations are found
	 */
	private Annotation[] getAssociatedAnnotations(ISourceReference element) {
		List retVal = new ArrayList();
		if (editor != null) {
			IEditorInput input = editor.getEditorInput();
			IAnnotationModel model =
				editor.getDocumentProvider().getAnnotationModel(input);
			for (Iterator i = model.getAnnotationIterator(); i.hasNext(); ) {
				Annotation annotation = (Annotation) i.next();
				Position pos = model.getPosition(annotation);
				if (isInside(pos.getOffset(), element)) {
					retVal.add(annotation);
				}
			}
		}
		return (Annotation[]) retVal.toArray(new Annotation[retVal.size()]);
	}

	/**
	 * Determines whether the given annotation is an error.
	 * 
	 * @param annotation the annotation to check
	 * @return <tt>true</tt> if the annotation is to be displayed as an error,
	 *         <tt>false</tt> otherwise
	 */
	private boolean isError(Annotation annotation) {
		return ANNOTATION_TYPE_ERROR.equals(annotation.getType());
	}

	/**
	 * Determines whether the given annotation is a warning.
	 * 
	 * @param annotation the annotation to check
	 * @return <tt>true</tt> if the annotation is to be displayed as a warning,
	 *         <tt>false</tt> otherwise
	 */
	private boolean isWarning(Annotation annotation) {
		return ANNOTATION_TYPE_WARNING.equals(annotation.getType());
	}

	/**
	 * Tests if the given position is inside the source region of a model
	 * element.
	 * 
	 * @param pos the position to be tested
	 * @param element the source element
	 * @return boolean <tt>true</tt> if position is located inside the
	 *         element, otherwise <tt>false</tt>
	 */
	private boolean isInside(int pos, ISourceReference element) {
		IRegion region = element.getSourceRegion();
		if (region != null) {
			int offset = region.getOffset();
			return ((offset <= pos) && (offset + region.getLength() > pos));			
		}
		return false;
	}

}
