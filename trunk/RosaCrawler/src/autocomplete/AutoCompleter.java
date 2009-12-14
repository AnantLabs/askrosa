package autocomplete;

import indexer.IndexBuilder;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.HashMap;
import java.util.Map;

import log.CrawlerLogger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import resource.CrawlerSetting;
import utils.KeyboardReader;
import utils.Pair;
import analysis.RosaAutoCompelerAnalyzer;
import database.Criteria;
import database.QueryStatistics;
import database.QueryStatisticsPeer;
import database.Scroller;

/**
 * Search term auto-completer, works for single terms (so use on the last term
 * of the query).
 * <p>
 * Returns more popular terms first.
 * 
 * @author Mat Mannion, M.Mannion@warwick.ac.uk
 */
public final class AutoCompleter
{

    private static final String GRAMMED_WORDS_FIELD = "words";

    private static final String SOURCE_WORD_FIELD = "sourceWord";

    private static final String COUNT_FIELD = "count";

    private static final String HITS_FIELD = "hits";

    private final Directory autoCompleteDirectory;

    private IndexReader autoCompleteReader;

    private IndexSearcher autoCompleteSearcher;

    public AutoCompleter(String autoCompleteDir) throws Exception
    {
	this.autoCompleteDirectory = FSDirectory.open(new File(autoCompleteDir));

	reOpenReader();
    }

    public Map<String, Integer> suggestTermsFor(String term) throws IOException, ParseException,
	    NotBoundException
    {
	QueryParser qp = new QueryParser(Version.LUCENE_CURRENT, GRAMMED_WORDS_FIELD, getAnalyzer());
	qp.setDefaultOperator(QueryParser.OR_OPERATOR);
	qp.setAllowLeadingWildcard(true);

	SortField[] sortFields = new SortField[]
	{ SortField.FIELD_SCORE, new SortField(COUNT_FIELD, SortField.INT, true) };
	Sort sort = new Sort(sortFields);
	Query query = qp.parse(term);
	TopDocs docs = autoCompleteSearcher.search(query, null, CrawlerSetting
		.getInt("autocomplete.rows"),sort);
	Map<String, Integer> suggestions = new HashMap<String, Integer>();
	for (ScoreDoc doc : docs.scoreDocs)
	{
	    String word = autoCompleteReader.document(doc.doc).get(SOURCE_WORD_FIELD);
	    String hits = autoCompleteReader.document(doc.doc).get(HITS_FIELD);
	    suggestions.put(word, Integer.parseInt(hits));
	}
	return suggestions;
    }

    public static Analyzer getAnalyzer()
    {
	return new RosaAutoCompelerAnalyzer();
    }

    public void reIndex() throws Exception
    {
	// build a dictionary (from the spell package)
	Criteria c = new Criteria();
	Scroller<QueryStatistics> scro = QueryStatisticsPeer.doSelect(c);

	// use a custom analyzer so we can do EdgeNGramFiltering
	IndexWriter writer = new IndexWriter(autoCompleteDirectory, getAnalyzer(), true,
		IndexWriter.MaxFieldLength.UNLIMITED);
	writer.setRAMBufferSizeMB(IndexBuilder.RAM_BUFFER);

//	writer.setMergeFactor(300);
//	writer.setMaxBufferedDocs(150);

	// go through every word, storing the original word (incl. n-grams)
	// and the number of times it occurs
	// word count hits
	Map<String, Pair<Integer, Integer>> wordsMap = new HashMap<String, Pair<Integer, Integer>>();
	while (scro.hasNext())
	{
	    QueryStatistics qs = scro.next();
	    String word = qs.getKeyword();
	    int len = word.length();
	    if (len < 2)
	    {
		continue;
	    }

	    if (wordsMap.containsKey(word))
	    {
		Pair<Integer, Integer> p = wordsMap.get(word);
		p.x++;
		p.y = qs.getHits();
	    }
	    else
	    {
		Pair<Integer, Integer> p = new Pair<Integer, Integer>(1, qs.getHits());
		wordsMap.put(word, p);
	    }
	}
	for (String word : wordsMap.keySet())
	{
	    Pair<Integer, Integer> p = wordsMap.get(word);
	    // ok index the word
	    Document doc = new Document();
	    doc.add(new Field(SOURCE_WORD_FIELD, word, Field.Store.YES, Field.Index.NOT_ANALYZED)); // orig
	    // term
	    doc.add(new Field(GRAMMED_WORDS_FIELD, word, Field.Store.YES, Field.Index.ANALYZED)); // grammed
	    doc.add(new Field(COUNT_FIELD, p.x + "", Field.Store.YES, Field.Index.NOT_ANALYZED)); // count
	    doc.add(new Field(HITS_FIELD, p.y + "", Field.Store.YES, Field.Index.NOT_ANALYZED)); // hits
	    writer.addDocument(doc);
	}

	// close writer
	writer.optimize();
	writer.close();

	// re-open our reader
	reOpenReader();
	
	CrawlerLogger.logger.info("AutoCompleter reindexed "+wordsMap.size()+" keywords into the dictionary");
    }

    private void reOpenReader() throws CorruptIndexException, Exception
    {
	if (autoCompleteReader == null)
	{
	    if (!IndexReader.indexExists(autoCompleteDirectory))
	    {
		IndexWriter w = new IndexWriter(autoCompleteDirectory,null,true,IndexWriter.MaxFieldLength.UNLIMITED);
		w.optimize();
		w.close();
	    }
	    autoCompleteReader = IndexReader.open(autoCompleteDirectory);
	}
	else
	{
	    autoCompleteReader.reopen();
	}

	autoCompleteSearcher = new IndexSearcher(autoCompleteReader);
    }

    public static void main(String[] args) throws Exception
    {
	AutoCompleter ac = new AutoCompleter("wrapper/index/indexFile");
//	ac.reIndex();
	while (true)
	{
	    System.out.println("input word:");
	    String word = KeyboardReader.readLine();
	    System.out.println("Querying similar for " + word);
	    Map<String, Integer> sims = ac.suggestTermsFor(word);
	    System.out.println(sims);
	}
    }
}
