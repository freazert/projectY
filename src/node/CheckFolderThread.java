package node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The Thread to check if there are new files on the system.
 */
public class CheckFolderThread extends Thread
{
	/**
	 * The node that runs this thread.
	 */
	private Node node;
	/**
	 * The refresh time (in ms).
	 */
	private int refreshMs;
	/**
	 * The path to the folder that needs to be checked.
	 */
	private String folderString;

	/**
	 * The constructor method for the check folder thread.
	 * 
	 * @param node
	 *            The node that runs this thread.
	 * @param refreshMs
	 *            the refresh time (in ms)
	 * @param folder
	 *            the path to the folder that needs to be checked.
	 */
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
			// if(!node.is_receiving) {
			System.out.print("[CheckFolder]");
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
					// GUIController gui_controller = new GUIController();
					// gui_controller.refreshList(this.folderString);
				}
			}
			if (newFiles.size() != 0)
			{
				System.out.println("sendfiles");
				node.sendFiles(newFiles);
			}
			try
			{
				Thread.sleep(this.refreshMs);
			} catch (InterruptedException e)
			{
				System.out.print("checkfolderthread.sleep exception!");
				e.printStackTrace();
			}

		}
	}

}
