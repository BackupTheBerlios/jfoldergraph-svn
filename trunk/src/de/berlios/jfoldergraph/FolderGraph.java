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

package de.berlios.jfoldergraph;

import de.berlios.jfoldergraph.gui.FolderGraphWindow;

/**
 * This is the main-class of JFolderGraph.
 * Only starting is arranged here, and some static objects
 * are declared
 * @author sparrow
 */
public class FolderGraph {
	
	/**
	 * The controller for JFolderGraph
	 */
	public static FolderGraphController controller;
	
	/**
	 * The main Window
	 */
	public static FolderGraphWindow mainWindow;
	
	/**
	 * Should be used to show the name of the programm
	 */
	public static final String PROGRAM_NAME = "JFolderGraph";
	
	/**
	 * Should be used to get the Version-Major-Value
	 */
	public static final int VERSION_MAJOR = 0;
	
	/**
	 * Should be used to get the Version-Minor-Value
	 */
	public static final int VERSION_MINOR = 1;
	
	/**
	 * Should be used to get the Version Suffox like "alpha" or "beta"
	 */
	public static final String VERSION_SUFFIX = "beta";

	/**
	 * Entey-Methode for the JFolderGraph
	 * @param args Command-Line Parameters (not used at the moment)
	 */
	public static void main(String[] args) {
		
		controller = new FolderGraphController();
		mainWindow = new FolderGraphWindow();
	}

}
