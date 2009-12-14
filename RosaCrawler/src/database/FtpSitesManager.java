package database;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.net.ftp.FTPClient;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import utils.Poolable;
import utils.StringPatternMatcher;
import utils.StringPatternMatcherFactory;

public class FtpSitesManager
{
    /**
     * 检查输入是否是域名或IP
     */
    public static StringPatternMatcher IP_DOMAIN_MATCHER = StringPatternMatcherFactory
	    .createStringPatternMatcher(StringPatternMatcherFactory.PATTERN_DOMAIN
		    + StringPatternMatcherFactory.PATTERN_IP);

    // /**
    // * 测试站点的连通性
    // *
    // * @param ftpSite
    // * 站点信息
    // * @return 连通返回true，否则返回false
    // */
    // public static boolean testConnection(FtpSiteInfo ftpSite)
    // {
    // return testConnection(ftpSite);
    // }

    /**
     * @param ftpSite
     *                站点信息
     * @param timeout
     *                超时
     * @return 连通返回true，否则返回false
     */
    private static boolean testConnection(FtpSiteInfo ftpSite)
    {
	FTPClient ftp = new FTPClient();
	try
	{
	    ftp.connect(ftpSite.getServer(), ftpSite.getPort());
	    ftp.login(ftpSite.getUsername(), ftpSite.getPassword());
	}
	catch (Exception e)
	{
	    return false;
	}
	finally
	{
	    if (ftp.isConnected())
	    {
		try
		{
		    ftp.disconnect();
		}
		catch (Exception f)
		{
		    return false;
		}
	    }
	}
	return true;
    }

    public static int TotalFtpSitesNumber() throws Exception
    {
	Criteria c = new Criteria();
	Scroller<FtpSiteInfo> scr = FtpSiteInfoPeer.doSelect(c);
	return scr.size();
    }

    public static ArrayList<FtpFileCount> TotalFileCount()
    {
	ArrayList<FtpFileCount> ftpFileCountList = new ArrayList<FtpFileCount>();
	try
	{
	    String sql = "select location,count(id),sum(totalFileCount) as totalfilecount from  FTPSITEINFO group by location";
	    Statement statement;
	    ResultSet results;

	    Connection connection = Database.getConnection();
	    statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
		    ResultSet.CONCUR_READ_ONLY);
	    results = statement.executeQuery(sql);

	    while (results.next())
	    {
		FtpFileCount ftpFileCount = new FtpFileCount();
		ftpFileCount.setLocation((short) Integer.parseInt(results.getString(1)));
		ftpFileCount.setFtpCount(Integer.parseInt(results.getString(2)));
		ftpFileCount.setFileCount(Integer.parseInt(results.getString(3)));
		ftpFileCountList.add(ftpFileCount);
	    }
	    Database.release(connection);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return ftpFileCountList;
    }

    public static Scroller<FtpSiteInfo> hotFtpSites(int top)
    {
	String sql = "select * from  FTPSITEINFO " + " order by hot DESC" + " limit 0 "
		+ "," + top;
	try
	{
	    Scroller<FtpSiteInfo> scr = FtpSiteInfoPeer.doSelect(sql);
	    return scr;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return null;
    }

    public static void hotAdd(String server, int count)
    {
	// String sql="update ftpsearch.FTPSITEINFO set hot =hot+"+count+"where
	// server="+server;
	Criteria c = new Criteria();
	c.add(FtpSiteInfo.SERVER, server);
	try
	{
	    Scroller<FtpSiteInfo> scr = FtpSiteInfoPeer.doSelect(c);
	    if (scr.hasNext())
	    {
		FtpSiteInfo ftpSiteInfo = scr.next();
		ftpSiteInfo.setHot(ftpSiteInfo.getHot() + count);
		FtpSiteInfoPeer.doUpdate(ftpSiteInfo);
	    }
	    return;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return;
    }

    public static Scroller<FtpSiteInfo> TotalFileCount(String ftpAddress,
	    String author, String dateFrom, String dateTo, short location)
    {
	try
	{
	    String sql = "select * from FTPSITEINFO";
	    String where = " ";
	    boolean and = false;
	    if (!(ftpAddress == null || ftpAddress.trim().equals("")))
	    {
		where += " server like '%" + ftpAddress.trim() + "%'";
		and = true;
	    }
	    if (!(author == null || author.trim().equals("")))
	    {
		if (and == true)
		    where += " and";
		where += " contact like '%" + author.trim() + "%'";
		and = true;
	    }
	    if (!(dateFrom == null || dateFrom.trim().equals("")))
	    {
		if (and == true)
		    where += " and";
		where += " updateTime > " + dateFrom.trim();
		and = true;
	    }
	    if (!(dateTo == null || dateTo.trim().equals("")))
	    {
		if (and == true)
		    where += " and";
		where += " updateTime < " + dateTo.trim();
	    }
	    if (location != -1)
	    {
		if (and == true)
		    where += " and";
		where += " location = " + location;
	    }

	    if (!where.trim().equals(""))
		sql += " where " + where;
	    return FtpSiteInfoPeer.doSelect(sql);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return null;
    }

    public static Scroller<FtpSiteInfo> listPagedFtpSites(String sort, int from, int num,
	    String ftpAddress, String author, String dateFrom, String dateTo, short location)
    {
	try
	{
	    String sql = "select * from FTPSITEINFO";
	    String where = " ";
	    boolean and = false;
	    if (!(ftpAddress == null || ftpAddress.trim().equals("")))
	    {
		where += " server like '%" + ftpAddress.trim() + "%'";
		and = true;
	    }
	    if (!(author == null || author.trim().equals("")))
	    {
		if (and == true)
		    where += " and";
		where += " contact like '%" + author.trim() + "%'";
		and = true;
	    }
	    if (!(dateFrom == null || dateFrom.trim().equals("")))
	    {
		if (and == true)
		    where += " and";
		where += " updateTime > " + dateFrom.trim();
		and = true;
	    }
	    if (!(dateTo == null || dateTo.trim().equals("")))
	    {
		if (and == true)
		    where += " and";
		where += " updateTime < " + dateTo.trim();
	    }
	    if (location != -1)
	    {
		if (and == true)
		    where += " and";
		where += " location = " + location;
	    }

	    if (!where.trim().equals(""))
		sql += " where " + where;

	    if (sort == null)
		sort = "hot";
	    if (sort.equals("server"))
		sql += " order by " + sort + " ASC";
	    else
		sql += " order by " + sort + " DESC";
	    sql += " limit " + from + "," + num;
	    return FtpSiteInfoPeer.doSelect(sql);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 不测试连通性直接添加到数据库
     * 
     * @param ftpSite
     *                站点信息
     * @return 正确返回null，否则返回错误描述
     */
    public static String addFtpSiteWithoutCheck(FtpSiteInfo ftpSite)
    {
	try
	{
	    InetAddress add = InetAddress.getByName(ftpSite.getServer());
	    if (IP_DOMAIN_MATCHER.match(add.getHostName()))
		ftpSite.setServer(add.getHostName());
	    else
		ftpSite.setServer(add.getHostAddress());
	    ftpSite.setAddress(add.getHostAddress());
	    ftpSite.save();
	    return null;
	}
	catch (UnknownHostException e)
	{
	    return e.getMessage();
	}
	catch (Exception e)
	{
	    return e.getMessage();
	}
    }

    /**
     * 向数据库添加站点
     * 
     * @param ftpSite
     *                站点信息
     * @param timeout
     *                超时
     * @return 正确添加返回null，否则返回错误描述
     */
    public static String addFtpSite(FtpSiteInfo ftpSite)
    {
	if (testConnection(ftpSite))
	{
	    return addFtpSiteWithoutCheck(ftpSite);
	}
	else
	    return "Unreachable";

    }

    /**
     * 删除站点
     * 
     * @param ftpSite
     *                站点信息
     * @return 正确删除返回null，否则返回错误描述
     */
    public static String deleteFtpSite(FtpSiteInfo ftpSite)
    {
	try
	{
	    Criteria c = new Criteria();
	    c.add(FtpSiteInfo.ID, ftpSite.getId());
	    c.add(FtpSiteInfo.VERIFY, ftpSite.getVerify());
	    Scroller<FtpSiteInfo> scr = FtpSiteInfoPeer.doSelect(c);
	    if (!scr.hasNext())
	    {
		return "Verify incorrect";
	    }
	    else
	    {
		FtpSiteInfoPeer.doDelete(ftpSite);
	    }
	    return null;
	}
	catch (UnknownHostException e)
	{
	    return e.getMessage();
	}
	catch (Exception e)
	{
	    return e.getMessage();
	}
    }

    public static String updateFtpSiteWithoutCheck(FtpSiteInfo ftpSite)
    {
	try
	{
	    Criteria c = new Criteria();
	    c.add(FtpSiteInfo.ID, ftpSite.getId());
	    c.add(FtpSiteInfo.VERIFY, ftpSite.getVerify());
	    Scroller<FtpSiteInfo> scr = FtpSiteInfoPeer.doSelect(c);
	    if (!scr.hasNext())
	    {
		return "Verify incorrect";
	    }
	    else
	    {
		InetAddress add = InetAddress.getByName(ftpSite.getServer());
		if (IP_DOMAIN_MATCHER.match(add.getHostName()))
		    ftpSite.setServer(add.getHostName());
		else
		    ftpSite.setServer(add.getHostAddress());
		ftpSite.setAddress(add.getHostAddress());
		ftpSite.save();
	    }
	    return null;
	}
	catch (UnknownHostException e)
	{
	    e.printStackTrace();
	    return e.getMessage();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    return e.getMessage();
	}
    }

    /**
     * 更新站点信息
     * 
     * @param ftpSite
     *                站点信息
     * @return 正确更新返回null，否则返回错误描述
     */
    public static String updateFtpSite(FtpSiteInfo ftpSite)
    {
	if (testConnection(ftpSite))
	{
	    return updateFtpSiteWithoutCheck(ftpSite);
	}
	return "Unreachable";
    }

    /**
     * 将所有站点信息存入文本文件，此文件可以导入CuteFtp，格式如下：
     * 
     * <pre>
     * Site Label: label
     * Hostname: hostname
     * Port: port
     * Username: username
     * Password: password
     * Uses passive mode
     * </pre>
     * 
     * @param file
     *                要存入信息的文件
     * @throws Exception
     *                 异常
     */
    public static void storeFtpSitesToText(File file) throws Exception
    {
	BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	Criteria c = new Criteria();
	c.addAscendingOrder("server");
	Scroller<FtpSiteInfo> scr;
	scr = FtpSiteInfoPeer.doSelect(c);
	while (scr.hasNext())
	{
	    FtpSiteInfo ftpSite = (FtpSiteInfo) scr.next();
	    if (ftpSite.getTotalFileCount() < 0)
		continue;
	    writer.append("Site Label: " + ftpSite.getServer() + "\n");
	    writer.append("HostName: " + ftpSite.getServer() + "\n");
	    writer.append("Port: " + ftpSite.getPort() + "\n");
	    writer.append("Username: " + ftpSite.getUsername() + "\n");
	    writer.append("Password: " + ftpSite.getPassword() + "\n");
	    writer.append("Uses passive mode\n");
	    writer.append("\n\n");
	}
	writer.close();
    }

    /**
     * 将所有站点信息存入XML文件，此文件可以导入FlashFxp
     * 
     * @param file
     *                存入信息的文件
     * @throws Exception
     *                 异常
     */
    public static void storeFtpSitesToXML(File file) throws Exception
    {

	Criteria c = new Criteria();
	c.addAscendingOrder("server");
	Scroller<FtpSiteInfo> scr;
	scr = FtpSiteInfoPeer.doSelect(c);

	storeFtpSitesToXML(scr, file);
    }

    public static void storeFtpSitesToXML(Iterator<FtpSiteInfo> ite, File out) throws Exception
    {
	Element sitesElement = new Element("sites");
	sitesElement.setAttribute("version", "1.0");
	sitesElement.setAttribute("time", new Date().toString());
	Document myDocument = new Document(sitesElement);
	while (ite.hasNext())
	{
	    FtpSiteInfo ftpSite = ite.next();
	    if (ftpSite.getTotalFileCount() < 0)
		continue;
	    Element site = new Element("site");
	    Element name = new Element("name");
	    name.addContent(ftpSite.getServer());
	    Element address = new Element("address");
	    address.addContent(ftpSite.getServer());
	    Element port = new Element("port");
	    port.addContent(ftpSite.getPort() + "");
	    Element username = new Element("username");
	    username.addContent(ftpSite.getUsername());
	    Element password = new Element("password");
	    password.addContent(ftpSite.getPassword());
	    site.addContent(name);
	    site.addContent(address);
	    site.addContent(port);
	    site.addContent(username);
	    site.addContent(password);
	    sitesElement.addContent(site);
	}

	XMLOutputter outputter = new XMLOutputter();
	outputter.setFormat(Format.getPrettyFormat());
	FileWriter writer;
	writer = new FileWriter(out);
	outputter.output(myDocument, writer);
	writer.close();
    }

    /**
     * 从XML文件导入站点到数据库
     * 
     * @param file
     *                XML文件
     * @param pool
     *                向调用者传送信息的Pool
     * @throws JDOMException
     *                 JDOM异常
     * @throws IOException
     *                 IO异常
     */
    public static void addFtpSitesFromXML(File file, Poolable<String> pool) throws JDOMException,
	    IOException
    {
	SAXBuilder builder = new SAXBuilder();
	Document doc = null;
	doc = builder.build(file);

	Element root = doc.getRootElement();
	List<?> sites = root.getChildren("site");
	int count = 0;
	for (Object e : sites)
	{
	    FtpSiteInfo ftpSite = new FtpSiteInfo();
	    String address = ((Element) e).getChildText("address");
	    String port = ((Element) e).getChildText("port");
	    String username = ((Element) e).getChildText("username").trim();
	    String password = ((Element) e).getChildText("password").trim();
	    ftpSite.setServer(address);
	    ftpSite.setPort(Integer.parseInt(port));
	    if (username != null && username.length() > 0)
		ftpSite.setUsername(username);
	    if (password != null && password.length() > 0)
		ftpSite.setPassword(password);
	    ftpSite.setVerify(address.replaceAll("\\.", ""));
	    String ret = addFtpSite(ftpSite);
	    if (ret == null)
	    {
		pool.pool("Connect " + ftpSite.getServer() + " succeed");
		count++;
	    }
	    else
	    {
		pool.pool(ftpSite.getServer() + " : " + ret);
	    }
	}
	pool.pool(count + " ftp sites are added");
    }

    public static int addSitesFromDat(File in)
    {
	try
	{
	    BufferedReader reader = new BufferedReader(new FileReader(in));
	    String line = null;
	    String disciption = "";
	    String server = "";
	    String port = "21";
	    String user = "anonymous";
	    String passwd = "anonymous";
	    while ((line = reader.readLine()) != null)
	    {
		line = line.trim();
		if (line.length() == 0)
		{
		    FtpSiteInfo ftpSite = new FtpSiteInfo();
		    ftpSite.setDescription(disciption);
		    ftpSite.setContact(disciption);
		    ftpSite.setServer(server);
		    ftpSite.setAddress(server);
		    ftpSite.setPort(Integer.parseInt(port));
		    ftpSite.setUsername(user);
		    ftpSite.setPassword(passwd);
		    ftpSite.setVerify(server.replaceAll("\\.", ""));
		    String ret = addFtpSiteWithoutCheck(ftpSite);
		    if (ret == null)
		    {
			System.out.println("Add " + ftpSite.getServer() + " succeed");
		    }
		    else
		    {
			System.out.println(ftpSite.getServer() + " : " + ret);
		    }
		    // System.out.println(server + "," + port + "," + user + ","
		    // + passwd+ "," + disciption);
		    continue;
		}
		if (line.startsWith("["))
		    disciption = line.substring(1, line.length() - 1);
		if (line.startsWith("IP="))
		    server = line.substring(3);
		if (line.startsWith("port="))
		    port = line.substring(5);
		if (line.startsWith("user="))
		{
		    user = line.substring(5);
		    passwd = user;
		}
	    }

	}
	catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return 0;
    }

    public static int convertThephyFtpSitesToXML(File in, File out)
    {
	try
	{
	    List<FtpSiteInfo> list = new LinkedList<FtpSiteInfo>();
	    BufferedReader reader = new BufferedReader(new FileReader(in));
	    String line = null;
	    while ((line = reader.readLine()) != null)
	    {
		line = line.trim();
		if (line.length() == 0)
		    continue;
		if (line.startsWith("#"))
		{
		    continue;
		    // line = line.substring(1).trim();
		}
		StringTokenizer tokenizer = new StringTokenizer(line, ",");
		String server = null, admin = null, user = "anonymous", psw = "", port = "21";
		if (tokenizer.hasMoreTokens())
		    server = tokenizer.nextToken().trim();
		if (tokenizer.hasMoreTokens())
		    admin = tokenizer.nextToken().trim();
		if (tokenizer.hasMoreTokens())
		{
		    user = tokenizer.nextToken().trim();
		    if (user.length() == 0)
			user = "anonymous";
		}
		if (tokenizer.hasMoreTokens())
		    psw = tokenizer.nextToken().trim();
		if (tokenizer.hasMoreTokens())
		    port = tokenizer.nextToken().trim();
		FtpSiteInfo ftpSite = new FtpSiteInfo();
		ftpSite.setServer(server);
		ftpSite.setAdmin(admin);
		ftpSite.setUsername(user);
		ftpSite.setPassword(psw);
		ftpSite.setPort(Integer.parseInt(port));
		list.add(ftpSite);
	    }
	    storeFtpSitesToXML(list.iterator(), out);
	    return list.size();
	}
	catch (FileNotFoundException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return 0;
    }

    /**
     * 测试函数
     * 
     * @param args
     *                参数
     */
    public static void main(String[] args)
    {
	// FtpSiteInfo ftpSite = new FtpSiteInfo();
	// ftpSite.setServer("sofree.9966.org");
	// ftpSite.setUsername("free");
	// ftpSite.setPassword("free");
	// boolean ret = testConnection(ftpSite);
	// System.out.println("ret:" + ret);
	// System.out.println(FtpSitesManager.convertThephyFtpSitesToXML(new
	// File(
	// "res/ThephyFtpSites.txt"), new File("res/ThephyFtpSites.xml")));
	FtpSitesManager.addSitesFromDat(new File("Sites.dat"));
	// System.out.println(FtpSitesManager.TotalFileCount());
    }
}
