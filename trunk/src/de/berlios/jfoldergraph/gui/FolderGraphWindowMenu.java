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

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.berlios.jfoldergraph.FolderGraph;


/**
 * This is the Menu in the main-frame
 * @author sebmeyer
 */
public class FolderGraphWindowMenu extends JMenuBar {
	
	/**
	 * Generated SVUID
	 */
	private static final long serialVersionUID = 6377378543426416320L;
	
	/**
	 * The parent (main) window
	 */
	FolderGraphWindow mainWindow;


	/**
	 * Wil construct the menu
	 * @param mainWindow The main-window
	 */
	public FolderGraphWindowMenu(FolderGraphWindow mainWindow) {
		this.mainWindow = mainWindow;
		// Adding the project-menu
		this.add(projectMenu());
		// adding the help-menu
		this.add(helpMenu());
	}
	
	
	/**
	 * Creates the help-menu
	 * @return The help-menu
	 */
	private JMenu helpMenu() {
		JMenu menu = new JMenu("Help");
		menu.setMnemonic('h');
		JMenuItem helpItem = new JMenuItem("Help");
		helpItem.setMnemonic('h');
		helpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performShowHelpFrame();
			}
		});
		menu.add(helpItem);
		menu.addSeparator();
		JMenuItem infoItem = new JMenuItem("Info");
		infoItem.setMnemonic('i');
		infoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performShowAboutFrame();	
			}
		});
		menu.add(infoItem);
		return menu;
	}
	
	
	/**
	 * Will be called by clicking Project->New Project 
	 */
	private void performNewProject() {
		mainWindow.performNewProject();
	}	

	
	/**
	 * Will be called by clicking Project->Open Project 
	 */
	private void performOpenProject() {
		FolderGraph.controller.performOpenProject();
	}
	
	/**
	 * Will be called by clicking Project->Save Project 
	 */
	private void performSaveProject() {
		mainWindow.performSaveProject();
	}
	
	/**
	 * Will be called by clicking Help->Info 
	 */
	private void performShowAboutFrame() {
		FolderGraph.controller.performShowAboutFrame();
	}
	
	/**
	 * Will be called by clicking Help->Help
	 */
	private void performShowHelpFrame() {
		FolderGraph.controller.showHelpWindow();
	}
	
	/**
	 * Creates the project-menu
	 * @return The project-menu
	 */
	private JMenu projectMenu() {
		JMenu menu = new JMenu("Project");
		menu.setMnemonic('p');
		JMenuItem newProjectItem = new JMenuItem("New Project");
		newProjectItem.setMnemonic('n');
		newProjectItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performNewProject();
			}
		});
		menu.add(newProjectItem);
		JMenuItem loadProjectItem = new JMenuItem("Load Project...");
		loadProjectItem.setMnemonic('l');
		loadProjectItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performOpenProject();
			}
		});
		menu.add(loadProjectItem);
		JMenuItem saveProjectItem = new JMenuItem("Save Project...");
		saveProjectItem.setMnemonic('s');
		saveProjectItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performSaveProject();
			}
		});
		menu.add(saveProjectItem);
		menu.addSeparator();
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('e');
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FolderGraph.controller.close();
			}
		});
		menu.add(exitItem);
		return menu;
	}

}
