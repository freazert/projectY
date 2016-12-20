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

	/*public HashingMap(Hashing hash) 
	{
		this.hashMap = new HashMap<Integer, String>();
		addRecord(hash, "192.168.1.1");
	}*/

	/**
	 * The constructor method for the hashing map.
	 */
	public HashingMap() 
	{
		this.hashMap = new HashMap<Integer, String>();
	}

	/**
	 * Add new node to the hashing map.
	 * 
	 * @param hash The hash that needs to be inserted in the hashingmap
	 * @param ip the ip of the new node.
	 * @return boolean, 1 on success. 0 on failure.
	 */
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

	/**
	 * Delete record from the hashingmap.
	 * 
	 * @param hash the hash of the node that needs to be removed.
	 * @return boolean. 1 on success, 0 on failure.
	 */
	public int removeRecord(int hash)
	{
		try {
			this.hashMap.remove(hash);
			
			return 1;
		} catch(Exception e) {
			e.printStackTrace();
			
			return 0;
		}
		
	}

	/**
	 * Get ip from the node with a certain hash.
	 * 
	 * @param hash hashed value of the name.
	 * @return the ip of the node.
	 */
	public String getIp(int hash) 
	{
		return this.hashMap.get(hash).toString();
	}

	/**
	 * Get the next hash
	 * 
	 * @param hash the current hash
	 * @return The next hash.
	 */
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
	
	/**
	 * Print the complete hashmap
	 * 
	 * @param map
	 */
	public  <K, V> void printMap (Map<K,V> map) {
		for (Map.Entry<K,V> entry: map.entrySet()) {
			System.out.println("Key: " + entry.getKey() + " value : " + entry.getValue());
		}
	}
	
	
	/**
	 * Get the previous hash
	 * 
	 * @param hash the current hash.
	 * @return the previous hash.
	 */
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
	
	/**
	 * Get ip of the owner of the file.
	 * 
	 * @param filename The name of the file that needs to be found.
	 * @return the ip of the owner of the file.
	 */
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
	
	/*public String getPrevIp(String filename)
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
	}*/
	
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
	
	/**
	 * Get size of the hashmap
	 * 
	 * @return size of the hashmap.
	 */
    public int getCount()
    {
    	return this.hashMap.size();
    }
    
    
    
}
