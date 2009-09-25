<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<html>
	<head>
		<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<title><bean:message key="ftpsearch.jsp.searchtrue.title" /></title>
		<style type="text/css" media="screen">@import "./styles/styles.css";</style>
        <script language="javascript" type="text/javascript" src="script/WdatePicker.js"></script> 
	</head>
<body BGCOLOR="#FFFFFF" LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">	 
<tiles:insert page="top.jsp" flush="true" /> 
<center> 
<table width="80%"  border="0" cellspacing="0" cellpadding="0">	
    <tr><td class=style25>&nbsp;</td></tr> 
    <tr><td class=style23>高级搜索</td> </tr>
    <tr><td class=style22>&nbsp;</td></tr> 
    <tr>
	  <td>      
	    <html:form action="/search.do" focus="keyword">      
	    <html:hidden property="pageIndex" value="advancedSearch"/>
		<table width=100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td style="text-align:right">类别选择：</td>
				<td style="text-align:left"><tiles:insert page="category.jsp" flush="true" /> </td>
				<td>&nbsp;</td>
				<td style="text-align:left">可以同时选择多组类别进行查询，不选等于全选</td>
			</tr>			
			<tr>
			    <td style="text-align:right">匹配方式：</td>
			    <td style="text-align:left">
			    <html:radio property="field" value="path"/>路径名
			    <html:radio property="field" value="name"/>文件名</td>
			    <td>&nbsp;</td>
			    <td style="text-align:left">以文件名只会显示与文件名字(不包括路径名)匹配的结果。 </td>	
			</tr>
			<tr>
			    <td style="text-align:right">搜索范围：</td>
			    <td style="text-align:left">
				    <html:multibox property="locations" value="0" />校内
				    <html:multibox property="locations" value="1" />教育
				    <html:multibox property="locations" value="2" />公网
			    </td>
			    <td>&nbsp;</td>
			    <td style="text-align:left">选择搜索校内站点，还是校内外全部站点</td>	
			</tr>			

			<tr>
				<td style="text-align:right"><bean:message key="ftpsearch.jsp.searchtrue.keywordInclude" />：</td>
				<td style="text-align:left"><html:text property="keyword" size="45" /></td>
				<td>&nbsp;</td>
				<td style="text-align:left">包含所填关键字的文件在搜索结果中将会列出</td>
			</tr> 
			<tr>
				<td style="text-align:right"></td>
				<td style="text-align:left" colspan=3><span class=errorlight><html:errors property="keyword"/></span></td>
			</tr> 
			<tr>
				<td style="text-align:right"><bean:message key="ftpsearch.jsp.searchtrue.keywordExclude" />：</td>
				<td style="text-align:left"><html:text property="keywordExclude" size="45" /></td>
				<td>&nbsp;</td>
				<td style="text-align:left">包含所填关键字的文件在搜索结果中将不会列出</td>
			</tr>
			<tr>
				<td style="text-align:right"></td>
				<td style="text-align:left" colspan=3><span class=highlight><html:errors property="keywordExclude"/></span></td>
			</tr> 
			<tr>
				<td style="text-align:right"><bean:message key="ftpsearch.jsp.searchtrue.date" />：</td>
				<td style="text-align:left"><input id="d5221" class="Wdate" type="text" size="10" name="dateFrom" onFocus="var d5222=$dp.$('d5222');WdatePicker({onpicked:function(){d5222.focus();},maxDate:'#F{$dp.$D(\'d5222\')}'});"/>
                     To: <input id="d5222" class="Wdate" type="text" size="10" name="dateTo" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d5221\')}'})"/>			
				</td>
				<td>&nbsp;</td>
				<td style="text-align:left">搜索文件创建时间在此公历日期起始时间之内的</td>
			</tr>	
			<tr>
				<td style="text-align:right"></td>
				<td style="text-align:left" colspan=3><span class=highlight><html:errors property="dateFrom"/></span>
				  <span class=highlight><html:errors property="dateTo"/></span>
				  </td>
			</tr> 							
			<tr>
			    <td style="text-align:right"><bean:message key="ftpsearch.jsp.searchtrue.ftpSite" />：</td>
			    <td style="text-align:left"><html:text property="server" size="45" /></td>
			    <td>&nbsp;</td>
			    <td style="text-align:left">用站点列表页填好此项，多站点查询参考使用说明</td>
			</tr>
			<tr>
				<td style="text-align:right"></td>
				<td style="text-align:left" colspan=3><span class=highlight><html:errors property="server"/></span></td>
			</tr> 			
			<tr>
			     <td style="text-align:right"><bean:message key="ftpsearch.jsp.searchtrue.access" />：</td>
			     <td style="text-align:left"><html:text property="access" size="45" /></td>
			     <td>&nbsp;</td>
			     <td style="text-align:left">通过访问控制码可以查询包括此访问控制的站点信息</td>
			</tr>
			<tr>
				<td style="text-align:right"></td>
				<td style="text-align:left" colspan=3><span class=highlight><html:errors property="access"/></span></td>
			</tr> 					
	    </table>
		<table width=100%" border="0" cellspacing="0" cellpadding="0">
		   <tr>
		     <td>
		      <html:submit><bean:message key="ftpsearch.jsp.searchtrue.search"/></html:submit>
		    </td>  
		   </tr>
		</table>			
    	</html:form>
      </td>
    </tr>
</table>	
<table><tr><td height=200></td></tr></table>		
<table>
   <tr><td>
    <tiles:insert page="welcomemainBottom.jsp" flush="true"/>
   </td></tr>
</table>			 
</center>
</body>
</html>
