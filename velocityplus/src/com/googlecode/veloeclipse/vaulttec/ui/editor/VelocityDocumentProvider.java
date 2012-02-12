package com.googlecode.veloeclipse.vaulttec.ui.editor;

import net.sf.wdte.js.core.parser.JSPartitionScanner;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import com.googlecode.veloeclipse.scanner.VelocityPartitionScanner;


/**
 * This class provides the IDocuments used by Velocity editors. These IDocuments
 * have an Velocity-aware partition scanner (multi-line comments) attached.
 */
public class VelocityDocumentProvider extends FileDocumentProvider
{

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#createDocument(Object)
     */
    @Override
	protected IDocument createDocument(Object anElement) throws CoreException
    {
        IDocument document = super.createDocument(anElement);
        if (document != null)
        {
            IDocumentPartitioner partitioner = new FastPartitioner(new VelocityPartitionScanner(), VelocityPartitionScanner.TYPES);
            partitioner.connect(document);
            document.setDocumentPartitioner(partitioner);
        }
        return document;
    }
    
    /**
	 * Array of constant token types that will be color hilighted.
	 */
	private static String[] colorTokens= { 
		JSPartitionScanner.JS_COMMENT,
		JSPartitionScanner.JS_STRING, 
		JSPartitionScanner.JS_KEYWORD 
	};
}
