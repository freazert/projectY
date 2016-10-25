package server.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Main {

	public static void main(String[] args) {
		Hashing hash = new Hashing("YorickDeBock");
		HashingMap hmap;

		System.out.println(hash.getHash());
		System.out.println(hash.getName());

		try {
			hmap = xmlToObject();
			System.out.println(hmap.getIp(new Hashing("YorickDeBock")));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//new HashingMap();
		
		
		
		/*hmap.addRecord(new Hashing("jeroen"), "192.168.1.2");
		hmap.addRecord(new Hashing("Kris"), "192.168.1.3");
		/*try {
			objectToXml(hmap);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		// System.out.println(hmap.getIp(new Hashing("Kris")));

	}

	private static <T> void objectToXml(T object) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
		// StringWriter writerTo = new StringWriter();
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("D:/school/hashMap.xml");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(object, fileOut);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static HashingMap xmlToObject() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(HashingMap.class);
		// StringWriter writerTo = new StringWriter();
		FileOutputStream fileOut;

		// marshaller = jaxbContext.createMarshaller();
		Unmarshaller u = jaxbContext.createUnmarshaller();
		
		return (HashingMap) u.unmarshal(new File("D:/school/hashMap.xml"));

	}

}
