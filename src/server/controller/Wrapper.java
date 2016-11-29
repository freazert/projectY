package server.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import interfaces.IWrapper;

public class Wrapper extends UnicastRemoteObject implements IWrapper{
	
	private static final long serialVersionUID = 1L;
	
	private HashingMap hmap;
	
	public Wrapper() throws RemoteException
	{
		super();
		try {
			this.hmap = xmlToObject();
			System.out.println("start up");
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	public HashingMap getHashingMap()
	{
		return this.hmap;
	}
	public String getFileNode(String name)
	{
		Hashing hash = new Hashing(name);
		System.out.println(hash.getHash());
		
		return this.hmap.getNode(hash);
	}
	
	public int removeNode(int name)
	{
		int success = this.hmap.removeRecord(name);
		try {
			objectToXml();
			
			return success;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return 0;
		}
	}
	
	private void objectToXml() throws JAXBException {
		HashingMap object = this.hmap;
		JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
		// StringWriter writerTo = new StringWriter();
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("hashMap.xml");
			Marshaller marshaller = jaxbContext.createMarshaller();
			//StringWriter wr = new StringWriter();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(object, fileOut);
			//System.out.println(wr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getIp(int hash)
	{
		String ip = this.hmap.getIp(hash);
		
		return ip;
	}
	
	public int createNode(String name, String ip)
	{
		try {
			int success = this.hmap.addRecord(new Hashing(name), ip);
			objectToXml();
			
			return success;
		/*} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return 0;*/
		} catch (JAXBException e) {
			e.printStackTrace();
			
			return 0;
		}
	}

	private HashingMap xmlToObject() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(HashingMap.class);
		Unmarshaller u = jaxbContext.createUnmarshaller();
		this.hmap = new HashingMap();
		
		
		try {
			File f = new File("hashMap.xml");
			f.createNewFile();
			this.hmap = new HashingMap();
			objectToXml();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (HashingMap) u.unmarshal(new File("hashMap.xml"));

	}
	
	public int getCount()
	{
		return hmap.getCount();
	}
	
	public int getHash(String name)
	{
		return (new Hashing(name)).getHash();
	}
	
	public String getPrevIp(String filename)
	{
		return hmap.getPrevIp(filename);
	}
	

}
