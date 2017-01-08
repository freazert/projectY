package gui;

import java.util.TreeMap;

import node.FileList;
import node.FileListAgent;

/**
 * 
 * GUI Class containing the MVC instances 
 *
 */
public class GUI {
	/**
	 *	The GUI Constructor.
	 *	Instanciates 1 Model, View and Controller.
	 *  Should be instanciated once for every Node.
	 */
	public GUI(FileList fileList) {
		
		Model model = new Model(fileList);
		View view = new View(model);
		Controller controller = new Controller(view, model);
		
		view.setVisible(true);

		fileList.addObserver(model);
	}
	
	

}

