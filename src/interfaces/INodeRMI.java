package interfaces;

import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface INodeRMI extends Remote
{
	/**
	 * Get the hash of the node with given name.
	 * 
	 * @param userName
	 *            the name of the node that needs to be hashed.
	 * @return
	 * @throws RemoteException
	 */
	int getCurrent(String userName) throws RemoteException;

	/**
	 * Get the node with the hash before the hash of the given name.
	 * 
	 * @param userName
	 *            the name of the node after the one requested.
	 * @return
	 * @throws RemoteException
	 */
	int getPrevious(String userName) throws RemoteException;

	/**
	 * Get the hash of the node after the hash of the given name.
	 * 
	 * @param userName
	 *            the hash of the name before the one requested
	 * @return
	 * @throws RemoteException
	 */
	int getNext(String userName) throws RemoteException;

	/**
	 * Get the IP of the node who is the owner of the file.
	 * 
	 * @param name
	 *            the name of the file.
	 * @return
	 * @throws RemoteException
	 */
	//String getFileNode(String name) throws RemoteException;

	/**
	 * Get the IP address corresponding to the given hash
	 * 
	 * @param hash
	 *            the hash of the searched node.
	 * @return
	 * @throws RemoteException
	 */
	String getIp(int hash) throws RemoteException;

	/**
	 * Create a new node.
	 * 
	 * @param name
	 *            the name of the new node.
	 * @return
	 * @throws RemoteException
	 * @throws UnknownHostException 
	 * @throws ServerNotActiveException 
	 */
	int createNode(String name) throws RemoteException, ServerNotActiveException;

	/**
	 * Remove a node.
	 * 
	 * @param name
	 *            the name of the node that has to be removed
	 * @return
	 * @throws RemoteException
	 */
	int removeNode(int name) throws RemoteException;

	/**
	 * Get the hash from the given name.
	 * 
	 * @param name
	 *            the name that needs to be hashed
	 * @return
	 * @throws RemoteException
	 */
	int getHash(String name) throws RemoteException;

	/**
	 * Get the file owner.
	 * 
	 * @param filename
	 *            the ip of the node before the filename (file owner)
	 * @return
	 * @throws RemoteException
	 */
	String getPrevIp(String filename) throws RemoteException;

	/**
	 * Get number of records in the hashmap.
	 * 
	 * @return
	 * @throws RemoteException
	 */
	int getHmapSize() throws RemoteException;

	int getPrevious(int hash) throws RemoteException;

	int getFileNode(int hash) throws RemoteException;

	int getNext(int hash) throws RemoteException;

	String getFileIp(String name) throws RemoteException;

}
