package com.googlecode.veloeclipse.vaulttec.ui.editor.text;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Detector for empty Velocity comments.
 */
public class EmptyCommentDetector implements IWordDetector
{

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
     */
    @Override
	public boolean isWordStart(char aChar)
    {
        return (aChar == '#');
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
     */
    @Override
	public boolean isWordPart(char aChar)
    {
        return ((aChar == '*') || (aChar == '#'));
    }
}
