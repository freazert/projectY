package node;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Maintain all socket information
 */
public class SocketHandler
{
	/**
	 * The socket used for udp communication
	 */
	private DatagramSocket udpSocket;
	/**
	 * The second socket used for udp communication
	 */
	private DatagramSocket udpInfoSocket;
	/**
	 * The socket used for listening to the multicast.
	 */
	private MulticastSocket multiSocket;
	/**
	 * The socket used for sending to the multicast;
	 */
	private DatagramSocket multicastSendSocket;
	/**
	 * The socket used to send over tcp.
	 */
	private ServerSocket serverSocket;
	/**
	 * The socket used to receive over tcp.
	 */
	private Socket receiveTCPSocket;
	/**
	 * The port number to initialize all sockets.
	 */
	private int tcpPort, udpPort, multicastPort, udpInfoPort;

	/**
	 * The constructor method for the socket handler.
	 * 
	 * @param tcpPort
	 *            The port for tcp
	 * @param udpPort
	 *            The port for udp
	 * @param multicastPort
	 *            The port for multicast
	 */
	public SocketHandler(int tcpPort, int udpPort, int multicastPort)
	{
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		this.multicastPort = multicastPort;
		this.udpInfoPort = 9666;

		try
		{
			this.udpSocket = new DatagramSocket(this.udpPort);
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// getters
	/**
	 * Get the udp socket.
	 * 
	 * @return The udp socket.
	 */
	public DatagramSocket getUdpSocket()
	{
		return udpSocket;
	}

	/**
	 * Get the socket for listening to the multicast
	 * 
	 * @return the socket for listening to the multicast.
	 */
	public MulticastSocket getMultiSocket()
	{
		return multiSocket;
	}

	/**
	 * Get the socket to send over TCP
	 * 
	 * @return the socket to send over TCP
	 */
	public ServerSocket getServerSocket()
	{
		return serverSocket;
	}

	/**
	 * Get the socket to send to the multicast group
	 * 
	 * @return the socket to send to the multicast group
	 */
	public DatagramSocket getMulticastSendSocket()
	{
		if (this.multicastSendSocket == null)
			this.startMulticastSendSocket();
		return this.multicastSendSocket;
	}

	// startup

	/**
	 * Get the socket to receive TCP from.
	 * 
	 * @return the socket to receive TCP from
	 */
	public Socket getReceiveTCPSocket()
	{
		return receiveTCPSocket;
	}

	/**
	 * Start running the UDP socket.
	 */
	public void startUdpSocket()
	{
		try
		{
			this.udpSocket = new DatagramSocket(this.udpPort);
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Start running the TCP socket.
	 */
	public void startServerSocket()
	{
		try
		{
			this.closeServerSocket();
			this.serverSocket = new ServerSocket(this.tcpPort);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Start the socket to listen on multicast.
	 */
	public void startMulticastSocket()
	{
		try
		{
			this.multiSocket = new MulticastSocket(this.multicastPort);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Start the socket to send over multicast.
	 */
	public void startMulticastSendSocket()
	{
		try
		{
			this.multicastSendSocket = new DatagramSocket(this.multicastPort);
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Start the socket to receive over TCP
	 * 
	 * @param ip
	 *            the ip of the sender.
	 */
	public void startReceiveTCPSocket(String ip)
	{
		try
		{
			this.closeServerSocket();

			System.out.println("tcp port " + this.tcpPort);
			this.receiveTCPSocket = new Socket(ip, this.tcpPort);
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// close

	/**
	 * Close the udp socket
	 */
	public void closeUdpSocket()
	{
		if (this.udpSocket != null && !this.udpSocket.isClosed())
			this.udpSocket.close();
	}

	/**
	 * Close the TCP send socket.
	 */
	public void closeServerSocket()
	{
		try
		{
			if (this.serverSocket != null && !this.serverSocket.isClosed())
				this.serverSocket.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Close the socket for listening on multicast.
	 */
	public void closeMulticastSocket()
	{
		if (this.multiSocket != null && !this.multiSocket.isClosed())
			this.multiSocket.close();
	}

	/**
	 * Close the socket for sending over multcast.
	 */
	public void closeMulticastSendSocket()
	{
		if (this.multicastSendSocket != null && !this.multicastSendSocket.isClosed())
			this.multicastSendSocket.close();
	}

	/**
	 * Close the socket to receive over TCP
	 */
	public void closeReceiveTCPSocket()
	{
		try
		{
			if (this.receiveTCPSocket != null && !this.receiveTCPSocket.isClosed())
				this.receiveTCPSocket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Start receiving over multicast.
	 */
	public void startMulticastReceive()
	{
		this.closeMulticastSendSocket();
		this.startMulticastSocket();

	}

	/**
	 * Start TCP serverSocket and return value of server socket.
	 */
	public void startSendFile()
	{
		this.closeUdpSocket();
		this.startUdpSocket();
		this.closeReceiveTCPSocket();

		this.startServerSocket();
	}

	/**
	 * End of sending file, close the server socket.
	 */
	public void stopSendFile()
	{
		this.closeServerSocket();
	}

	/**
	 * Start &amp; stop the correct sockets to start sending over multicast.
	 */
	public void startMulticastSend()
	{
		this.closeMulticastSocket();
		this.closeMulticastSendSocket();
		this.startMulticastSendSocket();

	}
}
