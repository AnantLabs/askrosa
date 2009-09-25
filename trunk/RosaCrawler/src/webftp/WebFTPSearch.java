package webftp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface WebFTPSearch extends Remote
{
    public Set<String> query(String keyword, int limit) throws MalformedURLException, RemoteException, IOException, NotBoundException;
}
