package gui;

import java.io.File;

import javax.accessibility.AccessibleContext;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

public class Model {
	
	private DefaultListModel listModel = new DefaultListModel();
	JList list;

	public Model()
	{
		this.list = new JList(listModel);
		fillListModel();
	}

	public DefaultListModel getListModel() {
		return listModel;
	}

	public void setListModel(DefaultListModel listModel) {
		this.listModel = listModel;
	}
	
	public JList getList() {
		return list;
	}

	public void setList(JList list) {
		this.list = list;
	}
	
	

	public void fillListModel()
	{
		File directory = new File("c:/nieuwe map");
		File[] fList = directory.listFiles();

		if (fList != null && fList.length > 0)
		{
			for (File file : fList)
			{
				if (file.isFile())
				{
					listModel.addElement(file);
				}
			}
		}

	}

	public void emptyListModel()
	{
		listModel.removeAllElements();
	}
	
	public void refreshListModel()
	{
		emptyListModel();
		fillListModel();
	}
	
	
	

	public void openSelected(){
		File file = (File) list.getSelectedValue();
		try
		{
			Runtime.getRuntime()
					.exec(new String[] { "rundll32", "url.dll,FileProtocolHandler", file.getAbsolutePath() });

		} catch (Exception e)
		{
			e.printStackTrace(System.err);
		}
	}
	
	public void removeSelected(){
		File file = null;
		String path = null;
		file = (File) list.getSelectedValue();
		System.out.println("open file: "+file+"\t(absolute path: "+file.getAbsolutePath()+")");
		file.delete();
		listModel.removeElementAt(list.getSelectedIndex());
	}
	
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
