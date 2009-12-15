/*
 * Generated by MyEclipse Struts Template path: templates/java/JavaClass.vtl
 */
package cn.askrosa.struts.action;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import cn.askrosa.struts.form.FtpSiteUpdateForm;

import database.Criteria;
import database.FtpSiteInfo;
import database.FtpSiteInfoPeer;
import database.FtpSitesManager;
import database.Scroller;

/**
 * MyEclipse Struts Creation date: 03-10-2008 XDoclet definition:
 * 
 * @struts.action path="/editFtpSite" name="updateFtpSiteForm"
 *                input="/listing.do" parameter="server" scope="request"
 * @struts.action-forward name="success" path="/updateFtpSite.jsp"
 */
public class FtpSiteUpdateAction extends Action
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
	FtpSiteUpdateForm updateFtpSiteForm = (FtpSiteUpdateForm) form;
	String id = request.getParameter("id");

	if (id != null)
	{
	    try
	    {

		// get Contact by Id
		Criteria crit = new Criteria();
		crit.add(FtpSiteInfo.ID, id.trim());
		Scroller<FtpSiteInfo> results = FtpSiteInfoPeer.doSelect(crit);
		if (!results.hasNext())
		    return mapping.getInputForward();

		// save Contact to form
		FtpSiteInfo ftpSite = (FtpSiteInfo) results.next();
		ftpSite.setVerify("");
		updateFtpSiteForm.setFtpSiteInfo(ftpSite);
		String path = this.getServlet().getServletContext().getRealPath("/")
		+ "/XMLForFlashFxp.ftp";
        	try
        	{
        	    FtpSitesManager.storeFtpSitesToXML(new File(path));
        	}
        	catch (Exception e)
        	{
        	    e.printStackTrace();
        	}		
		ActionForward af = mapping.findForward("success");
		return af;
	    }
	    catch (Exception ignore)
	    {
		ignore.printStackTrace();
	    }
	}

	return mapping.getInputForward();
    }
}