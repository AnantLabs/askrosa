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
		<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<title><bean:message key="ftpsearch.jsp.regftpsite.title" /></title>
		<style type="text/css" media="screen">@import "./styles/styles.css";</style>
<script type="text/javascript">
function coex(id1,id2)
    {
      var s1 = document.getElementById(id1);
      var s2 = document.getElementById(id2);
      if(s1.style.display=='none')
        {
           if(document.all)
              s1.style.display='inline';
           else
              s1.style.display='table-row';
           s2.src="images/ex-all.png";
        }
      else
        { 
           s1.style.display='none';
           s2.src="images/co-all.png";
        }
    }
</script>
	</head>	
<BODY BGCOLOR=#FFFFFF LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">
<tiles:insert page="top.jsp" flush="true" />  
<center> 
<table width="80%"  border="0" cellspacing="0" cellpadding="0">
    <tr><td class=style25>&nbsp;</td></tr> 
    <tr><td class=style23>增加站点</td> </tr>
    <tr><td class=style22>&nbsp;</td></tr> 
	<TR><TD>			
     <html:form action="/ftpSiteReg.do">
           <table width="100%"  border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td style="text-align:left"> 
                    基本选项：
                </td>
              </tr>
           </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed">
			<tr><td style="text-align:right" width=80><bean:message key="ftpsearch.jsp.regftpsite.server"/>：</td>
			    <td style="text-align:left" width=250><html:text property="ftpSiteInfo.server" size="30" /></td>
		        <td style="text-align:left">FTP域名或IP地址</td>  
		    </tr>
		    <tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="ftpSiteInfo.server"/></span>
		        </td>
		    <tr>
		    <tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.regftpsite.username"/>：</td>
			    <td style="text-align:left"><html:text property="ftpSiteInfo.username" size="30" /></td>
			    <td style="text-align:left">FTP用户名，默认anonymous，如果允许匿名访问，请不要改动</td>
			</tr>
			<tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="ftpSiteInfo.username"/></span>
		        </td>
		    <tr>
			<tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.regftpsite.password"/>：</td>
			    <td style="text-align:left"><html:text property="ftpSiteInfo.password" size="30" /></td>
			    <td style="text-align:left">FTP密码，默认anonymous，匿名访问无需密码</td>
			</tr>
			<tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="ftpSiteInfo.password"/></span>
		        </td>
		    <tr>
			<tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.regftpsite.verify"/>：</td>
			    <td style="text-align:left"><html:password property="ftpSiteInfo.verify" size="30" /></td>
			    <td style="text-align:left">为保证只有注册的人具有修改FTP信息的权限，用验证码来确认</td>
			</tr>
			<tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="ftpSiteInfo.verify"/></span>
		        </td>
		    <tr>		
			<tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.regftpsite.verifyConfirm"/>：</td>
			    <td style="text-align:left"><html:password property="verifyConfirm" size="30" /></td>
			    <td style="text-align:left">验证码确认</td>
			</tr>
			<tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="verifyConfirm"/></span>
		        </td>
		    <tr>
		    </table>
		    
		    <table width="100%"  border="0" cellspacing="0" cellpadding="0">
              <tr><td height=10></td></tr>		    
              <tr>
                <td style="text-align:left"> 
                 <a onClick="coex('tableadvance','imgopen')">[更多高<img style="vertical-align:middle" id="imgopen" src="images/co-all.png">级选项]:</a>
                </td>
              </tr>
              <tr> <td height=10></td></tr>	
            </table>
		   
		    <table id="tableadvance" width="100%"  border="0" cellspacing="0" cellpadding="0" style="display:none;table-layout:fixed">	    		    
			<tr><td style="text-align:right" width=80><bean:message key="ftpsearch.jsp.regftpsite.admin"/>：</td>
			    <td style="text-align:left" width=250><html:text property="ftpSiteInfo.admin" size="30" /></td>
			    <td style="text-align:left">FTP管理员,默认为UNKNOWN,注册者更具自己的情况命名</td>
			</tr>
			<tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="ftpSiteInfo.admin"/></span>
		        </td>
		    <tr>			
			<tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.regftpsite.contact"/>：</td>
			    <td style="text-align:left"><html:text property="ftpSiteInfo.contact" size="30"/></td>
			    <td style="text-align:left">管理员联系方式，邮箱、百合账号或其他</td>
			</tr>
			<tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="ftpSiteInfo.contact"/></span>
		        </td>
		    <tr>
		    <tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.regftpsite.port"/>：</td>
				<td style="text-align:left"><html:text property="ftpSiteInfo.port" size="30"/></td>
                <td style="text-align:left">FTP端口号，默认21</td>
			</tr>
			<tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="ftpSiteInfo.port"/></span>
		        </td>
		    <tr>			
			<tr><td style="text-align:right""><bean:message key="ftpsearch.jsp.regftpsite.crawlInterval"/>：</td>
			    <td style="text-align:left"><html:text property="ftpSiteInfo.crawlInterval" size="30" /></td>
		        <td style="text-align:left">索引程序对此站点进行索引的周期，默认为1天</td>
		    </tr>
		    <tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="ftpSiteInfo.crawlInterval"/></span>
		        </td>
		    <tr>			
			<tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.regftpsite.speed"/>：</td>
			    <td style="text-align:left"><html:text property="ftpSiteInfo.speed" size="30" /></td>
			    <td style="text-align:left">FTP速度限制，默认0，表示不限速</td>
			</tr>
			<tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="ftpSiteInfo.speed"/></span>
		        </td>
		    <tr>			
			<tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.regftpsite.userslimit"/>：</td>
			    <td style="text-align:left"><html:text property="ftpSiteInfo.userslimit" size="30" /></td>
			    <td style="text-align:left">FTP用户数限制，默认0，表示不限用户数</td>
			</tr>
			<tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="ftpSiteInfo.userslimit"/></span>
		        </td>
		    <tr>		
			<tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.regftpsite.description"/>：</td>
			    <td style="text-align:left"><html:textarea property="ftpSiteInfo.description" cols="26" rows="5"/></td>
			    <td style="text-align:left">FTP描述信息，FTP内容和作用等</td>
			</tr>
			<tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="ftpSiteInfo.description"/></span>
		        </td>
		    <tr>			
			<tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.regftpsite.encoding"/>：</td>
			    <td style="text-align:left">
			    <html:select property="ftpSiteInfo.encoding" >
	                <option value="GBK">GBK</option>
	                <option value="UTF-8">UTF-8</option>
	                <option value="GB2312">GB2312</option>
		            </html:select></td>
			    <td style="text-align:left">FTP传输编码，默认GBK，可选的有UTF-8和GB2312</td>
			</tr>				
			<tr><td style="text-align:right">站点位置：</td>
			    <td style="text-align:left"><html:select property="ftpSiteInfo.location" >
	                <option value=0>校园网</option>
	                <option value=1>教育网</option>
	                <option value=2>公共网</option>
		            </html:select></td>
			    <td style="text-align:left">FTP站点的位置,根据您的填写，我们采用不同的访问方式</td>
			</tr>		
			<tr><td style="text-align:right"><bean:message key="ftpsearch.jsp.regftpsite.access"/>：</td>
			    <td style="text-align:left"><html:text property="ftpSiteInfo.access" size="30" /></td>
			    <td style="text-align:left">默认anybody，表示FTP可以被任何人检索到，其他字串用户在检索时需提供此字串</td>
			</tr>
			<tr>
		        <td></td>
		        <td style="text-align:left" colspan=2>
		           <span class=errorlight><html:errors property="ftpSiteInfo.access"/></span>
		        </td>
		    <tr>	
		 </table>
		 <table width="100%"  border="0" cellspacing="0" cellpadding="0">
			<tr><td style="text-align:center">
				<html:submit><bean:message key="ftpsearch.jsp.regftpsite.submit"/></html:submit>
			    </td></tr>    
			<tr><td style="text-align:center">
	              注意：为了防止错误提交，提交的时候，必须保证FTP站点能够登陆进入，否则不会收录！如果登记不上，可联系我们帮您登记。
		        </td></tr>	
           </table>
		 </html:form>   	 
	  </TD></TR>
</TABLE> 

<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</BODY></HTML>