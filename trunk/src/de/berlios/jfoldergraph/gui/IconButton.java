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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is a little helpfull class to create a button from an imageicon.
 * It react on mouseinvents and can be used to make a iconbars, etc.
 * @author sparrow
 * @version 0.1
 */
public class IconButton extends JPanel {
	
	
	/**
	 * Generated SVUID
	 */
	private static final long serialVersionUID = 4464167436872749977L;
	
	/**
	 * Container for the ActionListener that can be added
	 */
	private ActionListener action;
	
	/**
	 * A boolean that is true if the mouse is over the button
	 */
	private boolean cursorOnIcon = false;
	
	/**
	 * Container for the icon
	 */
	private JLabel imageLabel = new JLabel();
	
	
	
	/**
	 * Constructor which need the icon to display. and also
	 * defines the mouselistener
	 * @param icon The Icon which should be used within this button
	 */
	public IconButton(Icon icon) {
		imageLabel.setIcon(icon);
		imageLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		this.add(imageLabel);
		
		imageLabel.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
				cursorOnIcon = true;
				imageLabel.setBorder(BorderFactory.createRaisedBevelBorder());
			}

			public void mouseExited(MouseEvent e) {
				cursorOnIcon = false;
				imageLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			}

			public void mousePressed(MouseEvent e) {
				imageLabel.setBorder(BorderFactory.createLoweredBevelBorder());
			}

			public void mouseReleased(MouseEvent e) {
				if (cursorOnIcon) {
					imageLabel.setBorder(BorderFactory.createRaisedBevelBorder());
					callActionPerformance();
				} else {
					imageLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				}
				
			}
			
		});
		
	}
	
	/**
	 * Adds the ActionListener which should be peformed
	 * when the button is pressed
	 * @param action The Action which should be performed
	 */
	public void addActionListener(ActionListener action) {
		this.action = action;
	}
	
	
	/**
	 * Calls the ActionListner when the button is pressed
	 */
	private void callActionPerformance() {
		if (action != null) {
			action.actionPerformed(new ActionEvent(this, 1, "IconButton invoked an action"));
		}
	}
	
	/**
	 * Set the Tooltiptext for the button
	 */
	public void setToolTipText(String text) {
		imageLabel.setToolTipText(text);
	}

}
