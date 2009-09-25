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
      <title><bean:message key="ftpsearch.jsp.regftpsite.title"/></title>
	  <style type="text/css" media="screen">@import "./styles/styles.css";</style>
</head>
<BODY BGCOLOR=#FFFFFF LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">
<tiles:insert page="top.jsp" flush="true" /> 
<center>
          <table WIDTH=100% BORDER=0 CELLPADDING=0 CELLSPACING=0 align="center">
          <tr>          
           <td align="center">
           <br>
           <bean:write name="infor"/>
           <a href="<bean:write name="forwarddo"/>"><bean:write name="forward"/></a>
           </td>
          </tr>          
         </table>
</center>
         
</body>
</html>