<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>

<%@taglib uri="http://java.fckeditor.net" prefix="FCK"%>
<html> 
	<head>
		<title>留言主题查看</title>
	    <meta http-equiv=Content-Type content="text/html;charset=utf-8">
	    <style type="text/css" media="screen">@import "./styles/styles.css";</style>
	   <style type="text/css">
	   <!--
      td{
        white-space: normal;
        word-break: break-all;
        }
   -->
   </style>
	</head>	
<BODY BGCOLOR=#FFFFFF LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">
<tiles:insert page="top.jsp" flush="true" />  
<center> 
<table width="70%"  border="0" cellspacing="0" cellpadding="0">
    <tr><td class=style25>&nbsp;</td></tr> 
    <tr><td class=style23>留言主题查看</td> </tr>
    <tr><td class=style22>&nbsp;</td></tr> 
	<TR>
	   <TD>			
	     <TABLE width="100%" style="table-layout:fixed"  bgcolor=#9FB6CD border="0" cellspacing="1" cellpadding="0">
	         <TR bgcolor=#C6E2FF>
              <TD width="80" style="text-align:center">
                 作者：&nbsp;</TD>
              <TD style="text-align:left">&nbsp;&nbsp; 标题：<bean:write name="article" property="title" filter="true"/></TD>
              <TD width="140"style="text-align:left"><bean:write name="article" property="time" filter="true"/></TD>
              <TD width="110">IP:<bean:write name="article" property="ip" filter="true"/></TD>
              <TD width="40"><a href="articleUpdate.do?id=<bean:write name="article" property="id" filter="true"/>"><bean:message key="ftpsearch.jsp.messageboard.modify"/></a></TD>
             </TR>	
              <tr bgcolor=#FFFFFF>
		          <td style="text-align:center;">
		          	 <bean:write name="article" property="author" filter="true"/>：&nbsp;
		          </td>
	          	  <td colspan="4">
	                 <table width=100% border="0" cellspacing="0" cellpadding="0">
	                    <tr><td height=8></td><td height=8></td></tr>
                        <tr><td width=8></td><td style="text-align:left"><bean:write name="article" property="content" filter="false"/></td></tr>
                        <tr><td height=8></td><td height=8></td></tr>
                     </table>
                  </td>
	          </tr>       
         </TABLE>     
       </TD>
	</TR>
</TABLE>
  
 <table width="70%"  border="0" cellspacing="0" cellpadding="0">
	 <tr>
	 <td style="text-align:left;color:#27408B;font-size:10pt;" height=30>
	   评论如下：
	 </td>
	 </tr>
</table> 
<logic:iterate id="ResponsePost" name="listing"  indexId="index">
<TABLE width="70%" style="table-layout:fixed" bgcolor=#9FB6CD  border="0" cellspacing="1" cellpadding="0">
    <TR  bgcolor=#B9D3EE>
         <TD width="80" style="text-align:center"><bean:write name="index"/>楼&nbsp;</TD>
         <td></TD>
         <TD width = "140" style="text-align:center"><bean:write name="ResponsePost" property="time" filter="true"/></TD>
         <TD width = "110" style="text-align:left">IP:<bean:write name="ResponsePost" property="ip" filter="true"/></TD>
         <TD width = "40" ><a href="articleResponsePostUpdate.do?id=<bean:write name="ResponsePost" property="postid"/>"><bean:message key="ftpsearch.jsp.messageboard.modify"/></a></TD>
         <TD width = "60" id="link"><a href="#">回到顶端</a></TD>
    </TR>	 
    <tr bgcolor=#FFFFFF>
      <td  style="text-align:center"><bean:write name="ResponsePost" property="author" filter="true"/>：&nbsp;</td>
      <td colspan="5">
           <table width=100% border="0" cellspacing="0" cellpadding="0">
              <tr><td height=8></td><td height=8></td></tr>
              <tr><td width=8></td><td style="text-align:left"><bean:write name="ResponsePost" property="content" filter="false"/></td></tr>
              <tr><td height=8></td><td height=8></td></tr>
           </table>

      </td>
    </tr>  
</TABLE>                                 
<table width="70%"  border="0" cellspacing="0" cellpadding="0">
 <tr>
 <td height=10>
 </td>
 </tr>
</table>
</logic:iterate>   
  
<% String id=(String)request.getAttribute("id");%>  
<html:form action="/articleResponsePostDo.do">
<input type="hidden" name="id" value="<%=id %>">
<table width="70%"  border="0" cellspacing="0" cellpadding="0">
	 <tr>
		 <td style="text-align:right">
		     返回上一页
		   	<a href="articleShow.do?id=<bean:write name="article" property="id" filter="true"/>">
		     刷新评论列表</a>
		 </td>
	 </tr>
	 <tr >
		 <td style="text-align:left;color:#27408B;font-size:10pt;">
		   	我也要说两句：
		 </td>
	 </tr>
</table>
<table width="70%"  bgcolor=#9FB6CD border="0" cellspacing="1" cellpadding="0">	 
 	 <tr>
	    <td style="text-align:left">
	   	<bean:message key="ftpsearch.jsp.messageboard.author"/>：
	   	<html:text property="responsePost.author" size="16" /> <span class=lightblue><html:errors property="responsePost.author"/></span>
	   	<bean:message key="ftpsearch.jsp.messageboard.verify"/>
		<html:password property="responsePost.verify" size="12" /> <span class=lightblue><html:errors property="responsePost.verify"/></span>
		<bean:message key="ftpsearch.jsp.messageboard.verifyconfirm"/>
		<html:password property="verifyConfirm" size="12" /> <span class=lightblue><html:errors property="verifyConfirm"/></span>
	    <html:submit>
		 发表
		</html:submit>
	 </td>
	 </tr>
	<TR>
	   <TD>		
			<FCK:editor instanceName="responsePost.content"  height="200pt">
			<jsp:attribute name="value">请输入内容...</jsp:attribute>
  			</FCK:editor> 
       </TD>
	</TR>
	<TR>
	   <TD style="text-align:left" >
  		    <span class=errorlight><html:errors property="responsePost.content"/></span>
       </TD>
	</TR>	
</table>
</html:form>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</BODY></HTML>

