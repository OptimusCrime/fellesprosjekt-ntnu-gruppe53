package main;

/*
 * Gui
 * 
 * Takes care of drawing the different views etc
 * 
 */

public class Gui {
	
	/*
	 * Variables
	 */
	
	private Program p;
	private Calendar c;
	
	/*
	 * Constructor
	 */
	
	public Gui (Program p, Calendar c) {
		// Set references
		this.p = p;
		this.c = c;
		
		// Debug-testing123
		ViewLogin login = new ViewLogin(this);
		login.setVisible(true);
		
		// Testing here aswell
		//ViewMain main = new ViewMain();
		//main.setVisible(true);
	}
	
	public boolean testConnection(String s, int port) {
		return p.testConnection(s, port);
	}
}
