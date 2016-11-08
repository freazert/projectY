package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IWrapper extends Remote {
	String getFileNode(String name) throws RemoteException;
	String getIp(String name) throws RemoteException;
	int createNode(String name, String ip) throws RemoteException;
	int removeNode(String name) throws RemoteException;

}
