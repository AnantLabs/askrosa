package analysis;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharReader;
import org.apache.lucene.analysis.CharStream;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

import resource.CrawlerSetting;
import solr.CharFilterFactory;

public class RosaAnalyzer extends Analyzer
{

    
    public static final String STEMMER = "English";
    
    public static final Set<Object> ROSA_STOP_SET;
    
    /**
     * An array containing some common English words that are usually not useful
     * for searching.
     */
    protected Set<Object> stopSet;
    static
    {
        //stopSet construction
	String stopwords = CrawlerSetting.getProperty("analyzer.ftp.stopwords");
	String[] splits = stopwords.split(";");
	String[] stopWords = new String[splits.length + StopAnalyzer.ENGLISH_STOP_WORDS_SET.size()];
	for (int i = 0; i < splits.length; i++)
	    stopWords[i] = splits[i];
	Iterator<?> iterator = StopAnalyzer.ENGLISH_STOP_WORDS_SET.iterator();
	for (int i = 0; iterator.hasNext(); i++)
	{
	    stopWords[i + splits.length] = (String) iterator.next();
	}
	ROSA_STOP_SET = StopFilter.makeStopSet(stopWords);
    }
    
    /** Default maximum allowed token length */
    public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

    protected int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;
    
    protected CharFilterFactory rosaCharFilterFactory = new RosaCharFilterFactory();

    
    public RosaAnalyzer()
    {
	this.stopSet = ROSA_STOP_SET;
    }
    
    public RosaAnalyzer(Set<Object> stopSet)
    {
	this.stopSet = stopSet;
    }
    
    
    public int getMaxTokenLength()
    {
        return maxTokenLength;
    }


    public void setMaxTokenLength(int maxTokenLength)
    {
        this.maxTokenLength = maxTokenLength;
    }


    public TokenFilter getStopFilter(TokenStream in)
    {
	return new StopFilter(true,in, stopSet);
	//return new RosaStopFilter(in, ROSA_STOP_SET);
    }

    
    static final class SavedStreams
    {
	StandardTokenizer tokenStream;

	TokenStream filteredTokenStream;
    }
    
    @Override
    public TokenStream tokenStream(String fieldName, Reader reader)
    {
	CharStream cs = rosaCharFilterFactory.create(CharReader.get(reader));
	StandardTokenizer tokenStream = new StandardTokenizer(Version.LUCENE_30, cs);
	tokenStream.setMaxTokenLength(maxTokenLength);
	TokenStream result = new StandardFilter(tokenStream);
	result = getStopFilter(result);
	result = new SnowballFilter(result, STEMMER);
	return result;
    }
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException
    {
	SavedStreams streams = (SavedStreams) getPreviousTokenStream();
	if (streams == null)
	{
	    streams = new SavedStreams();
	    setPreviousTokenStream(streams);
	    CharStream cs = rosaCharFilterFactory.create(CharReader.get(reader));
	    streams.tokenStream = new StandardTokenizer(Version.LUCENE_30, cs);
	    streams.filteredTokenStream = new StandardFilter(streams.tokenStream);
	    streams.filteredTokenStream = getStopFilter(streams.filteredTokenStream);
	    streams.filteredTokenStream = new SnowballFilter(streams.filteredTokenStream, STEMMER);
	}
	else
	{
	    CharStream cs = rosaCharFilterFactory.create(CharReader.get(reader));
	    streams.tokenStream.reset(cs);
	}
	streams.tokenStream.setMaxTokenLength(maxTokenLength);


	return streams.filteredTokenStream;
    }
    public static void main(String[] args)
    {
	Analyzer ana = new RosaAnalyzer();
	String test2 = "中影论坛 www.bbsmovie.com [王贵与安娜]01 season07 c++c++c#";
	StringReader reader = new StringReader(test2);
	TokenStream ts = ana.tokenStream("name", reader);
	try
	{
	    while (ts.incrementToken())
	    {
		TermAttribute termAtt = (TermAttribute) ts.getAttribute(TermAttribute.class);
		OffsetAttribute offsetAtt = (OffsetAttribute)ts.getAttribute(OffsetAttribute.class);
		System.out.println("("+offsetAtt.startOffset()+","+offsetAtt.endOffset()+")[" + termAtt.term() + "]");
	    }
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }
}
