package node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckFolderThread extends Thread {

	private Node node;
	private int refreshMs;
	private String folderString;

	public CheckFolderThread(Node node, int refreshMs, String folder) {
		this.node = node;
		this.refreshMs = refreshMs;
		this.folderString = folder;
	}

	@Override
	public void run() {
		while (true) {
			File folder = new File(this.folderString);
			File[] listOfFiles = folder.listFiles();
			List<File> newFiles = new ArrayList<File>();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile() && !node.getLocalList().contains(listOfFiles[i].getName())) {
					newFiles.add(listOfFiles[i]);
					node.addLocalList(listOfFiles[i].getName());
				}
			}
			if(newFiles.size() != 0)
				node.sendFiles(newFiles);
			try {
				Thread.sleep(this.refreshMs);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
