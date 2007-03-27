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

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import de.berlios.jfoldergraph.datastruct.ScannedFile;



/**
 * This class represent the Entry in the File-List for each ScannedFile
 * in the Mainframe
 * @author sparrow
 */
public class FileListItemRenderer extends JPanel implements ListCellRenderer, Serializable {

	// ---- Static Variables ----
	
	/**
	 * Contains the Format for the %-text.
	 * Static to load it only 1 time, not for every entry
	 */
	private static final DecimalFormat dFormat = new DecimalFormat("##0.0");
	
	/**
	 * The Border for selected/not selected
	 */
	private final static Border NO_FOCUS_BORDER =  new EmptyBorder(1, 1, 1, 1);
	
	/**
	 * Automatic genereatoed SVUID 
	 */
	private static final long serialVersionUID = 8413984277219151869L;
	
	// ---- End of Static Variables ----
	
	
	/**
	 * This will display the Icon
	 */
	private JLabel image;
	
	/**
	 * This will display the name of the File or Directory
	 */
	private JLabel name;
	
	/**
	 * This will display the %-size
	 */
	private JLabel percent;
	
	/**
	 * This will display ther PercentBar
	 */
	private RendererPercentBar percentBar = new RendererPercentBar();
	
	/**
	 * This will display the size
	 */
	private JLabel size;
	
	
	/**
	 * Construct the Renderer
	 */
	public FileListItemRenderer () {	
		
		// Init TextFields
		image = new JLabel();
		name = new JLabel();
		name.setFont(name.getFont().deriveFont(Font.PLAIN));
		size = new JLabel();
		size.setFont(size.getFont().deriveFont(Font.PLAIN));
		percent = new JLabel();
		percent.setFont(percent.getFont().deriveFont(Font.PLAIN));
		percent.setHorizontalTextPosition(JLabel.RIGHT);
		percent.setHorizontalAlignment(JLabel.RIGHT);
	
		// Doing the Layout for the List-entry
		prepareLayout();
    }
	
	/** 
	 * This will be called for rendering. For performance, this should not do the layout.
	 * Do the layout in the consutructor, here only set the values to the layout
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value != null && value instanceof ScannedFile) {
			ScannedFile sFile = (ScannedFile) value;
			if (sFile.isDirectory()) {
				switch (sFile.getErrorLevel()) {
					case ScannedFile.FILE_IS_READABLE:
						image.setIcon(IconManager.GREEN_FOLDER_ICON);
						break;
					case ScannedFile.FILE_IS_NOT_READABLE:
						image.setIcon(IconManager.RED_FOLDER_ICON);
						break;
					case ScannedFile.FILE_HAS_A_NOTREADABLE_CHILD:
						image.setIcon(IconManager.ORANGE_FOLDER_ICON);
						break;
				}
			} else {
				image.setIcon(IconManager.FILE_ICON);
			}
			name.setText("<html><b> " + sFile.getFilename() + "</b></html>");
			size.setText("<html><u>Size:</u> " + " ("+ sFile.getHumanReadableSISize() + ")</html>");
			percent.setText("<html>" + dFormat.format(sFile.getPercentSize()) + "%</html>");
			percentBar.setValue((int) sFile.getPercentSize());
		}
		
		setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
		setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
		setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : NO_FOCUS_BORDER);
		return this;
	}
	
	

	/**
	 * Preparing the Layout for the Entry-Renderer
	 */
	private void prepareLayout() {
		this.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
		
		// Set a GridBagLayout
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		GridBagConstraints gbc;
		
		// Adding the TextField for the Image
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 3;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(2, 3, 2, 3);
		layout.setConstraints(image, gbc);
		this.add(image);
		
		// Adding the TextField for the Name
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 100;
		gbc.weighty = 100;
		layout.setConstraints(name, gbc);
		this.add(name);
		
		// Adding the TextField for the size
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 100;
		gbc.weighty = 100;
		layout.setConstraints(size, gbc);
		this.add(size);
		
		// Adding the TextField for the percent
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 0;
		gbc.weighty = 0;
		layout.setConstraints(percent, gbc);
		this.add(percent);
		
		// Adding the TextField for the percentBAR
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 0;
		gbc.weighty = 0;
		layout.setConstraints(percentBar, gbc);
		this.add(percentBar);
	}
}
