package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;


public class MulticastClient {
	private static int portMulticasting = 4446;

    private DatagramSocket socket;
    private boolean broadcast =  true;
    private String group = "224.0.0.0"; //group address
    private int delay = 5000;


	public static void main(String[] args) 
	{
		System.out.println("Enter name of the new agent: ");
        Scanner sc = new Scanner(System.in);
        String agentName = sc.nextLine();

        MulticastClient agent = new MulticastClient();
        agent.start(agentName);
	}
	
	public MulticastClient()
	{
		try{
            socket = new DatagramSocket();
            System.out.println("agent ready");
        }
        catch (SocketException e){
            e.printStackTrace();
            System.exit(1);
        }
	}
	
	public void start(String name)
	{
		DatagramPacket packet;
        try{
            InetAddress address = InetAddress.getByName(group);
            while (broadcast){
                byte[] buf = new byte[256];
                buf = name.getBytes();
                packet = new DatagramPacket(buf,buf.length,address,portMulticasting);
                socket.send(packet);
                try{
                    Thread.sleep(delay);
                } catch (InterruptedException e){
                    System.exit(0);
                }   
            }
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
	}
}
