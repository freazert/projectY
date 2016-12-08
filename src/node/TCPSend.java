package node;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSend {
	Socket connectedSocket = null;
	// private int socketPort;
	private ServerSocket welcomeSocket;
	public final static String FILE_PATH_TO_SEND = "c:\\Nieuwe map\\";
	public final static String FILE_NAME_TO_SEND = "scwcsc.txt";

	TCPSend(ServerSocket welcomeSocket) {
		// this.connectedSocket = new Socket(6789);
		System.out.println("creating");
			this.welcomeSocket = welcomeSocket;
		}

	public void sendFile(String fileName) {
			Socket socket;
			try {
				System.out.println("waiting for accept");
				socket = this.welcomeSocket.accept();

				File transferFile = new File(FILE_PATH_TO_SEND + fileName);
				System.out.println("Received: " + FILE_PATH_TO_SEND + fileName);
				DataOutputStream outToClient;

				if (transferFile.exists()) {
					// send filename
					outToClient = new DataOutputStream(socket.getOutputStream());
					outToClient.writeBytes(fileName + "\n");
					outToClient.flush();

					byte[] bytearray = new byte[(int) transferFile.length()];
					FileInputStream fin = new FileInputStream(transferFile);
					BufferedInputStream bin = new BufferedInputStream(fin);
					bin.read(bytearray, 0, bytearray.length);
					/// OutputStream os = socket.getOutputStream();

					// send file
					System.out.println("Sending Files...");
					outToClient.write(bytearray, 0, bytearray.length);
					outToClient.flush();
					bin.close();
					fin.close();
					socket.close();
					System.out.println("File transfer complete");
				} else {
					System.out.println("file not found");
					outToClient = new DataOutputStream(socket.getOutputStream());
					outToClient.writeBytes("File doesn't exist!");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
