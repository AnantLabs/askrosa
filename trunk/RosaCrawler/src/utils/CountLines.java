package utils;

import javax.swing.*;

import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;
import java.awt.*;
import utils.DefaultFileFilter;
/**
 * count the lines of some files
 * @author Elegate
 * @author cs department of NJU
 */
public class CountLines extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JLabel lblResult=null;
	private JButton btnOpen=null;
	
	private String filters[]={"java","jsp","c","cpp","h","txt"
	};
	private DefaultFileFilter fileFilter=new DefaultFileFilter(filters,"Countable Files");
	
	private IOFileFilter ioFileFilter = new IOFileFilter(filters);
	public CountLines()
	{
		super("Count Lines");
		btnOpen=new JButton("Open");
		btnOpen.setMnemonic('O');
		btnOpen.addActionListener(new BtnOpenL());
		JButton btnQuit=new JButton("Quit");
		btnQuit.setMnemonic('Q');
		btnQuit.addActionListener(new BtnQuitL());
		lblResult=new JLabel("Files:0,Lines:0",new ImageIcon("chat/icon/lines.gif"),JLabel.CENTER);
		JPanel btnPanel=new JPanel();
		btnPanel.add(btnOpen);
		btnPanel.add(btnQuit);
		this.getContentPane().add("Center",lblResult);
		this.getContentPane().add("South",btnPanel);
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we)
			{
				solveCountLinesClose();
			}
		});
		
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)(d.getWidth()-300)/2,(int)(d.getHeight()-150)/2);
		this.setSize(300,150);
		this.setResizable(false);
		this.setVisible(true);
	}
	/**
	 *close the CountLines
	 */
	protected void solveCountLinesClose()
	{
		lblResult.setText("Files:0,Lines:0");
		this.setVisible(false);
		this.dispose();
	}
	
	protected void recursiveList(File[] files,LinkedList<File> list)
	{
	    
	    for(File f:files)
	    {
		if(f.isDirectory())
		{
		    File[] fileLst = f.listFiles(ioFileFilter);
		    recursiveList(fileLst,list);
		}
		else
		{
		    list.add(f);
		}
	    }
	}
	/**
	 * open files to count
	 * @author Elegate
	 * @author cs department of NJU
	 */
	class BtnOpenL implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			JFileChooser fileChooser=new JFileChooser();
			
			fileChooser.setFileFilter(fileFilter);
			fileChooser.setMultiSelectionEnabled(true);
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int option=fileChooser.showOpenDialog(CountLines.this);
			if(option==JFileChooser.APPROVE_OPTION)
			{
				File files[]=fileChooser.getSelectedFiles();
				LinkedList<File> list = new LinkedList<File>();
				recursiveList(files,list);
				new CountLinesThread(list);
			}
		}
	}
	/**
	 * to quit the CountLines
	 * @author Elegate
	 * @author cs department of NJU
	 */
	class BtnQuitL implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			solveCountLinesClose();
		}
	}
	/**
	 * thread to count lines
	 * @author Elegate
	 * @author cs department of NJU
	 */
	class CountLinesThread extends Thread
	{
		private LinkedList<File> list;
		public CountLinesThread(LinkedList<File> list)
		{
			super("CountLinesThread");
			this.list=list;
			this.start();
		}
		public void run()
		{
			int lines=0;
			btnOpen.setEnabled(false);
			lblResult.setText("Counting.....");
			for(File file:list)
			{
				try
				{
					RandomAccessFile ras=new RandomAccessFile(file,"r");
					while(ras.getFilePointer()!=ras.length())
					{
						ras.readLine();
						lines+=1;
					}
					ras.close();
				}
				catch(Exception e)
				{
//					File logFile=new File("chat/log.txt");
//					try
//					{
//						PrintStream ps=new PrintStream(new FileOutputStream(logFile,logFile.exists()));
//						e.printStackTrace(ps);
//						ps.close();
//					}
//					catch(FileNotFoundException fnfe)
//					{
//						fnfe.printStackTrace();
//					}
					e.printStackTrace();
				}
			}
			lblResult.setText("Files:"+list.size()+",Lines:"+lines);
			btnOpen.setEnabled(true);
		}
	}
	
	
	public static void main(String[] args)
	{
	    try
	    {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }
	    catch (ClassNotFoundException e)
	    {
		e.printStackTrace();
	    }
	    catch (InstantiationException e)
	    {
		e.printStackTrace();
	    }
	    catch (IllegalAccessException e)
	    {
		e.printStackTrace();
	    }
	    catch (UnsupportedLookAndFeelException e)
	    {
		e.printStackTrace();
	    }
		new CountLines();
	}
}
