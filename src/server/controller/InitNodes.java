package server.controller;

import interfaces.IInitNodes;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by kevinvdm on 15/11/2016.
 */
public class InitNodes extends UnicastRemoteObject implements IInitNodes {
    private int currentNode, previousNode, nextNode;
    private HashingMap hashmap;
    private Hashing currentHash;

    public InitNodes(HashingMap hashmap) throws RemoteException{
    	super();
    	this.hashmap = hashmap;
    }

    @Override
    public int getCurrent(String userName) throws RemoteException {
    	currentHash = new Hashing(userName);
        return this.currentHash.getHash();
    }

    @Override
    public int getPrevious(String userName) throws RemoteException {
        return this.hashmap.getPrev(this.currentHash);
    }

    @Override
    public int getNext(String userName) throws RemoteException {
        return this.hashmap.getNext(this.currentHash);
    }


}
