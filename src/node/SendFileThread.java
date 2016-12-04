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

	public SendFileThread(String ip, File file) {
		this.ip = ip;
		this.file = file;
	}

	@Override
	public void run() {
		try {

			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(this.ip);
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];

			JSONObject jobj = new JSONObject();
			jobj.put("type", "send");
			jobj.put("name", "lel");

			System.out.println(jobj);

			sendData = jobj.toString().getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 6789);
			clientSocket.send(sendPacket);
			/*
			 * DatagramPacket receivePacket = new DatagramPacket(receiveData,
			 * receiveData.length); clientSocket.receive(receivePacket); String
			 * modifiedSentence = new String(receivePacket.getData());
			 * System.out.println("FROM SERVER:" + modifiedSentence);
			 */
			TCPSend sendFile = new TCPSend(5555);
			sendFile.sendFile(this.file.getName());
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
