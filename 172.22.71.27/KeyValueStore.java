import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * An interface to the methods a remote Key Value Store needs to implement
 * @author David Kaplan
 */
public interface KeyValueStore extends Remote {
	/**
	 * Put a key in the store, replacing any previous value, if there was a
	 * value mapped to that key, or null if no value is mapped to the key.
	 * @param key The key to put a value for.
	 * @param value The value to associate with that key in the store.
	 * @return A wrapper containing the key, the value associate with that key in the map,
	 *  or null if the key is not found, and a Server code
     * representing the transaction status.
	 * @throws RemoteException if anything goes wrong.
	 */
    KeyValueReturnWrapper put(String key, String value) throws RemoteException;
    /**
     * Gets a value associated with a key in the store.
     * @param key The key to retrieve the value of.
     * @return A wrapper containing the key, the value put with that key, and a Server code
     * representing the transaction status.
     * @throws RemoteException
     */
    KeyValueReturnWrapper get(String key) throws RemoteException;
    /**
     * Attempts to delete a key from the store.
     * @param key The key to delete.
     * @return  wrapper containing the key deleted (if present),
     *  the value put with that key (if any), and a Server code
     * representing the transaction status.
     * @throws RemoteException
     */
    KeyValueReturnWrapper delete(String key)throws RemoteException;
    
    int ackPlease() throws RemoteException;
}
