package indexmanage;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

import utils.ElapseTimer;

import analysis.RosaAnalyzer;

public class IndexVersionConvert
{
    public static void main(String args[])
    {
	try
	{
	    ElapseTimer t = new ElapseTimer();
		t.begin();
	    IndexReader reader = IndexReader.open(FSDirectory.open(new File("index/indexFileBack")));
	    IndexWriter fsWriter = new IndexWriter(FSDirectory.open(new File("index/indexConvert")), new RosaAnalyzer(),
			true,
			IndexWriter.MaxFieldLength.UNLIMITED);
	    fsWriter.setRAMBufferSizeMB(100);
	    int num = reader.numDocs();
	    for(int i=0;i<num;i++)
	    {
		Document doc = reader.document(i);
		Field f = doc.getField("updateTime");
		 Calendar c = Calendar.getInstance();
		    String now = DateTools.dateToString(c.getTime(), DateTools.Resolution.DAY);
		if(f!=null)
		{
		   
		    f.setValue(now);
		}
		else
		{
		    Field updateTimeField = new Field("updateTime", now, Field.Store.YES,
				Field.Index.NOT_ANALYZED, Field.TermVector.NO);
		    doc.add(updateTimeField);
		}
		fsWriter.addDocument(doc);
	    }
	    reader.close();
	    fsWriter.optimize();
	    fsWriter.close();
	    t.finish();
	}
	catch (CorruptIndexException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
