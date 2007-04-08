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

import javax.swing.ImageIcon;

/**
 * This is the icon-Manager wich contains all the
 * Icons which can be used in the program
 * @author sebmeyer
 *
 */
public class IconManager {

	/**
	 * Contains the Icon for the File.
	 */
	public static final ImageIcon FILE_ICON = new ImageIcon(IconManager.class.getResource("icons/mime_txt.png"));
	
	/**
	 * Contains the small Icon for the File.
	 */
	public static final ImageIcon SMALL_FILE_ICON = new ImageIcon(IconManager.class.getResource("icons/mime_txt_small.png"));
	
	/**
	 * Contains the Icon for the green Folder.
	 */
	public static final ImageIcon GREEN_FOLDER_ICON = new ImageIcon(IconManager.class.getResource("icons/folder_green.png"));
	
	/**
	 * Contains the small Icon for the green Folder.
	 */
	public static final ImageIcon SMALL_GREEN_FOLDER_ICON = new ImageIcon(IconManager.class.getResource("icons/folder_green_small.png"));
	
	/**
	 * Contains the Icon for the orange Folder.
	 */
	public static final ImageIcon ORANGE_FOLDER_ICON = new ImageIcon(IconManager.class.getResource("icons/folder_orange.png"));
	
	/**
	 * Contains the small Icon for the orange Folder.
	 */
	public static final ImageIcon SMALL_ORANGE_FOLDER_ICON = new ImageIcon(IconManager.class.getResource("icons/folder_orange_small.png"));
	
	/**
	 * Contains the Icon for the red Folder.
	 */
	public static final ImageIcon RED_FOLDER_ICON = new ImageIcon(IconManager.class.getResource("icons/folder_red.png"));
	
	/**
	 * Contains the small Icon for the red Folder.
	 */
	public static final ImageIcon SMALL_RED_FOLDER_ICON = new ImageIcon(IconManager.class.getResource("icons/folder_red_small.png"));
	
	/**
	 * Contains the Icon for the Folder-Up-Button
	 */
	public static final ImageIcon FOLDER_UP_ICON = new ImageIcon(IconManager.class.getResource("icons/folder_up.png"));
	
}
