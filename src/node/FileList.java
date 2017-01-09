package node;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import java.util.TreeMap;

/**
 * The object that is used by the agent to update the nodes with the files in
 * the system.
 */
public class FileList extends Observable implements Serializable
{
	/**
	 * All the files known to this object.
	 */
	private TreeMap<String, Boolean> newSystemFileList;

	/**
	 * The constructor method of the fileList.
	 */
	public FileList()
	{
	}

	/**
	 * Get the full file list.
	 * 
	 * @return the file list.
	 */
	public TreeMap<String, Boolean> getFileList()
	{
		return newSystemFileList;
	}

	/**
	 * Add new files to the file list.
	 * 
	 * @param node
	 *            the node that is currently using the file list.
	 */
	public void setFileList(Node node)
	{
		List<String> local = node.getLocalList();
		List<String> owner = node.getOwnerList();
		for (int i = 0; i < local.size(); i++)
		{
			if (newSystemFileList != null)
			{
				if (!newSystemFileList.containsKey(local.get(i)))
				{
					newSystemFileList.put(local.get(i), false);
				}
			} else
			{
				newSystemFileList = new TreeMap<String, Boolean>();
				newSystemFileList.put(local.get(i), false);
			}
		}
		setChanged(); // mark the observable as change
		notifyObservers(newSystemFileList);
		System.out.println("\nSET newSystemFileList: " + newSystemFileList);
	}
}
