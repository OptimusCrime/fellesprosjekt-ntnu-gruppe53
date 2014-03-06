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
	
	private Calendar c;
	
	
	/*
	 * Constructor
	 */
	
	public Program() {
		// Init new instance of Calendar-class
		c = new Calendar();
	}
	
	/*
	 * Main
	 */
	
	public static void main(String[] args) {
		// Call the constructor here
		Program p = new Program();
	}

}
