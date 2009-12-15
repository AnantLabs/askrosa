<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<table id="advanceTable" style="display:none">
   <tr><td>
       <table>
		  <tr>
			 <td>
			 <html:multibox property="categories" value="video" />
			 <bean:message key="ftpsearch.jsp.ftpsitelist.video" />
			 </td>
			 <td>
			 <html:multibox property="categories" value="audio" />
			 <bean:message key="ftpsearch.jsp.ftpsitelist.audio" />
			 </td>
			 <td>
			 <html:multibox property="categories" value="compress" />
			 <bean:message key="ftpsearch.jsp.ftpsitelist.compress" />
			 </td>
			 <td>
             <html:multibox property="categories" value="image" />
			 <bean:message key="ftpsearch.jsp.ftpsitelist.image" />
			 </td>
			 <td>
			<html:multibox property="categories" value="executable" />
			<bean:message key="ftpsearch.jsp.ftpsitelist.executable" />	
			 </td>
			 <td style="text-align:center">
			   <html:radio property="field" value="name"/>文件名
			   <html:radio property="field" value="path"/>路径名
			 </td>
			</tr>
			
			<tr>
			<td>
			<html:multibox property="categories" value="text" />
			<bean:message key="ftpsearch.jsp.ftpsitelist.text" />			
			</td>
			<td>
			<html:multibox property="categories" value="document" />
			<bean:message key="ftpsearch.jsp.ftpsitelist.document" />			
			</td>
			<td>
			<html:multibox property="categories" value="directory" />
			<bean:message key="ftpsearch.jsp.ftpsitelist.directory" />	
			</td>
			<td>
			<html:multibox property="categories" value="program" />
			<bean:message key="ftpsearch.jsp.ftpsitelist.program" />		
			</td>
			<td>
			<html:multibox property="categories" value="subtitle" />
			<bean:message key="ftpsearch.jsp.ftpsitelist.subtitle" />	
		    </td>	
		    <td>
				<html:multibox property="locations" value="0" />校内
				<html:multibox property="locations" value="1" />教育
				<html:multibox property="locations" value="2" />公网
		    </td>		
			</tr>  
		</table>
   </td></tr>
   <tr><td>
	    <table>		
		    <tr>
			    <td style="text-align:left">
			       关键字不包含：<html:text property="keywordExclude" size="20" />
			    </td>
				<td style="text-align:left">日期范围：<input id="d5221" class="Wdate" type="text" value="<bean:write name="searchForm" property="dateFrom"/>" size="10" name="dateFrom" onFocus="var d5222=$dp.$('d5222');WdatePicker({onpicked:function(){d5222.focus();},maxDate:'#F{$dp.$D(\'d5222\')}'});"/>
		                     To: <input id="d5222" class="Wdate" type="text" value="<bean:write name="searchForm" property="dateTo"/>" size="10" name="dateTo" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d5221\')}'})"/>			
				</td>
		    </tr>
		    <tr>
			    <td style="text-align:left">
			     仅搜索此站点：<html:text property="server" size="20" />
			    </td>
			    <td style="text-align:left">
			     访问控制：<html:text property="access" size="30" />
				</td>
		    </tr>
		</table>
   </td></tr>
</table>