package gui;

public class GUI {
	
	public GUI() {
		
		Model model = new Model();
		View view = new View(model);
		Controller controller = new Controller(view, model);
		
		view.setVisible(true);

		
	}
	
	

}

