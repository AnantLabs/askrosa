<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<ul id="navbar">
 <li><img src="images/sites/search.png" ><a href="welcomemain.jsp"><bean:message key="ftpsearch.jsp.navigator.search"/>首页</a></li>
 <li><img src="images/sites/text.png" ><a href="articleList.do">留言板</a></li>
 <li><img src="images/sites/request.png" ><a href="resourceRequestList.do"><bean:message key="ftpsearch.jsp.navigator.resourceRequest"/></a></li>
 <li><img src="images/sites/sites.png" ><a href="ftpSiteList.do"><bean:message key="ftpsearch.jsp.navigator.sites"/></a></li>
 <li><img src="images/sites/regsites.png" ><a href="ftpSiteReg.jsp"><bean:message key="ftpsearch.jsp.navigator.regsite"/></a></li>
 <li><img src="images/sites/unknown.png" ><a href="help/RosaHelp.html"><bean:message key="ftpsearch.jsp.navigator.manual"/></a></li>
 <li><img src="images/sites/document.png" ><a href="changelog.jsp"><bean:message key="ftpsearch.jsp.navigator.changelog"/></a></li>
 <li><img src="images/sites/class.gif" ><a href="statisticsShow.do"><bean:message key="ftpsearch.jsp.navigator.statistics"/></a></li>
 <li><img src="images/sites/ftp.png" ><a href="rosaFTP.jsp"><bean:message key="ftpsearch.jsp.navigator.rosaftp"/></a></li>
 <li><img src="images/sites/gz.gif" ><a href="download.jsp">资料<bean:message key="ftpsearch.jsp.searchtrue.download"/></a></li>
 <li><img src="images/sites/bookmark.png" ><a href="javascript:addBookmark('Rosa','http://askrosa.cn')">加入收藏</a></li>
 </ul> 
<script type="text/javascript">
function addBookmark(title,url) 
{
	if (window.sidebar) 
	    { 
	     window.sidebar.addPanel(title, url,""); 
	    } 
	else if( document.all ) 
	    {
	     window.external.AddFavorite( url, title);
	    } 
	else if( window.opera && window.print ) 
	    {
	       return true;
	    }
}
</script> 
 