/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package cn.askrosa.struts.form;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import database.FtpFileCount;

/** 
 * MyEclipse Struts
 * Creation date: 03-09-2009
 * 
 * XDoclet definition:
 * @struts.form name="articleListForm"
 */
public class FtpSiteListForm extends ValidatorForm {
	/*
	 * Generated fields
	 */

	/**
     * 
     */
    private static final long serialVersionUID = -55969730676779183L;

	/** author property */
	private String author;

	/** dateForm property */
	private String dateFrom="";

	/** dateTo property */
	private String dateTo="";

	/** keyword property */
	private String ftpAddress;
	
	private short location=-1; 
	
	private String sort;
	
	ArrayList<FtpFileCount> ftpFileCountList=null;	
	int totalFtpCount = 0;
	long totalFileCount = 0;	
	
	
	/*
	 * Generated Methods
	 */

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}

	/** 
	 * Returns the author.
	 * @return String
	 */
	public String getAuthor() {
		return author;
	}

	/** 
	 * Set the author.
	 * @param author The author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/** 
	 * Returns the dateTo.
	 * @return String
	 */
	public String getDateTo() {
		return dateTo;
	}

	/** 
	 * Set the dateTo.
	 * @param dateTo The dateTo to set
	 */
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getFtpAddress()
	{
	    return ftpAddress;
	}

	public void setFtpAddress(String ftpAddress)
	{
	    this.ftpAddress = ftpAddress;
	}

	public String getDateFrom()
	{
	    return dateFrom;
	}

	public void setDateFrom(String dateFrom)
	{
	    this.dateFrom = dateFrom;
	}

	public short getLocation()
	{
	    return location;
	}

	public void setLocation(short location)
	{
	    this.location = location;
	}

	public String getSort()
	{
	    return sort;
	}

	public void setSort(String sort)
	{
	    this.sort = sort;
	}

	public ArrayList<FtpFileCount> getFtpFileCountList()
	{
	    return ftpFileCountList;
	}

	public void setFtpFileCountList(ArrayList<FtpFileCount> ftpFileCountList)
	{
	    this.ftpFileCountList = ftpFileCountList;
	}

	public int getTotalFtpCount()
	{
	    return totalFtpCount;
	}

	public void setTotalFtpCount(int totalFtpCount)
	{
	    this.totalFtpCount = totalFtpCount;
	}

	public long getTotalFileCount()
	{
	    return totalFileCount;
	}

	public void setTotalFileCount(long totalFileCount)
	{
	    this.totalFileCount = totalFileCount;
	}

}