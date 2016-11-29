package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.controller.Hashing;

public interface IWrapper extends Remote {
	String getFileNode(String name) throws RemoteException;
	String getIp(int hash) throws RemoteException;
	int createNode(String name, String ip) throws RemoteException;
	int removeNode(int name) throws RemoteException;
	int getHash(String name) throws RemoteException;
	public String getPrevIp(String filename) throws RemoteException;

}
