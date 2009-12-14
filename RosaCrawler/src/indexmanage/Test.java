package indexmanage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searchable;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import resource.CrawlerSetting;
import searcher.FtpSearch;
import searcher.Node;
import searcher.SearchResultElement;
import utils.ElapseTimer;
import utils.KeyboardReader;
import utils.Poolable;
import analysis.RosaAnalyzer;
import crawl.FtpCrawler;
import crawl.IndexTestPool;
import crawlerutils.RosaCrawlerConstants;
import database.FtpSiteInfo;
import ftp.FTPFileInfo;

public class Test
{
    public static LinkedList<Node> NODES = new LinkedList<Node>();

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
	return list.toArray(new Searchable[0]);
    }
    
    
    public static void buildIndex()
    {
	CrawlerSetting.setProxy();
	ElapseTimer timer = new ElapseTimer();
	timer.begin();
	FtpSiteInfo ftpSite = new FtpSiteInfo();
	ftpSite.setServer("172.16.65.79");
	ftpSite.setUsername("test");
	ftpSite.setPassword("down");
	FtpCrawler ftp = new FtpCrawler(ftpSite);
	Poolable<FTPFileInfo> pool = new IndexTestPool("test",ftpSite);
	ftp.crawl(pool);
	pool.close();
	System.out.println("count:" + ftp.getCount());
	System.out.println("Description:" + ftpSite.getDescription());
	try
	{
	    timer.finish();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static void search(String keyword) throws ParseException, CorruptIndexException, IOException
    {
	final int N = RosaCrawlerConstants.SEARCH_PAGE_SIZE * 10;
	IndexReader reader = IndexReader.open(FSDirectory.open(new File("wrapper/index/indexFile")));
	IndexSearcher searcher = new IndexSearcher(reader);
	QueryParser qp = new QueryParser(Version.LUCENE_30, "name", getAnalyzer());
	qp.setDefaultOperator(QueryParser.AND_OPERATOR);
	qp.setAllowLeadingWildcard(true);

	Query query = null;
	try
	{
	    query = qp.parse(keyword);
	}
	catch (ParseException e)
	{
	    query = qp.parse(keyword);
	}
	System.out.println("Query:"+query);

	TopDocs topFieldDocs = searcher.search(query, N);
	ScoreDoc[] hits = topFieldDocs.scoreDocs;
	System.out.println("Hits:" + topFieldDocs.totalHits);
	for (int i = 0; i < hits.length; i++)
	{
	    org.apache.lucene.document.Document doc = searcher.doc(hits[i].doc);
	    SearchResultElement ele = new SearchResultElement();
	    ele.setServer(doc.get("server"));
	    String path = doc.get("parent")+doc.get("name");
	    ele.setPath(path);
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
	    System.out.println(ele);
	}
    }
    
    public static void main(String[] args) throws ParseException, MalformedURLException,
	    RemoteException, IOException, NotBoundException
    {
//	buildIndex();
	while (true)
	{
	    System.out.println("input query:");
	    String q = KeyboardReader.readLine().trim();
	    if (q == null || q.length() == 0 || "exit".equals(q))
		break;
	     search(q);
	}
    }
}
