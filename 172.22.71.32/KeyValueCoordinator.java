import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Central Coordinator Server
 */
public class KeyValueCoordinator implements KeyValueStore {

	String[] serverIPs = {"172.22.71.27", "172.22.71.28", "172.22.71.29", "172.22.71.30", "172.22.71.31"};
	String[] serverNames = {"Server1", "Server2", "Server3", "Server4", "Server5"};
	int[] whichIsUpAndRunning = {0, 0, 0, 0, 0};
	ArrayList<KeyValueStore> stubs;

	private KeyValueCoordinator () {
		try {
			stubs = new ArrayList<KeyValueStore>();
	        for (int i = 0; i < serverNames.length; i++) {
	        	Registry registry = LocateRegistry.getRegistry(serverIPs[i]);
	            stubs.add((KeyValueStore) registry.lookup(serverNames[i]));
	        }
		} catch (Exception e) {
			System.out.println("Error");
			System.out.println(e.toString());
		}
	}
	
	@Override
	public synchronized KeyValueReturnWrapper put(String key, String value) throws RemoteException {
		int count = 0;
        for (int i = 0; i < serverNames.length; i++) {
            if (stubs.get(i).ackPlease() == 1) count++;
        }
        if (count == 5) {
        	for (int i = 0; i < serverNames.length; i++) {
                stubs.get(i).put(key, value);
            }
        }
        
		return new KeyValueReturnWrapper(key, value, KeyValueCode.PUT);
	}
	
	
	
	@Override
	public synchronized KeyValueReturnWrapper delete(String key) throws RemoteException {
		int count = 0;
		for (int i = 0; i < serverNames.length; i++) {
            if (stubs.get(i).ackPlease() == 1) count++;
        }
        if (count == 5) {
        	for (int i = 0; i < serverNames.length; i++) {
                stubs.get(i).delete(key);
            }
        }
        
        return new KeyValueReturnWrapper(key, "",
				KeyValueCode.DELETE);
	}
	
	
	@Override
	public synchronized KeyValueReturnWrapper get(String key) throws RemoteException  {
		KeyValueReturnWrapper returner = null;
		int count = 0;
		for (int i = 0; i < serverNames.length; i++) {
            if (stubs.get(i).ackPlease() == 1) count++;
        }
        if (count == 5) {
        	for (int i = 0; i < serverNames.length; i++) {
        		returner = stubs.get(i).get(key);
            }
        }
        return returner;
	}
	
	
	
	
	
	
	public static void main(String[] args) {

    	
    	try {
        	String host = "172.22.71.32";
        	KeyValueCoordinator kvserver = new KeyValueCoordinator();
            KeyValueStore stub = 
            		(KeyValueStore) UnicastRemoteObject.exportObject(kvserver, 0);
            Registry registry = LocateRegistry.getRegistry(host);
//            Registry registry = LocateRegistry.getRegistry();
            String name = "coordinator";
            registry.bind(name, stub);

            System.out.println("KeyValueCoordinator started, bound to ");
            System.out.println("registry at ip:" + host + "and name:" + name);
        } catch (Exception e) {
            System.out.println("Error starting server " + e.toString());
            e.printStackTrace();
        }
	}

	
	public void checkWhichAreRunning() throws RemoteException {
		
		int count = 0;
		int runningIndex = -1;
		for (int i = 0; i < serverNames.length; i++) {
            if (stubs.get(i).isUpAndrunning() == 1) {
            	whichIsUpAndRunning[i] = 1;
            	count++;
            	runningIndex = i;
            }
            else whichIsUpAndRunning[i] = 0;
    		// TODO Auto-generated method stub
    		
        }
		int[] promised = {0,0,0,0,0};
		int promisedSum = 0;
		if (count > 1) {
			int tempSeqNum = stubs.get(runningIndex).proposerSendPrepare();        // server 5 = proposer
			for (int i = 0; i < serverNames.length; i++) {
				if (whichIsUpAndRunning[i] == 1) {
					promised[i] = stubs.get(i).acceptorSendPromise(tempSeqNum);
					promisedSum++;
				}
			}
		}
		int[] accepted = {0,0,0,0,0};
		int acceptedSum = 0;
		stubs.get(runningIndex).proposerSendAccept();
		for (int i = 0; i < serverNames.length; i++) {
			if (promised[i] == 1) {
				acceptedSum++;
				accepted[i] = stubs.get(i).acceptorSendAccepted();
			}
		}
	}
	
	
	
	
	// not used for the coordinator.
	
	@Override
	public int ackPlease() throws RemoteException {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public int proposerSendPrepare() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public void proposerSendAccept() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int acceptorSendPromise(int seqNum) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int acceptorSendAccepted() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int isUpAndrunning() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}