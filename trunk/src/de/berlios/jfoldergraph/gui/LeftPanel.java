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

import javax.swing.JPanel;

import de.berlios.jfoldergraph.datastruct.ScannedFile;

/**
 * This is the the basic for the left Panel in the mainWindow.
 * All classes which creates panels for the left side should
 * extend from this class
 * @author sebmeyer
 */
public abstract class LeftPanel extends JPanel {
	
	
	/**
	 * A reference to the Main-Window
	 */
	FolderGraphWindow mainWindow;
	
	
	/**
	 * This constructer should be used for all LeftPanels.
	 * It set the reference to the mainWindow
	 * @param mainWindow
	 */
	public LeftPanel(FolderGraphWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	/**
	 * Returns the Main-Window
	 * @return the Main-Window
	 */
	public FolderGraphWindow getMainWindow() {
		return this.mainWindow;
	}
	
	
	/**
	 * This method should be called to inform the List to udate
	 * its view. It will be called after a new scan or when
	 * entering a subdirectory
	 * @param sf
	 */
	public abstract void updateListView(ScannedFile sf);
	
	
	/**
	 * Returns the current displayed ScannedFile
	 * @return the current displayed ScannedFile
	 */
	public abstract ScannedFile getCurrentDisplayedFile();
	
}
