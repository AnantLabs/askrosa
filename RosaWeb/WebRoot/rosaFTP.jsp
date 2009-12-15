<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	   <title><bean:message key="ftpsearch.jsp.navigator.rosaftp" /></title>
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
<TABLE  width="80%" cellSpacing=0 cellPadding=0  border=0>
	  <tr>
	   <td style="text-align:left">
	     <A href="/"><IMG alt="搜索引擎首页" src="images/rosa/rosa.jpg" width=200 vspace=5 border=0> </A>
	   </td>
	  </tr>
</TABLE>
<TABLE  width="80%" cellSpacing=0 cellPadding=0  border=0>
      <TR><TD style="text-align:left" width="100%">
      <HR noShade SIZE=1>
				<B>Rosa FTP</B><br>&nbsp;&nbsp; Rosa是基于Rosa FTP搜索引擎开发的FTP下载客户端（虽然实现了上传功能，但未提供GUI接口供使用）。  
      <HR noShade SIZE=1>
				<B>Rosa FTP的安装和使用</B><br> 
				<br>
				1 安装Rosa FTP <br>
				1.1 安装jre。<a href=""download/jrd6.exe">下载地址</a> <a href="search.do?keyword=jre 6u13 i586">Rosa搜索</a><br>
				1.2 <a href="app/ftp.jnlp">启动Rosa FTP</a><br>
				&nbsp;&nbsp;1.2.1 点击RosaWeb页面的 <a href="app/ftp.jnlp">启动Rosa FTP</a><br>
				&nbsp;&nbsp;1.2.2 需要验证的时候 点击确认：<br>
				<img border=0 style="vertical-align:middle;"  src="images/help/security.jpg"/><br>		    
				1.3 点击<img border=0 style="vertical-align:middle;"  src="images/download.png"/>进行下载。RosaFTP会弹出选择保存地址的对话框，选择保存地址后即可下载。<br>
				<br>
				2 Rosa FTP的安装位置<br>
				2.1 Rosa FTP的快捷方式<br>
				   2.1.1 桌面快捷方式<br>
				      <img border=0 style="vertical-align:middle;"  src="images/help/quickdesktop.jpg"/><br>
				   2.1.2 开始菜单快捷方式<br>
				      <img border=0 style="vertical-align:middle;"  src="images/help/quickbegin.jpg"/><br>
				2.2 Rosa FTP的保存位置<br>
				   保存在java缓存中<br>				      
				<br>
				3 Rosa FTP的卸载<br>
				  3.1 在java控制面板--&gt;添加删除程序中删除<br>
				      <img border=0 style="vertical-align:middle;"  src="images/help/uninstall.jpg"/><br>
				  3.2 清除java Rosa 临时Rosa FTP 文件 <br>
				      <img border=0 style="vertical-align:middle;"  src="images/help/javacontrol.jpg"/><br>
				      <img border=0 style="vertical-align:middle;"  src="images/help/delete1.jpg"/><br>
				      <img border=0 style="vertical-align:middle;"  src="images/help/delete2.jpg"/><br>
				      <img border=0 style="vertical-align:middle;"  src="images/help/delete3.jpg"/><br>
				  3.4 去除信任签名<br>
				      <img border=0 style="vertical-align:middle;"  src="images/help/deletesig.jpg"/><br>
				  3.5 如果您觉得没有必要使用jre也可以删除jre<br>
				      <img border=0 style="vertical-align:middle;"  src="images/help/uninstalljava.jpg"/><br>
				<HR noShade SIZE=1>
				<B>为什么开发Rosa FTP?</B><br> 
				&nbsp;&nbsp; 这个想法受到迅雷的启发，考虑到同一个资源在校内可能存在多分拷贝。如果能够充分利用这个特点，从多个FTP Server同时下载一个资源的话，速度将有所提升。对于提升的程度要视资源的重复程度和FTP Server的速度。 
				<HR noShade SIZE=1>
				<B>Rosa FTP的简单设计思路</B><br> 
				&nbsp;&nbsp; 结合Rosa FTP搜索引擎的RMI接口。对于一个资源，Rosa FTP使用这个接口来查询重复资源数，然后利用这些查询到的候选资源同时下载。下载之前对文件进行了分片。多个线程从这些分片组成的队列中取一个分片来完成。分片如果下载失败重新放回队列。<br>&nbsp;&nbsp; 考虑到续传的重要，对于用户下载过程中删除任务或退出程序的行为，Rosa FTP将下载元信息保存到下载文件的结尾。续传时从文件结尾读取信息然后开始续传。对于程序的异常终止，比如结束进程，目前还无法保存元信息来续传。<br>&nbsp;&nbsp; 同时Rosa FTP对剪贴板进行了监听，对于rosa://开头的URL进行下载提示。<br> 
			    <HR noShade SIZE=1>
				 <B>联系 Rosa</B><br>
				<a href="mailto:askrosateam@gmail.com">AskRosa Team</a>
			</TD>
		</TR>
</TABLE>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>	
</BODY>
</HTML>