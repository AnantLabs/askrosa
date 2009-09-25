<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
       <meta http-equiv=Content-Type content="text/html;charset=utf-8">
       <title><bean:message key="ftpsearch.jsp.navigator.statistics"/></title>
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
         年度高频词汇统计：<br>                   
        <logic:iterate id="result" name="statisticsResult"  indexId="index">
           <a href="search.do?keyword=<bean:write name="result" property="keyword"/>"><bean:write name="result" property="keyword"/>(<bean:write name="result" property="frequency"/>)</a> &nbsp;                                       
        </logic:iterate> 
      </td>
      </tr>
     <tr>
        <td style="align:center"><img src="statistics/hist.png" width=600 vspace=5 border=0 style="align:center"><br>
      说明：此图统计了距今24小时流量，每一小时更新一次。横坐标表示时间（24小时制），纵坐标表示访问人数。</td>
     </tr>
  </table>	
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</body>
</html>