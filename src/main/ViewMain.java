package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

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
	private JPanel splitLeftInner;
	private JPanel splitRight;
	private JPanel splitRightInner;
	private JPanel splitRightNav;
	
	// For the dyanamic sidepanels
	private JScrollPane ansatteScrollPane;
	
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
	private int column_width;
	private int row_height;
	
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
		super.setPreferredSize(new Dimension((int) dim.getWidth() - 75, (int) dim.getHeight()));
	    
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
		
		// Set main-panel impossible to resize
		main.setEnabled(false);
		
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
		
		// Dynamic left-panel
		splitLeftInner = new JPanel();
		splitLeft.add(splitLeftInner);
		
		// Build dynamic left-panel
		this.buildLeftpanel();
		
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
		super.setBounds(0, 0, (int) dim.getWidth() - 75, (int) dim.getHeight());
	    super.setLocationRelativeTo(null);
	    super.pack();
	    
	    // Set resize disallowed
	    super.setResizable(false);
	    
	    // Set initial time
	    this.setTime();
	    
	    // Calculate week, dates etc
	    this.calculateCalendar();
	    
	    // Draw calendar
	    this.drawCalendar();
	    
		// Set sizes for left-panel
		this.setSizesLeftPanel();
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
		
		// Add appointments
		this.drawAppointments();
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
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 1);
		this.weekDateStart = cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
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
		this.column_width = (int) width / 8;
		this.row_height = (int) (splitRightInner.getHeight() - 23) / numRows;
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
	 * Draws the appointments in the calendar
	 */
	
	public void drawAppointments() {
		// Get all appointments from the user
		ArrayList<Appointment> userAppointments = this.calendar.getAppointments();
		
		// Loop all the appointments
		for (int i = 0; i < userAppointments.size(); i++) {
			// Load the current appointment
			Appointment thisAppointment = userAppointments.get(i);
			
			// Check if we are in the right week for this appointment
			if (thisAppointment.getStart().after(this.weekDateStart)) {
				if (thisAppointment.getEnd().before(this.weekDateEnd)) {
					// This appointment should be painted to the calendar, get what weekday
					Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
					c.setTime(thisAppointment.getStart());

					System.out.println(c.get(Calendar.HOUR_OF_DAY));
					int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
					
					// Retarded US has sunday as first day of week...
					if (dayOfWeek == 1) {
						dayOfWeek = 7;
					}
					else {
						dayOfWeek--;
					}
					
					// Get the correct square
					GraphicSquare thisSquare = squareArr[dayOfWeek];
					
					// Get start-point offset
					c = Calendar.getInstance();
					c.setTime(thisAppointment.getStart());
					int startValue = (c.get(Calendar.HOUR_OF_DAY) * 60) + c.get(Calendar.MINUTE) - (7 * 60); // Remove hours before 0800, but adding one hour for the legends
					double startPos = (this.row_height/60.0)*startValue;
					
					// Get the height of the box
					c.setTime(thisAppointment.getEnd());
					int endValue = (c.get(Calendar.HOUR_OF_DAY) * 60) + c.get(Calendar.MINUTE) - (7 * 60); // Remove hours before 0800, but adding one hour for the legends
					double heightValue = (this.row_height/60.0)*endValue - startPos;
					
					// Create new square for this appointment
					GraphicAppointment appointmentSquare = new GraphicAppointment(0, 0, (this.column_width - 1), ((int) heightValue - 1), Color.pink, thisAppointment.getTitle() + ": " + thisAppointment.getDescription());
					appointmentSquare.setId(thisAppointment.getId());
					
					// Reset layout
					appointmentSquare.setLayout(null);
					
					// Setting bounds (not really sure what does that, but this works)
					appointmentSquare.setBounds(1, ((int) startPos + 1), (this.column_width - 1), ((int) heightValue - 1));
					
					// Click
					appointmentSquare.addMouseListener(new MouseAdapter () {
						public void mousePressed(MouseEvent e) {
							// Get object
							GraphicAppointment clickedAppointment = (GraphicAppointment)e.getSource();
							
							// Get id
							int appointmentId = clickedAppointment.getId();
							
							// Call show-method
							showAppointment(appointmentId);
						}
					});
					
					// Mouseover
					appointmentSquare.addMouseMotionListener(appointmentSquare);
					
					
					// Add the block to the square
					thisSquare.add(appointmentSquare);
					
					// Repaint
					thisSquare.repaint();
				}
			}
		}
	}
	
	/*
	 * TODO
	 */
	
	protected void showAppointment(int id) {
		System.out.println("Showing id = " + id);
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
	
	/*
	 * Building left panel-content
	 */
	
	private void buildLeftpanel() {
		// Testing ansatte
		ArrayList<String> derp = new ArrayList<String>();
		derp.add("Thomas Gautvedt");
		derp.add("asdfsdfsf");
		derp.add("43545345");
		derp.add("asd345345345fsdfsf");
		derp.add("a345345345sdfsdfsf");
		derp.add("as3453dfsdfsf");
		derp.add("as345345dfsdfsf");
		derp.add("as455dfsdfsf");
		derp.add("as345dfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		derp.add("a345sdfsdfsf");
		
		
		//
		// Employee - Panel
		//
		
		JPanel innerAnsattePanel = new JPanel();
		
		// Create dynamic RowSpec
		int rowSpecSize = 9 + (derp.size() * 2);
		RowSpec []ansatteRowSpec = new RowSpec[rowSpecSize];
		for (int i = 0; i < rowSpecSize; i++) {
			if (i % 2 == 0) {
				ansatteRowSpec[i] = FormFactory.RELATED_GAP_ROWSPEC;
			}
			else {
				ansatteRowSpec[i] = FormFactory.DEFAULT_ROWSPEC;
			}
		}
		
		innerAnsattePanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
				ansatteRowSpec));
		
		// Setting up static part of the panel
		JLabel ansatteAnsatteText = new JLabel("Ansatte");
		ansatteAnsatteText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		innerAnsattePanel.add(ansatteAnsatteText, "2, 2");
		
		// First seperator
		JSeparator ansatteSeperator = new JSeparator();
		innerAnsattePanel.add(ansatteSeperator, "2, 4, 3, 1");
		
		// My calendar
		JLabel ansatteMyCalendar = new JLabel("Min kalender");
		innerAnsattePanel.add(ansatteMyCalendar, "2, 6");
		
		// Checkbox for Mt calendar that is already selected
		JCheckBox ansatteMyCalendarCheckbox = new JCheckBox("");
		ansatteMyCalendarCheckbox.setSelected(true);
		innerAnsattePanel.add(ansatteMyCalendarCheckbox, "4, 6");
		
		// Second seperator
		JSeparator ansatteSeperator2 = new JSeparator();
		innerAnsattePanel.add(ansatteSeperator2, "2, 8, 3, 1");
		
		// Begin dynamic fill in names in the list
		int ansatteBaseIndex = 10;
		for (int i = 0; i < derp.size(); i++) {
			// Create textfield for the name of the employee
			JLabel ansatteNameList = new JLabel(derp.get(i));
			JCheckBox ansatteNameListCheckbox = new JCheckBox("");
			
			// Set the label for the checkbox (not really sure if this does anything at all?)
			ansatteNameList.setLabelFor(ansatteNameListCheckbox);
			
			// Add the items
			innerAnsattePanel.add(ansatteNameListCheckbox, "4, " + ansatteBaseIndex);
			innerAnsattePanel.add(ansatteNameList, "2, " + ansatteBaseIndex + ", fill, default");
			
			// Increase the base by two
			ansatteBaseIndex+= 2;
		}
		
		// Create new scrollpanel and set the inner content
		ansatteScrollPane = new JScrollPane(innerAnsattePanel);
		ansatteScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ansatteScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		ansatteScrollPane.setPreferredSize(new Dimension (300, 300));
		ansatteScrollPane.setBackground(null);
		ansatteScrollPane.setOpaque(true);
		ansatteScrollPane.setBorder(null);
		
		// Add the panel
		splitLeftInner.add(ansatteScrollPane, BorderLayout.WEST);
		
		//
		// Add/edit - Panel
		//
		
		// TODO
		
		//
		// Notifications - Panel
		//
		
		// TODO
		
		//
		// Info - Panel
		//
		
		// TODO
	}
	
	/*
	 * Set the correct height for the side-panels
	 */
	
	private void setSizesLeftPanel() {
		// Employees
		ansatteScrollPane.setPreferredSize(new Dimension (300, this.splitRightInner.getHeight() + 20));
	}
}
