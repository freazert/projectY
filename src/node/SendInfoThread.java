package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.json.JSONException;
import org.json.JSONObject;

public class SendInfoThread extends Thread
{
	private SocketHandler sHandler;
	private Node node;
	private String ip;

	public SendInfoThread(Node node, SocketHandler sHandler, String ip)
	{
		super();
		this.node = node;
		this.sHandler = sHandler;
		this.ip = ip;
	}

	public void run()
	{
		byte[] sendData = new byte[1024];
		while (true)
		{
			if(node.checkBusyState())
			try
			{
				this.sHandler.startInfo();

				DatagramSocket clientSocket = this.sHandler.getUdpInfoSocket();

				try
				{

					JSONObject jobj = new JSONObject();
					jobj.put("type", "inforeply");
					jobj.put("data", this.node.getBusyState());
					System.out.println("reply on info" + jobj.toString());

					sendData = jobj.toString().getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip),
							9666);
					clientSocket.send(sendPacket);

					System.out.println("packet sent in info");

				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
