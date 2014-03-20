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
	private ArrayList<Room> rooms;
	
	private boolean loadedAppointments;
	private boolean loadedRooms;
	
	/*
	 * Constructor
	 */
	
	public Cal() {
		// Create new instance of the Gui-class
		this.gui = new Gui(this);
		
		// Set not loaded
		loadedAppointments = false;
		loadedRooms = false;
		
		// User
		this.user = new User(this.gui);
		
		// Sockets
		this.sh = new SocketHandler(this);
		
		// Init lists
		this.employees = new ArrayList<Employee>();
		this.rooms = new ArrayList<Room>();
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
						// Clear all appointments
						this.user.clearAppointments();
						
						ArrayList<Appointment> tempAppointmentsList = new ArrayList<Appointment>();
						// Loop all the appointments
						for (int i = 0; i < appointments.size(); i++) {
							JSONObject thisAppointment = (JSONObject) appointments.get(i);
							
							// Check if appointment already exists
							int appointmentId = new BigDecimal((long) thisAppointment.get("id")).intValueExact();
							int appointmentUser =  new BigDecimal((long) thisAppointment.get("user")).intValueExact();
							
							boolean canAdd = true;
							for (int j = 0; j < tempAppointmentsList.size(); j++) {
								if (tempAppointmentsList.get(j).getId() == appointmentId) {
									// Should not be added
									canAdd = false;
									
									// Check if owned by the current user, in that case, update ownage
									if (appointmentUser == this.user.getId()) {
										tempAppointmentsList.get(j).setUser(this.user.getId());
									}
									
									break;
								}
							}
							
							if (canAdd) {
								// Create new appointment
								Appointment a = new Appointment(this.gui);
								
								// Set each field
								a.setId(appointmentId);
								a.setUser(appointmentUser);
								a.setTitle((String) thisAppointment.get("title"));
								a.setDescription((String) thisAppointment.get("description"));
								
								a.setStart(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse((String) thisAppointment.get("start")));
								a.setEnd(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse((String) thisAppointment.get("end")));

								a.setPlace((String) thisAppointment.get("place"));
								a.setParticipates((boolean) thisAppointment.get("participate"));
								a.setHide((boolean) thisAppointment.get("hide"));
								a.setAlarm((boolean) thisAppointment.get("alarm"));
								a.setAlarmTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse((String) thisAppointment.get("alarm_time")));
								
								// Room
								int appointmentRoom = new BigDecimal((long) thisAppointment.get("room")).intValueExact();
								for (int j = 0; j < this.rooms.size(); j++) {
									if (this.rooms.get(j).getId() == appointmentRoom) {
										a.setRoom(this.rooms.get(j));
										break;
									}
								}
								
								// Create the object
								a.create();
								
								// Add appointment to user
								this.user.addAppointment(a);
								
								// For searching
								tempAppointmentsList.add(a);
							}
						}
						
						// Send reflect to the gui from the user-class
						this.user.sendReflect("loaded-appointments");
					}
				}
			}
			else if (action.equals("room")) {
				// Rooms
				if (type.equals("get")) {
					JSONArray requestedRooms = (JSONArray) requestObj.get("data");
					
					// Check that we actually got someting back
					if (requestedRooms != null) {
						// Clear all appointments
						this.user.clearAppointments();
						
						ArrayList<Appointment> tempAppointmentsList = new ArrayList<Appointment>();
						// Loop all the appointments
						for (int i = 0; i < requestedRooms.size(); i++) {
							JSONObject thisAppointment = (JSONObject) requestedRooms.get(i);
							
							Room createNewRoom = new Room(this.gui);
							
							createNewRoom.setId(new BigDecimal((long) thisAppointment.get("id")).intValueExact());
							createNewRoom.setName((String) thisAppointment.get("name"));
							createNewRoom.setCapacity(new BigDecimal((long) thisAppointment.get("capacity")).intValueExact());
							createNewRoom.create();
							rooms.add(createNewRoom);
						}
					}
					
					// Check if we can load the appointments
					this.loadedRooms = true;
					if (this.loadedAppointments) {
						// We have loaded all dependencies, now load appointments
						this.loadAppointments();
					}
				}
				else if (type.equals("gets")) {
					// (
					JSONArray availableRoomsObj = (JSONArray) requestObj.get("data");
					
					// Check that we actually got someting back
					if (availableRoomsObj != null) {
						
						ArrayList<Room> availableRooms = new ArrayList<Room>();
						// Loop all the appointments
						for (int i = 0; i < availableRoomsObj.size(); i++) {
							// Get the current id
							int thisRequestedRoomId = new BigDecimal((long) availableRoomsObj.get(i)).intValueExact();
							for (int j = 0; j < this.getRooms().size(); j++) {
								if (thisRequestedRoomId == this.getRooms().get(j).getId()) {
									availableRooms.add(this.getRooms().get(j));
									break;
								}
							}
						}
						System.out.println("Her1");
						this.gui.updateAvailableRooms(availableRooms);
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
						this.user.setId(new BigDecimal((long) requestObj.get("id")).intValueExact()); 
						this.user.create();
						
						// Show home
						this.gui.showHome();
						
						// Load all stuff the user needs
						this.loadEmployees();
						this.loadRooms();
					}
					else {
						// Send error-message
						this.gui.sendLoginFailedMessage();
					}
				}
			}
			else if (action.equals("logout")) {
				this.user.setLoggedIn(false);
				sh.killConnection();
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
							
							// Set self init calendar
							if (e.getId() == this.user.getId()) {
								this.user.addCalendar(e);
							}
							
							// Add appointment to user
							this.employees.add(e);
						}
						
						// Send reflect to the gui from this class
						this.gui.reflectChange("employees", "create", null);
						
						// Check if we can load the appointments
						this.loadedAppointments = true;
						if (this.loadedRooms) {
							// We have loaded all dependencies, now load appointments
							this.loadAppointments();
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Delegate for loading all appointments
	 */
	
	private void loadAppointments() {
		JSONObject appointmentObj = this.initJSONObject("appointments", "get");
		
		// Add ids for the users we should load
		ArrayList<Employee> cakendarsList = this.user.getCalendars();
		JSONArray calendarsArr = new JSONArray();
		for (int i = 0; i < cakendarsList.size(); i++) {
			calendarsArr.add((int) cakendarsList.get(i).getId());
		}
		
		appointmentObj.put("data", calendarsArr);
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
	 * Delegate for loading all the rooms in the system
	 */
	
	private void loadRooms() {
		JSONObject employeeObj = this.initJSONObject("room", "get");
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
	
	/*
	 * Deletegates for updating the calendars we are displaying
	 */
	
	public void addCalendar(Employee e) {
		// Add employee
		this.user.addCalendar(e);
		
		// Update appointments
		this.loadAppointments();
	}
	
	public void removeCalendar(Employee e) {
		// Remove employee
		this.user.removeCalendar(e);
		
		// Update appointments
		this.loadAppointments();
	}
	
	public ArrayList<Employee> getCalendars() {
		return this.user.getCalendars();
	}
	
	/*
	 * Rooms
	 */
	
	public ArrayList<Room> getRooms() {
		return this.rooms;
	}
	
	/*
	 * Get username
	 */
	
	public String getUsername() {
		String []tempUsername = this.user.getLogin();
		if (tempUsername != null && tempUsername.length > 0) {
			return tempUsername[0];
		}
		else {
			return null;
		}
	}
	
	/*
	 * Fetch rooms from database
	 */
	
	public void calculateTime(Date from, Date to, int num) {
		// Format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// Obj
		JSONObject roomObj = this.initJSONObject("room", "gets");
		JSONObject innerRoomObj = new JSONObject();
		innerRoomObj.put("from", sdf.format(from));
		innerRoomObj.put("to", sdf.format(to));
		innerRoomObj.put("num", num);
		roomObj.put("data", innerRoomObj);
		String roomObjString = roomObj.toJSONString();
		sh.sendMessage(roomObjString);
	}
}
