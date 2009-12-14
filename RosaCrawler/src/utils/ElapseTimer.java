package utils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 * This class will help you to print the time used for certain operation in you
 * program
 * 
 * <pre>
 * Usage:
 * ElapseTimer t = new ElapseTimer();
 * t.begin();
 * ...Your Operation...
 * t.finish();
 * </pre>
 * 
 * @author elegate
 */
public class ElapseTimer
{
    /**
     * How many seconds in a minute
     */
    public static final int SECONDS_IN_MINUTE = 60;

    /**
     * How many seconds in a hour
     */
    public static final int SECONDS_IN_HOUR = 60 * 60;

    /**
     * How many seconds in one day
     */
    public static final int SECONDS_IN_DAY = 60 * 60 * 24;

    /**
     * Number Formatter, used to format the elapsed time
     */
    public static final NumberFormat NF = NumberFormat.getNumberInstance();

    public static final DateFormat DF = DateFormat.getDateTimeInstance();
    /**
     * static constructor
     */
    static
    {
	NF.setGroupingUsed(false);
	NF.setMaximumFractionDigits(2);
    }

    /**
     * remember the begin time
     */
    private long begin;

    /**
     * 
     */
    public ElapseTimer()
    {
	begin = -1;
    }

    /**
     * begin your timer
     */
    public void begin()
    {
	begin = System.currentTimeMillis();
    }

    /**
     * get current time
     * 
     * @return current time in millisecond
     */
    public long time()
    {
	return System.currentTimeMillis();
    }

    /**
     * finish your operation and print the time used
     * 
     * @throws Exception
     *                 if you havn't called begin(),you will get this exception
     */
    public void finish() throws Exception
    {
	printElapsedTime();
	begin = -1;
    }

    public void printElapsedTime(String msg) throws Exception
    {
	if (begin == -1)
	{
	    throw new Exception("You must call begin() first!!!");
	}
	Calendar c = Calendar.getInstance();
	System.out.print(DF.format(c.getTime()));
	if (msg != null)
	    System.out.println(" , time Used: "
		    + elapsedTimeToString((System.currentTimeMillis() - begin))
		    + " " + msg);
	else
	    System.out
		    .println(" , time Used: "
			    + elapsedTimeToString((System.currentTimeMillis() - begin)));
    }

    /**
     * print the elapsed time since your last call of begin()
     * 
     * @throws Exception
     *                 if you havn't called begin,you will get this exception
     */
    public void printElapsedTime() throws Exception
    {
	printElapsedTime(null);
    }

    /**
     * print the elapsed time in friendly format
     * 
     * @param elapsedMillis
     *                the elapsed time in millisecond
     */
    public static void printElapsedTime(double elapsedMillis)
    {
	System.out.println(elapsedTimeToString(elapsedMillis));
    }

    /**
     * convert the elapsed time to user friendly string
     * 
     * @param elapsedMillis
     *                the elapsed time in millisecond
     * @return the converted string of elapsed time
     */
    public static String elapsedTimeToString(double elapsedMillis)
    {
	double seconds = elapsedMillis / 1000;
	if (seconds < SECONDS_IN_MINUTE)
	    return NF.format(seconds) + " s";
	else if (seconds < SECONDS_IN_HOUR)
	{
	    return NF.format(seconds / SECONDS_IN_MINUTE) + " m";
	}
	else if (seconds < SECONDS_IN_DAY)
	{
	    return NF.format(seconds / SECONDS_IN_HOUR) + " h";
	}
	else
	{
	    return NF.format(seconds / SECONDS_IN_DAY) + " d";
	}
    }
}
