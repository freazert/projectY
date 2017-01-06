package node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gui.GUIController;
import gui.GUIModel;

public class CheckFolderThread extends Thread
{

	private Node node;
	private int refreshMs;
	private String folderString;

	public CheckFolderThread(Node node, int refreshMs, String folder)
	{
		this.node = node;
		this.refreshMs = refreshMs;
		this.folderString = folder;
	}

	@Override
	public void run()
	{
		while (true)
		{
			if(!node.is_receiving) {
			File folder = new File(this.folderString);
			File[] listOfFiles = folder.listFiles();
			List<File> newFiles = new ArrayList<File>();
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].isFile() && !node.getLocalList().contains(listOfFiles[i].getName()))
				{
					System.out.print("\nNew local File discovered: ");
					System.out.println(listOfFiles[i].getName());
					newFiles.add(listOfFiles[i]);
					node.addOwnerList(listOfFiles[i].getName());
					GUIController gui_controller = new GUIController();
					gui_controller.refreshList(this.folderString);
				}
			}
			if (newFiles.size() != 0)
				System.out.println("sendfiles");
				node.sendFiles(newFiles);
			}
			try
			{
				Thread.sleep(this.refreshMs);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
