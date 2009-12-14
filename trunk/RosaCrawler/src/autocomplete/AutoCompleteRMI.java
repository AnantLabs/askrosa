package autocomplete;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.StopWatch;

import log.CrawlerLogger;
import resource.CrawlerSetting;

public class AutoCompleteRMI extends UnicastRemoteObject implements AutoCompleteRemote
{
    /**
     * 
     */
    private static final long serialVersionUID = 3764793157579575883L;
    private AutoCompleter autoCompleter =null;
    public AutoCompleteRMI() throws RemoteException
    {
	super();
	try
	{
	    autoCompleter = new AutoCompleter(CrawlerSetting.getProperty("dictionaryIndex"));
	}
	catch (Exception e)
	{
	   CrawlerLogger.logger.error("failed to init autoCompleter", e);
	}
    }
    
    public void buildDictionary() throws Exception
    {
	StopWatch stop = new StopWatch();
	stop.start();
	CrawlerLogger.logger.info("Start building dictionary...");
	autoCompleter.reIndex();
	stop.stop();
	CrawlerLogger.logger.info("Building dictionary finished, time used:"+stop.toString());
    }
    
    @Override
    public Map<String,Integer> suggestSimilar(String word) throws RemoteException
    {
	try
	{
	    return autoCompleter.suggestTermsFor(word);
	}
	catch (Exception e)
	{
	   return new HashMap<String,Integer>();
	}
    }

    
}
