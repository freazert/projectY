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

public class Controller{
	
	private Model model;
	private View view;

	public Controller(View view, Model model)
	{
		this.model = model;
		this.view = view;
		
		view.openButtonListener(new openSelectionListener());
		view.removeButtonListener(new removeSelectionListener());
		view.downloadButtonListener(new downloadSelectionListener());
	}
	
	
	public void refreshListModel()
	{
		// this method needs to be fired with observer pattern > when files in network change (agent?)
		
		// only 1 instance of this gui/Controller class allowed
		// 		> instanciated from gui/GUI class
		//		> also only 1 instance of gui/GUI class allowed
		//				> instanciated from Node/Main Class
		
		model.refreshListModel();
	}
	

	class openSelectionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			model.openSelected();
		}
	}
	
	class removeSelectionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{	
			model.removeSelected();
		}
	}
	
	class downloadSelectionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			model.downloadSelected();
		}
	}
	
}
