package utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

public class ClearTextListener implements ActionListener
{
    private JTextArea textArea;

    public ClearTextListener(JTextArea textArea)
    {
	this.textArea = textArea;
    }

   
    public void actionPerformed(ActionEvent e)
    {
	this.textArea.setText("");
    }

}