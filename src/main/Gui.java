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
	
	private Calendar calendar;
	private ViewLogin login;
	private ViewMain main;
	
	/*
	 * Constructor
	 */
	
	public Gui (Calendar c) {
		// Set references
		this.calendar = c;
		
		// Display login-screen
		login = new ViewLogin(this, this.calendar);
		login.setVisible(true);
	}
	
	/*
	 * User is logged in, display home-screen
	 */
	
	public void showHome() {
		login.setVisible(false);
		login = null;
		main = new ViewMain(this, this.calendar);
		main.setVisible(true);
	}
	
	/*
	 * User wishes to log out, display login-screen again
	 */
	
	public void logout() {
		main.setVisible(false);
		main = null;
		login = new ViewLogin(this, this.calendar);
		login.setVisible(true);
	}
}
