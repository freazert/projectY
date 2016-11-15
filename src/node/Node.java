package node;

public class Node {
	private int nextNode, prevNode, myNode;
	
	public Node()
	{
		this.myNode = 5;
		this.nextNode = 3;
		this.prevNode = 8;
	}
	
	public void setNodes(String name)
	{
		setPrev(1);
		setNext(1);
	}
	
	private void setPrev(int hash)
	{
		if ((hash > this.prevNode && hash < this.myNode) || 
				( this.myNode < this.prevNode && hash > this.prevNode) ){
			this.prevNode = hash;
		}
	}
	
	private void setNext(int hash)
	{
		if ((hash < this.nextNode && hash > this.myNode)||
				( this.myNode > this.nextNode && hash < this.nextNode)){
			this.nextNode = hash;
		}
	}
	
	

}
