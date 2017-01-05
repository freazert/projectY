package node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.RemoteException;

import org.json.JSONException;
import org.json.JSONObject;

import interfaces.INodeRMI;

public class SendInfoThread extends Thread
{
	private SocketHandler sHandler;
	private Node node;
	private String ip;
	private INodeRMI rmi;

	public SendInfoThread(Node node, SocketHandler sHandler, String ip, INodeRMI rmi)
	{
		super();
		this.node = node;
		this.sHandler = sHandler;
		this.ip = ip;
		this.rmi = rmi;
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
	
	/**
     * Send server a message with the name of a file, requesting the ip of the
     * node that needs to be sent to. return null if
     *
     * @param name the name of file
     * @return the ip of the client node.
     *
     * @throws RemoteException Something went wrong while using the remote
     * method invocation to the name server.
     */
    private String getIP(String name) throws RemoteException {
        String ip = rmi.getPrevIp(name);

        int hash = rmi.getHash(name);
        if (ip.equals(rmi.getIp(this.node.getCurrent()))) {
            ip = rmi.getIp(this.node.getPrev());
        }

        return ip;
    }

    /**
     * Create the string that needs to be sent to the other node.
     *
     * @param name the name of the file.
     * @param size the size of the file.
     * @return the string for the user, on failure returns an empty string.
     */
    private String createJsonString(String name, long size) {
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("type", "file");
            jobj.put("data", name);
            jobj.put("size", size);

            return jobj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String createJsonInfo() {
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("type", "info");

            return jobj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * send UDP message to node that receives file.
     *
     * @param data the data that needs to be sent.
     * @param ip the IP address of the receiving node.
     */
    private void sendUdp(String data, InetAddress ip) {
        byte[] sendData = new byte[1024];

        try {
            DatagramSocket clientSocket = this.sHandler.getUdpInfoSocket();

            sendData = data.toString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, 9666);

            clientSocket.send(sendPacket);

            /*
			 * DatagramPacket receivePacket = new DatagramPacket(receiveData,
			 * receiveData.length); clientSocket.receive(receivePacket); String
			 * modifiedSentence = new String(receivePacket.getData());
			 * System.out.println("FROM SERVER:" + modifiedSentence);
             */
            // clientSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
