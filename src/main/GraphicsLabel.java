package main;

import javax.swing.JLabel;

/*
 * GraphicsLabel
 * 
 * Class for handling the "+"-labels in the calendar
 * 
 */

public class GraphicsLabel extends JLabel {

	/*
	 * Variables we need for this class
	 */
	
	private static final long serialVersionUID = 1L;
	private String time;
	
	/*
	 * Constructor
	 */
	
	public GraphicsLabel(String s) {
		super(s);
	}
	
	/*
	 * Getters and setters for the time-variable
	 */
	
	public void setTime(String t) {
		this.time = t;
	}
	
	public String getTime() {
		return this.time;
	}
}
