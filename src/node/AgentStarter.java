package node;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.server.*;
import interfaces.INodeAgentRMI;
import interfaces.INodeRMI;

import java.rmi.RemoteException;

public class AgentStarter extends UnicastRemoteObject implements INodeAgentRMI{

    INodeRMI rmi;
    Node node;

    public AgentStarter(Node node) throws RemoteException {
        this.node = node;
    }

    public FileListAgent startFileAgent(FileListAgent agent) throws RemoteException, MalformedURLException, NotBoundException{
        Thread agentThread = new Thread(agent);
        agentThread.start();
        System.out.println("File Agent started!");
        while(true) {
            System.out.println("File Agent running!");
            if (!agentThread.isAlive()) {
                node.updateSystemList(agent.getnewSystemFileList());
                return agent;
            }
            else {
                INodeAgentRMI nextAgentRmi = (INodeAgentRMI) Naming.lookup("//"+rmi.getIp(node.getNext())+"/AgentStarter");
                FileListAgent nextAgent = nextAgentRmi.startFileAgent(agent);
                System.out.println("File Agent moved to next node!");
                startFileAgent(nextAgent);
            }
        }
    }


    public FileRecoveryAgent startFileRecoveryAgent(FileRecoveryAgent agent) throws RemoteException, MalformedURLException, NotBoundException{
        Thread agentThread = new Thread(agent);
        agentThread.start();
        System.out.println("Node failed! File Recovery Agent started!");
        while(true) {
            if (!agentThread.isAlive()) {
                System.out.println("File Recovery Agent running!");
                return agent;
            }
            else {
                INodeAgentRMI nextAgentRmi = (INodeAgentRMI) Naming.lookup("//"+rmi.getIp(node.getNext())+"/AgentStarter");
                FileRecoveryAgent nextAgent = nextAgentRmi.startFileRecoveryAgent(agent);
                System.out.println("File Recovery Agent moved to next node!");
                startFileRecoveryAgent(nextAgent);
            }
        }
    }
}
