<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>

<HTML>
<HEAD>
   <meta http-equiv=Content-Type content="text/html;charset=utf-8">
   <TITLE><bean:message key="ftpsearch.jsp.searchtrue.title" /></TITLE>
   <script language="javascript" type="text/javascript" src="script/copy.js"></script>
   <script language="javascript" type="text/javascript" src="script/WdatePicker.js"></script>
   <script language="javascript" type="text/javascript" src="script/neverModules-autoComplete.js"></script>
   <script language="javascript" type="text/javascript" src="script/coex.js"></script>
   <script language="javascript" type="text/javascript" src="script/autoComplete.js"></script>

   <style type="text/css" media="screen">
     @import "./styles/styles.css";
     @import "./styles/autocomplete.css";
     <!--
	   td{
	    white-space: normal;
	    word-break: break-all;
	    }
     -->
   </style>
</HEAD>  	
<body>
<tiles:insert page="top.jsp" flush="true" />
<table><tr><td height=10></td></tr></table>
<center>
<TABLE WIDTH=80% BORDER=0 CELLPADDING=0 CELLSPACING=0>
   <html:form action="/search.do" focus="keywordInclude">
   <tr><td style="text-align:left">
	    <a href="aboutus.jsp" target="_blank"><img border="0" width=80 src="images/rosa/rosa.jpg" alt="<bean:message key="ftpsearch.jsp.navigator.aboutus" />"></a>
	    <input id="keywordInclude" value="<bean:write name="searchForm" property="keywordInclude"/>" name="keywordInclude" autocomplete="off" onkeyup="AjaxHdle(event)"  ondblclick="autoComplete.expandAllItem();" style="HEIGHT: 21px; WIDTH:300px"/>
	    <html:submit>Rosa <bean:message key="ftpsearch.jsp.searchtrue.search"/></html:submit>
	    <a href="app/ftp.jnlp">启动Rosa FTP</a>
	    <a id="advanceText" href="javascript:void(0);" onClick="coexText('advanceTable','advanceText')">显示高级搜索</a>	
   </td></tr>
   <tr><td style="text-align:left">
   <tiles:insert page="advanceSearch.jsp" flush="true" />			   
   </td></tr>
      <tr>
	   <td height=5 style="text-align:left">	
	     <span class=highlight><b><html:errors/></b></span>
	   </td>
   </tr>
   </html:form>	
	<tr>
	   <td style="text-align:left;BORDER-TOP:#1E90FF 1px solid;BACKGROUND:#dfdfef">
            &nbsp;&nbsp;用户检索"<span class=highlight><bean:write name="searchForm" property="keywordInclude"/></span>"，耗时<bean:write name="delayTime"/>毫秒，
              共<bean:write name="resultNumber"/>个结果。<bean:write name="count"/>
	   </td>
	</tr>
	<tr> <td height=10> </td></tr>	
	<TR><TD>	
			<TABLE WIDTH=100%  style="table-layout:fixed" bgcolor=#dfdfef BORDER=0 CELLPADDING=0 CELLSPACING=1>
              <TR bgcolor=#f5f8fe>
			    <TD class=tdButtomSolid width="<bean:write name="countlength"/>"style="text-align:center">
                       分类
                    <a title="点击按分类排列结果"  href="search.do?sort=category"><img id="category" border=0 src="images/down.png"></a>    
			    </TD> 
                <TD class=tdButtomSolid style="text-align:center">
                       名称
                    <a title="点击按默认排列结果"  href="search.do?sort=default"><img id="default" border=0 src="images/down.png"></a>     		    
			        <a onClick="coexall('coexall','imgall','textall')"><font color=#666666> [全部<img id="imgall" style="vertical-align:middle;" src="images/co-all.png"><span id="textall">展开</span>] </font></a>
                    <input type=hidden id="coexall" value="co"/>
                </TD>
                <TD width="125" class=tdButtomSolid style="text-align:center" >
    			     站点
			        <a title="点击按站点排列结果"  href="search.do?sort=ftpsite"><img id="ftpsite" border=0 src="images/down.png"></a>
			        (<a href="search.do?location=0"><span class=location0>校</span></a>
			        <a href="search.do?location=1"><span class=location1>教</span></a>
			        <a href="search.do?location=2"><span class=location2>公</span></a>)
                </TD>
				<TD width="70" class=tdButtomSolid style="text-align:center" >
			         大小
			       <a title="点击按大小降序排列结果" href="search.do?sort=sizedesc"><img id="sizedesc" border=0 src="images/down.png"></a>
			       <a title="点击按大小升序排列结果" href="search.do?sort=sizeasc"><img id="sizeasc" border=0 src="images/up.png"></a>
				</TD>
				<TD class=tdButtomSolid width="70" style="text-align:center">
			         日期
			       <a title="点击按日期降序排列结果"  href="search.do?sort=datedesc"><img id="datedesc" border=0 src="images/down.png"></a>
			       <a title="点击按日期升序排列结果"  href="search.do?sort=dateasc"><img id="dateasc" border=0 src="images/up.png"></a>
				</TD>
				<TD class=tdButtomSolid width="45" style="text-align:center">操作</TD>	
              </TR>
            <%Integer pageIndexBegin=(Integer)request.getAttribute("numberBegin");%>
			<logic:iterate id="List" name="outputList"  indexId="index">	
			<tr bgcolor=#<%=(index.intValue()%2==0)? "FFFFFF" : "F5F8FE" %>>			
				  <TD style="text-align:right"><%=index.intValue()+pageIndexBegin+1%>&nbsp;<a title="点击只显示此类别的结果" href="search.do?category=<bean:write name="List" property="category" />"><img height=20 width=20 style="vertical-align:middle;" border=0 src="images/category/<bean:write name="List" property="category" />.png"></a><img style="vertical-align:middle;" id="img<%=index.intValue()%>" onClick="coex('tr<%=index.intValue()%>','img<%=index.intValue()%>')" src="images/co.png">
				  </TD>
				  <TD style="text-align:left">  
	                <a target=_blank href="<bean:write name="List" property="absolutePath" />" 
	                   onClick="javascript:copyToClipboardObj(addr<%=index.intValue()+pageIndexBegin%>);return false;">
	                   <bean:write name="List" property="highlightedName" filter="ture"/></a>
	                   <span id="addr<%=index.intValue()+pageIndexBegin%>" class="hid"><bean:write name="List" property="absolutePath" /></span>
	  			  </TD>
	  			  <TD style="text-align:center">
					<a href="search.do?serverP=<bean:write name="List" property="server"/>" 
					   title="单击只显示此站点内的结果">
					   <span class=location<bean:write name="List" property="location"/>><bean:write name="List" property="server" /></span>
					</a>				
				  </TD>
				  <TD style="text-align:right"><font color=#008800><bean:write name="List" property="fileSize" /></font>&nbsp;</TD>
				  <TD style="text-align:center"><bean:write name="List" property="modifiedTime" />&nbsp;</TD>
				  <TD style="text-align:center">
				  <a title="用RosaFTP下载" href="#" onClick="javascript:RosaFTPDownload(addr<%=index.intValue()+pageIndexBegin%>);return false;"><img border=0 style="vertical-align:middle;"  src="images/download.png"/></a>
				    <logic:equal name="List" property="category" value="directory">
	                   <a title="查看目录之下文件" href="ftpSiteSnapshot.do?server=<bean:write name="List" property="server" />&username=<bean:write name="List" property="username" />&parentPath=<bean:write name="List" property="encodedPath" />/"><img border=0 style="vertical-align:middle;"  src="images/forward.png"/></a> 
	                </logic:equal>
	                <logic:notEqual name="List" property="category" value="directory">
	                   <a title="查看文件所在目录" href="ftpSiteSnapshot.do?server=<bean:write name="List" property="server" />&username=<bean:write name="List" property="username" />&parentPath=<bean:write name="List" property="encodedParentPath" />"><img border=0 style="vertical-align:middle;"  src="images/forward.png"/></a>
	                </logic:notEqual>  
					</TD>
			  </tr>
			  <tr id="tr<%=index.intValue()%>" style="display:none" bgcolor=#<%=(index.intValue()%2==0)? "FFFFFF" : "F5F8FE" %>>
			  <td class=tdButtomSolid style="text-align:right;vertical-align:middle;color:#666666">路径<img style="vertical-align:middle;" src="images/line.png"></td>
			  <td id="link" class=tdButtomSolid colspan=5 style="text-align:left;valign:center;color:#666666;word-break:break-all">
			   <img src="images/arrow.gif">&nbsp;资源的详细路径:&nbsp;<bean:write name="List" property="highlightedPath" filter="ture"/> 
			   <br><img src="images/arrow.gif">&nbsp;FTP连接字符串:&nbsp;&nbsp;<bean:write name="List" property="ftpAddress" />
			      &nbsp;&nbsp;&nbsp;<a style="color:#008B45" href="" onClick="javascript:copyToClipboard('<bean:write name="List" property="ftpAddress" />');return false;">[复制此字符串]</a>
			      &nbsp;&nbsp;&nbsp;<a style="color:#008B45" href="ftpSiteSnapshot.do?server=<bean:write name="List" property="server"/>&username=<bean:write name="List" property="username"/>">[查看站点快照]</a>
			  </td>
			  </tr>
			</logic:iterate>                
            </TABLE>                 
	</TD></TR>	
	<tr><td>
	   <table width=100%>
		 <tr>
		 <td>  
		  <font color="#0000ff"><bean:message key="ftpsearch.jsp.searchtrue.page"/>：</font>    
               <logic:iterate id="pages" name="pages"  indexId="index">	
               &nbsp;<a href="<bean:write name="pages" property="link" />"><font color="<bean:write name="pages" property="color"/>"><bean:write name="pages" property="page"/></font></a>
               </logic:iterate>                             
		 </td>
		</tr>
	   </table>	
	</td></tr>
</TABLE>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />

<script  type="text/javascript">
    var sort = document.getElementById('<bean:write name="searchForm" property="sort"/>');
    sort.src='images/<bean:write name="searchForm" property="sort"/>.png';
</script>

</center>
</BODY>
</HTML>
