package test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import searcher.FtpSearch;

public class Test
{
    public static void main(String[] args)
    {
	 try
	{
	   Map<String,Integer> map= FtpSearch.suggestSimilar("会");
	   for(Map.Entry<String, Integer> entry:map.entrySet())
	   {
	       System.out.println(entry.getKey()+"("+entry.getValue()+")");
	   }
	}
	catch (MalformedURLException e)
	{
	    e.printStackTrace();
	}
	catch (RemoteException e)
	{
	    e.printStackTrace();
	}
	catch (NotBoundException e)
	{
	    e.printStackTrace();
	}
    }

}
