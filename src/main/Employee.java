package main;

/*
 * Employee
 * 
 * Contains information about one employee
 * 
 */

public class Employee {
	
	/*
	 * Variables
	 */
	
	private int id;
	private String email;
	private String name;
	private boolean isChecked;
	private boolean isCurrentUser;
	
	/*
	 * Constructor
	 */
	
	public Employee(int i, String e, String n) {
		this.id = i;
		this.email = e;
		this.name = n;
		this.isChecked = false;
		this.isCurrentUser = false;
	}
	
	/*
	 * Setters
	 */
	
	public void setId(int i) {
		this.id = i;
	}
	
	public void setEmail(String e) {
		this.email = e;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public void setChecked(boolean c) {
		this.isChecked = c;
	}
	
	public void setIsCurrentUser(boolean b) {
		this.isCurrentUser = b;
	}
	
	/*
	 * Getters
	 */
	
	public int getId () {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public boolean isChecked() {
		return this.isChecked;
	}
	
	public boolean isCurrentUser() {
		return this.isCurrentUser;
	}
	
	/*
	 * toString
	 */
	
	public String toString() {
		return this.name;
	}
}
