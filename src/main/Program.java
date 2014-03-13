package main;

/*
 * Program
 * 
 * Main class for the entire program
 * 
 */

public class Program {
	
	/*
	 * Variables
	 */
	
	private Cal c;
	
	/*
	 * Constructor
	 */
	
	public Program() {
		// Init new instance of Calendar-class
		c = new Cal();
	}
	
	/*
	 * Main
	 */
	
	public static void main(String[] args) {
		// Call the constructor here
		new Program();
	}
}
