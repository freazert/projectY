package server.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//import marshal2.Jef;
@XmlRootElement
public class HashingMap {
	@XmlElement
	public HashMap<Integer, String> hashMap;

	public HashingMap(Hashing hash) {
		this.hashMap = new HashMap<Integer, String>();
		addRecord(hash, "192.168.1.1");
	}

	public HashingMap() {
		this.hashMap = new HashMap<Integer, String>();
	}

	public void addRecord(Hashing hash, String ip) {
		this.hashMap.put(hash.getHash(), ip);
	}

	public void removeRecord(Hashing hash) {
		this.hashMap.remove(hash.getHash());
	}

	public String getIp(Hashing hash) {
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
	        	System.out.println("record is null");
	        	record = pair;
	        	highestRecord = pair;
	        	lowestRecord = pair;
	        } else {
	        	System.out.println("hash: " + hash.getHash());
	        	
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
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    // if fileHash is higher than highest hash, take lowest hash
	    if(fileHash > highestRecord.getKey()) {
	    	record = lowestRecord;
	    }
	    
		return record.getValue();
	}

	private void sort() {

	}

}
