package utils;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class StringPatternMatcher
{
    /**
     * internal pattern the matcher tries to match, representing a file entry
     */
    private Pattern pattern = null;

    /**
     * internal match result used by the parser
     */
    private MatchResult result = null;

    /**
     * Internal PatternMatcher object used by the parser. It has protected scope
     * in case subclasses want to make use of it for their own purposes.
     */
    protected PatternMatcher matcher = null;

    public StringPatternMatcher(String sPattern)
    {
	try
	{
	    matcher = new Perl5Matcher();
	    pattern = new Perl5Compiler().compile(sPattern);
	}
	catch (MalformedPatternException e)
	{
	    throw new IllegalArgumentException("Unparseable regex supplied:  "
		    + sPattern);
	}
    }

    public boolean match(String str)
    {
	this.result = null;
	if (matcher.matches(str.trim(), this.pattern))
	{
	    this.result = matcher.getMatch();
	}
	return null != this.result;
    }

    public static void main(String[] args)
    {
	String ip = "((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)(\\.((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)){3}";
	String domain = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";
	StringPatternMatcher matcher = new StringPatternMatcher(ip + "|"
		+ domain);
	System.out.println(matcher.match("1234"));
    }
}
