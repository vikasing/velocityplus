package com.googlecode.veloeclipse.vaulttec.ui.editor.outline;

import net.sf.wdte.js.core.model.JSElementList;
import net.sf.wdte.js.core.parser.JSSyntaxModelFactory;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.googlecode.veloeclipse.editor.VelocityEditor;
import com.googlecode.veloeclipse.vaulttec.ui.editor.actions.CollapseAllAction;
import com.googlecode.veloeclipse.vaulttec.ui.model.ITreeNode;

/**
 * A content outline page which represents the content of an Velocity template
 * file.
 */
public class VelocityOutlinePage extends ContentOutlinePage
{

    private VelocityEditor               fEditor;
    private Object                       fInput;
    private String                       fSelectedNodeID;
    private VelocityOutlineLabelProvider fLabelProvider;
    private boolean                      fIsDisposed;

    /**
     * Creates a content outline page using the given editor.
     */
    public VelocityOutlinePage(VelocityEditor anEditor)
    {
        fEditor = anEditor;
        fIsDisposed = true;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
	public void createControl(Composite aParent)
    {
        super.createControl(aParent);
        fLabelProvider = new VelocityOutlineLabelProvider();
        // Init tree viewer
        TreeViewer viewer = getTreeViewer();
        viewer.setContentProvider(new VelocityOutlineContentProvider(fEditor));
        viewer.setLabelProvider(fLabelProvider);
        viewer.addSelectionChangedListener(this);
        if (fInput != null)
        {
            viewer.setInput(fInput);
        }
        fIsDisposed = false;
        // Add collapse all button to viewer's toolbar
        IToolBarManager mgr = getSite().getActionBars().getToolBarManager();
        mgr.add(new CollapseAllAction(viewer));
        // Refresh outline according to initial cursor position
        update();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    @Override
	public void selectionChanged(SelectionChangedEvent anEvent)
    {
        super.selectionChanged(anEvent);
        ISelection selection = anEvent.getSelection();
        if (!selection.isEmpty())
        {
            ITreeNode node = (ITreeNode) ((IStructuredSelection) selection).getFirstElement();
            if ((fSelectedNodeID == null) || isDifferentBlock(node))
            {
                fEditor.highlightNode(node, true);
                fSelectedNodeID = node.getUniqueID();
            } else
            {
                fEditor.revealNode(node);
            }
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param aLine
     *            DOCUMENT ME!
     * @param aForceSelect
     *            DOCUMENT ME!
     */
    public void selectNode(int aLine, boolean aForceSelect)
    {
        if (aLine > 0)
        {
            TreeViewer viewer = getTreeViewer();
            ITreeNode node = fEditor.getNodeByLine(aLine);
            viewer.removeSelectionChangedListener(this);
            if (node == null)
            {
                if (fSelectedNodeID != null)
                {
                    viewer.setSelection(new StructuredSelection());
                    fEditor.resetHighlightRange();
                    fSelectedNodeID = null;
                }
            } else
            {
                if (aForceSelect || isDifferentBlock(node))
                {
                    viewer.setSelection(new StructuredSelection(node));
                    // here we should fold
                    fEditor.fold(node.getStartLine(), node.getEndLine());
                    fEditor.highlightNode(node, false);
                    fSelectedNodeID = node.getUniqueID();
                }
                viewer.reveal(node);
            }
            viewer.addSelectionChangedListener(this);
        }
    }

    private boolean isDifferentBlock(ITreeNode aNode)
    {
        return ((fSelectedNodeID == null) || !fSelectedNodeID.equals(aNode.getUniqueID()));
    }

    /**
     * Sets the input of the outline page.
     */
    public void setInput(Object aInput)
    {
        fInput = aInput;
        update();
    }

    /**
     * Updates the outline page.
     */
    public void update()
    {
    	IDocument document = getDocument();
		JSSyntaxModelFactory factory = JSSyntaxModelFactory.getInstance();
		JSElementList model = factory.getContentOutline(document);
		if(model!=null){
        TreeViewer viewer = getTreeViewer();
        if (viewer != null)
        {
            Control control = viewer.getControl();
            if ((control != null) && !control.isDisposed())
            {
                viewer.removeSelectionChangedListener(this);
                control.setRedraw(false);
                //viewer.setInput(fInput);
                // viewer.expandAll();
                viewer.setInput(model);
                control.setRedraw(true);
                selectNode(fEditor.getCursorLine(), true);
                viewer.addSelectionChangedListener(this);
            }
        }
    }
		}
    private IDocument getDocument() {
		IDocumentProvider provider = fEditor.getDocumentProvider();
		return provider.getDocument(fEditor.getEditorInput());
	}
    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.part.IPage#dispose()
     */
    @Override
	public void dispose()
    {
        setInput(null);
        if (fLabelProvider != null)
        {
            fLabelProvider.dispose();
            fLabelProvider = null;
        }
        fIsDisposed = true;
        super.dispose();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public boolean isDisposed()
    {
        return fIsDisposed;
    }
}
