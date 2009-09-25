<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

 			<table border="0" cellspacing="0" cellpadding="0">		
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
			</tr>
			</table>