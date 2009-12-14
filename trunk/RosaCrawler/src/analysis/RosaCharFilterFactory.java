package analysis;

import org.apache.lucene.analysis.CharFilter;
import org.apache.lucene.analysis.CharStream;
import org.apache.lucene.analysis.MappingCharFilter;
import org.apache.lucene.analysis.NormalizeCharMap;

import resource.CrawlerSetting;
import solr.BaseCharFilterFactory;
import solr.PatternReplaceCharFilter;

public class RosaCharFilterFactory extends BaseCharFilterFactory
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
	for (int i = 0; i < splits.length; i += 2)
	{
	    RECOVERY_MAP.add(splits[i], splits[i + 1]);
	}

    }

    @Override
    public CharStream create(CharStream input)
    {
	CharFilter filter = new LowercaseCharFilter(input);
	/**
	 * 将season07这样的字符串过滤为season 07
	 */
	String pattern = "([\\p{Alpha}]+)(\\d+)";//"([\\D&&\\S]+)(\\d+)";
	String replace = "1,{ },2";
	filter = new PatternReplaceCharFilter(pattern, replace,
		PatternReplaceCharFilter.DEFAULT_MAX_BLOCK_CHARS,
		PatternReplaceCharFilter.DEFAULT_BLOCK_DELIMITERS, filter);
	filter = new MappingCharFilter(RECOVERY_MAP, filter);
	return filter;
    }
}
