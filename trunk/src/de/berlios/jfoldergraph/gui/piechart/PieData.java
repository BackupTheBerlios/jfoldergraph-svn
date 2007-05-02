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

package de.berlios.jfoldergraph.gui.piechart;

import java.awt.Color;

/**
 * This is the one data-object in the char.
 * @author sebmeyer
 */
public class PieData {
	
	/**
	 * Contains the Color of the item
	 * which willb be used to draw the object
	 */
	private Color color;
	
	/**
	 * Contains the name of the Item
	 */
	private String name;
	
	/**
	 * Contains the percent-value of the item
	 */
	private double value;
	
	/**
	 * Constructs to object, needs name and percent-value
	 * @param name The Name of the Item
	 * @param value The percent-value of the Item
	 */
	public PieData(String name, double value) {
		this.name = name;
		this.value = value;
	}
		
	/**
	 * Returns the Color of the item
	 * @return the Color of the item
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Returns the name of the item
	 * @return the name of the item
	 */
	public String getName() {
		return this.name;
	}
	
	/** 
	 * Returns the percent-value of the item 
	 * @return the percent-value of the item
	 */
	public double getValue() {
		return this.value;
	}
	
	/**
	 * Set the color of the Item
	 * @param color The color of the item
	 */
	public void setColor(Color color) {
		this.color = color;
	}

}
