package proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import resource.CrawlerSetting;

public class RosaAuthenticator extends Authenticator
{
    public RosaAuthenticator()
    {
    }
    
    protected PasswordAuthentication getPasswordAuthentication()
    {
	String host = this.getRequestingHost();
	if(host.equals(CrawlerSetting.getProperty("proxy.host")))
	return new PasswordAuthentication( CrawlerSetting.getProperty("proxy.username"), new String(
		CrawlerSetting.getProperty("proxy.password")).toCharArray());
	else if(host.equals(CrawlerSetting.getProperty("proxy.education.host")))
		return new PasswordAuthentication( CrawlerSetting.getProperty("proxy.education.username"), new String(
			CrawlerSetting.getProperty("proxy.education.password")).toCharArray());
	return null;
    }
}
