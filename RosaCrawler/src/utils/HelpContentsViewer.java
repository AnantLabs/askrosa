package utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HelpContentsViewer extends JFrame
{

    /**
     * 
     */
    private static final long serialVersionUID = 974400058846892764L;

    private JEditorPane editorPane;

    private Stack<URL> urlStack;

    private JButton btnBack;

    private JButton btnReload;

    private int HEIGHT = 600;

    private int WIDTH = 800;

    public HelpContentsViewer(final String path)
    {
	super("Help Contents");
	urlStack = new Stack<URL>();
	editorPane = new JEditorPane();
	editorPane.setEditable(false);
	editorPane.addHyperlinkListener(new MyHyperlinkListener());
	new Thread()
	{
	    public void run()
	    {
		try
		{
		    File file = new File(path);
		    editorPane.setPage(file.toURI().toURL());
		}
		catch (Exception e)
		{
		    editorPane.setText("Exception:" + e);
		}
	    }
	}.start();
	btnBack = new JButton("Back");
	btnBack.setMnemonic('B');
	btnBack.setToolTipText("Back to the last page");
	btnBack.addActionListener(new BtnBackL());
	btnReload = new JButton("Reload");
	btnReload.setMnemonic('R');
	btnReload.setToolTipText("Reload the URL");
	btnReload.addActionListener(new ActionListener()
	{

	    public void actionPerformed(ActionEvent e)
	    {
		try
		{
		    File file = new File(path);
		    urlStack.removeAllElements();
		    editorPane.setPage(file.toURI().toURL());
		}
		catch (Exception ex)
		{
		    editorPane.setText("Exception:" + ex.toString());
		}
	    }

	});
	JToolBar toolBar = new JToolBar();
	toolBar.add(btnBack);
	toolBar.add(btnReload);
	this.getContentPane().add(toolBar, BorderLayout.NORTH);
	this.getContentPane().add(new JScrollPane(editorPane),
		BorderLayout.CENTER);
	this.pack();
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	this.setSize(this.WIDTH, this.HEIGHT);
	this.setLocation((d.width - WIDTH) / 2, (d.height - HEIGHT) / 2);
	this.setVisible(true);
	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    class BtnBackL implements ActionListener
    {

	public void actionPerformed(ActionEvent e)
	{
	    if (urlStack.size() < 1)
		return;
	    try
	    {
		URL url = urlStack.pop();
		editorPane.setPage(url);
	    }
	    catch (Exception ex)
	    {
		editorPane.setText("Exception:" + e);
	    }
	}

    }

    class MyHyperlinkListener implements HyperlinkListener
    {
	public void hyperlinkUpdate(HyperlinkEvent e)
	{
	    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
	    {
		try
		{
		    urlStack.push(editorPane.getPage());
		    editorPane.setPage(e.getURL());
		}
		catch (IOException ex)
		{
		    editorPane.setText("Exception:" + e);
		}
	    }
	}
    }

}
