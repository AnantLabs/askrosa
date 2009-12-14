package resource;

import java.io.File;
import java.net.Authenticator;
import java.net.ProxySelector;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import log.CrawlerLogger;

import database.Criteria;
import database.FtpSiteInfo;
import database.FtpSiteInfoPeer;
import database.Scroller;

import proxy.RosaAuthenticator;
import proxy.RosaProxySelector;
/**
 * 从配置文件读取系统配置信息
 * @author elegate
 *
 */
public class CrawlerSetting
{
    public static final ResourceBundle SETTINGS;
    public static final SimpleDateFormat FTP_FILE_INFO_DATE_FORMAT ;
    
    static
    {
	SETTINGS = ResourceBundle.getBundle("resource.ProjectSetting");
	FTP_FILE_INFO_DATE_FORMAT =  new SimpleDateFormat(CrawlerSetting
		    .getProperty("ftp.ftpfileinfo.dateformat"));
    }

    public static String getBasePath()
    {
	String catalinaHome = System.getProperty("catalina.home");
	String basePath = null;
	if (catalinaHome != null)
	    basePath = catalinaHome + File.separator + "webapps/"
		    + getProperty("name") + "/";
	else
	    basePath = new File("").getAbsolutePath()+File.separator;
	return basePath;
    }
    
    public static void setProxy()
    {
	if (CrawlerSetting.getBoolean("proxy.useproxy"))
	{
	    ArrayList<String> socks = new ArrayList<String>();
	    socks.add("smtp.gmail.com");
	    
	    Criteria c = new Criteria();
	    c.add(FtpSiteInfo.LOCATION, FtpSiteInfo.CAMPUS_SITE, Criteria.NOT_EQUAL);
	    Scroller<FtpSiteInfo> scr;
	    try
	    {
		scr = FtpSiteInfoPeer.doSelect(c);
		while(scr.hasNext())
		    {
			FtpSiteInfo ftpSite = scr.next();
			socks.add(ftpSite.getServer());
		    }
	    }
	    catch (Exception e)
	    {
		CrawlerLogger.logger.error("Failed to set proxy", e);
	    }
	    
	    Authenticator au = new RosaAuthenticator();
	    Authenticator.setDefault(au);
	    ProxySelector.setDefault(null);
	    RosaProxySelector ps = new RosaProxySelector(ProxySelector
		    .getDefault(), socks);
	    ProxySelector.setDefault(ps);
	}
    }
    
    public static void unsetProxy()
    {
	ProxySelector.setDefault(null);
    }

    public static String getProperty(String key)
    {
	return SETTINGS.getString(key).trim();
    }
    
    public static boolean getBoolean(String key)
    {
	return Boolean.valueOf(getProperty(key));
    }
    
    public static int getInt(String key)
    {
	return Integer.parseInt(getProperty(key));
    }

    public static double getDouble(String key)
    {
	return Double.parseDouble(getProperty(key));
    }

    public static void main(String[] args)
    {
	System.out.println(CrawlerSetting
		.getBoolean("proxy.useproxy"));
    }
}
