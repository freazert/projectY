package server.controller;

import interfaces.IInitNodes;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by kevinvdm on 15/11/2016.
 */
public class InitNodes extends UnicastRemoteObject implements IInitNodes {
    private int currentNode, previousNode, nextNode;

    public InitNodes(int currentNode, int previousNode, int nextNode) throws RemoteException{
        this.currentNode = currentNode;
        this.previousNode = previousNode;
        this.nextNode = nextNode;
    }

    @Override
    public int getCurrent() throws RemoteException {
        return 0;
    }

    @Override
    public int getPrevious() throws RemoteException {
        return 0;
    }

    @Override
    public int getNext() throws RemoteException {
        return 0;
    }


}
