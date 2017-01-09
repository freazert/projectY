package node;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import gui.GUI;
import interfaces.INodeRMI;

import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Node implements Serializable
{

	/**
	 * serializable version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The nodes known to the current node.
	 */
	private int nextNode, prevNode, myNode;
	/**
	 * The remote method invocation object.
	 */
	private INodeRMI rmi;
	/**
	 * The name of the node.
	 */
	private String name;
	/**
	 * The state of the node.
	 */
	private boolean isBusy;
	/**
	 * A list of files of which the current node is the owner.
	 */
	private List<String> ownerList;
	/**
	 * A list of files that the current node has locally.
	 */
	private List<String> localList;
	/**
	 * A list of file fiches of all the files in in systemY known to the current
	 * node
	 */
	private List<FileFiche> FicheList;
	/**
	 * A Treemap representation of all the files in systemY known to the current
	 * node, with the locked state of each file.
	 */
	public TreeMap<String, Boolean> SystemList;
	/**
	 * The state of the system.
	 */
	private boolean isMapUpdating;
	/**
	 * A string representation of the ip address of the server.
	 */
	private String serverIP = "192.168.1.16";
	/**
	 * The folder to check for new files.
	 */
	private String folderString = "C:" + File.separator + "nieuwe map";
	// private String folderString = File.separator + "Users" + File.separator +
	// "kevinvdm" + File.separator + "systemwhy" + File.separator;
	/**
	 * The folder that holds the downloaded files.
	 */
	private String receiveString = "C:" + File.separator + "receive";
	// private String receiveString = File.separator + "Users" + File.separator
	// + "kevinvdm" + File.separator + "systemwhy" + File.separator;

	/**
	 * The object that maintains all the objects.
	 */
	private SocketHandler sHandler;
	/**
	 * The port number for UDP communication.
	 */
	private final int UDP_PORT = 6789;
	/**
	 * The port number for multicast communication.
	 */
	private final int MULTICAST_PORT = 4446;
	/**
	 * The port number for TCP communication.
	 */
	private final int TCP_PORT = 5555;
	/**
	 * the current receiving state.
	 */
	public boolean isReceiving = false;
	/**
	 * the current shutdown state.
	 */
	public boolean isShuttingDown = false;
	/**
	 * The object that maintains the agents
	 */
	private AgentStarter agentStarter;
	/**
	 * The file agent.
	 */
	private FileListAgent fileAgent;
	/**
	 * A list of all the files in the system.
	 */
	private FileList fileList;

	/**
	 * The constructor method node.
	 *
	 * @param name
	 *            the agentname of the node.
	 * @param rmi
	 *            the remote method invocation object to converse with the
	 *            server.
	 */
	public Node(String name, INodeRMI rmi)
	{
		try
		{
			this.ownerList = new ArrayList<String>();
			this.localList = new ArrayList<String>();
			this.fileList = new FileList();
			this.name = name;

			this.sHandler = new SocketHandler(TCP_PORT, UDP_PORT, MULTICAST_PORT);
			this.isMapUpdating = false;
			isBusy = false;
			this.sHandler.startServerSocket();

			MulticastClient mc = new MulticastClient(this, this.sHandler);

			mc.multicastStart(name);

			this.rmi = rmi;

			this.initNodes(name);
			this.agentStarter = new AgentStarter(this, this.rmi);
			Registry registry = LocateRegistry.createRegistry(1099);
			try
			{
				registry.bind("AgentStarter", agentStarter);
				System.out.println("agent rmi startup");
			} catch (AlreadyBoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// het probleem is dat op dit moment de localList en ownerList van
			// de node nog niet gevuld zijn
			this.fileAgent = new FileListAgent(fileList);
			// if(this.myNode == this.nextNode && this.myNode == this.prevNode)
			// {
			rmi.setbusy(this.getCurrent(), false);

			// } else {
			// this.mapUpdate = false;
			// rmi.setbusy(this.getCurrent(), true);
			// }
			printNodes();

			GUI gui = new GUI(fileList);

			new StartupThread(rmi, this, this.sHandler).start();
		} catch (RemoteException ex)
		{
			Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// Getters

	/**
	 * Get the list of files that this node has locally
	 * 
	 * @return the local list.
	 */
	public List<String> getLocalList()
	{
		return localList;
	}

	/**
	 * Get the list of which this node is the owner.
	 * 
	 * @return the owner list.
	 */
	public List<String> getOwnerList()
	{
		return ownerList;
	}

	/**
	 * The state of updating the file lists.
	 * 
	 * @return The state of updating the file list of this node.
	 */
	public synchronized boolean isMapUpdating()
	{
		return this.isMapUpdating;
	}

	/**
	 * Get previous node.
	 *
	 * @return the previous node hash
	 */
	public int getPrev()
	{
		return this.prevNode;
	}

	/**
	 * Get the list of files on the system.
	 * 
	 * @return the system list.
	 */
	public TreeMap<String, Boolean> getSystemList()
	{
		return this.SystemList;
	}

	/**
	 * Get the folder where is checked for new files.
	 * 
	 * @return the folder in a string representation.
	 */
	public String getFolderString()
	{
		return this.folderString;
	}

	/**
	 * Get next node.
	 *
	 * @return the next node hash
	 */
	public int getNext()
	{
		return this.nextNode;
	}

	/**
	 * get own node.
	 *
	 * @return the local node hash
	 */
	public int getCurrent()
	{
		return this.myNode;
	}

	// Setters
	/**
	 * Update the list of all files in the system.
	 * 
	 * @param newList
	 *            the list to merge with.
	 */
	public void updateSystemList(TreeMap<String, Boolean> newList)
	{
		this.SystemList = newList;
	}

	/**
	 * Add a file fiche to the fiche list.
	 * 
	 * @param fiche
	 *            the file ficheof the new file.
	 */
	public void addFileFiche(FileFiche fiche)
	{
		FicheList.add(fiche);
		System.out.println(FicheList);
	}

	/**
	 * Add filename to the local list.
	 * 
	 * @param fileName
	 *            the filename of the new file.
	 */
	public void addLocalList(String fileName)
	{
		localList.add(fileName);
	}

	/**
	 * Set if the map is updating or not.
	 * 
	 * @param mapUpdate
	 *            the state.
	 */
	public synchronized void setMapUpdating(boolean mapUpdate)
	{
		this.isMapUpdating = mapUpdate;
	}

	/**
	 * set previous and next node when a new connection is made, using the name
	 * of the new node.
	 *
	 * @param name
	 *            the name of the new node
	 */
	public void setNodes(String name)
	{
		try
		{
			int hash = this.rmi.getCurrent(name);
			System.out.println(name);

			setPrev(hash);
			setNext(hash);

		} catch (RemoteException e)
		{
			// failure();
			e.printStackTrace();
		}

		printNodes();
	}

	/**
	 * Set the new next node.
	 * 
	 * @param hash
	 *            the hashed value of the next node name.
	 */
	public void setNextNode(int hash)
	{
		this.nextNode = hash;
	}

	/**
	 * Set the new previous node.
	 * 
	 * @param hash
	 *            the hashed value of the previous node name.
	 */
	public void setPrevNode(int hash)
	{
		this.prevNode = hash;
	}

	/*
	 * public functions.
	 */
	/**
	 * Start the shutdown
	 */
	public void shutdown()
	{
		try
		{
			System.out.println("shutting down");

			rmi.removeNode(this.myNode);
			this.isShuttingDown = true;
			if (rmi.getHmapSize() > 0)
			{

				DatagramSocket socket = new DatagramSocket(4448);
				String toSendPrev = createJSONObject("previous", this.prevNode);
				sendUDP(socket, rmi.getIp(this.nextNode), toSendPrev);

				String toSendNext = createJSONObject("next", this.nextNode);
				sendUDP(socket, rmi.getIp(this.prevNode), toSendNext);

				shutdownReplicatedFiles();
				// shutdownLocalFiles();

				socket.close();
			}

		} catch (Exception e)
		{
			// failure();
			e.printStackTrace();
		} finally
		{
			System.exit(0);
		}
	}

	/**
	 * Next node fails. notify server and next + previous node of this change.
	 * reinitialize own previous and next nodes.
	 * 
	 * @param failingNode
	 *            the hashed value of the node name that is failing.
	 */
	public void failure(int failingNode)
	{
		try
		{
			System.out.println("failingnode: " + failingNode);
			this.rmi.removeNode(this.nextNode);
			int size = this.rmi.getHmapSize();
			int nextNode = this.rmi.getNext(failingNode);
			int prevNode = this.rmi.getPrevious(failingNode);
			System.out.println("nextnode: " + nextNode);
			System.out.println("prevnode: " + prevNode);

			DatagramSocket socket = this.sHandler.getUdpSocket();

			if (size == 1)
			{
				this.nextNode = this.myNode;
				this.prevNode = this.myNode;
			}
			String prev = createJSONObject("previous", prevNode);
			if (nextNode >= 0)
				sendUDP(socket, this.rmi.getIp(nextNode), prev);

			String next = createJSONObject("next", nextNode);
			if (prevNode >= 0)
				sendUDP(socket, this.rmi.getIp(prevNode), next);

			// this.sHandler.startMulticastReceive();

		} catch (MalformedURLException e)
		{
			System.out.println("FAILURE: MALFORMED URL EXCEPTION");
			e.printStackTrace();
		} catch (RemoteException e)
		{
			System.out.println("FAILURE: REMOTE EXCEPTION");
			e.printStackTrace();
		} catch (java.net.SocketException e)
		{
			System.out.println("FAILURE: SOCKET EXCEPTION");
			e.printStackTrace();
		} catch (java.io.IOException e)
		{
			System.out.println("FAILURE: IO EXCEPTION");
			e.printStackTrace();
		} catch (JSONException e)
		{ // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Send list of files.
	 *
	 * @param files
	 *            the files that needs to be sent
	 */
	public void sendFiles(List<File> files)
	{
		System.out.println("node: send files");
		SendFileThread sft = new SendFileThread(files, this.rmi, this, this.sHandler);
		sft.start();
	}

	/**
	 * Add new file to both the owner and the local list.
	 * 
	 * @param name
	 *            The name of the file.
	 */
	public void addOwnerList(String name)
	{
		this.localList.add(name);
		this.ownerList.add(name);
		setFileList();
	}

	/**
	 * Set the file list
	 */
	public void setFileList()
	{
		this.fileList.setFileList(this);
	}

	/**
	 * Remove a file of which this node is the owner, both on the ownerlist as
	 * on the disk.
	 *
	 * @param fileName
	 *            the name of the file that has to be removed.
	 */
	public void removeFile(String fileName)
	{
		int fileIndex = this.ownerList.indexOf(fileName);
		if (fileIndex >= 0)
		{
			File file = new File(this.folderString + File.separator + fileName);
			file.delete();
			this.ownerList.remove(fileIndex);
		}
	}

	/*
	 * internal private functions
	 */
	/**
	 * create a JSON string int the form of { type: "[type]", data: [object] }.
	 * this way certain data will be easy to send over any connection to another
	 * node.
	 *
	 * @param type
	 *            A short string representation of what the receiver has to do
	 *            with the file.
	 * @param data
	 *            The Object that has to be sent over
	 * @return a string representation of all the data that has to be sent.
	 * @throws JSONException
	 *             Something went wrong while creating the JSON object.
	 */
	private String createJSONObject(String type, Object data) throws JSONException
	{
		JSONObject jobj = new JSONObject();
		jobj.put("type", type);
		jobj.put("data", data);

		return jobj.toString();
	}

	/**
	 * Initialize nodes on startup.
	 * 
	 * @param name
	 *            the name of the node
	 */
	private void initNodes(String name)
	{
		try
		{
			this.myNode = this.rmi.getCurrent(name);
			this.prevNode = this.rmi.getPrevious(name);
			this.nextNode = this.rmi.getNext(name);

			/*
			 * if (this.myNode == this.prevNode && this.myNode == this.nextNode)
			 * { this.isBussy = true; AgentStarter runAgent = new
			 * AgentStarter(this, this.rmi); FileListAgent fileAgent = new
			 * FileListAgent(this);
			 * 
			 * runAgent.startFileAgent(fileAgent); }
			 */

		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Set new previous node.
	 *
	 * @param hash
	 *            the hash of the new node
	 */
	private void setPrev(int hash)
	{
		if ((hash > this.prevNode && hash < this.myNode) || (this.myNode < this.prevNode && hash > this.prevNode)
				|| this.prevNode == this.myNode)
		{
			this.prevNode = hash;
		}

	}

	/**
	 * Set new next node.
	 *
	 * @param hash
	 *            the hash of the new node
	 */
	private void setNext(int hash)
	{
		int oldNode = this.nextNode;
		if ((hash < this.nextNode && hash > this.myNode) || (this.myNode > this.nextNode && hash < this.nextNode)
				|| this.myNode == this.nextNode)
		{
			this.nextNode = hash;
			sendFilesToNewNode();
			if (oldNode == this.myNode)
			{
				try
				{
					this.agentStarter.startFileAgent(this.fileAgent);
				} catch (RemoteException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Send the replicated files to the new owner on shutdown. wait until this
	 * is done before exiting the process.
	 *
	 * @throws InterruptedException
	 *             there went something wrong in the send file thread. the
	 *             thread was stopped preemptively.
	 */
	private void shutdownReplicatedFiles() throws InterruptedException
	{
		List<File> files = new ArrayList<File>();
		for (String filename : this.ownerList)
		{
			System.out.println(filename);
			File f = new File(this.receiveString + File.separator + filename);
			if (f.isFile())
			{
				System.out.println("addFile");
				files.add(f);
			}
		}

		SendFileThread sft = new SendFileThread(files, this.rmi, this, this.sHandler);
		sft.start();
		sft.join();
		System.out.println("shutdown sent complete");
	}

	/**
	 * Tell the owner of your local files that you will shutdown. the owner will
	 * then remove the file if it hasn't been downloaded, otherwise it will
	 * change the download location.
	 */
	private void shutdownLocalFiles()
	{
		for (String file : this.localList)
		{
			try
			{
				String ip = this.rmi.getFileIp(file);
				DatagramSocket socket = new DatagramSocket(4448);
				String json = createJSONObject("remove", file);

				sendUDP(socket, ip, json);

			} catch (RemoteException | SocketException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Send UDP message.
	 *
	 * @param socket
	 *            the socket over which the file has to be sent
	 * @param ip
	 *            the ip of the node that needs to be sent to
	 * @param data
	 *            the data converted to a JSON string that will be sent to the
	 *            node.
	 * @throws IOException
	 *             Something went wrong while writing to the UDP socket
	 */
	private void sendUDP(DatagramSocket socket, String ip, String data) throws IOException
	{
		DatagramPacket packet;
		byte[] buf = new byte[data.getBytes().length];

		buf = data.getBytes();
		packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), 4448);
		socket.send(packet);
	}

	/**
	 * Print out the current nodes.
	 */
	public void printNodes()
	{
		System.out.println("my node: " + this.myNode);
		System.out.println("next node: " + this.nextNode);
		System.out.println("previous node: " + this.prevNode);
	}

	/**
	 * TODO: schrap files die verstuurd zijn.
	 * 
	 * Start sending files to newly arrived node.
	 */
	private void sendFilesToNewNode()
	{
		System.out.println("send file to new node");
		List<File> filesToSend = new ArrayList<>();
		for (String file : this.ownerList)
		{
			System.out.println("filename: " + file);
			// try {
			// if( this.myNode != rmi.getFileNode(this.rmi.getHash(file)))
			// {
			System.out.println(file + " added");
			filesToSend.add(new File("C:\\nieuwe map\\" + file));
			// filesToSend.add(new File(File.separator + "Users" +
			// File.separator + "kevinvdm" + File.separator + "systemwhy" +
			// file));
			// }

			// } catch (RemoteException ex) {
			// Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null,
			// ex);
			// }
		}
		System.out.println("send files - list made");
		try
		{
			while (this.rmi.getBusyState(this.nextNode))
			{
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.sendFiles(filesToSend);
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Remove file from the owner list.
	 * 
	 * @param name
	 *            The name of the file.
	 */
	void removeOwnerList(String name)
	{
		this.ownerList.remove(name);
	}

	/**
	 * Get the state of the node
	 * 
	 * @return the busy state.
	 */
	boolean getBusyState()
	{
		return this.isBusy;
	}

	/**
	 * Set the busy state
	 * 
	 * @param isBusy
	 *            the new busy state of the node.
	 */
	void setBusy(boolean isBusy)
	{
		this.isBusy = isBusy;
	}
}
