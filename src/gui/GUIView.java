package gui;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GUIView {
	
	public GUIView(){
		
	}
	

	
	public File getFileElementFromList(JList list){
		return (File) list.getSelectedValue();
	}
	
	public void removeFileElementFromList(JList list, DefaultListModel model){
        model.removeElementAt(list.getSelectedIndex() );
	}
	
	public JPanel createJPanel(String dir, String type){
		 JPanel panel = new JPanel();
		 DefaultListModel model = new DefaultListModel();
		 JList list = new JList(model);
		 JScrollPane pane = new JScrollPane(list);
		 JButton openButton = new JButton("Open File");
		 JButton removeButton = new JButton("Remove File");
		 File directory = new File(dir);
		  JLabel label3 = new JLabel(type);

		 
	     GUIModel gui_model = new GUIModel();
		 File[] fList = directory.listFiles();
		 gui_model.fillListModel(fList, model);
		 
		 openButton.addActionListener(new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
		    	  //File file = (File) model.getElementAt(list.getSelectedIndex()); werkt ook
		    	  File file = (File) list.getSelectedValue();
		    	  openFile(file);
	    	 }
		  });
		    
		  removeButton.addActionListener(new ActionListener() {//removes it from jlist not pc
			  public void actionPerformed(ActionEvent e) {
				  File file=null;
		    	  String path=null;
		          file = (File) list.getSelectedValue();
		          file.delete();
		          model.removeElementAt(list.getSelectedIndex() );
		        
			  }
		  });

		  panel.add(label3, BorderLayout.SOUTH);
		  panel.add(pane, BorderLayout.NORTH);
		  panel.add(openButton, BorderLayout.EAST);
		  panel.add(removeButton, BorderLayout.WEST);	
		 return panel;
		 
	       
	}
	
	public boolean openFile(File file)	{
	    try {
	    	Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler",
	    			file.getAbsolutePath()});
	        return true;
	        
	    } 
	    catch (Exception e) {
	        e.printStackTrace(System.err);
	        return false;
	    }
	}
}
