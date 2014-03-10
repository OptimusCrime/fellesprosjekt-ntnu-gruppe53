package main;

public class User {
	private Gui g;
	
	private String username;
	private String password;
	
	public User(Calendar c) {
		
		// Set initial data
		username = "";
		password = "";
	}
	
	public void setLogin(String u, String p) {
		username = u;
		password = p;
	}
	
	public String getLogin() {
		return "Derp";
	}
	
	public boolean doLogin() {
		return true;
	}
	
	public void getAppointments() {
		
	}
	
	public void addAppointment() {
		
	}
}
