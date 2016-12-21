package node;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SocketHandler {
	private DatagramSocket udpSocket;
	private MulticastSocket multiSocket;
	private DatagramSocket multicastReceiveSocket;
	private ServerSocket serverSocket;
	private Socket receiveTCPSocket;
	
	private int tcpPort, udpPort, multicastPort;
	
	//private multicastAddress

	public SocketHandler(int tcpPort, int udpPort, int multicastPort) {
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		this.multicastPort = multicastPort;
	}

	//getters
	
	public DatagramSocket getUdpSocket() {
		return udpSocket;
	}

	public MulticastSocket getMultiSocket() {
		return multiSocket;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	public DatagramSocket getMulticastReceiveSocket() {
		return this.multicastReceiveSocket;
	}
	
	//startup
	
	public Socket getReceiveTCPSocket() {
		return receiveTCPSocket;
	}

	public void startUdpSocket() {
		try {
			this.udpSocket = new DatagramSocket(this.udpPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.tcpPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startMulticastSocket() {
		try {
			this.multiSocket = new MulticastSocket(this.multicastPort);
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startMulticastReceiveSocket() {
		try {
			this.multicastReceiveSocket = new DatagramSocket(this.multicastPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startReceiveTCPSocket(String ip) {
		try {
			this.receiveTCPSocket = new Socket(ip, this.tcpPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//close
	
	public void closeUdpSocket() {
		this.udpSocket.close();
	}
	
	public void closeServerSocket()
	{
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeMulticastSocket() {
		this.multiSocket.close();
	}
	
	public void closeMulticastReceiveSocket() {
		this.multicastReceiveSocket.close();
	}
	
	public void closeReceiveTCPSocket() {
		try {
			this.receiveTCPSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startSending() {
		
		
	}

	public void startReceiving() {
		// TODO Auto-generated method stub
		
	}

	public void startMulticastSend() {
		// TODO Auto-generated method stub
		
	}

	public void startMulticastReceive() {
		// TODO Auto-generated method stub
		
	}

	public void startSendFile() {
		// TODO Auto-generated method stub
		
	}

	public void stopSendFile() {
		// TODO Auto-generated method stub
		
	}
	
}
