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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * This is the Bar which will be displayed in the File-List at the MainGUI.
 * Its a JPanel with a overwritten paint-method
 * @author sebmeyer
 */
public class RendererPercentBar extends JPanel {
	
	
	/**
	 * Genreratied SVUID
	 */
	private static final long serialVersionUID = 7545507736442642923L;
	
	/**
	 * This will contain the percent-value which will
	 * be used to draw the bar
	 */
	private int value = 0;
	
	
	/**
	 * Constructer which will define the maximum size of the
	 * panel 
	 */
	public RendererPercentBar() {
		Dimension dim = new Dimension(102, 15);
		this.setMinimumSize(dim);
		this.setMaximumSize(dim);
		this.setPreferredSize(dim);
	}
	
	/**
	 * Constructer which will define the maximum size of the
	 * panel 
	 */
	public RendererPercentBar(int width, int height) {
		Dimension dim = new Dimension(width, height);
		this.setMinimumSize(dim);
		this.setMaximumSize(dim);
		this.setPreferredSize(dim);
	}
	
	
	/** 
	 * The overwritten paint-method to paint the
	 * the panel which will be used do display the
	 * percentBar
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLUE);
		g.fillRect(1, 1, (int)((double)this.getWidth() / (double)100 * (double)value), this.getHeight() - 1);
	}
	
	
	/**
	 * Set the percent-value for the bar
	 * @param value The percent value
	 */
	public void setValue(int value) {
		this.value = value;
	}

}
