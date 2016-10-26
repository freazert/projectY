package server.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.IWrapper;

public class Wrapper extends UnicastRemoteObject implements IWrapper{
	
	private static final long serialVersionUID = 1L;
	
	private HashingMap hmap;
	
	public Wrapper() throws RemoteException
	{
		super();
	}
	
	public String getFileNode(String name)
	{
		return hmap.getNext(new Hashing(name));
	}

}
