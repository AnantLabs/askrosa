<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>留言列表</title>
		<script language="javascript" type="text/javascript" src="script/WdatePicker.js"></script>
		<style type="text/css" media="screen">@import "./styles/styles.css";</style>
	</head>	
<BODY>
<tiles:insert page="top.jsp" flush="true" />  
<center>
<table width="80%"  border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
		<tiles:insert page="topTitle.jsp" flush="true" />  
		</td>
	</tr>
 	<tr><td style="text-align:left">
        <html:form action="/articleList.do">
         标题：<html:text property="keyword" style="height:20" size="25" />
         作者：<html:text property="author"  style="height:20" size="10" />
         时间从 <input id="d5221" class="Wdate" value="<bean:write name="articleListForm" property="dateFrom"/>" type="text" size="10" name="dateFrom" onFocus="var d5222=$dp.$('d5222');WdatePicker({onpicked:function(){d5222.focus();},maxDate:'#F{$dp.$D(\'d5222\')}'});"/>
         到 <input id="d5222" class="Wdate" value="<bean:write name="articleListForm" property="dateTo"/>" type="text" size="10" name="dateTo" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d5221\')}'})"/>		
        <html:submit><bean:message key="ftpsearch.jsp.searchtrue.search"/>留言</html:submit>&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="articleNew.jsp"><img border=0 style="vertical-align:top"  src="images/article.gif"/></a>
        </html:form>	    
	</td></tr>
 
    <tr><td height=8></td></tr>
	<TR><TD>			
		<TABLE WIDTH="100%" bgcolor=#9FB6CD style="table-layout:fixed" BORDER=0 CELLPADDING=0 CELLSPACING=1>
            <TR bgcolor="#e8e8e8">
                <TD width=30>
                <bean:message key="ftpsearch.jsp.messageboard.id" /></TD>
   				<TD><a href="articleList.do?sort=title&page=1">
				<bean:message key="ftpsearch.jsp.messageboard.title" /></a></TD>             
				<TD width=100><a href="articleList.do?sort=author">
				<bean:message key="ftpsearch.jsp.messageboard.author" /></a></TD>
			    <TD width=70><a href="articleList.do?sort=responsePostCount">回复</a>/<a href="articleList.do?sort=clickcount"><bean:message key="ftpsearch.jsp.messageboard.clickcount"/></a></TD>			
				<TD width=150><a href="articleList.do?sort=time">
				时间</a></TD>	
            	<TD width=110><a href="articleList.do?sort=ip">
				<bean:message key="ftpsearch.jsp.messageboard.ip"/></a></TD>
				<TD width=40>
				<bean:message key="ftpsearch.jsp.messageboard.operation"/></TD>
             </TR>
             <% Integer pageIndexBegin=(Integer)request.getAttribute("page");%>
			 <logic:iterate id="Articles" name="listing"  indexId="index">	  
			 <tr bgcolor=#<%=(index.intValue()%2==0)? "FFFFFF" : "F5F8FE" %>>
				<td><%=index.intValue()+1+pageIndexBegin%></td>
				<td style="text-align:left">&nbsp;&nbsp;<a href="articleShow.do?id=<bean:write name="Articles" property="id"/>"><bean:write name="Articles" property="title" /></a></td>
				<td><bean:write name="Articles" property="author" /></td>
				<td style="text-align:center"><a href="articleShow.do?id=<bean:write name="Articles" property="id"/>"><bean:write name="Articles" property="responsePostCount" /></a>/<bean:write name="Articles" property="clickcount" /></td>					
				<td><bean:write name="Articles" property="time" /></td>
				<td><bean:write name="Articles" property="ip" /></td>
				<td><a href="articleUpdate.do?id=<bean:write name="Articles" property="id"/>">
				<bean:message key="ftpsearch.jsp.messageboard.modify"/></a></td>
			  </tr>
			  </logic:iterate>              		  			  
        </TABLE>
	</TD></TR>
	<TR><TD>
	   <table width=100%>
		 <tr>
		 <td>  
		  <font color="#0000ff"><bean:message key="ftpsearch.jsp.searchtrue.page"/>：</font>    
               <logic:iterate id="pageIndex" name="pageIndex"  indexId="index">	
               &nbsp;<a href="<bean:write name="pageIndex" property="link" />"><font color="<bean:write name="pageIndex" property="color"/>"><bean:write name="pageIndex" property="page"/></font></a>
               </logic:iterate>                             
		 </td>
		</tr>
	   </table>	
	</TD></TR>	
</TABLE> 
<table>		
 <tr>
 <td height=40>
 说明：留言版Beta版。如果对AskRosa有意见或者想对某些问题提出讨论，可以在此处留言。管理员会尽快回答所有提问并和您共同讨论提高。
 </td>
 </tr>
</table>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</BODY></HTML>