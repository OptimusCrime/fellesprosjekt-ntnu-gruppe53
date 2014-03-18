package main;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
	
	private ArrayList<Employee> employees;
	
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
		
		// Init list of employees
		this.employees = new ArrayList<Employee>();
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
	 * Sends login-details to the server
	 */
	
	public void doLogin() {
		JSONObject loginObj = this.initJSONObject("login", "put");
		String loginObjString = loginObj.toJSONString();
		
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
		// DEBUG TODO
		System.out.println("Got this = " + r);
		
		// Decode json
		JSONObject requestObj = (JSONObject)JSONValue.parse(r);
		
		// Exstract the different action and types
		try {
			String action = (String) requestObj.get("action");
			String type = (String) requestObj.get("type");
			
			// Change according to the different incoming types here
			if (action.equals("appointments")) {
				// We're dealing with an appointment
				if (type.equals("get")) {
					// Get all appointments
					JSONArray appointments = (JSONArray) requestObj.get("data");
					
					// Check that we actually got someting back
					if (appointments != null) {
						// Loop all the appointments
						for (int i = 0; i < appointments.size(); i++) {
							JSONObject thisAppointment = (JSONObject) appointments.get(i);
							
							// Create new appointment
							Appointment a = new Appointment(this.gui);
							
							// Set each field
							a.setId(new BigDecimal((long) thisAppointment.get("id")).intValueExact());
							a.setTitle((String) thisAppointment.get("title"));
							a.setDescription((String) thisAppointment.get("description"));
							
							a.setStart(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse((String) thisAppointment.get("start")));
							a.setEnd(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse((String) thisAppointment.get("end")));
							
							a.setPlace((String) thisAppointment.get("place"));
							a.setParticipates((boolean) thisAppointment.get("participate"));
							a.setHide((boolean) thisAppointment.get("hide"));
							a.setAlarm((boolean) thisAppointment.get("alarm"));
							a.setAlarmTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse((String) thisAppointment.get("alarm_time")));
							
							// Create the object
							a.create();
							
							// Add appointment to user
							this.user.addAppointment(a);
						}
						
						// Send reflect to the gui from the user-class
						this.user.sendReflect("loaded-appointments");
					}
				}
			}
			else if (action.equals("login")) {
				// Login
				if (type.equals("put")) {
					// Get the code
					int code = new BigDecimal((long) requestObj.get("code")).intValueExact();
					
					// Check what code was returned
					if (code == 200) {
						// Login sucessful
						this.user.setLoggedIn(true);
						this.user.create();
						
						// Show home
						this.gui.showHome();
						
						// Load all stuff the user needs
						this.loadAppointments();
						this.loadEmployees();
					}
					else {
						// Send error-message
						this.gui.sendLoginFailedMessage();
					}
				}
			}
			else if (action.equals("employees")) {
				// We're dealing with the list of employees
				if (type.equals("get")) {
					// Get all employees
					JSONArray employees = (JSONArray) requestObj.get("data");
					
					// Check that we actually got someting back
					if (employees != null) {
						// Loop all the employees
						for (int i = 0; i < employees.size(); i++) {
							JSONObject thisAppointment = (JSONObject) employees.get(i);
							
							// Create new employee
							Employee e = new Employee(new BigDecimal((long) thisAppointment.get("id")).intValueExact(),
									(String) thisAppointment.get("email"),
									(String) thisAppointment.get("name"));
							
							// Create the object
							//a.create();
							
							// Add appointment to user
							this.employees.add(e);
						}
						
						// Send reflect to the gui from this class
						this.gui.reflectChange("employees", "create", null);
					}
				}
			}
		}
		catch (Exception e) {}
	}
	
	/*
	 * Delegate for loading all appointments
	 */
	
	private void loadAppointments () {
		JSONObject appointmentObj = this.initJSONObject("appointments", "get");
		String appointmentObjString = appointmentObj.toJSONString();
		
		sh.sendMessage(appointmentObjString);
	}
	
	/*
	 * Delegate for loading all employees in the system
	 */
	
	private void loadEmployees() {
		JSONObject employeeObj = this.initJSONObject("employees", "get");
		String employeeObjString = employeeObj.toJSONString();
		
		sh.sendMessage(employeeObjString);
	}
	
	/*
	 * Delegate for getting all appointments from the user
	 */
	
	public ArrayList<Appointment> getAppointments() {
		return this.user.getAppointments();
	}
	
	/*
	 * Delegate for getting all employees in the system
	 */
	
	public ArrayList<Employee> getEmployees() {
		return this.employees;
	}
}
