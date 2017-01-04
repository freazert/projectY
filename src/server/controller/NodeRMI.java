package server.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.INodeRMI;

/**
 * The class that can be remotely called for remote actions with the hashmap.
 */
public class NodeRMI extends UnicastRemoteObject implements INodeRMI
{

	/**
	 * serialisable version.
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
		return this.hashmap.getPrev(new Hashing(userName));
	}

	@Override
	public int getNext(String userName) throws RemoteException
	{
		return this.hashmap.getNext(new Hashing(userName));
	}

	@Override
	public String getFileNode(String name) throws RemoteException
	{
		return this.hashmap.getNode(new Hashing(name));

	}

	@Override
	public String getIp(int hash) throws RemoteException
	{
		return this.hashmap.getIp(hash);
	}

	@Override
	public int createNode(String name, String ip) throws RemoteException
	{
		return wrap.createNode(name, ip);
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
