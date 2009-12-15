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
          textbox: document.getElementById("keywordInclude"),
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