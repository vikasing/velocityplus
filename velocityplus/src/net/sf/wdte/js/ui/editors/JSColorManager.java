/*
 * $RCSfile: JSColorManager.java,v $
 *
 * Copyright 2002
 * CH-1700 Fribourg, Switzerland
 * All rights reserved.
 *
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSColorManager.java,v $
 * Revision 1.1  2004/02/05 03:13:28  agfitzp
 * Initial submission, outline view is broken due to refactoring
 *
 * Revision 1.1  2003/05/28 15:17:12  agfitzp
 * net.sf.wdte.js.ui 0.0.1 code base
 *
 *========================================================================
*/

package net.sf.wdte.js.ui.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;


/**
 * 
 *
 * @author $Author: agfitzp $, $Date: 2004/02/05 03:13:28 $
 *
 * @version $Revision: 1.1 $
 */
public class JSColorManager
{
   protected Map fColorTable = new HashMap(10);

   /**
    *
    */
   public void dispose()
   {
      Iterator e = fColorTable.values().iterator();

      while(e.hasNext())
      {
         ((Color)e.next()).dispose();
      }
   }

   /**
    *
    *
    * @param rgb 
    *
    * @return 
    */
   public Color getColor(RGB rgb)
   {
      Color color = (Color)fColorTable.get(rgb);

      if(color == null)
      {
         color = new Color(Display.getCurrent(), rgb);
         fColorTable.put(rgb, color);
      }

      return color;
   }
}