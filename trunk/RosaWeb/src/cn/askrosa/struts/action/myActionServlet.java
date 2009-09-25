package cn.askrosa.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionServlet;

public class myActionServlet extends ActionServlet
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 8634027611296101016L;

    protected void process(HttpServletRequest request, HttpServletResponse response)
	    throws java.io.IOException, javax.servlet.ServletException
    {
	request.setCharacterEncoding("UTF-8");// 解决编码问题，这里要设置成UTF-8编码
	response.setContentType("text/html;charset=UTF-8");
	super.process(request, response);
    }
}
