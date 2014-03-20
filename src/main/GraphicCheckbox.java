package main;

import javax.swing.JCheckBox;

/*
 * GraphicCheckbox
 * 
 * Custom checkbox that stores what employees we are updating
 * 
 */

public class GraphicCheckbox extends JCheckBox {
	
	/*
	 * Variables
	 */
	
	private Employee referenceEmployee;
	
	/*
	 * Constructor
	 */
	
	public GraphicCheckbox (String s) {
		super(s);
	}
	
	/*
	 * Getters & Setters
	 */
	
	public void setReference(Employee e) {
		this.referenceEmployee = e;
	}
	
	public Employee getReference() {
		return this.referenceEmployee;
	}
}
