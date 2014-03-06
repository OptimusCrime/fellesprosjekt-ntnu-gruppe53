package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

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
	
	public SocketHandler () {
		// Set initial values for the variables
		out = null;
		in = null;
	}
	
	public boolean connect(String s, int p) {
		try {
			// Trying to establish connection with server
			try {
				client = new Socket(s, p);
				
				// Set up stream out
				OutputStream outToServer = client.getOutputStream();
				out = new DataOutputStream(outToServer);
				
				// Try to set up stream in
				InputStream inFromServer = client.getInputStream();
				
				// Store stream in private variable
				in = new DataInputStream(inFromServer);
				
				// Start listning
				
				// Return true, all is goot
				return true;
			} catch (ConnectException e) {
				// Could not connect to the server
				System.out.println("Err1");
				return false;
			}
		} catch(IOException e) {
			// Something went wrong
			System.out.println("Err2");
			return false;
		}
	}
}
