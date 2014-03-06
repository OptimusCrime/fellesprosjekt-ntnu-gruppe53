package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	protected Gui g;
	
	// Panels, parts of the window
	private JPanel header;
	private JPanel seperator;
	private JSplitPane main;
	
	// Buttons
	private JButton homeBtn;
	private JButton notificationsBtn;
	private JButton employeesBtn;
	
	/*
	 * Constructor
	 */
	
	public ViewMain(Gui gui) {
		// Set gui
		this.g = gui;
		
		// Set close-mode
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set title
		super.setTitle("NTNU Calendar");
		
		// Load size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Set initial size of the window and the relative loaction
	    super.setBounds(100, 100, (int) dim.getWidth(), (int) dim.getHeight());
	    super.setLocationRelativeTo(null);
	    
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
		
		JPanel panel_2 = new JPanel();
		header.add(panel_2, BorderLayout.WEST);
		
		homeBtn = new JButton("Hjem");
		panel_2.add(homeBtn);
		
		notificationsBtn = new JButton("Varsler (2)");
		panel_2.add(notificationsBtn);
		
		employeesBtn = new JButton("Ansatte");
		panel_2.add(employeesBtn);
		
		JPanel panel_3 = new JPanel();
		header.add(panel_3, BorderLayout.EAST);
		
		JButton btnNewButton = new JButton("Logg ut");
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				g.logout();
			}
			
		});
		panel_3.add(btnNewButton);
		
		
		
		
		JSeparator separatdfdfgor = new JSeparator();
		seperator.add(separatdfdfgor);
		getContentPane().add(main);
		
		JPanel panel_5 = new JPanel();
		main.setLeftComponent(panel_5);
		
		JLabel lblNewLabel = new JLabel("New label");
		panel_5.add(lblNewLabel);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		main.setRightComponent(panel_4);
		SpringLayout sl_panel_4 = new SpringLayout();
		panel_4.setLayout(sl_panel_4);
		
		JPanel panel_7 = new JPanel();
		sl_panel_4.putConstraint(SpringLayout.NORTH, panel_7, 55, SpringLayout.NORTH, panel_4);
		sl_panel_4.putConstraint(SpringLayout.SOUTH, panel_7, -24, SpringLayout.SOUTH, panel_4);
		panel_7.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panel_4.putConstraint(SpringLayout.WEST, panel_7, 14, SpringLayout.WEST, panel_4);
		sl_panel_4.putConstraint(SpringLayout.EAST, panel_7, -6, SpringLayout.EAST, panel_4);
		panel_4.add(panel_7);
		panel_7.setLayout(new GridLayout(1, 8, 0, 0));
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panel_4.putConstraint(SpringLayout.NORTH, panel_6, 12, SpringLayout.NORTH, panel_4);
		sl_panel_4.putConstraint(SpringLayout.WEST, panel_6, 20, SpringLayout.WEST, panel_4);
		sl_panel_4.putConstraint(SpringLayout.SOUTH, panel_6, -6, SpringLayout.NORTH, panel_7);
		sl_panel_4.putConstraint(SpringLayout.EAST, panel_6, -4, SpringLayout.EAST, panel_4);
		panel_4.add(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		JButton btnNewButton_4 = new JButton("New button");
		panel_6.add(btnNewButton_4, BorderLayout.EAST);
		
		JButton btnNewButton_5 = new JButton("New button");
		panel_6.add(btnNewButton_5, BorderLayout.WEST);
		
		JLabel lblDerp = new JLabel("Derp");
		lblDerp.setHorizontalAlignment(SwingConstants.CENTER);
		panel_6.add(lblDerp, BorderLayout.NORTH);
	}
	
	/*
	 * Setter for visiblity of the window
	 */
	
	public void setVisible(boolean b) {
		super.setVisible(b);	
	}
}
