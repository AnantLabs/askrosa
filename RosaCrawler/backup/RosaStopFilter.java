package analysis;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class RosaStopFilter extends TokenFilter
{
    private static boolean ENABLE_POSITION_INCREMENTS_DEFAULT = false;

    private final CharArraySet stopWords;

    private boolean enablePositionIncrements = ENABLE_POSITION_INCREMENTS_DEFAULT;

    private TermAttribute termAtt;

//    private OffsetAttribute offsetAtt;

    private PositionIncrementAttribute positionIncrementAtt;

    /**
     * An array containing some common English words that are usually not useful
     * for searching.
     */

    /**
     * Construct a token stream filtering the given input.
     */
    public RosaStopFilter(TokenStream input, String[] stopWords)
    {
	this(input, stopWords, false);
    }

    /**
     * Constructs a filter which removes words from the input TokenStream that
     * are named in the array of words.
     */
    public RosaStopFilter(TokenStream in, String[] stopWords, boolean ignoreCase)
    {
	super(in);
	this.stopWords = (CharArraySet) makeStopSet(stopWords, ignoreCase);
	termAtt = addAttribute(TermAttribute.class);
	positionIncrementAtt = addAttribute(PositionIncrementAttribute.class);
    }

    /**
     * Construct a token stream filtering the given input. If
     * <code>stopWords</code> is an instance of {@link CharArraySet} (true if
     * <code>makeStopSet()</code> was used to construct the set) it will be
     * directly used and <code>ignoreCase</code> will be ignored since
     * <code>CharArraySet</code> directly controls case sensitivity.
     * <p/>
     * If <code>stopWords</code> is not an instance of {@link CharArraySet}, a
     * new CharArraySet will be constructed and <code>ignoreCase</code> will be
     * used to specify the case sensitivity of that set.
     * 
     * @param input
     * @param stopWords
     *            The set of Stop Words.
     * @param ignoreCase
     *            -Ignore case when stopping.
     */
    public RosaStopFilter(TokenStream input, Set stopWords, boolean ignoreCase)
    {
	super(input);
	if (stopWords instanceof CharArraySet)
	{
	    this.stopWords = (CharArraySet) stopWords;
	}
	else
	{
	    this.stopWords = new CharArraySet(stopWords.size(), ignoreCase);
	    this.stopWords.addAll(stopWords);
	}
	termAtt = addAttribute(TermAttribute.class);
//	offsetAtt = addAttribute(OffsetAttribute.class);
	positionIncrementAtt = addAttribute(PositionIncrementAttribute.class);
    }

    /**
     * Constructs a filter which removes words from the input TokenStream that
     * are named in the Set.
     * 
     * @see #makeStopSet(java.lang.String[])
     */
    public RosaStopFilter(TokenStream in, Set stopWords)
    {
	this(in, stopWords, false);
    }

    /**
     * Builds a Set from an array of stop words, appropriate for passing into
     * the StopFilter constructor. This permits this stopWords construction to
     * be cached once when an Analyzer is constructed.
     * 
     * @see #makeStopSet(java.lang.String[], boolean) passing false to
     *      ignoreCase
     */
    public static final Set makeStopSet(String[] stopWords)
    {
	return makeStopSet(stopWords, false);
    }

    /**
     * @param stopWords
     * @param ignoreCase
     *            If true, all words are lower cased first.
     * @return a Set containing the words
     */
    public static final Set makeStopSet(String[] stopWords, boolean ignoreCase)
    {
	CharArraySet stopSet = new CharArraySet(stopWords.length, ignoreCase);
	stopSet.addAll(Arrays.asList(stopWords));
	return stopSet;
    }

    public boolean isMeaningless(String term)
    {
//	 char ch = term.charAt(0);
//	 if(term.length()==1&& (ch>=65 && ch<=122))
//	 {
//	     return true;
//	 }
	return false;
    }

    /**
     * Returns the next input Token whose term() is not a stop word.
     */
//    public final Token next(final Token reusableToken) throws IOException
//    {
//	assert reusableToken != null;
//	// return the first non-stop word found
//	int skippedPositions = 0;
//	for (Token nextToken = input.next(reusableToken); nextToken != null; nextToken = input
//		.next(reusableToken))
//	{
//	    if (!stopWords.contains(nextToken.termBuffer(), 0, nextToken.termLength())
//		    && !isMeaningless(nextToken.term()))
//	    {
//		if (enablePositionIncrements)
//		{
//		    nextToken.setPositionIncrement(nextToken.getPositionIncrement()
//			    + skippedPositions);
//		}
//		return nextToken;
//	    }
//	    skippedPositions += nextToken.getPositionIncrement();
//	}
//	// reached EOS -- return null
//	return null;
//    }

    /**
     * @see #setEnablePositionIncrementsDefault(boolean).
     */
    public static boolean getEnablePositionIncrementsDefault()
    {
	return ENABLE_POSITION_INCREMENTS_DEFAULT;
    }

    /**
     * Set the default position increments behavior of every StopFilter created
     * from now on.
     * <p>
     * Note: behavior of a single StopFilter instance can be modified with
     * {@link #setEnablePositionIncrements(boolean)}. This static method allows
     * control over behavior of classes using StopFilters internally, for
     * example {@link org.apache.lucene.analysis.standard.StandardAnalyzer
     * StandardAnalyzer}.
     * <p>
     * Default : false.
     * 
     * @see #setEnablePositionIncrements(boolean).
     */
    public static void setEnablePositionIncrementsDefault(boolean defaultValue)
    {
	ENABLE_POSITION_INCREMENTS_DEFAULT = defaultValue;
    }

    /**
     * @see #setEnablePositionIncrements(boolean).
     */
    public boolean getEnablePositionIncrements()
    {
	return enablePositionIncrements;
    }

    /**
     * Set to <code>true</code> to make <b>this</b> StopFilter enable position
     * increments to result tokens.
     * <p>
     * When set, when a token is stopped (omitted), the position increment of
     * the following token is incremented.
     * <p>
     * Default: see {@link #setEnablePositionIncrementsDefault(boolean)}.
     */
    public void setEnablePositionIncrements(boolean enable)
    {
	this.enablePositionIncrements = enable;
    }

    @Override
    public boolean incrementToken() throws IOException
    {
	// return the first non-stop word found
	int skippedPositions = 0;
	while(input.incrementToken())
	{
	    if(!stopWords.contains(termAtt.termBuffer(), 0, termAtt.termLength())&&!isMeaningless(termAtt.term()))
	    {
		if(enablePositionIncrements)
		{
		   positionIncrementAtt.setPositionIncrement(positionIncrementAtt.getPositionIncrement()+skippedPositions);
		}
		return true;
	    }
	    skippedPositions += positionIncrementAtt.getPositionIncrement();
	}
	// reached EOS -- return null
	return false;
    }
}
