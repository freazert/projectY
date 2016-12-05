package node;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.json.JSONException;
import org.json.JSONObject;

public class SendFileThread extends Thread {
	private String ip;
	private File file;

	/**
	 * The constructor method for the SendFileThread?
	 * 
	 * @param ip
	 *            ip of the node to send to.
	 * @param file
	 *            the file that needs to be sent.
	 */
	public SendFileThread(String ip, File file) {
		this.ip = ip;
		this.file = file;
	}

	@Override
	public void run() {
		try {
			InetAddress IPAddress = InetAddress.getByName(this.ip);

			String jsonString = createJsonString();
			sendUdp(jsonString, IPAddress);
			
			TCPSend sendFile = new TCPSend(5555);
			sendFile.sendFile(this.file.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
