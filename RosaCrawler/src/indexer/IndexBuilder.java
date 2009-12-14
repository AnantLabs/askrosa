/**
 * 负责建立索引的包
 */
package indexer;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import log.CrawlerLogger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.store.FSDirectory;

import resource.CategoryConfigure;
import resource.CrawlerSetting;
import utils.ElapseTimer;
import utils.Poolable;
import utils.Tools;
import analysis.RosaAnalyzer;
import crawl.FtpCrawler;
import crawlerutils.RosaCrawlerConstants;
import database.Criteria;
import database.Database;
import database.FtpSiteInfo;
import database.FtpSiteInfoPeer;
import database.Scroller;
import ftp.FTPFileInfo;

/**
 * Build index for all the ftp sites
 * 
 * @author elegate
 */
public class IndexBuilder
{

    /**
     * time out setting for index building
     */
    public static final int BUILD_INDEX_TIMEOUT = CrawlerSetting
	    .getInt("indexer.indexbuilder.timeout");

    /**
     * number of nodes
     */
    public static final int TOTAL = CrawlerSetting.getInt("node.total");

    /**
     * node id
     */
    public static final int NODE_ID = CrawlerSetting.getInt("node.id");

    /**
     * location of index file
     */
    public static final File INDEX = new File(CrawlerSetting
	    .getProperty("index"));

    /**
     * location of back of index file
     */
    public static final File INDEX_BACK = new File(CrawlerSetting
	    .getProperty("indexBack"));

    /**
     * size of thread pool
     */
    public static final int POOL_SIZE = Integer.parseInt(CrawlerSetting
	    .getProperty("indexer.indexbuilder.poolsize"));

    /**
     * memory setting for lucene
     */
    public static final double RAM_BUFFER = CrawlerSetting
	    .getDouble("org.apache.lucene.RAMbuffer");

    public static final int INVALID_PERIOD = CrawlerSetting
	    .getInt("indexer.indexbuilder.invalidateperiod");

    public static final int MAX_CRAWLINTERVAL = CrawlerSetting
	    .getInt("indexer.indexbuilder.maxcrawlinterval");

    /**
     * list of sites to be updated,subset of responsibleList
     */
    private Map<Integer, FtpSiteInfo> updateList;

    // private HashMap<Integer,FtpSiteInfo> allFtpSites;
    /**
     * sites list for this node,those in this list which meet the update
     * condition are put into updateList
     */
    private Map<Integer, FtpSiteInfo> responsibleList;

    /**
     * list of update failed site
     */
    private Map<Integer, FtpSiteInfo> updateFailure;

    /**
     * 长期未得到更新的站点被放入这个Map中
     */
    private Map<Integer, FtpSiteInfo> invalidSites;

    /**
     * 记录当前还未完成索引的站点
     */
    private List<FtpSiteInfo> unfinishedSites;

    /**
     * thread pool
     */
    private ThreadPoolExecutor pool;

    /**
     * message pool
     */
    // private Poolable<String> msgPool;
    // private IndexWriter ramWriter;
    //
    // private RAMDirectory ramDir;
//    private FSDirectory indexBackDirectory;

    private IndexWriter indexWriter;

    /**
     * logging system
     */
    // private Logger logger = Logger.getLogger(IndexBuilder.class.getName());
    public IndexBuilder()
    {
	updateList = Collections
		.synchronizedMap(new HashMap<Integer, FtpSiteInfo>());
	responsibleList = Collections
		.synchronizedMap(new HashMap<Integer, FtpSiteInfo>());
	updateFailure = Collections
		.synchronizedMap(new HashMap<Integer, FtpSiteInfo>());
	invalidSites = Collections
		.synchronizedMap(new HashMap<Integer, FtpSiteInfo>());
	unfinishedSites = Collections
		.synchronizedList(new LinkedList<FtpSiteInfo>());
    }

    /**
     * get the current time in the database host
     * 
     * @return current time
     */
    private long getTimeNowFromDB()
    {

	try
	{
	    Connection con = Database.getConnection();
	    Statement stmt;
	    stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery("select now()");
	    rs.next();
	    long now = rs.getTimestamp(1).getTime();
	    stmt.close();
	    Database.release(con);
	    return now;
	}
	catch (Exception e)
	{

	    CrawlerLogger.logger.error("get time from db failed", e);
	}
	return new Date().getTime();
    }

    /**
     * check whether it is necessary to perform update
     * 
     * @return true if some sites need to be update,false otherwise
     */
    private boolean checkForUpdate()
    {
	try
	{
	    // clear something
	    updateFailure.clear();
	    updateList.clear();
	    responsibleList.clear();
	    invalidSites.clear();
	    unfinishedSites.clear();
	    ArrayList<FtpSiteInfo> ftpSitesArr = new ArrayList<FtpSiteInfo>();
	    // update ftp sites list
	    Criteria c = new Criteria();
//	    c.add(FtpSiteInfo.SERVER, "172.16.65.79");
	    c.addAscendingOrder("totalFileCount");
	    Scroller<FtpSiteInfo> scr = FtpSiteInfoPeer.doSelect(c);
	    while (scr.hasNext())
	    {
		FtpSiteInfo info = (FtpSiteInfo) scr.next();
		ftpSitesArr.add(info);
	    }
	    int id = NODE_ID - 1;
	    long now = getTimeNowFromDB();
	    CrawlerLogger.logger.info("<Queue>");
	    for (FtpSiteInfo info : ftpSitesArr)
	    {
		if (info.getId() % TOTAL == id)
		{
		    responsibleList.put(info.getId(), info);
		    long diff = now - info.getUpdateTime().getTime();
		    if (diff > RosaCrawlerConstants.ONE_DAY_IN_MILLISECOND
			    * info.getCrawlInterval()
			    || info.getTotalFileCount() <= 0)
		    {

			updateList.put(info.getId(), info); //需要更新的站点
			unfinishedSites.add(info);          //还未完成的站点
			CrawlerLogger.logger.info("----->[ " + info.getId()
				+ ": " + info.getServer() + " "
				+ info.getUsername() + " ]");
			if (diff >= RosaCrawlerConstants.ONE_DAY_IN_MILLISECOND
				* INVALID_PERIOD)
			{
			    invalidSites.put(info.getId(), info);//多次更新失败的站点置为无效站点
			}
		    }
		}
	    }
	    CrawlerLogger.logger.info("</Queue>");
	    CrawlerLogger.logger.info("Checked " + responsibleList.size()
		    + " sites, update " + updateList.size() + " of them");
	    if (updateList.size() == 0)
		return false;
	}
	catch (Exception e)
	{
	    CrawlerLogger.logger.error("check for update failed", e);
	    return false;
	}
	return true;
    }

    /**
     * preparation for updating
     * 
     * @throws IOException
     *             exception thrown
     */
    private void prepare() throws IOException
    {
	checkWriteLock();
	// remove original index
	removeOriginalIndex(INDEX_BACK);
	// open writers
	indexWriter = new IndexWriter(FSDirectory.open(INDEX_BACK), getAnalyzer(),
		!IndexReader.indexExists(FSDirectory.open(INDEX_BACK)),
		IndexWriter.MaxFieldLength.UNLIMITED);
	indexWriter.setRAMBufferSizeMB(RAM_BUFFER);
	pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(POOL_SIZE);
    }

    private void checkWriteLock()
    {
	File indexLock = new File(INDEX.getAbsoluteFile() + "/write.lock");
	File backLock = new File(INDEX_BACK.getAbsoluteFile() + "/write.lock");
	if (backLock.exists())
	{
	   backLock.delete();
	}
	if (indexLock.exists())
	{
	    indexLock.delete();
	}
    }

    /**
     * delete the old index
     * 
     * @param dir
     *            the location of index file
     */
    private void removeOriginalIndex(File dir)
    {
	FSDirectory indexDir;
	IndexReader reader;
	try
	{
	    indexDir = FSDirectory.open(dir);
	    if (!IndexReader.indexExists(indexDir))
		return;
	    reader = IndexReader.open(indexDir,false);
	    int cnt = 0;
	    int numDocs = reader.numDocs();
	    Set<String> set = new HashSet<String>();
	    for (int i = 0; i < numDocs; i++)
	    {
		if (!reader.isDeleted(i))
		{
		    Document doc = reader.document(i);
		    int id = Integer.parseInt(doc.get("id"));
		    String server = doc.get("server");
		    if ((updateList.containsKey(id) && !updateFailure
			    .containsKey(id))
			    || !responsibleList.containsKey(id))
		    {

			reader.deleteDocument(i);
			cnt++;
			set.add(server);
		    }
		}
	    }
	    CrawlerLogger.logger.info("Delete " + cnt
		    + " original documents from " + dir + " for " + set);
	    reader.close();
	}
	catch (Exception e)
	{
	    CrawlerLogger.logger.error("remove original index failed", e);
	}
    }

    /**
     * call this to build index
     * 
     * @return the number of sites which are successfully updated
     */
    public int buildIndex()
    {
	if (!checkForUpdate())
	    return 0;
	// prepare for indexing
	try
	{
	    prepare();
	}
	catch (Exception e)
	{
	    CrawlerLogger.logger.error("prepare failed", e);
	    return 0;
	}

//	setProxy();

	for (FtpSiteInfo info : updateList.values())
	{

	    BuildSiteIndexTarget target = new BuildSiteIndexTarget(info);
	    pool.execute(target);
	}
	// finish indexing
	finish();
	return updateList.size() - updateFailure.size();
    }

//    private void setProxy()
//    {
//	if (CrawlerSetting.getBoolean("proxy.useproxy"))
//	{
//	    ArrayList<String> common = new ArrayList<String>();
//	    common.add("smtp.gmail.com");
//	    ArrayList<String> education = new ArrayList<String>();
//	    for (FtpSiteInfo ftp : responsibleList.values())
//	    {
//		if (ftp.getLocation() == FtpSiteInfo.EDUCATION_SITE)
//		    education.add(ftp.getServer());
//	    }
//	    Authenticator au = new RosaAuthenticator();
//	    Authenticator.setDefault(au);
//	    RosaProxySelector ps = new RosaProxySelector(ProxySelector
//		    .getDefault(), education, common);
//	    ProxySelector.setDefault(ps);
//	}
//    }

    /**
     * wait for the index building proecess
     */
    private void await()
    {
	pool.shutdown();
	try
	{
	    pool.awaitTermination(BUILD_INDEX_TIMEOUT, TimeUnit.HOURS);
	}
	catch (Exception e)
	{
	    CrawlerLogger.logger.error("await failed", e);
	}

    }

    /**
     * rollback the sites which are not successfully updated
     */
    private void rollbackFailure()
    {
	IndexReader reader;
	try
	{
	    // if the original index file doesn't exist,just skip rollback
	    FSDirectory indexDir = FSDirectory.open(INDEX);
	    if (!IndexReader.indexExists(indexDir))
		return;

	    reader = IndexReader.open(FSDirectory.open(INDEX_BACK),false);
	    for (FtpSiteInfo info : updateFailure.values())
	    {
		Term term = new Term("id", info.getId() + "");
		reader.deleteDocuments(term);
	    }
	    reader.close();

	    IndexWriter writer = new IndexWriter(FSDirectory.open(INDEX_BACK),
		    getAnalyzer(), false, IndexWriter.MaxFieldLength.UNLIMITED);
	    reader = IndexReader.open(indexDir);
	    for (FtpSiteInfo info : updateFailure.values())
	    {
		CrawlerLogger.logger.info("Rollback " + info.getServer() + " "
			+ info.getUsername());
		Term term = new Term("id", info.getId() + "");
		TermDocs td = reader.termDocs(term);
		while (td.next())
		{
		    Document doc = reader.document(td.doc());
		    writer.addDocument(doc);
		}
	    }
	    reader.close();
	    writer.optimize();
	    writer.close();
	}
	catch (CorruptIndexException e)
	{
	    CrawlerLogger.logger.error("rollback failed", e);
	}
	catch (Exception e)
	{
	    CrawlerLogger.logger.error("rollback failed", e);
	}
    }

    /**
     * finish update process
     */
    private void finish()
    {
	CrawlerLogger.logger.info("Finishing indexing...");

	await();

	closeWriter();

	removeInvalidSites(INDEX);

	rollbackFailure();

	updateIndex();
	
	updateDatabase();
    }
    
    private void updateDatabase()
    {
	for(FtpSiteInfo site:updateList.values())
	{
	    try
	    {
		site.save();
	    }
	    catch (Exception e)
	    {
		CrawlerLogger.logger.error("Failed to save "+site.getFtpAddressURL(), e);
	    }
	}
    }

    /**
     * 删除无效站点的索引，所谓无效站点就是长期没有正常更新的站点，这边设置为20天
     * 
     * @param index
     */
    private void removeInvalidSites(File index)
    {
	IndexReader reader;
	try
	{
	    // if the original index file doesn't exist,just skip rollback
	    FSDirectory indexDir = FSDirectory.open(index);
	    if (!IndexReader.indexExists(indexDir))
		return;
	    reader = IndexReader.open(indexDir,false);
	    Set<String> set = new HashSet<String>();
	    for (FtpSiteInfo info : invalidSites.values())
	    {
		Term term = new Term("id", info.getId() + "");
		reader.deleteDocuments(term);
		info.setTotalFileCount(-1);
		if (info.getCrawlInterval() < MAX_CRAWLINTERVAL)
		    info.setCrawlInterval(info.getCrawlInterval() + 1);
		//当站点已经无效后尝试下次改为使用ls -AlR命令爬行，因为手动递归往往会失败
		info.setRecursive(FtpSiteInfo.LS_ALR_RECURSIVE);
		info.save();
		set.add(info.getServer());
	    }
	    reader.close();
	    CrawlerLogger.logger.info("Invalidate sites: " + set);
	}
	catch (Exception e)
	{
	    CrawlerLogger.logger.error("remove invalid sites failed", e);
	}
    }

    /**
     * update the index current using by RMI,the index file is used when
     * performing update is the backup of index file,when updating done we use
     * the backup file to update the current using index in a flash
     */
    private void updateIndex()
    {
	// copy new index to index directory
	try
	{
	    CrawlerLogger.logger.info("Update index...");
	    FSDirectory back = FSDirectory.open(INDEX_BACK);
	    FSDirectory index = FSDirectory.open(INDEX);

	    removeOriginalIndex(INDEX);

	    IndexWriter writer = new IndexWriter(index, getAnalyzer(),
		    !IndexReader.indexExists(index),
		    IndexWriter.MaxFieldLength.UNLIMITED);
	    writer.setRAMBufferSizeMB(RAM_BUFFER);
	    IndexReader backReader = IndexReader.open(back);
	    int cnt = 0;
	    for (FtpSiteInfo info : updateList.values())
	    {
		if (!updateFailure.containsKey(info.getId()))
		{
		    Term term = new Term("id", info.getId() + "");
		    TermDocs td = backReader.termDocs(term);
		    while (td.next())
		    {
			Document doc = backReader.document(td.doc());
			writer.addDocument(doc);
			cnt++;
		    }
		}
	    }
	    CrawlerLogger.logger.info("Update " + cnt + " files");
	    writer.optimize();
	    writer.close();
	    backReader.close();
	    CrawlerLogger.logger.info("Update index done");
	}
	catch (Exception e)
	{
	    CrawlerLogger.logger.error("update index failed", e);
	}
    }

    /**
     * get the analyzer
     * 
     * @return analyzer
     */
    private Analyzer getAnalyzer()
    {
	return new RosaAnalyzer();
    }

    private void closeWriter()
    {
	try
	{
	    if (indexWriter != null)
	    {
		indexWriter.optimize();
		indexWriter.close();
	    }
	}
	catch (CorruptIndexException e)
	{
	    CrawlerLogger.logger.error("close writer failed", e);
	}
	catch (Exception e)
	{
	    CrawlerLogger.logger.error("close writer failed", e);
	}
    }

    /**
     * build index for a single ftp site
     * 
     * @author elegate
     */
    private class BuildSiteIndexTarget extends Thread implements
	    Poolable<FTPFileInfo>
    {
	// private FtpIndex ftpIndex = null;

	private FtpCrawler ftpCrawler = null;

	private FtpSiteInfo ftpSite;

	private String updateTime;

	public BuildSiteIndexTarget(FtpSiteInfo ftpSite)
	{
	    this.ftpSite = ftpSite;
	    // ftpIndex = new FtpIndex();
	    ftpCrawler = new FtpCrawler(ftpSite);
	}

	public String toString()
	{
	    return ftpSite.getServer();
	}

	@Override
	public void run()
	{
	    CrawlerSetting.setProxy();
	    updateTime= CrawlerSetting.FTP_FILE_INFO_DATE_FORMAT.format(new Date());
	    boolean ret = false;
	    ret = ftpCrawler.crawl(this);
	    int count = ftpCrawler.getCount();
	    if (!ret && (count <=0 || count < ftpSite.getTotalFileCount()*0.9) )//有些站点虽然没有完整更新，但成功获取的文件数量十分可观的时候不认为爬行失败
	    {
		CrawlerLogger.logger.info("Failed indexing "
			+ ftpSite.getServer() + " " + ftpSite.getUsername()
			+ " : " + ftpCrawler.getCount() + " files in all");
		updateFailure.put(ftpSite.getId(), ftpSite); //更新失败，将站点放入失败列表中
	    }
	    else
	    {
		invalidSites.remove(ftpSite.getId()); //站点更新成功，将其从失效站点中删除
		CrawlerLogger.logger.info("Finish indexing "
			+ ftpSite.getServer() + " " + ftpSite.getUsername()
			+ " : " + ftpCrawler.getCount() + " files in all");
		ftpSite.setLastUpdateTime(ftpSite.getUpdateTime());
		ftpSite.setUpdateTime(null);
		ftpSite.setTotalFileCount(ftpCrawler.getCount());
		for (Object obj : CategoryConfigure.getAllCategories())
		{
		    String category = ((String) obj).toLowerCase();
		    String method = "set"
			    + category.substring(0, 1).toUpperCase()
			    + category.substring(1);
		    try
		    {
			Tools.callMethod(ftpSite, method, new Class<?>[]
			{ int.class }, ftpCrawler.getCategoryCount(category));
		    }
		    catch (Exception e)
		    {
			CrawlerLogger.logger.error("index " + ftpSite
				+ " failed", e);
		    }
		}
//		try
//		{
//		    ftpSite.save();
//		}
//		catch (Exception e)
//		{
//		    CrawlerLogger.logger.error("index " + ftpSite + " failed",
//			    e);
//		}
	    }
	    unfinishedSites.remove(ftpSite);         //从未完成站点中删除
	    String taskMsg = "CompleteTaskCount:"
		    + (updateList.size() - unfinishedSites.size())
		    + ", TaskQueue(" + unfinishedSites.size() + ")"
		    + unfinishedSites;
	    CrawlerLogger.logger.info(taskMsg);
	}

	public boolean pool(FTPFileInfo obj)
	{
	    if (indexWriter != null)
	    {
		Document doc = new Document();
		Field.Store store = Field.Store.YES;

		Field idField = new Field("id", ftpSite.getId() + "", store,
			Field.Index.NOT_ANALYZED, Field.TermVector.NO);

		// index address
		Field siteField = new Field("server", ftpSite.getServer(),
			store, Field.Index.NOT_ANALYZED, Field.TermVector.NO);

		Field usernameField = new Field("username", ftpSite
			.getUsername(), store, Field.Index.NOT_ANALYZED,
			Field.TermVector.NO);

		Field passwordField = new Field("password", ftpSite
			.getPassword(), store, Field.Index.NOT_ANALYZED,
			Field.TermVector.NO);

		Field portField = new Field("port", ftpSite.getPort() + "",
			store, Field.Index.NOT_ANALYZED, Field.TermVector.NO);

		Field accessField = new Field("access", ftpSite.getAccess(),
			store, Field.Index.NOT_ANALYZED, Field.TermVector.NO);
		Field locationField = new Field("location",ftpSite.getLocation()+"",store, Field.Index.NOT_ANALYZED, Field.TermVector.NO);
		// index name(full path)
		Field pathField = new Field("path", obj.getPath(), store,
			Field.Index.ANALYZED, Field.TermVector.NO);

		Field parentPathField = new Field("parent",
			obj.getParentPath(), store, Field.Index.NOT_ANALYZED,
			Field.TermVector.NO);

		Field nameField = new Field("name", obj.getName(), store,
			Field.Index.ANALYZED, Field.TermVector.NO);

		// index file modified time
		Field dateField = new Field("date", obj.getDate(), store,
			Field.Index.NOT_ANALYZED, Field.TermVector.NO);

		// index file size but not index
		Field sizeField = new Field("size", obj.getSize() + "", store,
			Field.Index.NOT_ANALYZED, Field.TermVector.NO);

		// store file attribute but not index
		Field categoryField = new Field("cat", obj.getCategory(),
			store, Field.Index.NOT_ANALYZED, Field.TermVector.NO);

		Field extensionField = new Field("ext", obj.getExtension(),
			store, Field.Index.NOT_ANALYZED, Field.TermVector.NO);

		Field updateTimeField = new Field("updateTime", updateTime, store,
			Field.Index.NOT_ANALYZED, Field.TermVector.NO);

		doc.add(idField);
		doc.add(siteField);
		doc.add(usernameField);
		doc.add(passwordField);
		doc.add(portField);
		doc.add(accessField);
		doc.add(locationField);
		doc.add(pathField);
		doc.add(parentPathField);
		doc.add(nameField);
		doc.add(dateField);
		doc.add(sizeField);
		doc.add(categoryField);
		doc.add(extensionField);
		doc.add(updateTimeField);
		try
		{
		    indexWriter.addDocument(doc);
		}
		catch (CorruptIndexException e)
		{
		    CrawlerLogger.logger.error("add document failed", e);
		    return false;
		}
		catch (Exception e)
		{
		    CrawlerLogger.logger.error("add document failed", e);
		    return false;
		}
		return true;
	    }
	    return false;
	}
	@Override
	public void close()
	{
	    
	}
    }

    /**
     * test main function
     * 
     * @param args
     *            arguments
     */
    public static void main(String[] args)
    {
	ElapseTimer t = new ElapseTimer();
	t.begin();
	IndexBuilder builder = new IndexBuilder();
	builder.buildIndex();
	try
	{
	    t.finish();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
}
