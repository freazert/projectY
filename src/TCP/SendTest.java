package TCP;

import java.net.ServerSocket;
import java.net.Socket;

public class SendTest
{
	public static void main(String[] args)
	{
		try
		{
			ServerSocket socket = new ServerSocket(9876);
			for (int i = 0; i < 100; i++)
			{
				TCPSend send = new TCPSend(socket);
				send.send("lol.txt");
			}
		} catch (Exception e)
		{

		}
	}

}
