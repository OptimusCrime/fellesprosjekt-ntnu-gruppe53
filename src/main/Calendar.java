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
	
	private Gui gui;
	private User user;
	
	private SocketHandler sh;
	private SocketTranslator st;
	
	/*
	 * Constructor
	 */
	
	public Calendar() {
		// Create new instance of the Gui-class
		this.gui = new Gui(this);
		
		// User
		this.user = new User(this.gui);
		
		// Sockets
		sh = new SocketHandler();
		st = new SocketTranslator();
	}

	public void setLogin(String u, String p) {
		this.user.setLogin(u, p);
	}
	
	public boolean testConnection(String s, int p) {
		return sh.connect(s, p);
	}
	
	public boolean doLogin() {
		user.setLoggedIn(true);
		return true;
	}
}
