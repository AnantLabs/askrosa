package finder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import resource.CrawlerSetting;

import utils.Poolable;

/**
 * 多线程方式添加FTP
 * @author elegate
 *
 */
public class FtpSitesFinderTask
{
    private ThreadPoolExecutor pool;

    private Poolable<String> msgPool;

    public FtpSitesFinderTask(Poolable<String> msgPool)
    {
	this.msgPool = msgPool;
    }

    public void findSites(File xml)
    {
	pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
	SAXBuilder builder = new SAXBuilder();
	Document doc = null;
	try
	{
	    doc = builder.build(xml);

	    Element root = doc.getRootElement();
	    List<?> sites = root.getChildren("segment");
	    for (Object e : sites)
	    {
		String from = ((Element) e).getChildText("from");
		String to = ((Element) e).getChildText("to");
		msgPool.pool("Find ftp sites between " + from + " and " + to);
		pool.execute(new SegmentFinderTask(from, to));
	    }
	}
	catch (JDOMException e1)
	{
	    e1.printStackTrace();
	}
	catch (IOException e1)
	{
	    e1.printStackTrace();
	}
	await();
    }

    private void await()
    {
	pool.shutdown();
	try
	{
	    pool.awaitTermination(CrawlerSetting
		    .getInt("finder.ftpsitesfindertask.timeout"),
		    TimeUnit.HOURS);
	}
	catch (InterruptedException e)
	{
	    e.printStackTrace();
	}
    }

    class SegmentFinderTask extends Thread
    {
	private String from;

	private String to;

	public SegmentFinderTask(String from, String to)
	{
	    this.from = from;
	    this.to = to;
	}

	public void run()
	{
	    FtpSitesFinder.searchFtpSitesBetween(from, to, msgPool);
	}
    }

}
