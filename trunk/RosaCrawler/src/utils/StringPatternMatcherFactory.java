package utils;

public class StringPatternMatcherFactory
{

    public static final String IP = "((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)(\\.((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)){3}";

    public static final String DOMAIN = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";

    public static final int PATTERN_IP = 1;

    public static final int PATTERN_DOMAIN = 2;

    public static StringPatternMatcher createStringPatternMatcher(int type)
    {
	if (type == PATTERN_IP)
	{
	    return new StringPatternMatcher(IP);
	}
	else if (type == PATTERN_DOMAIN)
	{
	    return new StringPatternMatcher(DOMAIN);
	}
	else if (type == PATTERN_IP + PATTERN_DOMAIN)
	{
	    return new StringPatternMatcher(IP + "|" + DOMAIN);
	}
	return null;
    }
}
