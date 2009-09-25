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
   <script type="text/javascript" src="script/neverModules-autoComplete.js"></script>
	<script type="text/javascript">    
    //<![CDATA[
      /* 兼容IE 7.0以前版本 XMLHttpRequest */
      if (window.ActiveXObject) {
        window.XMLHttpRequest = function() {
          var x = null; var progIds = [
            'MSXML3.XMLHTTP.5.0',
            'MSXML3.XMLHTTP.4.0',
            'MSXML3.XMLHTTP.3.0',
            'MSXML3.XMLHTTP.2.0',
            'MSXML3.XMLHTTP',
            'MSXML2.XMLHTTP.5.0', 
            'MSXML2.XMLHTTP.4.0', 
            'MSXML2.XMLHTTP.3.0', 
            'MSXML2.XMLHTTP', 
            'Microsoft.XMLHTTP'];
          for (var i=0; i<progIds.length; i++) {
            try { x = new ActiveXObject(progIds[i]); break; } catch (ex) {};
          }; if (!x) throw new Error([0,"Can not create XMLHttpRequest Object"]);
          return x;
        };
      };

      /* Global XMLHttpRequest and autoComplete object */
      /* 全局的XMLHttpRequest对象和autoComplete对象 */
      var http = callbackHttp = null; var autoComplete = null;
      onload = function pageLoadHdle() {
        var completeConfiguration = {
          instanceName: "autoComplete",
          textbox: document.getElementById("keyword"),
          height: 200
        };
      
        /* Create a new instance of autoComplete */
        /* 创建自动完成的实例 */
        autoComplete = new neverModules.modules.autocomplete(completeConfiguration);
        autoComplete.useContent = true;
       
        /* 以下为可选项，这里的示例都是默认值 { */          
        /* 当useContent为false时，此功能生效，确定是否忽略速度，
        如果不忽略速度，则效率提高30%左右，（没测试过，估计的）
        ，也就是没有高亮功能，适合纯DHTML的匹配 */
        autoComplete.ignoreSpeed = false;
        /* 开启方向键（小键盘） */
        autoComplete.useArrow = true;
        /* 当数据量较大时，自动截取前部分的数据，提高效率 */
        autoComplete.autoSlice = true;
        /* 无论输入字符串在dataSource的何处，始终匹配 */
        autoComplete.ignoreWhere = true;
        /* 一个空格代表一个或多个字符（串） */
        autoComplete.useSpaceMatch = true;
        /* 忽略大小写 */
        autoComplete.ignoreCase = true;
        /* 以上为可选项，这里的示例都是默认值 } */       
        ///autoComplete.callback = callbackHdle;        
        /* Create autoComplete */
        /* 创建自动完成 */
        autoComplete.create();
        autoComplete.showAnimateImage("images/autocomplete_rightcap.gif");
      }

      function AjaxHdle (evt) {
        /* zhonghua Add 不处理回车键*/
        var keyCode = window.event?event.keyCode:evt.which;
        if (keyCode == 13) return;
        /* If the keycode is not a valid key or the query value in the cache, show autocomplete */
        /* 如果输入的键是有效的，即非方向键等无效键，或是在缓存中有该值，则显示自动完成 */       
        if (autoComplete.isValidKey(evt)==false || autoComplete.isRequireAjax()==false) {
          showAutocomplete(evt);
          return;
        }

        /* 显示loading图像 */
        autoComplete.showAnimateImage("images/indicator.gif");
        
        /* 异步获取数据 */
        http = new XMLHttpRequest();
        http.onreadystatechange = function() {loadCompleteData(evt)};
        var q=autoComplete.textbox.value;  
        q=encodeURI(q);
        http.open("GET", "autoComplete.do?q="+q, true);
        http.send(null);
        autoComplete.showAnimateImage("images/autocomplete_rightcap.gif");
      }
      
      /* 异步得到数据后加载dataSource */
      function loadCompleteData (evt) {
        if (http.readyState!=4) { return; }
        var dataSource = [];
        var _document = http.responseText;
        if(_document!='[')
        {
          dataSource = window.eval(_document);  
          autoComplete.setDataSource(dataSource);
          autoComplete.expandAllItem();      
        }
        else
        {
          autoComplete.setDataSource(dataSource);
        }
        http=null;
      }    

      /* 显示autoComplete */
      function showAutocomplete(evt) {
        autoComplete.hdleEvent(evt);
        window.setTimeout(function () {
          autoComplete.closeAnimateImage();
          },1000);
      }
      
      function loadCallbackData() {
        if (callbackHttp.readyState!=4) { return; }        
        window.setTimeout(function() {
          document.getElementById("loading").style.display="none";
          document.getElementById("tx").value = callbackHttp.responseText;
          callbackHttp = null;
          },1000); 
      }
    //]]>
    </script>
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
           s2.src="images/ex.png";
        }
      else
        { 
           s1.style.display='none';
           s2.src="images/co.png";
        }
    }
    function ex_all()
    {
      for(var i=<bean:write name="numberBegin"/>;i<=<bean:write name="numberEnd"/>;i++)
      {
        var tr="tr"+i; 
        var img="img"+i; 
        var s1 = document.getElementById(tr);
        var s2 = document.getElementById(img);      
        if(document.all)
            s1.style.display='inline';
        else
            s1.style.display='table-row';
        s2.src="images/ex.png";
      }      
    }
    function co_all()
    {
      for(var i=<bean:write name="numberBegin"/>;i<=<bean:write name="numberEnd"/>;i++)
      {
        var tr="tr"+i; 
        var img="img"+i; 
        var s1 = document.getElementById(tr);
        var s2 = document.getElementById(img);
        s1.style.display='none';
        s2.src="images/co.png";
      }      
    }
</script>
   <style type="text/css" media="screen">
     @import "./styles/styles.css";
     @import "./styles/autocomplete.css";
   </style>
   <style type="text/css">
   <!--
	   .mouselight{ color:red}
	   .mousere   { color:#2F4F4F}
	   td{
	    white-space: normal;
	    word-break: break-all;
	    }
   -->
   </style>
</HEAD>  	
<BODY BGCOLOR="#FFFFFF" LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">
<tiles:insert page="top.jsp" flush="true" />
<table>
<tr>
<td height=10>
</td>
</tr>
</table>
<center>    
<TABLE WIDTH=80% BORDER=0 CELLPADDING=0 CELLSPACING=0>
    <tr><td style="text-align:left">
		<table>
			<TR><TD style="text-align:left">
					<a href="aboutus.jsp" target="_blank">
					<img border="0" width=150 src="images/rosa/rosa.jpg" alt="<bean:message key="ftpsearch.jsp.navigator.aboutus" />">
					</a>
				</td>
				  <html:form action="/search.do" focus="keyword">
				   <html:hidden property="pageIndex" value="searchResult"/>
				<td style="text-align:left">  
				      <table border="0" cellspacing="0" cellpadding="0">
				      <tr>
					      <td style="text-align=center"> <tiles:insert page="category.jsp" flush="true" /></td>
				          <td style="text-align=left">
							  <table border="0" cellspacing="0" cellpadding="0">
								   <tr><td>
								  <html:radio property="field" value="name"/>文件名
							      <html:radio property="field" value="path"/>路径名
							      </td></tr>
							      <tr><td>
								    <html:multibox property="locations" value="0" />校内
								    <html:multibox property="locations" value="1" />教育
								    <html:multibox property="locations" value="2" />公网
							      </td></tr>
						      </table>	
					      </td>
					  </tr>
				      <tr>
				          <td style="text-align=center"> 
				          <input id="keyword" value="<bean:write name="searchForm" property="keyword"/>" name="keyword" autocomplete="off" onkeyup="AjaxHdle(event)"  ondblclick="autoComplete.expandAllItem();" style="HEIGHT: 21px; WIDTH:300px"/>
				          </td>
					      <td style="text-align=left"> <html:submit>Rosa <bean:message key="ftpsearch.jsp.searchtrue.search"/></html:submit>
					      <a href="app/ftp.jnlp">启动Rosa FTP</a>
					      </td>
				      </tr>	  
					  </table>		
		        </td>
		   		</html:form>  	
		    </tr>
		 </table>
	 </td></tr>
	<tr>
	   <td height=10 style="text-align:left">	
	     <span class=highlight><b><html:errors/></b></span>
	   </td>
	</tr>	
	<tr>
	   <td style="text-align:left;BORDER-TOP:#1E90FF 1px solid;BACKGROUND:#dfdfef">
            &nbsp;&nbsp;用户检索"<span class=highlight><bean:write name="keyword"/></span>"，耗时<bean:write name="delayTime"/>毫秒，
              共<bean:write name="resultNumber"/>个结果。<bean:write name="count"/>
	   </td>
	</tr>
	<tr>
	   <td height=10>
	   </td>
	</tr>	
	<TR>
	   <TD>	
			<TABLE WIDTH=100%  style="table-layout:fixed" bgcolor=#dfdfef BORDER=0 CELLPADDING=0 CELLSPACING=1>
              <TR bgcolor=#f5f8fe>
			    <TD class=tdButtomSolid width="<bean:write name="countlength"/>"style="text-align:center">
			         <a href="search.do?sort=category">
			         <span 
			           class=<logic:equal name="sort" value="category">
                              sort
                             </logic:equal>
			                 <logic:notEqual name="sort" value="category">
                              unsort
                              </logic:notEqual>>
			           <bean:message key="ftpsearch.jsp.searchtrue.category"/>
			          </span>
			         </a> 
			    </TD> 
                <TD class=tdButtomSolid style="text-align:center">
                    <a href="search.do?sort=default">
                    <span 
                      	class=<logic:equal name="sort" value="default">
                              sort
                             </logic:equal>
			                 <logic:notEqual name="sort" value="default">
                              unsort
                              </logic:notEqual>>
                      <bean:message key="ftpsearch.jsp.searchtrue.name"/></span>               		    
			        </a>
			        <a onClick="ex_all()"><font color=#666666> [全部<img style="vertical-align:middle;" src="images/ex-all.png">展开] </font></a>
			        <a onClick="co_all()"><font color=#9FB6CD> [全部<img style="vertical-align:middle;" src="images/co-all.png">合并] </font></a>
                </TD>
                <TD width="125" class=tdButtomSolid style="text-align:center" >
                    <a href="search.do?sort=server">
			        <span class=<logic:equal name="sort" value="server">
                              sort
                             </logic:equal>
			                 <logic:notEqual name="sort" value="server">
                              unsort
                              </logic:notEqual>>
			        <bean:message key="ftpsearch.jsp.searchtrue.serverIp"/></span>
			        </a>(<a href="search.do?location=0"><span class=location0>校内</span></a>
			        <a href="search.do?location=1"><span class=location1>教育</span></a>
			        <a href="search.do?location=2"><span class=location2>公网</span></a>)
                </TD>
				<TD width="70" class=tdButtomSolid style="text-align:center" >
				   <a href="search.do?sort=sizedesc">
			       <span class=<logic:equal name="sort" value="sizedesc">
                              sort
                             </logic:equal>
			                 <logic:notEqual name="sort" value="sizedesc">
                              unsort
                              </logic:notEqual>>			       
			         降</span>
			       </a>
			       |<a href="search.do?sort=sizeasc">
			       <span class=<logic:equal name="sort" value="ssizeasc">
                              sort
                             </logic:equal>
			                 <logic:notEqual name="sort" value="sizeasc">
                              unsort
                              </logic:notEqual>>			       
			         升</span>
			        </a> 
				</TD>
				<TD class=tdButtomSolid width="70" style="text-align:center">
				   	<a href="search.do?sort=datedesc">
			         <span class=<logic:equal name="sort" value="datedesc">
                              sort
                             </logic:equal>
			                 <logic:notEqual name="sort" value="datedesc">
                              unsort
                              </logic:notEqual>>			         
			         降</span>
			        </a>
			        |<a href="search.do?sort=dateasc">
			         <span class=<logic:equal name="sort" value="dateasc">
                              sort
                             </logic:equal>
			                 <logic:notEqual name="sort" value="dateasc">
                              unsort
                              </logic:notEqual>>			         
			          升</span>
			         </a>
				</TD>
				<TD class=tdButtomSolid width="45" style="text-align:center">操作</TD>	
              </TR>
            <% Integer pageIndexBegin=(Integer)request.getAttribute("numberBegin");%>
			<logic:iterate id="outputList" name="outputList"  indexId="index">	
			<tr bgcolor=#<%=(index.intValue()%2==0)? "FFFFFF" : "F5F8FE" %>>			
			  <TD style="text-align:right"><%=index.intValue()+pageIndexBegin%>&nbsp;<img height=20 width=20 style="vertical-align:middle;" src="images/category/<bean:write name="outputList" property="category" />.png"><img style="vertical-align:middle;" id="img<%=index.intValue()+pageIndexBegin%>" onClick="coex('tr<%=index.intValue()+pageIndexBegin%>','img<%=index.intValue()+pageIndexBegin%>')" src="images/co.png">
			  </TD>
			  <TD style="text-align:left">
                <table BORDER=0 CELLPADDING=0 CELLSPACING=0><tr><td height=5></td></tr></table>
                <table BORDER=0 CELLPADDING=0 CELLSPACING=0><tr><td style="text-align:left">
                <a target=_blank href="<bean:write name="outputList" property="absolutePath" />" 
                   onClick="javascript:copyToClipboardObj(addr<%=index.intValue()+pageIndexBegin%>);return false;">
                   <bean:write name="outputList" property="highlightedName" filter="ture"/></a>
                   <span id="addr<%=index.intValue()+pageIndexBegin%>" class="hid"><bean:write name="outputList" property="absolutePath" /></span>
                </td></tr>
                </table>
                <table BORDER=0 CELLPADDING=0 CELLSPACING=0><tr><td height=5></td></tr></table>
  			    </TD>
  			  <TD style="text-align:center">
				<a href="search.do?server=<bean:write name="outputList" property="server"/>" 
				   title="单击只显示此站点内的结果">
				   <span class=location<bean:write name="outputList" property="location"/>><bean:write name="outputList" property="server" /></span>
				</a>				
				</TD>
			  <TD style="text-align:right"><font color=#008800><bean:write name="outputList" property="fileSize" /></font>&nbsp;</TD>
			  <TD style="text-align:center"><bean:write name="outputList" property="modifiedTime" />&nbsp;</TD>
			  <TD style="text-align:center">
			  <a title="用RosaFTP下载" href="#" onClick="javascript:RosaFTPDownload(addr<%=index.intValue()+pageIndexBegin%>);return false;"><img border=0 style="vertical-align:middle;"  src="images/download.png"/></a>
			    <logic:equal name="outputList" property="category" value="directory">
                   <a title="查看目录之下文件" href="ftpSiteSnapshot.do?server=<bean:write name="outputList" property="server" />&username=<bean:write name="outputList" property="username" />&parentPath=<bean:write name="outputList" property="encodedPath" />/"><img border=0 style="vertical-align:middle;"  src="images/forward.png"/></a> 
                </logic:equal>
                <logic:notEqual name="outputList" property="category" value="directory">
                   <a title="查看文件所在目录" href="ftpSiteSnapshot.do?server=<bean:write name="outputList" property="server" />&username=<bean:write name="outputList" property="username" />&parentPath=<bean:write name="outputList" property="encodedParentPath" />"><img border=0 style="vertical-align:middle;"  src="images/forward.png"/></a>
                </logic:notEqual>  
				</TD>
			   </tr>
			  <tr id="tr<%=index.intValue()+pageIndexBegin%>" style="display:none" bgcolor=#<%=(index.intValue()%2==0)? "FFFFFF" : "F5F8FE" %>>
			  <td class=tdButtomSolid style="text-align:right;vertical-align:middle;color:#666666">路径<img style="vertical-align:middle;" src="images/line.png"></td>
			  <td id="link" class=tdButtomSolid colspan=5 style="text-align:left;valign:center;color:#666666;word-break:break-all">
			   <img src="images/arrow.gif">&nbsp;资源的详细路径:&nbsp;<bean:write name="outputList" property="highlightedPath" filter="ture"/> 
			   <br><img src="images/arrow.gif">&nbsp;FTP连接字符串:&nbsp;&nbsp;<bean:write name="outputList" property="ftpAddress" />
			      &nbsp;&nbsp;&nbsp;<a style="color:#008B45" href="" onClick="javascript:copyToClipboard('<bean:write name="outputList" property="ftpAddress" />');return false;">[复制此字符串]</a>
			      &nbsp;&nbsp;&nbsp;<a style="color:#008B45" href="ftpSiteSnapshot.do?server=<bean:write name="outputList" property="server"/>&username=<bean:write name="outputList" property="username"/>">[查看站点快照]</a>
			  </td>
			  </tr>
			</logic:iterate>                
            </TABLE>                 
	   </TD>
	</TR>	
	<tr>
	<td>
	   <table width=100%>
		 <tr>
		 <td>  
		  <font color="#0000ff"><bean:message key="ftpsearch.jsp.searchtrue.page"/>：</font>    
               <logic:iterate id="pageIndex" name="pageIndex"  indexId="index">	
               &nbsp;<a href="<bean:write name="pageIndex" property="link" />"><font color="<bean:write name="pageIndex" property="color"/>"><bean:write name="pageIndex" property="page"/></font></a>
               </logic:iterate>                             
		 </td>
		</tr>
	   </table>	
	</td>
	</tr>
</TABLE>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</BODY>
</HTML>
