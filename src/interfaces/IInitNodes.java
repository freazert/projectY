package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by kevinvdm on 15/11/2016.
 */
public interface IInitNodes extends Remote

{
    int getCurrent(String userName) throws RemoteException;
    int getPrevious(String userName) throws RemoteException;
    int getNext(String userName)  throws RemoteException;
}
