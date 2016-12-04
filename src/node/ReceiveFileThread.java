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
	public void run()  {
		byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
		DatagramSocket serverSocket;
		try {
			serverSocket = new DatagramSocket(9876);
			while(true)
	           {
	              DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	              serverSocket.receive(receivePacket);
	              String sentence = new String( receivePacket.getData());
	              System.out.println("RECEIVED: " + sentence);
	              InetAddress IPAddress = receivePacket.getAddress();
	              
	              TCPReceive receive = new TCPReceive(5555);
	              receive.receiveFile(IPAddress.getHostAddress());
	              /*int port = receivePacket.getPort();
	              String capitalizedSentence = sentence.toUpperCase();
	              sendData = capitalizedSentence.getBytes();
	              DatagramPacket sendPacket =
	              new DatagramPacket(sendData, sendData.length, IPAddress, port);
	              serverSocket.send(sendPacket);*/
	           }
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
	}

}
