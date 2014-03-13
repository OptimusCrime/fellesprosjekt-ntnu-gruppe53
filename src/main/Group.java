package main;

/*
 * Group
 * 
 * Object representation of an actual group
 * 
 */

public class Group implements CalendarObjects {
	
	/*
	 * Variables
	 */
	
	private Gui gui;
	private boolean created;
	
	private int id;
	private String name;
	
	/*
	 * Constructor
	 */
	
	public Group (Gui g) {
		this.gui = g;
		this.created = false;
	}
	
	/*
	 * Create & reflect
	 */
	
	public void create() {
		this.created = true;
		this.gui.reflectChange("group", "create", this);
	}
	
	public boolean isCreated() {
		return this.created;
	}
	
	/*
	 * Getters and setters
	 */
	
	public void setId (int i) {
		this.id = i;
		this.gui.reflectChange("group", "i", this);
	}
	
	public void setName (String s) {
		this.name = s;
		this.gui.reflectChange("group", "name", this);
	}
	
	public int getId () {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
}
