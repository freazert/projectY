package server.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

public class MulticastServerThread extends Thread {
	private Wrapper wrap;

	public MulticastServerThread(Wrapper wrap)
	{
		this.wrap = wrap;
	}
	
	public void run()
	{
		String groupIP = "230.2.2.3";
        int portMulticasting = 4446;
        MulticastSocket socket;
        
        DatagramSocket socketUni = null;
		try {
			socketUni = new DatagramSocket();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        InetAddress group;
        //if (args.length > 0)
        //    groupIP = args[0];
        try{
            //get a multicast socket and join group
            socket = new MulticastSocket(portMulticasting);
            group = InetAddress.getByName(groupIP);
            socket.joinGroup(InetAddress.getByName(groupIP));
            //get packet
            DatagramPacket packet;
            boolean is_true = true;
            System.out.println("lolz");
            while (is_true){
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf,buf.length);
                System.out.println("waiting...");
                socket.receive(packet);
                System.out.println("received!");
                buf = packet.getData();
                int len = packet.getLength();
                String received = (new String(buf)).substring(0,len);
                try{
                	JSONObject jobj = new JSONObject(received);
                	this.wrap.createNode(jobj.getString("name"), packet.getAddress().getHostAddress());
                    System.out.println("Agent name: " + jobj.getString("name") + " (" + packet.getAddress() + ")");
                    
                    buf = new byte[256];
                    
                    buf = String.valueOf(wrap.getHashMap().getCount()).getBytes();
                    packet = new DatagramPacket(buf , buf.length, packet.getAddress(), 3000);
                    socketUni.send(packet);
                } catch (NumberFormatException e){
                    System.out.println("cannot interpret number");
                } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            socket.leaveGroup(group);
            socket.close();
            
        } catch (IOException e){
            e.printStackTrace();
        } finally {
        	
        }
	}
}
