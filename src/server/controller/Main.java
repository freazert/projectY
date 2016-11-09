package server.controller;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

	public static void main(String[] args) throws AlreadyBoundException 
	{
		Registry registry;
		try {
			
			Wrapper wrap = new Wrapper();
			
			new MulticastServerThread(wrap).start();
			
			
			//registry = LocateRegistry.createRegistry(1099);
			//registry.bind("hash", (Remote)wrap);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*Hashing hash = new Hashing("YorickDeBock");
		HashingMap hmap;z

		/*System.out.println(hash.getHash());
		System.out.println(hash.getName());
		
		/*hmap = new HashingMap();
		hmap.addRecord(hash, "192.168.1.1");
		hash = new Hashing("Jeroen");
		hmap.addRecord(hash, "192.168.1.2");
		hash = new Hashing("Kris");
		hmap.addRecord(hash, "192.168.1.3");*/
		
		

		/*-try {
			hmap = xmlToObject();
			System.out.println(hmap.getNext(new Hashing("test.txt")));
			//System.out.println(hmap.toString());
			//objectToXml(hmap);
			//System.out.println(hmap.getIp(new Hashing("YorickDeBock")));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//new HashingMap();
		
		
		
		/*hmap.addRecord(new Hashing("jeroen"), "192.168.1.2");
		hmap.addRecord(new Hashing("Kris"), "192.168.1.3");
		/*try {
			objectToXml(hmap);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		// System.out.println(hmap.getIp(new Hashing("Kris")));

	}

	

}
