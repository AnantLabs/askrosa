package autocomplete;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.time.StopWatch;
import org.apache.lucene.queryParser.ParseException;

import searcher.FtpSearch;
import searcher.SearchParameter;
import searcher.SearchResult;
import database.Criteria;
import database.QueryStatistics;
import database.QueryStatisticsPeer;
import database.Scroller;

public class FixQueryStatistics
{
    private static int count = 0;
    private static final ReentrantLock mainLock = new ReentrantLock();
    public static void main(String[] args) throws Exception
    {
	ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
	Criteria c = new Criteria();
//	c.add(QueryStatistics.HITS, 0, Criteria.EQUAL);
//	c.addDescendingOrder(QueryStatistics.ID);
	StopWatch stop = new StopWatch();
	stop.start();
	Scroller<QueryStatistics> scr = QueryStatisticsPeer.doSelect(c);
	while (scr.hasNext())
	{
	    QueryStatistics qs = scr.next();
	    pool.execute(new FixQueryStatisticsTask(qs));
	}
	System.out.println("Task added finished, waiting for complete");
	pool.shutdown();
	pool.awaitTermination(10, TimeUnit.HOURS);
	stop.stop();
	System.out.println("Done, time used:"+stop);
    }

    static class FixQueryStatisticsTask implements Runnable
    {
	private QueryStatistics qs;

	public FixQueryStatisticsTask(QueryStatistics qs)
	{
	    this.qs = qs;
	}

	@Override
	public void run()
	{
	    SearchParameter parameter = new SearchParameter();
	    parameter.setCount(1);
	    parameter.setKeyword(qs.getKeyword());
	    SearchResult r;
	    try
	    {
		r = FtpSearch.search(parameter, false, false);
		System.out.println(qs.getId()+": "+qs.getKeyword() + " " + r.getHistNum());
		qs.setHits(r.getHistNum());
		qs.save();
		mainLock.lock();
		count++;
		mainLock.unlock();
		if(count%10000==0)
		{
		    System.out.println("************* Current fix count:"+count++);
		}
		qs = null;
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
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }

	}
    }
}
