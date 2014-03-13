package main;

import java.util.ArrayList;

/*
 * User
 * 
 * Holds the actual User-object. Used to fetch all sorts of data
 * 
 */

public class User implements CalendarObjects {
	
	/*
	 * Variables etc
	 */
	
	private Gui gui;
	private boolean created;
	
	private boolean isLoggedIn;
	private String username;
	private String password;
	private ArrayList<Appointment> appointments;
	private ArrayList<Notification> notifications;
	
	/*
	 * Constructor
	 */
	
	public User(Gui g) {
		// Set reference to gui
		this.gui = g;
		this.created = false;
		
		// Set initial data
		this.username = "";
		this.password = "";
		
		// Init arrayLists
		this.appointments = new ArrayList<Appointment>();
		this.notifications = new ArrayList<Notification>();
	}
	
	/*
	 * Create & reflect
	 */
	
	public void create() {
		this.created = true;
		this.gui.reflectChange("user", "create", this);
	}
	
	public boolean isCreated() {
		return this.created;
	}
	
	public void sendReflect(String r) {
		this.gui.reflectChange("user", r, this);
	}
	
	/*
	 * Public setter for login
	 */
	
	public void setLogin(String u, String p) {
		this.username = u;
		this.password = p;
	}
	
	/*
	 * Public getter for login
	 */
	
	public String[] getLogin() {
		return new String[] {this.username, this.password};
	}
	
	/*
	 * Get all appointments this user has
	 */
	
	public ArrayList<Appointment> getAppointments() {
		return this.appointments;
	}
	
	/*
	 * Add new appointment for this user
	 */
	
	public void addAppointment(Appointment a) {
		this.appointments.add(a);
	}
	
	/*
	 * Set logged-in value
	 */
	
	public void setLoggedIn(boolean l) {
		this.isLoggedIn = l;
	}
}
