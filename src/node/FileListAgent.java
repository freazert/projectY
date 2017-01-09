package node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.TreeMap;
import java.io.File;

/**
 * The agent that passes the file list to all the nodes.
 */
public class FileListAgent implements Runnable, Serializable
{
	/**
	 * A Treemap representation of the list of files present in the system.
	 */
	private TreeMap<String, Boolean> newSystemFileList;
	/**
	 * A list of files present in the system
	 */
	private FileList fileList;

	/**
	 * The constructor method for the file list agent.
	 * 
	 * @param fileList
	 *            A list of all the files in the system.
	 */
	public FileListAgent(FileList fileList)
	{
		this.newSystemFileList = new TreeMap<>();
		this.fileList = fileList;
	}

	@Override
	public void run()
	{
		newSystemFileList = fileList.getFileList();
	}

	/**
	 * Get the treemap representation of all files present in the system.
	 * 
	 * @return a treemap representation of the file list.
	 */
	public TreeMap<String, Boolean> getnewSystemFileList()
	{
		return newSystemFileList;
	}
}
