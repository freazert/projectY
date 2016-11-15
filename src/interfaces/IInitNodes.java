package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by kevinvdm on 15/11/2016.
 */
public interface IInitNodes extends Remote

{
    int getCurrent() throws RemoteException;
    int getPrevious() throws RemoteException;
    int getNext()  throws RemoteException;
}
