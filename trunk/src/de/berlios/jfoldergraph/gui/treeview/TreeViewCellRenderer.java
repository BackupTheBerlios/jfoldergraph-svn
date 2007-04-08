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

package de.berlios.jfoldergraph.gui.treeview;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

import de.berlios.jfoldergraph.datastruct.ScannedFile;
import de.berlios.jfoldergraph.gui.IconManager;
import de.berlios.jfoldergraph.gui.RendererPercentBar;

/**
 * This is the Cell-Renderer for the Tree-View
 * @author sebmeyer
 */
public class TreeViewCellRenderer extends JPanel implements TreeCellRenderer {
	
	/**
	 * Generated SVUID
	 */
	private static final long serialVersionUID = 6679578153727200172L;
	
	/**
	 * Will display the icon for the Item
	 */
	JLabel icon;
	
	/**
	 * Will display the name and the size of the Item
	 */
	JLabel name;
	
	/**
	 * Will display the percent-bar for the item
	 */
	RendererPercentBar percentBar;


	/**
	 * Constructs the renderer
	 */
	public TreeViewCellRenderer() {
		name = new JLabel();
		name.setFont(name.getFont().deriveFont(Font.PLAIN));
		icon = new JLabel();
		icon.setFont(name.getFont().deriveFont(Font.PLAIN));
		percentBar = new RendererPercentBar(20, 12);
		createGUI();
	}
	

	/**
	 * Creates the GUI and the layout
	 */
	private void createGUI() {
		this.add(icon);
		this.add(percentBar);
		this.add(name);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, 
													boolean selected, boolean expanded, 
													boolean leaf, int row, boolean hasFocus) {
		if (!(value instanceof ScannedFile)) {
			return null;
		}
		ScannedFile sf = (ScannedFile) value;
		
		name.setText("<html>" + sf.getFilename() + " <small>(" + sf.getHumanReadableSISize() + ")</small></html>");
		
		percentBar.setValue((int)sf.getPercentSize());
		
		if (sf.isDirectory()) {
			switch (sf.getErrorLevel()) {
			case ScannedFile.FILE_IS_READABLE:
				icon.setIcon(IconManager.SMALL_GREEN_FOLDER_ICON);
				break;
			case ScannedFile.FILE_HAS_A_NOTREADABLE_CHILD:
				icon.setIcon(IconManager.SMALL_ORANGE_FOLDER_ICON);
				break;
			case ScannedFile.FILE_IS_NOT_READABLE:
				icon.setIcon(IconManager.SMALL_RED_FOLDER_ICON);
				break;
			}
		} else {
			icon.setIcon(IconManager.SMALL_FILE_ICON);
		}
		
		setBackground(selected ? UIManager.getColor("Tree.selectionBackground") : tree.getBackground());
		setForeground(selected ? UIManager.getColor("Tree.selectionForeground") : tree.getForeground());
		
		return this;
	}

}
