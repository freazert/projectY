package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INodeRMI extends Remote{
	
	int getCurrent(String userName) throws RemoteException;
    int getPrevious(String userName) throws RemoteException;
    int getNext(String userName)  throws RemoteException;
    
	String getFileNode(String name) throws RemoteException;
	String getIp(int hash) throws RemoteException;
	
	int createNode(String name, String ip) throws RemoteException;
	int removeNode(String name) throws RemoteException;
	int getHash(String name) throws RemoteException;
	String getPrevIp(String filename) throws RemoteException;
	
}
