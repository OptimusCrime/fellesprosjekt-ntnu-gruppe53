package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

/*
 * GraphicSquare
 * 
 * Class for drawing a square in the calendar
 * 
 */

public class GraphicSquare extends JPanel {
	
	/*
	 * Variables we need
	 */
	
	private static final long serialVersionUID = 1L;
	private Rectangle rect;
	private int space;
	private int width;
	
	/*
	 * Constructor
	 */
	
	public GraphicSquare(int x, int y, int width, int height, int spaceHeight) {
		super();
		
		// Set some variables we need
		this.space = spaceHeight;
		this.width = height;
		
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
		g2.setColor(new Color(212, 229, 245));
		
		// Fill with color
		g2.fill(rect);
		
		// Set border-color
		g2.setColor(new Color(28, 64, 148));
		
		// Draw border
		g2.draw(rect);
		
		// Draw horizontal lines
		g2.setColor(new Color(28, 64, 148));
		for (int i = 1; i <= 9; i++) {
			g2.drawLine(0, this.space*i, this.width, this.space*i);
		}
	}
}