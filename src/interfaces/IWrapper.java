package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IWrapper extends Remote {
	
	String getFileNode(String name) throws RemoteException;

}
