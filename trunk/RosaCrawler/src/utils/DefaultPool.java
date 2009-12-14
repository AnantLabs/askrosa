package utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class DefaultPool implements Poolable<String>
{
    protected PrintStream print;

    /**
     * constructor,print the information to standard output
     */
    public DefaultPool()
    {
	print = System.out;
    }

    /**
     * constructor,store the information to file
     * 
     * @param file
     *                the file path
     */
    public DefaultPool(String file)
    {
	try
	{
	    print = new PrintStream(new FileOutputStream(file));
	}
	catch (FileNotFoundException e)
	{
	    e.printStackTrace();
	    print = System.out;
	}
    }

    /**
     * constructor,print the information to PrintStream
     * 
     * @param out
     */
    public DefaultPool(PrintStream out)
    {
	print = out;
    }

    /**
     * 
     */
    public boolean pool(String obj)
    {
	print.println(obj);
	return true;
    }

    @Override
    public void close()
    {
	// TODO Auto-generated method stub
	
    }
}
