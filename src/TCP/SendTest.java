package TCP;

public class SendTest {
	public static void main(String[] args)
	{
		TCPSend send = new TCPSend(6789);
		send.sendFile("lol.txt");
	}
	
}
