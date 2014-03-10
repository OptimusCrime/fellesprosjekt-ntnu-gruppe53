package main;

/*
 * Calendar
 * 
 * Takes care of handling user-state, handles GUI etc
 * 
 */

public class Calendar {
	
	/*
	 * Variables
	 */
	
	private Gui g;
	private Program p;
	private String username;
	private String password;
	private boolean loggedIn;
	private SocketHandler sh;
	private SocketTranslator st;
	
	/*
	 * Constructor
	 */
	
	public Calendar(Program p, SocketHandler socketHandler, SocketTranslator socketTranslator) {
		// Create new instance of the Gui-class
		g = new Gui(p, this);
		
		// Derp
		sh = socketHandler;
		st = socketTranslator;
		
		// Set initial data
		username = "";
		password = "";
		loggedIn = false;
	}
	
	/*
	 * Set username and password for the current user
	 */
	
	public void setLogin(String u, String p) {
		username = u;
		password = p;
	}
	
	public boolean testConnection(String s, int port) {
		return sh.connect(s, port);
	}
}
