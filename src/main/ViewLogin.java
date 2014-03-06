package main;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ViewLogin extends JPanel {
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	
	public ViewLogin() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("NTNU Calendar - Login");
		frame.setPreferredSize(new Dimension(500, 300));
		frame.pack();
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);	
	}
}
