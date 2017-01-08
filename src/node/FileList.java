package node;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import java.util.TreeMap;

public class FileList extends Observable implements Serializable {

    private TreeMap<String, Boolean> newSystemFileList;

    public FileList() {
        newSystemFileList = new TreeMap<String, Boolean>();
    }

    public TreeMap<String, Boolean> getFileList() {
        return newSystemFileList;
    }

    public void addLocalFileList(String file) {
        newSystemFileList.put(file, false);
        
        setChanged(); //mark the observable as change
        notifyObservers(newSystemFileList);
        System.out.println("\nSET newSystemFileList: " + newSystemFileList);
    }
}
