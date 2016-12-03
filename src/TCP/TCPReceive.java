package TCP;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Trivial client for the date server.
 */
public class TCPReceive {
	private String filePath;
	private int socketPort;

	public final static int FILE_SIZE = 6022386; // file size temporary hard coded
	                                               // should bigger than the file to be downloaded
	
	public TCPReceive(int socketPort)
	{
		this.filePath = "D:\\school\\SCH-IW_EI\\shared\\receive\\";
		this.socketPort = socketPort;
	}
	
	/**
	 * Receive a file from the given ip over the given socket
	 * 
	 * @param String
	 * @param int
	 * @throws Exception
	 */
	public void receiveFile(String ip) throws IOException
	{
        Socket socket = new Socket(ip, this.socketPort);

        try {
        	String name = connect(socket);
			getFile(socket, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        socket.close();
	}
	
	/**
	 * Create connection and receive filename.
	 * 
	 * @param socket
	 * @return String
	 * @throws Exception
	 */
	private String connect(Socket socket) throws Exception
	{
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        outToServer.writeBytes("listening" + '\n');

        return inFromServer.readLine();
	}
	

	/**
	 * Download the file.
	 * 
	 * @param socket
	 * @param name
	 * @throws Exception
	 */
	private void getFile(Socket socket, String name) throws Exception
	{
		int bytesRead;
        int currentTot = 0;
        int filesize=2022386;
        
		byte [] bytearray = new byte [filesize];
        InputStream is = socket.getInputStream();
        FileOutputStream fos = new FileOutputStream(filePath + name);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bytesRead = is.read(bytearray,0,bytearray.length);
        currentTot = bytesRead;
        do {
            bytesRead = is.read(bytearray, currentTot, (bytearray.length-currentTot));
            if(bytesRead >= 0) currentTot += bytesRead;
        }
        while(bytesRead > -1);
        System.out.println("bytes: " + bytesRead);
        System.out.println(new String(bytearray));
        
        bos.write(bytearray, 0 , currentTot);
        bos.flush();
        bos.close();
	}

    
    
}
