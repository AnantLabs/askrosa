<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv=Content-Type content="text/html;charset=utf-8">
	<title><bean:message key="ftpsearch.jsp.ftpsitelist.title" /></title>
	<script language="javascript" type="text/javascript" src="script/copy.js"></script>
<script type="text/javascript">
function coex(id1,id2)
    {
      var s1 = document.getElementById(id1);
      var s2 = document.getElementById(id2);
      if(s1.style.display=='none')
        {
           if(document.all)
              s1.style.display='inline';
           else
              s1.style.display='table-row';
           s2.src="images/ex-all.png";
        }
      else
        { 
           s1.style.display='none';
           s2.src="images/co-all.png";
        }
    }
</script>
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
<table width="80%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
		<tiles:insert page="topTitle.jsp" flush="true" />  
		</td>
	</tr>
    <tr><td style="text-align:left;BACKGROUND:#e8e8e8" id="link"> 
        <img height=18 width=18 src="images/host.gif">&nbsp;&nbsp;<span style="color:#27408B;font:bold;font-size:10pt">路径:</span>
        <a style="color:#27408B;font:bold;font-size:11pt" href="ftpSiteSnapshot.do?server=<bean:write name="server"/>">
        <bean:write name="server"/>--<bean:write name="username"/></a>&nbsp;
        <logic:iterate id="parentList" name="parentList"  indexId="index" > 
            &gt;<a style="color:#27408B;font:bold;font-size:10pt" href="ftpSiteSnapshot.do?server=<bean:write name="server"/>&parentPath=<bean:write name="parentList" property="encodedAbsolutePath"/>/">
            <bean:write name="parentList" property="relativePath"/></a>&nbsp;                   
        </logic:iterate>
        &gt;<span style="font-size:10pt"><bean:write name="selfPath"/></span>
        &nbsp;&nbsp;&nbsp;&nbsp;<a style="font-size:10pt" onClick="javascript:copyToClipboardObj(addr);return false;">[复制目录地址]</a>
        <span id="addr" class="hid"><bean:write name="ftpAddress" filter="ture"/></span>
      </td></tr>    
</table>
<table  width="80%" border="0" cellspacing="0" cellpadding="0" >
    <tr><td style="text-align:left"><a onClick="coex('tableFtpInfo','imgopen')">[点击查看<img style="vertical-align:middle" id="imgopen" src="images/co-all.png">此FTP信息]</a>
        &nbsp;&nbsp;<a href="app/ftp.jnlp">启动Rosa FTP</a>
        </td></tr>
    <tr id="tableFtpInfo" style="display:none"><td style="text-align:left"> 
        <bean:write name="ftpInfo" filter="ture"/>     
      </td></tr>    
   <tr><td height=8></td> </tr>
</table>
<table width="80%"  bgcolor=#dfdfef border="0" cellspacing="1" cellpadding="0"> 
  <tr bgcolor=#f5f8fe>
  	<TD width="50" style="text-align:right">
         <a href="ftpSiteSnapshot.do?sort=category&server=<bean:write name="server"/>&parentPath=<bean:write name="parentPath"/>">
           <bean:message key="ftpsearch.jsp.searchtrue.category"/>
         </a>
    </TD> 
	<TD style="text-align:center">
           <a href="ftpSiteSnapshot.do?sort=default&server=<bean:write name="server"/>&parentPath=<bean:write name="parentPath"/>">
              <bean:message key="ftpsearch.jsp.searchtrue.name"/>             		    
           </a>
	</TD>
	<TD width="70" style="text-align:center"  ><bean:message key="ftpsearch.jsp.searchtrue.size"/>
	      <a href="ftpSiteSnapshot.do?sort=sizedesc&server=<bean:write name="server"/>&parentPath=<bean:write name="parentPath"/>">降</a>
	      |<a href="ftpSiteSnapshot.do?sort=sizeasc&server=<bean:write name="server"/>&parentPath=<bean:write name="parentPath"/>">升</a> 
	</TD>
		<TD width="80" style="text-align:center"><bean:message key="ftpsearch.jsp.searchtrue.modifiedTime"/>
	     	<a href="ftpSiteSnapshot.do?sort=datedesc&server=<bean:write name="server"/>&parentPath=<bean:write name="parentPath"/>">降</a>
	       |<a href="ftpSiteSnapshot.do?sort=dateasc&server=<bean:write name="server"/>&parentPath=<bean:write name="parentPath"/>">升	</a>
	</TD>
	<TD width="40" style="text-align:center">操作</TD>	
  </tr>
  <logic:iterate id="outputList" name="outputList"  indexId="index" >   
  <tr bgColor=#<%=(index.intValue()%2==0)? "FFFFFF" : "f5f8fe" %>>
      <td style="text-align:right"> 
        <%=index.intValue()+1%>
        <img height=18 width=18 src="images/category/<bean:write name="outputList" property="category"/>.png">
      </td>
      <td style="text-align:left" >      
        <logic:equal name="outputList" property="category" value="directory">
          <a href="ftpSiteSnapshot.do?server=<bean:write name="server"/>&parentPath=<bean:write name="outputList" property="encodedPath"/>/">
          <bean:write name="outputList" property="name"/></a>
          &nbsp;&nbsp;<a title="在此目录内搜索" href="singleSiteSearch.do?server=<bean:write name="server"/>&keyword=<bean:write name="parentPath"/><bean:write name="outputList" property="name"/>/"><img height=14 width=14 border=0 style="vertical-align:middle;"  src="images/search.gif"/></a>
        </logic:equal>
        <logic:notEqual name="outputList" property="category" value="directory">
          <a target=_blank href="<bean:write name="outputList" property="absolutePath"/>" onClick="javascript:copyToClipboardObj(addr<%=index.intValue()%>);return false;">
          <bean:write name="outputList" property="name"/></a>         
        </logic:notEqual>  
      </td>
      <td style="text-align:center">
        <bean:write name="outputList" property="fileSize"/>
      </td>
      <td style="text-align:center">
         <bean:write name="outputList" property="modifiedTime"/>
      </td>  
       <td style="text-align:center"> 
        <span id="addr<%=index.intValue()%>" class="hid"><bean:write name="outputList" property="absolutePath" /></span>  
       <a title="用RosaWebFTP下载" href="#" onClick="javascript:RosaFTPDownload(addr<%=index.intValue()%>);return false;"><img border=0 style="vertical-align:middle;"  src="images/download.png"/></a>
       </td>     
  </tr>
  </logic:iterate>  
</table>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</BODY>
</HTML>