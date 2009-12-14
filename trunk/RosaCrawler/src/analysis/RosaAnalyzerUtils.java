package analysis;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class RosaAnalyzerUtils
{
    public static List<String> getTokens(Analyzer analyzer, String input) throws IOException
    {
	StringReader reader = new StringReader(input);
	TokenStream ts = analyzer.reusableTokenStream("path", reader);
	List<String> ret = new LinkedList<String>();
	try
	{
	    while (ts.incrementToken())
	    {
		TermAttribute term = (TermAttribute) ts.getAttribute(TermAttribute.class);
		ret.add(term.term());
	    }
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	return ret;
    }

    public static String getAnalyzedString(Analyzer analyzer, String input) throws IOException
    {
	final String CJ_TYPE = "<CJ>";
	StringReader reader = new StringReader(input);
	TokenStream ts = analyzer.reusableTokenStream("path", reader);
	String ret = "";
	String previousType = null;
	try
	{
	    while (ts.incrementToken())
	    {
		TermAttribute term = (TermAttribute) ts.getAttribute(TermAttribute.class);
		TypeAttribute type = (TypeAttribute) ts.getAttribute(TypeAttribute.class);
		if (type.type().equals(CJ_TYPE) && CJ_TYPE.equals(previousType))
		    ret += term.term();
		else if (previousType != null)
		    ret += " " + term.term();
		else 
		    ret += term.term();
		previousType = type.type();

	    }
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	return ret;
    }

    public static void main(String[] args) throws IOException
    {
	Analyzer analyzer = new RosaAnalyzer();
	String test = "c++c++season07.dvdrip";
	List<String> str = RosaAnalyzerUtils.getTokens(analyzer, test);
	System.out.println(str);
    }
}
