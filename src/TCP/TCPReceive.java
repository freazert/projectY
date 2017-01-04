package TCP;

import java.io.*;
import java.net.Socket;

public class TCPReceive
{
	private String filePath;
	private int socketPort;
	// private Node node;

	public final static int FILE_SIZE = 6022386; // file size temporary hard
													// coded
													// should bigger than the
													// file to be downloaded

	public TCPReceive(int socketPort)
	{
		this.filePath = "C:" + File.separator + "receive" + File.separator;
		this.socketPort = socketPort;
		// this.node = node;
	}

	/**
	 * Receive a file from the given ip over the given socket
	 * 
	 * @param ip
	 *            the IP of the TCP sender.
	 * @throws Exception An exception occured while receiving the file.
	 */
	public void receiveFile(String ip, String name, int size) throws Exception
	{
		System.out.println("receive file started.");

		Socket socket = new Socket(ip, this.socketPort);

		getFile(socket, name, size);

		socket.close();
	}

	/**
	 * Create connection and receive filename.
	 * 
	 * @param socket
	 *            The socket through which the data comes
	 * @return The line read from the server
	 * @throws IOException
	 *             Something went wrong wile writing to the bus.
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
	 * @param socket
	 *            The socket used to download over.
	 * @param name
	 *            the name of the file.
	 * @param size
	 *             the size of the file.
	 * @throws IOException Something went wrong with the connection to the other node.
	 */
	private void getFile(Socket socket, String name, int size) throws IOException
	{
		int bytesRead;
		int currentTot = 0;
		int filesize = size + 1;

		byte[] bytearray = new byte[filesize];
		InputStream is = socket.getInputStream();
		FileOutputStream fos = new FileOutputStream(filePath + name);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		System.out.println("bytearray length: " + bytearray.length);
		// bytesRead = is.read(bytearray, 0, bytearray.length);
		currentTot = 0;// 0bytesRead;
		do
		{
			bytesRead = is.read(bytearray, currentTot, (filesize - currentTot));
			if (bytesRead >= 0)
				currentTot += bytesRead;
		} while (bytesRead > -1);
		System.out.println("bytes: " + currentTot);
		System.out.println(new String(bytearray));

		bos.write(bytearray, 0, bytearray.length - 1);

		// node.addOwnerList(name);

		bos.flush();
		bos.close();
	}

}