package analysis;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

/**
 * 字母或数字Tokenizer
 * 
 * @author elegate
 */
public class LetterDigitBreakTokenizer extends Tokenizer
{
    private int offset = 0, bufferIndex = 0, dataLen = 0;

    private static final int MAX_WORD_LEN = 255;

    private static final int IO_BUFFER_SIZE = 4096;

    private final char[] ioBuffer = new char[IO_BUFFER_SIZE];

    public LetterDigitBreakTokenizer(Reader in)
    {
	super(in);
    }

    /**
     * Returns true iff a character should be included in a token. This
     * tokenizer generates as tokens adjacent sequences of characters which
     * satisfy this predicate. Characters for which this is false are used to
     * define token boundaries and are not included in tokens.
     */
    protected boolean isTokenChar(char c)
    {
	if( Character.isLetterOrDigit(c) )
	{
	    return true;
	}
	else
	{
	    pre = 0;
	    return false;
	}
    }
    
    private char pre = 0;
    
    private boolean isBreakPostion(char c)
    {
	boolean r = false;
	if(pre==0)
	{
	    r = false;
	}
	else if(Character.isDigit(c)&&Character.isDigit(pre) || Character.isLetter(c)&&Character.isLetter(pre))
	{
	    r = false;
	}
	else
	    r = true;
	pre = c;
	return r;
    }

    /**
     * Called on each token character to normalize it before it is added to the
     * token. The default implementation does nothing. Subclasses may use this
     * to, e.g., lowercase tokens.
     */
    protected char normalize(char c)
    {
	return c;
    }

    public final Token next(final Token reusableToken) throws IOException
    {
	assert reusableToken != null;
	reusableToken.clear();
	int length = 0;
	int start = bufferIndex;
	char[] buffer = reusableToken.termBuffer();
	while (true)
	{

	    if (bufferIndex >= dataLen)
	    {
		offset += dataLen;
		dataLen = input.read(ioBuffer);
		if (dataLen == -1)
		{
		    if (length > 0)
			break;
		    else
			return null;
		}
		bufferIndex = 0;
	    }

	    final char c = ioBuffer[bufferIndex++];

	    if (isTokenChar(c))
	    { // if it's a token char
		if(isBreakPostion(c))
		{
		    bufferIndex--;
		    break;
		}
		else
		{
		
    			if (length == 0) // start of token
        		    start = offset + bufferIndex - 1;
        		else if (length == buffer.length)
        		    buffer = reusableToken.resizeTermBuffer(1 + length);
        
        		buffer[length++] = normalize(c); // buffer it, normalized
        
        		if (length == MAX_WORD_LEN) // buffer overflow!
        		    break;
		}
	    }
	    else if (length > 0) // at non-Letter w/ chars
		break; // return 'em
	}

	reusableToken.setTermLength(length);
	reusableToken.setStartOffset(start);
	reusableToken.setEndOffset(start + length);
	return reusableToken;
    }
}
