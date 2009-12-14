package searcher;

import java.io.Serializable;

import resource.CrawlerSetting;
import crawlerutils.RosaCrawlerConstants;
import database.FtpSiteInfo;

/**
 * 用于保存搜索参数的类
 * @author elegate
 *
 */
public class SearchParameter implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -7001570804645832128L;
    
    public static final String DEFAULT_SORT_TYPE="datedesc";
    /**
     * 关键字
     */
    private String keyword = "";
    /**
     * 排序类型
     */
    private String sortType = DEFAULT_SORT_TYPE;
    /**
     * 访问控制
     */
    private String access = "anybody";
    /**
     * 用户IP
     */
    private String remoteIP;
    /**
     * 搜索域，默认为"name"
     */
    private String field="name";
    /**
     * 搜索结果中提取位置
     */
    private int begin;
    /**
     * 从搜索结果中提取的文件信息条数
     */
    private int count = RosaCrawlerConstants.SEARCH_PAGE_SIZE;
    /**
     * 分类过滤选择
     */
    private String[] categories; 
    /**
     * 索引时效性约束
     */
    
    private short[] locations = new short[]{FtpSiteInfo.CAMPUS_SITE};
    
    private String extensions = null;  //文件后缀查询，后缀用逗号分割，小写
    
//    private int recentResourceModifiedConstraint = -1;
    
    private int recentUpdateConstraint = CrawlerSetting.getInt("searcher.recentupdateconstraint"); //updated in xx days
    
//    public int getRecentResourceModifiedConstraint()
//    {
//        return recentResourceModifiedConstraint;
//    }
//
//    public void setRecentResourceModifiedConstraint(int recentResourceModifiedConstraint)
//    {
//        this.recentResourceModifiedConstraint = recentResourceModifiedConstraint;
//    }

    public short[] getLocations()
    {
        return locations;
    }

    public void setLocations(short[] locations)
    {
        this.locations = locations;
    }

   

    /**
     * @return the recentUpdateConstraint
     */
    public int getRecentUpdateConstraint()
    {
        return recentUpdateConstraint;
    }

    /**
     * @param recentUpdateConstraint the recentUpdateConstraint to set
     */
    public void setRecentUpdateConstraint(int recentUpdateConstraint)
    {
        this.recentUpdateConstraint = recentUpdateConstraint;
    }

    public void setCategories(String[] c)
    {
	this.categories = c;
    }

    public String[] getCategories()
    {
	return this.categories;
    }

    public void setKeyword(String keyword)
    {
	this.keyword = keyword;
    }

    public String getKeyword()
    {
	return this.keyword;
    }

    public int getBegin()
    {
	return this.begin;
    }

    public void setBegin(int begin)
    {
	this.begin = begin;
    }

    public int getCount()
    {
	return this.count;
    }

    public void setCount(int count)
    {
	this.count = count;
    }

    public void setRemoteIP(String ip)
    {
	this.remoteIP = ip;
    }

    public String getRemoteIP()
    {
	return this.remoteIP;
    }

    public void setAccess(String acc)
    {
	this.access = acc;
    }

    public String getAccess()
    {
	return this.access;
    }

    public void setSortType(String sortType)
    {
	this.sortType = sortType;
    }

    public String getSortType()
    {
	return this.sortType;
    }

    public String toString()
    {
	return this.keyword + "," + this.sortType + "," + this.begin + ","
		+ this.count + "," + access + "," + this.getRemoteIP();
    }

    /**
     * @return the field
     */
    public String getField()
    {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field)
    {
        this.field = field;
    }

    public String getExtensions()
    {
        return extensions;
    }

    public void setExtensions(String extensions)
    {
        this.extensions = extensions;
    }
}
