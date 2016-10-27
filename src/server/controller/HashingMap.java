package server.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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

	public int addRecord(Hashing hash, String ip) 
	{
		try {
			String isFree = this.hashMap.get(hash.getHash());
			System.out.println(isFree);
			if(isFree == null) {
				this.hashMap.put(hash.getHash(), ip);
				System.out.println("toch ni null, ofwel ofni");
				return 1;
			}
			
			return 0;
			
		} catch(Exception e) {
			e.printStackTrace();
			
			return 0;
		}
		
	}

	public int removeRecord(Hashing hash) 
	{
		try {
			this.hashMap.remove(hash.getHash());
			
			return 1;
		} catch(Exception e) {
			e.printStackTrace();
			
			return 0;
		}
		
	}

	public String getIp(Hashing hash) 
	{
		return this.hashMap.get(hash.getHash()).toString();
	}

	public String getNext(Hashing hash)
	{
		Map.Entry<Integer, String> record = null;
		Map.Entry<Integer, String> highestRecord = null;
		Map.Entry<Integer, String> lowestRecord = null;
		int fileHash = hash.getHash();
		
		Iterator it = this.hashMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Integer, String> pair = (Map.Entry)it.next();
	        if( record == null) {
	        	record = pair;
	        	highestRecord = pair;
	        	lowestRecord = pair;
	        } else {	        	
	        	//set lowest and highest hashes
	        	if(highestRecord.getKey() < pair.getKey()) {
	        		highestRecord = pair;
	        	} else if(lowestRecord.getKey() > pair.getKey()) {
	        		lowestRecord = pair;
	        	}
	        	
	        	//check for new record
	        	if(fileHash < pair.getKey() && record.getKey() > pair.getKey()) {
	        		record = pair;
	        	}
	        }
	    }
	    
	    // if fileHash is higher than highest hash, take lowest hash
	    if(fileHash > highestRecord.getKey()) {
	    	record = lowestRecord;
	    }
	    
		return record.getValue();
	}
}
