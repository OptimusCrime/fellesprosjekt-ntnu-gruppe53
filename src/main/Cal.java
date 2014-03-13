package main;

import org.json.simple.JSONObject;

/*
 * Calendar
 * 
 * Takes care of handling user-state, handles GUI etc
 * 
 */

public class Cal {
	
	/*
	 * Variables
	 */
	
	private Gui gui;
	private User user;
	
	private SocketHandler sh;
	private SocketTranslator st;
	
	/*
	 * Constructor
	 */
	
	public Cal() {
		// Create new instance of the Gui-class
		this.gui = new Gui(this);
		
		// User
		this.user = new User(this.gui);
		
		// Sockets
		this.sh = new SocketHandler();
		this.st = new SocketTranslator();
	}
	
	/*
	 * Method to test connection to the server
	 */
	
	public boolean testConnection(String s, int p) {
		return this.sh.connect(s, p);
	}
	
	/*
	 * Method that sets the login-creditials in the user-object
	 */
	
	public void setLogin(String u, String p) {
		this.user.setLogin(u, p);
	}
	
	/*
	 * Method that does the actual login (TOOD this is just a test for now)
	 */
	
	public boolean doLogin() {
		JSONObject loginObj = this.initJSONObject("login", "put");
		String loginObjString = loginObj.toJSONString();
		
		//String derp = sh.sendMessageWithResponse(loginObjString, "login/put");
		
		this.user.setLoggedIn(true);
		
		this.loadAppointments();
		
		// Always returns true for testing
		return true;
	}
	
	/*
	 * Delegate for loading all appointments
	 */
	
	private void loadAppointments () {
		JSONObject appointmentObj = this.initJSONObject("appointment", "get");
		String loginObjString = appointmentObj.toJSONString();
		
		sh.sendMessage(loginObjString);
	}
	
	/*
	 * Initilize new JSONObject that appends the correct method, action, type and login-information
	 */
	
	@SuppressWarnings("unchecked")
	private JSONObject initJSONObject(String a, String t) {
		// New JSONObject
		JSONObject tempObj = new JSONObject();
		
		// Set the correct type/request-data
		tempObj.put("method", "request");
		tempObj.put("action", a);
		tempObj.put("type", t);
		
		// Append the login-information
		String[] userLogin = user.getLogin();
		JSONObject loginObj = new JSONObject();
		loginObj.put("username", userLogin[0]);
		loginObj.put("password", userLogin[1]);
		tempObj.put("login", loginObj);
		
		// Return
		return tempObj;
	}
}
