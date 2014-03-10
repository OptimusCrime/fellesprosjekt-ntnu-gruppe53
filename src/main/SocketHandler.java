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
	private Socket client;
	private DataOutputStream out;
	private DataInputStream in;
	private Map<String, String> requestQueue;

	public SocketHandler () {
		// Set initial values for the variables
		client = null;
		out = null;
		in = null;
		
		requestQueue = new HashMap<String, String>();
	}
	
	public boolean connect(String s, int p) {
		// Trying to establish connection with server
		try {
			// Setting up socket-connection here
			client = new Socket(s, p);
			
			// Set up stream out
			OutputStream outToServer = client.getOutputStream();
			out = new DataOutputStream(outToServer);
			
			// Set up stream in
			InputStream inFromServer = client.getInputStream();
			in = new DataInputStream(inFromServer);
			
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
	
	public void sendMessage(String s) {
		try {
			out.writeUTF(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
					System.out.println(msg);
					
					// Check if in queue
					JSONObject obj = (JSONObject) JSONValue.parse(msg);
					
					// Get different values from the json-object
					String action = (String) obj.get("action");
					String type = (String) obj.get("type");
					String path = action + "/" + type;
					
					if (requestQueue.containsKey(path)) {
						requestQueue.put(path, msg);
					}
				}
			} catch (IOException e) {
				// Quitting
				break;
			}
		}
	}
}