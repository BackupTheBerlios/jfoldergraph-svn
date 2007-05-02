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
import java.util.ArrayList;

/**
 * This is the dataset for the PieChart
 * @author sebmeyer
 */
public class PieDataSet {
	
	/**
	 * Contains the Items for the PieChart
	 */
	ArrayList<PieData> pieDataList;
	
	/**
	 * Constructs the PieData-Objects and init the Lists
	 */
	public PieDataSet() {
		 pieDataList = new ArrayList<PieData>();
	}
	
	/**
	 * Adds a new item to the Dataset
	 * @param piedata The PieData which should be added
	 */
	public void addItem(PieData piedata) {
		piedata.setColor(new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
		pieDataList.add(piedata);
	}
	
	/**
	 * Adds a new item to the Dataset
	 * @param name Name of the Item
	 * @param value Percent-Value of the Item
	 */
	public void addItem(String name, double value) {
		PieData piedata = new PieData(name, value);
		addItem(piedata);
	}
	
	/**
	 * Get the color of one of the items
	 * @param index The index of the item
	 * @return The color of the item
	 */
	public Color getColorAt(int index) {
		return pieDataList.get(index).getColor();
	}
	
	/**
	 * Get the name of an item
	 * @param index The index of the Object
	 * @return The name of the Object
	 */
	public String getNameAt(int index) {
		return pieDataList.get(index).getName();
	}
	
	/**
	 * Returns a PieData-Object from the PieDataSet
	 * @param index The Index of the Item
	 * @return The Item
	 */
	public PieData getPieDataEntryAt(int index) {
		return pieDataList.get(index);
	}
	
	/**
	 * Get size of the items in the dataset
	 * @return
	 */
	public int getSize() {
		return pieDataList.size();
	}
	
	/**
	 * Get the percent-value of an item
	 * @param index The index of the item
	 * @return The percent-value of the item
	 */
	public double getValueAt(int index) {
		return pieDataList.get(index).getValue();
	}
	
	/**
	 * Clears and deletes the entrys of the dataset
	 */
	public void removeAll() {
		pieDataList.clear();
	}
	
	/**
	 * Sets the color of one of the items
	 * @param index The index of the item
	 * @param color The Color which should be set
	 */
	public void setColorAt(int index, Color color) {
		pieDataList.get(index).setColor(color);
	}

}
