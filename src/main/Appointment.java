package main;

import java.util.ArrayList;
import java.util.Date;

/*
 * Appointment
 * 
 * Class for storing information about the appointment
 * 
 */

public class Appointment implements CalendarObjects {
	
	/*
	 * Variables we need
	 */
	
	private Gui gui;
	private boolean created;
	
	private int id;
	private String title;
	private String description;
	private Date start;
	private Date end;
	private String place;
	private Room room;
	
	private boolean participates;
	private boolean hide;
	private boolean alarm;
	private Date alarmTime;
	
	private ArrayList<User> users;
	private ArrayList<Group> groups;
	
	/*
	 * Constructor
	 */
	
	public Appointment (Gui g) {
		this.gui = g;
		this.created = false;
	}
	
	/*
	 * Create & reflect
	 */
	
	public void create() {
		this.created = true;
		this.gui.reflectChange("appointment", "create", this);
	}
	
	public boolean isCreated() {
		return this.created;
	}
	
	/*
	 * Generated getters and setters
	 */
	
	public int getId() {
		return this.id;
	}
	
	public void setId (int i) {
		this.id = i;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		this.gui.reflectChange("appointment", "title", this);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		this.gui.reflectChange("appointment", "description", this);
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
		this.gui.reflectChange("appointment", "start", this);
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
		this.gui.reflectChange("appointment", "end", this);
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
		this.gui.reflectChange("appointment", "place", this);
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
		this.gui.reflectChange("appointment", "room", this);
	}

	public void setParticipates(boolean participates) {
		this.participates = participates;
		this.gui.reflectChange("appointment", "participates", this);
	}

	public void setHide(boolean hide) {
		this.hide = hide;
		this.gui.reflectChange("appointment", "hide", this);
	}
	
	public boolean getHide() {
		return this.hide;
	}
	
	public void setAlarm(boolean alarm) {
		this.alarm = alarm;
		this.gui.reflectChange("appointment", "alarm", this);
	}

	public Date getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
		this.gui.reflectChange("appointment", "alarmtime", this);
	}
	public void addUser(User user) {
		if (!users.contains(user)) {
			users.add(user);
		}
	}
	public void removeUser(User user) {
		if (users.contains(user)) {
			users.remove(user);
		}
	}
	public void addGroup(Group group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
	}
	public void removeGroup(Group group) {
		if (groups.contains(group)) {
			groups.remove(group);
		}
	}
}
