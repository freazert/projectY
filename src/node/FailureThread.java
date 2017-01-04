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
 *
 * @author Kris
 */
public class FailureThread extends Thread
{

	private Node node;
	private INodeRMI nodeRMI;

	public FailureThread(Node node, INodeRMI nodeRMI)
	{
		this.node = node;
		this.nodeRMI = nodeRMI;
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				int nextNode = node.getNext();
				InetAddress inet = InetAddress.getByName(nodeRMI.getIp(nextNode));
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
			} catch (IOException ex)
			{
				Logger.getLogger(FailureThread.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
	}

}
