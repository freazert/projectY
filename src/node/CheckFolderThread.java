package node;

import java.io.File;

public class CheckFolderThread extends Thread {

	private Node node;
	private int refreshMs;

	public CheckFolderThread(Node node, int refreshMs) {
		this.node = node;
		this.refreshMs = refreshMs;
	}

	@Override
	public void run() {
		while (true) {
			File folder = new File("c:\\Nieuwe map");
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile() && !node.getLocalList().contains(listOfFiles[i].getName())) {
					//node.newFile(listOfFiles[i]);
				}
			}

			try {
				Thread.sleep(this.refreshMs);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
