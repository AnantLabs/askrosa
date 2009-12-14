package utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

public class PopupMouseListener extends MouseAdapter
{
    private JPopupMenu popupMenu;

    public PopupMouseListener(JPopupMenu mnu)
    {
	this.popupMenu = mnu;
    }

    public void mousePressed(MouseEvent me)
    {
	if (me.getButton() == MouseEvent.BUTTON3)
	{
	    this.popupMenu.show(me.getComponent(), me.getX(), me.getY());
	}
    }
}