package node;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.util.Scanner;

import gui.GUI;
import interfaces.INodeRMI;

/**
 * The static class used to run the node side of the project.
 */
public class Main
{
	/**
	 * The ip address of the name server.
	 */
	private static String SERVER_IP = "192.168.1.16";

	/**
	 * The first function that is ran by the user.
	 * 
	 * @param args
	 *            all the arguments given in the console
	 * @throws MalformedURLException
	 *             Something went wrong while forming the RMI url.
	 * @throws RemoteException
	 *             Something went wrong while conecting to the server.
	 */
	public static void main(String args[]) throws MalformedURLException, RemoteException
	{
		System.setProperty("java.net.preferIPv4Stack", "true");

		try
		{
			INodeRMI rmi = (INodeRMI) Naming.lookup("//" + SERVER_IP + "/nodeRMI");

			int success = 0;
			String name = "";
			Scanner sc = new Scanner(System.in);

			while (success <= 0)
			{
				System.out.println("Enter name of the new node: ");

				name = sc.nextLine();
				success = rmi.createNode(name);
				if (success == 0)
				{
					System.out.println("This name is already taken, please choose another.");
				} else
				{
					System.out.println("Node added succesfully.");
				}
			}

			Node node = new Node(name, rmi);

			listenForInput(node, sc);
		} catch (MalformedURLException | RemoteException | NotBoundException e)
		{
			// failure();
			e.printStackTrace();
		} catch (ServerNotActiveException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Ping to given ip address.
	 * 
	 * @param ip
	 *            the ip address to ping to.
	 * @return boolean, failure: 0 success: 1.
	 */
	public static boolean ping(String ip)
	{

		InetAddress inet;

		try
		{
			inet = InetAddress.getByName(ip);
			if (inet.isReachable(500))
				return true;
			return false;
		} catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			return false;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			return false;
		}

		/*
		 * try { String command = "ping  " + ip; Process process =
		 * Runtime.getRuntime().exec(command); Scanner s = new
		 * Scanner(process.getInputStream()).useDelimiter("\\A"); String result
		 * = s.hasNext() ? s.next() : ""; System.out.print(result);
		 * 
		 * } catch (Exception e) { }
		 * 
		 * System.out.println("done"); // String ip = obj.getIp("Jeroen");
		 * 
		 * // System.out.print(ip);
		 * 
		 */
	}

	/**
	 * Listen for input in the command line.
	 * 
	 * @param node
	 *            The node that runs the project.
	 * @param sc
	 *            The reader object for the command line.
	 */
	private static void listenForInput(Node node, Scanner sc)
	{
		while (true)
		{
			String cmd;
			while (!sc.hasNextLine())
			{
				// System.out.println("noooooz");
			}

			cmd = sc.nextLine();
			System.out.println("ciao bello");
			node.shutdown();

			// } catch (Exception e) {
			// e.printStackTrace(arg0);
			// }
		}
	}

}
// TESt = 30538 => solution = 192.168.56.1
// Jeroen = 23658 => solution = 192.168.1.1
// test = 17074 => solution = 192.168.56.1