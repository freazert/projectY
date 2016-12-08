package node;

import java.io.File;
import java.util.List;

import interfaces.INodeRMI;

public class StartupThread extends Thread {
	private List<File> files;
	private INodeRMI rmi;
	private Node node;

	public StartupThread(List<File> files, INodeRMI rmi, Node node) {
		this.files = files;
		this.rmi = rmi;
		this.node = node;
	}
	
	@Override
	public void run() {
		SendFileThread sft = new SendFileThread(files, rmi, node);
		sft.start();
		try {
			sft.join();
			ReceiveUDPThread rUDPt = new ReceiveUDPThread(node);
			rUDPt.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
