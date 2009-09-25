<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.fckeditor.net" prefix="FCK"%>  

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<title>新留言</title>
		<style type="text/css" media="screen">@import "./styles/styles.css";</style>
	</head>	
<BODY BGCOLOR=#FFFFFF LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">
<tiles:insert page="top.jsp" flush="true" />  
<center> 
<table width="70%"  border="0" cellspacing="0" cellpadding="0" align="center">
    <tr><td class=style25>&nbsp;</td></tr> 
    <tr><td class=style23>发表新的留言</td> </tr>
    <tr><td class=style22>&nbsp;</td></tr> 
	<TR>
	   <TD>			
            <html:form action="/articleNew.do">
            <table width="100%" border="0" cellspacing="0" cellpadding="1">
				<tr><td style="text-align:right" width=60><bean:message key="ftpsearch.jsp.messageboard.author"/>*：</td>
				    <td style="text-align:left"><html:text property="article.author" size="40" /><span class=errorlight><html:errors property="article.author"/></span></td></tr>
			    <tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.messageboard.title"/>*：</td>
					<td style="text-align:left"><html:text property="article.title" size="40"/><span class=errorlight><html:errors property="article.title"/></span></td></tr>
			    <tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.messageboard.content"/>*：</td>
				    <td style="text-align:left"><FCK:editor instanceName="article.content"  height="400pt">
					   <jsp:attribute name="value">请输入内容...</jsp:attribute>
		  			   </FCK:editor></td></tr>
		  		<tr><td ></td>
			        <td style="text-align:left"><span class=errorlight><html:errors property="article.content"/></span></td>
			    </tr>   
				<tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.messageboard.verify"/>*：</td>
				    <td style="text-align:left"><html:password property="article.verify" size="20" /><span class=errorlight><html:errors property="article.verify"/></span>
				    <bean:message key="ftpsearch.jsp.messageboard.verifyconfirm"/>*：
				    <html:password property="verifyConfirm" size="20" /><span class=errorlight><html:errors property="verifyConfirm"/></span>
				    </td></tr>
				<tr><td style="text-align:center" colspan=2>
				      <html:submit>
				         <bean:message key="ftpsearch.jsp.regftpsite.submit"/>留言
				      </html:submit>
				    </td></tr>
            </table>	        
		   </html:form>   
	   </TD>
	</TR>	
</TABLE>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</BODY></HTML>