package finder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import resource.CrawlerSetting;

import utils.DefaultPool;
import utils.Poolable;

import database.FtpSitesManager;
import database.FtpSiteInfo;
/**
 * 根据给定的网段搜索匿名FTP
 * @author elegate
 *
 */
public final class FtpSitesFinder
{
    public static final int REACHABLE_TIMEOUT = CrawlerSetting
	    .getInt("ftp.ftpcrawler.find.reachable.timeout");

    private FtpSitesFinder()
    {

    }

    public static int addressToInt(byte[] addr)
    {
	int address = addr[3] & 0xFF;
	address |= ((addr[2] << 8) & 0xFF00);
	address |= ((addr[1] << 16) & 0xFF0000);
	address |= ((addr[0] << 24) & 0xFF000000);
	return address;
    }

    public static byte[] intToAddress(int address)
    {
	byte[] addr = new byte[4];

	addr[0] = (byte) ((address >>> 24) & 0xFF);
	addr[1] = (byte) ((address >>> 16) & 0xFF);
	addr[2] = (byte) ((address >>> 8) & 0xFF);
	addr[3] = (byte) (address & 0xFF);
	return addr;
    }

    public static void searchFtpSitesBetween(String fromIP, String toIP,
	    Poolable<String> pool)
    {
	try
	{
	    int count = 0;
	    InetAddress from = InetAddress.getByName(fromIP);
	    InetAddress to = InetAddress.getByName(toIP);

	    byte[] fromAdd = from.getAddress();
	    byte[] toAdd = to.getAddress();

	    int fromAddress = addressToInt(fromAdd);
	    int toAddress = addressToInt(toAdd);

	    for (int i = fromAddress; i < toAddress; i++)
	    {
		byte[] add = intToAddress(i);
		InetAddress inetAdd = InetAddress.getByAddress(add);
		if (inetAdd.isReachable(REACHABLE_TIMEOUT))
		{
		    FtpSiteInfo ftpSite = new FtpSiteInfo();
		    ftpSite.setServer(inetAdd.getHostName());
		    ftpSite.setVerify(inetAdd.getHostName().replaceAll("\\.",
			    ""));
		    String ret = FtpSitesManager.addFtpSite(ftpSite);
		    if (ret == null)
		    {
			pool
				.pool("Connect " + ftpSite.getServer()
					+ " succeed");
			count++;
		    }
		    else
		    {
			pool
				.pool("Server " + ftpSite.getServer() + " : "
					+ ret);
		    }
		}
		else
		{
		    pool.pool("Address " + inetAdd + " is unreachable");
		}
	    }
	    pool.pool(count + " sites are added");
	}
	catch (UnknownHostException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}

    }

    public static void main(String args[])
    {
	searchFtpSitesBetween("172.16.65.104", "172.16.65.106",
		new DefaultPool());
    }
}
