package node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Main {

	public static void main(String args[]) throws MalformedURLException, RemoteException, NotBoundException {

		System.setProperty("java.net.preferIPv4Stack", "true");

		/*
		 * String name; if(args.length > 0) { name = args[0]; } else { name =
		 * "noName"; } IWrapper obj = (IWrapper) Naming.lookup("//" +
		 * "192.168.1.16" + "/hash"); // objectname
		 * 
		 * System.out.println("connected"); //obj.createNode(name, ip) // in
		 * //obj.createNode(name); String ip = obj.getFileNode("lalala");
		 * System.out.println(ip); boolean test = ping(ip); if(test) {
		 * System.out.println("yowza"); }
		 */
		System.out.println("Enter name of the new agent: ");
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();

		Node node = new Node(name);
		// MulticastClient mc = new MulticastClient(node);
		while (true) {
			String cmd;
			// System.out.println("waiting for the cmd...");
			while (!sc.hasNextLine()) {
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

	/**
	 * Ping to given ip address.
	 * 
	 * @param ip
	 *            the ip address to ping to.
	 * @return boolean, failure: 0 success: 1.
	 */
	public static boolean ping(String ip) {

		InetAddress inet;

		try {
			inet = InetAddress.getByName(ip);
			if (inet.isReachable(500))
				return true;
			return false;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			return false;
		} catch (IOException e) {
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

}
// TESt = 30538 => solution = 192.168.56.1
// Jeroen = 23658 => solution = 192.168.1.1
// test = 17074 => solution = 192.168.56.1