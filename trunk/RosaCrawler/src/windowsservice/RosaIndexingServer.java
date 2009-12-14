package windowsservice;

import indexer.IndexBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import log.CrawlerLogger;

import org.apache.commons.lang.time.StopWatch;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.RemoteSearchable;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import resource.CrawlerSetting;
import searcher.FtpSearch;
import searcher.SearchParameter;
import searcher.SearchResult;
import utils.Tools;
import webftp.WebFTPSearch;
import webftp.WebFTPSearchEngine;
import autocomplete.AutoCompleteRMI;
import crawlerutils.RosaCrawlerConstants;
import database.Criteria;
import database.Database;
import database.ResourceRequest;
import database.ResourceRequestPeer;
import database.Scroller;

public class RosaIndexingServer
{
    /**
     * logging system
     */

    private static final DateFormat DF = Tools.DEFAULT_LOCAL_DATE_FORMAT;

    private java.util.Timer indexTimer;
    
    //in hours
    private int interval;

    private Registry registry;
    private AutoCompleteRMI engine;

    public RosaIndexingServer()
    {
	try
	{
	    registry = LocateRegistry.createRegistry(CrawlerSetting
		    .getInt("node.port"));
	}
	catch (RemoteException e)
	{
	    e.printStackTrace();
	}
	interval = 24;
	try
	{
	    engine = new AutoCompleteRMI();
	}
	catch (RemoteException e)
	{
	    CrawlerLogger.logger.error("Failed to init FTPSpellChecker", e);
	}
    }

    public void startService()
    {
	startRMIServer();
	startIndex();
	startWebFTPSearchEngine();
	startAutoCompleteRMI();
    }

    private void startWebFTPSearchEngine()
    {
	try
	{
	    WebFTPSearch engine = new WebFTPSearchEngine();
	    registry.rebind(CrawlerSetting.getProperty("webftp.search.name"),
		    engine);
	    CrawlerLogger.logger.info("Web FTP search engine started,bind to "
		    + CrawlerSetting.getProperty("webftp.search.name"));
	}
	catch (RemoteException e)
	{
	    e.printStackTrace();
	}
    }
    
    private void startAutoCompleteRMI()
    {
	try
	{
	    if(engine!=null)
	    {
		registry.rebind(CrawlerSetting.getProperty("autocomplete.name"), engine);
		 CrawlerLogger.logger.info("FTP spell checker engine started,bind to "
			    + CrawlerSetting.getProperty("autocomplete.name"));
	    }
	}
	catch (RemoteException e)
	{
	    e.printStackTrace();
	}
    }

    protected void startIndex()
    {
	indexTimer = new Timer("Index Timer", true);
	Calendar c = Calendar.getInstance();
//	c.add(Calendar.DAY_OF_YEAR, 1);
	c.set(Calendar.HOUR_OF_DAY, 1);
	c.set(Calendar.MINUTE, 0);
	c.set(Calendar.SECOND, 0);
	indexTimer.scheduleAtFixedRate(new IndexTask(), c.getTime(), interval*RosaCrawlerConstants.ONE_HOUR_IN_MILLISECOND);
    }

    protected void stopRMIServer()
    {
	try
	{
	    registry.unbind(CrawlerSetting.getProperty("node.name"));
	    CrawlerLogger.logger.info("Server stoped\n");
	}
	catch (RemoteException e)
	{
	    e.printStackTrace();
	}
	catch (NotBoundException e)
	{
	    e.printStackTrace();
	}

    }

    protected void startRMIServer()
    {
	try
	{
	    String index = CrawlerSetting
	    .getProperty("index");
	    Directory dir = FSDirectory.open(new File(index));
	    if (IndexReader.indexExists(dir))
	    {
		Searcher searcher = new IndexSearcher(dir);
		RemoteSearchable remote = new RemoteSearchable(searcher);
		String bindName = CrawlerSetting.getProperty("node.name");
		registry
			.rebind(bindName, remote);
		CrawlerLogger.logger.info("Server started,bind to "
			+ CrawlerSetting.getProperty("node.name"));
	    }
	}
	catch (RemoteException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    private void checkResourceRequest()
    {
	try
	{
	    Date now = new Date();
	    CrawlerLogger.logger
		    .info("************* Start Check Resouce Request at:"
			    + DF.format(now) + " *************");
	    Criteria c = new Criteria();
	    c.add(ResourceRequest.STATE, ResourceRequest.NOT_ANSWERED);
	    Scroller<ResourceRequest> requests = ResourceRequestPeer
		    .doSelect(c);
	    while (requests.hasNext())
	    {
		try
		{
		    ResourceRequest r = requests.next();
		    SearchParameter parameter = new SearchParameter();
		    parameter.setBegin(0);
		    parameter.setCount(1);
		    parameter.setKeyword(r.getResourcename());
		    parameter.setField("name");
		    SearchResult result = FtpSearch.search(parameter,false);
		    if (result.getHistNum() > 0)
		    {
			String host = "smtp.gmail.com";
			String from = "askrosateam@gmail.com";
			String psw = "welovefreedom";
			String to = r.getEmail();
			String fromname = "AskRosa Team";
			String subject = "AskRosa消息通知";
			String msg = "尊敬的用户，您所请求的资源 \""
				+ r.getResourcename()
				+ "\" 已经出现在搜索结果中，下面是对应的链接地址："
				+ "http://askrosa.cn/search.do?keyword="+URLEncoder.encode(r.getResourcename(), "utf-8")
				+ "\n\n\n\n AskRosaTeam敬上";

			Tools.sendEmail(host, true, "465", from, psw, fromname,
				to, subject, msg);
			CrawlerLogger.logger.info("Answer " + r.getEmail()
				+ " for resource: " + r.getResourcename()
				+ " at " + DF.format(new Date()));
			r.setState(ResourceRequest.ANSWERED);
			r.save();
		    }
		}
		catch (Exception e)
		{
		    CrawlerLogger.logger
			    .error("Failed to send answer email", e);
		}

	    }
	    now = new Date();
	    CrawlerLogger.logger
		    .info("************* Resouce Request Check Finished at:"
			    + DF.format(now) + " *************");
	}
	catch (Exception e)
	{
	    CrawlerLogger.logger.error("Failed to answer resource request", e);
	}

    }

    private class IndexTask extends TimerTask
    {
	private IndexBuilder builder;

	public IndexTask()
	{
	    builder = new IndexBuilder();
	}

	public void run()
	{
	    StopWatch stop = new StopWatch();
	    stop.start();
	    CrawlerLogger.logger.info("********** Start time:"
		    + DF.format(new Date()) + " **********");
	    //rebuild dictionary
	    try
		{
		    engine.buildDictionary();
		    startAutoCompleteRMI();
		}
		catch (Exception e)
		{
		   CrawlerLogger.logger.error("Failed to build dictionary", e);
		}
		
	    //rebuild ftp content index
	    int updateNumOfSite = builder.buildIndex();
	    stop.stop();
	    CrawlerLogger.logger.info("********** Finish time:"
		    + DF.format(new Date()) + ", time used:"+stop);

	    if (updateNumOfSite > 0)
	    {
		CrawlerLogger.logger.info("********** Updated "
			+ updateNumOfSite + " sites **********");
		CrawlerLogger.logger
			.info("********** Restart RMI Server **********");
		startRMIServer();
		checkResourceRequest();
	    }
	    Database.shutdown();
	}

    }

    public static void main(String[] args)
    {
	CrawlerLogger.logger.info("TotalMemory:"
		+ Runtime.getRuntime().maxMemory() / RosaCrawlerConstants.MB
		+ " M");
	CrawlerSetting.setProxy();
	RosaIndexingServer server = new RosaIndexingServer();
	server.startService();
	CrawlerLogger.logger.info("Rosa indexing server started");
	try
	{
	    System.in.read();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }
}
