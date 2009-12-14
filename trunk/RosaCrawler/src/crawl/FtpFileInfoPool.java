package crawl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import analysis.RosaAnalyzer;
import analysis.RosaAnalyzerUtils;

import ftp.FTPFileInfo;

import utils.Poolable;

/**
 * A class used for test,which implemented Poolable interface
 * 
 * @author elegate
 */
public class FtpFileInfoPool implements Poolable<FTPFileInfo>
{

    protected PrintStream print;

    /**
     * constructor,print the information to standard output
     */
    public FtpFileInfoPool()
    {
	print = System.out;
    }

    /**
     * constructor,store the information to file
     * 
     * @param file
     *                the file path
     */
    public FtpFileInfoPool(String file)
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
    public FtpFileInfoPool(PrintStream out)
    {
	print = out;
    }
    
    public void close()
    {
	print.close();
    }

    /**
     * 
     */
    public boolean pool(FTPFileInfo obj)
    {
	try
	{
	    print.println(obj+" "+RosaAnalyzerUtils.getTokens(new RosaAnalyzer(), obj.getName()));
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	return true;
    }

}
