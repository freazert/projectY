package server.controller;

public class Wrapper {
	private HashingMap hmap;
	
	public Wrapper()
	{
		
	}
	
	public String getFileNode(String name)
	{
		return hmap.getNext(new Hashing(name));
	}

}
