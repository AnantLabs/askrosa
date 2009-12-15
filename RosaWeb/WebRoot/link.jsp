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
		<title>友情链接</title>
		<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<style type="text/css" media="screen">@import "./styles/styles.css";</style>
	</head>	
<BODY LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">
<tiles:insert page="top.jsp" flush="true" />  
<center> 
<table width="80%"  border="0" cellspacing="0" cellpadding="0">
    <tr><td class=style25>&nbsp;</td></tr> 
    <tr><td class=style23>友情链接</td> </tr>
    <tr><td class=style22>&nbsp;</td></tr> 
	<TR>
	   <TD>			
	     <TABLE width="100%" border="0" cellspacing="0" cellpadding="0"> 
     <tr><td class=style21 bgColor=#f5f8fe height=22 style="text-align:left" id="link">
           友情链接：(FTP 搜索)&nbsp; <img src="images/arrow.gif">&nbsp;
         <a href="http://thephy.nju.edu.cn" target="_blank">南大Thephy</a>|
         <a href="http://bingle.pku.edu.cn/" target="_blank">北大天网</a>|
         <a href="http://sheenk.com/ftpsearch/search.html" target="_blank">清华星空</a>|
         <a href="http://search.ustc.edu.cn/" target="_blank">中科大search</a>|
         <a href="http://grid.ustc.edu.cn/" target="_blank">中科大Grid</a>|
         <a href="http://search.ipcn.org/" target="_blank">IPCN</a>|
         <a href="http://www.fdigg.net/" target="_blank">华科大FileDigger</a>
         <a href="http://www.sowang.com/search/ftpsearch.htm" target="_blank"><img src="images/more.gif" border=0></a>  
           </td> </tr>
     <tr><td class=style24 height=22 style="text-align:left" id="link">
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          (校内搜索)&nbsp; <img src="images/arrow.gif">&nbsp; 
          <a href="http://202.119.47.8:8080/opac/search.php" target="_blank">图书信息查询</a>|
          <a href="http://lib.nju.edu.cn/resource/resource_zhongwen.php" target="_blank">中文电子资源</a>|
          <a href="http://lib.nju.edu.cn/resource/resource_waiwen.php" target="_blank">外文电子资源</a>|
          <a href="http://lib.nju.edu.cn/resource/resource_shiyong.php" target="_blank">试用电子资源</a>|
          <a href="http://bras.nju.edu.cn/" target="_blank">bras自助查询</a>|
          <a href="http://bbs.nju.edu.cn/bbsfind" target="_blank">百合文章查询</a>
            </td></tr>
	 <tr><td class=style24 bgColor=#f5f8fe height=22 style="text-align:left" id="link">                             
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          (常用搜索)&nbsp; <img src="images/arrow.gif">&nbsp;  
          <a href="http://www.google.cn" target="_blank">Google</a>|
          <a href="http://www.baidu.com" target="_blank">百度搜索</a>|
          <a href="http://www.sowang.com/sousuoyinqing.htm" target="_blank">强力搜索</a>|
          <a href="http://www.yahoo.cn/" target="_blank">雅虎搜索</a>|
          <a href="http://www.soso.com/" target="_blank">腾讯搜搜</a>|
          <a href="http://www.sogou.com/" target="_blank">搜狗搜索</a> 
          <a href="http://www.btchina.net/" target="_blank">BT@China</a>|
          <a href="http://bt.fkee.net/" target="_blank">飞客BT</a>  |
          <a href="http://www.gougou.com" target="_blank">狗狗搜索</a>|
          <a href="http://www.verycd.com" target="_blank">VeryCD</a>            
          <a href="http://www.sowang.com/" target="_blank"><img src="images/more.gif" border=0></a>  
          </td></tr>
     <tr><td class=style24 height=22  style="text-align:left" id="link">                             
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          (日常查询)&nbsp; <img src="images/arrow.gif">&nbsp; 
          <a href="http://www.hao123.com/haoserver/kuaidi.htm" target="_blank">快递查询</a>|
          <a href="http://flights.ctrip.com/Domestic/SearchFlights.aspx" target="_blank">航班查询</a>|
          <a href=" http://www.jlweather.com/nanjing_map.asp#" target="_blank">金陵气象</a>|
          <a href="http://www.jslw.gov.cn/homepage/index.jsp" target="_blank">江苏交通</a>|                
             <a href="http://www.ip138.com/" target="_blank">ip138</a>|
             <a href="http://www.hao123.com/haoserver/wn.htm" target="_blank">万年历</a>|
             <a href="http://dict.cn" target="_blank">Dict.cn</a>|
          <a href="http://detail.zol.com.cn/" target="_blank">ZOL产品报价</a>|
          <a href="http://www.pconline.com.cn/market/" target="_blank">太平洋电脑</a>|
          <a href="http://quote.eastmoney.com/quote1_1.html" target="_blank">东方财富</a>
          <a href="http://www.hao123.com/haoserver/index.htm" target="_blank"><img src="images/more.gif" border=0></a>
             </td></tr>
     <tr><td class=style24 bgColor=#f5f8fe height=22  style="text-align:left" id="link">           
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          (影视相关)&nbsp; <img src="images/arrow.gif">&nbsp; 
          <a href="http://nic.nju.edu.cn/index.php?option=com_wrapper&Itemid=27" target="_blank">南大网络电视</a>|   
          <a href="http://www.youku.com/" target="_blank">优酷</a>|
          <a href="http://www.tudou.com/" target="_blank">土豆</a>|
          <a href="http://video.sina.com.cn/" target="_blank">新浪视频</a>|
          <a href="http://kankan.xunlei.com/" target="_blank">迅雷看看</a>|                 
          <a href="http://www.imdb.cn" target="_blank">IMDB中文</a>|
          <a href="http://www.mtime.com/" target="_blank">时光网</a>|             
          <a href="http://www.shooter.cn/" target="_blank">射手网</a>|
          <a href="http://www.xjkyc.com/" target="_blank">新街口影城</a>|
          <a href="http://www.wandafilm.com/" target="_blank">万达影城</a>|
          <a href="http://www.njsycinema.com/" target="_blank">南京上影</a>
             </td></tr>
    <tr><td class=style24 height=22  style="text-align:left" id="link">            
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          (校内导航)&nbsp; <img src="images/arrow.gif">&nbsp; 
          <a href="http://go.nju.edu.cn" target="_blank">GO.nju</a>
          <a href="http://www.nju.edu.cn" target="_blank">南大首页</a>|
          <a href="http://lib.nju.edu.cn" target="_blank">南大图书馆</a>|
          <a href="http://bbs.nju.edu.cn" target="_blank">南大小百合</a>|
          <a href="http://cs.nju.edu.cn" target="_blank">计算机系</a>|
          <a href="http://jw.nju.edu.cn" target="_blank">教务处</a>| 
          <a href="http://grawww.nju.edu.cn/" target="_blank">研究生院</a>|  
          <a href="http://grawww.nju.edu.cn/mis/ygraphy/index.asp" target="_blank">研究生管理</a>         
             </td></tr>                      
         </TABLE>             
	   </TD>
	</TR>
  </TABLE>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</BODY></HTML>