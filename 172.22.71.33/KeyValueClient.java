import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * A client for a remote Key Value Server
 * @author Dave K.
 * @version 2015
 */
public class KeyValueClient {
	
	private static FileWriter fw = null;
	
	/**
	 * A method to get the current time to stamp our log
	 * Given time in ms since January 1, 1970 UDT
	 * @return String representation of current time.
	 */
	private static String getCurrentTimeStamp() {
        /* Get the current system time stamp */
        return "[" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Date()) + "] ";
    }
	/**
	 * Launch an instance of the KeyValueClient
	 * @param args args[0] is host name or null for localhost,
	 * args[1] is a client number, integer >=0
	 */
	public static void main(String[] args){

		//http://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html
		//This example used in general for our client-server outline

//		String host = null;
//		int client_number = -1;
//		if (args.length != 2 && Integer.parseInt(args[0]) < 0) {
//			System.out.println("Usage: KeyValueClient HostIPAddr ClientNum(>=0)");
//			return;
//		} else {
//			host = args[0];
//			client_number = Integer.parseInt(args[1]);			
//		}
//        

		String host = "172.22.71.32";
		int client_number = 1;
        try {
        	//if host = null means "localhost"
			Registry registry = LocateRegistry.getRegistry(host);
	        KeyValueStore stub = (KeyValueStore) registry.lookup("coordinator");

	        fw = new FileWriter("ClientLog_#" + client_number + ".txt", true);
	        
	        // 5 puts
	        fw.write(put(stub, "Alpha", "password"));
			fw.flush();
	        fw.write(put(stub, "Alpha", "password"));
			fw.flush();
	        fw.write(put(stub, "Beta", "wdfdfdfd"));
			fw.flush();
	        fw.write(put(stub, "Gamma", "1n2b5h"));
			fw.flush();
	        fw.write(put(stub, "Omega", "1232322"));
			fw.flush();
	        fw.write(put(stub, "Trianga", "1ooooooo2"));
			fw.flush();

	       
	        // 5 gets
	        fw.write(get(stub, "Alpha"));
			fw.flush();
	        fw.write(get(stub, "Beta"));
			fw.flush();
	        fw.write(get(stub, "Gamma"));
			fw.flush();
	        fw.write(get(stub, "Omega"));
			fw.flush();
	        fw.write(get(stub, "Holymonga"));

	       
	        // 5 deletes
	        fw.write(delete(stub, "Alpha"));
			fw.flush();
	        fw.write(delete(stub, "Beta"));
			fw.flush();
	        fw.write(delete(stub, "Gamma"));
			fw.flush();
	        fw.write(delete(stub, "Omega"));
			fw.flush();
	        fw.write(delete(stub, "Holymonga"));
			fw.flush();

	        fw.close();

		} catch (RemoteException | NotBoundException e) {
			try {
				fw.write(getCurrentTimeStamp() + "Error sending data:" + e.getMessage());
			} catch (IOException e1) {
				System.out.println("Error connecting to server and logging");
				System.exit(client_number);
			}
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error writing log file, exiting");
			e.printStackTrace();
			System.exit(client_number);
		}

	}

	/**
	 * Wrapper for put so you put to a stub, and also returns a log string.
	 * @param stub
	 * @param key
	 * @param value
	 * @return String of the log that it wants to write.
	 * @throws RemoteException
	 */
	public static String put(KeyValueStore stub, String key, String value) throws RemoteException {
		KeyValueReturnWrapper w  = stub.put(key, value);
		String returner = getCurrentTimeStamp() + "Sending data: put, key = " 
					+ key + ", value = " + value +  ", Server Message = " +  w + "\n";
		return returner;
	}
	
	/**
	 * Wrapper for get method so gets from a stub, and also returns a log string.
	 * @param stub
	 * @param key
	 * @return String of the log that it wants to write.
	 * @throws RemoteException
	 */
	public static String get(KeyValueStore stub, String key) throws RemoteException {
		KeyValueReturnWrapper w = stub.get(key);
		String returner = getCurrentTimeStamp() + "Sending data: get, key = " 
					+ key + ", Server Message = " +  w + "\n";
		return returner;
	}
	
	
	/**
	 * Wrapper for delete method so delete from a stub, and also returns a log string.
	 * @param stub
	 * @param key
	 * @return String of the log that it wants to write.
	 * @throws RemoteException
	 */
	public static String delete(KeyValueStore stub, String key) throws RemoteException {
		KeyValueReturnWrapper w  = stub.delete(key);
		String returner = getCurrentTimeStamp() + "Sending data: delete, key = " 
					+ key +  ", Server Message = " +  w + "\n";
		return returner;
	}
}