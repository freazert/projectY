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
	private DatagramSocket multicastSendSocket;
	private ServerSocket serverSocket;
	private Socket receiveTCPSocket;

	private int tcpPort, udpPort, multicastPort;

	// private multicastAddress

	public SocketHandler(int tcpPort, int udpPort, int multicastPort) {
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		this.multicastPort = multicastPort;

		try {
			this.udpSocket = new DatagramSocket(this.udpPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// getters

	public DatagramSocket getUdpSocket() {
		return udpSocket;
	}

	public MulticastSocket getMultiSocket() {
		return multiSocket;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public DatagramSocket getMulticastSendSocket() {
		if(this.multicastSendSocket == null)
			this.startMulticastSendSocket();
		return this.multicastSendSocket;
	}

	// startup

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
			this.closeServerSocket();
			this.serverSocket = new ServerSocket(this.tcpPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startMulticastSocket() {
		try {
			this.multiSocket = new MulticastSocket(this.multicastPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startMulticastSendSocket() {
		try {
			this.multicastSendSocket = new DatagramSocket(this.multicastPort);
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

	// close

	public void closeUdpSocket() {
		if (this.udpSocket != null &&
				!this.udpSocket.isClosed())
			this.udpSocket.close();
	}

	public void closeServerSocket() {
		try {
			if (this.serverSocket != null &&
					!this.serverSocket.isClosed())
				this.serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeMulticastSocket() {
		if (this.multiSocket != null && 
				!this.multiSocket.isClosed())
			this.multiSocket.close();
	}

	public void closeMulticastSendSocket() {
		if (this.multicastSendSocket != null && 
				!this.multicastSendSocket.isClosed())
			this.multicastSendSocket.close();
	}

	public void closeReceiveTCPSocket() {
		try {
			if (this.receiveTCPSocket != null && 
					!this.receiveTCPSocket.isClosed())
				this.receiveTCPSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startMulticastReceive() {
		this.closeMulticastSendSocket();
		this.startMulticastSocket();
		

	}

	/**
	 * Start TCP serverSocket and return value of server socket.
	 * 
	 * @return The serversocket.
	 */
	public void startSendFile() {
		this.closeUdpSocket();
		this.startUdpSocket();
		this.closeReceiveTCPSocket();

		this.startServerSocket();
	}

	/**
	 * End of sending file, close the server socket.
	 */
	public void stopSendFile() {
		this.closeServerSocket();
	}

	public void startMulticastSend() {
		this.closeMulticastSocket();
		this.closeMulticastSendSocket();
		this.startMulticastSendSocket();
		
	}

}
