package webftp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.ParallelMultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searchable;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import analysis.RosaAnalyzer;

import crawlerutils.RosaCrawlerConstants;

import resource.CrawlerSetting;
import searcher.Node;

public class WebFTPSearchEngine extends UnicastRemoteObject implements WebFTPSearch
{
    public WebFTPSearchEngine() throws RemoteException
    {
	super();
    }
    /**
     * 
     */
    private static final long serialVersionUID = 2076462083059246648L;
    public static LinkedList<Node> NODES = new LinkedList<Node>();
    static
    {
	SAXBuilder builder = new SAXBuilder();
	Document doc = null;
	try
	{
	    URL url = WebFTPSearchEngine.class
		    .getResource("/resource/crawlernodes.xml");
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
    
    /**
     * 获得查询过滤器，分类，访问控制，及结果时效性过滤都由此函数负责。
     * 
     * @param parameter
     *            搜索参数
     * @return 过滤器
     */
    private static Filter getQueryFilter()
    {
	BooleanQuery query = new BooleanQuery();

	TermQuery accessAnybody = new TermQuery(new Term("access", "anybody"));

	query.add(accessAnybody, BooleanClause.Occur.MUST);

	// 只检索最近N天更新的站点
	Calendar c = Calendar.getInstance();
	c.setTimeInMillis(c.getTimeInMillis()
		- CrawlerSetting.getInt("searcher.recentupdateconstraint")
		* RosaCrawlerConstants.ONE_DAY_IN_MILLISECOND);
	String fromTime = DateTools.dateToString(c.getTime(),
		DateTools.Resolution.DAY);
	
	Query updateTimeRange = NumericRangeQuery.newIntRange("updateTime", Integer.parseInt(fromTime), Integer.MAX_VALUE, true, false); 
//	= new RangeQuery(new Term("updatetime",
//		fromTime), null, true);
	query.add(updateTimeRange, BooleanClause.Occur.MUST);

	return new CachingWrapperFilter(new QueryWrapperFilter(query));
    }
    @Override
    public Set<String> query(String keyword, int limit) throws MalformedURLException, RemoteException, IOException, NotBoundException
    {
	ParallelMultiSearcher searcher = new ParallelMultiSearcher(
		getAllRemoteSearchers());
	QueryParser qp = new QueryParser(Version.LUCENE_30, "name", getAnalyzer());
	qp.setDefaultOperator(QueryParser.AND_OPERATOR);
	qp.setAllowLeadingWildcard(true);

	Query query;
	try
	{
	    query = qp.parse(keyword);
	    Filter filter = getQueryFilter();
	    TopDocs topFieldDocs = searcher.search(query, filter,
			limit);
	    return getSearchResult(searcher, topFieldDocs);
	}
	catch (ParseException e)
	{
	    e.printStackTrace();
	}
	return null;
    }
    
    private static Set<String> getSearchResult(Searcher searcher,
	    TopDocs topFieldDocs) throws CorruptIndexException, IOException
    {
	Set<String> retList = new LinkedHashSet<String>();

	ScoreDoc[] hits = topFieldDocs.scoreDocs;
	int endIndex = hits.length;

	for (int i = 0; i < endIndex; i++)
	{
	    org.apache.lucene.document.Document doc;
	    doc = searcher.doc(hits[i].doc);

	    String server = doc.get("server");
	    String username = doc.get("username");
	    String password = doc.get("password");
	    String port = doc.get("port");
	    String parent = doc.get("parent");
	    String ret = "ftp://"+username+":"+password+"@"+server+":"+port+" "+parent;
	    retList.add(ret);
	}
	return retList;
    }
    /**
     * 获取分析器，这个分析器对不同的域使用不同的分析器，对于name和path域使用索引时候使用的FtpFilePathAnalyzer
     * 其他域使用关键字分析器
     * 
     * @return 分析器
     */
    public static Analyzer getAnalyzer()
    {
	PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(
		new KeywordAnalyzer());
	analyzer.addAnalyzer("name", new RosaAnalyzer());
	analyzer.addAnalyzer("path", new RosaAnalyzer());
	return analyzer;
    }
    /**
     * 获取RMI调用
     * 
     * @return RMI调用
     * @throws NotBoundException
     * @throws RemoteException
     * @throws MalformedURLException
     */
    public static Searchable[] getAllRemoteSearchers()
	    throws MalformedURLException, RemoteException, NotBoundException
    {
	LinkedList<Searchable> list = new LinkedList<Searchable>();
	for (Node node : NODES)
	{
	    Searchable s;
	    String rmi = "rmi://" + node.getAddress() + ":" + node.getPort()
		    + "/" + node.getName();
	    s = (Searchable) Naming.lookup(rmi);
	    list.add(s);

	}
	return list.toArray(new Searchable[0]);
    }

}
