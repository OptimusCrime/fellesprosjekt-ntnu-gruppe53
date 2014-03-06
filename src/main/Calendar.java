package main;

/*
 * Calendar
 * 
 * Takes care of handling user-state, handles GUI etc
 * 
 */

public class Calendar {
	
	/*
	 * Variables
	 */
	
	private Gui g;
	private String server;
	private int port;
	
	/*
	 * Constructor
	 */
	
	public Calendar() {
		// Create new instance of the Gui-class
		g = new Gui();
	}
}
