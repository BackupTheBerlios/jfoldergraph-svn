/*
 * JFolder Graph - Graphical directory-size viewer and browser
 * Copyright (C) (2007) Sebastian Meyer
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package de.berlios.jfoldergraph.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

/**
 * This is a little helpfull class which can be used to confirm the
 * entry of a textfield.
 * You can add it as a FocusListener to a TextField and specify which
 * values can be insert in the textfields.
 * At the moment it can handle Integer and Double values
 * @author sebmeyer
 */
public class ConfirmFocusListener implements FocusListener {
	
    /**
     * Static variabel which specify the type "Integer"
     */
	public final static int INTEGER = 1;
	
	/**
     * Static variabel which specify the type "Double"
     */
    public final static int DOUBLE = 2;
    
    /**
     * Static variabel which specify a number which can end with M for mega and so on
     */
    public final static int KMGT = 3;
    
    /**
     * Specifies the type of the value. Can be one of the
     * static variabels
     */
    private int valueType = 0;
    
    /**
     * The default value which will be the text in the TextField if the
     * entered text is not like the type of text which is allowed.
     * It should be init by construct
     */
    private String defaultValue = null;   
	
	
	public ConfirmFocusListener(int valueType, String defaultValue) {
		setValueType(valueType);
		setDefaultValue(defaultValue);
	}
	
	
    /**
     * This methdo sould check the value type
     */
    private String checkValueType(String value) {
    	switch (valueType) {
    	// Check for Type Integer
    		case INTEGER:
    			try {
    				Integer.parseInt(value);
    			} catch (Exception e) {
    				return null;
    			}
    		break;
    		// Check for Type Double
    		case DOUBLE:
    			try {
    				Double.parseDouble(value);
    			} catch (Exception e) {
    				if (value.contains(",")) {
    					boolean changeable = true;
    					String[] splitValue = value.split(",");
    					if (splitValue.length == 2) {
    						try {
    							Integer.parseInt(splitValue[0]);
    							Integer.parseInt(splitValue[1]);
    						} catch (Exception ex) {
    							changeable = false;
    						}
    						if (changeable) {
    							return value.replaceAll(",", ".");
    						} else {
    							return null;
    						}
    					} else {
    						return null;
    					}
    				} else {
    					return null;
    				}
    			}
    		break;
    		// Check for type KMGT
    		case KMGT:
    			try {
    				Double.parseDouble(value);
    			} catch (Exception e) {
    				String valueLower = null;
    				if (value != null && value.toLowerCase() != null) {
    					valueLower = value.toLowerCase();
    				} else {
    					return null;
    				}
    				if (valueLower.endsWith("k") || valueLower.endsWith("m") || valueLower.endsWith("g") || valueLower.endsWith("t")) {
    					try {
    						Double.parseDouble(value.substring(0, value.length()-1));
    					} catch (Exception ex) {
    						return null;
    					}
    				} else {
    					return null;
    				}
    			}
    		break;
    	}
    	return value;
    }
    
    
    /* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}
    
    
    /**
	 * Will be called if the focus is lost
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		JTextField parent = (JTextField) e.getComponent();
		String ret = null;
		if (parent.getText() != null) {
			ret = checkValueType(parent.getText());
		}
		if (ret == null) {
			parent.setText(defaultValue);
		}
	}
	

	/**
     * Set the default Value of the TextField
     * @param string The DefaultValue
     */
    public void setDefaultValue(String string) {
    	if (valueType > 0) {
    		if (valueType == INTEGER) {
    			try {
    				Integer.parseInt(string);
    				defaultValue = string;
    			} catch (Exception e) {
    				System.out.println("Can not Parse default Value to Integer. Will be '0'");
    				defaultValue = "0";
    			}
    		} else if (valueType == DOUBLE) {
    			try {
    				Double.parseDouble(string);
    				defaultValue = string;
    			} catch (Exception e) {
    				System.out.println("Can not Parse default Value to Double. Will be '0.00'");
    				defaultValue = "0.00";
    			}
    		}
    	} else {
    		defaultValue = string;
    	}
    }
	

	/**
     * Set the value Type which is one of the static-variables
     * @param i The Type of the value
     */
    public void  setValueType(int i) {
    	valueType = i;
    	if (defaultValue != null) {
    		if (valueType == INTEGER) {
    			try {
    				Integer.parseInt(defaultValue);
    			} catch (Exception e){
    				System.out.println("Error while Parsing default Value to INTEGER, I will set it to 0");
    				defaultValue = "0";
    			}
    		} else if (valueType == DOUBLE) {
    			try {
    				Double.parseDouble(defaultValue);
    			} catch (Exception e){
    				System.out.println("Error while Parsing default Value to DOUBLE, I will set it to 0.00");
    				defaultValue = "0.00";
    			}
    		}
    	} else {
    		if (valueType == INTEGER) {
    			defaultValue = "0";
    		} else if (valueType == DOUBLE) {
    			defaultValue = "0.00";
    		} else if (valueType == KMGT) {
    			String ret = checkValueType(defaultValue);
    			if (ret == null) {
    				System.out.println("Default Value is not ok, I will set it to 0");
    				defaultValue = "0";
    			}
    		}
    	}
    }

}
