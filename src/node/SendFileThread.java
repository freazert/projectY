package node;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import interfaces.INodeRMI;

public class SendFileThread extends Thread {
	private String ip;
	private List<File> files;
	private INodeRMI rmi;
	private Node node;

	/**
	 * The constructor method for the SendFileThread?
	 * 
	 * @param ip
	 *            ip of the node to send to.
	 * @param file
	 *            the file that needs to be sent.
	 */
	public SendFileThread(List<File> files, INodeRMI rmi, Node node) {
		this.ip = ip;
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

				//receive
				TCPSend sendFile = new TCPSend(5555);
				sendFile.sendFile(file.getName());
				
				ReceiveFileThread rft = new ReceiveFileThread();
				rft.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getIP(String name) {
		try {
			String ip = rmi.getPrevIp(name);

			if ((rmi.getHash(ip)) == this.node.getCurrent()) {
				ip = rmi.getIp(this.node.getPrev());
			}

			return ip;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private String createJsonString() {
		try {
			JSONObject jobj = new JSONObject();
			jobj.put("type", "send");
			jobj.put("name", "lel");

			return jobj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

	private void sendUdp(String data, InetAddress ip) {
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];

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
