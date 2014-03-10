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
	private User u;
	
	private SocketHandler sh;
	private SocketTranslator st;
	
	/*
	 * Constructor
	 */
	
	public Calendar() {
		// User
		this.u = new User(this);
		
		// Create new instance of the Gui-class
		this.g = new Gui(this);
		
		// Sockets
		sh = new SocketHandler();
		st = new SocketTranslator();
	}
	
	/*
	 * Set username and password for the current user
	 */
	
	public void setLogin(String user, String pass) {
		//u.(user, pass);
	}
	
	public boolean testConnection(String s, int port) {
		return sh.connect(s, port);
	}
	
	public String send(String s) {
		return "Derp";
	}
}
