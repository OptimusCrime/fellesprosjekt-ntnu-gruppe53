package main;

import java.util.ArrayList;
import java.util.Date;
/*
 * Room
 * 
 * Object representation of a room
 * 
 */

public class Room implements CalendarObjects {
	
	/*
	 * Variables we need
	 */
	
	private Gui gui;
	private boolean created;
	
	private int id;
	private String name;
	private int capacity;
	
	private ArrayList<ArrayList<Date>> availability;
	/*
	 * Constructor
	 */
	
	public Room (Gui g) {
		this.gui = g;
		this.created = false;
	}
		
	/*
	 * Create & reflect
	 */
	
	public void create() {
		this.created = true;
		this.gui.reflectChange("room", "create", this);
	}
	
	public boolean isCreated() {
		return this.created;
	}
	
	/*
	 * Getters and setters
	 */
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		this.gui.reflectChange("room", "id", this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.gui.reflectChange("room", "name", this);
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
		this.gui.reflectChange("room", "capacity", this);
	}
	
	public boolean isAvailable(Date startDate, Date endDate) {
		if (startDate.after(endDate) || startDate.equals(endDate)) {
			System.out.println("Starting date cant be after or equal to ending date");
		}
		else {
			for (int i = 0; i < availability.size(); i++) {
				if (startDate.equals(getEndDate(i)) || getStartDate(i).equals(endDate)) {
					return false;
				} 
				if (startDate.before(getEndDate(i)) && endDate.after(getStartDate(i))) {
					return false;
				}
				if (getStartDate(i).before(endDate) && getEndDate(i).after(startDate)) {
					return false;
				}
			}
 		}
 		return true;
	}
	public void reserve(Date startDate, Date endDate) {
		availability.add(ArrayList<Date>);
		availability.get(availability.size() - 1).add(startDate);
		availability.get(availability.size() - 1).add(endDate);
	}
	private Date getStartDate(int index) {
		return availability.get(index).get(0);
	}
	private Date getEndDate(int index) {
		return availability.get(index).get(1);
	}
}
