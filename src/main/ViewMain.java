package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	private JSplitPane main;
	
	private JPanel splitLeft;
	private JPanel splitLeftInner;
	private JPanel splitRight;
	private JPanel splitRightInner;
	private JPanel splitRightNav;
	
	// For the dyanamic sidepanels
	private Map<String, JScrollPane> scrollPanes;
	private JScrollPane employeeScrollPane;
	private JScrollPane infoScrollPane;
	private JScrollPane homeScrollPane;
	private JScrollPane notificationsScrollPane;
	private JScrollPane addEditScrollPane;
	
	// Buttons
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
	private int calendarYear;
	private Map<String, String> calendarReplaces;
	
	// The different squares
	private GraphicSquare[] squareArr;
	private int column_width;
	private int row_height;
	private Map<Integer, Color> appointmentColors;
	private GraphicSquare lastHoovered;
	
	// Add/Edit
	private JLabel addEditHeaderLabel;
	private JTextField addEditTitle;
	private JTextField addEditDesc;
	private JLabel addEditDate;
	private JComboBox addEditParticipants;
	private JComboBox<String> addEditFrom;
	private JComboBox<String> addEditTo;
	protected JList<Employee> addEditParticipantsAll;
	protected JList<Employee> addEditParticipantsChosen;
	protected JButton addEditParticipantsAllButton;
	protected JButton addEditParticipantsChosenButton;
	protected DefaultListModel<Employee> addEditParticipantsListNotInvited;
	protected DefaultListModel<Employee> addEditParticipantsListInvited;
	private JButton addEditSave;
	private JComboBox addEditRoom;
	private DefaultComboBoxModel<Room> addEditRoomModel;
	private JLabel addEditRoomLabel;
	private JLabel infoAttendingLabel;
	private JLabel infoNotAttendingLabel;
	private boolean infoThisUserAttending;
	private ButtonGroup infoButtonGroup;
	
	// Info
	private JLabel infoHeaderLabel;
	private JLabel infoDescLabel;
	private JLabel infoDate;
	private JLabel infoFrom;
	private JLabel infoTo;
	private JLabel infoParticipants;
	private JLabel infoRoom;
	private JList infoParticipantsChosen;
	private DefaultListModel<String> infoParticipatesStatus;
	private JRadioButton infoAttending;
	private JRadioButton infoNotAttending;
	private Appointment infoShowThisAppointment;
	
	// Debugging
	private JLabel innerInfoTestLabel;
	
	/*
	 * Constructor
	 */
	
	public ViewMain(Gui g, Cal c) {
		// Set gui
		this.gui = g;
		this.calendar = c;
		
		// For effect
		this.lastHoovered = null;
		
		// Derp
		this.infoThisUserAttending = false;
		
		// ListModel for the participants-lists
		addEditParticipantsListNotInvited = new DefaultListModel<Employee>();
		addEditParticipantsListInvited = new DefaultListModel<Employee>();
		infoParticipatesStatus = new DefaultListModel<String>();
		
		// Setting up hashmap for colors
		appointmentColors = new HashMap<Integer, Color>();
		
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
		springLayout.putConstraint(SpringLayout.WEST, header, 0, SpringLayout.WEST, super.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, header, 40, SpringLayout.NORTH, super.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, header, 0, SpringLayout.EAST, super.getContentPane());
		super.getContentPane().add(header);
		header.setLayout(new BorderLayout(0, 0));
		
		// Add main-panel
		main = new JSplitPane();
		main.setDividerSize(0);
		springLayout.putConstraint(SpringLayout.NORTH, main, 10, SpringLayout.SOUTH, header);
		springLayout.putConstraint(SpringLayout.WEST, main, 10, SpringLayout.WEST, super.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, main, -10, SpringLayout.SOUTH, super.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, main, -10, SpringLayout.EAST, super.getContentPane());
		
		// Set main-panel impossible to resize
		main.setEnabled(false);
		
		// Add header-left panel
		JPanel headerLeft = new JPanel();
		header.add(headerLeft, BorderLayout.WEST);
		
		// Add all buttons in the header-left panel
		notificationsBtn = new JButton("Varsler");
		headerLeft.add(notificationsBtn);
		employeesBtn = new JButton("Ansatte");
		headerLeft.add(employeesBtn);
		
		// Add events for all the header-left-buttons
		notificationsBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				displayLeftPanel("notifications");
			}
		});
		employeesBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				displayLeftPanel("employees");
			}
		});
		
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
		
		// Add main here
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
		splitRightLayout.putConstraint(SpringLayout.SOUTH, splitRightInner, 0, SpringLayout.SOUTH, splitRight);
		splitRightLayout.putConstraint(SpringLayout.WEST, splitRightInner, -2, SpringLayout.WEST, splitRight);
		splitRightLayout.putConstraint(SpringLayout.EAST, splitRightInner, -8, SpringLayout.EAST, splitRight);
		splitRight.add(splitRightInner);		
		splitRightInner.setLayout(new GridLayout(1, 8, 0, 0));
		
		// Add inner JPanel to the right-split content that holds the nav
		splitRightNav = new JPanel();
		splitRightLayout.putConstraint(SpringLayout.NORTH, splitRightNav, 10, SpringLayout.NORTH, splitRight);
		splitRightLayout.putConstraint(SpringLayout.WEST, splitRightNav, -6, SpringLayout.WEST, splitRight);
		splitRightLayout.putConstraint(SpringLayout.SOUTH, splitRightNav, -2, SpringLayout.NORTH, splitRightInner);
		splitRightLayout.putConstraint(SpringLayout.EAST, splitRightNav, -6, SpringLayout.EAST, splitRight);
		splitRight.add(splitRightNav);
		splitRightNav.setLayout(new BorderLayout(0, 0));
		
		// Add nav-buttons to the right-split nav-panel
		navLeft = new JButton("<");
		splitRightNav.add(navLeft, BorderLayout.WEST);
		navRight = new JButton(">");
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
	    
	    // Set sizes for left-panel
	 	this.setSizesLeftPanel();
	    
	    // Draw calendar (HAX)
	    this.drawCalendar();
	    
	    // Add appointments
	  	this.drawAppointments();
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
		
		// Set current year
		Date d = new Date(ts.getTime());
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		calendarYear = cal.get(Calendar.YEAR);
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
		navWeek.setText("Uke " + Integer.toString(week) + ", " + this.calendarYear);
		
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
		this.row_height = (int) (splitRightInner.getHeight() - 11) / numRows;
		int height = row_height * numRows;
		
		// Loop over each of the squares (one for each day in the week + one for holding the time)
		for (int i = 0; i < 8; i++) {
			// Create new square
			GraphicSquare square = new GraphicSquare(this, 0, 0, ((i == 7) ? (column_width - 1) : column_width), height, row_height, ((i == 0)? false : true));
			
			// Add listeners
			square.addMouseMotionListener(square);
			square.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// Cast the square
					GraphicSquare thisSquare = (GraphicSquare) e.getSource();
					thisSquare.sendClearAllPreviousHoovered();
					thisSquare.registerHoover();
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {}

				@Override
				public void mousePressed(MouseEvent e) {}

				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
			});
			
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
					box.setBounds(1, (((j + 1) * row_height) - compensateForLineOffset), (column_width - 5), 14);
					
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
		
		super.revalidate();
	}
	
	/*
	 * Draws the appointments in the calendar
	 */
	
	public void drawAppointments() {
		this.clearCalendar();
		this.drawCalendar();
		
		// Clear all objects from all graphics
		for (int i = 0; i < squareArr.length; i++) {
			squareArr[i].clearAllObjs();
		}
		
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
					
					// Get color
					Color thisAppointmentColor;
					if (appointmentColors.containsKey(thisAppointment.getUser())) {
						thisAppointmentColor = appointmentColors.get(thisAppointment.getUser());
					}
					else {
						thisAppointmentColor = new Color(this.generateRandomColorInt(), this.generateRandomColorInt(), this.generateRandomColorInt());
						appointmentColors.put(thisAppointment.getUser(), thisAppointmentColor);
					}
					
					// Create text for the appointment
					String startMin=Integer.toString(thisAppointment.getStart().getMinutes());
					if (startMin.equals("0"))
						startMin="00";
					String endMin=Integer.toString(thisAppointment.getEnd().getMinutes());
					if (endMin.equals("0"))
						endMin="00";
					String thisAppointmentToolTip = "<html>" + thisAppointment.getDescription() + "<br /><br />"
					+ thisAppointment.getStart().getHours()+":"+startMin+" - "
					+ thisAppointment.getEnd().getHours()+":"+endMin+"</html>";
					
					// Create new square for this appointment
					thisSquare.addObj(new GraphicAppointment(0, 0, (this.column_width - 1), ((int) heightValue - 1), thisAppointmentColor, thisAppointment.getTitle(), thisAppointmentToolTip));
					thisSquare.getLastObj().setId(thisAppointment.getId());
					
					// Reset layout
					thisSquare.getLastObj().setLayout(null);
					
					// Setting bounds (not really sure what does that, but this works)
					thisSquare.getLastObj().setBounds(1, ((int) startPos + 1), (this.column_width - 1), ((int) heightValue - 1));
					
					// Click
					thisSquare.getLastObj().addMouseListener(new MouseAdapter () {
						public void mousePressed(MouseEvent e) {
							// Get object
							GraphicAppointment clickedAppointment = (GraphicAppointment)e.getSource();
							
							// Get id
							int appointmentId = clickedAppointment.getId();
							
							// Call show-method
							showAppointment(appointmentId);
						}
					});
					
					// Add the block to the square
					thisSquare.add(thisSquare.getLastObj());
					
					
					// Repaint
					thisSquare.repaint();
					thisSquare.revalidate();
				}
			}
		}
		
		// Add plusses
		this.drawPlusSymboles();
	}
	
	/*
	 * Draws + that apprear when hoovering over the calendar
	 */
	
	private void drawPlusSymboles() {
		// Some variables we need
		int width = splitRightInner.getWidth();
		int numRows = 10;
		this.column_width = (int) width / 8;
		this.row_height = (int) (splitRightInner.getHeight() - 23) / numRows;
		
		String plussSymbolesHours[] = new String[]{"08", "09", "10", "11", "12", "13", "14", "15", "16", "17"};
		
		// Loop all the squares
		for (int i = 1; i < squareArr.length; i++) {
			// Get the current square
			GraphicSquare thisSquare = squareArr[i];
			
			// Loop all nine hours to display + - sign for
			for (int j = 1; j <= 9; j++) {
				GraphicsLabel plusSignLabel = new GraphicsLabel("+");
				plusSignLabel.setBounds(this.column_width - 14, ((this.row_height+1) * j + 2), 14, 14);
				plusSignLabel.setVisible(false);
				plusSignLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				plusSignLabel.setTime(plussSymbolesHours[j - 1] + ":00");
				plusSignLabel.setDate(calendarText[i - 1]);
				plusSignLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {  
						// Get the right object
						GraphicsLabel clickedPlusSignLabel = (GraphicsLabel) e.getSource();
						
						// Show new-screen
						showNewAppointment(clickedPlusSignLabel.getTime(), clickedPlusSignLabel.getDate());
					} 
				});
				
				// Add pluss to square-object
				squareArr[i].addLabel(plusSignLabel);
				
				// Add to square
				squareArr[i].add(plusSignLabel);
			}
			
			// Repaint 
			thisSquare.revalidate();
		}
	}
	
	/*
	 * Adds all the employees to the list
	 */
	
	public void drawEmployees(ArrayList<Employee> employees) {		
		//
		// Employee - Panel
		//
		
		JPanel innerEmployeePanel = new JPanel();
		
		// Create dynamic RowSpec
		int rowSpecSize = 9 + ((employees.size() - 1) * 2);
		RowSpec []ansatteRowSpec = new RowSpec[rowSpecSize];
		for (int i = 0; i < rowSpecSize; i++) {
			if (i % 2 == 0) {
				ansatteRowSpec[i] = FormFactory.RELATED_GAP_ROWSPEC;
			}
			else {
				ansatteRowSpec[i] = FormFactory.DEFAULT_ROWSPEC;
			}
		}
		
		innerEmployeePanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
				ansatteRowSpec));
		
		// Setting up static part of the panel
		JLabel employeeText = new JLabel("Ansatte");
		employeeText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		innerEmployeePanel.add(employeeText, "2, 2");
		
		// First seperator
		JSeparator employeeSeperator = new JSeparator();
		innerEmployeePanel.add(employeeSeperator, "2, 4, 3, 1");
		
		// My calendar
		JLabel employeeMyCalendar = new JLabel("Min kalender");
		innerEmployeePanel.add(employeeMyCalendar, "2, 6");
		
		// Checkbox for My calendar that is already selected
		JCheckBox employeeMyCalendarCheckbox = new JCheckBox("");
		employeeMyCalendarCheckbox.setSelected(true);
		employeeMyCalendarCheckbox.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Get current user as an employee
				ArrayList<Employee> tempEmployeeList = calendar.getEmployees();
				Employee tempThisUserAsEmployee = null;
				String tempCurrentUsername = calendar.getUsername();
				for (int i = 0; i < tempEmployeeList.size(); i++) {
					if (tempEmployeeList.get(i).getEmail().equals(tempCurrentUsername)) {
						tempThisUserAsEmployee = tempEmployeeList.get(i);
					}
				}
				
				if (tempThisUserAsEmployee != null) {
					// Get the current checkbox being clicked
					JCheckBox thisCheckbox = (JCheckBox) e.getSource();
					
					// Just doublecheck that we are not NullPointer!
					if (thisCheckbox != null) {
						// Check if checked or not
						if (thisCheckbox.isSelected()) {
							calendar.addCalendar(tempThisUserAsEmployee);
						}
						else {
							calendar.removeCalendar(tempThisUserAsEmployee);
						}
					}
				}
			}
			
		});
		innerEmployeePanel.add(employeeMyCalendarCheckbox, "4, 6");
		
		// Second seperator
		JSeparator employeeSeperator2 = new JSeparator();
		innerEmployeePanel.add(employeeSeperator2, "2, 8, 3, 1");
		
		// Begin dynamic fill in names in the list
		int ansatteBaseIndex = 10;
		for (int i = 0; i < employees.size(); i++) {
			// Check if self
			if (!employees.get(i).isCurrentUser()) {
				// Create textfield for the name of the employee
				JLabel employeeNameList = new JLabel(employees.get(i).getName());
				GraphicCheckbox employeeNameListCheckbox = new GraphicCheckbox("");
				
				// Set reference to the employee
				employeeNameListCheckbox.setReference(employees.get(i));
				employeeNameListCheckbox.addActionListener(new ActionListener () {

					@Override
					public void actionPerformed(ActionEvent e) {
						// Get the current checkbox being clicked
						GraphicCheckbox thisCheckbox = (GraphicCheckbox) e.getSource();
						
						// Just doublecheck that we are not NullPointer!
						if (thisCheckbox != null) {
							// Check if checked or not
							if (thisCheckbox.isSelected()) {
								calendar.addCalendar(thisCheckbox.getReference());
							}
							else {
								calendar.removeCalendar(thisCheckbox.getReference());
							}
						}
					}
					
				});
				
				// Set the label for the checkbox (not really sure if this does anything at all?)
				employeeNameList.setLabelFor(employeeNameListCheckbox);
				
				// Add the items
				innerEmployeePanel.add(employeeNameListCheckbox, "4, " + ansatteBaseIndex);
				innerEmployeePanel.add(employeeNameList, "2, " + ansatteBaseIndex + ", fill, default");
				
				// Increase the base by two
				ansatteBaseIndex += 2;
			}
		}
		
		// Create new scrollpanel and set the inner content
		employeeScrollPane = new JScrollPane(innerEmployeePanel);
		employeeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		employeeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		employeeScrollPane.setPreferredSize(new Dimension (300, 300));
		employeeScrollPane.setBackground(null);
		employeeScrollPane.setOpaque(true);
		employeeScrollPane.setBorder(null);
		homeScrollPane.setVisible(false);
		employeeScrollPane.setVisible(true);
		
		// Add the panel
		splitLeftInner.add(employeeScrollPane, BorderLayout.WEST);
		scrollPanes.put("employees", employeeScrollPane);
		
		// Update changes
		this.setSizesLeftPanel();
		
		//
		// Add/Edit
		//
		
		for (int i = 0; i < employees.size(); i++) {
			if (!employees.get(i).getEmail().equals(this.calendar.getUsername())) {
				addEditParticipantsListNotInvited.addElement(employees.get(i));
			}
		}
	}
	
	/*
	 * Display screen for creating new appointment
	 */
	
	protected void showNewAppointment(String t, String d) {
		// Set hours
		this.addEditFrom.setSelectedItem(t);
		this.addEditTo.setSelectedItem(t.replace("00", "45"));
		
		// Set date
		String []dateSplit = d.split(" ");
		this.addEditDate.setText(dateSplit[1] + "." + this.calendarYear);
		
		// Load rooms
		this.calculateAvailableRooms();
		
		// Display the right left-panel
		this.displayLeftPanel("addedit");
	}
	
	/*
	 * Display information about one appointment
	 */
	
	protected void showAppointment(int id) {
		// Find appointment
		Appointment thisAppointment = null;
		ArrayList<Appointment> tempAppointmentList = this.calendar.getAppointments();
		for (int i = 0; i < tempAppointmentList.size(); i++) {
			if (tempAppointmentList.get(i).getId() == id) {
				thisAppointment = tempAppointmentList.get(i);
				break;
			}
		}
		
		// Check if any appointment was returned
		if (thisAppointment != null) {
			// Store the current appointment in variable
			this.infoShowThisAppointment = thisAppointment;
			
			// Set the different fields
			infoHeaderLabel.setText(thisAppointment.getTitle());
			infoDescLabel.setText(thisAppointment.getDescription());
			
			// Date
			SimpleDateFormat formatEngToNor = new SimpleDateFormat("E");
			SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM");
			Calendar cal = Calendar.getInstance();
			cal.setTime(thisAppointment.getStart());
			
			String tempDate;
			if (calendarReplaces.get(formatEngToNor.format(cal.getTime())) != null) {
				tempDate = calendarReplaces.get(formatEngToNor.format(cal.getTime()));
			}
			else {
				tempDate = formatEngToNor.format(cal.getTime());
			}
			tempDate += " " + formatDate.format(cal.getTime());
			infoDate.setText(tempDate);
			
			// Hours
			infoFrom.setText(((cal.get(Calendar.HOUR_OF_DAY) < 10) ? "0" : "") + cal.get(Calendar.HOUR_OF_DAY) + ":" + ((cal.get(Calendar.MINUTE) < 10) ? "0" : "") + cal.get(Calendar.MINUTE));
			
			cal.setTime(thisAppointment.getEnd());
			infoTo.setText(((cal.get(Calendar.HOUR_OF_DAY) < 10) ? "0" : "") + cal.get(Calendar.HOUR_OF_DAY) + ":" + ((cal.get(Calendar.MINUTE) < 10) ? "0" : "") + cal.get(Calendar.MINUTE));
			
			// Room
			infoRoom.setText(thisAppointment.getRoomString()); 
			
			// Clear the participant-list
			infoParticipatesStatus.clear();
			infoParticipatesStatus.add(0, "Laster...");
			infoParticipantsChosen.repaint();
			
			// Check if we should load participates
			if (!thisAppointment.hasLoadedParticipates()) {
				this.calendar.loadParticipates(thisAppointment.getId());
			}
			else {
				this.participatesLoaded(thisAppointment.getParticipatesList(), thisAppointment.getParticipatesStatus());
			}
			
			// Display the correct sidepanel
			this.displayLeftPanel("info");
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
	
	/*
	 * Building left panel-content
	 */
	
	private void buildLeftpanel() {
		
		//
		// Init the list
		//
		
		scrollPanes = new HashMap<String, JScrollPane>();
		
		//
		// Add/edit - Panel
		//
		
		JPanel innerAddEditPanel = new JPanel();
		
		innerAddEditPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				}));
		
		 // Header
		addEditHeaderLabel = new JLabel("Legg til");
		addEditHeaderLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		innerAddEditPanel.add(addEditHeaderLabel, "1, 2, 3, 1");
		
		// Add all seperators
		JSeparator addEditSeperator1 = new JSeparator();
		innerAddEditPanel.add(addEditSeperator1, "1, 4, 3, 1");
		JSeparator addEditSeperator2 = new JSeparator();
		innerAddEditPanel.add(addEditSeperator2, "1, 11, 3, 1");
		JSeparator addEditSeperator3 = new JSeparator();
		innerAddEditPanel.add(addEditSeperator3, "1, 17, 3, 1");
		JSeparator addEditSeperator4 = new JSeparator();
		innerAddEditPanel.add(addEditSeperator4, "1, 20, 3, 1");
		JSeparator addEditSeperator5 = new JSeparator();
		innerAddEditPanel.add(addEditSeperator5, "1, 26, 3, 1");
		
		// Label for title
		JLabel addEditTitleLabel = new JLabel("Tittel");
		innerAddEditPanel.add(addEditTitleLabel, "1, 6, 3, 1");
		
		// TextField for the title
		addEditTitle = new JTextField();
		innerAddEditPanel.add(addEditTitle, "1, 7, 3, 1, fill, default");
		addEditTitle.setColumns(10);
		
		// Label for description
		JLabel addEditDescLabel = new JLabel("Beskrivelse");
		innerAddEditPanel.add(addEditDescLabel, "1, 9, 3, 1");
		
		// TextField for the description
		addEditDesc = new JTextField();
		innerAddEditPanel.add(addEditDesc, "1, 10, 3, 1, fill, default");
		addEditDesc.setColumns(10);
		
		// Label for the date (not containing the actual date)
		JLabel addEditDateLabel = new JLabel("Dato:");
		innerAddEditPanel.add(addEditDateLabel, "1, 12");
		
		// Label for the date (the date itself)
		addEditDate = new JLabel("Laster");
		innerAddEditPanel.add(addEditDate, "3, 12");
		
		// Creating list with the hours
		ArrayList<String> addEditHours = new ArrayList<String>();
		String hoursInner[] = new String[] {"00", "15", "30", "45"};
		for (int i = 8; i < 17; i++) {
			for (int j = 0; j < 4; j++) {
				addEditHours.add(((i < 10) ? "0" : "") + i + ":" + hoursInner[j]);
			}	
		}
		addEditHours.add("17:00");
		
		// Label for from-time
		JLabel addEditFromLabel = new JLabel("Fra:");
		innerAddEditPanel.add(addEditFromLabel, "1, 14, left, default");
		
		// From combobox
		addEditFrom = new JComboBox(addEditHours.toArray());
		addEditFrom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				calculateAvailableRooms();
			}
		});
		innerAddEditPanel.add(addEditFrom, "3, 14, fill, default");
		
		// Label for to-time
		JLabel addEditToLabel = new JLabel("Til:");
		innerAddEditPanel.add(addEditToLabel, "1, 16, left, default");
		
		// To combobox
		addEditTo = new JComboBox(addEditHours.toArray());
		addEditTo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				calculateAvailableRooms();
			}
		});
		innerAddEditPanel.add(addEditTo, "3, 16, fill, default");
		
		// Label for participants
		JLabel addEditParticipantsLabel = new JLabel("Deltakere:");
		innerAddEditPanel.add(addEditParticipantsLabel, "1, 18, left, default");
		
		// Create array with number of participants
		ArrayList<Integer> participatntList = new ArrayList<Integer>();
		for (int i = 0; i < 201; i++) {
			participatntList.add(i);
		}
		
		// Combobox with participants
		addEditParticipants = new JComboBox(participatntList.toArray());
		addEditParticipants.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				calculateAvailableRooms();
			}
		});
		innerAddEditPanel.add(addEditParticipants, "3, 18, fill, default");
		
		// Label for rom
		addEditRoomLabel = new JLabel("Rom:");
		innerAddEditPanel.add(addEditRoomLabel, "1, 19, left, default");
		
		// Combobox with rooms
		addEditRoomModel = new DefaultComboBoxModel<Room>();
		addEditRoom = new JComboBox(addEditRoomModel);
		innerAddEditPanel.add(addEditRoom, "3, 19, fill, default");
		
		// Label for participants (lists)
		JLabel addEditParticipantsLabel2 = new JLabel("Deltakere:");
		innerAddEditPanel.add(addEditParticipantsLabel2, "1, 21, 3, 1");
		
		// Create border for the JList
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		
		// Create List for the participants not invited
		addEditParticipantsAll = new JList();
		addEditParticipantsAll.setModel(addEditParticipantsListNotInvited);
		addEditParticipantsAll.setBorder(border);
		addEditParticipantsAll.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				addEditParticipantsAllButton.setEnabled(true);
			}
		});
		addEditParticipantsAll.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane addEditParticipantsAllScrollPane = new JScrollPane(addEditParticipantsAll);
		innerAddEditPanel.add(addEditParticipantsAllScrollPane, "1, 22, 3, 1");
		
		// Add button for participants not invited
		addEditParticipantsAllButton = new JButton("Legg til");
		addEditParticipantsAllButton.setEnabled(false);
		addEditParticipantsAllButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Check if anything is selected
				if (!addEditParticipantsAll.isSelectionEmpty()) {
					// Get selected employee
					Employee chosenEmployee = (Employee) addEditParticipantsAll.getSelectedValue();
					
					// Remove from not invited
					addEditParticipantsListNotInvited.removeElement(chosenEmployee);
					
					// Include in invited
					addEditParticipantsListInvited.addElement(chosenEmployee);
					
					// Set buttons not active
					addEditParticipantsAllButton.setEnabled(false);
					addEditParticipantsChosenButton.setEnabled(false);
					
					// Remove selection from list
					addEditParticipantsAll.clearSelection();
				}
			}
		});
		innerAddEditPanel.add(addEditParticipantsAllButton, "3, 23, right, default");
		
		// Create List for the participants invited
		addEditParticipantsChosen = new JList();
		addEditParticipantsChosen.setModel(addEditParticipantsListInvited);
		addEditParticipantsChosen.setBorder(border);
		addEditParticipantsChosen.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				addEditParticipantsChosenButton.setEnabled(true);
			}
		});
		addEditParticipantsChosen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane addEditParticipantsChosenScrollPane = new JScrollPane(addEditParticipantsChosen);
		innerAddEditPanel.add(addEditParticipantsChosenScrollPane, "1, 24, 3, 1");
		
		// Add button for participants invited
		addEditParticipantsChosenButton = new JButton("Fjern");
		addEditParticipantsChosenButton.setEnabled(false);
		addEditParticipantsChosenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Check if anything is selected
				if (!addEditParticipantsChosen.isSelectionEmpty()) {
					// Get selected employee
					Employee chosenEmployee = (Employee) addEditParticipantsChosen.getSelectedValue();
					
					// Remove from not invited
					addEditParticipantsListInvited.removeElement(chosenEmployee);
					
					// Include in invited
					addEditParticipantsListNotInvited.addElement(chosenEmployee);
					
					// Set buttons not active
					addEditParticipantsAllButton.setEnabled(false);
					addEditParticipantsChosenButton.setEnabled(false);
					
					// Remove selection from list
					addEditParticipantsChosen.clearSelection();
				}
			}
		});
		innerAddEditPanel.add(addEditParticipantsChosenButton, "3, 25, right, default");
		
		// Create the save-button
		addEditSave = new JButton("Lagre");
		addEditSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean error = false;
				String errorMsg = "";
				if (addEditTitle.getText().length() == 0) {
					error = true;
					errorMsg += "- Tittel\n";
				}
				if (addEditDesc.getText().length() == 0) {
					error = true;
					errorMsg += "- Beskrivelse\n";
				}
				if (addEditFrom.getSelectedItem().equals(addEditTo.getSelectedItem())) {
					error = true;
					errorMsg += "- Tidene må være forskjellige\n";
				}
				
				if (error) {
					JOptionPane.showMessageDialog(null, "Vennligst fyll ut manglende informasjon:\n\n" + errorMsg, "Feil", JOptionPane.PLAIN_MESSAGE);
				}
				else {
					// Get all info
					String title = addEditTitle.getText();
					String description = addEditDesc.getText();
					
					// From, to
					String []fromTime = ((String) addEditFrom.getSelectedItem()).split(":");
					String []toTime = ((String) addEditTo.getSelectedItem()).split(":");
					int num = (int) addEditParticipants.getSelectedItem();
					String []date = addEditDate.getText().split("\\.");
					
					// From
					Calendar cal = Calendar.getInstance();
					cal.set(calendarYear, (Integer.parseInt(date[1]) - 1), Integer.parseInt(date[0]), Integer.parseInt(fromTime[0]), Integer.parseInt(fromTime[1]));
					Date fromTimeAsDate = cal.getTime();
					
					// To
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(toTime[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(toTime[1]));
					Date toTimeAsDate = cal.getTime();
					
					int participants = (int) addEditParticipants.getSelectedItem();
					Room room = (Room) addEditRoom.getSelectedItem();
					
					// Participants
					ArrayList<Employee> participantsArr = new ArrayList<Employee>();
					for (int i = 0; i < addEditParticipantsListInvited.size(); i++) {
						participantsArr.add(addEditParticipantsListInvited.get(i));
					}
					
					// Send info
					calendar.createAppointment(
							title, description,
							fromTimeAsDate, toTimeAsDate,
							participants, room,
							participantsArr);
					
					// Reset fields
					addEditTitle.setText("");
					addEditDesc.setText("");
					
					// Switch panel
					displayLeftPanel("notifications");
				}
			}
		});
		innerAddEditPanel.add(addEditSave, "3, 27, right, default");
		
		// Add the panel itself
		addEditScrollPane = new JScrollPane(innerAddEditPanel);
		addEditScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		addEditScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		addEditScrollPane.setPreferredSize(new Dimension (300, 300));
		addEditScrollPane.setBackground(null);
		addEditScrollPane.setOpaque(true);
		addEditScrollPane.setBorder(null);
		addEditScrollPane.setVisible(false);
		
		// Add the panel
		splitLeftInner.add(addEditScrollPane, BorderLayout.WEST);
		scrollPanes.put("addedit", addEditScrollPane);
		
		//
		// Notifications - Panel
		//
		
		JPanel innerNotificationsPanel = new JPanel();
		
		// Add dummy
		JLabel dummy3 = new JLabel("Notifications goes here");
		innerNotificationsPanel.add(dummy3);
		
		notificationsScrollPane = new JScrollPane(innerNotificationsPanel);
		notificationsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		notificationsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		notificationsScrollPane.setPreferredSize(new Dimension (300, 300));
		notificationsScrollPane.setBackground(null);
		notificationsScrollPane.setOpaque(true);
		notificationsScrollPane.setBorder(null);
		notificationsScrollPane.setVisible(false);
		
		// Add the panel
		splitLeftInner.add(notificationsScrollPane, BorderLayout.WEST);
		scrollPanes.put("notifications", notificationsScrollPane);
		
		//
		// Info - Panel
		//
		
		JPanel innerInfoPanel = new JPanel();
		
		innerInfoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				}));
		
		 // Header
		infoHeaderLabel = new JLabel("[Tittel her]");
		infoHeaderLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		innerInfoPanel.add(infoHeaderLabel, "1, 2, 3, 1");
		
		// Add all seperators
		JSeparator infoSeperator1 = new JSeparator();
		innerInfoPanel.add(infoSeperator1, "1, 4, 3, 1");
		JSeparator infoSeperator2 = new JSeparator();
		innerInfoPanel.add(infoSeperator2, "1, 7, 3, 1");
		JSeparator infoSeperator3 = new JSeparator();
		innerInfoPanel.add(infoSeperator3, "1, 13, 3, 1");
		JSeparator infoSeperator4 = new JSeparator();
		innerInfoPanel.add(infoSeperator4, "1, 16, 3, 1");
		JSeparator infoSeperator5 = new JSeparator();
		innerInfoPanel.add(infoSeperator5, "1, 19, 3, 1");
		
		// Label for description
		infoDescLabel = new JLabel("Beskrivelse");
		innerInfoPanel.add(infoDescLabel, "1, 5, 3, 1");
		
		// Label for the date (not containing the actual date)
		JLabel infoDateLabel = new JLabel("Dato:");
		innerInfoPanel.add(infoDateLabel, "1, 8");
		
		// Label for the date (the date itself)
		infoDate = new JLabel("");
		innerInfoPanel.add(infoDate, "3, 8");
		
		// Label for from-time
		JLabel infoFromLabel = new JLabel("Fra:");
		innerInfoPanel.add(infoFromLabel, "1, 10, left, default");
		
		// From label
		infoFrom = new JLabel("16:00");
		innerInfoPanel.add(infoFrom, "3, 10, fill, default");
		
		// Label for to-time
		JLabel infoToLabel = new JLabel("Til:");
		innerInfoPanel.add(infoToLabel, "1, 12, left, default");
		
		// To label
		infoTo = new JLabel("");
		innerInfoPanel.add(infoTo, "3, 12, fill, default");
		
		// Label for participants
		JLabel infoParticipantsLabel = new JLabel("Deltakere:");
		innerInfoPanel.add(infoParticipantsLabel, "1, 14, left, default");
		
		// Combobox with participants
		infoParticipants = new JLabel("");
		innerInfoPanel.add(infoParticipants, "3, 14, fill, default");
		
		// Label for rom
		JLabel infoRoomLabel = new JLabel("Rom:");
		innerInfoPanel.add(infoRoomLabel, "1, 15, left, default");
		
		// Label with room
		infoRoom = new JLabel("");
		innerInfoPanel.add(infoRoom, "3, 15, fill, default");
		
		// Label for participants (lists)
		JLabel infoParticipantsLabel2 = new JLabel("Deltakere:");
		innerInfoPanel.add(infoParticipantsLabel2, "1, 17, 3, 1");
		
		// Create border for the JList
		Border infoBorder = BorderFactory.createLineBorder(Color.BLACK);
		
		// Create List for the participants that are invited
		infoParticipantsChosen = new JList();
		infoParticipantsChosen.setBorder(infoBorder);
		infoParticipantsChosen.setModel(infoParticipatesStatus);
		infoParticipantsChosen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane infoParticipantsChosenScrollPane = new JScrollPane(infoParticipantsChosen);
		innerInfoPanel.add(infoParticipantsChosenScrollPane, "1, 18, 3, 1");
		
		// Kommer, kommer ikke
		infoButtonGroup = new ButtonGroup();
		
		infoAttending = new JRadioButton("Kommer");
		infoButtonGroup.add(infoAttending);
		innerInfoPanel.add(infoAttending, "1, 20, left, default");
		
		infoNotAttending = new JRadioButton("Kommer ikke");
		infoButtonGroup.add(infoNotAttending);
		innerInfoPanel.add(infoNotAttending, "1, 21, left, default");
		
		// Hide them all!
		infoAttending.setVisible(false);
		infoNotAttending.setVisible(false);
		
		// ActionListener
		infoAttending.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton thisRadio = (JRadioButton) e.getSource();
				updateThisUserDoesParticipate(thisRadio.getText());
				
			}
		});
		infoNotAttending.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton thisRadio = (JRadioButton) e.getSource();
				updateThisUserDoesParticipate(thisRadio.getText());
				
			}
		});
		
		// Add to scrollpane
		infoScrollPane = new JScrollPane(innerInfoPanel);
		infoScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		infoScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		infoScrollPane.setPreferredSize(new Dimension (300, 300));
		infoScrollPane.setBackground(null);
		infoScrollPane.setOpaque(true);
		infoScrollPane.setBorder(null);
		infoScrollPane.setVisible(false);
		
		// Add the panel
		splitLeftInner.add(infoScrollPane, BorderLayout.WEST);
		scrollPanes.put("info", infoScrollPane);
		
		//
		// Home - Panel
		//
		
		JPanel innerHomePanel = new JPanel();
		
		homeScrollPane = new JScrollPane(innerHomePanel);
		homeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		homeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		homeScrollPane.setPreferredSize(new Dimension (300, 300));
		homeScrollPane.setBackground(null);
		homeScrollPane.setOpaque(true);
		homeScrollPane.setBorder(null);
		homeScrollPane.setVisible(true);
		
		// Add the panel
		splitLeftInner.add(homeScrollPane, BorderLayout.WEST);
		scrollPanes.put("home", homeScrollPane);
	}
	
	/*
	 * Set the correct height for the side-panels
	 */
	
	private void setSizesLeftPanel() {
		// Info
		infoScrollPane.setPreferredSize(new Dimension (300, this.splitRightInner.getHeight() + 20));
		
		// Home
		homeScrollPane.setPreferredSize(new Dimension (300, this.splitRightInner.getHeight() + 20));
		
		// Notifications
		notificationsScrollPane.setPreferredSize(new Dimension (300, this.splitRightInner.getHeight() + 20));
		
		// Add/Edit
		addEditScrollPane.setPreferredSize(new Dimension (300, this.splitRightInner.getHeight() + 20));
		
		// Employees
		if (employeeScrollPane != null) {
			employeeScrollPane.setPreferredSize(new Dimension (300, this.splitRightInner.getHeight() + 20));
		}
		
		// Redraw
		splitLeftInner.revalidate();
	}
	
	/*
	 * Method to show/hide dynamic sidepanels
	 */
	
	protected void displayLeftPanel(String n) {
		// Get all the keys
		Set<String> leftPanelKeys = scrollPanes.keySet();
		
		// Loop all the keys
		for (String s : leftPanelKeys) {
			// Set hidden to all
			JScrollPane panelToHide = scrollPanes.get(s);
			if (panelToHide != null) {
				panelToHide.setVisible(false);
			}
		}
		
		// Get panel to display
		JScrollPane panelToView = scrollPanes.get(n);
		if (panelToView != null) {
			// Set visible
			panelToView.setVisible(true);
			
			// Redraw super to show the changes!
			super.revalidate();
		}
	}
	
	/*
	 * Generate random number between 0, 225
	 */
	
	private int generateRandomColorInt() {
		return (int) (Math.random() * (225 + 1));
	}
	
	/*
	 * Hoovered
	 */
	
	public void setLastHoovered(GraphicSquare g) {
		this.lastHoovered = g;
	}
	
	public GraphicSquare getLastHoovered() {
		return this.lastHoovered;
	}
	
	/*
	 * Calculate available rooms
	 */
	
	protected void calculateAvailableRooms() {
		// Get info
		String []fromTime = ((String) addEditFrom.getSelectedItem()).split(":");
		String []toTime = ((String) addEditTo.getSelectedItem()).split(":");
		int num = (int) addEditParticipants.getSelectedItem();
		String []date = this.addEditDate.getText().split("\\.");
		
		if (date.length > 1) {
			// From
			Calendar cal = Calendar.getInstance();
			cal.set(this.calendarYear, Integer.parseInt(date[1]), Integer.parseInt(date[0]), Integer.parseInt(fromTime[0]), Integer.parseInt(fromTime[1]));
			Date fromTimeAsDate = cal.getTime();
			
			// To
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(toTime[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(toTime[1]));
			Date toTimeAsDate = cal.getTime();
			
			// Set loadingtime
			addEditRoomLabel.setText("Laster rom...");
			
			this.calendar.calculateAvailabelRooms(fromTimeAsDate, toTimeAsDate, num);
		}
	}
	
	/*
	 * Update combobox with rooms
	 */
	
	public void updateAvailableRooms (ArrayList<Room> rooms) {
		addEditRoomModel.removeAllElements();
		
		for (int i = 0; i < rooms.size(); i++) {
			addEditRoomModel.addElement(rooms.get(i));
		}
		addEditRoom.setSelectedIndex(0);
		
		// Reset loading
		addEditRoomLabel.setText("Rom:");
	}
	
	/*
	 * Update participates-list
	 */
	
	public void participatesLoaded(ArrayList<Employee> list, ArrayList<String> status) {
		// Clear the list
		infoParticipatesStatus.clear();
		
		// Set to false
		this.infoThisUserAttending = false;
		
		// Derp
		this.infoButtonGroup.clearSelection();
		
		// Look the list
		for (int i = 0; i < list.size(); i++) {
			// Check if the current user is in this appointment
			if (list.get(i).getEmail().equals(this.calendar.getUsername())) {
				this.infoThisUserAttending = true;
				
				infoAttending.setVisible(true);
				infoNotAttending.setVisible(true);
				
				if (status.get(i).equals("Kommer")) {
					infoAttending.setSelected(true);
				}
				else {
					if (status.get(i).equals("Kommer ikke")) {
						infoNotAttending.setSelected(true);
					}
				}
			}
			
			// Add to list
			infoParticipatesStatus.add(i, list.get(i).getName() + " [" + status.get(i) + "]");
		}
		
		// Lena
		if (this.infoThisUserAttending == true) {
			infoAttending.setVisible(true);
			infoNotAttending.setVisible(true);
		}
		
		// Repaint
		infoParticipantsChosen.repaint();
		infoAttending.repaint();
		infoNotAttending.repaint();
	}
	
	/*
	 * Update the status for the current user
	 */
	
	public void updateThisUserDoesParticipate(String s) {
		// Set does attend locally
		for (int i = 0; i < infoShowThisAppointment.getParticipatesList().size(); i++) {
			// Check if the current user is in this appointment
			if (infoShowThisAppointment.getParticipatesList().get(i).getEmail().equals(this.calendar.getUsername())) {
				infoShowThisAppointment.updateParticipateStatus(infoShowThisAppointment.getParticipatesList().get(i), s);
			}
		}
		
		// Send to server
		this.calendar.updateThisUserDoesParticipate(infoShowThisAppointment.getId(), s);
	}
}
