package node;

import java.io.*;
import java.net.Socket;

public class TCPReceive
{
	/**
	 * The path where to save the file.
	 */
	private String filePath;
	/**
	 * The object that maintains all sockets.
	 */
	private SocketHandler sHandler;
	/**
	 * The node that will receive the file.
	 */
	private Node node;

	/**
	 * The max size of the file.
	 */
	public final static int FILE_SIZE = 6022386; // file size temporary hard
													// coded
													// should bigger than the
													// file to be downloaded

	/**
	 * The constructor method for the TCP receive.
	 * 
	 * @param node The node that will receive the file.
	 * @param sHandler The object that maintains all sockets
	 */
	public TCPReceive(Node node, SocketHandler sHandler)
	{
		this.filePath = "c:" + File.separator + "receive" + File.separator;
		//this.filePath = File.separator + "Users" + File.separator + "kevinvdm" + File.separator + "systemwhy" + File.separator;
		this.sHandler = sHandler;
		this.node = node;
	}

	/**
	 * Receive a file from the given ip
	 * 
	 * @param ip
	 *            the IP of the sender.
	 * @param name
	 *            the name of the file.
	 * @param size
	 *            the size of the file.
	 * @throws IOException Something went wrong while reading the file.
	 */
	public void receiveFile(String ip, String name, int size) throws IOException
	{
		System.out.println("receive file started.");
		//System.out.println(ip);
		try
		{
			Thread.sleep(10);
		} catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.sHandler.startReceiveTCPSocket(ip);
		// Socket socket = new Socket(ip, this.socketPort);
		try
		{
			// String name = connect(socket);
			getFile(name, size);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.sHandler.stopSendFile();
		node.is_receiving = false;
	}

	/**
	 * Create connection and receive filename.
	 * 
	 * @param socket The socket used to connect
	 * @return the data written to the socket
	 * @throws IOException something went wrong while reading the data.
	 */
	private String connect(Socket socket) throws IOException
	{
		DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		outToServer.writeBytes("listening" + '\n');
		outToServer.flush();

		return inFromServer.readLine();
	}

	/**
	 * Download the file.
	 * 
	 * @param size
	 *            The size of the file.
	 * @param name
	 *            The name of the file.
	 * @throws Exception There went something wrong while receiving the file.
	 */
	private void getFile(String name, int size) throws Exception
	{
		int bytesRead;
		int currentTot = 0;
		int filesize = 1000000;

		byte[] bytearray = new byte[filesize];
		
		//Thread.sleep(2000);
		InputStream is = this.sHandler.getReceiveTCPSocket().getInputStream();
		FileOutputStream fos = new FileOutputStream(filePath + name);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		//System.out.println("bytearray length: " + bytearray.length);
		// bytesRead = is.read(bytearray, 0, bytearray.length);
		currentTot = 0;// 0bytesRead;
		/**
		 * TODO: bigger files.
		 * 
		 * glue parts of bigger files together.
		 */
		//bytesRead = is.read(bytearray, 0, bytearray.length);
		do {
			//System.out.println("gunna read");
			bytesRead = is.read(bytearray, 0, filesize);
			//System.out.println(currentTot + "B read");
			bos.write(bytearray, 0, bytesRead);
			//System.out.println("bytesWritten");
			//;
			//Thread.sleep(100);
			if (bytesRead > 0)
				currentTot += bytesRead;
			else break;
		} while (currentTot < size);
		System.out.println("bytes: " + currentTot);
		//System.out.println(new String(bytearray));

		//bos.write(bytearray, 0, bytearray.length);

		node.addOwnerList(name);

		bos.flush();
		bos.close();
		//GUIController gui_controller = new GUIController();
		//gui_controller.refreshList(filePath);
	}

}
