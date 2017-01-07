package gui;

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
	public GUI() {
		
		Model model = new Model();
		View view = new View(model);
		Controller controller = new Controller(view, model);
		
		view.setVisible(true);

		
	}
	
	

}

