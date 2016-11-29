package node;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import interfaces.IInitNodes;
import interfaces.IWrapper;

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
	
	 public void shutdown()
		{/*
			try {
				DatagramPacket packet;
				IWrapper obj = (IWrapper) Naming.lookup("//" + "192.168.1.15" + "/getWrapper");
				obj.removeNode(this.prevNode);
				
				socket = new DatagramSocket(2000);
				
				String toSend = "previous: " + previous;
				byte[] buf = new byte[toSend.getBytes().length];
				buf = toSend.getBytes();
				
	            packet = new DatagramPacket(buf,buf.length,obj.getIp(next),portMulticasting);
	            socket.send(packet);
	            
	            
	            
	            
				obj.getIp(previuos);
				
				String toSend = "next: " + next;
				byte[] buf = new byte[toSend.getBytes().length];
				buf = toSend.getBytes();
				
	            packet = new DatagramPacket(buf,buf.length,obj.getIp(previous),portMulticasting);
	            socket.send(packet);
				
				
			} catch (MalformedURLException e) {
				// ODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// ODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// ODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}

	public void SearchMap() {
		// TODO Auto-generated method stub
		File folder = new File("c:\\Nieuwe map");
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  
		        System.out.println("File " + listOfFiles[i].getName());
				try {
					IWrapper obj = (IWrapper) Naming.lookup("//" + "192.168.1.15" + "/getWrapper");
					String ipFileToRep = obj.getPrevIp(listOfFiles[i].getName());
					
					if((obj.getHash(ipFileToRep)) == this.myNode)
					{
						ipFileToRep = obj.getIp(this.prevNode);
					}
					//int hash =  this.rmi.(obj.getHash(listOfFiles[i].getName()));
				
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		      } 
		    }
	}
	

}
