<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	   <title><bean:message key="ftpsearch.jsp.navigator.aboutus" /></title>
       <meta http-equiv=Content-Type content="text/html;charset=utf-8">
       <style type="text/css" media="screen">@import "./styles/styles.css";</style>
       <style type="text/css">
	   <!--
		    td{
		    white-space: normal;
		    word-break: break-all;
		    }
	   -->
       </style>
	</head>	
<BODY>
 <tiles:insert page="top.jsp" flush="true" /> 
 <center>
<TABLE  width="80%" cellSpacing=0 cellPadding=0  border=0>
	  <tr>
	   <td style="text-align:left">
	     <A href="/"><IMG alt="搜索引擎首页" src="images/rosa/rosa.jpg" width=200 vspace=5 border=0> </A>
	   </td>
	  </tr>
</TABLE>
<TABLE  width="80%" cellSpacing=0 cellPadding=0  border=0>
      <TR><TD style="text-align:left" width="100%">
				<HR noShade SIZE=1>
				<B>Rosa</B><br>
				Rosa是一个通用意义上的开源FTP搜索引擎，由几个穷极无聊而又想寻求自我价值的研究生开发。
				<HR noShade SIZE=1>
				<B>开发成员</B>
				  <table width="100%" cellSpacing=0 cellPadding=0  border=0 >
				  <tr>
				  <td style="text-align:left"><a href="mailto:tianbaom@gmail.com">5yanzhao</a>&nbsp; 兴趣：足球，聊天</td>
				  <td style="text-align:left"><a href="mailto:zhonghua0747@gmail.com">DH</a>&nbsp; 兴趣：乒乓，游泳</td>
				  <td style="text-align:left"><a href="mailto:andynjux@gmail.com">AndyNJUX</a>&nbsp; 兴趣：看电影，篮球</td>
				  </tr>
				  <tr>
				  <td style="text-align:left"><a href="mailto:liang.wang.84@gmail.com">wangdogman</a>&nbsp; 兴趣：美工，编程 </td>
				  <td style="text-align:left"><a href="mailto:iseeicy@126.com">icy001</a>&nbsp; 兴趣：Painting</td>
				  <td style="text-align:left"><a href="http://hi.baidu.com/lolorosa">elegate</a>&nbsp; 兴趣：羽毛球，摄影</td>
				  </tr>
				  </table> 					
				<HR noShade SIZE=1>
				<B>技术相关</B><br>
				&nbsp;&nbsp; 在开发中，我们借鉴和使用了大量开源软件，包括 Luence ，Struts等等。Rosa也是开放源码的，如果你需要源码的话，请使用下面提供的链接下载。
				<HR noShade SIZE=1>
				<B>ROSA的未来</B><br>
				&nbsp;&nbsp; Rosa现在的功能只是我们一次次热烈的讨论的一小部分，我们希望在将来Rosa不仅能对ftp资源进行搜索，还能对其他的的资源进行搜索， 进而成为一个校内资源共享的平台。 Dream Big, Work Hard！
				<HR noShade SIZE=1>
				<B>Rosa名字的由来</B><br>
				&nbsp;&nbsp;&nbsp; Rosa的这个名字的由来源于童年时代的回忆。小时侯在家，院中有株月季，长的很高，每到夏日，满树红花，和旁边的枣树相应成趣。阳光穿过花簇照到我瘦小的脸上，
				那情景至今都历历在目。记得学了自然课之后，早上我还常口含井水对着月季花喷水雾，这样可以看到瞬间的彩虹（月季花在院子西面，这样正好背对阳光），虽然只有那么一小段并且瞬间就消失，
				但我却常常乐此不彼。 <br>
				&nbsp;&nbsp;&nbsp; 现在那株月季花还在，只是在奶奶去世后便无人过问，现在个头长的太过高大（没人修剪），不好看了。并且现在大了，在家的时间有限，很少再那么傻傻的站在那儿做花痴状了。<br>
				&nbsp;&nbsp;&nbsp; 当FTP项目完成后，名字成了难题，那段时间常常工作过度，每晚都睡得很早，梦中老是回到过去，回想那个已经很遥远的童年往事。而这往事中自然常常幻现院中月季的影子，
				有时候醒来还要发一阵呆。后来决定就用月季花作为项目的名字，但rose这个名字普通且泛滥就使用了她的异体词Rosa代替了。
			    <HR noShade SIZE=1>
				 <B>联系 Rosa</B><br>
				<a href="mailto:askrosateam@gmail.com">AskRosa Team</a>
			</TD>
		</TR>
</TABLE>
<tiles:insert page="welcomemainBottom.jsp" flush="true" />
</center>	
</BODY>
</HTML>