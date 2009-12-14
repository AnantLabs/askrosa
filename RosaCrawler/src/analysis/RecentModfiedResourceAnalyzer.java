package analysis;

import java.util.Iterator;
import java.util.Set;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import resource.CrawlerSetting;

public class RecentModfiedResourceAnalyzer extends RosaAnalyzer
{
    public static final Set<Object> RECENT_MODIFIED_RESOURCE_STOP_SET;

    static
    {
	String stopwords = CrawlerSetting
		.getProperty("analyzer.ftp.recent.modified.resource.stopwords");
	String[] splits = stopwords.split(";");
	String[] STOP = new String[splits.length + StopAnalyzer.ENGLISH_STOP_WORDS_SET.size()];
	for (int i = 0; i < splits.length; i++)
	    STOP[i] = splits[i];
	Iterator<?> iterator = StopAnalyzer.ENGLISH_STOP_WORDS_SET.iterator();
	for (int i = 0; iterator.hasNext(); i++)
	{
	    STOP[i + splits.length] = (String) iterator.next();
	}
	RECENT_MODIFIED_RESOURCE_STOP_SET = StopFilter.makeStopSet(STOP);
    }

    public RecentModfiedResourceAnalyzer()
    {
	super(RECENT_MODIFIED_RESOURCE_STOP_SET);
    }

    // public TokenStream tokenStream(String fieldName, Reader reader)
    // {
    // StandardTokenizer tokenStream = new
    // StandardTokenizer(Version.LUCENE_30,reader);
    // tokenStream.setMaxTokenLength(maxTokenLength);
    // TokenStream result = new StandardFilter(tokenStream);
    // result = new LowerCaseFilter(result);
    // result = getStopFilter(result);
    // return result;
    // }
    //    
    // public TokenStream reusableTokenStream(String fieldName, Reader reader)
    // throws IOException {
    // SavedStreams streams = (SavedStreams) getPreviousTokenStream();
    // if (streams == null) {
    // streams = new SavedStreams();
    // setPreviousTokenStream(streams);
    // streams.tokenStream = new StandardTokenizer(Version.LUCENE_30,reader);
    // streams.filteredTokenStream = new StandardFilter(streams.tokenStream);
    // streams.filteredTokenStream = new
    // LowerCaseFilter(streams.filteredTokenStream);
    // streams.filteredTokenStream = getStopFilter(streams.filteredTokenStream);
    // }
    // else
    // {
    // streams.tokenStream.reset(reader);
    // }
    // streams.tokenStream.setMaxTokenLength(maxTokenLength);
    //        
    //
    // return streams.filteredTokenStream;
    // }

//    public TokenFilter getStopFilter(TokenStream in)
//    {
//	return new RosaStopFilter(in, stopSet)
//	{
//	    public boolean isMeaningless(String term)
//	    {
//		boolean b = super.isMeaningless(term);
//		if (b)
//		    return true;
//		else
//		{
//		    try
//		    {
//			Integer.valueOf(term);
//			return true;
//		    }
//		    catch (NumberFormatException e)
//		    {
//			return false;
//		    }
//		}
//	    }
//	};
//    }
}
