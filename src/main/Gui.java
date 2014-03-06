package main;

/*
 * Gui
 * 
 * Takes care of drawing the different views etc
 * 
 */

public class Gui {
	
	/*
	 * Constructor
	 */
	
	public Gui () {
		// Debug-testing123
		ViewLogin login = new ViewLogin();
		login.setVisible(true);
		
		// Testing here aswell
		ViewMain main = new ViewMain();
		main.setVisible(true);
	}
}
