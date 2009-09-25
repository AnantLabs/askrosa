package cn.askrosa.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import database.Criteria;
import database.Scroller;
import database.Sessions;
import database.SessionsPeer;

public class OnlineUserCounterListener implements HttpSessionListener
{
    public void sessionCreated(HttpSessionEvent arg0)
    {
	OnlineCounter.raise();

	Sessions s = new Sessions();
	s.setCreateTime(arg0.getSession().getCreationTime());
	try
	{
	    s.save();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void sessionDestroyed(HttpSessionEvent arg0)
    {
	// 保存session
	Criteria c = new Criteria();
	c.add(Sessions.CREATETIME, arg0.getSession().getCreationTime());
	Scroller<Sessions> scr;
	try
	{
	    scr = SessionsPeer.doSelect(c);
	    if (scr.hasNext())
	    {
		Sessions s = (Sessions) scr.next();
		s.setDestroyTime(System.currentTimeMillis());
		s.save();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	OnlineCounter.reduce();
    }
}
