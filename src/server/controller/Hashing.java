package server.controller;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
	private String name;
	private int hash;

	public Hashing(String name)
	{
		this.name = name;
		makeHash();
	}
	
	public int getHash()
	{
		return this.hash;
	}
	
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
	
	public String getName()
	{
		return this.name;
	}
	
	
	
	
}
