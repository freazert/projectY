package server.controller;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
	private String name;
	private int hash;

	/**
	 * The constructor method for Hashing.
	 * 
	 * @param name the name that needs to be hashed.
	 */
	public Hashing(String name)
	{
		this.name = name;
		makeHash();
	}
	
	/**
	 * Getter method for the hashed value.
	 * 
	 * @return the hashed value.
	 */
	public int getHash()
	{
		return this.hash;
	}
	
	/**
	 * Getter method for the initial name.
	 * 
	 * @return the initial name.
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Create the hash.
	 */
	private void makeHash()
	{
		MessageDigest md = null;
		try{
			md = MessageDigest.getInstance("SHA-1");
		} catch(NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
		md.reset();
		try {
			md.update(this.name.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int h = ByteBuffer.wrap(md.digest()).getInt();
		h = Math.abs(h);
		this.hash = h % 32769;
	}
	
	
	
	
	
	
}
