package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Scanner;


public class MulticastClient {
	private static int portMulticasting = 4446;

    private DatagramSocket socket;
    private boolean broadcast =  true;
    private String group = "224.0.0.0"; //group address
    private int delay = 5000;
    private DatagramSocket socketReceive;
	private MulticastSocket multiSocket;
    
	public MulticastClient()
	{
		try{
			System.out.println("Enter name of the new agent: ");
	        Scanner sc = new Scanner(System.in);
	        String agentName = sc.nextLine();
	        
            socket = new DatagramSocket();
            socketReceive = new DatagramSocket(3000);
           
            multiSocket = new MulticastSocket(portMulticasting);
            
            System.out.println("agent ready");
            
            start(agentName);
            
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
                
                socketReceive.receive(packet);
                
                int countNodes = Integer.parseInt(new String(packet.getData(), 0, packet.getLength()));
                
                System.out.print(countNodes);
                try{
                    Thread.sleep(delay);
                } catch (InterruptedException e){
                    System.exit(0);
                }   
            //}
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
	}
}
