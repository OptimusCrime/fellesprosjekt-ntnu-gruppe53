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
	
	/*
	 * ToString
	 */
	
	public String toString() {
		return this.name + "[" + this.capacity + "]";
	}
}
