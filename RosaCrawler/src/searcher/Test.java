package searcher;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Map;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

public class Test
{

    /**
     * @param args
     * @throws NotBoundException 
     * @throws ParseException 
     * @throws IOException 
     * @throws InvalidTokenOffsetsException 
     */
    public static void main(String[] args) throws IOException, ParseException, NotBoundException, InvalidTokenOffsetsException
    {
	Map<String,String> map = FtpSearch.getRecentModifiedResource(new String[]{"video"}, 10, 20);
	for(Map.Entry<String, String> entry:map.entrySet())
	{
	    System.out.println(entry.getKey()+" ---> "+entry.getValue());
	}
    }

}
