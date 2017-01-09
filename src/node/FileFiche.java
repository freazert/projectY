package node;

import java.util.ArrayList;
import java.util.List;

/**
 * The representation of a file in the system Y.
 */
public class FileFiche
{
	/**
	 * The name of the file
	 */
	private String fileName;
	/**
	 * A list with all the locations (the hash of the node) where the file is
	 * present
	 */
	private List<Integer> locationList = new ArrayList<>();
	/**
	 * The hash of the owner
	 */
	private int ownerId;
	/**
	 * The ip address of the owner
	 */
	private String ownerIp;

	/**
	 * The constructor method for the file fiches.
	 * 
	 * @param name
	 *            the name of the file.
	 * @param id
	 *            the hash of the owner.
	 * @param ip
	 *            the IP of the owner.
	 */
	public FileFiche(String name, int id, String ip)
	{
		this.fileName = name;
		this.ownerId = id;
		this.ownerIp = ip;

		locationList.add(id);
		System.out.println("Filefiche" + locationList);
		System.out.println("Filefiche" + name);
		System.out.println("Filefiche" + id);
		System.out.println("Filefiche" + ip);
	}

	/**
	 * Add location to the location list
	 * 
	 * @param id
	 *            the hash of the owner.
	 */
	public void addLocation(int id)
	{
		locationList.add(id);
	}

	/**
	 * remove location from the location list
	 * 
	 * @param id
	 *            the hash of the owner.
	 */
	public void rmLocation(int id)
	{
		for (int i = 0; i < locationList.size(); i++)
		{
			if (locationList.get(i).equals(id))
				locationList.remove(i);
		}
	}

}
