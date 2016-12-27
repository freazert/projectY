package server.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.INodeRMI;

public class NodeRMI extends UnicastRemoteObject implements INodeRMI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashingMap hashmap;
    private Hashing currentHash;
    private Wrapper wrap;

    /**
     * The constructor method for nodeRMI. nodeRMI is used through 
     * 
     * @param wrap Wrapper object.
     * @throws RemoteException
     */
    protected NodeRMI(Wrapper wrap) throws RemoteException {
		
    	super();
		this.hashmap = wrap.getHashMap();
		this.wrap = wrap;
		// TODO Auto-generated constructor stub
	}

    @Override
    public int getCurrent(String userName) throws RemoteException {
        return new Hashing(userName).getHash();
    }

    @Override
    public int getPrevious(String userName) throws RemoteException {
    	return this.hashmap.getPrev(new Hashing(userName));
    }

    @Override
    public int getNext(String userName) throws RemoteException {
        return this.hashmap.getNext(new Hashing(userName));
    }

	@Override
	public String getFileNode(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return this.hashmap.getNode(new Hashing(name));

	}

	@Override
	public String getIp(int hash) throws RemoteException {
		return this.hashmap.getIp(hash);
	}

	@Override
	public int createNode(String name, String ip) throws RemoteException {
		// TODO Auto-generated method stub
		return wrap.createNode(name, ip);
	}

	@Override
	public int removeNode(int hash) throws RemoteException {
		// TODO Auto-generated method stub
		return wrap.removeNode(hash);
	}

	@Override
	public int getHash(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return (new Hashing(name)).getHash();

	}

	@Override
	public String getPrevIp(String filename) throws RemoteException {
		// TODO Auto-generated method stub
		return this.hashmap.getPrevIp(filename);
	}
	
	@Override
	public int getHmapSize() throws RemoteException {
		return this.hashmap.getCount();
	}


}
