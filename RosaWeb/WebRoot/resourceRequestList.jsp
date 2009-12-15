<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<title>资源请求列表</title>
		<script language="javascript" type="text/javascript" src="script/WdatePicker.js"></script>
		<script type="text/javascript">
		  function deleteResource(email,id)
		  {
		      var emailText=document.getElementById(email).value;
		      window.location.href='resourceRequestDelete.do?id='+id+'&email='+emailText;
		  }
		</script>
		<style type="text/css" media="screen">@import "./styles/styles.css";</style>
	</head>	
<BODY BGCOLOR=#FFFFFF LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">
<tiles:insert page="top.jsp" flush="true" />  
<center> 
<table width="80%"  border="0" cellspacing="0" cellpadding="0">
   	<tr>
		<td>
		<tiles:insert page="topTitle.jsp" flush="true" />  
		</td>
	</tr>
   <tr>
      <td style="text-align:left">
        <html:form action="/resourceRequestList.do">
         资源名:<html:text property="keyword" style="height:20" size="25" />
         状态:        <select name="state" id="select">
                    <option value="-1">全部</option>
                    <option value="1">已回复</option>
                    <option value="0">未回复</option>
		            </select>
         
         请求时间从 <input id="d5221" class="Wdate" value="<bean:write name="resourceRequestListForm" property="dateFrom"/>" type="text" size="10" name="dateFrom" onFocus="var d5222=$dp.$('d5222');WdatePicker({onpicked:function(){d5222.focus();},maxDate:'#F{$dp.$D(\'d5222\')}'});"/>
         到 <input id="d5222" class="Wdate" value="<bean:write name="resourceRequestListForm" property="dateTo"/>" type="text" size="10" name="dateTo" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d5221\')}'})"/>		
        <html:submit><bean:message key="ftpsearch.jsp.searchtrue.search"/>请求</html:submit>&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="resourceRequest.jsp"><img border=0 style="vertical-align:top"  src="images/request.gif"/></a>
        </html:form>
      </td>
    </tr>    
    <tr>
    <td height=10>
    </td>
    </tr>
	<TR>
	   <TD>			
			<TABLE WIDTH="100%" style="table-layout:fixed"  bgcolor=#dfdfef BORDER=0 CELLPADDING=0 CELLSPACING=1 align="center">
              <TR bgcolor="#e8e8e8">
                <TD width=30>
                <bean:message key="ftpsearch.jsp.resourcerequest.id" /></TD>   				           
				<TD><a href="resourceRequestList.do?sort=resourcename">
				<bean:message key="ftpsearch.jsp.resourcerequest.resourcename" /></a></TD>
				<TD width=110><a href="resourceRequestList.do?sort=nickname">
				<bean:message key="ftpsearch.jsp.resourcerequest.nickname" /></a></TD> 						
				<TD width=80><a href="resourceRequestList.do?sort=time">
				请求<bean:message key="ftpsearch.jsp.resourcerequest.time" /></a></TD>	
				<TD width=80><a href="resourceRequestList.do?sort=deadline">
				截止<bean:message key="ftpsearch.jsp.resourcerequest.time" /></a></TD>
				<TD width=60>状态</TD>
				<TD width=150><a href="resourceRequestList.do?sort=email">
				<bean:message key="ftpsearch.jsp.resourcerequest.email"/></a></TD>	
				<TD width=40> 操作 </TD>
             </TR>
            <% Integer pageIndexBegin=(Integer)request.getAttribute("page");%>
			<logic:iterate id="ResourceRequest" name="listing"  indexId="index">		  
			  <tr bgcolor=#<%=(index.intValue()%2==0)? "FFFFFF" : "F5F8FE" %>>
					<td ><%=index.intValue()+1+pageIndexBegin%></td>					
					<td style="text-align:left">&nbsp;&nbsp;<bean:write name="ResourceRequest" property="resourcename" />	</td>
					<td><bean:write name="ResourceRequest" property="nickname" /></td>									
					<td><bean:write name="ResourceRequest" property="time" /></td>
					<td><bean:write name="ResourceRequest" property="deadline" /></td>
					<TD><bean:write name="ResourceRequest" property="stateString" /></TD>
					<td><input id="email<%=index.intValue()%>" type="text" style="height:18;width:140;font-size:8pt">
					</td>
					<td><a href="#" onClick="javascript:deleteResource('email<%=index.intValue()%>','<bean:write name="ResourceRequest" property="id"/>');return false;">
					   删除</a></td>
				</tr>
			</logic:iterate>              		  			  
            </TABLE>
	   </TD>
	</TR>
	<tr><td>
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
	  </td></tr>
 </TABLE>
<table>		
 <tr>
 <td height=40>
 说明：对于目前搜索不到的资源，您可以填写资源请求，当AksRosa更新索引时发现此资源已经存在，将会给您发送邮件通知。
 </td>
 </tr>
</table>		
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
<script type="text/javascript">
	document.getElementById('select').value = '<bean:write name="resourceRequestListForm" property="state"/>';
</script>
</BODY></HTML>