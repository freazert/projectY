package node;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import interfaces.INodeRMI;

public class SendFileThread extends Thread {

	private List<File> files;
	private INodeRMI rmi;
	private Node node;
	SocketHandler sHandler;

	/**
	 * The constructor method for the SendFileThread.
	 *
	 * @param files
	 *            List of files to be sent
	 * @param rmi
	 *            the remote method invocation object used to communicate with
	 *            the nameServer.
	 * @param node
	 *            the node that sends the files.
	 */
	public SendFileThread(List<File> files, INodeRMI rmi, Node node, SocketHandler sHandler) {

		this.sHandler = sHandler;

		this.files = files;
		this.rmi = rmi;
		this.node = node;
	}

	@Override
	public void run() {
		System.out.println("send list of files");
		try {
			synchronized (this.node) {
				node.setMapUpdate(true);
				sHandler.startSending();
				for (File file : files) {

					InetAddress IPAddress = InetAddress.getByName(getIP(file.getName()));

					String jsonString = createJsonString(file.getName(), file.length());
					System.out.print(jsonString);
					sendUdp(jsonString, IPAddress);

					// receive
					TCPSend sendFile = new TCPSend(this.sHandler);
					sendFile.send(file.getName());

				}
				// node.openServerSocket();

				sHandler.startReceiving();
				node.setMapUpdate(false);
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

		int hash = rmi.getHash(name);
		if (ip.equals(rmi.getIp(this.node.getCurrent()))) {
			ip = rmi.getIp(this.node.getPrev());
		}

		return ip;
	}

	/**
	 * Create the string that needs to be sent to the other node.
	 *
	 * @return the string for the user, on failure returns an empty string.
	 */
	private String createJsonString(String name, long size) {
		try {
			JSONObject jobj = new JSONObject();
			jobj.put("type", "file");
			jobj.put("data", name);
			jobj.put("size", size);

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
