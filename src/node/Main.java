package node;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import interfaces.IWrapper;

public class Main {
	public static void main(String args[]) throws MalformedURLException, RemoteException, NotBoundException
	{
		IWrapper obj = (IWrapper) Naming.lookup( "//"+"localhost"+"/hash");         //objectname in registry
		//obj.addNode("test23", "192.168.1.4");
		//System.out.println(obj.removeNode("test1234"));
		//System.out.println("done");
		System.out.println(obj.getFileNode("TESt"));
		//String ip = obj.getIp("Jeroen");
		
		//System.out.print(ip);
	}
}
//TESt = 30538 => solution = 192.168.56.1
//Jeroen = 23658 => solution = 192.168.1.1
//test = 17074 => solution = 192.168.56.1