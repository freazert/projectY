package node;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.server.*;
import interfaces.INodeAgentRMI;
import interfaces.INodeRMI;

import java.rmi.RemoteException;

public class AgentStarter extends UnicastRemoteObject implements INodeAgentRMI
{

	INodeRMI rmi;
	Node node;
	
	/**
	 * serializable version.
	 */
	private static final long serialVersionUID = 1L;

	public AgentStarter(Node node, INodeRMI rmi) throws RemoteException
	{
		this.node = node;
		this.rmi = rmi;
	}

	public FileListAgent startFileAgent(FileListAgent agent)
			throws RemoteException, MalformedURLException, NotBoundException
	{
		try
		{
			Thread.sleep(10000);
		} catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Thread agentThread = new Thread(agent);
		agentThread.start();
		System.out.println("File Agent started!");
		while (true)
		{
			// System.out.println("File Agent running!");
			if (!agentThread.isAlive())
			{
				System.out.println("agentThread is not alive");
				node.updateSystemList(agent.getnewSystemFileList());
				return agent;
			} else
			{
				System.out.println("\n\nagentThread is  alive");
				// System.out.println("next node:" + rmi.getIp(node.getNext()));
				if (node.getCurrent() != node.getNext())
				{
					System.out.println("node.getcurrent() !=  node.getNext !");
					int nextNode = node.getNext() + 10000;
					System.out.println("next node:" + nextNode);
					INodeAgentRMI nextAgentRmi = (INodeAgentRMI) Naming
							.lookup("//" + rmi.getIp(node.getNext()) + "/AgentStarter");
					System.out.println("START file agent, nextAgentRmi= "+nextAgentRmi);
					FileListAgent nextAgent = nextAgentRmi.startFileAgent(agent);
					System.out.println("File Agent moved to next node!");
					startFileAgent(nextAgent);

				}
				else{
					System.out.println("node.getcurrent() ==  node.getNext");
					System.out.println("node.getCurrent="+node.getCurrent());
					System.out.println("node.getNext="+node.getNext());
				}
			}
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public FileRecoveryAgent startFileRecoveryAgent(FileRecoveryAgent agent)
			throws RemoteException, MalformedURLException, NotBoundException
	{
		Thread agentThread = new Thread(agent);
		agentThread.start();
		System.out.println("Node failed! File Recovery Agent started!");
		while (true)
		{
			if (!agentThread.isAlive())
			{
				System.out.println("File Recovery Agent running!");
				return agent;
			} else
			{
				INodeAgentRMI nextAgentRmi = (INodeAgentRMI) Naming
						.lookup("//" + rmi.getIp(node.getNext()) + "/AgentStarter");
				FileRecoveryAgent nextAgent = nextAgentRmi.startFileRecoveryAgent(agent);
				System.out.println("File Recovery Agent moved to next node!");
				startFileRecoveryAgent(nextAgent);
			}
		}
	}
}
