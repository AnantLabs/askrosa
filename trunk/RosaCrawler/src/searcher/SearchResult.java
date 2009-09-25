package searcher;

import java.util.LinkedList;
import java.util.List;
/**
 * 保存搜索结果的类
 * @author elegate
 *
 */
public class SearchResult
{
    /**
     * the time-consumed for search
     */
    private long delay = 0;

    /**
     * the searched file information list
     */
    private List<SearchResultElement> searchFileList;
    /**
     * 结果总数，注意这个和上面的searchFieldList的size不同，因为每次只显示搜索结果的一部分
     * 所以list中存放的是不分结果，hitsNum表示的是所有搜索结果的总数
     */
    private int histNum;

    public SearchResult()
    {
	delay = 0;
	searchFileList = new LinkedList<SearchResultElement>();
    }

    public void setHitsNum(int num)
    {
	this.histNum = num;
    }

    public int getHistNum()
    {
	return this.histNum;
    }

    public void addSearchResultElement(SearchResultElement ele)
    {
	searchFileList.add(ele);
    }

    /**
     * @param l
     */
    public void setDelay(long l)
    {
	delay = l;
    }

    /**
     * @return delay time
     */
    public long getDelay()
    {
	return delay;
    }

    public void setResultFileList(List<SearchResultElement> list)
    {
	this.searchFileList = list;
    }

    /**
     * @return search result list
     */
    public List<SearchResultElement> getResultFileList()
    {
	return searchFileList;
    }

}
