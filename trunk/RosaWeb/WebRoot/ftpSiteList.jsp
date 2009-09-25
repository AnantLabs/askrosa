<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<title><bean:message key="ftpsearch.jsp.ftpsitelist.title" /></title>
		<script language="javascript" type="text/javascript" src="script/WdatePicker.js"></script>
		<script language="javascript" type="text/javascript" src="script/copy.js"></script>
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
<BODY LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">
<tiles:insert page="top.jsp" flush="true" />  
<center> 
<table width="90%"  border="0" cellspacing="0" cellpadding="0">
   	<tr>
		<td>
		<tiles:insert page="topTitle.jsp" flush="true" />  
		</td>
	</tr>
    <tr><td style="text-align:left">
         1.<a href="XMLForFlashFxp.ftp">可导入FlashFxp的XML格式下载</a>&nbsp;&nbsp;&nbsp;
         2.<a href="download/FlashFXP.rar">FlashFxp-3.4.0下载</a>&nbsp;&nbsp;&nbsp;
         3.站点:(
         <logic:iterate id="ftpFileCountList" name="ftpFileCountList"  indexId="index">
         <bean:write name="ftpFileCountList" property="locationString"/>:
         <bean:write name="ftpFileCountList" property="ftpCount"/>
         </logic:iterate>)
          站点总数:<bean:write name="totalFtpCount" />
          文件总数:<bean:write name="totalFileCount" />
         </td></tr>
    <tr> <td height=10> </td></tr>
    <tr>
      <td style="text-align:left">
        <html:form action="/ftpSiteList.do">
        FTP地址:<html:text property="ftpAddress" style="height:20" size="25" />
         联系方式：<html:text property="author" style="height:20" size="10" />
         站点位置：    <select name="location" id="select">
                    <option value="-1">全部</option>
                    <option value="0">校园网</option>
                    <option value="1">教育网</option>
                    <option value="2">公共网</option>
		            </select>
         
         更新日期从 <input id="d5221" class="Wdate" type="text" value="<bean:write name="ftpSiteListForm" property="dateFrom"/>" size="10" name="dateFrom" onFocus="var d5222=$dp.$('d5222');WdatePicker({onpicked:function(){d5222.focus();},maxDate:'#F{$dp.$D(\'d5222\')}'});"/>
         到 <input id="d5222" class="Wdate" value="<bean:write name="ftpSiteListForm" property="dateTo"/>" type="text" size="10" name="dateTo" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d5221\')}'})"/>		
        <html:submit><bean:message key="ftpsearch.jsp.searchtrue.search"/>站点</html:submit>
        </html:form>
      </td></tr>
    <tr><td height=10></td></tr>
	<TR><TD>			
			<TABLE WIDTH="100%" style="table-layout:fixed" bgcolor=#dfdfef BORDER=0 CELLPADDING=0 CELLSPACING=1 align="center">
              <TR bgcolor="#e8e8e8">
			    <TD width="30"><bean:message key="ftpsearch.jsp.ftpsitelist.id"/></TD> 
                <TD width="120"><a href="ftpSiteList.do?sort=server">
                <bean:message key="ftpsearch.jsp.ftpsitelist.server" /></a></TD> 
                <TD width="70"><a href="ftpSiteList.do?sort=username">
                  用户名</a></TD>                         
				<TD width="50"><a href="ftpSiteList.do?sort=totalFileCount">
				<bean:message key="ftpsearch.jsp.ftpsitelist.total" /></a></TD>				
				<TD width="50"><a href="ftpSiteList.do?sort=video">
				<bean:message key="ftpsearch.jsp.ftpsitelist.video" /></a></TD>				
				<TD width="50"><a href="ftpSiteList.do?sort=audio">
				<bean:message key="ftpsearch.jsp.ftpsitelist.audio" /></a></TD>				
				<TD width="50"><a href="ftpSiteList.do?sort=document">
				<bean:message key="ftpsearch.jsp.ftpsitelist.document"/></a></TD>
				<TD width="50"><a href="ftpSiteList.do?sort=executable">
				程序</a></TD>               
				<TD width="50"><a href="ftpSiteList.do?sort=hot">
				HOT</a></TD>   
                <TD width="70"><a href="ftpSiteList.do?sort=updateTime">
                <bean:message key="ftpsearch.jsp.ftpsitelist.updateTime" /></a></TD> 
                <TD width="30"><a href="ftpSiteList.do?sort=crawlInterval">
                  周期</a></TD>                 
                <TD><a href="ftpSiteList.do?sort=contact">
                <bean:message key="ftpsearch.jsp.ftpsitelist.contact" /></a></TD>     
                <TD width="40">位置</TD>             
                <TD width="70">操作</TD>                
              </TR>
                 <% Integer pageIndexBegin=(Integer)request.getAttribute("page");%>
			<logic:iterate id="FtpSiteInfo" name="listing"  indexId="index">			  
			  <tr bgcolor=#<%=(index.intValue()%2==0)? "FFFFFF" : "F5F8FE" %>>
					<td ><%=index.intValue()+1+pageIndexBegin%></td>
					<td style="text-align:left"><a title="点击查看站点快照" href="ftpSiteSnapshot.do?server=<bean:write name="FtpSiteInfo" property="server" />&username=<bean:write name="FtpSiteInfo" property="username" />">
					<bean:write name="FtpSiteInfo" property="server" />	</a></td>
					<td style="text-align:center"><bean:write name="FtpSiteInfo" property="username"/>
					<td style="text-align:right"><bean:write name="FtpSiteInfo" property="totalFileCount" /></td>
					<td style="text-align:right"><bean:write name="FtpSiteInfo" property="video" /></td>
					<td style="text-align:right"><bean:write name="FtpSiteInfo" property="audio" /></td>
					<td style="text-align:right"><bean:write name="FtpSiteInfo" property="document" /></td>					
					<td style="text-align:right"><bean:write name="FtpSiteInfo" property="executable" /></td>
					<td style="text-align:right"><bean:write name="FtpSiteInfo" property="hot" /></td>
					<td ><bean:write name="FtpSiteInfo" property="updateTime" /></td>
					<td style="text-align:right"><bean:write name="FtpSiteInfo" property="crawlInterval" /></td>
					<td ><bean:write name="FtpSiteInfo" property="contact" /></td>
					<td style="text-align:center">
					   <bean:write name="FtpSiteInfo" property="locationString"/>				   					   
					</td>
					<td ><a title="修改站点信息" href="ftpSiteUpdate.do?id=<bean:write name="FtpSiteInfo" property="id"/>">修改</a>
					<a title="复制站点的链接地址" href="" onClick="javascript:copyToClipboardObj(addr<%=index.intValue()+pageIndexBegin%>);return false;">复制</a>
					<span id="addr<%=index.intValue()+pageIndexBegin%>" class="hid"><bean:write name="FtpSiteInfo" property="connectString" /></span></td>					
				</tr>
			</logic:iterate>              		  			  
            </TABLE>
	   </TD></TR>	
	<tr><td>	   
	   <table width=100%>
		 <tr>
		 <td>  
		  <font color="#0000ff"><bean:message key="ftpsearch.jsp.searchtrue.page"/>：</font>    
               <logic:iterate id="pageIndex" name="pageIndex"  indexId="index">	
               <a href="<bean:write name="pageIndex" property="link" />"><font color="<bean:write name="pageIndex" property="color"/>"><bean:write name="pageIndex" property="page"/></font></a>&nbsp;
               </logic:iterate>                             
		 </td>
		</tr>
	   </table>	
	   </td></tr>
	<tr><td style="text-align:left">
	  <br>说明:<br>
	  &nbsp;&nbsp;&nbsp;1.单击站点可以查看此站点的详细信息。拥有此站点管理密码的管理员可以更改此站点的信息，也可删除此站点<br>
	  &nbsp;&nbsp;&nbsp;2.站点快照还没有做好，现在只能提供伪快照功能，但是用户通过一定的查询手段还是可以离线看到站点的所有的信息的。<br>
	  &nbsp;&nbsp;&nbsp;3.文件总数为0的站点是由于一些原因在爬虫程序访问该ftp站点时此ftp站点不可访问，也可能其文件总数就是0,但这是少数。<br>
	  &nbsp;&nbsp;&nbsp;4.欢迎ftp管理员在此注册您的ftp站点，但要保证ftp站点在您注册的时候是开通的。<br>
	  &nbsp;&nbsp;&nbsp;5.过期站点的文件总数被设置成-1，其他分类的文件数都是最后一次成功更新的统计结果。<br>
	  &nbsp;&nbsp;&nbsp;6.默认验证码是IP或者域名中去掉.的字串。比如FTP地址为202.119.32.7的验证码为202119327，域名类似。
	  </td></tr>
</TABLE>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>

<script type="text/javascript">
	document.getElementById('select').value = '<bean:write name="ftpSiteListForm" property="location" />';
</script>
</BODY></HTML>