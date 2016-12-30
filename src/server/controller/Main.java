package server.controller;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main
{

	public static void main(String[] args) throws AlreadyBoundException
	{
		Registry registry;
		try
		{

			Wrapper wrap = new Wrapper();

			new MulticastServerThread(wrap).start();

			NodeRMI nodeRMI = new NodeRMI(wrap);
			// InitNodes newNode = new InitNodes(wrap.getHashingMap());
			registry = LocateRegistry.createRegistry(1099);
			registry.bind("nodeRMI", (Remote) nodeRMI);

			// registry = LocateRegistry.createRegistry(1099);
			// registry.bind("hash", (Remote)wrap);

		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
