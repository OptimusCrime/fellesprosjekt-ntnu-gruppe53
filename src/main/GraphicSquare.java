package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class GraphicSquare extends JPanel {
	private Rectangle rect;
	private int space;
	private int width;
	public GraphicSquare(int x, int y, int width, int height, int spaceHeight) {
		super();
		this.space = spaceHeight;
		this.width = height;
		
		rect = new Rectangle(x, y, width, height);
	}
	
	
	@Override 
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(212, 229, 245));
		g2.fill(rect);
		
		g2.setColor(new Color(28, 64, 148));
		g2.draw(rect);
		
		g2.setColor(new Color(28, 64, 148));
		for (int i = 1; i <= 9; i++) {
			g2.drawLine(0, this.space*i, this.width, this.space*i);
		}
	}
}