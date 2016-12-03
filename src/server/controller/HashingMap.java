package server.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

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
			
			if(isFree == null) {
				this.hashMap.put(hash.getHash(), ip);
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

	public String getIp(int hash) 
	{
		return this.hashMap.get(hash).toString();
	}

	public int getNext(Hashing hash)
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
	    if(fileHash == highestRecord.getKey()) {
	    	record = lowestRecord;
	    }
	    
		return record.getKey();
	}
	
	public  <K, V> void printMap (Map<K,V> map) {
		for (Map.Entry<K,V> entry: map.entrySet()) {
			System.out.println("Key: " + entry.getKey() + " value : " + entry.getValue());
		}
	}
	
	
	
	public int getPrev(Hashing hash)
	{
		TreeMap<Integer, String> treeMap = new TreeMap<Integer, String> (this.hashMap);
		int value = hash.getHash();
		
		printMap(treeMap);
		
		Integer[] keyArr = treeMap.keySet().toArray(new Integer[treeMap.keySet().size()]);
		for (int i = 0; i < keyArr.length; i++) {
			int record =keyArr[i];
			System.out.println(keyArr[i] + ", ");
			if( value == record) {
				System.out.println(keyArr.length);
				if(i == 0) {
					
					return keyArr[keyArr.length-1];
					
				}
				
				return keyArr[i-1];
				
			} 
		}
		
		return value;
	}
	
	public String getPrevIp(String filename)
	{
		Map.Entry<Integer, String> record = null;
		Map.Entry<Integer, String> highestRecord = null;
		Map.Entry<Integer, String> lowestRecord = null;
		int fileHash = new Hashing(filename).getHash();
		
		Iterator it = this.hashMap.entrySet().iterator();
		System.out.println("searching... filehash = " + fileHash);
	    while (it.hasNext()) {
	        Map.Entry<Integer, String> pair = (Map.Entry)it.next();
	        System.out.println("pair key = " + pair.getKey());
	        if( record == null) {
	        	record = pair;
	        	System.out.println("new at start: " + record.getKey());
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
	        	if(fileHash > pair.getKey() && 
	        			(pair.getKey() > record.getKey() || fileHash < record.getKey())){
	        		record = pair;
	        		System.out.println("new: " + record.getKey());
	        		//System.out.Println(record.getKey().toString());
	        	}
	        }
	    }
	    
	    
	    // if fileHash is higher than highest hash, take lowest hash
	    if(fileHash < lowestRecord.getKey()) {
	    	record = highestRecord;
	    	System.out.println("new at end: " + record.getKey());
	    }
	    System.out.println("Search complete");
	    System.out.println("result: " + record.getKey());
	    
		return record.getValue();
	}
	
	public String get(Hashing hash)
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
	
	public String getNode(Hashing hash)
	{
		Map.Entry<Integer, String> record = null;
		Map.Entry<Integer, String> highestRecord = null;
		Map.Entry<Integer, String> lowestRecord = null;
		int fileHash = hash.getHash();
		
		Iterator it = this.hashMap.entrySet().iterator();
		System.out.println("searching... filehash = " + fileHash);
	    while (it.hasNext()) {
	        Map.Entry<Integer, String> pair = (Map.Entry)it.next();
	        System.out.println("pair key = " + pair.getKey());
	        if( record == null) {
	        	record = pair;
	        	System.out.println("new at start: " + record.getKey());
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
	        	if(fileHash > pair.getKey() && 
	        			(pair.getKey() > record.getKey() || fileHash < record.getKey())){
	        		record = pair;
	        		System.out.println("new: " + record.getKey());
	        		//System.out.Println(record.getKey().toString());
	        	}
	        }
	    }
	    
	    
	    // if fileHash is higher than highest hash, take lowest hash
	    if(fileHash < lowestRecord.getKey()) {
	    	record = highestRecord;
	    	System.out.println("new at end: " + record.getKey());
	    }
	    System.out.println("Search complete");
	    System.out.println("result: " + record.getKey());
	    
		return record.getValue();
	}
	
    public int getCount()
    {
    	return this.hashMap.size();
    }
    
    
    
}
