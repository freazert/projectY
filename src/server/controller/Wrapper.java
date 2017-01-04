package server.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Wrapper
{
	/**
	 * The Hashmap which holds the (key, value) pair (int nodeHash, String ip)
	 */
	private HashingMap hmap;

	/**
	 * The filename used for the xml file
	 */
	private static String FILE_NAME = "hashmap.xml";

	/**
	 * The constructor method for the Wrapper.
	 */
	public Wrapper()
	{
		try
		{
			hmap = new HashingMap();
			this.hmap = xmlToObject();
			System.out.println("start up");

		} catch (JAXBException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Remove node from the hashmap.
	 * 
	 * @param name
	 *            the hash of the node that needs to be removed
	 * @return boolean. returns 1 on success, 0 on failure
	 */
	public int removeNode(int name)
	{
		int success = this.hmap.removeRecord(name);
		try
		{
			objectToXml();

			return success;
		} catch (JAXBException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

			return 0;
		}
	}

	/**
	 * Convert object to XML string.
	 * 
	 * @throws JAXBException
	 *             Something went wrong while marshalling this object.
	 */
	private void objectToXml() throws JAXBException
	{
		HashingMap object = this.hmap;
		JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
		// StringWriter writerTo = new StringWriter();
		FileOutputStream fileOut;
		try
		{
			fileOut = new FileOutputStream(FILE_NAME);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(object, fileOut);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Add new node to the hashmap.
	 * 
	 * @param name
	 *            the name of the new node.
	 * @param ip
	 *            the ip of the new node.
	 * @return the status of creating the node. returns 1 on success, 0 on
	 *         failure.
	 */
	public int createNode(String name, String ip)
	{
		try
		{
			int success = this.hmap.addRecord(new Hashing(name), ip);
			objectToXml();

			return success;
		} catch (JAXBException e)
		{
			e.printStackTrace();

			return 0;
		}
	}

	/**
	 * Convert XML string to hashmap.
	 * 
	 * @return the HashingMap object.
	 * @throws JAXBException
	 *             There went something wrong while marshalling the class
	 */
	private HashingMap xmlToObject() throws JAXBException
	{
		JAXBContext jaxbContext = JAXBContext.newInstance(HashingMap.class);
		Unmarshaller u = jaxbContext.createUnmarshaller();
		this.hmap = new HashingMap();

		try
		{
			File f = new File(FILE_NAME);
			f.createNewFile();
			this.hmap = new HashingMap();
			objectToXml();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return (HashingMap) u.unmarshal(new File(FILE_NAME));

	}

	/**
	 * Getter method for the hashmap.
	 * 
	 * @return the current hashmap.
	 */
	public HashingMap getHashMap()
	{
		return this.hmap;
	}

}
