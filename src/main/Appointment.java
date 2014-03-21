package main;

import java.util.ArrayList;
import java.util.HashMap;
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
	private int user;
	
	private boolean participates;
	private boolean hide;
	private boolean alarm;
	private Date alarmTime;
	
	private boolean hasLoadedParticipates;
	private ArrayList<Employee> participatesList;
	private ArrayList<String> participatesStatus;
	
	public Appointment (Gui g) {
		this.gui = g;
		this.created = false;
		this.room = null;
		
		this.hasLoadedParticipates = false;
		this.participatesList = new ArrayList<Employee>();
		this.participatesStatus = new ArrayList<String>();
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
	
	public String getRoomString() {
		if (room == null) {
			return null;
		}
		
		return room.toString();
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
	
	public void setUser(int i) {
		this.user = i;
	}
	
	public int getUser() {
		return this.user;
	}
	
	/*
	 * ParticipatesList
	 */
	
	public boolean hasLoadedParticipates() {
		return this.hasLoadedParticipates;
	}
	
	public void setHasLoadedParticipates(boolean b) {
		this.hasLoadedParticipates = b;
	}
	
	public void addParticipates(Employee e, String s) {
		this.participatesList.add(e);
		this.participatesStatus.add(s);
	}
	
	public ArrayList<Employee> getParticipatesList() {
		return this.participatesList;
	}
	
	public ArrayList<String> getParticipatesStatus() {
		return this.participatesStatus;
	}
}
