package analysis;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharReader;
import org.apache.lucene.analysis.CharStream;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter.Side;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

public class RosaAutoCompelerAnalyzer extends RosaAnalyzer
{
    public int minGram;
    public int maxGram;
    public RosaAutoCompelerAnalyzer()
    {
	this(1,10);
    }
    
    public RosaAutoCompelerAnalyzer(int minGram, int maxGram)
    {
	this.minGram = minGram;
	this.maxGram = maxGram;
    }
    @Override
    public TokenStream tokenStream(String fieldName, Reader reader)
    {
	CharStream cs = new RosaCharFilterFactory().create(CharReader.get(reader));
	TokenStream result = new StandardTokenizer(Version.LUCENE_CURRENT, cs);
	result = new StandardFilter(result);
	result = getStopFilter(result);
	result = new SnowballFilter(result, STEMMER);
	result = new EdgeNGramTokenFilter(result, Side.FRONT, minGram, maxGram);
	return result;
    }
    
    @Override
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
	    streams.filteredTokenStream = new EdgeNGramTokenFilter(streams.filteredTokenStream,
		    Side.FRONT, minGram, maxGram);
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
	Analyzer ana = new RosaAutoCompelerAnalyzer();
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
