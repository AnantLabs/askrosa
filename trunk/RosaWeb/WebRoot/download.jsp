<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>资料下载</title>
		<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<style type="text/css" media="screen">@import "./styles/styles.css";</style>
	</head>	
<BODY LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">
<tiles:insert page="top.jsp" flush="true" />  
<center> 
<table width="80%"  border="0" cellspacing="0" cellpadding="0">
    <tr><td class=style25>&nbsp;</td></tr> 
    <tr><td class=style23>下载页面</td> </tr>
    <tr><td class=style22>&nbsp;</td></tr> 
	<TR>
	   <TD>			
	     <TABLE width="100%" bgcolor=#dfdfef  border="0" cellspacing="1" cellpadding="0"> 
	     	<TR bgcolor="#FFFFFF">
              <TD align=right width=30 height=18><IMG src="images/arrow.gif" >&nbsp;1</TD>
              <TD style="text-align:left">&nbsp;&nbsp;我是一个普通的下载用户</TD>              
              <TD style="text-align:center" width=80>链接</TD>
              <TD width=70 >发布时间</TD>
            </TR>
	      	<TR bgcolor="#FFFFFF">
              <TD  width=30 height=18></TD>
              <TD style="text-align:left">&nbsp;&nbsp;Rosa综述（1.1M）</TD>
              <TD width=80><a href="download/rosa_overview.pdf" target=_blank><bean:message key="ftpsearch.jsp.searchtrue.download"/></TD>
              <TD width=70 >2009-09-12</TD>
            </TR>            
	      	<TR bgcolor="#F5F8FE">
              <TD  width=30 height=18></TD>
              <TD style="text-align:left">&nbsp;&nbsp;Rosa使用手册（1.9M）</TD>
              <TD width=80><a href="download/rosa_manual.pdf" target=_blank><bean:message key="ftpsearch.jsp.searchtrue.download"/></a></TD>
              <TD width=70 >2009-09-12</TD>
            </TR>            
	      	<TR bgcolor="#FFFFFF">
              <TD  width=30 height=18></TD>
              <TD style="text-align:left">&nbsp;&nbsp;Rosa设计说明（1.1M）</TD>
              <TD width=80><a href="download/rosa_design.pdf" target=_blank><bean:message key="ftpsearch.jsp.searchtrue.download"/></a></TD>
              <TD width=70 >2009-09-12</TD>
            </TR>
 	        <TR bgcolor="#F5F8FE">
              <TD  width=30 height=18></TD>
              <TD style="text-align:left">&nbsp;&nbsp;FlashFxp版本3.4.0下载（1.6M）</TD>
              <TD width=80><a href="download/FlashFXP.rar" target=_blank><bean:message key="ftpsearch.jsp.searchtrue.download"/></a></TD>
              <TD width=70 >2009-09-12</TD>
            </TR>
                       	     
 	     	<TR bgcolor="#FFFFFF">
              <TD align=right height=18><IMG src="images/arrow.gif" >&nbsp;2</TD>
              <TD style="text-align:left">&nbsp;&nbsp;我想搭建自己的FTP服务器</TD>              
              <TD style="text-align:center"></TD>
              <TD></TD>
            </TR>                                    
	        <TR bgcolor="#FFFFFF">
              <TD  height=18></TD>
              <TD style="text-align:left">&nbsp;&nbsp;Serv-U FTPServer_v6.3.0.1.rar（4.2M）</TD>
              <TD ><a href="download/Serv-U FTPServer_v6.3.0.1.rar" target=_blank><bean:message key="ftpsearch.jsp.searchtrue.download"/></a></TD>
              <TD >2009-09-12</TD>
            </TR>   
            <TR bgcolor="#FFFFFF">
              <TD align=right height=18><IMG src="images/arrow.gif" >&nbsp;3</TD>
              <TD style="text-align:left">&nbsp;&nbsp;我想利用Rosa的代码</TD>              
              <TD style="text-align:center"></TD>
              <TD></TD>
            </TR> 
  	        <TR bgcolor="#FFFFFF">
              <TD  height=18></TD>
              <TD style="text-align:left">&nbsp;&nbsp;Rosa安装程序，安装所需软件，使用说明，源码全部（209M）</TD>
              <TD ><a href="download/rosa_all.rar" target=_blank><bean:message key="ftpsearch.jsp.searchtrue.download"/></a></TD>
              <TD >2009-09-12</TD>
            </TR> 
  	        <TR bgcolor="#F5F8FE">
              <TD  height=18></TD>
              <TD style="text-align:left">&nbsp;&nbsp;如果您只需要源码（10M）</TD>
              <TD><a href="download/rosa_source.rar" target=_blank><bean:message key="ftpsearch.jsp.searchtrue.download"/></a></TD>
              <TD >2009-09-12</TD>
            </TR>              
 	        <TR bgcolor="#FFFFFF">
              <TD height=18></TD>
              <TD style="text-align:left">&nbsp;&nbsp;您还需自己下载安装MyEclipse</TD>
              <TD ><bean:message key="ftpsearch.jsp.searchtrue.download"/></TD>
              <TD >2009-09-12</TD>
            </TR>                       
         </TABLE>             
	   </TD>
	</TR>
  </TABLE>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</BODY></HTML>