/*
 * Generated by MyEclipse Struts Template path: templates/java/JavaClass.vtl
 */
package cn.askrosa.struts.form;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import searcher.SearchParameter;

/**
 * MyEclipse Struts Creation date: 03-02-2008 XDoclet definition:
 * 
 * @struts.form name="searchtrueForm"
 */
public class SearchForm extends ValidatorForm
{
    /**
     * 
     */
    private static final long serialVersionUID = 7489006234077477085L;

    /*
     * Generated Methods
     */

    /** keywordExclude property */
    private String keywordExclude="";

    private String search="rosa";
    
    /** categories property */
    private String[] categories = null;

    /** access property */
    private String access="";

    /** sever property */
    private String server="";

    private String keywordInclude="";
    
    private String dateFrom="";

    private String dateTo="";

    private String field = "name";

    private String sort = SearchParameter.DEFAULT_SORT_TYPE;

    private short[] locations = null;

    private String keyword="";
    
    public void resetall()
    {
	    this.setKeywordInclude("");
	    this.setAccess("");
	    this.setCategories(null);
	    this.setServer("");
	    this.setDateFrom("");
	    this.setDateTo("");
	    this.setField("name");
	    this.setSearch("rosa");
	    this.setKeywordExclude("");
	    this.setLocations(null);
	    this.setSort(SearchParameter.DEFAULT_SORT_TYPE);
    }
    /**
     * @return the field
     */
    public String getField()
    {
	return field;
    }

    /**
     * @param field
     *                the field to set
     */
    public void setField(String field)
    {
	this.field = field;
    }

    /**
     * Returns the keywordExclude.
     * 
     * @return String
     */
    public String getKeywordExclude()
    {
	return keywordExclude;
    }

    /**
     * Set the keywordExclude.
     * 
     * @param keywordExclude
     *                The keywordExclude to set
     */
    public void setKeywordExclude(String keywordExclude)
    {
	this.keywordExclude = keywordExclude;
    }

    /**
     * Returns the categories.
     * 
     * @return String
     */
    public String[] getCategories()
    {
	return categories;
    }

    /**
     * Set the categories.
     * 
     * @param categories
     *                The categories to set
     */
    public void setCategories(String[] categories)
    {
	this.categories = categories;
    }

    /**
     * Returns the access.
     * 
     * @return String
     */
    public String getAccess()
    {
	return access;
    }

    /**
     * Set the access.
     * 
     * @param access
     *                The access to set
     */
    public void setAccess(String access)
    {
	this.access = access;
    }

    /**
     * Returns the sever.
     * 
     * @return String
     */
    public String getServer()
    {
	return server;
    }

    /**
     * Set the sever.
     * 
     * @param sever
     *                The sever to set
     */
    public void setServer(String server)
    {
	this.server = server;
    }

    /**
     * Method reset
     * 
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
	super.reset(mapping, request);
	Enumeration<?> en = request.getParameterNames();
	boolean hasCategories = false;
	boolean hasLocations = false;
	while (en.hasMoreElements())
	{
	    String enele = en.nextElement().toString();
	    if (enele.equals("categories"))
		hasCategories = true;
	    if (enele.equals("locations"))
		hasCategories = true;
	}
	if (!hasCategories)
	    this.setCategories(null);
	if (!hasLocations)
	    this.setLocations(null);
    }

    public String getDateFrom()
    {
	return dateFrom;
    }

    public void setDateFrom(String dateFrom)
    {
	this.dateFrom = dateFrom;
    }

    public String getDateTo()
    {
	return dateTo;
    }

    public void setDateTo(String dateTo)
    {
	this.dateTo = dateTo;
    }

    public String getSort()
    {
	return sort;
    }

    public void setSort(String sort)
    {
	this.sort = sort;
    }

    public short[] getLocations()
    {
        return locations;
    }

    public void setLocations(short[] locations)
    {
        this.locations = locations;
    }

    public String getKeywordInclude()
    {
        return keywordInclude;
    }

    public void setKeywordInclude(String keywordInclude)
    {
        this.keywordInclude = keywordInclude;
    }
    public String getKeyword()
    {
        return keyword;
    }
    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }
    public String getSearch()
    {
        return search;
    }
    public void setSearch(String search)
    {
        this.search = search;
    }
}