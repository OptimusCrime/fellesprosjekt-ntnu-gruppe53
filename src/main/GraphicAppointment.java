package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/*
 * GraphicAppointment
 * 
 * Class for drawing the square for an appointment
 * 
 */

public class GraphicAppointment extends JPanel implements MouseMotionListener {
	
	/*
	 * Variables we need
	 */
	
	private static final long serialVersionUID = 1L;
	private Rectangle rect;
	private Color color;
	private int id;
	
	public GraphicAppointment(int x, int y, int width, int height, Color c) {
		super();
		
		// Store color
		this.color = c;
		
		// Create new Rect from Swing
		rect = new Rectangle(x, y, width, height);
		
		// Set cursor
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	/*
	 * Getter & Setter for the id
	 */
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
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

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Put stuff here
	}

}
