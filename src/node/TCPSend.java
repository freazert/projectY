package node;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSend {

	private ServerSocket welcomeSocket;
	private String filePath;

	/**
	 * The constructor method for TCPSend
	 * 
	 * @param socketPort the port over which the TCP connection will be made.
	 */
	TCPSend(ServerSocket welcomeSocket) {
		// this.connectedSocket = new Socket(6789);
		this.filePath = "C:" + File.separator + "nieuwe map" + File.separator;
		System.out.println("creating");
			this.welcomeSocket = welcomeSocket;
		}

	/**
	 * The constructor method for TCPSend
	 * 
	 * @param socketPort The port over which the TCP connection is made.
	 * @param filePath The path of the directory where the files are stored.
	 */
	TCPSend(int socketPort, String filePath) {
		this.filePath = filePath;
		try {
			this.welcomeSocket = new ServerSocket(socketPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * send a file over the TCP connection
	 * 
	 * @param fileName
	 *            the name of the file that needs to be sent.
	 */
	public void send(String fileName) {
		String fullName = this.filePath + fileName;
		System.out.print(fullName);
		DataOutputStream outToClient;
		Socket socket;
		try {
			System.out.println("waiting for accept");
			socket = this.welcomeSocket.accept();
			
			byte[] b = new byte[250];
			
			InputStream is = socket.getInputStream();
			
			File transferFile = new File(fullName);
			System.out.println("Received: " + fullName);

			if (transferFile.exists()) {
				outToClient = new DataOutputStream(socket.getOutputStream());
				//sendName(fileName, outToClient);	
				sendFile(transferFile, outToClient);
			} else {
				System.out.println("file not found");
				outToClient = new DataOutputStream(socket.getOutputStream());
				outToClient.writeBytes("File doesn't exist!");
			}

			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Send the name of the file to the receiver.
	 * 
	 * @param name
	 *            The name of the file.
	 * @param outToClient
	 *            The output stream to the receiver.
	 * @throws IOException
	 */
	private void sendName(String name, DataOutputStream outToClient) throws IOException {
		outToClient.writeBytes(name + "\n");
		outToClient.flush();
	}

	/**
	 * Send the file over the TCP connection.
	 * 
	 * @param file The file that needs to be sent.
	 * @param outToClient The outputstream to the receiver.
	 * @throws IOException
	 */
	private void sendFile(File file, DataOutputStream outToClient) throws IOException {
		byte[] bytearray = new byte[(int) file.length()];
		FileInputStream fin = new FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fin);
		bin.read(bytearray, 0, bytearray.length);
		/// OutputStream os = socket.getOutputStream();

		// send file
		System.out.println("Sending Files...");
		outToClient.write(bytearray, 0, bytearray.length);
		outToClient.flush();
		bin.close();

		System.out.println("File transfer complete");
	}
}