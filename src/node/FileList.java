package node;

import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;

public class FileList implements Serializable {
    private TreeMap<String, Boolean> newSystemFileList;
    
    
    public FileList(){ }
    
    public TreeMap<String, Boolean> getFileList()
    {
    	return newSystemFileList;
    }
    




    
    public void setFileList(Node node){
    	 List <String> local = node.getLocalList();
         List <String> owner = node.getOwnerList();
         for (int i = 0; i<local.size(); i++)
          {
        	  if(newSystemFileList!=null){
                  if(!newSystemFileList.containsKey(local.get(i)))
                  {
                      newSystemFileList.put(local.get(i), false);
                  }       		  
        	  }
        	  else{
        		  newSystemFileList = new TreeMap<String, Boolean>(); 
                  newSystemFileList.put(local.get(i), false);
        	  }
          }
          
          System.out.println("\nSET newSystemFileList: "+newSystemFileList);
    }
}
