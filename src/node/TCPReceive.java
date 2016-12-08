package node;

import java.io.*;
import java.net.Socket;

public class TCPReceive {
	private String filePath;
	private int socketPort;
	private Node node;

	public final static int FILE_SIZE = 6022386; // file size temporary hard
													// coded
													// should bigger than the
													// file to be downloaded

	public TCPReceive(Node node, int socketPort) {
		this.filePath = "D:" + File.separator + "school"+ File.separator + "SCH-IW_EI" + File.separator + "shared" + File.separator + "receive" + File.separator;
		this.socketPort = socketPort;
		this.node = node;
	}

	/**
	 * Receive a file from the given ip over the given socket
	 * 
	 * @param ip
	 *            the IP of the TCP sender.
	 * @throws Exception
	 */
	public void receiveFile(String ip, String name, int size) throws IOException {
		System.out.println("receive file started.");
		Socket socket = new Socket(ip, this.socketPort);
		try {
			//String name = connect(socket);
			getFile(socket, name, size);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		socket.close();
	}

	
	/**
	 * Create connection and receive filename.
	 * 
	 * @param socket
	 * @return
	 * @throws Exception
	 */
	private String connect(Socket socket) throws Exception {
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
	 * @param name
	 * @throws Exception
	 */
	private void getFile(Socket socket, String name, int size) throws Exception {
		int bytesRead;
		int currentTot = 0;
		int filesize = size + 1;

		
		byte[] bytearray = new byte[filesize];
		InputStream is = socket.getInputStream();
		FileOutputStream fos = new FileOutputStream(filePath + name);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		System.out.println("bytearray length: " + bytearray.length);
		bytesRead = is.read(bytearray, 0, bytearray.length);
		currentTot = bytesRead;
		do {
			bytesRead = is.read(bytearray, currentTot, (bytearray.length - currentTot));
			if (bytesRead >= 0)
				currentTot += bytesRead;
		} while (bytesRead > -1);
		System.out.println("bytes: " + currentTot);
		System.out.println(new String(bytearray));

		bos.write(bytearray, 0, bytearray.length - 1);
		
		node.addOwnerList(name);
		
		bos.flush();
		bos.close();
	}

}
