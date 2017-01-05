package node;

import java.io.Serializable;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class FileRecoveryAgent implements Runnable, Serializable{
    private int failedNodeId;
    private int newNodeId;

    FileRecoveryAgent(int failedId, int newId){
        failedNodeId = failedId;
        newNodeId = newId;
    }

    @Override
    public void run() {
    }
}
