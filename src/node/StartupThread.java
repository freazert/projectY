package node;

import java.io.File;
import java.util.List;

import interfaces.INodeRMI;

public class StartupThread extends Thread {

    private List<File> files;
    private INodeRMI rmi;
    private Node node;

    public StartupThread( INodeRMI rmi, Node node) {
        this.files = files;
        this.rmi = rmi;
        this.node = node;
    }

    @Override
    public void run() {
        //SendFileThread sft = new SendFileThread(files, rmi, node);
        //sft.start();
          //  sft.join();
                new CheckFolderThread(this.node, 10000, this.node.getFolderString()).start();
                ReceiveUDPThread rft = new ReceiveUDPThread(this.node, this.node.getServerSocket());
                rft.start();
    }

}
