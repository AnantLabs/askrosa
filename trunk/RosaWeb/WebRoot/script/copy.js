function getInnerText(obj)
{ 
  return document.all?obj.innerText:obj.textContent; 
}
function copyToClipboardObj(obj) //拷贝到剪切板函数
{    
     txt=getInnerText(obj);
     if(window.clipboardData) 
	 {    
             window.clipboardData.clearData();    
             window.clipboardData.setData("Text", txt); 
			 alert("FTP服务器地址:"+"“"+txt+"”"+" 已经复制到了剪贴板。按Ctrl+V可复制到FTP客户端软件的地址栏中。");   
     } 
	 else if(navigator.userAgent.indexOf("Opera") != -1) 
	 {    
          window.location = txt;    
     } 
	 else if (window.netscape) 
	 {    
          try 
		  {    
               netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");    
          } 
		  catch (e) 
		  {    
           alert("被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将'signed.applets.codebase_principal_support'设置为'true'");
          }    
          var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);    
          if (!clip)  return;    
          var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);    
          if (!trans) return;    
          trans.addDataFlavor('text/unicode');    
          var str = new Object();    
          var len = new Object();    
          var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);    
          var copytext = txt;    
          str.data = copytext;    
          trans.setTransferData("text/unicode",str,copytext.length*2);    
          var clipid = Components.interfaces.nsIClipboard;    
          if (!clip)return false;    
          clip.setData(trans,null,clipid.kGlobalClipboard);    
          alert("FTP服务器地址:"+"“"+txt+"”"+" 已经复制到了剪贴板。按Ctrl+V可复制到FTP客户端软件的地址栏中。");   
     }    
} 
function RosaFTPDownload(obj) //用RosaFTP下载，Rosa://ftp://到剪切板
{    
     txt='rosa://'+getInnerText(obj);
     if(window.clipboardData) 
	 {    
             window.clipboardData.clearData();    
             window.clipboardData.setData("Text", txt); 
     } 
	 else if(navigator.userAgent.indexOf("Opera") != -1) 
	 {    
          window.location = txt;    
     } 
	 else if (window.netscape) 
	 {    
          try 
		  {    
               netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");    
          } 
		  catch (e) 
		  {    
           alert("被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将'signed.applets.codebase_principal_support'设置为'true'");
          }    
          var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);    
          if (!clip)  return;    
          var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);    
          if (!trans) return;    
          trans.addDataFlavor('text/unicode');    
          var str = new Object();    
          var len = new Object();    
          var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);    
          var copytext = txt;    
          str.data = copytext;    
          trans.setTransferData("text/unicode",str,copytext.length*2);    
          var clipid = Components.interfaces.nsIClipboard;    
          if (!clip)return false;    
          clip.setData(trans,null,clipid.kGlobalClipboard);      
     }    
}

function copyToClipboard(txt) //拷贝到剪切板函数
{    
     if(window.clipboardData) 
	 {    
             window.clipboardData.clearData();    
             window.clipboardData.setData("Text", txt); 
			 alert("FTP服务器地址:"+"“"+txt+"”"+" 已经复制到了剪贴板。按Ctrl+V可复制到FTP客户端软件的地址栏中。");   
     } 
	 else if(navigator.userAgent.indexOf("Opera") != -1) 
	 {    
          window.location = txt;    
     } 
	 else if (window.netscape) 
	 {    
          try 
		  {    
               netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");    
          } 
		  catch (e) 
		  {    
           alert("被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将'signed.applets.codebase_principal_support'设置为'true'");
          }    
          var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);    
          if (!clip)  return;    
          var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);    
          if (!trans) return;    
          trans.addDataFlavor('text/unicode');    
          var str = new Object();    
          var len = new Object();    
          var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);    
          var copytext = txt;    
          str.data = copytext;    
          trans.setTransferData("text/unicode",str,copytext.length*2);    
          var clipid = Components.interfaces.nsIClipboard;    
          if (!clip)return false;    
          clip.setData(trans,null,clipid.kGlobalClipboard);    
          alert("FTP服务器地址:"+"“"+txt+"”"+" 已经复制到了剪贴板。按Ctrl+V可复制到FTP客户端软件的地址栏中。");   
     }    
} 