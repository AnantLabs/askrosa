/*
 * Generated by MyEclipse Struts Template path: templates/java/JavaClass.vtl
 */
package cn.askrosa.struts.action;

import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import searcher.FtpSearch;

import database.QueryStatisticsResult;
import database.QueryStatisticsResultPeer;
import database.ResourceRequestPeer;
import database.Scroller;
import database.FtpSiteInfo;
import database.FtpSitesManager;

/**
 * MyEclipse Struts Creation date: 06-01-2008 XDoclet definition:
 * 
 * @struts.action validate="true"
 * @struts.action-forward name="success" path="/index.jsp"
 */
public class WelcomemainAction extends Action
{
    /*
     * Generated Methods
     */

    /**
     * Method execute
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */

    public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response)
    {
	try
	{
	    final int pageSize = 10;
	    int page = 1;
	    int from = (page - 1) * 30;

	    request.setAttribute("listing", ResourceRequestPeer.listPagedResourceRequest(null,
		    from, pageSize, null, null, "time", (short) 0));
	    // 计算热门搜索
	    // Scroller<QueryStatisticsResult> statisticsResult =
	    // QueryStatisticsResultPeer
	    // .weeklyStatistics(10);
	    Scroller<QueryStatisticsResult> statisticsResult = QueryStatisticsResultPeer
		    .statisticsDaysAgo(1, 10);
	    // 计算热门站点
	    Scroller<FtpSiteInfo> hotFtpSite = FtpSitesManager.hotFtpSites(6);
	    Map<String, String> newres = FtpSearch.getRecentModifiedResource(new String[]
	    { "video" }, 3, 14);
	    String[] newreslist = new String[newres.size() + 1];
	    String[] newresKeywordsList = new String[newres.size() + 1];
	    int i = 0;
	    for (Map.Entry<String, String> entry : newres.entrySet())
	    {
		newreslist[i] = entry.getKey();
		newresKeywordsList[i] = "search.do?keyword="
			+ java.net.URLEncoder.encode(entry.getValue(), "UTF-8");
		i++;
	    }
	    Calendar c = Calendar.getInstance();
	    int today = c.get(Calendar.YEAR) * 10000 + (c.get(Calendar.MONTH) + 1) * 100
		    + c.get(Calendar.DATE);
	    int dateFrom=today-2;
	   
	    String todayString = Integer.toString(dateFrom);
	    newreslist[newres.size()] = "<img border=0 src=\"images/righttag.gif\" > More...";
	    newresKeywordsList[newres.size()] = "search.do?newRes=" + todayString;
	    request.setAttribute("statisticsResult", statisticsResult);
	    request.setAttribute("hotFtpSite", hotFtpSite);
	    request.setAttribute("newres", newreslist);
	    request.setAttribute("newresKeywords", newresKeywordsList);
	    return mapping.findForward("success");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return mapping.getInputForward();
    }
}