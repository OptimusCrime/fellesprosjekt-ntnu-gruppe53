package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.JPanel;
import javax.swing.JLabel;

/*
 * GraphicSquare
 * 
 * Class for drawing a square in the calendar
 * 
 */

public class GraphicSquare extends JPanel implements MouseMotionListener {
	
	/*
	 * Variables we need
	 */
	
	private static final long serialVersionUID = 1L;
	private Rectangle rect;
	private int space;
	private int width;
	private ArrayList<GraphicsLabel> labelsOnHover;
	private boolean shouldDrawHorizontal;
	
	/*
	 * Constructor
	 */
	
	public GraphicSquare(int x, int y, int width, int height, int spaceHeight, boolean s) {
		super();
		
		// Set some variables we need
		this.space = spaceHeight;
		this.width = width;
		this.shouldDrawHorizontal = s;
		
		// Initialize list of labels
		labelsOnHover = new ArrayList<GraphicsLabel>();
		
		// Create new Rect from Swing
		rect = new Rectangle(x, y, width, height);
	}
	
	/*
	 * Add label to the square
	 */
	
	public void addLabel(GraphicsLabel j) {
		labelsOnHover.add(j);
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
		
		// Check if we should draw horizontal lines
		if (this.shouldDrawHorizontal) {
			// Draw horizontal lines
			g2.setColor(new Color(28, 64, 148));
			for (int i = 1; i <= 9; i++) {
				g2.drawLine(0, this.space*i, this.width, this.space*i);
			}
		}
	}
	
	/*
	 * When the mouse is moved, toggle show/hide on the different labels based on the height of the squares
	 */
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// Check if moving moving to the sides to remove it
		int xPos = e.getX();
		if (xPos <= 3 || xPos >= (this.width - 6)) {
			for (int i = 0; i < labelsOnHover.size(); i++) {
				labelsOnHover.get(i).setVisible(false);
			}
		}
		else {
			int yPos = e.getY();
			int index = (int) Math.floor(yPos/this.space) - 1;
			
			for (int i = 0; i < labelsOnHover.size(); i++) {
				if (i == index) {
					labelsOnHover.get(i).setVisible(true);
				}
				else {
					labelsOnHover.get(i).setVisible(false);
				}
			}
		}
	}
	
	/*
	 * Method that we don't need
	 */
	
	@Override
	public void mouseDragged(MouseEvent e) {}
}