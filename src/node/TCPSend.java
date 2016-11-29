package node;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSend {

	private Socket connectedSocket;
	private int socketPort;
	private String filePathSend;
	private String fileNameSend;

	public TCPSend(Socket connectedSocket, String filePath, String fileName) {
		this.connectedSocket = connectedSocket;
		this.socketPort = 1300;
		this.filePathSend = filePath;
		this.fileNameSend = fileName;
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	// public final static int SOCKET_PORT = 13000;
	// public final static String FILE_PATH_TO_SEND = "D:\\school\\";
	// public final static String FILE_NAME_TO_SEND = "scwcsc.txt";

	public void init() throws IOException {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(this.socketPort);
			while (true) {
				try {
					Socket connectedSocket = serverSocket.accept();
					System.out.print("connected");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void run() {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		
		try {
			byte[] nameByteArray = this.fileNameSend.getBytes();

			StringBuilder sb = new StringBuilder(this.filePathSend + this.fileNameSend);
			System.out.println(sb.toString());
			os = connectedSocket.getOutputStream();
			os.write(nameByteArray, 0, nameByteArray.length);
			os.flush();

			File fileToSend = new File(sb.toString());
			byte[] fileByteArray = new byte[(int) fileToSend.length()];
			fis = new FileInputStream(fileToSend);
			bis = new BufferedInputStream(fis);
			bis.read(fileByteArray, 0, fileByteArray.length);

			System.out.println("Sending " + sb.toString() + "(" + fileByteArray.length + " bytes)");
			os.write(fileByteArray, 0, fileByteArray.length);
			os.flush();
			System.out.println("Done.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				try {
					bis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (os != null)
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				} // os.close();

		}
	}

}
