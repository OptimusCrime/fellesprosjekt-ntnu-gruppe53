package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

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
	
	public SocketHandler () {
		// Set initial values for the variables
		client = null;
		out = null;
		in = null;
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
			
			// Try to read
			in.readUTF();
			
			// Start listning
			// TODO
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
			System.out.println("Err4");
			return false;
		}
		
		// If we did not get any expcetions so far, everyhing is sweet as gold
		return true;
	}
	
	public void sendMessage() {
		// TODO
	}
	
	public void listen () {
		// TODO
	}
}