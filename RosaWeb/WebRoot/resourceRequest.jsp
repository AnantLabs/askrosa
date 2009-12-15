<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<title><bean:message key="ftpsearch.jsp.ftpsitelist.title" /></title>
		<script language="javascript" type="text/javascript" src="script/WdatePicker.js"></script>
		<style type="text/css" media="screen">@import "./styles/styles.css";</style>
	</head>	
<BODY BGCOLOR=#FFFFFF LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">
<tiles:insert page="top.jsp" flush="true" />  
<center> 
<table width="80%"  border="0" cellspacing="0" cellpadding="0">
    <tr><td class=style25>&nbsp;</td></tr> 
    <tr><td class=style23>资源请求</td> </tr>
    <tr><td class=style22>&nbsp;</td></tr> 
	<TR>
	   <TD>		
   		<html:form action="/resourceRequest.do">
		 <table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed">
		      <tr>
			   <td style="text-align:right" width=80>
		         <bean:message key="ftpsearch.jsp.request.nickname"/>： 			    
			    </td>
			   <td style="text-align:left" width=300>
                 <html:text property="request.nickname"/>
			    </td>
			    <td style="text-align:left">
                    您的昵称，可以不填，默认未匿名
			    </td>			    
			   </tr> 			     
			   <tr>
			     <td> </td>
			     <td style="text-align:left" colspan=2>
			     <span class=errorlight><html:errors property="request.nickname"/></span>
			     </td>
			   </tr> 
			   
		      <tr>
			   <td style="text-align:right">
		         <bean:message key="ftpsearch.jsp.request.resourcename"/>*：		    
			    </td>
			   <td style="text-align:left">
                  <html:text property="request.resourcename" size="40"/>
			    </td>
			    <td style="text-align:left">
                    请求资源的关键字，确定此关键字的搜索结果为空。
			    </td>			    
			   </tr> 			     
			   <tr>
			     <td> </td>
			     <td style="text-align:left" colspan=2>
			     <span class=errorlight><html:errors property="request.resourcename"/></span>
			     </td>
			   </tr> 
		
			      <tr>
			   <td style="text-align:right">
		         <bean:message key="ftpsearch.jsp.request.email"/>*：		    
			    </td>
			   <td style="text-align:left">
                  <html:text property="request.email" size="40"/>
			    </td>
			    <td style="text-align:left">
                    搜索到资源之后，您希望Rosa发送的Email地址
			    </td>			    
			   </tr> 			     
			   <tr>
			     <td> </td>
			     <td style="text-align:left" colspan=2>
			     <span class=errorlight><html:errors property="request.email"/></span>
			     </td>
			   </tr> 				   			   		    	     

				<tr>
			   <td style="text-align:right">
		           截止日期：	    
			    </td>
			   <td style="text-align:left">
                  <input id="d5221" class="Wdate" type="text" size="10" name="deadline" onFocus="WdatePicker()">
			    </td>
			    <td style="text-align:left">
                    在截止日期之后，我们将自动删除您的请求，空值为一个月之后。
			    </td>			    
			   </tr> 			     
			   <tr>
			     <td> </td>
			     <td style="text-align:left" colspan=2>
			     <span class=errorlight><html:errors property="request.deadline"/></span>
			     </td>
			   </tr> 	

				<tr>
			   <td style="text-align:right">
		           是否显示：	    
			    </td>
			   <td style="text-align:left">
                  	<html:select property="request.disply">
			              <option value="1">显示</option>
                          <option value="0">不显示</option>
			        </html:select>	 
			    </td>
			    <td style="text-align:left">
                    您是否希望我们把您的请求放在页面，让更多的人知道您的请求。
			    </td>			    
			   </tr> 			     
			   <tr>
			     <td> </td>
			     <td style="text-align:left" colspan=2>
			      <span class=errorlight><html:errors property="request.disply"/></span>
			     </td>
			   </tr> 			   
			   <tr>
			     <td style="text-align:center" colspan=3>
			      	 <html:submit>
				     <bean:message key="ftpsearch.jsp.regftpsite.submit"/>
				     </html:submit>
			     </td>
			   </tr> 		   			   		
		 </table>
		</html:form>          
	   </TD>
	</TR>
	<tr >
	<td style="text-align:left">
	注：1：系统根据您提交的关键字自动处理您的资源请求<br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	2：每次索引更新，系统查找到您请求的资源，如果存在自动向您发邮件提醒<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	3：资源名的填写建议使用搜索关键字，比如您需要<strong>量子危机清晰版</strong>，可以填写:<strong>量子危机清晰版</strong>，不需要再补上<strong>谢谢</strong>两字，这样方便系统的自动处理。</td>
	</tr>
  </TABLE>
  
		
<table>		
 <tr>
 <td>
  <tiles:insert page="welcomemainBottom.jsp" flush="true" />
 </td>
 </tr>
</table>

</center>
<script language="JavaScript" type="text/javascript">
<!--
function numberToString(num)
{
  if(num<10)return '0'+num;
  else return num.toString();
}

var objDate=new Date();//创建一个日期对象表示当前时间
var addTime=objDate.getTime()+30*24*3600000;//30天之后
objDate.setTime(addTime);
var year=objDate.getFullYear();
var month=objDate.getMonth()+1;    //getMonth返回的月份是从0开始的，因此要加1
var date=objDate.getDate();
document.getElementById('d5221').value=numberToString(year)+numberToString(month)+numberToString(date);
//-->  
</script>
</BODY></HTML>