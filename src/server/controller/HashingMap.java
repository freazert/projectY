package server.controller;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


//import marshal2.Jef;
@XmlRootElement
public class HashingMap {
	@XmlElement
	public HashMap<Integer, String> hashMap;
	
	public HashingMap(Hashing hash)
	{
		this.hashMap = new HashMap<Integer, String>();
		addRecord(hash, "192.168.1.1");
	}
	
	public HashingMap()
	{
		this.hashMap = new HashMap<Integer, String>();
	}
	
	public void addRecord(Hashing hash, String ip)
	{
		this.hashMap.put(hash.getHash(), ip);
	}
	
	public void removeRecord(Hashing hash)
	{
		this.hashMap.remove(hash.getHash());
	}
	
	public String getIp(Hashing hash)
	{
		return this.hashMap.get(hash.getHash()).toString();
	}
	
	
}
