package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;
import org.json.*;



public class MulticastClient {
	private static int portMulticasting = 4446;

    private DatagramSocket socket;
    private String group = "230.2.2.3"; //group address
    private DatagramSocket socketReceive;
	private MulticastSocket multiSocket;
	private Node node;
	
	
    
	public MulticastClient(Node node)
	{
		this.node = node;
		try{
			System.out.println("Enter name of the new agent: ");
	        Scanner sc = new Scanner(System.in);
	        String agentName = sc.nextLine();
	        //sc.close();
	        
	        JSONObject jobj = new JSONObject();
    		jobj.put("type", "new");
    		jobj.put("name", agentName);
    		
    		System.out.println(jobj);
	        
            socket = new DatagramSocket(portMulticasting);
            socketReceive = new DatagramSocket(3000);
           
            start(jobj.toString());
            
            System.out.println("agent ready");
            
    		this.node.initNodes(jobj.getString("name"));

    		//IInitNodes obj = (IInitNodes) Naming.lookup("//" + "192.168.1.15" + "/initNode");
    		
    		
    		
            
            System.out.println("agent ready");
            
            socket.close();
            multiSocket = new MulticastSocket(portMulticasting);
            
            new MulticastRecieveThread(multiSocket, group, this.node).start();
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
                byte[] buf = new byte[1024];
                buf = name.getBytes();
                packet = new DatagramPacket(buf,buf.length,address,portMulticasting);
                socket.send(packet);
                
                buf = new byte[1024];
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
	
	/*public void shutdown()
	{
		try {
			DatagramPacket packet;
			IWrapper obj = (IWrapper) Naming.lookup("//" + "192.168.1.15" + "/getWrapper");
			obj.removeNode(currentHash);
			
			socket = new DatagramSocket(2000);
			
			String toSend = "previous: " + previous;
			byte[] buf = new byte[toSend.getBytes().length];
			buf = toSend.getBytes();
			
            packet = new DatagramPacket(buf,buf.length,obj.getIp(next),portMulticasting);
            socket.send(packet);
            
            
            
            
			obj.getIp(previuos);
			
			String toSend = "next: " + next;
			byte[] buf = new byte[toSend.getBytes().length];
			buf = toSend.getBytes();
			
            packet = new DatagramPacket(buf,buf.length,obj.getIp(previous),portMulticasting);
            socket.send(packet);
			
			
		} catch (MalformedURLException e) {
			// ODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// ODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// ODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
