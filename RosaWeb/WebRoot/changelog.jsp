<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<title><bean:message key="ftpsearch.jsp.navigator.changelog" /></title>
		<style type="text/css" media="screen">@import "./styles/styles.css";</style>
	</head>	
<BODY LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0" onload=”initialize()”>
<tiles:insert page="top.jsp" flush="true" />  
<center> 
<iframe src="http://www.google.com/calendar/embed?src=0729hhsjq6lshttogcfr24bkgg%40group.calendar.google.com&ctz=Asia/Shanghai" style="border: 0" width="800" height="600" frameborder="0" scrolling="no"></iframe>
<script src=”http://wave-api.appspot.com/public/embed.js” type=”text/javascript”></script>
<table width="80%"  border="0" cellspacing="0" cellpadding="0">
    <tr><td class=style25>&nbsp;</td></tr> 
    <tr><td class=style23>更新历史</td> </tr>
    <tr><td class=style22>&nbsp;</td></tr>    
    	<TR>
	   <TD>			
	     <TABLE width="100%"  bgcolor=#dfdfef  border="0" cellspacing="1" cellpadding="0">     
	        
	        <tr bgcolor="#F5F8FE"><td colspan="3" style="text-align:center"><h4>ChangeLog of Version 1.1</h4></td></tr>
	        
	           <TR bgcolor="#F5F8FE">
              <TD width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;16</TD>
              <TD style="text-align:left">增加最近更新的视频资源提醒。</TD>
              <TD width=70 >2009-09-28</TD>
             </TR> 
	        
	        <TR bgcolor="#F5F8FE">
              <TD width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;15</TD>
              <TD style="text-align:left">重置了数据库中部分站点的两个属性：是否支持ls -lR和站点爬行周期。</TD>
              <TD width=70 >2009-09-09</TD>
             </TR> 
	        <TR bgcolor="#F5F8FE">
              <TD width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;14</TD>
              <TD style="text-align:left">修正爬虫程序，当索引中出现网络连接故障不完全抛弃已经获取的不完整数据</TD>
              <TD width=70 >2009-09-09</TD>
             </TR> 
	           <TR bgcolor="#F5F8FE">
              <TD width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;13</TD>
              <TD style="text-align:left">增加搜索建议功能，支持Firefox,IE,Opera等浏览器</TD>
              <TD width=70 >2009-04-10</TD>
             </TR> 
	           <TR bgcolor="#F5F8FE">
              <TD width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;12</TD>
              <TD style="text-align:left">增加基于文件后缀的搜索</TD>
              <TD width=70 >2009-04-10</TD>
             </TR> 
	           <TR bgcolor="#F5F8FE">
              <TD width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;11</TD>
              <TD style="text-align:left">增加基于站点位置的搜索，比如教育网，校园网，公网</TD>
              <TD width=70 >2009-04-10</TD>
             </TR> 
				<TR bgcolor="#F5F8FE">
              <TD width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;10</TD>
              <TD style="text-align:left">修改了FtpSearch的代码，修正了原来当用户点击页码数超过一定数值抛出异常的bug</TD>
              <TD width=70 >2009-04-04</TD>
             </TR> 
             <TR bgcolor="#F5F8FE">
              <TD width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;9</TD>
              <TD style="text-align:left">更新了lucene版本到2.4.1</TD>
              <TD width=70 >2009-04-04</TD>
             </TR> 
	           <TR bgcolor="#F5F8FE">
              <TD width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;8</TD>
              <TD style="text-align:left">用FTP4j替换Apache Common Net，尝试用代理索引教育网站点</TD>
              <TD width=70 >2009-03-25</TD>
             </TR>  
	        <TR bgcolor="#F5F8FE">
              <TD width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;7</TD>
              <TD style="text-align:left">新增资料下载，整合本站所有的下载资料</TD>
              <TD width=70 >2009-03-18</TD>
             </TR>	
	        <TR bgcolor="#FFFFFF">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;6</TD>
              <TD style="text-align:left">更改资源请求页面</TD>
              <TD width=70 >2009-03-18</TD>
             </TR>	
	        <TR bgcolor="#F5F8FE">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;5</TD>
              <TD style="text-align:left">FTP站点、留言板、资源请求分页搜索</TD>
              <TD width=70 >2009-03-18</TD>
             </TR>	
	        <TR bgcolor="#FFFFFF">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;4</TD>
              <TD style="text-align:left">更改留言板页面</TD>
              <TD width=70 >2009-03-18</TD>
             </TR>	
	        <TR bgcolor="#F5F8FE">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;3</TD>
              <TD style="text-align:left">增加快照功能</TD>
              <TD width=70 >2009-03-18</TD>
             </TR>	
	        <TR bgcolor="#FFFFFF">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;2</TD>
              <TD style="text-align:left">增加下载功能</TD>
              <TD width=70 >2009-03-18</TD>
             </TR>	
	        <TR bgcolor="#F5F8FE">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;1</TD>
              <TD style="text-align:left">修改搜索结果显示页面</TD>
              <TD width=70 >2009-03-18</TD>
             </TR>	      
         </TABLE>             
	   </TD>
	</TR> 
	<tr><td height=10></td></tr>    
	<TR>
	   <TD>			
	     <TABLE width="100%"  bgcolor=#dfdfef  border="0" cellspacing="1" cellpadding="0">     
	        <tr bgcolor="#F5F8FE"><td colspan="3" style="text-align:center"><h4>ChangeLog of Version 1.0</h4></td></tr>  
	        <TR bgcolor="#F5F8FE">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;13</TD>
              <TD style="text-align:left">更改站点登记页面</TD>
              <TD width=70 >2009-02-08</TD>
             </TR>	       
	        <TR bgcolor="#FFFFFF">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;12</TD>
              <TD style="text-align:left">增加友情链接</TD>
              <TD width=70 >2009-02-08</TD>
             </TR>
	        <TR bgcolor="#F5F8FE">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;11</TD>
              <TD style="text-align:left">增加留言板和资源请求页面</TD>
              <TD width=70 >2008-10-25</TD>
             </TR>
	     
	         <TR bgcolor="#FFFFFF">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;10</TD>
              <TD style="text-align:left">首页增加热点搜索排名</TD>
              <TD width=70 >2008-07-19</TD>
             </TR>	
	     
	         <TR bgcolor="#F5F8FE">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;9</TD>
              <TD style="text-align:left">增加支持FlashFxp导入的站点列表文件下载</TD>
              <TD width=70 >2008-07-19</TD>
             </TR>	
	     	     
	         <TR bgcolor="#FFFFFF">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;8</TD>
              <TD style="text-align:left">更改高级搜索页面查询解释，方便用户理解</TD>
              <TD width=70 >2008-06-06</TD>
             </TR>	
             
	         <TR bgcolor="#F5F8FE">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;7</TD>
              <TD style="text-align:left">更改高级搜索页面日期大小对比操作，增加页面农历显示</TD>
              <TD width=70 >2008-06-05</TD>
             </TR>	
             
	         <TR bgcolor="#FFFFFF">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;6</TD>
              <TD style="text-align:left">增加页面底部信息提示</TD>
              <TD width=70 >2008-06-03</TD>
             </TR>	 
             	     
	         <TR bgcolor="#F5F8FE">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;5</TD>
              <TD style="text-align:left">增加历史访问量统计</TD>
              <TD width=70 >2008-06-01</TD>
             </TR>	     

             <TR bgcolor="#FFFFFF">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;4</TD>
              <TD style="text-align:left">申请了域名http://askrosa.cn(校园网)，http://cs.nju.edu.cn:8888/RosaWeb(公网)</TD>
              <TD width=70 >2008-05-29</TD>
            </TR>
 
 	        <TR bgcolor="#F5F8FE">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;3</TD>
              <TD style="text-align:left">增加ChangeLog页面</TD>
              <TD width=70 >2008-05-27</TD>
            </TR>
                       	     
	        <TR bgcolor="#FFFFFF">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;2</TD>
              <TD style="text-align:left">首页布局调整</TD>
              <TD width=70 >2008-05-27</TD>
            </TR>   

	        <TR bgcolor="#F5F8FE">
              <TD  width=30 height=18><IMG src="images/arrow.gif" border=0>&nbsp;1</TD>
              <TD style="text-align:left">增加站点列表中的站点快照和单点搜索功能</TD>
              <TD width=70 >2008-05-27</TD>
            </TR>         
         </TABLE>             
	   </TD>
	</TR>
  </TABLE>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</BODY></HTML>