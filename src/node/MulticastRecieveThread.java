package node;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

public class MulticastRecieveThread extends Thread {
	
	private MulticastSocket socket;
	private String addr;
	
	public MulticastRecieveThread(MulticastSocket socket, String addr)
	{
		this.socket = socket;
		this.addr = addr;
	}
	
	public void run()
	{
		try{
			byte[] buf = new byte[256];
			socket.joinGroup(InetAddress.getByName(addr));
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			while(true)
			{
				socket.receive(dp);
				System.out.print("lol");
			}
		}
		catch(Exception e)
		{
			
		}
			
	}

}
