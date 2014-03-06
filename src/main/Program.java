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
	private SocketHandler sh;
	private SocketTranslator st;
	
	/*
	 * Constructor
	 */
	
	public Program() {
		// Init new instance of Calendar-class
		c = new Calendar(this);
		
		// Init sockets
		sh = new SocketHandler();
		st = new SocketTranslator();
	}
	
	/*
	 * Main
	 */
	
	public static void main(String[] args) {
		// Call the constructor here
		new Program();
		
	}
	
	public boolean testConnection(String s, int p) {
		// Trying to connect to socket
		return sh.connect(s, p);
	}

}
