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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

/*
 * ViewMain
 * 
 * The main view (holds the calendar etc)
 * 
 */

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
	private Date weekDateStart;
	private Date weekDateEnd;
	private long tsOffset;
	private String[] calendarText;
	private Map<String, String> calendarReplaces;
	
	// The different squares
	private GraphicSquare[] squareArr;
	
	/*
	 * Constructor
	 */
	
	public ViewMain(Gui g, Cal c) {
		// Set gui
		this.gui = g;
		this.calendar = c;
		
		// Setting up array for replacing english weekdays to norwegian ones
		calendarReplaces = new HashMap<String, String>();
		calendarReplaces.put("l?", "lø");
		calendarReplaces.put("s?", "sø");
		
		// Setting up array for holding the different squares
		squareArr = new GraphicSquare[8];
		
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
		
		// Add actionListeners for the nav-buttons
		navLeft.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateTime(false);
			}
			
		});
		navRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateTime(true);
			}
			
		});
		
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
	 * Method that updates the ts-offset, calculates new timestamp and updates the GUI
	 */
	
	protected void updateTime(boolean w) {
		// Check what way we're changing the time
		if (w) {
			// Increase
			this.tsOffset += (60*60*24*7*1000);
		}
		else {
			// Decrease
			this.tsOffset -= (60*60*24*7*1000);
		}
		
		// Calculate calendar again
		this.recalculateTime();
		this.calculateCalendar();
		
		// Redraw the calendar
		this.clearCalendar();
		this.drawCalendar();
	}
	
	/*
	 * Sets the time for the calendar
	 */
	
	private void setTime() {
		// Set initial ts-offset
		this.tsOffset = 0;
		
		// Calculate the first timestamp
		this.recalculateTime();
	}
	
	/*
	 * Method for calculating timestamp +/- the offset
	 */
	
	private void recalculateTime() {
		this.ts = new Timestamp(System.currentTimeMillis() + this.tsOffset);
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
		
		// Store dates for the beginning and end of the week
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		this.weekDateStart = cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.add(Calendar.HOUR_OF_DAY, 24);
		this.weekDateEnd = cal.getTime();
		
		// Calculate the days we display as a legend for the calendar
		calendarText = new String[7];
		
		// Dateformatters
		SimpleDateFormat formatEngToNor = new SimpleDateFormat("E");
		SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM");
		
		// Loop all the days and calculate dates
		for (int i = 1; i <= 7; i++) {
			// Set date
			cal.set(Calendar.DAY_OF_WEEK, i);
			
			// Create string for the date (such hack)
			String tempDate;
			if (calendarReplaces.get(formatEngToNor.format(cal.getTime())) != null) {
				tempDate = calendarReplaces.get(formatEngToNor.format(cal.getTime()));
			}
			else {
				tempDate = formatEngToNor.format(cal.getTime());
			}
			
			// Append the actual date
			tempDate += " " + formatDate.format(cal.getTime());
			
			// Check where in the array we should put it (US has Sunday as first day or week, stupids)
			if (i == 1) {
				calendarText[6] = tempDate;
			}
			else {
				calendarText[i - 2] = tempDate;
			}
		}
	}
	
	/*
	 * Draws the actual graphics for the calendar (not appointments)
	 */
	
	private void drawCalendar() {
		// Some variables we need
		int width = splitRightInner.getWidth();
		int numRows = 10;
		int column_width = (int) width / 8;
		int row_height = (int) (splitRightInner.getHeight() - 23) / numRows;
		int height = row_height * numRows;
		
		// Loop over each of the squares (one for each day in the week + one for holding the time)
		for (int i = 0; i < 8; i++) {
			// Create new square
			GraphicSquare square = new GraphicSquare(0, 0, column_width, height, row_height);
			
			// Reset layoutManager to null to be able to use absolute positions
			square.setLayout(null);
			
			// Check what column we are working with
			if (i == 0) {
				// Define where we start the hours and how much we should move the block to match the horizontal lines
				int startHour = 8;
				int compensateForLineOffset = 7;
				
				// Loop ten times to fill the time-column with hours from 08:00 to 18:00
				for (int j = 0; j < numRows; j++) {
					// Create box
					Box box = Box.createVerticalBox();
					
					// Check if we should adjust the compenstate-value
					if ((j + 1) == numRows) {
						compensateForLineOffset = 14;
					}
					
					// Set bounds for the box
					box.setBounds(1, (((j + 1) * row_height) - compensateForLineOffset), (column_width - 1), 14);
					
					// Little hack to hide the horizontal line in this column
					box.setOpaque(true);
					box.setBackground(new Color(212, 229, 245));
					
					// Set content of the label
					JLabel calendarHours = new JLabel(((startHour < 10)? "0" : "") + Integer.toString(startHour) + ":00");
					
					// Right-align it to prettify it
					calendarHours.setAlignmentX(Component.RIGHT_ALIGNMENT);
					
					// Add to the square
					box.add(calendarHours);
					square.add(box);
					
					// Increase the hour by one
					startHour++;
				}
			}
			else {
				// We're working on a weekday, create box-object
				Box box = Box.createVerticalBox();
				
				// Calculate padding based on the height of the rows
				int datePadding = (int) (row_height-16)/2;
				
				// Append day-date
				JLabel dayLegend = new JLabel(calendarText[i - 1]);
				
				// Set height and width of the box to fix the date-content
				box.setBounds(0, datePadding, column_width, 30);
				
				// Set center-align
				dayLegend.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				// Add legend to the box
				box.add(dayLegend);
				
				// Add the box (with all content) to the square
				square.add(box);
			}
			
			// Add the square (with all content) to the calendar-wrapepr
			splitRightInner.add(square);
			
			// Add square to the array
			squareArr[i] = square;
		}
	}
	
	/*
	 * Draws the appointments in the calendar (TODO, test)
	 */
	
	public void drawAppointments() {
		// Get all appointments from the user
		ArrayList<Appointment> userAppointments = this.calendar.getAppointments();
		
		// Loop all the appointments
		for (int i = 0; i < userAppointments.size(); i++) {
			// Load the current appointment
			Appointment thisAppointment = userAppointments.get(i);
			
			System.out.println("Start = " + thisAppointment.getStart());
			System.out.println("End = " + thisAppointment.getEnd());
			
			// Check if we are in the right week for this appointment
			if (thisAppointment.getStart().after(this.weekDateStart)) {
				if (thisAppointment.getEnd().before(this.weekDateEnd)) {
					GraphicSquare nigger = squareArr[2];
					GraphicAppointment squar2222e = new GraphicAppointment(0, 0, 100, 100, Color.pink);
					squar2222e.setLayout(null);
					squar2222e.setBounds(0, 0, 100, 100);
					nigger.add(squar2222e);
				}
			}
		}
	}
	
	/*
	 * Removes all graphics from the calendar-inner-wrapper
	 */
	
	private void clearCalendar() {
		splitRightInner.removeAll();
	}
	
	/*
	 * Setter for visiblity of the window
	 */
	
	public void setVisible(boolean b) {
		super.setVisible(b);
	}
}
