package TCP;

import java.io.IOException;

public class clientTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TCPReceive tr = new TCPReceive(6789);
		try {
			tr.receiveFile("127.0.0.1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
