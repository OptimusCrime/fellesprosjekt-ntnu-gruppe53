package main;

import java.math.BigDecimal;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/*
 * Cal(endar)
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
	
	/*
	 * Constructor
	 */
	
	public Cal() {
		// Create new instance of the Gui-class
		this.gui = new Gui(this);
		
		// User
		this.user = new User(this.gui);
		
		// Sockets
		this.sh = new SocketHandler(this);
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
	
	/*
	 * This method takes care of incoming messages from the socket
	 */
	
	public void handleIncoming(String r) {
		// Decode json
		JSONObject requestObj = (JSONObject)JSONValue.parse(r);
		
		// Exstract the different action and types
		String action = (String) requestObj.get("action");
		String type = (String) requestObj.get("type");
		
		// Change according to the different incoming types here
		if (action.equals("appointment")) {
			// We're dealing with an appointment
			if (type.equals("get")) {
				// The request is of the type get
				
				// Parse to array
				JSONArray appointments = (JSONArray) requestObj.get("data");
				
				// Loop all the appointments
				for (int i = 0; i < appointments.size(); i++) {
					JSONObject thisAppointment = (JSONObject) appointments.get(i);
					
					// Create new appointment
					Appointment a = new Appointment(this.gui);
					
					// Set each field (TODO)
					a.setId(new BigDecimal((long) thisAppointment.get("id")).intValueExact());
					a.setTitle((String) thisAppointment.get("title"));
					a.setDescription((String) thisAppointment.get("description"));
					a.setStart(new Date());
					a.setEnd(new Date());
					a.setPlace("Place");
					a.setRoom(new Room(this.gui));
					a.setParticipates(true);
					a.setHide(true);
					a.setAlarm(false);
					a.setAlarmTime(new Date());
					
					// Create the object
					a.create();
					
					// Add appointment to user
					this.user.addAppointment(a);
				}
			}
		}
	}
}
