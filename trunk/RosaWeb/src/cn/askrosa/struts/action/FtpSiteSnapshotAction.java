package cn.askrosa.struts.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import cn.askrosa.others.FtpSiteSnapshotPath;
import cn.askrosa.struts.form.FtpSiteSnapshotForm;
import database.Criteria;
import database.FtpSiteInfo;
import database.FtpSiteInfoPeer;
import database.FtpSitesManager;
import database.Scroller;

import searcher.FtpSearch;
import searcher.SearchParameter;
import searcher.SearchResult;
import searcher.SearchResultElement;

/**
 * MyEclipse Struts Creation date: 03-05-2009 XDoclet definition:
 * 
 * @struts.action validate="true"
 */

public class FtpSiteSnapshotAction extends Action
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
	    HttpServletRequest request, HttpServletResponse response) throws Exception
    {
	FtpSiteSnapshotForm ftpSiteSnapshotForm = (FtpSiteSnapshotForm) form;
	String server = request.getParameter("server");
	String username = request.getParameter("username");
	String preServer = ftpSiteSnapshotForm.getPreServer();
	String preUsername = ftpSiteSnapshotForm.getPreUsername();
	boolean ftpInfoNew = false;
	if (!server.equals(preServer))
	{
	    ftpInfoNew = true;
	    ftpSiteSnapshotForm.setPreServer(server);
	}
	if (username == null)
	{
	    username = preUsername;
	    ftpInfoNew = true;
	}
	else if (!username.equals(preUsername))
	{
	    ftpSiteSnapshotForm.setPreUsername(username);
	    ftpInfoNew = true;
	}
	// 得到站点的信息
	FtpSitesManager.hotAdd(server, 1);
	if (ftpInfoNew)
	{
	    Criteria crit = new Criteria();
	    crit.add(FtpSiteInfo.SERVER, server.trim());
	    crit.add(FtpSiteInfo.USERNAME, username.trim());
	    Scroller<FtpSiteInfo> results = FtpSiteInfoPeer.doSelect(crit);
	    if (!results.hasNext())
		return mapping.getInputForward();
	    FtpSiteInfo ftpSite = (FtpSiteInfo) results.next();

	    ftpSiteSnapshotForm.setFtpInfo("FTP信息：" + ftpSite.getDescription() + "<br>站点位置："
		    + ftpSite.getLocationString() + "<br>端口：" + ftpSite.getPort()
		    + "&nbsp;&nbsp;&nbsp;用户名：" + ftpSite.getUsername() + "&nbsp;&nbsp;&nbsp;密码："
		    + ftpSite.getPassword() + "&nbsp;&nbsp;&nbsp;管理员：" + ftpSite.getAdmin()
		    + "&nbsp;&nbsp;&nbsp;管理员联系方式：" + ftpSite.getContact() + "<br>最后更新时间："
		    + ftpSite.getLastUpdateTime() + "&nbsp;&nbsp;&nbsp;编码方式："
		    + ftpSite.getEncoding() + "&nbsp;&nbsp;&nbsp;文件总数："
		    + ftpSite.getTotalFileCount() + "&nbsp;&nbsp;&nbsp;速度限制：" + ftpSite.getSpeed()
		    + "&nbsp;&nbsp;&nbsp;用户限制：" + ftpSite.getUserslimit()
		    + "&nbsp;&nbsp;&nbsp;更新周期：" + ftpSite.getRecursive());
	    ftpSiteSnapshotForm.setFtpAddress(ftpSite.getFtpAddressURL());
	}
	String parentPath = request.getParameter("parentPath");
	String sort = request.getParameter("sort");
	if (sort == null)
	    sort = "category";
	String keyword = "server:" + server.trim();
	keyword += " AND username:" + username.trim();
	ArrayList<FtpSiteSnapshotPath> parentList = new ArrayList<FtpSiteSnapshotPath>();

	String selfPath = "";
	if (parentPath == null || parentPath.equals("/"))
	{
	    parentPath = "/";
	    keyword += " AND parent:/";
	}
	else
	{
	    keyword += " AND parent:\"" + parentPath + "\"";
	    String[] splits = parentPath.split("/");
	    String parent = "/";
	    for (int i = 1; i < splits.length - 1; i++)
	    {
		String s = splits[i].trim();
		if (s.length() > 0)
		{
		    FtpSiteSnapshotPath p = new FtpSiteSnapshotPath();
		    p.setAbsolutePath(parent + s);
		    p.setRelativePath(s);
		    parentList.add(p);
		    parent += s + "/";
		}
	    }
	    selfPath = splits[splits.length - 1];
	}

	String ftpAddress = ftpSiteSnapshotForm.getFtpAddress() + parentPath;
	SearchParameter parameter = new SearchParameter();
	parameter.setKeyword(keyword);
	parameter.setSortType(sort);
	parameter.setBegin(0);
	parameter.setCount(500);
	SearchResult res = new SearchResult();
	try
	{
	    res = FtpSearch.search(parameter, false);
	    List<SearchResultElement> outputList;
	    outputList = res.getResultFileList();
	    request.setAttribute("server", server);
	    request.setAttribute("username", username);
	    request.setAttribute("ftpAddress", ftpAddress);
	    request.setAttribute("ftpInfo", ftpSiteSnapshotForm.getFtpInfo());
	    request.setAttribute("parentPath", parentPath);
	    request.setAttribute("parentList", parentList);
	    request.setAttribute("selfPath", selfPath);
	    request.setAttribute("outputList", outputList);
	    request.setAttribute("title", "站点快照");
	    return mapping.findForward("success");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return mapping.getInputForward();
    }
}