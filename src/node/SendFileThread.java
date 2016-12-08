package node;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import interfaces.INodeRMI;

public class SendFileThread extends Thread {
	private List<File> files;
	private INodeRMI rmi;
	private Node node;

	/**
	 * The constructor method for the SendFileThread.
	 * 
	 * @param files
	 *            List of files to be sent
	 * @param rmi
	 *            the remote method invocation object used to communicate with the nameServer.
	 * @param node
	 *            the node that sends the files.
	 */
	public SendFileThread(List<File> files, INodeRMI rmi, Node node) {
		this.files = files;
		this.rmi = rmi;
		this.node = node;
	}

	@Override
	public void run() {
		try {
			for (File file : files) {
				InetAddress IPAddress = InetAddress.getByName(getIP(file.getName()));

				String jsonString = createJsonString();
				sendUdp(jsonString, IPAddress);

				// receive
				TCPSend sendFile = new TCPSend(5555);
				sendFile.send(file.getName());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send server a message with the name of a file, requesting the ip of the
	 * node that needs to be sent to. return null if
	 * 
	 * @param name
	 *            the name of file
	 * @return the ip of the client node.
	 */
	private String getIP(String name) throws RemoteException {
		String ip = rmi.getPrevIp(name);

		if ((rmi.getHash(ip)) == this.node.getCurrent()) {
			ip = rmi.getIp(this.node.getPrev());
		}

		return ip;
	}

	/**
	 * Create the string that needs to be sent to the other node.
	 * 
	 * @return the string for the user, on failure returns an empty string.
	 */
	private String createJsonString() {
		try {
			JSONObject jobj = new JSONObject();
			jobj.put("type", "file");
			jobj.put("name", "lel");

			return jobj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * send UDP message to node that receives file.
	 * 
	 * @param data
	 *            the data that needs to be sent.
	 * @param ip
	 *            the IP address of the receiving node.
	 */
	private void sendUdp(String data, InetAddress ip) {
		byte[] sendData = new byte[1024];

		try {
			DatagramSocket clientSocket = new DatagramSocket();

			sendData = data.toString().getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, 6789);

			clientSocket.send(sendPacket);

			/*
			 * DatagramPacket receivePacket = new DatagramPacket(receiveData,
			 * receiveData.length); clientSocket.receive(receivePacket); String
			 * modifiedSentence = new String(receivePacket.getData());
			 * System.out.println("FROM SERVER:" + modifiedSentence);
			 */

			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
