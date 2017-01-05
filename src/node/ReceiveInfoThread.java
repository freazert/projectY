package node;

import java.io.IOException;
import java.net.DatagramPacket;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiveInfoThread extends Thread
{

	private Node node;
	private SocketHandler sHandler;

	public ReceiveInfoThread(Node node, SocketHandler sHandler)
	{
		super();
		this.node = node;
		this.sHandler = sHandler;
		this.sHandler.startInfo();
	}

	public void run()
	{

		while (true)
		{

			try
			{
				while (!node.isMapUpdate())
				{
					this.node.printNodes();
					System.out.println("receiveUDPThread run");
					DatagramPacket data = getData();
					String ip = data.getAddress().getHostAddress();

					handleData(new String(data.getData()), ip);
				}
			} catch (JSONException | IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * receive data from the socket.
	 *
	 * @return the received data
	 * @throws IOException
	 *             Something went wrong while receiving data through UDP
	 */
	private DatagramPacket getData() throws IOException
	{
		byte[] receiveData = new byte[10240];

		this.sHandler.startInfo();
		System.out.println("rft receiveUDP");

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		System.out.println("waiting for udp receive");
		sHandler.getUdpInfoSocket().receive(receivePacket);

		System.out.println("UDP received");

		// reply();
		return receivePacket;
	}

	/**
	 * Handle JSON data on type. handleData checks for the given type in the
	 * JSON message. starts the TCP receiveThread when it gets file, start the
	 * control files method when the type is next.
	 *
	 * @param data
	 *            the unparsed JSON string
	 * @param ip
	 *            the ip of the UDP sender
	 * @throws JSONException
	 *             Something went wrong while parsing the JSON data. possible
	 *             that the data is not correctly formatted to json.
	 * @throws IOException
	 *             Something went wrong while receiving the file.
	 */
	private void handleData(String data, String ip) throws JSONException, IOException
	{
		JSONObject jobj = new JSONObject(data);

		String type = jobj.getString("type");
		System.out.println("type: " + type);
		switch (type)
		{
		case "info" :
			//new SendInfoThread(this.node, this.sHandler, ip).start();
			break;
		}

	}
}
