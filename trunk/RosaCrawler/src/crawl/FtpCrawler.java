/**
 * 爬虫程序包
 */
package crawl;

import indexer.IndexBuilder;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;

import log.CrawlerLogger;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.FTPReply;

import resource.CategoryConfigure;
import resource.CrawlerSetting;
import utils.CommonConstants;
import utils.ElapseTimer;
import utils.Poolable;
import database.FtpSiteInfo;
import ftp.FTPFileInfo;

/**
 * 爬虫程序，负责爬单个站点，建议先参看Apache的FtpClient文档
 * 
 * @author elegate
 */
public class FtpCrawler
{
    /**
     * 分页获取结果
     */
    public static final int CRAWLER_PAGE_SIZE = 5000;
    
    
    public static final int FTP_CLIENT_BUFFER_SIZE=CrawlerSetting.getInt("ftp.client.buffer.size")*1024;
    /**
     * ls命令的参数
     */
    public static final String LIST_COMMAND = "-AlR";
    /**
     * 默认的超时时间，在connect之前调用
     */
    public static final int DEFAULT_TIMEOUT;
    /**
     * 超时时间，在connect之后调用
     */
    public static final int SO_TIMEOUT;

    static
    {
	DEFAULT_TIMEOUT = CommonConstants.MINUTE_IN_MILLISECOND
		* CrawlerSetting.getInt("ftp.ftpcrawler.defaulttimeout");
	SO_TIMEOUT = CommonConstants.MINUTE_IN_MILLISECOND
		* CrawlerSetting.getInt("ftp.ftpcrawler.sotimeout");
    }
    /**
     * 保存分类统计信息
     */
    private HashMap<String, Integer> categoryCount;

    /**
     * used to count the file crawled
     */
    private int count = 0;
    /**
     * 此类负责连接的Ftp站点信息
     */
    private FtpSiteInfo ftpSite;


    public FtpCrawler(FtpSiteInfo ftpSite)
    {
	this.ftpSite = ftpSite;
	categoryCount = new HashMap<String, Integer>();
	for (Object category : CategoryConfigure.getAllCategories())
	{
	    categoryCount.put((String) category, 0);
	}
    }

    /**
     * crawl the ftp to get all the files' information from the root directory
     * 
     * @param pool
     *                pool to store the indexed file
     * @return true if no error exists,otherwise false
     */
    public boolean crawl(Poolable<FTPFileInfo> pool)
    {

	return crawl(pool, null);

    }

    /**
     * crawl the ftp to get all the files' information under the specified directory 
     * 
     * @param pool
     *                pool to store the indexed file
     * @param directory
     *                the start directory
     * @return true if no error exists,otherwise false
     */
    public boolean crawl(Poolable<FTPFileInfo> pool, String directory)
    {
	// ----------- begin --------------------
	count = 0;
	for (String key : categoryCount.keySet())
	{
	    categoryCount.put(key, 0);
	}
	CrawlerLogger.logger.info("Start crawling ftp://" + ftpSite.getServer() + "...");
	// ---------------------------------------
	boolean ret = false;
	FTPClient ftp = new FTPClient();
	try
	{
	    FTPClientConfig conf = new FTPClientConfig(
		    FTPClientConfig.SYST_UNIX);
	    ftp.configure(conf);
	    ftp.setControlEncoding(ftpSite.getEncoding());
	    ftp.setDefaultTimeout(DEFAULT_TIMEOUT);
	    ftp.connect(ftpSite.getServer(), ftpSite.getPort());
	    ftp.setSoTimeout(SO_TIMEOUT);
	    int reply = ftp.getReplyCode();
	    if (!FTPReply.isPositiveCompletion(reply))
	    {
		ftp.disconnect();
		CrawlerLogger.logger.error("FTP server:" + ftpSite.getServer()
			+ " refused connection.");
		return false;
	    }
	    CrawlerLogger.logger.info(ftpSite.getServer() + " connected: "
		    + ftp.getReplyString());

	    if(!ftp.login(ftpSite.getUsername(), ftpSite.getPassword()))
	    {
		CrawlerLogger.logger.info("Login to ftp://" + ftpSite.getServer() + " failed:"
			    + ftp.getReplyString());
		return false;
	    }
	     ftp.enterLocalPassiveMode();
	    CrawlerLogger.logger.info("Login to ftp://" + ftpSite.getServer() + " ok:"
		    + ftp.getReplyString());
	    
	    ftp.setBufferSize(FTP_CLIENT_BUFFER_SIZE);
	    if (ftpSite.getDescription() == null
		    || ftpSite.getDescription().trim().length() == 0)
	    {
		ftpSite.setDescription(ftp.getReplyString().trim());
	    }
	    // crawStarted = true;
	    if (ftpSite.getRecursive() == FtpSiteInfo.LS_ALR_RECURSIVE)          //ls -lR supported
	    {
		CrawlerLogger.logger.info(ftpSite.getServer()+ " ls -lR supported,so send ls -lR command");
		ret = lsLRCrawl(ftp, pool, directory);
		if (ret)
		{
		    FTPFile[] files = null;
		    if (directory != null)
			files = ftp.listFiles(directory);
		    else
			files = ftp.listFiles();
		    if (this.count > 0 &&  files.length  >= this.count)
			ftpSite.setRecursive(FtpSiteInfo.MANUAL_RECURSIVE);
		}
	    }
	    else                 //ls -lR not supported
	    {
		CrawlerLogger.logger.info(ftpSite.getServer()+ " ls -lR not supported,enter recursive crawling");
		ftpSite.setCrawlInterval(IndexBuilder.MAX_CRAWLINTERVAL);
		ret = lsRecursiveCrawl(ftp, pool, directory);
	    }
	}
	catch (SocketException e)
	{
	    error(e);
	}
	catch (IOException e)
	{
	    error(e);
	}
	catch (Exception e)
	{
	    error(e);
	}
	finally
	{
	    if (ftp.isConnected())
	    {
		try
		{
		    ftp.disconnect();
		}
		catch (IOException f)
		{
		    // do nothing
		}
	    }
	}
	// ------------------- done ----------------------------
	CrawlerLogger.logger.info("Finish crawling ftp://" + ftpSite.getServer());
	CrawlerLogger.logger.info(count + " items are crawled");
	return ret;
    }
    /**
     * 返回文件总数
     * @return 文件总数
     */
    public int getCount()
    {
	return count;
    }
    /**
     * 返回文件分类统计
     * @param category 文件分类
     * @return 分类统计结果
     */
    public int getCategoryCount(String category)
    {
	return categoryCount.get(category);
    }

    /**
     * method to send ls -LR to crawl the ftp site to get file information, called by
     * {@link #crawl(Poolable, String) crawl}
     * 
     * @param ftp
     *                FTPClient object
     * @param pool
     *                pool used to store indexed file
     * @param dir
     *                current working directory
     * @return true if no error exists,otherwise false
     * @throws IOException
     */
    protected boolean lsLRCrawl(FTPClient ftp, Poolable<FTPFileInfo> pool,
	    String dir) throws IOException
    {

	String command = null;
	if (dir != null)
	{
	    command = LIST_COMMAND + " " + dir;
	}
	else
	{
	    command = LIST_COMMAND;
	}
	FTPListParseEngine engine = ftp.initiateListParsing(
		"parser.LSLRFtpEntryParser", command);
	while (engine.hasNext())
	{
	    FTPFile[] files = engine.getNext(CRAWLER_PAGE_SIZE);
	    for (FTPFile file : files)
	    {
		if (file == null)
		{
		    continue;
		}

		String name = file.getName();
		if (name.equals(".") || name.equals(".."))
		    continue;
		count++;
		FTPFileInfo info = new FTPFileInfo(file);
		poolFile(info, pool);
	    }
	}
	return true;
    }
    /**
     * 这个函数负责将获取的信息向调用者传送
     * @param fileInfo FTP文件信息
     * @param pool FTP文件信息池
     */
    private void poolFile(FTPFileInfo fileInfo, Poolable<FTPFileInfo> pool)
    {
	String category = fileInfo.getCategory();
	int cnt = categoryCount.get(category);
	categoryCount.put(category, cnt + 1);
	pool.pool(fileInfo);
    }
    /**
     * 递归爬虫
     * @param ftp ftp连接
     * @param pool 信息池
     * @param dir 当前路径
     * @return true
     * @throws IOException exception thrown
     */
    protected boolean lsRecursiveCrawl(FTPClient ftp,
	    Poolable<FTPFileInfo> pool, String dir) throws IOException
    {
	boolean ret = true;
	String command = null;
	String parent = null;
	if (dir != null)
	{
	    command = dir;
	    parent = dir + "/";
	}
	else
	{
	    command = "";
	    parent = "/";
	}

	FTPFile[] files = ftp.listFiles(command);
	for (FTPFile file : files)
	{
	    if (file == null)
	    {
		continue;
	    }
	    String name = file.getName();
	    if (name.equals(".") || name.equals(".."))
		continue;
	    count++;
	    FTPFileInfo info = new FTPFileInfo(file);
	    info.setPath(parent + file.getName());
	    poolFile(info, pool);
	    if (file.getType() == FTPFile.DIRECTORY_TYPE)
	    {
		ret &= lsRecursiveCrawl(ftp, pool, parent + file.getName());
	    }
	}
	return ret;
    }

    private void error(Exception e)
    {
	CrawlerLogger.logger.error(ftpSite.getServer()
		+ " , current crawling count:" + count, e);
    }


    /**
     * test main
     * 
     * @param args
     *                arguments
     */
    public static void main(String[] args)
    {
	CrawlerSetting.setProxy();
	ElapseTimer timer = new ElapseTimer();
	timer.begin();
	FtpSiteInfo ftpSite = new FtpSiteInfo();
	ftpSite.setServer("172.16.65.79");
	ftpSite.setUsername("test");
	ftpSite.setPassword("down");
	FtpCrawler ftp = new FtpCrawler(ftpSite);
	Poolable<FTPFileInfo> pool = new IndexTestPool("test",ftpSite);
	ftp.crawl(pool);
	pool.close();
	System.out.println("count:" + ftp.getCount());
	System.out.println("Description:" + ftpSite.getDescription());
	try
	{
	    timer.finish();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
}
