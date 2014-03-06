package main;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

public class ViewLogin extends JPanel {
	
	/*
	 * Variables
	 */
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	
	/*
	 * Constructor
	 */
	
	public ViewLogin() {
		// Init new frame
		frame = new JFrame();
		
		// Set close-operation
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set title
		frame.setTitle("NTNU Calendar - Login");
		
		// Set size
		frame.setPreferredSize(new Dimension(500, 300));
		
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
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
		
		JLabel lblNewLabel = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel, "4, 4, right, default");
		
		JTextField textField = new JTextField();
		frame.getContentPane().add(textField, "6, 4, fill, default");
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_1, "4, 6, right, default");
		
		JTextField textField_1 = new JTextField();
		frame.getContentPane().add(textField_1, "6, 6, fill, default");
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_2, "4, 10, right, default");
		
		JTextField textField_2 = new JTextField();
		frame.getContentPane().add(textField_2, "6, 10, fill, default");
		textField_2.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_3, "4, 12, right, default");
		
		JTextField textField_3 = new JTextField();
		frame.getContentPane().add(textField_3, "6, 12, fill, default");
		textField_3.setColumns(10);
		
		JButton btnNewButton = new JButton("New button");
		frame.getContentPane().add(btnNewButton, "6, 14");
		
		// Pack everything
		frame.pack();
	}
	
	/*
	 * Public setter for setting visible state
	 */
	
	public void setVisible(boolean b) {
		frame.setVisible(b);	
	}
}
