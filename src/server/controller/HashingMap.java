package server.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

//import marshal2.Jef;

public class HashingMap {
	private Hashing hash;
	
	public HashingMap(Hashing hash)
	{
		this.hash = hash;
		createHashMap();
	}
	
	private void createHashMap()
	{
		Map hashMap = new HashMap();
		
		hashMap.put(hash.getHash(), "192.168.1.1");
		
		System.out.println("size: " + hashMap.size());
		
		
		//File file = new File("D:/school/hashmap.xml");
		try {
		/*	JAXBContext jc = JAXBContext.newInstance( Array.class);
			Unmarshaller u = jc.createUnmarshaller();
			
			Marshaller m = jc.createMarshaller();
		       
			FileOutputStream fileOut =
			         new FileOutputStream("D:/school/hashmap.xml");
			//m.marshal(persoon, fileOut);
			String[] hashArr = hashMap.entrySet().toArray();
			m.marshal(hashMap,  
				fileOut);
			/*ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(persoon);
         	out.close();*/
         	/*fileOut.close();
         	System.out.println("Serialized data is saved in /tmp/employee.xml");
         	
         	Jef persoon2 = (Jef)u.unmarshal(new File("D:/school/sch-IW_EI/employee.xml"));
         	
         	System.out.println(persoon2.getName());
         	System.out.println(persoon2.getAge());
         	System.out.println(persoon2.getCapacity());	
	         
         	/*FileInputStream fileIn = 
	        		 new FileInputStream("D:/school/SCH-IW_EI/employee.ser");
         	ObjectInputStream oIn = new ObjectInputStream(fileIn);
         	Jef persoon2 = (Jef)oIn.readObject();
         	System.out.println(persoon2.getName());*/
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
