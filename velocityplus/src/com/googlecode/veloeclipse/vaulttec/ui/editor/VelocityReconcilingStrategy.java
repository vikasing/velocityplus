package com.googlecode.veloeclipse.vaulttec.ui.editor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.Token;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IFileEditorInput;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

import com.googlecode.veloeclipse.editor.VelocityEditor;
import com.googlecode.veloeclipse.editor.VelocityFoldingStructureProvider;
import com.googlecode.veloeclipse.vaulttec.ui.VelocityPlugin;
import com.googlecode.veloeclipse.vaulttec.ui.editor.parser.NodeVisitor;
import com.googlecode.veloeclipse.vaulttec.ui.model.ITreeNode;
import com.googlecode.veloeclipse.vaulttec.ui.model.ITreeVisitor;
import com.googlecode.veloeclipse.vaulttec.ui.model.Template;

/**
 * Reconciler strategy which parses the whole editor's content (a Velocity
 * template) on a document change.
 */
public class VelocityReconcilingStrategy implements IReconcilingStrategy,
    IReconcilingStrategyExtension
{
  private VelocityEditor fEditor;
  private Template fTemplate;
  private Template fLastTemplate;
  private String fError;
  private IDocument fDocument;
  private VelocityFoldingStructureProvider fFoldingStructureProvider;

  public VelocityReconcilingStrategy(VelocityEditor anEditor)
  {
    fEditor = anEditor;
    fFoldingStructureProvider = new VelocityFoldingStructureProvider(fEditor);
  }

  /**
   * DOCUMENT ME!
   * 
   * @param aDocument
   *          DOCUMENT ME!
   */
  @Override
public void setDocument(IDocument aDocument)
  {
    fDocument = aDocument;
    fFoldingStructureProvider.setDocument(fDocument);
    parse();
  }

  /**
   * DOCUMENT ME!
   * 
   * @param aDirtyRegion
   *          DOCUMENT ME!
   * @param aRegion
   *          DOCUMENT ME!
   */
  @Override
public void reconcile(DirtyRegion aDirtyRegion, IRegion aRegion)
  {
    parse();
  }

  /**
   * DOCUMENT ME!
   * 
   * @param aPartition
   *          DOCUMENT ME!
   */
  @Override
public void reconcile(IRegion aPartition)
  {
    parse();
  }

  private List htmlTags = null;

  private void parse()
  {
    String name = fEditor.getEditorInput().getName();
    Reader reader = new StringReader(fEditor.getDocument().get());
    Template template = null;
    try
    {
      parseHtml();
      if (fEditor.getEditorInput() instanceof IFileEditorInput)
        ((IFileEditorInput) fEditor.getEditorInput()).getFile().deleteMarkers(
            IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
      RuntimeInstance runtime = VelocityEditorEnvironment.getParser();
      SimpleNode root = runtime.parse(reader, name);
      // Create tree model
      NodeVisitor visitor = new NodeVisitor(name);
      root.jjtAccept(visitor, null);
      template = visitor.getTemplate();
      fError = "";
    }
    catch (ParseException e)
    {
      if (!name.matches(".*?\\.jsp.*"))
      {
        if (e.getMessage() != null)
        {
          fError = e.getMessage();
          Token token = e.currentToken;
          if (token != null)
          {
            fEditor.addProblemMarker(e.getMessage(), token.next.beginLine,
                IMarker.SEVERITY_ERROR);
          }
        }
        else
        {
          fError = "";
        }
      }
    }
    catch (Exception e)
    {
      fError = "";
      VelocityPlugin.log(e);
    }
    finally
    {
      try
      {
        reader.close();
      }
      catch (IOException e)
      {
        VelocityPlugin.log(e);
      }
    }
    // Replace saved template with the new parsed one
    synchronized (this)
    {
      if (template != null)
      {
        fTemplate = template;
        // Save last successful parse tree
        fLastTemplate = template;
      }
      else
      {
        fTemplate = null;
      }
    }
    // Update outline view and display error message in status line
    Display.getDefault().syncExec(new Runnable()
    {
      @Override
	public void run()
      {
        fEditor.updateOutlinePage();
        fEditor.displayErrorMessage(fError);
      }
    });
    reconcile();
  }

  private class HtmlNode implements ITreeNode
  {
    private Tag arg0 = null;

    /**
	 * 
	 */
    public HtmlNode(Tag arg0)
    {
      this.arg0 = arg0;
    }

    @Override
	public String getName()
    {
      return null;
    }

    @Override
	public Object getParent()
    {
      return null;
    }

    @Override
	public boolean hasChildren()
    {
      return false;
    }

    @Override
	public Object[] getChildren()
    {
      return NO_CHILDREN;
    }

    @Override
	public String getUniqueID()
    {
      return arg0.toString();
    }

    @Override
	public int getStartLine()
    {
      return arg0.getStartingLineNumber() + 1;
    }

    @Override
	public int getEndLine()
    {
      int nr = arg0.getStartingLineNumber() + 1;
      int nrE = nr;
      Tag endTag = arg0.getEndTag();
      if (endTag != null)
      {
        nrE = endTag.getEndingLineNumber();
        int offset = endTag.getStartPosition() - endTag.getEndPosition();
        if (offset == 0)
          fEditor.addProblemMarker(endTag.getTagName().toLowerCase()
              + " is not correctly closed proposed line for closing is line "
              + nrE, nr, IMarker.SEVERITY_WARNING);
      }
      return nrE;
    }

    @Override
	public boolean accept(ITreeVisitor aVisitor)
    {
      // TODO Auto-generated method stub
      return false;
    }
  }

  private class VHtmlNodeVisitor extends org.htmlparser.visitors.NodeVisitor
  {
    @Override
	public void visitTag(final Tag arg0)
    {
      htmlTags.add(new HtmlNode(arg0));
    }
  }

  /**
   * @throws ParserException
   */
  private void parseHtml() throws ParserException
  {
    htmlTags = new ArrayList();
    Parser parser = new Parser();
    parser.setInputHTML(fDocument.get());
    for (NodeIterator e = parser.elements(); e.hasMoreNodes();)
    {
      Node node = e.nextNode();
      VHtmlNodeVisitor htmlNodeVisitor = new VHtmlNodeVisitor();
      node.accept(htmlNodeVisitor);
    }
  }

  /**
   * Returns root elements of current parse tree.
   */
  public Object[] getRootElements()
  {
    return ((fTemplate != null) ? fTemplate.getChildren()
        : ITreeNode.NO_CHILDREN);
  }

  /**
   * Returns root node of current parse tree.
   */
  public ITreeNode getRootNode()
  {
    return fTemplate;
  }

  /**
   * Returns last successful parse tree.
   */
  public ITreeNode getLastRootNode()
  {
    return fLastTemplate;
  }

  /*
   * (non-Javadoc)
   * 
   * @seeorg.eclipse.jface.text.reconciler.IReconcilingStrategyExtension#
   * initialReconcile()
   */
  @Override
public void initialReconcile()
  {
    reconcile();
  }

  private void reconcile()
  {
    if (fTemplate != null && htmlTags != null)
      htmlTags.add(fTemplate);
    if (htmlTags != null)
    {
      fFoldingStructureProvider.updateFoldingRegions(htmlTags.toArray());// TODO
    }
  }

  /**
     * 
     */
  /*
   * (non-Javadoc)
   * 
   * @seeorg.eclipse.jface.text.reconciler.IReconcilingStrategyExtension#
   * setProgressMonitor(org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
public void setProgressMonitor(IProgressMonitor monitor)
  {
    // TODO Auto-generated method stub
  }
}
