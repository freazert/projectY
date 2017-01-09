package gui;

import java.io.File;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import javax.accessibility.AccessibleContext;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

import node.FileList;

/**
 * 
 * Model Class responsible for handling the data.
 * Contains the logic that does action with the files and the Node, fired from the Controller.
 *
 */
public class Model implements Observer{

	private FileList fileList;
	private TreeMap<String, Boolean> treeMap;
	
	/**
	 * This DefaultListModel lists all the files in the network.
	 * This list of data is used in the JList's constructor, so when this data gets updated the JList gets updated to.
	 * The data in this DefaultListModel should be updated with the observer pattern (NEEDS TO BE IMPLEMENTED STILL)
	 * 
	 */
	private DefaultListModel listModel = new DefaultListModel();
	/**
	 * The actual list of data that is displayed in the view's JScrollPane.
	 * This JList gets instanciated with DefaultListModel listModel as constructor, and updated whenever the listModel gets updated.
	 */
	private JList list;

	
	/**
	 * The constructor of the gui's Model ( M in MVC pattern ).
	 * instanciates a Jlist with a DefaultListModel in it's constructor
	 * fills the listmodel with data from c/nieuwe map > SHOULD BE OBSERVING THE AGENT's FILELIST
	 */
	public Model(FileList fileList)
	{
		this.list = new JList(listModel);
		this.fileList = fileList;
		this.treeMap = new TreeMap<String,Boolean>();
		fillListModel();
	}
	
	
	
	@Override
	public
	void update(Observable o, Object arg){
		refreshListModel();
	}	
	

	/**
	 * Gets the DefaultListModel listModel data.
	 * 
	 * @return the DefaultListModel listModel
	 */
	public DefaultListModel getListModel() {
		return listModel;
	}

	/**
	 * Sets the Model's DefaultListModel listModel to param data.
	 * 
	 * @param listModel
	 * 					The new DefaultListModel data.
	 * 
	 */
	public void setListModel(DefaultListModel listModel) {
		this.listModel = listModel;
	}
	
	
	/**
	 * Gets the JList list data.
	 * 
	 * @return The JList list.
	 */
	public JList getList() {
		return list;
	}
	/**
	 * Sets the Model's JList list to param data.
	 * 
	 * @param list
	 * 				The new JList data.
	 */
	public void setList(JList list) {
		this.list = list;
	}
	
	
	/**
	 * Fills the DefaultListModel listModel.
	 * Currently with files from "c:/nieuwe map" > SHOULD BE FILLED WITH FILEFICHES FROM AGENT.
	 */
	public void fillListModel()
	{
		treeMap = fileList.getFileList();
		if(treeMap!=null){
			for(Map.Entry<String, Boolean> entry : treeMap.entrySet())
			{
				System.out.println("Key: "+entry.getKey()+". Value: " + entry.getValue());
				listModel.addElement(entry.getKey());

			}			
		}

		
		/*File directory = new File("c:/nieuwe map");
		File[] fList = FileList fileList.listFiles();

		if (fList != null && fList.length > 0)
		{
			for (File file : fList)
			{
				if (file.isFile())
				{
					listModel.addElement(file);
				}
			}
		}*/

	}

	/**
	 * Empties the DefaultListModel listModel.
	 */
	public void emptyListModel()
	{
		listModel.removeAllElements();
	}
	
	/**
	 * Refreshes the DefaultListModel listModel.
	 */
	public void refreshListModel()
	{
		emptyListModel();
		fillListModel();
	}
	
	
	
	/**
	 * Opens a selected file
	 */
	public void openSelected(){
		String filePath = "c:\\nieuwe map\\"+(String)list.getSelectedValue();
		File file = new File(filePath);
		if(file.exists())
		{
			try
			{
				Runtime.getRuntime()
						.exec(new String[] { "rundll32", "url.dll,FileProtocolHandler", file.getAbsolutePath() });

			} catch (Exception e)
			{
				e.printStackTrace(System.err);
			}		
		}
		else
		{
			System.out.println("\n\n\n File not available on this node. Implement DOWNLOAD first");
			// Agent.lock(file);
			//~~ File downloaded = Agent.download(file,nodeThatOwnsFile); //normale download zoals elke file, naar c:\nieuwe map voor checkFolderThread
			// Agent.unlock(file);
			
			//Runtime.getRuntime().exec(new String[] { "rundll32", "url.dll,FileProtocolHandler", file.getAbsolutePath() });

		}

	}
	
	/**
	 * Removes a selected file locally
	 */
	public void removeSelected(){
		String filePath = "c:\\nieuwe map\\"+(String)list.getSelectedValue();
		File file = new File(filePath);
		if(file.exists())
		{
			file.delete();
			listModel.removeElementAt(list.getSelectedIndex());			
		}
		else{
			System.out.println("\n\n\n File not available on this node. Implement  remote deleting of files (by agent?/owner?) first");
			
		}

	}
	
	/**
	 * SHOULD DOWNLOAD SELECTED FILE
	 */
	public void downloadSelected(){
		File _file = (File) list.getSelectedValue();
		int _index = list.getSelectedIndex();
		ListModel _listModel = list.getModel();
		System.out.println("DOWNLAD");
		System.out.println("file: "+ _file);
		System.out.println("index: "+ _index);
		System.out.println("listModel: "+ _listModel);
		
	}
	
	

	
	
}
