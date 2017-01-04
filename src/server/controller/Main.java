package server.controller;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The static class used to run the Name server.
 * 
 *
 */
public class Main
{

	/**
	 * Start running the name server.
	 * 
	 * @throws AlreadyBoundException
	 */
	public static void main(String[] args) throws AlreadyBoundException
	{
		Registry registry;
		try
		{

			Wrapper wrap = new Wrapper();

			new MulticastServerThread(wrap).start();

			NodeRMI nodeRMI = new NodeRMI(wrap);
			registry = LocateRegistry.createRegistry(1099);
			registry.bind("nodeRMI", (Remote) nodeRMI);

		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
