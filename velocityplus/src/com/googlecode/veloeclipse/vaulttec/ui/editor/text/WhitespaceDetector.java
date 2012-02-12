package com.googlecode.veloeclipse.vaulttec.ui.editor.text;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * A Velocity directive aware whitespace detector.
 */
public class WhitespaceDetector implements IWhitespaceDetector
{

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.text.rules.IWhitespaceDetector#isWhitespace(char)
     */
    @Override
	public boolean isWhitespace(char aChar)
    {
        return ((aChar == ' ') || (aChar == '\t') || (aChar == '\n') || (aChar == '\r'));
    }
}
