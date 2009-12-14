package crawl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import log.CrawlerLogger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

import analysis.RosaAnalyzer;
import resource.CrawlerSetting;
import database.FtpSiteInfo;
import ftp.FTPFileInfo;
import utils.Poolable;
/**
 * for testing
 * @author elegate
 *
 */
public class IndexTestPool implements Poolable<FTPFileInfo>
{
    private IndexWriter writer;

    private FtpSiteInfo ftpSite;
    private String updateTime= CrawlerSetting.FTP_FILE_INFO_DATE_FORMAT.format(new Date());
    public IndexTestPool(String dir, FtpSiteInfo ftpSite)
    {
	this.ftpSite = ftpSite;
	FSDirectory directory;
	try
	{
	    directory = FSDirectory.open(new File(dir));
	    writer = new IndexWriter(directory, new RosaAnalyzer(),!IndexReader.indexExists(directory),IndexWriter.MaxFieldLength.UNLIMITED);
	}
	catch (CorruptIndexException e)
	{
	    e.printStackTrace();
	}
	catch (LockObtainFailedException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    public void close()
    {
	try
	{
	    writer.optimize();
	    writer.close();
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

    public boolean pool(FTPFileInfo obj)
    {
	if (writer != null)
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
		
		Field identifierField = new Field("identifier",ftpSite.getFtpAddressURL()+obj.getPath(),store,Field.Index.NOT_ANALYZED, Field.TermVector.NO);
		
//		Field pathField = new Field("path", obj.getPath(), store,
//			Field.Index.NOT_ANALYZED, Field.TermVector.NO);

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

		Field updateTimeField = new Field("updatetime", updateTime, store,
			Field.Index.NOT_ANALYZED, Field.TermVector.NO);

		doc.add(idField);
		doc.add(siteField);
		doc.add(usernameField);
		doc.add(passwordField);
		doc.add(portField);
		doc.add(accessField);
		doc.add(locationField);
		doc.add(identifierField);
		doc.add(parentPathField);
		doc.add(nameField);
		doc.add(dateField);
		doc.add(sizeField);
		doc.add(categoryField);
		doc.add(extensionField);
		doc.add(updateTimeField);
		try
		{
		    System.out.println("identifier:"+ftpSite.getFtpAddressURL()+obj.getPath());
		    writer.updateDocument(new Term("identifier",ftpSite.getFtpAddressURL()+obj.getPath()), doc);
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
}
