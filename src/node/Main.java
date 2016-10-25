package node;

import java.rmi.Naming;

import be.uantwerpen.rmiInterfaces.IBankAccount;

public class Main {
	public static void main(String args[])
	{
		IBankAccount obj = (IBankAccount) Naming.lookup( "//"+"localhost"+"/Bank");         //objectname in registry 
	}
}
