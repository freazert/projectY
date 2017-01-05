package node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.io.File;

public class FileListAgent implements Runnable, Serializable {
    private TreeMap<String, Boolean> newSystemFileList;
    Node node;

    public FileListAgent(Node node) {
        newSystemFileList = new TreeMap<>();
        this.node = node;
    }

    public void run() {
        List <String> local = node.getLocalList();
        List <String> owner = node.getOwnerList();

        for (int i = 0; i<local.size(); i++)
        {
            if(!newSystemFileList.containsKey(local.get(i)))
            {
                newSystemFileList.put(local.get(i), false);
            }
        }
    }

    public TreeMap<String, Boolean> getnewSystemFileList() {
        return newSystemFileList;
    }
}
