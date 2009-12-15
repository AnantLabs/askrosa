<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 


<html>

<head>
		<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<title><bean:message key="ftpsearch.jsp.searchtrue.title" /></title>
		<style type="text/css" media="screen">@import "./styles/styles.css";</style>
</head>
 
<body BGCOLOR="#FFFFFF" LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">	 
       <tiles:insert page="top.jsp" flush="true" /> 
       <TABLE WIDTH=80% BORDER=0 CELLPADDING=0 CELLSPACING=0 align="center">
        <tr>
        <td>
        <tiles:insert page="help.htm" flush="true" />
        </td>
        </tr>
        </TABLE>
</body>

</html>
