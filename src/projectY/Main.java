package projectY;

//import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.controller.Wrapper;

public class Main
{
	public static void main(String args[]) throws AlreadyBoundException
	{
		Registry registry;
		try
		{

			Wrapper wrap = new Wrapper();

			registry = LocateRegistry.createRegistry(1099);
			registry.bind("hash", (Remote) wrap);

		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
