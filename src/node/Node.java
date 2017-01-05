package node;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import interfaces.INodeRMI;

import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Node
{

	private int nextNode, prevNode, myNode;
	private INodeRMI rmi;
	// private DatagramSocket serverSocket;
	private String name;
        private boolean isBussy;
	private List<String> ownerList;
	private List<String> localList;
	private List<FileFiche> FicheList;
    public TreeMap<String, Boolean> SystemList;
	private boolean mapUpdate;
	private String serverIP = "192.168.1.16";
	private String folderString = "C:" + File.separator + "nieuwe map";
	private SocketHandler sHandler;
	private final int UDP_PORT = 6789;
	private final int MULTICAST_PORT = 4446;
	private final int TCP_PORT = 5555;

	public List<String> getLocalList()
	{
		return localList;
	}
    public List<String> getOwnerList()
    {
        return ownerList;
    }

    public void addFileFiche(FileFiche fiche) {FicheList.add(fiche);}

	public void addLocalList(String fileName)
	{
		localList.add(fileName);
	}

	public synchronized boolean isMapUpdate()
	{
		return this.mapUpdate;
	}

	public synchronized void setMapUpdate(boolean mapUpdate)
	{
		this.mapUpdate = mapUpdate;
	}

	/**
	 * The constructor method node.
	 *
	 * @param name
	 *            the agentname of the node.
	 */
	public Node(String name, INodeRMI rmi)
	{
		this.ownerList = new ArrayList<String>();
		this.localList = new ArrayList<String>();
		this.name = name;
		this.mapUpdate = false;
		this.sHandler = new SocketHandler(TCP_PORT, UDP_PORT, MULTICAST_PORT);

                isBussy =false;
		this.sHandler.startServerSocket();

		/*
		 * ListenToCmdThread cmd = new ListenToCmdThread(this); cmd.start();
		 */
		MulticastClient mc = new MulticastClient(this, this.sHandler);

		mc.multicastStart(name);

		this.rmi = rmi;
		this.initNodes();
		printNodes();

		new StartupThread(rmi, this, this.sHandler).start();
	}

	// Getters
	/**
	 * Get previous node.
	 *
	 * @return the previous node hash
	 */
	public int getPrev()
	{
		return this.prevNode;
	}

	public TreeMap<String, Boolean> getSystemList() { return this.SystemList; }

    public void updateSystemList(TreeMap<String, Boolean> newList) { this.SystemList = newList; }

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

	public void setNextNode(int hash)
	{
		this.nextNode = hash;
	}

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

			if(size == 1) {
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

	public void controlFiles()
	{
		// TODO Auto-generated method stub
		for (String bestand : this.localList)
		{
			try
			{
				// <<<<<<< HEAD
				if (this.rmi.getHash(this.rmi.getPrevIp(bestand)) == this.prevNode)
				{

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
			} catch (RemoteException e)
			{
				// failure();
				e.printStackTrace();
			}
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

	public void addOwnerList(String name)
	{
		this.localList.add(name);
		this.ownerList.add(name);
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
	 */
	private void initNodes()
	{
		try
		{
			this.myNode = this.rmi.getCurrent(name);
			this.prevNode = this.rmi.getPrevious(name);
			this.nextNode = this.rmi.getNext(name);
                        
                        if(this.myNode == this.prevNode && this.myNode  == this.nextNode )
                        {
                            this.isBussy = true;
                        }
                        
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
		if ((hash < this.nextNode && hash > this.myNode) || (this.myNode > this.nextNode && hash < this.nextNode)
				|| this.myNode == this.nextNode)
		{
			this.nextNode = hash;
                        sendFilesToNewNode();
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
			File f = new File(folderString + File.separator + filename);
			if (f.isFile())
			{
				System.out.println("addFile");
				files.add(f);
			}
		}

		SendFileThread sft = new SendFileThread(files, this.rmi, this, this.sHandler);
		sft.start();
		sft.join();
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
	 */
    private void sendFilesToNewNode() {
    	System.out.println("send file to new node");
        List<File> filesToSend = new ArrayList<>();
        for(String file : this.ownerList)
        {
        	System.out.println("filename: " + file);
            //try {
                //if( this.myNode  != rmi.getFileNode(this.rmi.getHash(file)))
                //{
                	System.out.println(file + " added");
                    filesToSend.add(new File(file));
                //}
                
                    
            //} catch (RemoteException ex) {
            //    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            //}
        }
        System.out.println("send files - list made");
        this.sendFiles(filesToSend);
    }

    void removeOwnerList(String name) {
        this.ownerList.remove(name);
    }

    boolean getBusyState() {
    	return this.isBussy;
    }

    void setBussy(boolean b) {
        this.isBussy = b;
    }

	public boolean checkBusyState()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
