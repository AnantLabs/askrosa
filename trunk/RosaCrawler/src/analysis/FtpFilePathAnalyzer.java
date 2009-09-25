package analysis;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.snowball.SnowballFilter;

/**
 * Filters {@link StandardTokenizer} with {@link StandardFilter}, {@link
 * LowerCaseFilter} and {@link StopFilter}, using a list of English stop words.
 *
 * @version $Id: FtpFilePathAnalyzer.java,v 1.8 2009-01-09 09:27:24 rl Exp $
 */
public class FtpFilePathAnalyzer extends Analyzer {
  @SuppressWarnings("unchecked")
private Set stopSet;
  private static final String STEMMER ="English";

  /**
   * Specifies whether deprecated acronyms should be replaced with HOST type.
   * This is false by default to support backward compatibility.
   * 
   * @deprecated this should be removed in the next release (3.0).
   *
   * See https://issues.apache.org/jira/browse/LUCENE-1068
   */
  private boolean replaceInvalidAcronym = defaultReplaceInvalidAcronym;

  private static boolean defaultReplaceInvalidAcronym;

  // Default to true (fixed the bug), unless the system prop is set
  static {
    final String v = System.getProperty("org.apache.lucene.analysis.standard.StandardAnalyzer.replaceInvalidAcronym");
    if (v == null || v.equals("true"))
      defaultReplaceInvalidAcronym = true;
    else
      defaultReplaceInvalidAcronym = false;
  }

  /**
   *
   * @return true if new instances of StandardTokenizer will
   * replace mischaracterized acronyms
   *
   * See https://issues.apache.org/jira/browse/LUCENE-1068
   * @deprecated This will be removed (hardwired to true) in 3.0
   */
  public static boolean getDefaultReplaceInvalidAcronym() {
    return defaultReplaceInvalidAcronym;
  }

  /**
   *
   * @param replaceInvalidAcronym Set to true to have new
   * instances of StandardTokenizer replace mischaracterized
   * acronyms by default.  Set to false to preseve the
   * previous (before 2.4) buggy behavior.  Alternatively,
   * set the system property
   * org.apache.lucene.analysis.standard.StandardAnalyzer.replaceInvalidAcronym
   * to false.
   *
   * See https://issues.apache.org/jira/browse/LUCENE-1068
   * @deprecated This will be removed (hardwired to true) in 3.0
   */
  public static void setDefaultReplaceInvalidAcronym(boolean replaceInvalidAcronym) {
    defaultReplaceInvalidAcronym = replaceInvalidAcronym;
  }


  /** An array containing some common English words that are usually not
  useful for searching. */
  public static final String[] STOP_WORDS = StopAnalyzer.ENGLISH_STOP_WORDS;

  /** Builds an analyzer with the default stop words ({@link #STOP_WORDS}). */
  public FtpFilePathAnalyzer() {
    this(STOP_WORDS);
  }

  /** Builds an analyzer with the given stop words. */
  @SuppressWarnings("unchecked")
public FtpFilePathAnalyzer(Set stopWords) {
    stopSet = stopWords;
  }

  /** Builds an analyzer with the given stop words. */
  public FtpFilePathAnalyzer(String[] stopWords) {
    stopSet = StopFilter.makeStopSet(stopWords);
  }

  /** Builds an analyzer with the stop words from the given file.
   * @see WordlistLoader#getWordSet(File)
   */
  public FtpFilePathAnalyzer(File stopwords) throws IOException {
    stopSet = WordlistLoader.getWordSet(stopwords);
  }

  /** Builds an analyzer with the stop words from the given reader.
   * @see WordlistLoader#getWordSet(Reader)
   */
  public FtpFilePathAnalyzer(Reader stopwords) throws IOException {
    stopSet = WordlistLoader.getWordSet(stopwords);
  }

 
  /** Constructs a {@link StandardTokenizer} filtered by a {@link
  StandardFilter}, a {@link LowerCaseFilter} and a {@link StopFilter}. */
  public TokenStream tokenStream(String fieldName, Reader reader) {
    StandardTokenizer tokenStream = new StandardTokenizer(reader, replaceInvalidAcronym);
    tokenStream.setMaxTokenLength(maxTokenLength);
    TokenStream result = new StandardFilter(tokenStream);
    result = new LowerCaseFilter(result);
    result = new StopFilter(result, stopSet);
    result = new SnowballFilter(result,STEMMER);
    return result;
  }

  private static final class SavedStreams {
    StandardTokenizer tokenStream;
    TokenStream filteredTokenStream;
  }

  /** Default maximum allowed token length */
  public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

  private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

  /**
   * Set maximum allowed token length.  If a token is seen
   * that exceeds this length then it is discarded.  This
   * setting only takes effect the next time tokenStream or
   * reusableTokenStream is called.
   */
  public void setMaxTokenLength(int length) {
    maxTokenLength = length;
  }
    
  /**
   * @see #setMaxTokenLength
   */
  public int getMaxTokenLength() {
    return maxTokenLength;
  }
  
  @SuppressWarnings("deprecation")
public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
    SavedStreams streams = (SavedStreams) getPreviousTokenStream();
    if (streams == null) {
      streams = new SavedStreams();
      setPreviousTokenStream(streams);
      streams.tokenStream = new StandardTokenizer(reader);
      streams.filteredTokenStream = new StandardFilter(streams.tokenStream);
      streams.filteredTokenStream = new LowerCaseFilter(streams.filteredTokenStream);
      streams.filteredTokenStream = new StopFilter(streams.filteredTokenStream, stopSet);
      streams.filteredTokenStream = new SnowballFilter(streams.filteredTokenStream,STEMMER);
    } else {
      streams.tokenStream.reset(reader);
    }
    streams.tokenStream.setMaxTokenLength(maxTokenLength);
    
    streams.tokenStream.setReplaceInvalidAcronym(replaceInvalidAcronym);

    return streams.filteredTokenStream;
  }
  
  public static void main(String[] args)
  {
      	Analyzer ana = new FtpFilePathAnalyzer();
        String test2= "[先知].Knowing.2009.BDRip.X264-TLF";
        System.out.println(test2);
        StringReader reader = new StringReader(test2);
	TokenStream ts = ana.tokenStream("path", reader);
	Token t = null;
	final Token reusableToken = new Token();
	try
	{
	    while((t=ts.next(reusableToken))!=null)
	    {
	        System.out.println(t);
	    }
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
  }
}
