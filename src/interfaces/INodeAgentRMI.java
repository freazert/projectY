package interfaces;

import node.FileListAgent;
import node.FileRecoveryAgent;
import node.Node;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INodeAgentRMI extends Remote{

    /**
     * Start de File List Agent.
     *
     * @param agent
     *            the agent that needs to be started.
     * @return
     * @throws RemoteException, MalformedURLException, NotBoundException
     */
    FileListAgent startFileAgent(FileListAgent agent) throws RemoteException, MalformedURLException, NotBoundException;

    /**
     * Start de File Recovery Agent.
     *
     * @param agent
     *            the agent that needs to be started.
     * @return
     * @throws RemoteException, MalformedURLException, NotBoundException
     */
    FileRecoveryAgent startFileRecoveryAgent(FileRecoveryAgent agent) throws RemoteException, MalformedURLException, NotBoundException;
}
