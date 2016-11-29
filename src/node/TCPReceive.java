package node;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPReceive {
	private int socketPort;      // you may change this
	private String server;  		// PC asus.
	private String filePathReceived; // you may change this, I give a
	                                                            // different name because i don't want to
	                                                            // overwrite the one used by server...

	private int fileSize;// = 1024*1024*10; // file size temporary hard coded
	                                               // should bigger than the file to be downloaded

	public TCPReceive(String server)
	{
		this.socketPort = 13000;
		this.server = server;
		this.filePathReceived = "D:\\school\\SCH-IW_EI\\shared\\receive\\";
		this.fileSize = 1024*1024*10;
	}
    
	/**
	 * Start receiving file.
	 */
	public void run()
	{
		FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        int current = 0;
        int bytesRead;
        
        try {
            sock = new Socket(this.server, this.socketPort);
            System.out.println("Connecting...");
            InputStream is = sock.getInputStream();
            BufferedReader inFromServer = new BufferedReader(
          		  new InputStreamReader(is));
            System.out.println("in from server");
            String name = inFromServer.readLine();
            System.out.println(name);
            System.out.println("end");
            
            // receive file
        	byte [] myByteArray  = new byte [fileSize];
        	//InputStream is = sock.getInputStream();
        	fos = new FileOutputStream(this.filePathReceived + name);
        	bos = new BufferedOutputStream(fos);
        	bytesRead = is.read(myByteArray,0,myByteArray.length);
        	//System.out.println(myByteArray.toString());
        	//System.out.println(new String(myByteArray, "UTF-8"));
        	current = bytesRead;
        	System.out.println(current);

        	 // receive file
			do {
				bytesRead = is.read(myByteArray, current, (myByteArray.length - current));
				System.out.println("bytesRead:" + current);
				if (bytesRead >= 0)
					current += bytesRead; // System.out.println(bytesRead);
			} while (bytesRead > -1);
          
          System.out.println(current);
            

            bos.write(myByteArray, 0 , current);
            bos.flush();
            System.out.println("File " + this.filePathReceived + name
                + " downloaded (" + current + " bytes read)");
            
          } catch(IOException e) {
        	  e.printStackTrace();
          } finally {
              if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              if (sock != null)
				try {
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        
        
	}
	
	/**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
    /*public static void main(String[] args) throws IOException {
    	String server;
    	int fileSize = FILE_SIZE;
    	if(args.length == 0) {
    		server = SERVER;
    	} else {
    		server = args[0];
    		if(args.length > 1) {
    			fileSize = Integer.parseInt(args[1]);
    		}
    	}
    	
    	System.out.println("file size: " + fileSize);
    	
        
        
        
        
      }*/

}
