package server.controller;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.INodeRMI;

/**
 * The class that can be remotely called for remote actions with the hashmap.
 */
public class NodeRMI extends UnicastRemoteObject implements INodeRMI
{

	/**
	 * serializable version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private HashingMap hashmap;
	private Wrapper wrap;

	
	protected NodeRMI(Wrapper wrap) throws RemoteException
	{
		super();
		this.hashmap = wrap.getHashMap();
		this.wrap = wrap;
	}

	@Override
	public int getCurrent(String userName) throws RemoteException
	{
		return new Hashing(userName).getHash();
	}

	@Override
	public int getPrevious(String userName) throws RemoteException
	{
		return this.hashmap.getPrev(new Hashing(userName).getHash());
	}
	
	@Override 
	public int getPrevious(int hash) throws RemoteException
	{
		return this.hashmap.getNodeHash(hash);
	}

	@Override
	public int getNext(String userName) throws RemoteException
	{
		return this.hashmap.getNext(new Hashing(userName).getHash());
	}
	
	@Override
	public int getNext(int hash) throws RemoteException
	{
		return this.hashmap.getNext(hash);
	}

	@Override
	public String getFileIp(String name) throws RemoteException
	{
		return this.hashmap.getNode(new Hashing(name).getHash());

	}
	
	@Override
	public int getFileNode(int hash) throws RemoteException
	{
		return this.hashmap.getNodeHash(hash);

	}

	@Override
	public String getIp(int hash) throws RemoteException
	{
		return this.hashmap.getIp(hash);
	}

	@Override
	public int createNode(String name) throws RemoteException, ServerNotActiveException
	{
		return wrap.createNode(name, getClientHost());
	}

	@Override
	public int removeNode(int hash) throws RemoteException
	{
		return wrap.removeNode(hash);
	}

	@Override
	public int getHash(String name) throws RemoteException
	{
		return (new Hashing(name)).getHash();

	}

	@Override
	public String getPrevIp(String filename) throws RemoteException
	{
		return this.hashmap.getPrevIp(filename);
	}

	@Override
	public int getHmapSize() throws RemoteException
	{
		return this.hashmap.getCount();
	}

}
