package main;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * ViewLogin
 * 
 * The loginview
 * 
 */

public class ViewLogin extends JPanel {
	
	/*
	 * Variables
	 */
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	
	/*
	 * Constructor
	 */
	
	public ViewLogin() {
		// Init new frame
		frame = new JFrame();
		
		// Set close-operation
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set title
		frame.setTitle("NTNU Calendar - Login");
		
		// Set size
		frame.setPreferredSize(new Dimension(500, 300));
		
		// Pack everything
		frame.pack();
	}
	
	/*
	 * Public setter for setting visible state
	 */
	
	public void setVisible(boolean b) {
		frame.setVisible(b);	
	}
}
