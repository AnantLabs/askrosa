package autocomplete;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface AutoCompleteRemote extends Remote
{
    public Map<String, Integer> suggestSimilar(String word) throws RemoteException;
}
