package log;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class CrawlerLogger
{
    public static final Logger logger = Logger.getLogger(CrawlerLogger.class
	    .getName());
    static
    {
	   URL url = CrawlerLogger.class.getResource("/resource/crawlerlog4j.properties");
	   PropertyConfigurator.configure(url);
    }
    
    public static void main(String[] args)
    {
	System.out.println(logger.getAppender("gui"));
    }
}
