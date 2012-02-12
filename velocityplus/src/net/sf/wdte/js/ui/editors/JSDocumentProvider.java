/*
 * $RCSfile: JSDocumentProvider.java,v $
 *
 * Copyright 2002
 * CH-1700 Fribourg, Switzerland
 * All rights reserved.
 *
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSDocumentProvider.java,v $
 * Revision 1.2  2004/02/26 02:25:57  agfitzp
 * renamed packages to match xml & css
 *
 * Revision 1.1  2004/02/05 03:13:28  agfitzp
 * Initial submission, outline view is broken due to refactoring
 *
 * Revision 1.1.2.1  2003/12/12 21:37:24  agfitzp
 * Experimental work for Classes view
 *
 * Revision 1.1  2003/05/28 15:17:11  agfitzp
 * net.sf.wdte.js.ui 0.0.1 code base
 *
 *========================================================================
*/

package net.sf.wdte.js.ui.editors;

import net.sf.wdte.js.core.parser.JSPartitionScanner;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.DefaultPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 * 
 *
 * @author $Author: agfitzp $, $Date: 2004/02/26 02:25:57 $
 *
 * @version $Revision: 1.2 $
 */
public class JSDocumentProvider extends FileDocumentProvider {

	/**
	 * Array of constant token types that will be color hilighted.
	 */
	private static String[] colorTokens= { 
		JSPartitionScanner.JS_COMMENT,
		JSPartitionScanner.JS_STRING, 
		JSPartitionScanner.JS_KEYWORD 
	};

	/**
	 * Constructor for JSDocumentProvider.
	 */
	public JSDocumentProvider() {
		super();
	}

	/**
	 * @param element 
	 *
	 * @return 
	 *
	 * @throws CoreException 
	 */
	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);

		if (document != null) {
			IDocumentPartitioner partitioner =
				new DefaultPartitioner(new JSPartitionScanner(), colorTokens);
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}

		return document;
	}
}