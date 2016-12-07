package node;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
			failure();
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
			failure();
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
			failure();
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
	{
		try {
			DatagramPacket packetNext, packetPrevious;
			IWrapper obj = (IWrapper) Naming.lookup("//" + "192.168.1.15" + "/hash");
			obj.removeNode(this.myNode);
			
			try {
                   DatagramSocket socket = new DatagramSocket(4448);
                    String toSendPrev = "node gone, previous: " + this.prevNode;
                   byte[] bufPrev = new byte[toSendPrev.getBytes().length];
                   bufPrev = toSendPrev.getBytes();
					packetNext = new DatagramPacket(bufPrev, bufPrev.length, InetAddress.getByName(obj.getIp(this.nextNode)), 4448);
                   socket.send(packetNext);
                    String toSendNext = "node gone, next: " + this.nextNode;
                   byte[] bufNext = new byte[toSendNext.getBytes().length];
                   bufNext = toSendNext.getBytes();
					packetPrevious = new DatagramPacket(bufNext, bufNext.length, InetAddress.getByName(obj.getIp(this.prevNode)), 4448);
                   socket.send(packetPrevious);
            } 
			catch (java.net.SocketException e){
                   failure();
                   e.printStackTrace();
            } 
			catch(java.io.IOException e) {
                   failure();
                   e.printStackTrace();
            }
			
		} 
		catch (MalformedURLException e) {
			failure();
			e.printStackTrace();
		} 
		catch (RemoteException e) {
			failure();
			e.printStackTrace();
		} 
		catch (NotBoundException e) {
			failure();
			e.printStackTrace();
		}
	}
	 
	public void failure(){
		try {
			DatagramPacket packetNext, packetPrevious;
			this.prevNode 	= this.rmi.getPrevious(name);
			this.nextNode 	= this.rmi.getNext(name);
			IWrapper obj = (IWrapper) Naming.lookup("//" + "192.168.1.15" + "/hash");
			
			try {
                DatagramSocket socket = new DatagramSocket(4448);
                
                String toSendPrev = "node failed, previous: " + this.prevNode;
                byte[] bufPrev = new byte[toSendPrev.getBytes().length];
                bufPrev = toSendPrev.getBytes();
				packetNext = new DatagramPacket(bufPrev, bufPrev.length, InetAddress.getByName(obj.getIp(this.nextNode)), 4448);
                socket.send(packetNext);

                String toSendNext = "node failed, next: " + this.nextNode;
                byte[] bufNext = new byte[toSendNext.getBytes().length];
                bufNext = toSendNext.getBytes();
				packetPrevious = new DatagramPacket(bufNext, bufNext.length, InetAddress.getByName(obj.getIp(this.prevNode)), 4448);
                socket.send(packetPrevious);
                
    			obj.removeNode(this.myNode);

            }
			catch (java.net.SocketException e){
                System.out.println("FAILURE: SOCKET EXCEPTION");
                e.printStackTrace();
            }
            catch(java.io.IOException e) {
                System.out.println("FAILURE: IO EXCEPTION");
                e.printStackTrace();
            }
			
		} 
		catch (MalformedURLException e) {
            System.out.println("FAILURE: MALFORMED URL EXCEPTION");
			e.printStackTrace();
		} 
		catch (RemoteException e) {
            System.out.println("FAILURE: REMOTE EXCEPTION");
			e.printStackTrace();
		} 
		catch (NotBoundException e) {
            System.out.println("FAILURE: NOT BOUND EXCEPTION");
			e.printStackTrace();
		}
	}

	public void SearchMap() {
		// TODO Auto-generated method stub
		File folder = new File("your/path");
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
					failure();
					e.printStackTrace();
				} catch (RemoteException e) {
					failure();
					e.printStackTrace();
				} catch (NotBoundException e) {
					failure();
					e.printStackTrace();
				}

		      } 
		    }
	}
	

}
