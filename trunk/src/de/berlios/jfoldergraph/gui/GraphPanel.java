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
 * This is the template for all JPanels which dispplays the Graph
 * in JFolderGraph
 * @author sebmeyer
 */
public abstract class GraphPanel extends JPanel {
	
	
	/**
	 * A reference to the Main-Windoe
	 */
	private FolderGraphWindow mainWindow;
	

	/**
	 * Constructs the Panel with a reference to the mainWindow
	 * @param mainWindow
	 */
	public GraphPanel(FolderGraphWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	/**
	 * Returns the Main-Window of JFolderGraph
	 * @return the Main-Window of JFolderGraph
	 */
	public FolderGraphWindow getMainWindow() {
		return this.mainWindow;
	}
	
	
	/**
	 * This should be called to update the view to display another
	 * ScannedFile
	 * @param sf The ScannedFile which should be displayed
	 */
	public abstract void updateGraphView(ScannedFile sf);

}
