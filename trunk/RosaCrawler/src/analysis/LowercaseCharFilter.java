package analysis;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.BaseCharFilter;
import org.apache.lucene.analysis.CharReader;
import org.apache.lucene.analysis.CharStream;


public class LowercaseCharFilter extends BaseCharFilter
{

    public LowercaseCharFilter(CharStream in)
    {
	super(in);
    }
    
    public LowercaseCharFilter(Reader in)
    {
	super(CharReader.get(in));
    }
    @Override
    public int read() throws IOException 
    {
	return Character.toLowerCase(input.read());
    }
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
	int ret = input.read(cbuf, off, len);
	if(ret!=-1)
	{
	    for(int i=off; i<off+ret; i++)
		cbuf[i] = Character.toLowerCase(cbuf[i]);
	}
	return ret;
    }
}
