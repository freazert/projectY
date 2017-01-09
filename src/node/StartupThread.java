package node;

import interfaces.INodeRMI;

/**
 * Startup the basics for running the node.
 */
public class StartupThread extends Thread
{
	/**
	 * The remote method invocation object to connect to the server.
	 */
	private INodeRMI rmi;
	/**
	 * The node that is running the project.
	 */
	private Node node;
	/**
	 * The object that maintains all socket information.
	 */
	private SocketHandler sHandler;

	/**
	 * The constructor method for the startup thread
	 * 
	 * @param rmi
	 *            the remote method invocation object to connect to the server.
	 * @param node
	 *            The node thatis runing the project.
	 * @param sHandler
	 *            The object that maintains all socket information.
	 */
	public StartupThread(INodeRMI rmi, Node node, SocketHandler sHandler)
	{
		this.sHandler = sHandler;
		this.rmi = rmi;
		this.node = node;
	}

	@Override
	public void run()
	{
		ReceiveUDPThread rft = new ReceiveUDPThread(this.node, this.sHandler);
		rft.start();

		new CheckFolderThread(this.node, 10000, this.node.getFolderString()).start();
		new FailureThread(this.node, rmi).start();
	}
}