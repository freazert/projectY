package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import org.json.*;

/**
 * Send username over the multicast and start running the listen on multicast
 * thread.
 */
public class MulticastClient
{
	/**
	 * The port used for multicasting.
	 */
	private static int portMulticasting = 4446;
	/**
	 * The group address of the multicast
	 */
	private String group = "230.2.2.3"; // group address
	/**
	 * The node that runs the project.
	 */
	private Node node;
	/**
	 * The object that maintains all sockets.
	 */
	private SocketHandler sHandler;

	/**
	 * The constructor method for the MulticastClient.
	 * 
	 * @param node
	 *            The node that runs the project.
	 * @param sHandler
	 *            The object that maintains all sockets.
	 */
	public MulticastClient(Node node, SocketHandler sHandler)
	{
		this.node = node;
		this.sHandler = sHandler;
		sHandler.startMulticastSend();
	}

	/**
	 * Send message to the multicast group.
	 * 
	 * @param name
	 *            the name of the node.
	 */
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

	/**
	 * The method to start up the multicast send and when finished start the
	 * multicast listen.
	 * 
	 * @param name
	 *            the name of the node.
	 */
	public void multicastStart(String name)
	{
		try
		{
			JSONObject jobj = new JSONObject();
			jobj.put("type", "new");
			jobj.put("data", name);

			System.out.println(jobj);

			start(jobj.toString());

			this.sHandler.startMulticastReceive();
			
			new MulticastRecieveThread(group, this.node, this.sHandler).start();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
