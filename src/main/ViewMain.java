package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class ViewMain extends JFrame {
	
	/*
	 * Variables, references etc we need
	 */
	
	private static final long serialVersionUID = 1L;
	
	protected Gui gui;
	protected Cal calendar;
	
	// Panels, parts of the window
	private JPanel header;
	private JPanel seperator;
	private JSplitPane main;
	
	private JPanel splitLeft;
	private JPanel splitRight;
	private JPanel splitRightInner;
	private JPanel splitRightNav;
	
	// Buttons
	private JButton homeBtn;
	private JButton notificationsBtn;
	private JButton employeesBtn;
	private JButton logoutBtn;
	
	// For the nav
	private JButton navLeft;
	private JButton navRight;
	private JLabel navWeek;
	
	// For calculating the displayed week
	private Timestamp ts;
	private long tsOffset;
	
	/*
	 * Constructor
	 */
	
	public ViewMain(Gui g, Cal c) {
		// Set gui
		this.gui = g;
		this.calendar = c;
		
		// Set close-mode
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set title
		super.setTitle("NTNU Calendar");
		
		// Load size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Set initial size of the window and the relative loaction
		super.setPreferredSize(new Dimension((int) dim.getWidth() - 200, (int) dim.getHeight()));
	    
	    // Adder springlayout to base
	    SpringLayout springLayout = new SpringLayout();
	    super.getContentPane().setLayout(springLayout);
		
	    // Add header-panel
		header = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, header, 10, SpringLayout.NORTH, super.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, header, 10, SpringLayout.WEST, super.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, header, 61, SpringLayout.NORTH, super.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, header, -10, SpringLayout.EAST, super.getContentPane());
		super.getContentPane().add(header);
		header.setLayout(new BorderLayout(0, 0));
		
		// Add seperator-panel
		seperator = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, seperator, 2, SpringLayout.SOUTH, header);
		springLayout.putConstraint(SpringLayout.WEST, seperator, 10, SpringLayout.WEST, super.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, seperator, 53, SpringLayout.SOUTH, header);
		springLayout.putConstraint(SpringLayout.EAST, seperator, -10, SpringLayout.EAST, super.getContentPane());
		super.getContentPane().add(seperator);
		
		// Add main-panel
		main = new JSplitPane();
		springLayout.putConstraint(SpringLayout.NORTH, main, 9, SpringLayout.SOUTH, seperator);
		springLayout.putConstraint(SpringLayout.WEST, main, 10, SpringLayout.WEST, super.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, main, -25, SpringLayout.SOUTH, super.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, main, -10, SpringLayout.EAST, super.getContentPane());
		
		// Add header-left panel
		JPanel headerLeft = new JPanel();
		header.add(headerLeft, BorderLayout.WEST);
		
		// Add all buttons in the header-left panel
		homeBtn = new JButton("Hjem");
		headerLeft.add(homeBtn);
		notificationsBtn = new JButton("Varsler (2)");
		headerLeft.add(notificationsBtn);
		employeesBtn = new JButton("Ansatte");
		headerLeft.add(employeesBtn);
		
		// Add header-right panel
		JPanel headerRight = new JPanel();
		header.add(headerRight, BorderLayout.EAST);
		
		// Add logout-button in the header-right panel
		logoutBtn = new JButton("Logg ut");
		
		// Add actionListner for the logout-button
		logoutBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gui.logout();
			}
			
		});
		headerRight.add(logoutBtn);
		
		// Add seperator (TODO, this is not working)
		JSeparator swingSeperator = new JSeparator();
		seperator.add(swingSeperator);
		getContentPane().add(main);
		
		// Set left-split content
		splitLeft = new JPanel();
		main.setLeftComponent(splitLeft);
		
		// Placeholder TODO
		JLabel lblNewLabel = new JLabel("New label");
		splitLeft.add(lblNewLabel);
		
		// Set right-split content
		splitRight = new JPanel();
		main.setRightComponent(splitRight);
		
		// Add SpringLayout to the right-split content
		SpringLayout splitRightLayout = new SpringLayout();
		splitRight.setLayout(splitRightLayout);
		
		// Add inner JPanel to the right-split content (holds the calendar and nav)
		splitRightInner = new JPanel();
		splitRightLayout.putConstraint(SpringLayout.NORTH, splitRightInner, 55, SpringLayout.NORTH, splitRight);
		splitRightLayout.putConstraint(SpringLayout.SOUTH, splitRightInner, -24, SpringLayout.SOUTH, splitRight);
		splitRightLayout.putConstraint(SpringLayout.WEST, splitRightInner, 14, SpringLayout.WEST, splitRight);
		splitRightLayout.putConstraint(SpringLayout.EAST, splitRightInner, -6, SpringLayout.EAST, splitRight);
		splitRight.add(splitRightInner);		
		splitRightInner.setLayout(new GridLayout(1, 8, 0, 0));
		
		// Add inner JPanel to the right-split content that holds the nav
		splitRightNav = new JPanel();
		splitRightLayout.putConstraint(SpringLayout.NORTH, splitRightNav, 12, SpringLayout.NORTH, splitRight);
		splitRightLayout.putConstraint(SpringLayout.WEST, splitRightNav, 20, SpringLayout.WEST, splitRight);
		splitRightLayout.putConstraint(SpringLayout.SOUTH, splitRightNav, -6, SpringLayout.NORTH, splitRightInner);
		splitRightLayout.putConstraint(SpringLayout.EAST, splitRightNav, -4, SpringLayout.EAST, splitRight);
		splitRight.add(splitRightNav);
		splitRightNav.setLayout(new BorderLayout(0, 0));
		
		// Add nav-buttons to the right-split nav-panel
		navLeft = new JButton("◄");
		splitRightNav.add(navLeft, BorderLayout.WEST);
		navRight = new JButton("►");
		splitRightNav.add(navRight, BorderLayout.EAST);
		
		// Add label which identifies what week we're currently in
		navWeek = new JLabel("Laster...");
		navWeek.setHorizontalAlignment(SwingConstants.CENTER);
		splitRightNav.add(navWeek, BorderLayout.NORTH);
		
		// Show, set width etc
		super.setVisible(true);
		super.setBounds(100, 100, (int) dim.getWidth() - 200, (int) dim.getHeight());
	    super.setLocationRelativeTo(null);
	    super.pack();
	    
	    // Set initial time
	    this.setTime();
	    
	    // Calculate week, dates etc
	    this.calculateCalendar();
	    
	    // Draw calendar
	    this.drawCalendar();
	}
	
	/*
	 * Sets the time for the calendar
	 */
	
	private void setTime() {
		this.tsOffset = 0;
		this.ts = new Timestamp(System.currentTimeMillis());
	}
	
	/*
	 * Calculates what dates to display, what week we're in etc
	 */
	
	private void calculateCalendar() {
		// Parse timestamp to Date-object
		Date d = new Date(ts.getTime());
		
		// Use the Calendar-class to get the info
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		
		// Extract week
		int week = cal.get(Calendar.WEEK_OF_YEAR);
		
		// Set week in the label
		navWeek.setText("Uke " + Integer.toString(week));
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
		cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		System.out.println(sdf.format(cal.getTime()));    
	}
	
	/*
	 * Draws the actual graphics for the calendar (not appointments)
	 */
	
	private void drawCalendar() {
		// Some variables we need
		int width = splitRightInner.getWidth();
		int column_width = (int) splitRightInner.getWidth() / 8;
		int row_height = (int) (splitRightInner.getHeight() - 23) / 10;
		int height = row_height * 10;
		
		for (int i = 0; i < 8; i++) {
			GraphicSquare square = new GraphicSquare(0, 0, column_width, height, row_height);
			square.setLayout(null);
			Box box = Box.createVerticalBox();
			if (i == 0) {
				box.setOpaque(true);
				box.setBackground(Color.yellow);
				box.setBounds(0, 0, column_width, height);
				for (int j = 0; j < 10; j++) {
					JLabel ngger = new JLabel("08:00");
					ngger.setAlignmentX(Component.RIGHT_ALIGNMENT);
					box.add(ngger);
				}
			}
			else {
				box.setOpaque(false);
				int datePadding = (int) (row_height-16)/2;
				JLabel ngger = new JLabel("ma 17.02");
				box.setBounds(0, datePadding, column_width, 30);
				ngger.setAlignmentX(Component.CENTER_ALIGNMENT);
				box.add(ngger);
			}
			square.add(box);
			splitRightInner.add(square);
		}
		
		System.out.println(getContentPane().getWidth());
	}
	
	/*
	 * Setter for visiblity of the window
	 */
	
	public void setVisible(boolean b) {
		super.setVisible(b);
	}
}
