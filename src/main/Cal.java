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
		sh = new SocketHandler();
		st = new SocketTranslator();
	}

	public boolean testConnection(String s, int p) {
		return sh.connect(s, p);
	}
	
	public void setLogin(String u, String p) {
		this.user.setLogin(u, p);
	}
	
	public boolean doLogin() {
		JSONObject loginObj = this.initJSONObject("login", "put");
		String loginObjString = loginObj.toJSONString();
		
		//String derp = sh.sendMessageWithResponse(loginObjString, "login/put");
		
		user.setLoggedIn(true);
		
		this.loadAppointments();
		
		return true;
	}
	
	private void loadAppointments () {
		JSONObject appointmentObj = this.initJSONObject("appointment", "get");
		String loginObjString = appointmentObj.toJSONString();
		
		sh.sendMessage(loginObjString);
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject initJSONObject(String a, String t) {
		JSONObject tempObj = new JSONObject();
		tempObj.put("method", "request");
		tempObj.put("action", a);
		tempObj.put("type", t);
		
		String[] userLogin = user.getLogin();
		JSONObject loginObj = new JSONObject();
		loginObj.put("username", userLogin[0]);
		loginObj.put("password", userLogin[1]);
		tempObj.put("login", loginObj);
		
		// Return
		return tempObj;
	}
}
