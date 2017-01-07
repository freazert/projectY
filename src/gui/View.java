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

public class View extends JFrame{
	
	private Model model;
	JLabel label3 = new JLabel("ditIsEenLabel");
	private JButton openButton, removeButton, downloadButton;

	
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

	public void openButtonListener(ActionListener event)
	{
		this.openButton.addActionListener(event);
	}

	public void removeButtonListener(ActionListener event)
	{
		this.removeButton.addActionListener(event);
	}	
	
	public void downloadButtonListener(ActionListener event)
	{
		this.downloadButton.addActionListener(event);
	}
	
	
}
