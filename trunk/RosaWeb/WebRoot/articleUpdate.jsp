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
		<title><bean:message key="ftpsearch.jsp.regftpsite.title" /></title>
		<style type="text/css" media="screen">@import "./styles/styles.css";</style>
	</head>	
<BODY BGCOLOR=#FFFFFF LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">
<tiles:insert page="top.jsp" flush="true" />  
<center> 
<table width="70%"  border="0" cellspacing="0" cellpadding="0">
    <tr><td class=style25>&nbsp;</td></tr> 
    <tr><td class=style23>更改留言</td> </tr>
    <tr><td class=style22>&nbsp;</td></tr> 
	<TR><TD>			
        <html:form action="/articleUpdateDo.do">
        <table width="100%" border="0" cellspacing="0" cellpadding="1">
			<tr><td style="text-align:right" width="60"><bean:message key="ftpsearch.jsp.messageboard.author"/>：</td>
			    <td style="text-align:left"><html:text property="article.author" size="40" /><span class=errorlight><html:errors property="article.author"/></span></td></tr>
		    <tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.messageboard.title"/>：</td>
				<td style="text-align:left"><html:text property="article.title" size="40"/><span class=errorlight><html:errors property="article.title"/></span></td></tr>
		    <tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.messageboard.content"/>*：</td>
			    <td style="text-align:left">	 
			       <FCK:editor instanceName="article.content"  height="400pt">
			       <jsp:attribute name="value"><bean:write name="articleUpdateForm" property="article.content" filter="false"/></jsp:attribute>
  				   </FCK:editor></td></tr>
			<tr><td ></td>
			    <td style="text-align:left"><span class=errorlight><html:errors property="article.content"/></span></td>
			</tr>
			<tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.messageboard.verify"/>：</td>
			    <td style="text-align:left"><html:password  property="article.verify" size="40" /><span class=errorlight><html:errors property="article.verify"/></span>
			        <html:select property="type" >
                    <option><bean:message key="ftpsearch.jsp.updateftpsite.type.update"/></option>
                    <option><bean:message key="ftpsearch.jsp.updateftpsite.type.delete"/></option>
		            </html:select>
		            <html:submit>
				      <bean:message key="ftpsearch.jsp.regftpsite.submit"/>更改
				    </html:submit>
		            </td></tr>
		    <tr><td style="text-align:right">	
		         修改码：
	           </td>
	           <td style="text-align:left">
	              为保证只有此留言的作者具有修改留言的权限，提供修改码来确认用户
	           </td>
	           </tr>	       
        </table>
        </html:form>  
	   </TD>
	</TR>

</TABLE> 
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</BODY></HTML>