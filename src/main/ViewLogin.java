package main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/*
 * ViewLogin
 * 
 * The loginview
 * 
 */

public class ViewLogin extends JFrame {
	
	/*
	 * Variables
	 */
	
	protected Gui g;
	private static final long serialVersionUID = 1L;
	protected JTextField textField1;
	protected JTextField textField2;
	protected JTextField textField3;
	protected JPasswordField textField4;
	
	/*
	 * Constructor
	 */
	
	public ViewLogin(Gui gui) {
		// Set gui
		this.g = gui;
		
		// Set close-operation
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set title
		super.setTitle("NTNU Calendar - Login");
		
		// Set size
		super.setPreferredSize(new Dimension(500, 300));
		
		// Set layout TODO
		super.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		// Add all labels
		JLabel label1 = new JLabel("Server:");
		super.getContentPane().add(label1, "4, 4, right, default");
		JLabel label2 = new JLabel("Port:");
		super.getContentPane().add(label2, "4, 6, right, default");
		JLabel label3 = new JLabel("E-post");
		super.getContentPane().add(label3, "4, 10, right, default");
		JLabel label4 = new JLabel("Passord");
		super.getContentPane().add(label4, "4, 12, right, default");
		
		// Add all textfields
		textField1 = new JTextField("localhost");
		super.getContentPane().add(textField1, "6, 4, fill, default");
		textField1.setColumns(10);
		textField2 = new JTextField("8080");
		super.getContentPane().add(textField2, "6, 6, fill, default");
		textField2.setColumns(10);
		textField3 = new JTextField();
		super.getContentPane().add(textField3, "6, 10, fill, default");
		textField3.setColumns(10);
		textField4 = new JPasswordField();
		super.getContentPane().add(textField4, "6, 12, fill, default");
		textField4.setColumns(10);
		
		// Tweak textfields
		
		// Add button
		JButton button1 = new JButton("Logg inn");
		button1.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Button was clicked, check input
				String errors = "";
				
				if (textField1.getText().length() == 0) {
					errors += "- Server\n";
				}
				if (textField2.getText().length() == 0) {
					errors += "- Port\n";
				}
				if (textField3.getText().length() == 0) {
					errors += "- E-post\n";
				}
				if (textField4.getPassword().length == 0) {
					errors += "- Passord\n";
				}
				
				// Check if we had any errors
				if (errors.length() > 0) {
					JOptionPane.showMessageDialog(null, "Vennligst fyll ut manglende informasjon:\n\n" + errors, "Feil", JOptionPane.PLAIN_MESSAGE);
				}
				else {
					// Let's try to connect to the database
					if (g.testConnection(textField1.getText(), Integer.parseInt(textField2.getText()))) {
						// Connect OK
						g.showHome();
					}
					else {
						// Something's fucked up
						System.out.println("no");
					}
				}
			}
		});
		super.getContentPane().add(button1, "6, 14");
		
		
		// Pack everything
		super.pack();
	}
	
	/*
	 * Public setter for setting visible state
	 */
	
	public void setVisible(boolean b) {
		super.setVisible(b);	
	}
}
