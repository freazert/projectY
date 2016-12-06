package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ReceiveFileThread extends Thread {

	public ReceiveFileThread() {

	}

	@Override
	public void run() {
		DatagramSocket serverSocket;
		try {
			serverSocket = new DatagramSocket(6789);
			while (true) {
				System.out.println("rft run");
				receiveUDP(serverSocket);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} // catch (IOException e) {
			//e.printStackTrace();
		//}

	}

	private void receiveUDP(DatagramSocket socket) {
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		System.out.println("rft receiveUDP");
		
		try {
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		System.out.println("waiting for udp receive");
		socket.receive(receivePacket);
		System.out.println("UDP received");
		String sentence = new String(receivePacket.getData());
		System.out.println("RECEIVED: " + sentence);
		InetAddress IPAddress = receivePacket.getAddress();

		TCPReceive receive = new TCPReceive(5555);
		receive.receiveFile(IPAddress.getHostAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * int port = receivePacket.getPort(); String capitalizedSentence =
		 * sentence.toUpperCase(); sendData = capitalizedSentence.getBytes();
		 * DatagramPacket sendPacket = new DatagramPacket(sendData,
		 * sendData.length, IPAddress, port); serverSocket.send(sendPacket);
		 */
	}

}
