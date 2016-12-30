package TCP;

import java.io.IOException;

public class clientTest
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		TCPReceive tr = new TCPReceive(9876);
		try
		{
			for (int i = 0; i < 100; i++)
				tr.receiveFile("192.168.1.30", i + ".txt", 7);
			// tr.receiveFile("127.0.0.1");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
