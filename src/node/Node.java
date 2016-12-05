package node;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import interfaces.INodeRMI;

public class Node {
	private int nextNode, prevNode, myNode;
	private INodeRMI rmi;
	private String name;
	private List<String> OwnerList;
	private List<String> localList;

	public List<String> getLocalList() {
		return localList;
	}

	/**
	 * The constructor method node.
	 * 
	 * @param name the agentname of the node.
	 */
	public Node(String name) {
		this.OwnerList = new ArrayList<String>();
		this.localList = new ArrayList<String>();
		this.name = name;

		MulticastClient mc = new MulticastClient(this);

		mc.multicastStart(name);

		this.initNodes();
		this.SearchMap();
		new CheckFolderThread(this, 400).start();

		try {
			this.rmi = (INodeRMI) Naming.lookup("//" + "192.168.1.15" + "/nodeRMI");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize nodes on startup.
	 */
	private void initNodes() {
		try {
			this.myNode = this.rmi.getCurrent(name);
			this.prevNode = this.rmi.getPrevious(name);
			this.nextNode = this.rmi.getNext(name);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * set previous and next node when a new connection is made, using the name of the new node.
	 * 
	 * @param name the name of the new node
	 */
	public void setNodes(String name) {
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

	/**
	 * Set new previous node.
	 * 
	 * @param hash the hash of the new node
	 */
	private void setPrev(int hash) {
		if ((hash > this.prevNode && hash < this.myNode) || (this.myNode < this.prevNode && hash > this.prevNode)
				|| this.prevNode == this.myNode) {
			this.prevNode = hash;
		}

	}

	/**
	 * Set new next node.
	 * 
	 * @param hash the hash of the new node
	 */
	private void setNext(int hash) {
		if ((hash < this.nextNode && hash > this.myNode) || (this.myNode > this.nextNode && hash < this.nextNode)
				|| this.myNode == this.nextNode) {
			this.nextNode = hash;
		}
	}

	/**
	 * Print out the current nodes.
	 */
	private void printNodes() {
		System.out.println("my node: " + this.myNode);
		System.out.println("next node: " + this.nextNode);
		System.out.println("previous node: " + this.prevNode);
	}

	public void shutdown() {/*
							 * try { DatagramPacket packet; IWrapper obj =
							 * (IWrapper) Naming.lookup("//" + "192.168.1.15" +
							 * "/getWrapper"); obj.removeNode(this.prevNode);
							 * 
							 * socket = new DatagramSocket(2000);
							 * 
							 * String toSend = "previous: " + previous; byte[]
							 * buf = new byte[toSend.getBytes().length]; buf =
							 * toSend.getBytes();
							 * 
							 * packet = new
							 * DatagramPacket(buf,buf.length,obj.getIp(next),
							 * portMulticasting); socket.send(packet);
							 * 
							 * 
							 * 
							 * 
							 * obj.getIp(previuos);
							 * 
							 * String toSend = "next: " + next; byte[] buf = new
							 * byte[toSend.getBytes().length]; buf =
							 * toSend.getBytes();
							 * 
							 * packet = new
							 * DatagramPacket(buf,buf.length,obj.getIp(previous)
							 * ,portMulticasting); socket.send(packet);
							 * 
							 * 
							 * } catch (MalformedURLException e) { // ODO
							 * Auto-generated catch block e.printStackTrace(); }
							 * catch (RemoteException e) { // ODO Auto-generated
							 * catch block e.printStackTrace(); } catch
							 * (NotBoundException e) { // ODO Auto-generated
							 * catch block e.printStackTrace(); }
							 */
	}

	/**
	 * Get list of files in map.
	 */
	private void SearchMap() {
		// TODO Auto-generated method stub
		File folder = new File("c:\\Nieuwe map");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				this.newFile(listOfFiles[i]);
			}
		}
	}

	/**
	 * Get previous node.
	 * 
	 * @return the previous node hash
	 */
	public int getPrev() {
		return this.prevNode;
	}

	/**
	 * Get next node.
	 * 
	 * @return the next node hash
	 */
	public int getNext() {
		return this.nextNode;
	}

	/**
	 * get own node.
	 * 
	 * @return the local node hash
	 */
	public int getCurrent() {
		return this.myNode;
	}

	public void controlFiles() {
		// TODO Auto-generated method stub
		String[] eigenaarBestand = new String[] { "lol", "lp" };

		for (String bestand : eigenaarBestand) {
			try {
				if (this.rmi.getHash(this.rmi.getPrevIp(bestand)) == this.prevNode) {
					// send file
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get the owner of the file.
	 * 
	 * @param file the file that needs to be sent
	 */
	public void newFile(File file) {
		// TODO Auto-generated method stub
		try {

			String ipFileToRep = rmi.getPrevIp(file.getName());

			if ((rmi.getHash(ipFileToRep)) == this.myNode) {
				ipFileToRep = rmi.getIp(this.prevNode);
			}
			// int hash = this.rmi.(obj.getHash(listOfFiles[i].getName()));
			SendFileThread sft = new SendFileThread(ipFileToRep, file);
			sft.start();

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
