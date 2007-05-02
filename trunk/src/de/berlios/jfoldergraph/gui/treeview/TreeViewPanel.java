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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import de.berlios.jfoldergraph.datastruct.ScannedFile;
import de.berlios.jfoldergraph.gui.FileInfoPanel;
import de.berlios.jfoldergraph.gui.FolderGraphWindow;
import de.berlios.jfoldergraph.gui.LeftPanel;

/**
 * This class displays the Tree-View-List in the Main-Panel
 * @author sebmeyer
 */
public class TreeViewPanel extends LeftPanel {
	
	/**
	 * Generated SVUID
	 */
	private static final long serialVersionUID = 1L;
	
	ScannedFile currentSelectedFile;
	
	/**
	 * The TreeModel for the Tree
	 */
	JFGTreeModel treeModel;
	
	/**
	 * Contains the fileInfoPanel which shows some informationsa
	 * about the selected file
	 */
	private FileInfoPanel fileInfoPanel = new FileInfoPanel("Some informations about the selected file");

	/**
	 * The displayed JTree 
	 */
	JTree jtree;

	/**
	 * Constructs the Tree-View
	 * @param mainWindow A reffer to the main window
	 */
	public TreeViewPanel(FolderGraphWindow mainWindow) {
		super(mainWindow);
		createGUI();
		createRightInfoPanel();
	}

	/**
	 * Creates the GUI and layout
	 */
	private void createGUI() {
		this.setLayout(new BorderLayout());
		treeModel = new JFGTreeModel(getMainWindow().getRootOfTheProject());
		TreeViewCellRenderer cellRenderer = new TreeViewCellRenderer();
		jtree = new JTree(treeModel);
		jtree.setCellRenderer(cellRenderer);
		DefaultTreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
		selectionModel.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		jtree.setSelectionModel(selectionModel);
		selectionModel.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath newPath = e.getNewLeadSelectionPath();
				treeSelectionChanged(newPath);
			}
		});
		this.add(new JScrollPane(jtree), BorderLayout.CENTER);
	}

	/**
	 * Creates the right Info Panel which displays the Informations
	 * about the files
	 */
	private void createRightInfoPanel() {
		JPanel panel = getMainWindow().getRightInfoPanel();
		panel.getParent().invalidate();
		panel.removeAll();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gbc;
		
		gbc = makegbc(0, 0, 1, 1);
		gbc.weightx = 100;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		layout.setConstraints(fileInfoPanel, gbc);
		panel.add(this.fileInfoPanel);
		
		panel.getParent().validate();
	}
	
	
	/**
	 * Other Panels returns the current displayed File, this returns
	 * the current selected file (or its parent)
	 * @see de.berlios.jfoldergraph.gui.LeftPanel#getCurrentDisplayedFile()
	 */
	public ScannedFile getCurrentDisplayedFile() {
		return this.currentSelectedFile;
	}
	
	/**
	 * Little helpfull method to get a GridBagConstraints
	 * @param x the gridx-value
	 * @param y the gridy-value
	 * @param width the gridwidth
	 * @param height the gridheight
	 * @return The GridBagConstraints witht the given values
	 */
	private GridBagConstraints makegbc(int x, int y, int width, int height) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets = new Insets(2, 2, 2, 2);
		return gbc;
	}
	
	
	/**
	 * Will be called when the selection in the tree has changed
	 * @param path
	 */
	private void treeSelectionChanged(TreePath path) {
		if (path != null && path.getLastPathComponent() != null) {
			ScannedFile sf = (ScannedFile) path.getLastPathComponent();
			if (sf.isDirectory()) {
				this.currentSelectedFile = sf;
				this.getMainWindow().updateGraphView(sf);
			} else {
				if (sf.getParent() != null) {
					this.currentSelectedFile = sf.getParent();
					this.getMainWindow().updateGraphView(sf.getParent());
				}
			}
			fileInfoPanel.setDisplayedScannedFile(sf);
		} else {
			fileInfoPanel.setDisplayedScannedFile((ScannedFile)treeModel.getRoot());
		}
	}
	
	
	/**
	 * Should be called to update the view to display another
	 * ScannedFile
	 * @see de.berlios.jfoldergraph.gui.LeftPanel#updateListView(de.berlios.jfoldergraph.datastruct.ScannedFile)
	 */
	public void updateListView(ScannedFile root, ScannedFile sf) {
		if (sf != null) {
			if (root == treeModel.getRoot()) {
				TreePath treePath = new TreePath(sf.getPathFromRoot());
				jtree.setSelectionPath(treePath);
			} else {
				jtree.setModel(new JFGTreeModel(root));
				TreePath treePath = new TreePath(root.getPathFromRoot());
				jtree.setSelectionPath(treePath);
			}
		}
	}

}
