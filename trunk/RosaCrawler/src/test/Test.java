package test;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class Test
{
    public static void main(String[] args)
    {
	    FTPClient ftp = new FTPClient();
	    ftp.setControlEncoding("GBK");
	    try
	    {
		ftp.connect("media.nju.edu.cn");
		ftp.login("anonymous", "anonymous");
		ftp.enterLocalPassiveMode();
		FTPFile[] files = ftp.listFiles("/LilyStudio");
		for(FTPFile file:files)
		    System.out.println(file);
		ftp.disconnect();
	    }
	    catch (IllegalStateException e)
	    {
		e.printStackTrace();
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
	    }
    }

}
