package server.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServerThread extends Thread {
	private Wrapper wrap;

	public MulticastServerThread(Wrapper wrap)
	{
		this.wrap = wrap;
	}
	
	public void run()
	{
		String groupIP = "224.0.0.0";
        int portMulticasting = 4446;
        MulticastSocket socket;
        InetAddress group;
        //if (args.length > 0)
        //    groupIP = args[0];
        try{
            //get a multicast socket and join group
            socket = new MulticastSocket(portMulticasting);
            group = InetAddress.getByName(groupIP);
            socket.joinGroup(group);
            //get packet
            DatagramPacket packet;
            boolean is_true = true;
            //while (is_true){
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf,buf.length);
                socket.receive(packet);
                buf = packet.getData();
                int len = packet.getLength();
                String received = (new String(buf)).substring(0,len);
                try{
                	this.wrap.createNode(received, packet.getAddress().getHostAddress());
                    System.out.println("Agent name: " + received + " (" + packet.getAddress() + ")");
                } catch (NumberFormatException e){
                    System.out.println("cannot interpret number");
                }
            //}
            socket.leaveGroup(group);
            socket.close();
            
        } catch (IOException e){
            e.printStackTrace();
        } finally {
        	
        }
	}
}
