package node;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

//<<<<<<< HEAD
import interfaces.INodeRMI;
/*=======
import interfaces.IInitNodes;
import interfaces.IWrapper;
>>>>>>> origin/feature/shutdown*/

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
	 * @param name
	 *            the agentname of the node.
	 */
	public Node(String name) {
		this.OwnerList = new ArrayList<String>();
		this.localList = new ArrayList<String>();
		this.name = name;

		MulticastClient mc = new MulticastClient(this);

		mc.multicastStart(name);

		try {
			this.rmi = (INodeRMI) Naming.lookup("//" + "192.168.1.16" + "/nodeRMI");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			failure();
			e.printStackTrace();
		}
		printNodes();
		this.initNodes();
		if (this.myNode != this.prevNode) {
			this.SearchMap();
		} else {
			ReceiveUDPThread rft = new ReceiveUDPThread(this);
			rft.start();
		}

		//new CheckFolderThread(this, 400).start();
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
			failure();
			e.printStackTrace();
		}
	}
	
	

	/**
	 * set previous and next node when a new connection is made, using the name
	 * of the new node.
	 * 
	 * @param name
	 *            the name of the new node
	 */
	public void setNodes(String name) {
		try {
			int hash = this.rmi.getCurrent(name);
			System.out.println(name);

			setPrev(hash);
			setNext(hash);

		} catch (RemoteException e) {
			failure();
			e.printStackTrace();
		}

		printNodes();
	}

	/**
	 * Set new previous node.
	 * 
	 * @param hash
	 *            the hash of the new node
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
	 * @param hash
	 *            the hash of the new node
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

	public void shutdown() {
		try {
			DatagramPacket packetNext, packetPrevious;
			INodeRMI obj = (INodeRMI) Naming.lookup("//" + "192.168.1.15" + "/hash");
			obj.removeNode(this.myNode);

			DatagramSocket socket = new DatagramSocket(4448);
			String toSendPrev = "node gone, previous: " + this.prevNode;
			byte[] bufPrev = new byte[toSendPrev.getBytes().length];
			bufPrev = toSendPrev.getBytes();
			packetNext = new DatagramPacket(bufPrev, bufPrev.length, InetAddress.getByName(obj.getIp(this.nextNode)),
					4448);
			socket.send(packetNext);
			String toSendNext = "node gone, next: " + this.nextNode;
			byte[] bufNext = new byte[toSendNext.getBytes().length];
			bufNext = toSendNext.getBytes();
			packetPrevious = new DatagramPacket(bufNext, bufNext.length,
					InetAddress.getByName(obj.getIp(this.prevNode)), 4448);
			socket.send(packetPrevious);
			socket.close();

		} catch (Exception e) {
			failure();
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public void failure() {

		try {
			DatagramPacket packetNext, packetPrevious;
			this.prevNode = this.rmi.getPrevious(name);
			this.nextNode = this.rmi.getNext(name);

			INodeRMI obj = (INodeRMI) Naming.lookup("//" + "192.168.1.15" + "/hash");

			DatagramSocket socket = new DatagramSocket(4448);

			String toSendPrev = "node failed, previous: " + this.prevNode;
			byte[] bufPrev = new byte[toSendPrev.getBytes().length];
			bufPrev = toSendPrev.getBytes();
			packetNext = new DatagramPacket(bufPrev, bufPrev.length, InetAddress.getByName(obj.getIp(this.nextNode)),
					4448);
			socket.send(packetNext);

			String toSendNext = "node failed, next: " + this.nextNode;
			byte[] bufNext = new byte[toSendNext.getBytes().length];
			bufNext = toSendNext.getBytes();
			packetPrevious = new DatagramPacket(bufNext, bufNext.length,
					InetAddress.getByName(obj.getIp(this.prevNode)), 4448);
			socket.send(packetPrevious);

			obj.removeNode(this.myNode);

		} catch (MalformedURLException e) {
			System.out.println("FAILURE: MALFORMED URL EXCEPTION");
			e.printStackTrace();
		} catch (RemoteException e) {
			System.out.println("FAILURE: REMOTE EXCEPTION");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("FAILURE: NOT BOUND EXCEPTION");
			e.printStackTrace();
		} catch (java.net.SocketException e) {
			System.out.println("FAILURE: SOCKET EXCEPTION");
			e.printStackTrace();
		} catch (java.io.IOException e) {
			System.out.println("FAILURE: IO EXCEPTION");
			e.printStackTrace();
		}

	}

	/**
	 */
	private void SearchMap() {
		// TODO Auto-generated method stub
		File folder = new File("c:\\Nieuwe map");
		File[] listOfFiles = folder.listFiles();
		List<File> listOfValidFiles = new ArrayList<File>();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				// this.newFile(listOfFiles[i]);
				listOfValidFiles.add(listOfFiles[i]);
			}
		}

		this.sendFiles(listOfValidFiles);
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
				// <<<<<<< HEAD
				if (this.rmi.getHash(this.rmi.getPrevIp(bestand)) == this.prevNode) {
					// send file
				}
				/*
				 * ======= DatagramPacket packetNext, packetPrevious; IWrapper
				 * obj = (IWrapper) Naming.lookup("//" + "192.168.1.15" +
				 * "/hash"); obj.removeNode(this.myNode);
				 * 
				 * try { DatagramSocket socket = new DatagramSocket(4448);
				 * 
				 * String toSendPrev = "node gone, previous: " + this.prevNode;
				 * byte[] bufPrev = new byte[toSendPrev.getBytes().length];
				 * bufPrev = toSendPrev.getBytes();
				 * 
				 * packetNext = new DatagramPacket(bufPrev, bufPrev.length,
				 * InetAddress.getByName(obj.getIp(this.nextNode)), 4448);
				 * socket.send(packetNext);
				 * 
				 * String toSendNext = "node gone, next: " + this.nextNode;
				 * byte[] bufNext = new byte[toSendNext.getBytes().length];
				 * bufNext = toSendNext.getBytes();
				 * 
				 * packetPrevious = new DatagramPacket(bufNext, bufNext.length,
				 * InetAddress.getByName(obj.getIp(this.prevNode)), 4448);
				 * socket.send(packetPrevious); } catch
				 * (java.net.SocketException e){ // ODO Auto-generated catch
				 * block e.printStackTrace(); } catch(java.io.IOException e) {
				 * // ODO Auto-generated catch block e.printStackTrace(); }
				 * 
				 * } catch (MalformedURLException e) { // ODO Auto-generated
				 * catch block e.printStackTrace(); >>>>>>>
				 * origin/feature/shutdown
				 */
			} catch (RemoteException e) {
				failure();
				e.printStackTrace();
			}
		}
		// <<<<<<< HEAD
	}

	/**
	 * Get the owner of the file.
	 * 
	 * @param file
	 *            the file that needs to be sent
	 */
	public void sendFiles(List<File> files) {
		// TODO Auto-generated method stub
		// try {
		/*
		 * =======
		 * 
		 * public void SearchMap() { // TODO Auto-generated method stub File
		 * folder = new File("your/path"); File[] listOfFiles =
		 * folder.listFiles();
		 * 
		 * for (int i = 0; i < listOfFiles.length; i++) { if
		 * (listOfFiles[i].isFile()) {
		 * 
		 * System.out.println("File " + listOfFiles[i].getName()); try {
		 * IWrapper obj = (IWrapper) Naming.lookup("//" + "192.168.1.15" +
		 * "/getWrapper"); String ipFileToRep =
		 * obj.getPrevIp(listOfFiles[i].getName());
		 * 
		 * if((obj.getHash(ipFileToRep)) == this.myNode) { ipFileToRep =
		 * obj.getIp(this.prevNode); } //int hash =
		 * this.rmi.(obj.getHash(listOfFiles[i].getName()));
		 * 
		 * } catch (MalformedURLException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } catch (RemoteException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch
		 * (NotBoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * } } }
		 * 
		 * >>>>>>> origin/feature/shutdown
		 */

		// int hash = this.rmi.(obj.getHash(listOfFiles[i].getName()));

		try {
			SendFileThread sft = new SendFileThread(files, this.rmi, this);
			sft.start();
		} catch (Exception e) {
			failure();
			e.printStackTrace();
		}

		// sft.

		// } catch (RemoteException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	
}
