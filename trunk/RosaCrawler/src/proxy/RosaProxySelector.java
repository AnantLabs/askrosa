package proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import resource.CrawlerSetting;

public class RosaProxySelector extends ProxySelector
{
    // Keep a reference on the previous default
    ProxySelector defsel = null;

    /*
     * Inner class representing a Proxy and a few extra data
     */
    class InnerProxy
    {
	Proxy proxy;

	SocketAddress addr;

	// How many times did we fail to reach this proxy?
	int failedCount = 0;

	InnerProxy(InetSocketAddress a, Proxy.Type type)
	{
	    addr = a;
	    proxy = new Proxy(type, a);
	}

	SocketAddress address()
	{
	    return addr;
	}

	Proxy toProxy()
	{
	    return proxy;
	}

	int failed()
	{
	    return ++failedCount;
	}
    }

    /*
     * A list of proxies, indexed by their address.
     */
    private HashMap<SocketAddress, InnerProxy> socksProxies = new HashMap<SocketAddress, InnerProxy>();
    private List<String> socksProxyHosts;
    
    public RosaProxySelector(ProxySelector def, List<String> socks)
    {
	defsel = def;
	this.socksProxyHosts = socks;
	InnerProxy socksProxy = new InnerProxy(new InetSocketAddress(
		CrawlerSetting.getProperty("proxy.host"), CrawlerSetting.getInt("proxy.port")),Proxy.Type.SOCKS);
	socksProxies.put(socksProxy.address(), socksProxy);
    }

    /*
     * This is the method that the handlers will call. Returns a List of proxy.
     */
    public java.util.List<Proxy> select(URI uri)
    {
	// Let's stick to the specs.
	if (uri == null)
	{
	    throw new IllegalArgumentException("URI can't be null.");
	}

	/*
	 * If it's a http (or https) URL, then we use our own list.
	 */
	String host = uri.getHost();
	List<Proxy> l = null;
	if (socksProxyHosts.contains(host))
	{
	    l = new ArrayList<Proxy>();
	    for (InnerProxy p : socksProxies.values())
	    {
		l.add(p.toProxy());
	    }
	}

	/*
	 * Not HTTP or HTTPS (could be SOCKS or FTP) defer to the default
	 * selector.
	 */
	else if (defsel != null)
	{
	    l = defsel.select(uri);
	}
	else
	{
	    l = new ArrayList<Proxy>();
	    l.add(Proxy.NO_PROXY);
	}
	return l;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe)
    {
	if(defsel!=null)
	    defsel.connectFailed(uri, sa, ioe);
    }
}
