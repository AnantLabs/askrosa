package cn.askrosa.listener;

import database.Criteria;
import database.HistoryUsers;
import database.HistoryUsersPeer;
import database.Scroller;

public class OnlineCounter
{
    private static long online = 1;

    public static void setOnline(long l)
    {
	online = l;
    }

    public static long getOnline()
    {
	return online;
    }

    public static void raise()
    {
	Criteria c = new Criteria();
	c.add(HistoryUsers.ID, "1");
	Scroller<HistoryUsers> scr;
	try
	{
	    scr = HistoryUsersPeer.doSelect(c);
	    if (scr.hasNext())
	    {
		HistoryUsers h = (HistoryUsers) scr.next();
		h.setCount(h.getCount() + 1);
		h.save();
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	online++;
    }

    public static void reduce()
    {
	if (online > 0)
	    online--;
    }
}