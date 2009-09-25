package gui;

import finder.FtpSitesFinderTask;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryParser.ParseException;
import org.jdom.JDOMException;

import crawlerutils.RosaCrawlerConstants;

import resource.CrawlerSetting;
import searcher.FtpSearch;
import searcher.SearchParameter;
import searcher.SearchResult;
import searcher.SearchResultElement;
import utils.DefaultFileFilter;
import utils.LookAndFeelManager;
import utils.Poolable;
import utils.PopupEditMenuManager;
import utils.Tools;
import database.Criteria;
import database.FtpSiteInfo;
import database.FtpSiteInfoPeer;
import database.FtpSitesManager;
import database.Scroller;

/**
 * GUI interface，providing index building,RMI search ,ftp site searching
 * interfaces
 * 
 * @author elegate
 */
public class CrawlerFrame extends JFrame
{

    /**
     * 
     */
    private static final long serialVersionUID = -5421878269537489326L;

    private static Dimension DEFAULT_SIZE = new Dimension(800, 600);


    private static final String START_IMPORT = "Start Import";

    private static final String STOP_IMPORT = "Stop Import";

    private static final DateFormat DF = Tools.DEFAULT_LOCAL_DATE_FORMAT;

    private static final int CMD_HISTORY_SIZE = CrawlerSetting
    .getInt("cmd.history.size");
    
    private java.util.Timer importTimer;


    private TrayIcon trayIcon;

    private JTable table;

    private JTextArea textImport;

    private JTextArea textFinder;
    
    private JTextPane searchResult;
    
    private JComboBox searchCombo;

    private DefaultComboBoxModel comboBoxModel;

    private JLabel lblCurrentPage;

    private JLabel lblTotalPages;

    private JLabel lblInfo;
    
    private JButton btnSearch;
    
    private JButton btnPrePage;
    
    private JButton btnNextPage;

    private FtpSitesTableModel model;

    private JMenuItem mnuItemImport;

    private JMenuItem mnuItemFinder;

    private Date startTime;;

//    private int interval; // minutes
    
    private int currentPage = 1;

    private int totalPages = 1;
    
    private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

    public CrawlerFrame()
    {
	String title = "Rosa GUI Helper Beta 0.1";
	this.setTitle(title);
	this.setSize(DEFAULT_SIZE);

	initParameter();

	layoutComponents();
	this.addWindowListener(new WindowAdapter()
	{
	    public void windowClosing(WindowEvent we)
	    {
		exit();
	    }
	});

	Tools.center(this);
	this.setVisible(true);
	addToSystemTray();

	loadCmdHistory();
	updateTable("id");
    }
    private void loadCmdHistory()
    {
	File file = new File("./conf/cmdHistory.txt");
	if(!file.exists())
	    return;
	try
	{
	    BufferedReader reader = new BufferedReader(new FileReader(
		    file));
	    String line = null;
	    while ((line = reader.readLine()) != null)
	    {
		if (StringUtils.isNotBlank(line))
		    queue.add(line.trim());
	    }
	    reader.close();
	    updateCmdHistory();
	    searchCombo.setSelectedItem(null);
	}
	catch (FileNotFoundException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }
    private void initParameter()
    {
	Calendar c = Calendar.getInstance();
	c.set(Calendar.HOUR_OF_DAY, 0);
	c.set(Calendar.MINUTE, 0);
	c.set(Calendar.SECOND, 0);
	startTime = c.getTime();
//	interval = 30;
    }

    public void setStarTime(Date time)
    {
	this.startTime = time;
    }

    public Date getStartTime()
    {
	return this.startTime;
    }

//    // in minutes
//    public void setInterval(int interval)
//    {
//	this.interval = interval;
//    }
//
//    public int getInterval()
//    {
//	return this.interval;
//    }

    public void layoutComponents()
    {
	JMenuBar mnuBar = new JMenuBar();
	JMenu mnuFile = new JMenu("File");
//	JMenu mnuTools = new JMenu("Tools");

//	mnuItemIndex = new JMenuItem(START_CRAWL);
//	mnuItemIndex.addActionListener(new MnuItemIndexL());

	// JMenuItem mnuItemUpdate = new JMenuItem("Update Ftp Sites");
	// mnuItemUpdate.addActionListener(new MnuItemShowL());

	// mnuItemServer = new JMenuItem(START_SERVER);
	// mnuItemServer.addActionListener(new MnuItemServerL());

	mnuItemImport = new JMenuItem(START_IMPORT);
	mnuItemImport.addActionListener(new MnuItemImportL());

	mnuItemFinder = new JMenuItem("Find FTP Sites");
	mnuItemFinder.addActionListener(new MnuItemFinderL());

	JMenuItem mnuItemExport = new JMenuItem("Export Ftp Sites");
	mnuItemExport.addActionListener(new MnuItemExportL());

	JMenuItem mnuExit = new JMenuItem("Exit");
	mnuExit.addActionListener(new ExitL());

//	mnuFile.add(mnuItemIndex);
	// mnuFile.add(mnuItemServer);
	mnuFile.add(mnuItemImport);
	mnuFile.add(mnuItemFinder);
	mnuFile.add(mnuItemExport);
	mnuFile.add(mnuExit);

//	JMenuItem mnuItemOption = new JMenuItem("Setting");
//	mnuItemOption.addActionListener(new ActionListener()
//	{
//
//	    public void actionPerformed(ActionEvent e)
//	    {
//		new SettingDialog(CrawlerFrame.this);
//	    }
//
//	});
//	mnuTools.add(mnuItemOption);

	mnuBar.add(mnuFile);
//	mnuBar.add(mnuTools);

	this.setJMenuBar(mnuBar);

	model = new FtpSitesTableModel();
	table = new JTable(model);
	table.setRowHeight(20);
	table.setFont(table.getFont().deriveFont(16.0f));
	table.getTableHeader().addMouseListener(new SortListener());
	TableColumnModel columnModel = table.getColumnModel();
	columnModel.getColumn(0).setPreferredWidth(20);
	columnModel.getColumn(1).setPreferredWidth(150);
	columnModel.getColumn(2).setPreferredWidth(50);
	// columnModel.getColumn(1).setMinWidth(150);

	
	searchResult = new JTextPane();
	searchResult.setEditable(false);
	PopupEditMenuManager.addPopupEditMenu(searchResult);
	searchResult.setContentType("text/html");
	searchResult.addHyperlinkListener(new PaperHyperlinkListener());
	
	JPanel panel = new JPanel(new BorderLayout());
	JPanel upPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	upPanel.add(new JLabel("Search:"));
	comboBoxModel = new DefaultComboBoxModel();
	searchCombo = new JComboBox(comboBoxModel);
	upPanel.add(searchCombo);
	searchCombo.setEditable(true);
	searchCombo.setMaximumRowCount(10);
	searchCombo.getEditor().getEditorComponent().addKeyListener(
		new KeyListener()
		{
		    public void keyPressed(KeyEvent event)
		    {
			int keyCode = event.getKeyCode();
			if (keyCode == KeyEvent.VK_ENTER)
			{
			    JTextField txtField = (JTextField) event
				    .getComponent();
			    searchCombo.setSelectedItem(txtField.getText());
			    btnSearch.doClick();
			}
		    }

		    public void keyReleased(KeyEvent event)
		    {
		    }

		    public void keyTyped(KeyEvent event)
		    {
		    }
		});
	btnSearch = new JButton("Go");
	btnSearch.addActionListener(new BtnSearchL());

	btnPrePage = new JButton("Previous");
	btnPrePage.setEnabled(false);
	btnPrePage.addActionListener(new ActionListener()
	{

	    public void actionPerformed(ActionEvent e)
	    {
		currentPage--;
		doSearch();
		updateInfoDisplay();
	    }

	});
	btnNextPage = new JButton("Next");
	btnNextPage.setEnabled(false);
	btnNextPage.addActionListener(new ActionListener()
	{

	    public void actionPerformed(ActionEvent e)
	    {
		currentPage++;
		doSearch();
		updateInfoDisplay();
	    }

	});

	upPanel.add(btnSearch);

	upPanel.add(Box.createHorizontalStrut(40));
	upPanel.add(new JLabel("Current Page:"));
	lblCurrentPage = new JLabel("" + currentPage);
	upPanel.add(lblCurrentPage);

	upPanel.add(new JLabel("Total Pages:"));
	lblTotalPages = new JLabel("" + totalPages);
	upPanel.add(lblTotalPages);

	upPanel.add(btnPrePage);
	upPanel.add(btnNextPage);
	Box b = Box.createVerticalBox();
	b.add(upPanel);

	b.add(Box.createVerticalStrut(10));
	JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	lblInfo = new JLabel(" ");
	infoPanel.add(lblInfo);
	b.add(infoPanel);
	
	panel.add(b, BorderLayout.NORTH);
	panel.add(new JScrollPane(searchResult), BorderLayout.CENTER);
//	textIndex = new JTextArea(20, 20);
//	textIndex.setEditable(false);
	// textIndex.setLineWrap(true);

	textImport = new JTextArea(20, 20);
	textImport.setEditable(false);
	// textImport.setLineWrap(true);

//	textSearchServer = new JTextArea(20, 20);
//	textSearchServer.setEditable(false);
	// textSearchServer.setLineWrap(true);

	textFinder = new JTextArea(20, 20);
	textFinder.setEditable(false);
	// textFinder.setLineWrap(true);

//	textResourceRequest = new JTextArea(20, 20);
//	textResourceRequest.setEditable(false);

	Font font = textImport.getFont().deriveFont(20.0f);
//	textIndex.setFont(font);
	textImport.setFont(font);
//	textSearchServer.setFont(font);
	textFinder.setFont(font);

//	PopupEditMenuManager.addPopupEditMenu(textIndex);
	PopupEditMenuManager.addPopupEditMenu(textImport);
//	PopupEditMenuManager.addPopupEditMenu(textSearchServer);
	PopupEditMenuManager.addPopupEditMenu(textFinder);
//	PopupEditMenuManager.addPopupEditMenu(textResourceRequest);

	JTabbedPane tabbedPane = new JTabbedPane();
	tabbedPane.add(new JScrollPane(table), "Ftp Sites");
	tabbedPane.add(panel,"Search");
//	tabbedPane.add(new JScrollPane(textIndex), "Update Info");
	tabbedPane.add(new JScrollPane(textImport), "Import Sites");
//	tabbedPane.add(new JScrollPane(textSearchServer), "Search Server");
	tabbedPane.add(new JScrollPane(textFinder), "FTP Sites Finder");
//	tabbedPane
//		.add(new JScrollPane(textResourceRequest), "Resource Request");

	this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
    
    private synchronized void doSearch()
    {
	SearchParameter p = new SearchParameter();
	p.setBegin((currentPage - 1) * RosaCrawlerConstants.PAGE_SIZE);
	p.setCount(RosaCrawlerConstants.PAGE_SIZE);
	String keyword = (String) searchCombo.getSelectedItem();
	p.setKeyword(keyword);
	searcher.SearchResult result = new SearchResult();
	try
	{
	    result = FtpSearch.search(p);
		int total = result.getHistNum();
		totalPages = (int) Math.ceil(total / (double) RosaCrawlerConstants.PAGE_SIZE);
		if (totalPages > currentPage)
		    enableNextPage(true);
		else
		    enableNextPage(false);
		displayResult(result, p);


		updateInfoDisplay();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	catch (ParseException e)
	{
	    e.printStackTrace();
	}
	catch (NotBoundException e)
	{
	    e.printStackTrace();
	}

    }
    private void displayResult(SearchResult result, SearchParameter parameter)
    {
	lblInfo.setText("Keyword: " + parameter.getKeyword()
		+ ", hits number: " + result.getHistNum() + ", " + RosaCrawlerConstants.PAGE_SIZE
		+ " results per page, time used: " + result.getDelay()
		+ " milliseconds");
	List<SearchResultElement> elements = result.getResultFileList();
	String text = "<html><body><TABLE WIDTH=100%  style=\"table-layout:fixed\" BORDER=0 CELLPADDING=0 CELLSPACING=0>";
	text+="<TR><TD bgcolor=\"#F5F5F5\" width=\"60%\"style=\"text-align:left\" nowrap>Name</TD><TD bgcolor=\"#F5F5F5\"  style=\"text-align:left\" nowrap>Site</TD></TR>";
	for (SearchResultElement ele : elements)
	{
	    text += "<TR><td><a href=\"" + ele.getAbsolutePath() + "\">" + ele.getHighlightedName()
		    + "</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>";
	    text += "<td><a href=\"" + ele.getFtpAddress() + "\">" + ele.getServer() + "</a><br></td></tr>";

	}
	text += "</table></body></html>";
	text = text.replaceAll("\n", "<br>");
	searchResult.setText(text);
	searchResult.setCaretPosition(0);
    }
    private void enableNextPage(boolean b)
    {
	btnNextPage.setEnabled(b);
	if (currentPage > 1)
	    btnPrePage.setEnabled(true);
	else
	    btnPrePage.setEnabled(false);
    }
    private void updateInfoDisplay()
    {
	lblCurrentPage.setText("" + currentPage);
	lblTotalPages.setText("" + totalPages);
    }
    private class BtnSearchL implements ActionListener
    {

	public void actionPerformed(ActionEvent e)
	{
	    currentPage = 1;
	    totalPages = 1;
	    String input = (String) searchCombo.getSelectedItem();
	    if (StringUtils.isNotBlank(input))
	    {
		doSearch();

		if (!queue.contains(input))
		{
		    if (queue.size() >= CMD_HISTORY_SIZE)
		    {
			queue.remove();
		    }
		    queue.add(input);
		}
		else
		{
		    queue.remove(input);
		    queue.add(input);
		}
		updateCmdHistory();
		searchCombo.setSelectedIndex(0);
	    }
	}
    }
    
    private void updateCmdHistory()
    {
	comboBoxModel.removeAllElements();
	for (String ele : queue)
	    comboBoxModel.insertElementAt(ele, 0);
    }

    private class PaperHyperlinkListener implements HyperlinkListener
    {

	public void hyperlinkUpdate(HyperlinkEvent e)
	{
	    URL url = e.getURL();
	    if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
		return;
	    openURL(url);
	}

    }
    
    private Process openURL(URL url)
    {
	try
	{
	    String osName = System.getProperty("os.name").toLowerCase();
	    Runtime rt = Runtime.getRuntime();
	    if (osName.indexOf("win") >= 0)
	    {
		return rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
	    }
	    else if (osName.indexOf("mac") >= 0)
	    {
		return rt.exec("open " + url);
	    }

	    else if (osName.indexOf("ix") >= 0 || osName.indexOf("ux") >= 0
		    || osName.indexOf("sun") >= 0)
	    {
		String[] browsers =
		{ "epiphany", "firefox", "mozilla", "konqueror", "netscape",
			"opera", "links", "lynx" };

		// Build a command string which looks like "browser1 "url"
		// || browser2 "url" ||..."
		StringBuffer cmd = new StringBuffer();
		for (int i = 0; i < browsers.length; i++)
		    cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \""
			    + url + "\" ");

		return rt.exec(new String[]
		{ "sh", "-c", cmd.toString() });
	    }
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
	return null;
    }

    
    private class SortListener extends MouseAdapter
    {
	public void mouseClicked(MouseEvent event)
	{
	    if (event.getClickCount() < 2)
		return;
	    else
	    {
		int column = table.columnAtPoint(event.getPoint());
		int modelColumn = table.convertColumnIndexToModel(column);
		updateTable(model.getColumnFieldName(modelColumn));
	    }
	}
    }

    private class ExitL implements ActionListener
    {

	public void actionPerformed(ActionEvent e)
	{
	    exit();
	}
    }

//    private class MnuItemIndexL implements ActionListener
//    {
//
//	public void actionPerformed(ActionEvent e)
//	{
//	    if (mnuItemIndex.getText().equals(START_CRAWL))
//	    {
//		indexTimer = new Timer("Index Timer", true);
//		textIndex.append("Start index task,first at "
//			+ DF.format(startTime) + ",interval is " + interval
//			+ " minutes\n");
//		indexTimer.schedule(new IndexTask(), startTime, interval
//			* UsefulConstants.MINUTE_IN_MILLISECOND);
//		mnuItemIndex.setText(STOP_CRAWL);
//	    }
//	    else
//	    {
//		textIndex.append("Index task stoped\n");
//		indexTimer.cancel();
//		indexTimer = null;
//		mnuItemIndex.setText(START_CRAWL);
//	    }
//	}
//    }

//    private void startResourceRequestResponse()
//    {
//	resourceRequestTimer = new Timer("Resource Request", true);
//	resourceRequestTimer.schedule(new ResourceRequestTask(), new Date(),
//		UsefulConstants.ONE_HOUR_IN_MILLISECOND*ProjectSetting.getInt("ftp.resource.request.check.interval"));
//    }

    // private class MnuItemServerL implements ActionListener
    // {
    //
    // @Override
    // public void actionPerformed(ActionEvent arg0)
    // {
    // if (mnuItemServer.getText().equals(START_SERVER))
    // {
    // startServer();
    //
    // mnuItemServer.setText(STOP_SERVER);
    // }
    // else
    // {
    // stopServer();
    //
    // mnuItemServer.setText(START_SERVER);
    // }
    //
    // }
    // }

//    private void startServer()
//    {
//	try
//	{
//	    Directory dir = FSDirectory.getDirectory(ProjectSetting
//		    .getProperty("index"));
//	    if (IndexReader.indexExists(dir))
//	    {
//		// RAMDirectory ramDir = new RAMDirectory(dir);
//		Searcher searcher = new IndexSearcher(dir);
//		RemoteSearchable remote = new RemoteSearchable(searcher);
//		InetAddress add = InetAddress.getLocalHost();
//		String address = add.getHostAddress();
//		String str = "rmi://" + address + "/"
//			+ ProjectSetting.getProperty("node.name");
//		Naming.rebind(str, remote);
//		textSearchServer.append("Server started,bind to " + str + "\n");
//	    }
//	}
//	catch (RemoteException e)
//	{
//	    e.printStackTrace();
//	}
//	catch (IOException e)
//	{
//	    e.printStackTrace();
//	}
//    }

//    private void restartServer()
//    {
//	try
//	{
//	    Directory dir = FSDirectory.getDirectory(ProjectSetting
//		    .getProperty("index"));
//	    if (IndexReader.indexExists(dir))
//	    {
//		// RAMDirectory ramDir = new RAMDirectory(dir);
//		Searcher searcher = new IndexSearcher(dir);
//		RemoteSearchable remote = new RemoteSearchable(searcher);
//		InetAddress add = InetAddress.getLocalHost();
//		String address = add.getHostAddress();
//		String str = "rmi://" + address + "/"
//			+ ProjectSetting.getProperty("node.name");
//		Naming.rebind(str, remote);
//		textSearchServer.append("Server restarted,bind to " + str
//			+ " at " + DF.format(new Date()) + "\n");
//	    }
//	}
//	catch (RemoteException e)
//	{
//	    e.printStackTrace();
//	}
//	catch (IOException e)
//	{
//	    e.printStackTrace();
//	}
//    }

//    private void stopServer()
//    {
//	try
//	{
//	    InetAddress add = InetAddress.getLocalHost();
//	    String address = add.getHostAddress();
//	    String str = "rmi://" + address + "/"
//		    + ProjectSetting.getProperty("node.name");
//	    Naming.unbind(str);
//	    textSearchServer.append("Server stoped\n");
//	}
//	catch (UnknownHostException e)
//	{
//	    // e.printStackTrace();
//	}
//	catch (RemoteException e)
//	{
//	    // e.printStackTrace();
//	}
//	catch (MalformedURLException e)
//	{
//	    // e.printStackTrace();
//	}
//	catch (NotBoundException e)
//	{
//	    // e.printStackTrace();
//	}
//
//    }

    private class MnuItemImportL implements ActionListener
    {

	public void actionPerformed(ActionEvent e)
	{
	    if (mnuItemImport.getText().equals(START_IMPORT))
	    {
		File file = getXMLFile(true);
		if (file != null)
		{
		    importTimer = new Timer("Import Timer", true);
		    importTimer.schedule(new ImportFtpSitesTask(file),
			    startTime, 120
				    * RosaCrawlerConstants.MINUTE_IN_MILLISECOND);
		    mnuItemImport.setText(STOP_IMPORT);
		}
	    }
	    else
	    {
		importTimer.cancel();
		importTimer = null;
		mnuItemImport.setText(START_IMPORT);
	    }

	}
    }

    private class MnuItemFinderL implements ActionListener
    {

	public void actionPerformed(ActionEvent e)
	{
	    final File xml = getXMLFile(true);
	    if (xml != null)
		new FtpSitesFinderThread(xml).start();
	}
    }

    private class FtpSitesFinderThread extends Thread implements
	    Poolable<String>
    {
	private File xml;

	public FtpSitesFinderThread(File xml)
	{
	    this.xml = xml;
	}

	public void run()
	{
	    mnuItemFinder.setEnabled(false);
	    new FtpSitesFinderTask(this).findSites(xml);
	    mnuItemFinder.setEnabled(true);
	}

	public boolean pool(String obj)
	{
	    textFinder.append(obj + "\n");
	    return true;
	}

    }

    private class MnuItemExportL implements ActionListener
    {

	public void actionPerformed(ActionEvent e)
	{
	    File file = getOutputFile(false);
	    if (file != null)
	    {
		try
		{
		    String name = file.getName().toLowerCase();
		    if (name.endsWith("xml"))
			FtpSitesManager.storeFtpSitesToXML(file);
		    else if (name.endsWith("txt"))
			FtpSitesManager.storeFtpSitesToText(file);
		}
		catch (Exception e1)
		{
		    JOptionPane.showMessageDialog(CrawlerFrame.this, e1
			    .getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		JOptionPane.showMessageDialog(CrawlerFrame.this,
			"Ftp sites have been saved to " + file, "Info",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}

    }

    // private class MnuItemShowL implements ActionListener
    // {
    //
    // 
    // public void actionPerformed(ActionEvent e)
    // {
    // updateTable("server");
    // }
    //
    // }
    private File getXMLFile(boolean open)
    {
	JFileChooser fileChooser = new JFileChooser("");
	fileChooser.setAcceptAllFileFilterUsed(false);
	DefaultFileFilter filter = new DefaultFileFilter("xml",
		"XML for FlashFxp");
	fileChooser.setFileFilter(filter);
	int option = 0;
	if (open)
	    option = fileChooser.showOpenDialog(this);
	else
	    option = fileChooser.showSaveDialog(this);
	if (option == JFileChooser.APPROVE_OPTION)
	{
	    File file = fileChooser.getSelectedFile();
	    return file;
	}
	return null;
    }

    private File getOutputFile(boolean open)
    {
	JFileChooser fileChooser = new JFileChooser("");
	fileChooser.setAcceptAllFileFilterUsed(false);
	DefaultFileFilter filter = new DefaultFileFilter("xml",
		"XML for FlashFxp");
	fileChooser.setFileFilter(filter);
	filter = new DefaultFileFilter("txt", "Text for CuteFtp");
	fileChooser.setFileFilter(filter);
	int option = 0;
	if (open)
	    option = fileChooser.showOpenDialog(this);
	else
	    option = fileChooser.showSaveDialog(this);
	if (option == JFileChooser.APPROVE_OPTION)
	{
	    File file = fileChooser.getSelectedFile();
	    return file;
	}
	return null;
    }

    private class ImportFtpSitesTask extends TimerTask implements
	    Poolable<String>
    {
	private File file;

	public ImportFtpSitesTask(File file)
	{
	    this.file = file;
	}

	public void run()
	{
	    pool("********** Start time:" + DF.format(new Date())
		    + " **********");
	    try
	    {
		FtpSitesManager.addFtpSitesFromXML(file, this);
	    }
	    catch (JDOMException e)
	    {
		JOptionPane.showMessageDialog(CrawlerFrame.this,
			e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	    catch (IOException e)
	    {
		JOptionPane.showMessageDialog(CrawlerFrame.this,
			e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	    pool("********** Finish time:" + DF.format(new Date())
		    + " **********");
	}

	public boolean pool(String obj)
	{
	    textImport.append(obj + "\n");
	    return false;
	}
    }

    private void updateTable(final String sortField)
    {
	Criteria c = new Criteria();
	c.addAscendingOrder(sortField);
	Scroller<FtpSiteInfo> scr;
	try
	{
	    scr = FtpSiteInfoPeer.doSelect(c);
	    model.update(scr);
	}
	catch (Exception e1)
	{
	    JOptionPane.showMessageDialog(CrawlerFrame.this, e1.getMessage(),
		    "Error", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void addToSystemTray()
    {
	if (SystemTray.isSupported())
	{
	    SystemTray tray = SystemTray.getSystemTray();
	    Image image = Toolkit.getDefaultToolkit().getImage(
		    "res/icons/tray.png");

	    PopupMenu popup = new PopupMenu();
	    MenuItem defaultItem = new MenuItem("Exit");
	    defaultItem.addActionListener(new ExitL());
	    popup.add(defaultItem);

	    trayIcon = new TrayIcon(image, "Double Click to hide the window",
		    popup);

	    ActionListener actionListener = new ActionListener()
	    {
		public void actionPerformed(ActionEvent e)
		{
		    if (CrawlerFrame.this.isVisible())
		    {
			CrawlerFrame.this.setVisible(false);
			trayIcon.setToolTip("Double Click to show the window");
		    }
		    else
		    {
			CrawlerFrame.this.setVisible(true);
			trayIcon.setToolTip("Double Click to hide the window");
		    }
		}
	    };

	    trayIcon.setImageAutoSize(true);
	    trayIcon.addActionListener(actionListener);

	    try
	    {
		tray.add(trayIcon);
	    }
	    catch (AWTException e)
	    {
		System.err.println("TrayIcon could not be added.");
	    }
	}
	else
	{
	    JOptionPane.showMessageDialog(this, "System tray not supported",
		    "Info", JOptionPane.INFORMATION_MESSAGE);
	}
    }

    private void exit()
    {
	if (SystemTray.isSupported())
	{
	    SystemTray tray = SystemTray.getSystemTray();
	    tray.remove(trayIcon);
	}
	saveCmdHistory();
	System.exit(0);
    }

    private void saveCmdHistory()
    {
	BufferedWriter writer;
	try
	{
	    writer = new BufferedWriter(new FileWriter(new File("conf/cmdHistory.txt")));
	    for (String ele : queue)
	    {
		writer.write(ele + "\n");
	    }
	    writer.close();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}

    }

    public static void main(String[] args) throws IOException
    {
	System.out.println("TotalMemory:" + Runtime.getRuntime().maxMemory()
		/ RosaCrawlerConstants.MB + " M");
	
	CrawlerSetting.setProxy();
	LookAndFeelManager.changeToSystemLookAndFeel(null);
	new CrawlerFrame();
    }
}
