package node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import interfaces.IWrapper;
import sun.misc.IOUtils;

public class Main {

	public static void main(String args[]) throws MalformedURLException, RemoteException, NotBoundException {
		IWrapper obj = (IWrapper) Naming.lookup("//" + "localhost" + "/hash"); // objectname
																				// in
																				// registry
		obj.createNode("kris");
		// System.out.println(obj.removeNode("test1234"));

		String ip = obj.getFileNode("test1234");
		System.out.println(ping(ip));

	}

	public static boolean ping(String ip) {
		
		InetAddress inet;

	    try {
			inet = InetAddress.getByName(ip);
		    if(inet.isReachable(5000)) 
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
		try {
			String command = "ping  " + ip;
			Process process = Runtime.getRuntime().exec(command);
			Scanner s = new Scanner(process.getInputStream()).useDelimiter("\\A");
			String result = s.hasNext() ? s.next() : "";
			System.out.print(result);

		} catch (Exception e) {
		}

		System.out.println("done");
		// String ip = obj.getIp("Jeroen");

		// System.out.print(ip);

*/
	}

}
//TESt = 30538 => solution = 192.168.56.1
//Jeroen = 23658 => solution = 192.168.1.1
//test = 17074 => solution = 192.168.56.1