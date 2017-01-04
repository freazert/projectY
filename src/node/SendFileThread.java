package node;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import interfaces.INodeRMI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendFileThread extends Thread {

    private List<File> files;
    private INodeRMI rmi;
    private Node node;
    ServerSocket serverSocket;
    private SocketHandler sHandler;

    /**
     * The constructor method for the SendFileThread.
     *
     * @param files List of files to be sent
     * @param rmi the remote method invocation object used to communicate with
     * the nameServer.
     * @param node the node that sends the files.
     * @param sHandler the object that maintains all the sockets.
     */
    public SendFileThread(List<File> files, INodeRMI rmi, Node node, SocketHandler sHandler) {

        this.sHandler = sHandler;
        this.files = files;
        this.rmi = rmi;
        this.node = node;
    }

    @Override
    public void run() {
        synchronized (this.node) {
            node.setMapUpdate(true);
            sHandler.startSendFile();
            for (File file : files) {
                try {
                    String ip = getIP(file.getName());
                    if (!ip.equals(rmi.getIp(this.node.getCurrent()))) {
                        InetAddress IPAddress = InetAddress.getByName(ip);

                        boolean canSendFile = false;
                        while (!canSendFile && !this.node.getBusyState()) {
                            String jsonString = createJsonInfo();
                            System.out.print(jsonString);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(SendFileThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            sendUdp(jsonString, IPAddress);

                            byte[] receiveData = new byte[10240];

                            System.out.println("rft receiveUDP");
                            
                            sHandler.startInfo();

                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            System.out.println("waiting for udp receive");
                            sHandler.getUdpInfoSocket().receive(receivePacket);
                           
                            String receive = new String(receivePacket.getData());
                            System.out.print(receive);
                            try { 
                                JSONObject json = new JSONObject(receive);
                                if(json.get("type").equals("inforeply"))
                                {
                                    canSendFile = !json.getBoolean("data");
                                }
                            } catch (JSONException ex) {
                                Logger.getLogger(SendFileThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try
							{
								this.sleep(1000);
							} catch (InterruptedException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }
                        
                        //this.node.setBussy(true);

                        String jsonString = createJsonString(file.getName(), file.length());
                        System.out.print(jsonString);
                        sendUdp(jsonString, IPAddress);

                        // receive
                        TCPSend sendFile = new TCPSend(this.sHandler);
                        sendFile.send(file.getName());
                        this.node.removeOwnerList(file.getName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        node.failure(this.rmi.getPrevious(file.getName()));
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }

            }

            
            //this.node.setBussy(false);
            sHandler.stopSendFile();
            node.setMapUpdate(false);
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
            DatagramSocket clientSocket = this.sHandler.getUdpSocket();

            sendData = data.toString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, 6789);

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
