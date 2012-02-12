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
 * $Id: I18NDocumentProvider.java,v 1.1 2004/02/10 12:39:25 cell Exp $
 */

package net.sf.wdte.ui.editor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerGenerator;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;

/**
 * @author Igor Malinin
 */
public class I18NDocumentProvider extends FileDocumentProvider {

	private static final char BOM = 0xFEFF;

	/*
	 * @see org.eclipse.ui.editors.text.StorageDocumentProvider#setDocumentContent(IDocument,
	 *      InputStream, String)
	 */
	@Override
	protected void setDocumentContent(IDocument document,
			InputStream contentStream, String encoding) throws CoreException {
		Reader in = null;

		try {
			if (encoding == null) {
				encoding = getDefaultEncoding();
			}

			in = new InputStreamReader(contentStream, encoding);

			StringBuffer buffer = new StringBuffer();

			char[] readBuffer = new char[2048];
			int n = in.read(readBuffer);
			while (n > 0) {
				buffer.append(readBuffer, 0, n);
				n = in.read(readBuffer);
			}

			if (buffer.length() > 0 && buffer.charAt(0) == BOM) {
				buffer.deleteCharAt(0);
			}

			document.set(buffer.toString());
		} catch (IOException x) {
			String msg = x.getMessage();
			if (msg == null) {
				msg = ""; //$NON-NLS-1$
			}

			IStatus s = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID,
					IStatus.OK, msg, x);

			throw new CoreException(s);
		} finally {
			if (in != null) try {
				in.close();
			} catch (IOException x) {
			}
		}
	}

	/*
	 * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#doSaveDocument(IProgressMonitor,
	 *      Object, IDocument, boolean)
	 */
	@Override
	protected void doSaveDocument(IProgressMonitor monitor, Object element,
			IDocument document, boolean overwrite) throws CoreException {
		if (!(element instanceof IFileEditorInput)) {
			super.doSaveDocument(monitor, element, document, overwrite);
			return;
		}

		IFileEditorInput input = (IFileEditorInput) element;

		try {
			String content = document.get();

			String encoding = getDeclaredEncoding(new ByteArrayInputStream(
					content.getBytes("ISO-8859-1")));

			if (encoding == null) {
				encoding = super.getEncoding(element);
				if (encoding == null /* || !encoding.startsWith("UTF-16") */) {
					encoding = getDefaultEncoding();
				}
			} else {
				setEncoding(element, encoding);
			}

			if (encoding.startsWith("UTF-16")) {
				content = BOM + content;
			}

			InputStream stream;
			try {
				stream = new ByteArrayInputStream(content.getBytes(encoding));
			} catch (UnsupportedEncodingException e) {
				IStatus s = new Status(
						IStatus.ERROR,
						PlatformUI.PLUGIN_ID,
						IStatus.OK,
						EditorMessages
								.getString("I18NDocumentProvider.error.encoding"),
						e);

				throw new CoreException(s);
			}

			IFile file = input.getFile();
			if (file.exists()) {
				FileInfo info = (FileInfo) getElementInfo(element);

				if (info != null && !overwrite) {
					checkSynchronizationState(info.fModificationStamp, file);
				}

				// inform about the upcoming content change
				fireElementStateChanging(element);

				try {
					file.setContents(stream, overwrite, true, monitor);
				} catch (CoreException x) {
					// inform about failure
					fireElementStateChangeFailed(element);
					throw x;
				} catch (RuntimeException x) {
					// inform about failure
					fireElementStateChangeFailed(element);
					throw x;
				}

				// If here, the editor state will be flipped to "not dirty".
				// Thus, the state changing flag will be reset.

				if (info != null) {
					ResourceMarkerAnnotationModel model = (ResourceMarkerAnnotationModel) info.fModel;

					model.updateMarkers(info.fDocument);

					info.fModificationStamp = computeModificationStamp(file);
				}
			} else {
				try {
					monitor.beginTask(EditorMessages
							.getString("I18NDocumentProvider.task.saving"), //$NON-NLS-1$
							2000);

					ContainerGenerator generator = new ContainerGenerator(file
							.getParent().getFullPath());

					generator.generateContainer(new SubProgressMonitor(monitor,
							1000));

					file.create(stream, false, new SubProgressMonitor(monitor,
							1000));
				} finally {
					monitor.done();
				}
			}
		} catch (IOException x) {
			IStatus s = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID,
					IStatus.OK, x.getMessage(), x);

			throw new CoreException(s);
		}
	}

	/*
	 * @see org.eclipse.ui.editors.text.IStorageDocumentProvider#getEncoding(Object)
	 */
	@Override
	public String getEncoding(Object element) {
		String encoding = super.getEncoding(element);
		if (encoding != null) {
			return encoding;
		}

		if (element instanceof IStorageEditorInput) {
			IStorageEditorInput sei = (IStorageEditorInput) element;

			try {
				InputStream in = sei.getStorage().getContents();
				try {
					encoding = getDeclaredEncoding(in);
				} finally {
					in.close();
				}
			} catch (CoreException e) {
			} catch (IOException e) {
			}

			if (encoding == null) {
				encoding = getDefaultEncoding();
			}

			setEncoding(element, encoding);
		}

		return encoding;
	}

	/*
	 * @see org.eclipse.ui.editors.text.IStorageDocumentProvider#setEncoding(Object,
	 *      String)
	 */
	@Override
	public void setEncoding(Object element, String encoding) {
		if (encoding == null) {
			encoding = getDefaultEncoding();
		}

		super.setEncoding(element, encoding);
	}

	/**
	 * Tries to determine encoding from contents of the stream. Returns <code>null</code>
	 * if encoding is unknown.
	 */
	public String getDeclaredEncoding(InputStream in) throws IOException {
		return getBOMEncoding(in);
	}

	/**
	 * Tries to determine encoding from the byte order mark. Returns <code>null</code>
	 * if encoding is unknown.
	 */
	private String getBOMEncoding(InputStream in) throws IOException {
		int first = in.read();
		if (first < 0) {
			return null;
		}

		int second = in.read();
		if (second < 0) {
			return null;
		}

		// look for the UTF-16 Byte Order Mark (BOM)
		if (first == 0xFE && second == 0xFF) {
			return "UTF-16BE";
		}

		if (first == 0xFF && second == 0xFE) {
			return "UTF-16LE";
		}

		int third = in.read();
		if (third < 0) {
			return null;
		}

		// look for the UTF-8 BOM
		if (first == 0xEF && second == 0xBB && third == 0xBF) {
			return "UTF-8";
		}

		return null;
	}
}
