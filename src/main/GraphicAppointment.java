package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

/*
 * GraphicAppointment
 * 
 * Class for drawing the square for an appointment
 * 
 */

public class GraphicAppointment extends JPanel {
	
	/*
	 * Variables we need
	 */
	
	private static final long serialVersionUID = 1L;
	private Rectangle rect;
	private Color color;
	
	public GraphicAppointment(int x, int y, int width, int height, int spaceHeight, Color c) {
		super();
		
		// Store color
		this.color = c;
		
		// Create new Rect from Swing
		rect = new Rectangle(x, y, width, height);
	}
	
	/*
	 * Paint the component
	 */
	
	@Override 
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Cast to grapgics
		Graphics2D g2 = (Graphics2D) g;
		
		// Set color
		g2.setColor(this.color);
		
		// Fill with color
		g2.fill(rect);
		
		// Draw border
		g2.draw(rect);
	}

}
