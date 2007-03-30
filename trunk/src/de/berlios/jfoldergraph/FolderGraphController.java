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

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.berlios.jfoldergraph.datastruct.ScannedFile;
import de.berlios.jfoldergraph.gui.AboutFrame;
import de.berlios.jfoldergraph.gui.HelpWindow;
import de.berlios.jfoldergraph.gui.MyFileFilter;



/**
 * This is the JFolderGraphController.
 * It should be the basic of the project with all methods which should be accesable from
 * everywheres.
 * It is referenced as an static object in the class JFodlerGraph as controller
 * @author sebmeyer
 */
public class FolderGraphController {
	
	
	/**
	 * This Method close the project
	 */
	public void close() {
		System.exit(0);
	}
	
	
	/**
	 * This should be used to open a saved project
	 */
	public void performOpenProject() {
		try {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.setFileFilter(new MyFileFilter());
			int jfcRet = jfc.showOpenDialog(FolderGraph.mainWindow);
			if (jfcRet == JFileChooser.APPROVE_OPTION) {
				FileInputStream fis = new FileInputStream(jfc.getSelectedFile());
				GZIPInputStream gzis = new GZIPInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(gzis);
				ScannedFile scannedFile = (ScannedFile) ois.readObject();
				FolderGraph.mainWindow.updateGUIForNewScan(scannedFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(FolderGraph.mainWindow, "Error while opening: " + e.getMessage(), "Error while loading", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/**
	 * Should be called to show the "about" Frame
	 */
	public void performShowAboutFrame() {
		new AboutFrame();
	}
	
	
	/**
	 * This should be called to show the Help-Window
	 */
	public void showHelpWindow() {
		new HelpWindow();
	}
	
}
