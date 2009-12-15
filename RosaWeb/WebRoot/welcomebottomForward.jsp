<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="cn.askrosa.listener.OnlineCounter" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html>
	<head>
	    <meta  HTTP-EQUIV="refresh"  CONTENT="180"> 
	    <meta name="verify-v1" content="s+S6lFhJVOHSuJZVWFccNKf/TTZWTgZE1Sv4GaVezwQ=" />
		<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<title><bean:message key="ftpsearch.jsp.searchtrue.title" /></title>
		<style type="text/css" media="screen">@import "./styles/styles.css";</style>
	</head>

<body BGCOLOR="#FFFFFF" LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">	 
<ul id="navbar">
 <li><bean:write name="solarDate"/> <bean:write name="sFestivalName"/> <bean:write name="weekName"/></li>
 <li>农历:<bean:write name="lunarDate"/> <bean:write name="term"/> <bean:write name="lFestival"/> </li>
 <li><bean:message key="ftpsearch.jsp.users"/>:&nbsp;[<%=OnlineCounter.getOnline()%>]</li>
 <li><bean:message key="ftpsearch.jsp.historyusers"/>:&nbsp;[<bean:write name="users" property="count"/>]</li>
 <li>用户IP:[<bean:write name="remoteIp"/>]</li>
 <li>服务器IP:[<bean:write name="serverIP"/>]</li>
 <li>[<a href="javascript:window.location.reload()" >刷新</a>]</li>
 </ul>
</body>
</html>
