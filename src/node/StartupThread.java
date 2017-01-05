package node;

import java.io.File;
import java.util.List;

import interfaces.INodeRMI;

public class StartupThread extends Thread
{

	private List<File> files;
	private INodeRMI rmi;
	private Node node;
	private SocketHandler sHandler;

	public StartupThread(INodeRMI rmi, Node node, SocketHandler sHandler)
	{
		this.sHandler = sHandler;
		this.files = files;
		this.rmi = rmi;
		this.node = node;
	}

	@Override
	public void run()
	{
		// SendFileThread sft = new SendFileThread(files, rmi, node);
		// sft.start();
		// sft.join();
		
		ReceiveUDPThread rft = new ReceiveUDPThread(this.node, this.sHandler);
                rft.start();
                new CheckFolderThread(this.node, 10000, this.node.getFolderString()).start();
		//new ReceiveInfoThread(this.node, this.sHandler).start();
		new FailureThread(this.node, rmi).start();
	}

}