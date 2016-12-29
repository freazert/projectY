package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;

public class GUIModel {
	
	public GUIModel(){
		
	}
	
	public void fillListModel(String dir, DefaultListModel model){
		 File directory = new File(dir);
		 File[] fList = directory.listFiles();

	     if(fList!=null&&fList.length>0){
			 for (File file : fList){
			      	if (file.isFile()){
			      		model.addElement(file);
			         }
			      }    	 
	     }

	}
	
	public void emptyListModel(){
		DefaultListModel model = new DefaultListModel();
		model.removeAllElements();
	}
	
	public void refreshList(String directory){
		emptyListModel();
		//fillListModel(directory);
	}
	 




}
