<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html>
	<head>
	    <meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<title><bean:message key="ftpsearch.jsp.searchtrue.title" /></title>
        <style type="text/css" media="screen">
          @import "./styles/styles.css";
          @import "./styles/autocomplete.css";
        </style>        
        <script language="javascript" type="text/javascript" src="script/copy.js"></script>
        <script language="javascript" type="text/javascript" src="script/coex.js"></script>
        <script language="javascript" type="text/javascript" src="script/WdatePicker.js"></script>
        <script language="javascript" type="text/javascript" src="script/neverModules-autoComplete.js"></script>
        <script language="javascript" type="text/javascript" src="script/autoComplete.js"></script>       
</head>
<body >	 
<tiles:insert page="top.jsp" flush="true" />
<center> 
<table>
   <tr>
     <td height=40></td>
   </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td >
		 <a href="aboutus.jsp" target="_blank"><img border="0" width="200" src="images/rosa/rosa.jpg" alt="<bean:message key="ftpsearch.jsp.navigator.aboutus" />"> </a>
		</td>
	</tr>
</table>
<html:form action="/search.do" focus="keywordInclude">
<table cellpadding=0 cellspacing=0 border="0">	
	<tr>
	   <td>
	   	  <html:radio onclick="javascript:document.forms[0].target='_self';" property="search" value="rosa"/>Rosa FTP
		  <html:radio onclick="javascript:document.forms[0].target='_blank';" property="search" value="shooter"/>字幕(shooter)
		  <html:radio onclick="javascript:document.forms[0].target='_blank';" property="search" value="njubt"/>校内BT
	   </td>
	</tr>
	<tr>	 
		<td style="text-align:left">
		    <input id="keywordInclude" name="keywordInclude" autocomplete="off" value="<bean:write name="searchForm" property="keywordInclude"/>" onkeyup="AjaxHdle(event)"  ondblclick="autoComplete.expandAllItem();" style="HEIGHT: 21px; WIDTH:350px"/>
			<html:submit>Rosa <bean:message key="ftpsearch.jsp.searchtrue.search"/></html:submit>
			<a id="advanceText" href="javascript:void(0);" onClick="coexText('advanceTable','advanceText')">显示高级搜索</a>					
		</td>
	</tr>
	<tr>
		<td style="text-align:left">
	     <span class=errorlight><html:errors/></span>				
		</td>
	</tr>
</table>		
<tiles:insert page="advanceSearch.jsp" flush="true" />
</html:form>
<table>	
     <tr><td height=10></td></tr>		
     <tr><td class=style21 bgColor=#f5f8fe height=22 style="text-align:left">                
           热门搜索：<img src="images/hot2.gif" >&nbsp;&nbsp;                  
         <logic:iterate id="result" name="statisticsResult">
         <a href="search.do?keyword=<bean:write name="result" property="encodedKeyword"/>"><bean:write name="result" property="keyword"/>(<bean:write name="result" property="frequency"/>)</a>                                       
         </logic:iterate>
     </td></tr>
     <tr><td class=style24 height=22 style="text-align:left" >                
           热门站点：<img src="images/top.gif" >&nbsp;&nbsp;
           <logic:iterate id="list" name="hotFtpSite">
              <a href="ftpSiteSnapshot.do?server=<bean:write name="list" property="server"/>&username=<bean:write name="list" property="username"/>"><bean:write name="list" property="server"/>(<bean:write name="list" property="hot"/>)</a>&nbsp;
           </logic:iterate>
     </td></tr>
     
     <tr><td height=10></td></tr>   
     <tr><td class=style21 bgColor=#f5f8fe height=22 style="text-align:left">                  
            最新资源：<img src="images/new.gif" >&nbsp;&nbsp;
     <%
       String[] newreslist=(String[])request.getAttribute("newres");
       String[] newresKeywordsList = (String[])request.getAttribute("newresKeywords");
       int colcount=5;
       int i=0;
       for(;i<newreslist.length&&i<colcount;i++)
       {
     %> 
         <a href="<%=newresKeywordsList[i]%>"><%=newreslist[i]%></a>&nbsp;
     <%
      } 
     %>
     </td></tr> 
      <%
       int j=2; 
       while(i<newreslist.length)
       {
     %>     
     <tr><td class=style24 height=22 style="text-align:left" >                
	     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	     <%
	       for(;i<newreslist.length&&i<colcount*j;i++)
	       {
	     %> 
	         <a href="<%=newresKeywordsList[i]%>"><%=newreslist[i]%></a>&nbsp;
	     <%
	      } 
	     %>          
     </td></tr>
     <%
        j++;
       } 
     %>   
     <tr><td height=10></td></tr>
  
     <tr><td class=style24 height=22 style="text-align:left" >                
      <a href="resourceRequestList.do?page=1">资源请求：</a><img src="images/req.gif" >&nbsp;&nbsp;
           <logic:iterate id="list" name="listing">
              <bean:write name="list" property="resourcename" />
           </logic:iterate>
     </td></tr>
     <tr><td  class=style24 colspan="4" style="text-align:left">
 	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;        
               如果您拥有此资源可以上传到任意Rosa可以检索到的FTP服务器，也可以上传到<a target=_blank href="ftp://up:up@ftp.askrosa.cn" onClick="javascript:copyToClipboard('ftp://up:up@ftp.askrosa.cn');return false;">Rosa交换空间</a>
	 </td></tr>
     <tr><td height=10></td></tr>
     
     <tr><td>
       <table width=100%>
		   <tr><td class=style24 height=20  style="text-align:left">
		     提醒：1. 当你发现某些站点AskRosa没有收录的时候，你可以通过<a href="ftpSiteReg.jsp">登记站点</a>来将其添加到AskRosa。
		   </td></tr>
		   <tr><td class=style24 bgColor=#f5f8fe height=20  style="text-align:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		         2. 对于目前没有的资源，您可以提交请求，当索引更新的时候发现资源会邮件通知阁下。<a href="resourceRequest.jsp">资源请求提交页面</a>
		   </td></tr>
		   <tr><td class=style24 bgColor=#f5f8fe height=20  style="text-align:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      3. 如有问题或意见可以给AskRosa<a href="articleNew.jsp">留言</a>或Email <a href="mailto:askrosateam@gmail.com">Askrosa Team</a>
		   </td></tr>
		</table>
     </td></tr>
</table>
<script language="javascript">
  if(searchForm.search[0].checked)
     document.forms[0].target='_self';
  if(searchForm.search[1].checked)
     document.forms[0].target='_blank';
  if(searchForm.search[2].checked)
     document.forms[0].target='_blank';             
</script>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</body>
</html>

