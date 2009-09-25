<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html>
	<head>
	    <meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<title><bean:message key="ftpsearch.jsp.searchtrue.title" /></title>
        <style type="text/css" media="screen">
          @import "./styles/styles.css";
          @import "./styles/autocomplete.css";
        </style>        
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
          ///animateImage:"images/autocomplete_rightcap.gif"
        };
      
        /* Create a new instance of autoComplete */
        /* 创建自动完成的实例 */
        autoComplete = new neverModules.modules.autocomplete(completeConfiguration);
        autoComplete.useContent = true;
        ///autoComplete.animateImage = {src:"images/autocomplete_rightcap.gif"};
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
        autoComplete.showAnimateImage("images/autocomplete_rightcap.gif");
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
</head>
<body BGCOLOR="#FFFFFF" LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 rightmargin="0" bottommargin="0">	 
<tiles:insert page="top.jsp" flush="true" />
<center> 
<html:form styleId="searchForm" action="/search.do" focus="keyword">
<table width="10" height="46">
   <tr>
     <td height=40><a style="color:#ff0000;font:bold;font-size:11pt" href="mailto:askrosateam@gmail.com?subject=Donation domain name">近期学校网络中心DNS对公网域名askrosa.cn解析频繁抽筋，如果哪位有rosa.nju.edu.cn这样的域名可以贡献或申请，万分感谢！</a></td>
   </tr>
</table>	
<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td >
		 <a href="aboutus.jsp" target="_blank"><img border="0" width="200" src="images/rosa/rosa.jpg" alt="<bean:message key="ftpsearch.jsp.navigator.aboutus" />"> </a>
		</td>
	</tr>
</table>
<table cellpadding=0 cellspacing=0 border="0">	
	<tr>	 
		<td style="text-align:left">
		    <html:hidden property="pageIndex" value="welcomemainIndex"/>
		    <input id="keyword" name="keyword" autocomplete="off" value="<bean:write name="searchForm" property="keyword"/>" onkeyup="AjaxHdle(event)"  ondblclick="autoComplete.expandAllItem();" style="HEIGHT: 21px; WIDTH:350px"/>
			<html:submit>Rosa <bean:message key="ftpsearch.jsp.searchtrue.search"/></html:submit>
			<a href="advancedSearch.jsp"><bean:message key="ftpsearch.jsp.searchtrue.advancedSearch"/></a>					
		</td>
	</tr>
	<tr>	 
		<td style="text-align:left">
	     <span class=errorlight><html:errors/></span>				
		</td>
	</tr>
</table>		
<table>		
    <tr>
	    <td style="text-align:left">
	       <tiles:insert page="category.jsp" flush="true" /> 
	    </td>
	    <td style="text-align:left">
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
</table>	
</html:form>
<table cellpadding=0 cellspacing=0 border="0">	
     <tr><td height=10> </td></tr>				
     <tr><td class=style21 bgColor=#f5f8fe height=22 style="text-align:left">                
           热门搜索：<img src="images/hot.gif" >&nbsp;&nbsp;                  
         <logic:iterate id="result" name="statisticsResult">
         <a href="search.do?keyword=<bean:write name="result" property="encodedKeyword"/>"><bean:write name="result" property="keyword"/>(<bean:write name="result" property="frequency"/>)</a>                                       
         </logic:iterate></td></tr>
     <tr><td class=style24 height=22 style="text-align:left" >                
           热门站点：<img src="images/hot.gif" >&nbsp;&nbsp;
           <logic:iterate id="list" name="hotFtpSite">
              <a href="ftpSiteSnapshot.do?server=<bean:write name="list" property="server"/>&username=<bean:write name="list" property="username"/>"><bean:write name="list" property="server"/>(<bean:write name="list" property="hot"/>)</a>&nbsp;
           </logic:iterate>
          </td></tr> 
     <tr><td height=10> </td></tr>                
     <tr><td class=style21 bgColor=#f5f8fe height=22 style="text-align:left" id="link">
           友情链接：(FTP 搜索)&nbsp; <img src="images/arrow.gif">&nbsp;
         <a href="http://thephy.nju.edu.cn" target="_blank">南大Thephy</a>|
         <a href="http://bingle.pku.edu.cn/" target="_blank">北大天网</a>|
         <a href="http://sheenk.com/ftpsearch/search.html" target="_blank">清华星空</a>|
         <a href="http://search.ustc.edu.cn/" target="_blank">中科大search</a>|
         <a href="http://grid.ustc.edu.cn/" target="_blank">中科大Grid</a>|
         <a href="http://search.ipcn.org/" target="_blank">IPCN</a>|
         <a href="http://www.fdigg.net/" target="_blank">华科大FileDigger</a>
         <a href="http://www.sowang.com/search/ftpsearch.htm" target="_blank"><img src="images/more.gif" border=0></a>  
           </td> </tr>
     <tr><td class=style24 height=22 style="text-align:left" id="link">
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          (校内搜索)&nbsp; <img src="images/arrow.gif">&nbsp; 
          <a href="http://202.119.47.8:8080/opac/search.php" target="_blank">图书信息查询</a>|
          <a href="http://lib.nju.edu.cn/resource/resource_zhongwen.php" target="_blank">中文电子资源</a>|
          <a href="http://lib.nju.edu.cn/resource/resource_waiwen.php" target="_blank">外文电子资源</a>|
          <a href="http://lib.nju.edu.cn/resource/resource_shiyong.php" target="_blank">试用电子资源</a>|
          <a href="http://bras.nju.edu.cn/" target="_blank">bras自助查询</a>|
          <a href="http://bbs.nju.edu.cn/bbsfind" target="_blank">百合文章查询</a>
            </td></tr>
	 <tr><td class=style24 bgColor=#f5f8fe height=22 style="text-align:left" id="link">                             
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          (常用搜索)&nbsp; <img src="images/arrow.gif">&nbsp;  
          <a href="http://www.google.cn" target="_blank">Google</a>|
          <a href="http://www.baidu.com" target="_blank">百度搜索</a>|
          <a href="http://www.sowang.com/sousuoyinqing.htm" target="_blank">强力搜索</a>|
          <a href="http://www.yahoo.cn/" target="_blank">雅虎搜索</a>|
          <a href="http://www.soso.com/" target="_blank">腾讯搜搜</a>|
          <a href="http://www.sogou.com/" target="_blank">搜狗搜索</a> 
          <a href="http://www.btchina.net/" target="_blank">BT@China</a>|
          <a href="http://bt.fkee.net/" target="_blank">飞客BT</a>  |
          <a href="http://www.gougou.com" target="_blank">狗狗搜索</a>|
          <a href="http://www.verycd.com" target="_blank">VeryCD</a>            
          <a href="http://www.sowang.com/" target="_blank"><img src="images/more.gif" border=0></a>  
          </td></tr>
     <tr><td class=style24 height=22  style="text-align:left" id="link">                             
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          (日常查询)&nbsp; <img src="images/arrow.gif">&nbsp; 
          <a href="http://www.hao123.com/haoserver/kuaidi.htm" target="_blank">快递查询</a>|
          <a href="http://flights.ctrip.com/Domestic/SearchFlights.aspx" target="_blank">航班查询</a>|
          <a href=" http://www.jlweather.com/nanjing_map.asp#" target="_blank">金陵气象</a>|
          <a href="http://www.jslw.gov.cn/homepage/index.jsp" target="_blank">江苏交通</a>|                
             <a href="http://www.ip138.com/" target="_blank">ip138</a>|
             <a href="http://www.hao123.com/haoserver/wn.htm" target="_blank">万年历</a>|
             <a href="http://dict.cn" target="_blank">Dict.cn</a>|
          <a href="http://detail.zol.com.cn/" target="_blank">ZOL产品报价</a>|
          <a href="http://www.pconline.com.cn/market/" target="_blank">太平洋电脑</a>|
          <a href="http://quote.eastmoney.com/quote1_1.html" target="_blank">东方财富</a>
          <a href="http://www.hao123.com/haoserver/index.htm" target="_blank"><img src="images/more.gif" border=0></a>
             </td></tr>
     <tr><td class=style24 bgColor=#f5f8fe height=22  style="text-align:left" id="link">           
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          (影视相关)&nbsp; <img src="images/arrow.gif">&nbsp; 
          <a href="http://nic.nju.edu.cn/index.php?option=com_wrapper&Itemid=27" target="_blank">南大网络电视</a>|   
          <a href="http://www.youku.com/" target="_blank">优酷</a>|
          <a href="http://www.tudou.com/" target="_blank">土豆</a>|
          <a href="http://video.sina.com.cn/" target="_blank">新浪视频</a>|
          <a href="http://kankan.xunlei.com/" target="_blank">迅雷看看</a>|                 
          <a href="http://www.imdb.cn" target="_blank">IMDB中文</a>|
          <a href="http://www.mtime.com/" target="_blank">时光网</a>|             
          <a href="http://www.shooter.cn/" target="_blank">射手网</a>|
          <a href="http://www.xjkyc.com/" target="_blank">新街口影城</a>|
          <a href="http://www.wandafilm.com/" target="_blank">万达影城</a>|
          <a href="http://www.njsycinema.com/" target="_blank">南京上影</a>
             </td></tr>
    <tr><td class=style24 height=22  style="text-align:left" id="link">            
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          (校内导航)&nbsp; <img src="images/arrow.gif">&nbsp; 
          <a href="http://go.nju.edu.cn" target="_blank">GO.nju</a>
          <a href="http://www.nju.edu.cn" target="_blank">南大首页</a>|
          <a href="http://lib.nju.edu.cn" target="_blank">南大图书馆</a>|
          <a href="http://bbs.nju.edu.cn" target="_blank">南大小百合</a>|
          <a href="http://cs.nju.edu.cn" target="_blank">计算机系</a>|
          <a href="http://jw.nju.edu.cn" target="_blank">教务处</a>| 
          <a href="http://grawww.nju.edu.cn/" target="_blank">研究生院</a>|  
          <a href="http://grawww.nju.edu.cn/mis/ygraphy/index.asp" target="_blank">研究生管理</a>         
             </td></tr> 
    <tr><td height=10> </td></tr>  
    <tr><td class=style21 bgColor=#f5f8fe height=20  style="text-align:left">
         提醒：1. 对于目前没有的资源，您可以提交请求，当索引更新的时候发现资源会邮件通知阁下。<a href="resourceRequest.jsp">资源请求提交页面</a>
      </td></tr>
    <tr><td class=style24 height=20  style="text-align:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      2. 当你发现某些站点AskRosa没有收录的时候，你可以通过登记站点来将其添加到AskRosa。<a href="ftpSiteReg.jsp">登记站点页面</a></td>
    </tr>
    <tr><td class=style24 bgColor=#f5f8fe height=20  style="text-align:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      3. 如有问题或意见可以给AskRosa<a href="articleNew.jsp">留言</a>或Email <a href="mailto:askrosateam@gmail.com">Askrosa Team</a>
      </td></tr>
    <tr><td height=10> </td></tr>  
    <tr><td>
		<table bgcolor=#dfdfef cellpadding="0" cellspacing="1" width="100%" >
		     <tr bgColor=#f5f8fe >
		       <td height=20 style="text-align:center" width=60><a href="resourceRequestList.do?page=1">资源请求</a></td>		       
		       <td height=20 style="text-align:center"><bean:message key="ftpsearch.jsp.resourcerequest.resourcename" />(TOP5)</td>
		       <td height=20 style="text-align:center" width=100><bean:message key="ftpsearch.jsp.resourcerequest.nickname" /></td>
		       <td height=20 style="text-align:center" width=80><bean:message key="ftpsearch.jsp.resourcerequest.time" /></td>
		    </tr>
		    <logic:iterate id="ResourceRequest" name="listing"  indexId="index">
		    <tr height=20 bgcolor=#<%=(index.intValue()%2==0)? "FFFFFF" : "F5F8FE" %>>
		        <td ><%=index.intValue()+1%></td>		        
		        <td style="text-align:left">&nbsp;<bean:write name="ResourceRequest" property="resourcename" /></td>
		        <td><bean:write name="ResourceRequest" property="nickname" /></td>
		        <td>us<bean:write name="ResourceRequest" property="time" /></td>
		    </tr>
		    </logic:iterate>
		</table>
		<table bgcolor=#dfdfef cellpadding="0" cellspacing="1" width="100%" >
	       <tr height=20 bgColor=#f5f8fe ><td> 
	         如果您拥有此资源可以上传到任意Rosa可以检索到的FTP服务器，也可以上传到<a target=_blank href="ftp://up:up@ftp.askrosa.cn" onClick="javascript:copyToClipboard('ftp://up:up@ftp.askrosa.cn');return false;">Rosa交换空间</a>
	       </td></tr>
		</table>
   </td></tr>          
</table>	  
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>
</body>
</html>

