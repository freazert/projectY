package node;

import java.io.IOException;
import java.net.DatagramPacket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The class that receives udp messages and handles accordingly.
 */
public class ReceiveUDPThread extends Thread
{

	/**
	 * The object that maintains all the socket actions.
	 */
	private SocketHandler sHandler;
	/**
	 * The node that receives the data.
	 */
	private Node node;

	/**
	 * The constructor method for the ReceiveUDPThread
	 *
	 * @param node
	 *            The node that receives the data.
	 * @param sHandler
	 *            The object that maintains all the socket actions.
	 */
	public ReceiveUDPThread(Node node, SocketHandler sHandler)
	{
		this.node = node;
		this.sHandler = sHandler;
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				while (true)
				{
					// System.out.println("receiveUDPThread run");
					DatagramPacket data = getData();
					String ip = data.getAddress().getHostAddress();

					handleData(new String(data.getData()), ip);
				}

			} catch (IOException e)
			{
				// e.printStackTrace();
			} catch (JSONException e)
			{
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

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		sHandler.getUdpSocket().receive(receivePacket);

		System.out.println("UDP received");

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
		case "file" :
			System.out.println("receive file");
			node.isReceiving = true;
			TCPReceive receive = new TCPReceive(node, this.sHandler);
			String name = jobj.getString("data");
			int size = (int) jobj.getLong("size");
			System.out.println("the name is: " + name);
			receive.receiveFile(ip, name, size);
			// FileFiche fiche = new FileFiche(name, 2, ip); //voeg nieuwe
			// filefiche toe --> ID VAN STURENDE NODE?
			// node.addFileFiche(fiche);
			break;
		case "next" :
			this.node.setNextNode(jobj.getInt("data"));
			break;
		case "previous" :
			this.node.setPrevNode(jobj.getInt("data"));
			break;
		case "remove" :
			String dataObj = jobj.getString("data");
			this.node.removeFile(dataObj);
			break;
		}

	}

}
