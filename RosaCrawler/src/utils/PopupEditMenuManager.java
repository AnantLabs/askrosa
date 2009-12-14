package utils;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;

public final class PopupEditMenuManager
{
    public static void addPopupEditMenu(JTextComponent txtComponent)
    {
	JPopupMenu popupMenu = new JPopupMenu();
	JMenuItem mnuCut = new JMenuItem("Cut");
	mnuCut.addActionListener(new CutL(txtComponent));
	JMenuItem mnuCopy = new JMenuItem("Copy");
	mnuCopy.addActionListener(new CopyL(txtComponent));
	JMenuItem mnuPaste = new JMenuItem("Paste");
	mnuPaste.addActionListener(new PasteL(txtComponent));
	JMenuItem mnuSelectAll = new JMenuItem("Select All");
	mnuSelectAll.addActionListener(new SelectAllL(txtComponent));
	JMenuItem mnuClear = new JMenuItem("Clear");
	mnuClear.addActionListener(new ClearL(txtComponent));
	popupMenu.add(mnuCut);
	popupMenu.add(mnuCopy);
	popupMenu.add(mnuPaste);
	popupMenu.add(mnuSelectAll);
	popupMenu.add(mnuClear);
	txtComponent.add(popupMenu);
	txtComponent.addMouseListener(new MouseL(popupMenu, txtComponent,
		mnuCut, mnuPaste));
    }

    private static class MouseL extends MouseAdapter
    {
	private JPopupMenu popupMenu;

	private JTextComponent txtComp;

	private JMenuItem mnuCut;

	private JMenuItem mnuPaste;

	public MouseL(JPopupMenu popup, JTextComponent txtComp,
		JMenuItem mnuCut, JMenuItem mnuPaste)
	{
	    this.popupMenu = popup;
	    this.txtComp = txtComp;
	    this.mnuCut = mnuCut;
	    this.mnuPaste = mnuPaste;
	}

	public void mouseClicked(MouseEvent me)
	{
	    if (me.getButton() == MouseEvent.BUTTON3 && me.getClickCount() == 1)
	    {
		if (txtComp.isEditable())
		{
		    mnuCut.setEnabled(true);
		    mnuPaste.setEnabled(true);
		}
		else
		{
		    mnuCut.setEnabled(false);
		    mnuPaste.setEnabled(false);
		}

		if (txtComp instanceof JTextPane)
		{
		    mnuPaste.setEnabled(true);
		}
		Point p = me.getPoint();
		popupMenu.show(txtComp, p.x, p.y);
	    }
	}
    }

    private static class CopyL implements ActionListener
    {
	private JTextComponent txtComp;

	public CopyL(JTextComponent txtComp)
	{
	    this.txtComp = txtComp;
	}

	public void actionPerformed(ActionEvent e)
	{
	    if (this.txtComp instanceof JTextPane)
	    {
		JTextPane pane = (JTextPane) this.txtComp;
		DefaultStyledDocument dsd = (DefaultStyledDocument) pane
			.getStyledDocument();
		Element ele = dsd.getParagraphElement(0).getElement(0);
		if (ele.getName().equals("icon"))
		{
		    ImageIcon icon = (ImageIcon) StyleConstants.getIcon(ele
			    .getAttributes());
		    ImageSelection imageSelection = new ImageSelection(icon
			    .getImage());
		    Clipboard cb = Toolkit.getDefaultToolkit()
			    .getSystemClipboard();
		    cb.setContents(imageSelection, null);
		}
	    }
	    else
	    {
		txtComp.copy();
	    }
	}

    }

    private static class CutL implements ActionListener
    {
	private JTextComponent txtComp;

	public CutL(JTextComponent txtComp)
	{
	    this.txtComp = txtComp;
	}

	public void actionPerformed(ActionEvent e)
	{
	    txtComp.cut();
	}

    }

    private static class PasteL implements ActionListener
    {
	private JTextComponent txtComp;

	public PasteL(JTextComponent txtComp)
	{
	    this.txtComp = txtComp;
	}

	public void actionPerformed(ActionEvent e)
	{

	    Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
	    Transferable contents = cb.getContents(null);
	    if (contents != null
		    && contents.isDataFlavorSupported(DataFlavor.stringFlavor))
	    {
		txtComp.paste();
	    }
	    else if (contents != null
		    && contents.isDataFlavorSupported(DataFlavor.imageFlavor))
	    {
		try
		{
		    Image img = (Image) contents
			    .getTransferData(DataFlavor.imageFlavor);
		    Icon icon = new ImageIcon(img);
		    if (txtComp instanceof JTextPane)
		    {
			txtComp.setText("");
			((JTextPane) txtComp).insertIcon(icon);
		    }
		}
		catch (UnsupportedFlavorException ex)
		{
		    JOptionPane.showMessageDialog(null, ex, "Error",
			    JOptionPane.ERROR_MESSAGE);
		    ex.printStackTrace();
		}
		catch (IOException exp)
		{
		    exp.printStackTrace();
		}
	    }
	}
    }

    private static class SelectAllL implements ActionListener
    {
	private JTextComponent txtComp;

	public SelectAllL(JTextComponent txtComp)
	{
	    this.txtComp = txtComp;
	}

	public void actionPerformed(ActionEvent e)
	{
	    int l = txtComp.getText().length();
	    txtComp.setSelectionStart(0);
	    txtComp.setSelectionEnd(l);
	}
    }

    private static class ClearL implements ActionListener
    {
	private JTextComponent txtComp;

	public ClearL(JTextComponent txtComp)
	{
	    this.txtComp = txtComp;
	}

	public void actionPerformed(ActionEvent arg0)
	{
	    txtComp.setText("");

	}

    }
}

class ImageSelection implements Transferable
{

    public ImageSelection(Image image)
    {
	this.image = image;
    }

    public Object getTransferData(DataFlavor flavor)
	    throws UnsupportedFlavorException, IOException
    {
	if (flavor.equals(DataFlavor.imageFlavor))
	{
	    return image;
	}
	else
	{
	    throw new UnsupportedFlavorException(flavor);
	}
    }

    public DataFlavor[] getTransferDataFlavors()
    {
	return new DataFlavor[]
	{ DataFlavor.imageFlavor };
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
	return DataFlavor.imageFlavor.equals(flavor);
    }

    private Image image;
}
