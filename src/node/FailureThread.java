/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package node;

import interfaces.INodeRMI;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Thread that checks if the next node is still reachable. it pings every
 * 10s to the next node and when the node doesn't reply runs the failure
 * sequence.
 */
public class FailureThread extends Thread
{
	/**
	 * The node that runs the project
	 */
	private Node node;
	/**
	 * The remote method invocation object that invokes methods on the server.
	 */
	private INodeRMI rmi;

	/**
	 * The constructor of the FailureThread.
	 * 
	 * @param node The node that runs the project.
	 * @param rmi the remote method invocation object that invokes methds on the server.
	 */
	public FailureThread(Node node, INodeRMI rmi)
	{
		this.node = node;
		this.rmi = rmi;
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				if (!node.isShuttingDown)
				{
					int nextNode = node.getNext();
					InetAddress inet = InetAddress.getByName(rmi.getIp(nextNode));
					boolean reachable = inet.isReachable(10000);
					System.out.print(reachable);

					if (!reachable)
					{
						node.failure(nextNode);
					}

					try
					{
						Thread.sleep(10000);
					} catch (InterruptedException ex)
					{
						Logger.getLogger(FailureThread.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			} catch (IOException ex)
			{
				Logger.getLogger(FailureThread.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
	}

}
