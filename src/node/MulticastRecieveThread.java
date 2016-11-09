package node;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.rmi.Naming;

import interfaces.IWrapper;

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
			System.out.println("before join");
			System.out.println(this.addr);
			System.out.println(this.socket.toString());
			socket.joinGroup(InetAddress.getByName(this.addr));
			System.out.println("joined group");
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			
			IWrapper obj = (IWrapper) Naming.lookup("//" + "192.168.1.16" + "/hash");
			while(true)
			{
				socket.receive(dp);
				System.out.print("lol");
				
				buf = dp.getData();
                int len = dp.getLength();
                String received = (new String(buf)).substring(0,len);
                
                System.out.println("nodenaam: " + received);
                int newHashing = obj.getHash(received);
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
	}

}
