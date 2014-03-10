package main;

import java.util.ArrayList;

public class User {
	private Gui g;
	
	private boolean isLoggedIn;
	private String username;
	private String password;
	private ArrayList<Appointment> appointments;
	//private 
	
	public User(Gui g) {
		
		// Set initial data
		this.username = "";
		this.password = "";
	}
	
	public void setLogin(String u, String p) {
		this.username = u;
		this.password = p;
	}
	
	public String[] getLogin() {
		return new String[] {this.username, this.password};
	}
	
	public ArrayList<Appointment> getAppointments() {
		return this.appointments;
	}
	
	public void addAppointment(Appointment a) {
		this.appointments.add(a);
	}
	
	public void setLoggedIn(boolean l) {
		this.isLoggedIn = l;
	}
}
