package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 
 * Controller Class contains the logic that connects the view and the model.
 * When a user clicks a button, the View will perform the necessary actions in the Model trhough This Controller.
 *
 */
public class Controller{
	
	/**
	 * The gui's View ( V in MVC).
	 */
	private View view;

	/**
	 * The gui's Model ( M in MVC ).
	 */
	private Model model;

	/**
	 * The constructor of the gui's Controller ( C in MVC pattern ).
	 * Instanciates the Model and the View.
	 * Also adds ButtonListener(new SelectionListener)s to the View's addActionListener(ActionListener event)s 
	 * 
	 * @param view
	 * 				The gui's View ( V in MVC pattern)
	 * @param model
	 * 				The gui's Model ( M in MVC pattern)
	 * 
	 */
	public Controller(View view, Model model)
	{
		this.model = model;
		this.view = view;
		
		view.openButtonListener(new openSelectionListener());
		view.removeButtonListener(new removeSelectionListener());
		view.downloadButtonListener(new downloadSelectionListener());
	}
	
	/**
	 * refreshes the Model's DefaultListModel listModel.
	 */
	public void refreshListModel()
	{
		// this method needs to be fired with observer pattern > when files in network change (agent?)
		
		// only 1 instance of this gui/Controller class allowed
		// 		> instanciated from gui/GUI class
		//		> also only 1 instance of gui/GUI class allowed
		//				> instanciated from Node/Main Class
		
		model.refreshListModel();
	}
	
	/**
	 * 
	 * Opens the selected file.
	 *
	 */
	class openSelectionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			model.openSelected();
		}
	}
	
	/**
	 * 
	 * Removes the selected file.
	 *
	 */
	class removeSelectionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{	
			model.removeSelected();
		}
	}
	
	/**
	 * 
	 * Downloads the selected file.
	 *
	 */
	class downloadSelectionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			model.downloadSelected();
		}
	}
	
}
