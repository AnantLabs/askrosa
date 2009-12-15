<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<html>
<head>
<meta http-equiv=Content-Type content="text/html;charset=utf-8">
<title>info</title>
</head>
<BODY>
<center>
<script language="JavaScript">  
alert('<bean:write name="infor"/>');
history.go(-1);
</script>
</center>
</body>
</html>