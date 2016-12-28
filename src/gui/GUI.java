package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame{
	public GUI(){
		
		super("title");
		setSize(600,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		GUIView gui_view = new GUIView();

		JPanel panel1 = gui_view.createJPanel("c:/test" , "Local Files");
		JPanel panel2 = gui_view.createJPanel("c:/test2", "Replicated Files");
		
				
		add(panel1, BorderLayout.CENTER);
		add(panel2, BorderLayout.SOUTH);	
		
	}

}
