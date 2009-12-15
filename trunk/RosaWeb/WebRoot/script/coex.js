function coexText(id1,id2)
    {
      var s1 = document.getElementById(id1);
      var s2 = document.getElementById(id2);
      if(s1.style.display=='none')
        {
           if(document.all)
              {
              s1.style.display='inline';
              }
           else
             {
              s1.style.display='table-row';
             }
           s2.innerText="隐藏高级搜索";
        }
      else
        { 
           s1.style.display='none';
           s2.innerText="显示高级搜索";
        }
    }
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

function coexall(id,imgall,textall)
    {
      var coex=document.getElementById(id);
      var img=document.getElementById(imgall);
      var text=document.getElementById(textall);
      if(coex.value=='co')
       {
         ex_all();
         coex.value='ex';
         img.src="images/ex-all.png";
         text.innerText='合并';
       }      
      else
      {
         co_all();
         coex.value='co';
         img.src="images/co-all.png";
         text.innerText='展开';
      }
    }

function ex_all()
    {
      for(var i=0;i<30;i++)
      {
        var tr="tr"+i; 
        var img="img"+i; 
        var s1 = document.getElementById(tr);
        var s2 = document.getElementById(img);      
        if(s1!=null)
        {
	        if(document.all)
	            s1.style.display='inline';
	        else
	            s1.style.display='table-row';
	        s2.src="images/ex.png";
        }
      }      
    }
function co_all()
    {
      for(var i=0;i<30;i++)
      {
        var tr="tr"+i; 
        var img="img"+i; 
        var s1 = document.getElementById(tr);
        var s2 = document.getElementById(img);
        if(s1!=null)
        {
          s1.style.display='none';
          s2.src="images/co.png";
        }        
      }      
    }