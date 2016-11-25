package node;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import interfaces.IInitNodes;

public class Node {
	private int nextNode, prevNode, myNode;
	private IInitNodes rmi;
	private String name;
	
	public Node()
	{
		this.myNode = 5;
		this.nextNode = 3;
		this.prevNode = 8;
		
		try {
			this.rmi = (IInitNodes) Naming.lookup("//" + "192.168.1.15" + "/initNode");
		}catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initNodes(String name)
	{
		this.name = name;
		System.out.println("my name:" + name);
		try {
			this.myNode 	= this.rmi.getCurrent(name);
			this.prevNode 	= this.rmi.getPrevious(name);
			this.nextNode 	= this.rmi.getNext(name);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		printNodes();
	}
		
	
	public void setNodes(String name)
	{
		System.out.println("");
		System.out.println("node name: " + name);
		System.out.println("my name: " + this.name);
		int hash;
		try {
			hash = rmi.getCurrent(name);
			
			setPrev(hash);
			setNext(hash);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		printNodes();
	}
	
	private void setPrev(int hash)
	{
		if ((hash > this.prevNode && hash < this.myNode) || 
				( this.myNode < this.prevNode && hash > this.prevNode) || 
				this.prevNode == this.myNode ){
			this.prevNode = hash;
		}

	}
	
	private void setNext(int hash)
	{
		if ((hash < this.nextNode && hash > this.myNode) ||
				( this.myNode > this.nextNode && hash < this.nextNode) ||
				this.myNode == this.nextNode){
			this.nextNode = hash;
		}
	}
	
	public void printNodes()
	{
		System.out.println("my node: " + this.myNode);
		System.out.println("next node: " + this.nextNode);
		System.out.println("previous node: " + this.prevNode);
	}
	
	

}
