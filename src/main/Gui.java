package main;

import java.util.ArrayList;

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
		// Check what changes to reflect over :)
		if (t.equals("employees")) {
			// Get self id
			String username = this.calendar.getUsername();
			
			// Check if the user was returned
			if (username != null) {
				// Remove self
				ArrayList<Employee> tempEmployeeList = calendar.getEmployees();
				
				// Loop all employees
				for (int i = 0; i < tempEmployeeList.size(); i++) {
					// Check if the current user is the one being looped
					if (tempEmployeeList.get(i).getEmail().equals(username)) {
						// Get the current user
						Employee employeeThisUser = tempEmployeeList.get(i);
						
						// Set checked to true
						employeeThisUser.setChecked(true);
						
						// Remove from the list
						employeeThisUser.setIsCurrentUser(true);
						
						// Break the loop
						break;
					}
				}
			}
			
			// Special case, this is not a part of the CalendarObjects-family
			main.drawEmployees(calendar.getEmployees());
		}
		else {
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
	}
	
	/*
	 * Delegate for displaying the error-message in the login-view
	 */
	
	public void sendLoginFailedMessage() {
		login.sendLoginFailedMessage();
	}
}
