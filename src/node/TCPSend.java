package node;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Send a file over TCP
 */
public class TCPSend
{
	/**
	 * The path to the file that has to be sent.
	 */
	private String filePath;

	/**
	 * The object that performs and maintains all socket actions.
	 */
	private SocketHandler sHandler;

	/**
	 * The constructor method for TCPSend
	 *
	 * @param sHandler
	 *            The object that performs and maintains all socket actions.
	 */
	TCPSend(SocketHandler sHandler)
	{
		// this.connectedSocket = new Socket(6789);
		this.sHandler = sHandler;
		this.filePath = "C:" + File.separator + "nieuwe map" + File.separator;
		//System.out.println("creating");

	}

	/**
	 * send a file over the TCP connection
	 *
	 * @param fileName
	 *            the name of the file that needs to be sent.
	 * @throws IOException
	 */
	public void send(File file) throws IOException
	{
		String fullName = file.getAbsolutePath();
		System.out.print(file.getName());
		DataOutputStream outToClient;
		Socket socket;

		System.out.print("\nwaiting for accept...\n");
                this.sHandler.startServerSocket();
		ServerSocket sSocket = this.sHandler.getServerSocket();
		sSocket.setSoTimeout(60000);
		socket = sSocket.accept();
		/**
		 * TODO: add functionalities to sockethandler.
		 */

		byte[] b = new byte[250];

		InputStream is = socket.getInputStream();

		File transferFile = file;//new File(fullName);
		System.out.println("Sending: " + fullName);

		if (transferFile.exists())
		{
			outToClient = new DataOutputStream(socket.getOutputStream());
			// sendName(fileName, outToClient);
			sendFile(transferFile, outToClient);
		} else
		{
			System.out.println("file not found");
			outToClient = new DataOutputStream(socket.getOutputStream());
			outToClient.writeBytes("File doesn't exist!");
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
	 *             there was a problem while writing something to the other
	 *             node.
	 */
	private void sendName(String name, DataOutputStream outToClient) throws IOException
	{
		outToClient.writeBytes(name + "\n");
		outToClient.flush();
	}

	/**
	 * Send the file over the TCP connection.
	 *
	 * @param file
	
        *            The file that needs to be sent.
	 * @param outToClient
	 *            The outputstream to the receiver.
	 * @throws IOException
	 *             There was a problem while writing something to the other
	 *             node.
	 */
	private void sendFile(File file, DataOutputStream outToClient) throws IOException
	{
		byte[] bytearray = new byte[(int) file.length()];
		FileInputStream fin = new FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fin);
		bin.read(bytearray, 0, bytearray.length);
		/// OutputStream os = socket.getOutputStream();

		// send file
		//System.out.println("Sending Files...");
		outToClient.write(bytearray, 0, bytearray.length);
		outToClient.flush();
		fin.close();
		bin.close();

		System.out.println("File transfer complete");
	}
}
