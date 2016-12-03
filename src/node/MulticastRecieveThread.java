package node;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.rmi.Naming;
import org.json.JSONObject;
import org.json.JSONArray;

import interfaces.INodeRMI;

public class MulticastRecieveThread extends Thread {
	
	private MulticastSocket socket;
	private String addr;
	private Node node;
	
	public MulticastRecieveThread(MulticastSocket socket, String addr, Node node)
	{
		this.socket = socket;
		this.addr = addr;
		this.node = node;
	}
	
	public void run()
	{
		try{
			byte[] buf = new byte[1024];
			JSONObject jobj;
			System.out.println("before join");
			System.out.println(this.addr);
			System.out.println(this.socket.toString());
			socket.joinGroup(InetAddress.getByName(this.addr));
			System.out.println("joined group");
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			
			INodeRMI obj = (INodeRMI) Naming.lookup("//" + "192.168.1.16" + "/");
			while(true)
			{
				
				socket.receive(dp);
				System.out.print("lol");
				
				buf = dp.getData();
                int len = dp.getLength();
                String received = (new String(buf)).substring(0,len);
                jobj = new JSONObject(received);
                
                this.node.setNodes(jobj.getString("name"));
                
                if(received.equals("next"))
                {
                	node.controlFiles();
                }
                
                System.out.println("nodenaam: " + jobj.getString("name"));
                //int newHashing = obj.getHash(received);
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
	}

}
