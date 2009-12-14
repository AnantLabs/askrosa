package searcher;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import log.CrawlerLogger;

import org.apache.commons.mail.EmailException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.ParallelMultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searchable;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import resource.CrawlerSetting;
import utils.KeyboardReader;
import utils.Tools;
import analysis.RecentModfiedResourceAnalyzer;
import analysis.RosaAnalyzer;
import analysis.RosaAnalyzerUtils;
import autocomplete.AutoCompleteRemote;
import crawlerutils.RosaCrawlerConstants;
import database.QueryStatistics;
import editdistance.LevensteinDistance;


/**
 * 负责搜索的类，所有的方法均为静态方法
 * 
 * @author elegate
 */
public class FtpSearch
{
    public static LinkedList<Node> NODES = new LinkedList<Node>();
    

    public static final String LUCENE_RESERVED = "[\\+\\-~!\\[\\]]";

    static
    {
	SAXBuilder builder = new SAXBuilder();
	Document doc = null;
	try
	{
	    URL url = FtpSearch.class.getResource("/resource/crawlernodes.xml");
	    doc = builder.build(url);
	    Element root = doc.getRootElement();
	    List<?> nodes = root.getChildren("node");
	    for (Object e : nodes)
	    {
		String name = ((Element) e).getChildText("name");
		String address = ((Element) e).getChildText("address");
		int port = Integer.parseInt(((Element) e).getChildText("port"));
		NODES.add(new Node(name, address, port));
	    }
	}
	catch (JDOMException e1)
	{
	    e1.printStackTrace();
	}
	catch (IOException e1)
	{
	    e1.printStackTrace();
	}
    }

    public static Map<String, Integer> suggestSimilar(String word) throws MalformedURLException,
	    RemoteException, NotBoundException
    {
	String rmi = "rmi://" + CrawlerSetting.getProperty("autocomplete.host") + ":"
		+ CrawlerSetting.getProperty("autocomplete.port") + "/"
		+ CrawlerSetting.getProperty("autocomplete.name");
	AutoCompleteRemote checker = (AutoCompleteRemote) Naming.lookup(rmi);
	return checker.suggestSimilar(word);
    }
    
    public static Map<String,String> getRecentModifiedResource(String[] categories,int daysConstraint, int count) throws IOException, ParseException, NotBoundException, InvalidTokenOffsetsException
    {
	LevensteinDistance ld = new LevensteinDistance();
	SearchParameter parameter = new SearchParameter();
	parameter.setBegin(0);
	Calendar c = Calendar.getInstance();
	c.setTimeInMillis(c.getTimeInMillis() - daysConstraint
		* RosaCrawlerConstants.ONE_DAY_IN_MILLISECOND);
	String fromTime = DateTools.dateToString(c.getTime(), DateTools.Resolution.DAY);
	parameter.setKeyword("date:["+fromTime+" TO null]");
	parameter.setCategories(categories);
	parameter.setField("name");
	parameter.setSortType("datedesc");
	parameter.setBegin(0);
	parameter.setCount(count);
	SearchResult result  = FtpSearch.search(parameter, false);
	HashMap<String,String> map = new HashMap<String,String>();
	Analyzer analyzer = getRecentModifiedResourceAnalyzer();
	for(SearchResultElement ele:result.getResultFileList())
	{
	    String name = Tools.getFileName(ele.getName());
	    String parent = ele.getParentPath();
	    parent = parent.substring(parent.lastIndexOf('/',parent.length()-2)+1,parent.length()-1);
	    String parsedParent = RosaAnalyzerUtils.getAnalyzedString(analyzer, parent).trim();
	    String parsedName = RosaAnalyzerUtils.getAnalyzedString(analyzer, name).trim();
	    
	    double distance = ld.getDistance(parsedParent, parsedName);
	    if(distance>0.5)
	    {
		if(parsedName.length()>0)
		    map.put(parsedName, name);
	    }
	    else
	    {
		int nameLen = parsedName.getBytes().length;
		int parentLen = parsedParent.getBytes().length;
        	    if(nameLen>=parentLen&&parentLen>0)
        		map.put(parsedName, name);
        	    else if(parentLen>0)
        		map.put(parsedParent, name);
	    }
	}
	return map;
    }

    /**
     * 获取高亮处理器
     * 
     * @param query
     *            查询
     * @return 高亮处理器
     */
    public static Highlighter getHighlighter(Query query)
    {
	QueryScorer scorer = new QueryScorer(query);
	SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class=\"highlight\">",
		"</span>");
	Highlighter h = new Highlighter(formatter, scorer);
	return h;
    }

    private static String filterKeyword(String keyword)
    {
	return keyword.replaceAll(LUCENE_RESERVED, " ").trim();
    }

    public static SearchResult search(SearchParameter parameter) throws IOException,
	    ParseException, NotBoundException, InvalidTokenOffsetsException
    {
	return search(parameter, true);
    }

//    public static SearchResult search(SearchParameter parameter, boolean filterKeyword)
//	    throws IOException, ParseException, NotBoundException
//    {
//	return search(parameter, filterKeyword, true);
//    }

    /**
     * 根据提供的参数进行搜索并返回搜索结果
     * 
     * @param parameter
     *            搜索参数
     * @return 搜索结果
     * @throws IOException
     * @throws ParseException
     * @throws NotBoundException
     * @throws ParseException 
     * @throws InvalidTokenOffsetsException 
     */
    public static SearchResult search(SearchParameter parameter, boolean doStatistics) throws IOException, NotBoundException, ParseException, InvalidTokenOffsetsException
    {
	final int N = RosaCrawlerConstants.SEARCH_PAGE_SIZE * 10;
	long begin = System.currentTimeMillis();
	ParallelMultiSearcher searcher = new ParallelMultiSearcher(getAllRemoteSearchers());
	QueryParser qp = new QueryParser(Version.LUCENE_30, parameter.getField(), getAnalyzer());
	qp.setDefaultOperator(QueryParser.OR_OPERATOR);
	qp.setAllowLeadingWildcard(true);
	String keyword = parameter.getKeyword();
	
	Query query =null;
	try
	{
	    query= qp.parse(keyword);
	}
	catch(ParseException e)
	{
	    query = qp.parse(filterKeyword(keyword));
	}
	
	
	Filter filter = getQueryFilter(parameter);
	Sort sort = getSort(parameter);
	TopDocs topFieldDocs = searcher.search(query, filter, N,sort);
	if (N < topFieldDocs.totalHits)
	    topFieldDocs = searcher.search(query, filter, topFieldDocs.totalHits,sort);
//	TopDocs topFieldDocs = searcher.search(query, N);
	Highlighter h = getHighlighter(query);
	SearchResult result = getSearchResult(searcher, topFieldDocs, parameter, h);

	result.setHitsNum(topFieldDocs.totalHits);
	result.setDelay(System.currentTimeMillis() - begin);
	if (doStatistics && parameter.getBegin() == 0
		&& parameter.getSortType().equals(SearchParameter.DEFAULT_SORT_TYPE)
		)
	{
	    doQueryStatistics(query, parameter, result);
	}
	return result;
    }

    /**
     * 查询统计
     * 
     * @param query
     *            查询，lucene对象
     * @param parameter
     *            查询参数
     */
    private static void doQueryStatistics(Query query, SearchParameter parameter,
	    SearchResult result)
    {
	String qString = query.toString();
	String[] fields = qString.split("\\s\\p{Print}");
	int fieldLen = parameter.getField().length();
	String key = null;
	for (String value : fields)
	{
	    int index = value.indexOf(parameter.getField());
	    if (index >= 0)
	    {
		String keyword = value.substring(fieldLen + index + 1);
		keyword = keyword.replaceAll("(\\s|\")", "");

		if (key == null)
		{
		    key = keyword;
		}
		else
		    key += " " + keyword;

	    }
	}
	if (key != null)
	{
	    QueryStatistics q = new QueryStatistics();
	    q.setKeyword(key);
	    q.setHits(result.getHistNum());
	    try
	    {
		q.save();
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}
    }

    /**
     * 获得查询过滤器，分类，访问控制，及结果时效性过滤都由此函数负责。
     * 
     * @param parameter
     *            搜索参数
     * @return 过滤器
     */
    private static Filter getQueryFilter(SearchParameter parameter)
    {
	BooleanQuery query = new BooleanQuery();

	// categories filter
	if (parameter.getCategories() != null)
	{
	    BooleanQuery categories = new BooleanQuery();
	    for (String category : parameter.getCategories())
	    {
		TermQuery cat = new TermQuery(new Term("cat", category));
		categories.add(cat, BooleanClause.Occur.SHOULD);
	    }
	    query.add(categories, BooleanClause.Occur.MUST);
	}

	if (parameter.getLocations() != null)
	{
	    BooleanQuery locations = new BooleanQuery();
	    for (int location : parameter.getLocations())
	    {
		TermQuery loc = new TermQuery(new Term("location", location + ""));
		locations.add(loc, BooleanClause.Occur.SHOULD);
	    }
	    query.add(locations, BooleanClause.Occur.MUST);
	}

	if (parameter.getExtensions() != null && parameter.getExtensions().length() > 0)
	{
	    String[] exts = parameter.getExtensions().split(",");
	    if (exts.length > 0)
	    {
		BooleanQuery extensions = new BooleanQuery();

		for (int i = 0; i < exts.length; i++)
		{
		    TermQuery e = new TermQuery(new Term("ext", exts[i]));
		    extensions.add(e, BooleanClause.Occur.SHOULD);
		}
		query.add(extensions, BooleanClause.Occur.MUST);
	    }
	}
	// access control
	TermQuery accessUser = new TermQuery(new Term("access", parameter.getAccess()));
	accessUser.setBoost(10.0f);
	TermQuery accessAnybody = new TermQuery(new Term("access", "anybody"));
	BooleanQuery access = new BooleanQuery();
	access.add(accessUser, BooleanClause.Occur.SHOULD);
	access.add(accessAnybody, BooleanClause.Occur.SHOULD);
	// end of access control

	query.add(access, BooleanClause.Occur.MUST);

	// 只检索最近N天更新的站点
	/**
	 * 此处使用updateTime和updatetime两个域是因为以前的编码失误，计划域2009/12/24修正。
	 */
	Calendar c = Calendar.getInstance();
	c.setTimeInMillis(c.getTimeInMillis() - parameter.getRecentUpdateConstraint()
		* RosaCrawlerConstants.ONE_DAY_IN_MILLISECOND);
	String fromTime = DateTools.dateToString(c.getTime(), DateTools.Resolution.DAY);
	Query updateTimeRange = new TermRangeQuery("updateTime",fromTime,null,true,false);
	
	query.add(updateTimeRange, BooleanClause.Occur.MUST);
	
	
	return new CachingWrapperFilter(new QueryWrapperFilter(query));
    }

    /**
     * 根据搜索参数提取搜索结果，比如只提取其中部分结果，并对结果进行高亮显示
     * 
     * @param hits
     *            搜索条件命中的结果集
     * @param parameter
     *            搜索参数
     * @param h
     *            高亮处理器
     * @return 提取后的结果集
     * @throws InvalidTokenOffsetsException 
     */
    private static SearchResult getSearchResult(Searcher searcher, TopDocs topFieldDocs,
	    SearchParameter parameter, Highlighter h) throws InvalidTokenOffsetsException
    {
	SearchResult result = new SearchResult();
	int beginIndex = parameter.getBegin();
	int endIndex = parameter.getCount() + beginIndex;
	h.setTextFragmenter(new SimpleFragmenter(32000));
	ScoreDoc[] hits = topFieldDocs.scoreDocs;
	endIndex = Math.min(endIndex, hits.length);

	for (int i = beginIndex; i < endIndex; i++)
	{
	    org.apache.lucene.document.Document doc;
	    try
	    {
		doc = searcher.doc(hits[i].doc);
		SearchResultElement ele = new SearchResultElement();
		ele.setServer(doc.get("server"));
		String path = doc.get("path");
		ele.setPath(path);

		String name = doc.get("name");
		// highlight

		TokenStream pathStream = getAnalyzer().tokenStream("path", new StringReader(path));
		String highlightedPath = h.getBestFragment(pathStream, path);
		if (highlightedPath == null)
		    highlightedPath = path;
		TokenStream nameStream = getAnalyzer().tokenStream("name", new StringReader(name));
		String highlightedName = h.getBestFragment(nameStream, name);
		if (highlightedName == null)
		    highlightedName = name;
		ele.setHighlightedPath(highlightedPath);
		ele.setHighlightedName(highlightedName);

		ele.setCategory(doc.get("cat"));
		ele.setDate(doc.get("date"));
		ele.setUpdateTime(doc.get("updateTime"));
		ele.setSize(Long.parseLong(doc.get("size")));
		ele.setUsername(doc.get("username"));
		ele.setPassword(doc.get("password"));
		ele.setPort(Integer.parseInt(doc.get("port")));
		String location = doc.get("location");
		if (location != null)
		    ele.setLocation(Integer.parseInt(location));
		result.addSearchResultElement(ele);
	    }
	    catch (CorruptIndexException e)
	    {
		e.printStackTrace();
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
	    }
	}
	return result;
    }

    // /**
    // * 获取高亮后的路径的最后一个路径分割位置，也就是最后一个“/”号的位置
    // *
    // * @param text
    // * @param fromIndex
    // * @return 分割位置
    // */
    // private static int getPathNameSeperator(String text, int fromIndex)
    // {
    // int i = text.lastIndexOf('/', fromIndex);
    // if (i < 1)
    // return i;
    // else if (text.charAt(i - 1) == '<' && text.startsWith("span>", i + 1))
    // return getPathNameSeperator(text, i - 1);
    // else
    // return i;
    // }

    /**
     * 获得排序类型
     * 
     * @param parameter
     *            搜索参数
     * @return 排序类型
     */
    private static Sort getSort(SearchParameter parameter)
    {
	String value = CrawlerSetting.getProperty(parameter.getSortType());
	if (value != null && !value.equalsIgnoreCase("similarity"))
	{
	    StringTokenizer tokenizer = new StringTokenizer(value);
	    if (tokenizer.countTokens() != 3)
		return new Sort(SortField.FIELD_SCORE);
	    String fieldName = tokenizer.nextToken();
	    int dataType = SortField.SCORE;
	    try
	    {
		dataType = (Integer) Tools.getStaticFieldValue(SortField.class, tokenizer
			.nextToken());
		boolean desc = Boolean.valueOf(tokenizer.nextToken());
		SortField[] sortFields = new SortField[]
		{  new SortField(fieldName, dataType, desc),SortField.FIELD_SCORE };
		return new Sort(sortFields);
	    }
	    catch (SecurityException e)
	    {
		e.printStackTrace();
	    }
	    catch (IllegalArgumentException e)
	    {
		e.printStackTrace();
	    }
	    catch (NoSuchFieldException e)
	    {
		e.printStackTrace();
	    }
	    catch (IllegalAccessException e)
	    {
		e.printStackTrace();
	    }
	}
	return new Sort(SortField.FIELD_SCORE);
    }

    /**
     * 获取RMI调用
     * 
     * @return RMI调用
     * @throws NotBoundException
     * @throws RemoteException
     * @throws MalformedURLException
     */
    public static Searchable[] getAllRemoteSearchers() throws MalformedURLException,
	    RemoteException, NotBoundException
    {
	LinkedList<Searchable> list = new LinkedList<Searchable>();
	for (Node node : NODES)
	{
	    Searchable s;
	    String rmi = "rmi://" + node.getAddress() + ":" + node.getPort() + "/" + node.getName();
	    s = (Searchable) Naming.lookup(rmi);
	    list.add(s);

	}
	if (list.size() == 0)
	{
	    sendAlarmEmail();
	}
	return list.toArray(new Searchable[0]);
    }

    public static void sendAlarmEmail()
    {
	String host = "smtp.gmail.com";
	String from = "askrosateam@gmail.com";
	String psw = "welovefreedom";
	String to = "ww.wang.cs@gmail.com";
	String fromname = "AskRosa Team";
	String subject = "Problem with Rosa" + new Date().toString();
	String msg = "Rosa RMI Error";
	try
	{
	    Tools.sendEmail(host, true, "465", from, psw, fromname, to, subject, msg);
	}
	catch (EmailException e)
	{
	    CrawlerLogger.logger.error("Failed to send email to " + to, e);
	}
    }

    /**
     * 获取分析器，这个分析器对不同的域使用不同的分析器，对于name和path域使用索引时候使用的FtpFilePathAnalyzer
     * 其他域使用关键字分析器
     * 
     * @return 分析器
     */
    public static Analyzer getAnalyzer()
    {
	PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new KeywordAnalyzer());
	analyzer.addAnalyzer("path", new RosaAnalyzer());
	analyzer.addAnalyzer("name", new RosaAnalyzer());
	return analyzer;
    }

    public static Analyzer getRecentModifiedResourceAnalyzer()
    {
	return new RecentModfiedResourceAnalyzer();
    }
    
    /**
     * 测试函数
     * 
     * @param args
     *            参数
     * @throws IOException
     *             异常
     * @throws NotBoundException
     * @throws ParseException 
     */
    public static void main(String[] args) throws IOException, NotBoundException, ParseException
    {
	SearchParameter parameter = new SearchParameter();
	parameter.setBegin(0);
	parameter.setCount(RosaCrawlerConstants.SEARCH_PAGE_SIZE);
	parameter.setField("name");
	while (true)
	{
	    System.out.println("input query:");
	    String q = KeyboardReader.readLine().trim();
	    if (q == null || q.length() == 0 || "exit".equals(q))
		break;
	    parameter.setKeyword(q);
	    SearchResult result = new SearchResult();
	    try
	    {
		result = FtpSearch.search(parameter, true);
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	    for (SearchResultElement ele : result.getResultFileList())
	    {
		System.out.println(ele);
	    }
	    System.out.println("Time used:" + result.getDelay() + " ms," + result.getHistNum()
		    + " hits");
	}
    }
}
