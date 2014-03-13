package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/*
 * SocketHandler
 * 
 * Takes care of interaction with the server
 * 
 */

public class SocketHandler extends Thread {
	
	/*
	 * Variables we need
	 */
	
	private Cal calendar;
	private Socket client;
	private DataOutputStream out;
	private DataInputStream in;
	private Map<String, String> requestQueue;
	
	/*
	 * Constructor
	 */

	public SocketHandler (Cal c) {
		// Set initial values for the variables
		this.calendar = c;
		this.client = null;
		this.out = null;
		this.in = null;
		
		this.requestQueue = new HashMap<String, String>();
	}
	
	/*
	 * Method for connecting to the server
	 */
	
	public boolean connect(String s, int p) {
		// Trying to establish connection with server
		try {
			// Setting up socket-connection here
			this.client = new Socket(s, p);
			
			// Set up stream out
			OutputStream outToServer = this.client.getOutputStream();
			this.out = new DataOutputStream(outToServer);
			
			// Set up stream in
			InputStream inFromServer = this.client.getInputStream();
			this.in = new DataInputStream(inFromServer);
			
			start();
		} catch (ConnectException e1) {
			// Could not connect to the server
			System.out.println("Err1");
			return false;
		} catch (UnknownHostException e2) {
			// Could not connect to the server
			System.out.println("Err2");
			return false;
		} catch (IOException e3) {
			// Could not connect to the server
			e3.printStackTrace();
			System.out.println("Err4");
			return false;
		}
		
		// If we did not get any expcetions so far, everyhing is sweet as gold
		return true;
	}
	
	/*
	 * Method for sending a message
	 */
	
	public void sendMessage(String s) {
		try {
			this.out.writeUTF(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Derp TODO, this does not wkrk as intended
	 */
	
	public String sendMessageWithResponse(String s, String p) {
		try {
			out.writeUTF(s);
		} catch (IOException e) {
			return null;
		}
		
		requestQueue.put(p, null);
		
		while (true) {
			String request = requestQueue.get(p);
			if (request != null) {
				requestQueue.remove(p);
				return request;
			}
		}
	}
	
	/*
	 * Listen for stuff in the stream
	 */
	
	public void run() {
		// Store message here
		String msg;
		
		// Read forever
		while (true) {
			
			// Try to read stream
			try {
				// Store information in variable
				msg = in.readUTF();
				
				// Check if the variable got actual content
				if (msg.length() > 0) {
					// Print message
					System.out.println(msg);
					
					// Send incoming to calendar-class to take care of it
					this.calendar.handleIncoming(msg);
				}
			} catch (IOException e) {
				// Quitting
				break;
			}
		}
	}
}