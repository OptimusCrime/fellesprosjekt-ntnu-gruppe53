package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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
	private int id;
	private String title;
	private int height;
	private int width;
	
	public GraphicAppointment(int x, int y, int width, int height, Color c, String title, String toolTip) {
		super();
		
		// Store stuff
		this.width = width;
		this.height = height;
		
		this.color = c;
		this.title = title;
		
		// Create new Rect from Swing
		rect = new Rectangle(x, y, width, height);
		
		// Set cursor
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// Set tooltip
		setToolTipText(toolTip);
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
		
		// Text
		g2.setColor(Color.BLACK);
		g2.drawString(this.title, (int) this.width/4, (int) this.height/2);
	}
}
