package solr;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.BaseCharFilter;
import org.apache.lucene.analysis.CharStream;

/**
 * CharFilter that uses a regular expression for the target of replace string.
 * The replacement can be represented as list of group numbers or literal that
 * is put curly parentheses around {} via replaceGroups parameter. The pattern
 * match will be done in each "block" in char stream.
 * <p>
 * ex1) source="aa&nbsp;&nbsp;bb&nbsp;aa&nbsp;bb", groupedPattern="(aa)\\s+(bb)"
 * replaceGroups="1,{#},2"<br/>
 * output="aa#bb&nbsp;aa#bb"
 * </p>
 * NOTE: If you produce a phrase that has different length to source string and
 * the field is used for highlighting for a term of the phrase, you will face a
 * trouble.
 * <p>
 * ex2) source="aa123bb", groupedPattern="(aa)\\d+(bb)"
 * replaceGroups="1,{&nbsp;},2"<br/>
 * output="aa&nbsp;bb"<br/>
 * and you want to search bb and highlight it, you will get<br/>
 * highlight snippet="aa1&lt;em&gt;23bb&lt;/em&gt;"
 * </p>
 * 
 * @version $Id$
 * @since Solr 1.5
 */
public class PatternReplaceCharFilter extends BaseCharFilter
{

    private final Pattern groupedPattern;

    private final List<ReplaceElement> rList;

    private final int maxBlockChars;

    private final String blockDelimiters;

    public static final int DEFAULT_MAX_BLOCK_CHARS = 10000;

    public static final String DEFAULT_BLOCK_DELIMITERS = ".";

    private LinkedList<Character> buffer;

    private int nextCharCounter;

    private char[] blockBuffer;

    private int blockBufferLength;

    private String replaceBlockBuffer;

    private int replaceBlockBufferOffset;

    public PatternReplaceCharFilter(String groupedPattern, String replaceGroups, CharStream in)
    {
	this(groupedPattern, replaceGroups, DEFAULT_MAX_BLOCK_CHARS, DEFAULT_BLOCK_DELIMITERS, in);
    }

    public PatternReplaceCharFilter(String groupedPattern, String replaceGroups, int maxBlockChars,
	    CharStream in)
    {
	this(groupedPattern, replaceGroups, maxBlockChars, DEFAULT_BLOCK_DELIMITERS, in);
    }

    public PatternReplaceCharFilter(String groupedPattern, String replaceGroups,
	    String blockDelimiters, CharStream in)
    {
	this(groupedPattern, replaceGroups, DEFAULT_MAX_BLOCK_CHARS, blockDelimiters, in);
    }

    public PatternReplaceCharFilter(String groupedPattern, String replaceGroups, int maxBlockChars,
	    String blockDelimiters, CharStream in)
    {
	super(in);
	this.groupedPattern = Pattern.compile(groupedPattern);
	if (maxBlockChars < 1)
	    throw new IllegalArgumentException("maxBlockChars should be greater than 0, but it is "
		    + maxBlockChars);
	this.maxBlockChars = maxBlockChars;
	this.blockDelimiters = blockDelimiters;
	rList = getReplaceElementList(replaceGroups);
	blockBuffer = new char[maxBlockChars];
    }

    private boolean prepareReplaceBlock() throws IOException
    {
	while (true)
	{
	    if (replaceBlockBuffer != null
		    && replaceBlockBuffer.length() > replaceBlockBufferOffset)
		return true;
	    // prepare block buffer
	    blockBufferLength = 0;
	    while (true)
	    {
		int c = nextChar();
		if (c == -1)
		    break;
		blockBuffer[blockBufferLength++] = (char) c;
		// end of block?
		if (blockDelimiters.indexOf(c) >= 0 || blockBufferLength >= maxBlockChars)
		    break;
	    }
	    // block buffer available?
	    if (blockBufferLength == 0)
		return false;
	    replaceBlockBuffer = getReplaceBlock(blockBuffer, 0, blockBufferLength);
	    replaceBlockBufferOffset = 0;
	}
    }

    public int read() throws IOException
    {
	while (prepareReplaceBlock())
	{
	    return replaceBlockBuffer.charAt(replaceBlockBufferOffset++);
	}
	return -1;
    }

    public int read(char[] cbuf, int off, int len) throws IOException
    {
	char[] tmp = new char[len];
	int l = input.read(tmp, 0, len);
	if (l != -1)
	{
	    for (int i = 0; i < l; i++)
		pushLastChar(tmp[i]);
	}
	l = 0;
	for (int i = off; i < off + len; i++)
	{
	    int c = read();
	    if (c == -1)
		break;
	    cbuf[i] = (char) c;
	    l++;
	}
	return l == 0 ? -1 : l;
    }

    private int nextChar() throws IOException
    {
	nextCharCounter++;
	if (buffer != null && !buffer.isEmpty())
	{
	    return buffer.removeFirst().charValue();
	}
	return input.read();
    }

    private void pushLastChar(int c)
    {
	if (buffer == null)
	{
	    buffer = new LinkedList<Character>();
	}
	buffer.addLast(new Character((char) c));
    }

    static List<ReplaceElement> getReplaceElementList(String replaceGroups)
    {
	List<ReplaceElement> elements = new ArrayList<ReplaceElement>();
	int p = 0;
	while (p < replaceGroups.length())
	{
	    char c = replaceGroups.charAt(p);
	    if (isDigit(c))
	    {
		StringBuilder sb = new StringBuilder();
		sb.append(c);
		p++;
		while (p < replaceGroups.length())
		{
		    char cc = replaceGroups.charAt(p);
		    if (isDigit(cc))
		    {
			sb.append(cc);
			p++;
		    }
		    else
		    {
			break;
		    }
		}
		elements.add(new ReplaceElement(Integer.valueOf(sb.toString())));
	    }
	    else if (startLiteral(c))
	    {
		StringBuilder sb = new StringBuilder();
		p++;
		while (p < replaceGroups.length())
		{
		    char cc = replaceGroups.charAt(p);
		    if (endLiteral(cc))
		    {
			p++;
			break;
		    }
		    if (isEscape(cc))
		    {
			// TODO: should we treat \n, \t, ...?
			cc = replaceGroups.charAt(++p);
		    }
		    sb.append(cc);
		    p++;
		}
		elements.add(new ReplaceElement(sb.toString()));
	    }
	    else if (isDelimiter(c))
	    {
		p++;
		continue;
	    }
	    else
	    {
		throw new RuntimeException("Syntax error in replaceGroups=\"" + replaceGroups
			+ "\" at " + p + " ('" + c + "')");
	    }
	}
	return elements;
    }

    private static boolean isDigit(char c)
    {
	return '0' <= c && c <= '9';
    }

    private static boolean startLiteral(char c)
    {
	return c == '{';
    }

    private static boolean endLiteral(char c)
    {
	return c == '}';
    }

    private static boolean isEscape(char c)
    {
	return c == '\\';
    }

    private static boolean isDelimiter(char c)
    {
	return c == ',';
    }

    String getReplaceBlock(String block)
    {
	char[] blockChars = block.toCharArray();
	return getReplaceBlock(blockChars, 0, blockChars.length);
    }

    String getReplaceBlock(char block[], int offset, int length)
    {
	StringBuilder replaceBlock = new StringBuilder();
	Matcher m = groupedPattern.matcher(new String(block, offset, length));
	int lastMatchOffset = 0;
	while (m.find())
	{
	    // copy the prior part of the match
	    replaceBlock.append(block, offset + lastMatchOffset, m.start(0) - lastMatchOffset);
	    // copy the replacement of the match
	    String replaceString = getReplaceString(m);
	    replaceBlock.append(replaceString);
	    // record cumulative diff for the offset correction
	    int diff = replaceString.length() - (m.end(0) - m.start(0));
	    if (diff != 0)
	    {
		int prevCumulativeDiff = getLastCumulativeDiff();
		if (diff > 0)
		{
		    for (int i = 0; i < diff; i++)
		    {
			addOffCorrectMap(nextCharCounter - length - 1 + m.end(0) + i
				- prevCumulativeDiff, prevCumulativeDiff - 1 - i);
		    }
		}
		else
		{
		    addOffCorrectMap(nextCharCounter - length - 1 + m.end(0) + diff
			    - prevCumulativeDiff, prevCumulativeDiff - diff);
		}
	    }
	    // set last offset
	    lastMatchOffset = m.end(0);
	}
	// copy remaining of the part of source block
	replaceBlock.append(block, offset + lastMatchOffset, length - lastMatchOffset);
	return replaceBlock.toString();
    }

    private String getReplaceString(Matcher m)
    {
	StringBuilder replaceString = new StringBuilder();
	for (ReplaceElement element : rList)
	{
	    replaceString.append(element.getReplace(m));
	}
	return replaceString.toString();
    }

    static class ReplaceElement
    {

	final int group;

	final String literal;

	// group element
	ReplaceElement(int group)
	{
	    this.group = group;
	    literal = null;
	}

	// literal element
	public ReplaceElement(String literal)
	{
	    this.literal = literal;
	    group = -1;
	}

	public String getReplace(Matcher m)
	{
	    return literal != null ? literal : m.group(group);
	}

	// used by test in the meantime
	public boolean equals(Object o)
	{
	    if (o == this)
		return true;
	    if (!(o instanceof ReplaceElement))
		return false;
	    ReplaceElement other = (ReplaceElement) o;
	    return group == other.group && literal.equals(other.literal);
	}

	public int hashCode()
	{
	    int result = 17;
	    result = 37 * result + group;
	    result = 37 * result + literal.hashCode();
	    return result;
	}
    }
}
