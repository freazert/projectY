package node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.io.File;

public class FileListAgent implements Runnable, Serializable {
    private TreeMap<String, Boolean> newSystemFileList;
    private FileList fileList;

    public FileListAgent(FileList fileList) {
        this.newSystemFileList = new TreeMap<>();
        this.fileList = fileList;
    }

    public void run() {
    	newSystemFileList = fileList.getFileList();
    	//System.out.println("\n\nRUN getFileList()>"+newSystemFileList);

    }

    public TreeMap<String, Boolean> getnewSystemFileList() {
    	//System.out.println("\n\ngetnewSystemFileList()>"+newSystemFileList);
        return newSystemFileList;
    }
}
