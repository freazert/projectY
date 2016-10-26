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
		String ip = obj.getFileNode("test.txt");
		
		System.out.print(ip);
	}
}
