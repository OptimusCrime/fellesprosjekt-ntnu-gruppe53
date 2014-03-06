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
	private ViewLogin login;
	private ViewMain main;
	
	/*
	 * Constructor
	 */
	
	public Gui (Program p, Calendar c) {
		// Set references
		this.p = p;
		this.c = c;
		
		// Display login-screen
		login = new ViewLogin(this);
		login.setVisible(true);
	}
	
	
	/*
	 * Testing connection entered during login
	 */
	
	public boolean testConnection(String s, int port) {
		return p.testConnection(s, port);
	}
	
	/*
	 * User is logged in, display home-screen
	 */
	
	public void showHome() {
		login.setVisible(false);
		login = null;
		main = new ViewMain(this);
		main.setVisible(true);
	}
	
	/*
	 * User wishes to log out, display login-screen again
	 */
	
	public void logout() {
		main.setVisible(false);
		main = null;
		login = new ViewLogin(this);
		login.setVisible(true);
		
	}
}
