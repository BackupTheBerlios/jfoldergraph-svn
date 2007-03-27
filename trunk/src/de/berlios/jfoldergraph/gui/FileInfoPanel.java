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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.berlios.jfoldergraph.datastruct.ScannedFile;


/**
 * This is the File-Information-Panel in the
 * Main-Window
 * @author sparrow
 */
public class FileInfoPanel extends JPanel {
	
	/**
	 * Generated SVUID
	 */
	private static final long serialVersionUID = -6726065081880347169L;
	
	/**
	 * Will be used to set the title of the border
	 */
	private String borderTitle;
	
	/**
	 * Will display the count of Subdirectories and Files
	 */
	private JLabel countLabel;
	
	/**
	 * Will display the informations about the errorlevel
	 */
	private JLabel errorlevelLabel;
	
	/**
	 * Will display the name of the File
	 */
	private JLabel nameLabel;
	
	/**
	 * Will display the size of the File
	 */
	private JLabel sizeLabel;
	
	
	/**
	 * Constructs the Infopanel
	 * @param borderTitle The Title which will be shown in the Border
	 */
	public FileInfoPanel(String borderTitle) {
		this.borderTitle = borderTitle;
		nameLabel = new JLabel(" ", JLabel.LEFT);
		sizeLabel = new JLabel(" ", JLabel.LEFT);
		countLabel = new JLabel(" ", JLabel.LEFT);
		errorlevelLabel = new JLabel (" ", JLabel.LEFT);
		nameLabel.setFont(nameLabel.getFont().deriveFont(Font.PLAIN));
		createGUI();
	}
	
	
	/**
	 * Clear all shown information
	 */
	private void clearAllDetails() {
		nameLabel.setText(" ");
		sizeLabel.setText(" ");
		countLabel.setText(" ");
		errorlevelLabel.setText(" ");
		errorlevelLabel.setIcon(null);
	}
	
	
	/**
	 * Creates the gui for the panel
	 */
	private void createGUI() {
		// Setting the Border
		this.setBorder(BorderFactory.createTitledBorder(borderTitle));
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		GridBagConstraints gbc;
		// The Filename
		gbc = makegbc(0, 0, 1, 1);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		layout.setConstraints(nameLabel, gbc);
		this.add(nameLabel);
		// The size
		sizeLabel.setFont(sizeLabel.getFont().deriveFont(Font.PLAIN));
		gbc = makegbc(0, 1, 1, 1);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		layout.setConstraints(sizeLabel, gbc);
		this.add(sizeLabel);
		// The size
		countLabel.setFont(countLabel.getFont().deriveFont(Font.PLAIN));
		gbc = makegbc(0, 2, 1, 1);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		layout.setConstraints(countLabel, gbc);
		this.add(countLabel);
		
		// right side
		errorlevelLabel.setFont(errorlevelLabel.getFont().deriveFont(Font.PLAIN));
		gbc = makegbc(1, 0, 1, 3);
		gbc.anchor = GridBagConstraints.WEST;
		layout.setConstraints(errorlevelLabel, gbc);
		this.add(errorlevelLabel);
	}
	
	
	/**
	 * This is a helpfull method to create a GBC
	 * @param x the gridx-coordinate
	 * @param y the gridy-coordinate
	 * @param width the width of the component
	 * @param height the height of the component
	 * @return the created gbc
	 */
	private GridBagConstraints makegbc(int x, int y, int width, int height) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = 100;
		gbc.weighty = 100;
		return gbc;
	}
	
	
	/**
	 * Set the ScannedFile wich Informations should be displayed.<br>
	 * You can give null to delete all displayed Informations
	 * @param object The ScannedFile which details should be displayed
	 */
	public void setDisplayedScannedFile(ScannedFile sf) {
		if (sf == null) {
			clearAllDetails();
		} else {
			nameLabel.setText("<html><b><u>Name:</u> " + sf.getFilename() + "</b></html>");
			DecimalFormat df = new DecimalFormat("#,##0.###");
			sizeLabel.setText("<html><u>Size:</u> "+ df.format(sf.getSize()) +" Byte = " +
					sf.getHumanReadableSISize() + " = " + sf.getHumanReadableBinarySize() + "</html>");
			if (sf.isDirectory()) {
				countLabel.setText("<html><u>The directory contains:</u> " + df.format(sf.getCountainedSubfolderCount()) + " Folders and " +
						df.format(sf.getContainedFilesCount()) + " Files</html>");
			} else {
				countLabel.setText(" ");
			}
			// Right side
			if (!sf.isDirectory()) {
				errorlevelLabel.setIcon(IconManager.FILE_ICON);
				errorlevelLabel.setText("Is a file");
			} else {
				switch (sf.getErrorLevel()) {
					case ScannedFile.FILE_IS_READABLE:
						errorlevelLabel.setIcon(IconManager.GREEN_FOLDER_ICON);
						errorlevelLabel.setText("Is a directory");
						break;
					case ScannedFile.FILE_IS_NOT_READABLE:
						errorlevelLabel.setIcon(IconManager.RED_FOLDER_ICON);
						errorlevelLabel.setText("<html>Dircetory is not readable,<br>can't analize the files in it</html>");
						break;
					case ScannedFile.FILE_HAS_A_NOTREADABLE_CHILD:
						errorlevelLabel.setIcon(IconManager.ORANGE_FOLDER_ICON);
						errorlevelLabel.setText("<html>Dircetory contains childs which are not readable,<br>so files and directories in the unreadable <br>directories are ignored</html>");
						break;
				}
			}
		}
	}
	

}
