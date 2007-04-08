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

package de.berlios.jfoldergraph.gui.filelist;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.berlios.jfoldergraph.datastruct.ScannedFile;
import de.berlios.jfoldergraph.gui.FileInfoPanel;
import de.berlios.jfoldergraph.gui.FolderGraphWindow;
import de.berlios.jfoldergraph.gui.IconButton;
import de.berlios.jfoldergraph.gui.IconManager;
import de.berlios.jfoldergraph.gui.LeftPanel;

/**
 * This class is the "classic File List" in the Main Window.
 * It is one of the Views that can be used to browse trough the
 * Folders
 * @author sebmeyer
 */
public class ClassicFileListPanel extends LeftPanel {

	/**
	 * Generated SVUID
	 */
	private static final long serialVersionUID = -7376081934939096188L;

	/**
	 * Will show the current directory in the main window 
	 */
	private JTextField currentDirectory;
	
	/**
	 * Contains the actual displayed ScannedFile 
	 */
	private ScannedFile currentDisplayedFile;
	
	/**
	 * Contains the fileInfoPanel which shows some informationsa
	 * about the current active file
	 */
	private FileInfoPanel currentInfoPanel = new FileInfoPanel("Some informations about the current file");
	
	/**
	 * Contains the fileInfoPanel which shows some informationsa
	 * about the selected file
	 */
	private FileInfoPanel fileInfoPanel = new FileInfoPanel("Some informations about the selected file");
	
	/**
	 * The FileList in the main window
	 */
	private JList fileList;
	
	/**
	 * Is the flag to show files in the list
	 */
	private JCheckBox fileListShowFiles;
	
	/**
	 * The Button to open a folder, in the left panel
	 */
	private JButton openFolderButton;

	
	
	/**
	 * Constructs the ClassicList
	 * @param mainWindow The Main-Window
	 */
	public ClassicFileListPanel(FolderGraphWindow mainWindow) {
		super(mainWindow);
		createGUI();
	}
	
	
	/**
	 * Creates the GUI and the Layout
	 */
	private void createGUI() {
		// create the rightInfoPanel
		createRightInfoPanel();
		// Creating left Panel
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		GridBagConstraints gbc;
		// Creating  Panel with directory info
		JPanel leftNorth = new JPanel();
		leftNorth.setLayout(new BorderLayout());
		leftNorth.add(new JLabel("Current Folder:", JLabel.CENTER), BorderLayout.NORTH);
		currentDirectory = new JTextField();
		currentDirectory.setEditable(false);
		leftNorth.add(currentDirectory, BorderLayout.CENTER);
		IconButton folderUpButton = new IconButton(IconManager.FOLDER_UP_ICON);
		folderUpButton.setToolTipText("Change to parent folder");
		folderUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performFolderUp();
			}
		});
		leftNorth.add(folderUpButton, BorderLayout.EAST);
		gbc = makegbc(0, 0, 1, 1);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 100;
		layout.setConstraints(leftNorth, gbc);
		this.add(leftNorth);
		// Creating Panel with File list
		fileList = new JList(new FileListModel());
		fileList.setCellRenderer(new FileListItemRenderer());
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				performListSelectionChanged();
			}
		});
		JScrollPane scrollPane = new JScrollPane(fileList);
		gbc = makegbc(0, 1, 1, 1);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 100;
		layout.setConstraints(scrollPane, gbc);
		this.add(scrollPane);
		// Createing the checkbox for the files
		fileListShowFiles = new JCheckBox("Show Files", true);
		fileListShowFiles.setEnabled(false);
		fileListShowFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateListView(currentDisplayedFile);
				// updateListView(getMainWindow().getCurrentDisplayedScannedFiles());
			}
		});
		gbc = makegbc(0, 2, 1, 1);
		layout.setConstraints(fileListShowFiles, gbc);
		this.add(fileListShowFiles);
		//  Creating the south panel with the buttons
		openFolderButton = new JButton("Open Directory");
		openFolderButton.setEnabled(false);
		openFolderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performOpenFolder();
			}
		});
		gbc = makegbc(0, 3, 1, 1);
		layout.setConstraints(openFolderButton, gbc);
		this.add(openFolderButton);
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
		layout.setConstraints(currentInfoPanel, gbc);
		panel.add(this.currentInfoPanel);
		
		gbc = makegbc(0, 1, 1, 1);
		gbc.weightx = 100;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		layout.setConstraints(fileInfoPanel, gbc);
		panel.add(this.fileInfoPanel);
		panel.getParent().validate();
	}
	
	
	/** Enables the buttons
	 * @see de.berlios.jfoldergraph.gui.LeftPanel#enableButtons(boolean)
	 */
	public void enableButtons(boolean bol) {
		openFolderButton.setEnabled(bol);
		fileListShowFiles.setEnabled(bol);
	}
	
	
	/**
	 * Returns the current displayed ScannedFile
	 * @see de.berlios.jfoldergraph.gui.LeftPanel#getCurrentDisplayedFile()
	 */
	public ScannedFile getCurrentDisplayedFile() {
		return this.currentDisplayedFile;
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
	 * Will be called to change to the parent's folder on the FileList
	 */
	private void performFolderUp() {
		if (currentDisplayedFile == null) {
			JOptionPane.showMessageDialog(this, "There is no actual project.", "Failed", JOptionPane.INFORMATION_MESSAGE);
		} else if (currentDisplayedFile.getParent() == null) {
			JOptionPane.showMessageDialog(this, "Sorry, you can not change to a folder \"above\" the folder you scanned.", "Failed", JOptionPane.INFORMATION_MESSAGE);
		} else {
			getMainWindow().updateGUIForNewScan(currentDisplayedFile.getParent());
		}
	}
	
	/**
     * Will be called if the selection of the FileList has
     * been changed
     */
    private void performListSelectionChanged() {
    	if (fileList.getSelectedIndex() >=0  ) {
    		ScannedFile sf = (ScannedFile) fileList.getSelectedValue();
    		fileInfoPanel.setDisplayedScannedFile(sf);
    		openFolderButton.setEnabled(sf.isDirectory());
    	} else {
    		fileInfoPanel.setDisplayedScannedFile(null);
    	}
    }


	/**
	 * Will be called to open an subfolder, by clicking "open folder" in the mainframe
	 */
	private void performOpenFolder() {
		if (fileList.getSelectedIndex() >= 0) {
			ScannedFile sf = (ScannedFile) fileList.getSelectedValue();
			if (sf.isDirectory()) {
				getMainWindow().updateGUIForNewScan(sf);
			} else {
				JOptionPane.showMessageDialog(this, "The selected item is not a directory", "Failed", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}


	/**
	 * Updates the the List on the display.
	 * @param sf The ScannedFile which should be displayed
	 */
	public void updateListView(ScannedFile sf) {
		this.enableButtons(true);
		this.currentInfoPanel.setDisplayedScannedFile(sf);
		this.currentDirectory.setText(sf.getPath());
		this.currentDisplayedFile = sf;
		// Removing all entries of the list
		FileListModel model = (FileListModel) fileList.getModel();
		model.removeAllElements();
		// adding the entries to the lists
		Iterator<ScannedFile> it = sf.getSortedChildFiles(fileListShowFiles.isSelected());
		while (it.hasNext()) {
			ScannedFile sfc = it.next();
			model.addElement(sfc);
		}
		// Now notify the FileList
		model.fireEntryChanged();
		this.openFolderButton.setEnabled(false);
	}


}
