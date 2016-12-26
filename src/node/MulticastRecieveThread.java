package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import org.json.JSONObject;
import org.json.JSONException;


public class MulticastRecieveThread extends Thread {

	private SocketHandler sHandler;
	private String addr;
	private Node node;

	/**
	 * The constructor method for the MulticastReceiveThread.
	 * 
	 * @param group The socket for the multicast group.
	 * @param node The IP of the multicast.
	 * @param sHandler The node that uses the multicastReceiveThread.
	 */
	public MulticastRecieveThread(String group, Node node, SocketHandler sHandler) {
		this.addr = group;
		this.node = node;
		this.sHandler = sHandler;
	}

	public void run() {
		try {
			joinMulticast();

			while (true)
				handleData(receiveData());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Join Multicast group.
	 * 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	private void joinMulticast() throws UnknownHostException, IOException{
		System.out.println("before join");
		sHandler.getMultiSocket().joinGroup(InetAddress.getByName(this.addr));
		System.out.println("joined group");
	}

	/**
	 * handle data of the JSON string.
	 * 
	 * @param data
	 *            the string that needs to be handled
	 * @throws JSONException
	 */
	private void handleData(String data) throws JSONException {
		String name;
		System.out.println("received: " +data);
		JSONObject jobj = new JSONObject(data);
		String type = jobj.getString("type");
		
		
		switch (type) {
		case "next":
			name = jobj.getString("data");
			node.controlFiles();
			break;
		case "new":
			name = jobj.getString("data");
			this.node.setNodes(name);
		}
 
	}

	/**
	 * receive Data from the multicast
	 * 
	 * @return Data sent over UDP parsed as string.
	 * @throws IOException
	 */
	private String receiveData() throws IOException {
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);

		this.sHandler.getMultiSocket().receive(dp);
		System.out.println("lol");

		buf = dp.getData();
		int len = dp.getLength();
		return (new String(buf)).substring(0, len);

	}

}
