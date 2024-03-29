/*
 * Generated by MyEclipse Struts Template path: templates/java/JavaClass.vtl
 */
package cn.askrosa.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import resource.WebSetting;

import cn.askrosa.struts.form.ArticleUpdateForm;

import database.Article;
import database.ArticlePeer;

/**
 * MyEclipse Struts Creation date: 09-22-2008 XDoclet definition:
 * 
 * @struts.action path="/updateMessage" name="modifyMessageForm"
 *                input="/modifyMessage.jsp" scope="request" validate="true"
 * @struts.action-forward name="success" path="/inforOutput.jsp"
 */
public class ArticleUpdateDoAction extends Action
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
	ArticleUpdateForm articleUpdateForm = (ArticleUpdateForm) form;
	Article article = articleUpdateForm.getArticle();
	article.setIp(request.getRemoteAddr());
	String type = articleUpdateForm.getType();
	try
	{
	    if (type.equals(WebSetting.getProperty("ftpsearch.jsp.updateftpsite.type.update")))
	    {
		String ret = ArticlePeer.updateArticle(article);
		if (ret == null)
		{
		    request.setAttribute("infor", "留言修改成功");
		    request.setAttribute("forward", "查看留言列表");
		    request.setAttribute("forwarddo", "articleList.do?page=1");
		    return mapping.findForward("success");
		}
		else
		{
		    article.setVerify("");
		    ActionMessages errors = new ActionMessages();
		    errors.add("article.verify", new ActionMessage(ret, false));
		    saveErrors(request, errors);
		    return mapping.getInputForward();
		}
	    }
	    else if (type.equals(WebSetting.getProperty("ftpsearch.jsp.updateftpsite.type.delete")))
	    {
		String ret = ArticlePeer.deleteArticle(article);
		if (ret == null)
		{
		    request.setAttribute("infor", "留言删除成功");
		    request.setAttribute("forward", "查看留言列表");
		    request.setAttribute("forwarddo", "articleList.do?page=1");
		    return mapping.findForward("success");
		}
		else
		{
		    ActionMessages errors = new ActionMessages();
		    errors.add("article.verify", new ActionMessage(ret, false));
		    saveErrors(request, errors);
		    return mapping.getInputForward();
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return null;
    }
}