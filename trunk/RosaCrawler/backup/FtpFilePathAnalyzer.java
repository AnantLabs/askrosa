package analysis;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharFilter;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.MappingCharFilter;
import org.apache.lucene.analysis.NormalizeCharMap;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.Version;

import resource.CrawlerSetting;
import standard.StandardFilter;
import standard.StandardTokenizer;

/**
 * Filters {@link StandardTokenizer} with {@link StandardFilter},
 * {@link LowerCaseFilter} and {@link StopFilter}, using a list of English stop
 * words.
 * 
 * @version $Id: FtpFilePathAnalyzer.java,v 1.8 2009-01-09 09:27:24 rl Exp $
 */
public class FtpFilePathAnalyzer extends Analyzer
{
    /**
     * Add by Weiwei Wang to correct c++ like input
     */
    public static final NormalizeCharMap RECOVERY_MAP;
    static
    {
	RECOVERY_MAP = new NormalizeCharMap();
	String recoveryWords = CrawlerSetting.getProperty("analyzer.ftp.recovery");
        String[] splits = recoveryWords.split(";");
        for(int i=0;i<splits.length;i+=2)
        {
            RECOVERY_MAP.add(splits[i], splits[i+1]);
        }
    }
    
    @SuppressWarnings("unchecked")
    protected Set stopSet;

    protected static final String STEMMER = "English";

    /**
     * Specifies whether deprecated acronyms should be replaced with HOST type.
     * This is false by default to support backward compatibility.
     * 
     * @deprecated this should be removed in the next release (3.0). See
     *             https://issues.apache.org/jira/browse/LUCENE-1068
     */
    protected boolean replaceInvalidAcronym = defaultReplaceInvalidAcronym;

    private static boolean defaultReplaceInvalidAcronym;

    // Default to true (fixed the bug), unless the system prop is set
    static
    {
	final String v = System
		.getProperty("org.apache.lucene.analysis.standard.StandardAnalyzer.replaceInvalidAcronym");
	if (v == null || v.equals("true"))
	    defaultReplaceInvalidAcronym = true;
	else
	    defaultReplaceInvalidAcronym = false;
    }

    /**
     * @return true if new instances of StandardTokenizer will replace
     *         mischaracterized acronyms See
     *         https://issues.apache.org/jira/browse/LUCENE-1068
     * @deprecated This will be removed (hardwired to true) in 3.0
     */
    public static boolean getDefaultReplaceInvalidAcronym()
    {
	return defaultReplaceInvalidAcronym;
    }

    /**
     * @param replaceInvalidAcronym
     *            Set to true to have new instances of StandardTokenizer replace
     *            mischaracterized acronyms by default. Set to false to preseve
     *            the previous (before 2.4) buggy behavior. Alternatively, set
     *            the system property
     *            org.apache.lucene.analysis.standard.StandardAnalyzer
     *            .replaceInvalidAcronym to false. See
     *            https://issues.apache.org/jira/browse/LUCENE-1068
     * @deprecated This will be removed (hardwired to true) in 3.0
     */
    public static void setDefaultReplaceInvalidAcronym(boolean replaceInvalidAcronym)
    {
	defaultReplaceInvalidAcronym = replaceInvalidAcronym;
    }

    /**
     * An array containing some common English words that are usually not useful
     * for searching.
     */
    public static final String[] STOP_WORDS;// =
					    // StopAnalyzer.ENGLISH_STOP_WORDS;

    static
    {
	String stopwords = CrawlerSetting.getProperty("analyzer.ftp.stopwords");
	String[] splits = stopwords.split(";");
	String[] STOP = new String[splits.length + StopAnalyzer.ENGLISH_STOP_WORDS_SET.size()];
	for (int i = 0; i < splits.length; i++)
	    STOP[i] = splits[i];
	Iterator<?> iterator = StopAnalyzer.ENGLISH_STOP_WORDS_SET.iterator();
	for (int i = 0; iterator.hasNext(); i++)
	{
	    STOP[i + splits.length] = (String) iterator.next();
	}
	STOP_WORDS = STOP;
    }

    /** Builds an analyzer with the default stop words ({@link #STOP_WORDS}). */
    public FtpFilePathAnalyzer()
    {
	this(STOP_WORDS);
    }

    /** Builds an analyzer with the given stop words. */
    @SuppressWarnings("unchecked")
    public FtpFilePathAnalyzer(Set stopWords)
    {
	stopSet = stopWords;
    }

    /** Builds an analyzer with the given stop words. */
    public FtpFilePathAnalyzer(String[] stopWords)
    {
	stopSet = StopFilter.makeStopSet(stopWords);
    }

    /**
     * Builds an analyzer with the stop words from the given file.
     * 
     * @see WordlistLoader#getWordSet(File)
     */
    public FtpFilePathAnalyzer(File stopwords) throws IOException
    {
	stopSet = WordlistLoader.getWordSet(stopwords);
    }

    /**
     * Builds an analyzer with the stop words from the given reader.
     * 
     * @see WordlistLoader#getWordSet(Reader)
     */
    public FtpFilePathAnalyzer(Reader stopwords) throws IOException
    {
	stopSet = WordlistLoader.getWordSet(stopwords);
    }

    /**
     * Constructs a {@link StandardTokenizer} filtered by a
     * {@link StandardFilter}, a {@link LowerCaseFilter} and a
     * {@link StopFilter}.
     */
    public TokenStream tokenStream(String fieldName, Reader reader)
    {
	CharFilter filter = new LowercaseCharFilter(reader);
	filter = new MappingCharFilter(RECOVERY_MAP,filter);
	StandardTokenizer tokenStream = new StandardTokenizer(Version.LUCENE_30, filter);
	tokenStream.setMaxTokenLength(maxTokenLength);
	TokenStream result = new StandardFilter(tokenStream);
//	result = new LowerCaseFilter(result);
	result = getStopFilter(result);
	result = new SnowballFilter(result, STEMMER);
	return result;
    }

    public TokenFilter getStopFilter(TokenStream in)
    {
	return new RosaStopFilter(in, stopSet);
    }

    static final class SavedStreams
    {
	StandardTokenizer tokenStream;

	TokenStream filteredTokenStream;
    }

    /** Default maximum allowed token length */
    public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

    protected int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

    /**
     * Set maximum allowed token length. If a token is seen that exceeds this
     * length then it is discarded. This setting only takes effect the next time
     * tokenStream or reusableTokenStream is called.
     */
    public void setMaxTokenLength(int length)
    {
	maxTokenLength = length;
    }

    /**
     * @see #setMaxTokenLength
     */
    public int getMaxTokenLength()
    {
	return maxTokenLength;
    }

    @SuppressWarnings("deprecation")
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException
    {
	SavedStreams streams = (SavedStreams) getPreviousTokenStream();
	if (streams == null)
	{
	    streams = new SavedStreams();
	    setPreviousTokenStream(streams);
	    CharFilter filter = new LowercaseCharFilter(reader);
	    filter = new MappingCharFilter(RECOVERY_MAP,filter);
	    streams.tokenStream = new StandardTokenizer(Version.LUCENE_30, filter);
	    streams.filteredTokenStream = new StandardFilter(streams.tokenStream);
//	    streams.filteredTokenStream = new LowerCaseFilter(streams.filteredTokenStream);
	    streams.filteredTokenStream = getStopFilter(streams.filteredTokenStream);
	    streams.filteredTokenStream = new SnowballFilter(streams.filteredTokenStream, STEMMER);
	}
	else
	{
	    CharFilter filter = new LowercaseCharFilter(reader);
	    filter = new MappingCharFilter(RECOVERY_MAP,filter);
	    streams.tokenStream.reset(filter);
	}
	streams.tokenStream.setMaxTokenLength(maxTokenLength);

	streams.tokenStream.setReplaceInvalidAcronym(replaceInvalidAcronym);

	return streams.filteredTokenStream;
    }

    public static void main(String[] args)
    {
	Analyzer ana = new FtpFilePathAnalyzer();
	String test2 = "c++c++";
	StringReader reader = new StringReader(test2);
	TokenStream ts = ana.tokenStream("path", reader);
	try
	{
	    while (ts.incrementToken())
	    {
		TermAttribute termAtt = (TermAttribute) ts.getAttribute(TermAttribute.class);
		OffsetAttribute offsetAtt = (OffsetAttribute)ts.getAttribute(OffsetAttribute.class);
		PositionIncrementAttribute posIncrAtt = (PositionIncrementAttribute)ts.getAttribute(PositionIncrementAttribute.class);
		TypeAttribute typeAtt = (TypeAttribute) ts.getAttribute(TypeAttribute.class);
		System.out.print("("+offsetAtt.startOffset()+","+offsetAtt.endOffset()+") ["+posIncrAtt.getPositionIncrement()+","+typeAtt.type()+"] "+"[" + termAtt.term() + "]");
	    }
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }
}
