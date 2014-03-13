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
	
	private Cal calendar;
	private ViewLogin login;
	private ViewMain main;
	
	/*
	 * Constructor
	 */
	
	public Gui (Cal c) {
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
	
	/*
	 * This message is called when an object is changed
	 */
	
	public void reflectChange(String t, String f, Object o) {
		// Cast object
		CalendarObjects obj = (CalendarObjects)o;
		
		// Check if created
		if ((boolean) obj.isCreated()) {
			// Object is created, changes should be reflected now
			if (t.equals("user")) {
				// Dealing with a user
				if (f.equals("loaded-appointments")) {
					// Draw appointments
					main.drawAppointments();
				}
			}
		}
	}
	
	/*
	 * Delegate for displaying the error-message in the login-view
	 */
	
	public void sendLoginFailedMessage() {
		login.sendLoginFailedMessage();
	}
}
