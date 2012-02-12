/*
 * $RCSfile: JSDoubleClickStrategy.java,v $
 *
 * Copyright 2002
 * CH-1700 Fribourg, Switzerland
 * All rights reserved.
 *
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSDoubleClickStrategy.java,v $
 * Revision 1.1  2004/02/05 03:13:28  agfitzp
 * Initial submission, outline view is broken due to refactoring
 *
 * Revision 1.1  2003/05/28 15:17:12  agfitzp
 * net.sf.wdte.js.ui 0.0.1 code base
 *
 *========================================================================
*/

package net.sf.wdte.js.ui.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;


/**
 * 
 *
 * @author $Author: agfitzp $, $Date: 2004/02/05 03:13:28 $
 *
 * @version $Revision: 1.1 $
 */
public class JSDoubleClickStrategy implements ITextDoubleClickStrategy
{
   protected ITextViewer fText;

   /**
    * Creates a new JSDoubleClickStrategy object.
    */
   public JSDoubleClickStrategy()
   {
      super();
   }

   /**
    *
    *
    * @param part 
    */
   @Override
public void doubleClicked(ITextViewer part)
   {
      int pos = part.getSelectedRange().x;

      if(pos < 0)
      {
         return;
      }

      fText = part;

      if(!selectComment(pos))
      {
         selectWord(pos);
      }
   }

   /**
    *
    *
    * @param caretPos 
    *
    * @return 
    */
   protected boolean selectComment(int caretPos)
   {
      IDocument doc = fText.getDocument();
      int startPos;
      int endPos;

      try
      {
         int pos = caretPos;
         char c = ' ';

         while(pos >= 0)
         {
            c = doc.getChar(pos);

            if(c == '\\')
            {
               pos -= 2;

               continue;
            }

            if(c == Character.LINE_SEPARATOR || c == '\"')
            {
               break;
            }

            --pos;
         }

         if(c != '\"')
         {
            return false;
         }

         startPos = pos;

         pos = caretPos;

         int length = doc.getLength();
         c = ' ';

         while(pos < length)
         {
            c = doc.getChar(pos);

            if(c == Character.LINE_SEPARATOR || c == '\"')
            {
               break;
            }

            ++pos;
         }

         if(c != '\"')
         {
            return false;
         }

         endPos = pos;

         int offset = startPos + 1;
         int len = endPos - offset;
         fText.setSelectedRange(offset, len);

         return true;
      }
      catch(BadLocationException x)
      {
      }

      return false;
   }

   /**
    *
    *
    * @param caretPos 
    *
    * @return 
    */
   protected boolean selectWord(int caretPos)
   {
      IDocument doc = fText.getDocument();
      int startPos;
      int endPos;

      try
      {
         int pos = caretPos;
         char c;

         while(pos >= 0)
         {
            c = doc.getChar(pos);

            if(!Character.isJavaIdentifierPart(c))
            {
               break;
            }

            --pos;
         }

         startPos = pos;

         pos = caretPos;

         int length = doc.getLength();

         while(pos < length)
         {
            c = doc.getChar(pos);

            if(!Character.isJavaIdentifierPart(c))
            {
               break;
            }

            ++pos;
         }

         endPos = pos;
         selectRange(startPos, endPos);

         return true;
      }
      catch(BadLocationException x)
      {
      }

      return false;
   }

   private void selectRange(int startPos, int stopPos)
   {
      int offset = startPos + 1;
      int length = stopPos - offset;
      fText.setSelectedRange(offset, length);
   }
}