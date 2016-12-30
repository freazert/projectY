package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import org.json.*;

public class MulticastClient
{
	private static int portMulticasting = 4446;

	private DatagramSocket socket;
	private String group = "230.2.2.3"; // group address
	private DatagramSocket socketReceive;
	private MulticastSocket multiSocket;
	private Node node;
	private SocketHandler sHandler;

	public MulticastClient(Node node, SocketHandler sHandler)
	{
		this.node = node;
		this.sHandler = sHandler;
		sHandler.startMulticastSend();
	}

	public void start(String name)
	{
		DatagramPacket packet;
		try
		{
			InetAddress address = InetAddress.getByName(group);
			// while (broadcast){
			byte[] buf = new byte[1024];
			buf = name.getBytes();
			packet = new DatagramPacket(buf, buf.length, address, portMulticasting);
			sHandler.getMulticastSendSocket().send(packet);

			// }
			// socket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public MulticastRecieveThread multicastStart(String name)
	{
		try
		{
			// TODO Auto-generated method stub

			JSONObject jobj = new JSONObject();
			jobj.put("type", "new");
			jobj.put("data", name);

			System.out.println(jobj);

			start(jobj.toString());

			this.sHandler.startMulticastReceive();
			// socket.close();

			// multiSocket = new MulticastSocket(portMulticasting);

			new MulticastRecieveThread(group, this.node, this.sHandler).start();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
