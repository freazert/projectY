package node;

import java.util.Scanner;

public class ListenToCmdThread extends Thread
{
	private Node node;
	// private Scanner sc;

	public ListenToCmdThread(Node node)
	{
		this.node = node;
		// this.sc = scanner;
	}

	@Override
	public void run()
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("to remove node: type getmeout");
		while (true)
		{
			String cmd;
			// System.out.println("waiting for the cmd...");
			while (!sc.hasNext())
			{
				// System.out.println("noooooz");
			}

			cmd = sc.next();
			System.out.println("jeah");

			// } catch (Exception e) {
			// e.printStackTrace(arg0);
			// }
		}
	}

}
