package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
/**
 * 
 * View Class containing the actual GUI with wich the user interacts.
 * Extends JFrame so GUI elements can be added to it.
 *
 */
public class View extends JFrame{
	
	private Model model;
	JLabel label3 = new JLabel("ditIsEenLabel");
	private JButton openButton, removeButton, downloadButton;

	
	/**
	 * The constructor of the gui's VIEW ( V in MVC pattern ).
	 * Instanciates the JScrollPane that uses the Model's JList list in it's constructor, and other GUI elements like buttons.
	 * Also adds the elements (buttons and JScrollPane) to the JFrame
	 * 
	 * @param model
	 * 				the gui's Model (M in MVC), containing the JList list that is used in the JScrollFrame
	 */
	public View(Model model)
	{
		this.model = model;
		
		this.setBackground(Color.DARK_GRAY);
		this.setSize(600, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	

		//JPanel panel = new JPanel();
		JScrollPane pane = new JScrollPane(model.getList());
		openButton = new JButton("Open File");
		removeButton = new JButton("Remove File");
		downloadButton = new JButton("Download File");
		

		add(label3, BorderLayout.SOUTH); 
		add(pane, BorderLayout.NORTH);
		
		add(openButton, BorderLayout.EAST);
		add(removeButton, BorderLayout.WEST);
		

	}

	/**
	 * creates an addActionListener(ActionListener event) on the openButton. 
	 * This function is used in the controller.
	 * 
	 * @param event
	 * 				The click event on the openButton
	 */
	public void openButtonListener(ActionListener event)
	{
		this.openButton.addActionListener(event);
	}

	/**
	 * creates an addActionListener(ActionListener event) on the removeButton. 
	 * This function is used in the controller.
	 * 
	 * @param event
	 * 				The click event on the removeButton
	 */
	public void removeButtonListener(ActionListener event)
	{
		this.removeButton.addActionListener(event);
	}	
	
	/**
	 * creates an addActionListener(ActionListener event) on the dowloadButton. 
	 * This function is used in the controller.
	 * 
	 * @param event
	 * 				The click event on the downloadButton
	 */
	public void downloadButtonListener(ActionListener event)
	{
		this.downloadButton.addActionListener(event);
	}
	
	
}
