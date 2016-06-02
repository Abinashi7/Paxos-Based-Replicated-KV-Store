import java.io.FileWriter;
import java.io.IOException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * SERVER 1
 * @version 2015
 */
public class KeyValueServer2 implements KeyValueStore {
	Random r;
	private int seqNum = 0;
	private static FileWriter fw = null;
	
	private Map<String, String> myKeyValueMap;
	
	/**
	 * Private constructor prevents instantiation
	 * @throws IOException 
	 */
	private KeyValueServer2() {
		r = new Random();
		myKeyValueMap = new HashMap<String, String>();
		
		try {
			fw = new FileWriter("ServerLog.txt", true);
			fw.write(getCurrentTimeStamp() + " Server starting.");
			fw.flush();
		} catch (IOException e) {
			System.out.println("Error opening log file server closing");
			e.printStackTrace();
			System.exit(1);
		}

        //Populate the KeyValueMap.
    	myKeyValueMap.put("testingId1", "password123");
    	myKeyValueMap.put("testingId2", "passw023");
    	myKeyValueMap.put("testingId3", "pasee123");
    	myKeyValueMap.put("testingId4", "passwoe3");
    	myKeyValueMap.put("testingId5", "password9595");
    	myKeyValueMap.put("testingId6", "password321");
    	myKeyValueMap.put("testingId7", "pswd123");
	}
	
	/**
	 * A method to get the current time to stamp our log
	 * Given time in ms since January 1, 1970 UDT
	 * @return the time of the event
	 */
	private synchronized String getCurrentTimeStamp() {
        /* Get the current system time stamp */
        return new SimpleDateFormat("[yyyy-MM-dd'T'HH:mm:ss.SSS] ").format(new Date());
    }
	
	@Override
	public synchronized KeyValueReturnWrapper put(String key, String value) {
		myKeyValueMap.put(key, value);
		try {
			fw.write(getCurrentTimeStamp() + "Receiving data: put, key = " + key + ", value = " + value + "\n");
			fw.flush();
		} catch (Exception e) {
			try {
				fw.write(getCurrentTimeStamp() + "Error receiving data: put, key = " + key + ", value = " + value + "\n");
				fw.flush();
			} catch (IOException e1) {
				System.out.println(getCurrentTimeStamp() + "Error writing to log file");
				e1.printStackTrace();
			}
		}

		return new KeyValueReturnWrapper(key, value, KeyValueCode.PUT);
	}

	@Override
	public int ackPlease() {
		try {
			fw.write(getCurrentTimeStamp() + "Ack asked\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}

	@Override
	public synchronized KeyValueReturnWrapper get(String key)  {
		KeyValueReturnWrapper kvr;
		if (myKeyValueMap.containsKey(key)) {
			kvr = new KeyValueReturnWrapper(key, myKeyValueMap.get(key),
					KeyValueCode.GET);
		} else {
			kvr = new KeyValueReturnWrapper(key, "",
					KeyValueCode.GET_KEY_MISSING);
		}
		try {
			fw.write(getCurrentTimeStamp() + "Receiving data: get, key = " + key 
					+ ", Returning: " +  kvr  + "\n");
			fw.flush();
		} catch (Exception e) {
			try {
				fw.write(getCurrentTimeStamp() + "Retrying writing log data: get, key = " + key + 
						", Returning: " +  kvr  + "\n");
				fw.flush();
			} catch (IOException e1) {
				System.out.println(getCurrentTimeStamp() + "Error writing to log file");
				e1.printStackTrace();
			}
		}
		return kvr;
	}

	@Override
	public synchronized KeyValueReturnWrapper delete(String key) {
		KeyValueReturnWrapper kvr;
		if (myKeyValueMap.containsKey(key)) {
			kvr = new KeyValueReturnWrapper(key, myKeyValueMap.remove(key),
					KeyValueCode.DELETE);
		} else {
			kvr = new KeyValueReturnWrapper(key, "",
					KeyValueCode.DELETE_KEY_MISSING);
		}
		try {
			fw.write(getCurrentTimeStamp() + "Deleting data: delete, key = " + key + ", Returning: " +  kvr + "\n");
			fw.flush();
		} catch (Exception e) {
			try {
				fw.write(getCurrentTimeStamp() + "Retrying writing log data: delete, key = " + key + ", Returning: " 
						+  kvr +  "\n");
				fw.flush();
			} catch (IOException e1) {
				System.out.println(getCurrentTimeStamp() + "Error writing to log file");
				e1.printStackTrace();
			}
		}

		return kvr;
	}
	/**
	 * No arguments used here
	 */
    public static void main(String args[]) {
        try {
        	String host = "172.22.71.32";
        	KeyValueServer2 kvserver = new KeyValueServer2();
            KeyValueStore stub = 
            		(KeyValueStore) UnicastRemoteObject.exportObject(kvserver, 0);
//            Registry registry = LocateRegistry.getRegistry(host);
            Registry registry = LocateRegistry.getRegistry();
            String name = "Server2";
            registry.bind(name, stub);

            System.out.println("KeyValueServer started, bound to ");
            System.out.println("registry at ip:" + host + "and name:" + name);
        } catch (Exception e) {
            System.out.println("Error starting server " + e.toString());
            e.printStackTrace();
        }
    }

	@Override
	public int proposerSendPrepare() {
		int temp = seqNum;
		seqNum++;
		return temp;
	}

	@Override
	public void proposerSendAccept() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int acceptorSendPromise(int proposeNum) {/// Random between 1 and 0. 
		if (proposeNum > seqNum) {
			seqNum = proposeNum;
			return r.nextInt(2);
		} else {
			return 0;
		}
	}

	@Override
	public int acceptorSendAccepted() { /// Random between 1 and 0. 
		return r.nextInt(2);
	}

	@Override
	public int isUpAndrunning() {
		// always running
		return 1;
	}
}
