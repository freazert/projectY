package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.rmi.Naming;
import java.util.Scanner;

import interfaces.IInitNodes;


public class MulticastClient {
	private static int portMulticasting = 4446;

    private DatagramSocket socket;
    private boolean broadcast =  true;
    private String group = "230.2.2.3"; //group address
    private int delay = 5000;
    private DatagramSocket socketReceive;
	private MulticastSocket multiSocket;
    
	public MulticastClient()
	{
		try{
			System.out.println("Enter name of the new agent: ");
	        Scanner sc = new Scanner(System.in);
	        String agentName = sc.nextLine();
	        
            socket = new DatagramSocket(portMulticasting);
            socketReceive = new DatagramSocket(3000);
           
            start(agentName);
            
            System.out.println("agent ready");
            
            String prevNode, nextNode;
    		IInitNodes obj = (IInitNodes) Naming.lookup("//" + "192.168.1.15" + "/initNode");
    		System.out.println(obj.getCurrent(agentName) + "n gggnad");
    		System.out.println(obj.getPrevious(agentName) + "previous");
    		System.out.println(obj.getNext(agentName) + "next");
            
            System.out.println("agent ready");
            
            
            
            
            socket.close();
            multiSocket = new MulticastSocket(portMulticasting);
            
            new MulticastRecieveThread(multiSocket, group).start();
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
	}
	
	public void start(String name)
	{
		DatagramPacket packet;
        try{
            InetAddress address = InetAddress.getByName(group);
            //while (broadcast){
                byte[] buf = new byte[256];
                buf = name.getBytes();
                packet = new DatagramPacket(buf,buf.length,address,portMulticasting);
                socket.send(packet);
                
                buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                System.out.println("before receive");
                socketReceive.receive(packet);
                System.out.println("after receive");
                
                int countNodes = Integer.parseInt(new String(packet.getData(), 0, packet.getLength()));
                
                System.out.print(countNodes);
                   
            //}
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
	}
}
