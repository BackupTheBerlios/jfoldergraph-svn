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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import de.berlios.jfoldergraph.datastruct.ScannedFile;
import de.berlios.jfoldergraph.gui.filelist.ClassicFileListPanel;
import de.berlios.jfoldergraph.gui.piechart.PieChartPanel;
import de.berlios.jfoldergraph.gui.treeview.TreeViewPanel;


/**
 * This is the main window, with it's integrated panels
 * @author sebmeyer
 */
public class FolderGraphWindow extends JFrame {
	
	/**
	 * Generated SVUID
	 */
	private static final long serialVersionUID = 4426940537269141174L;
	
	/**
	 * Contains the actual displayed ScannedFile
	 */
	private ScannedFile actualProject;
	
	/**
	 * Will contain the Panel which displays the graph
	 */
	private GraphPanel graphPanel;
	
	/**
	 * Contains the view of the leftPanel
	 */
	private LeftPanel leftPanelContainer;

	/**
	 * The pane which contains the splitted pain in the main window
	 */
	private JSplitPane mainPane;
	
	/**
	 * Should contain the info Panel which canis be displayed on the right side below
	 * the Graph
	 */
	private JPanel rightInfoPanel;
	
	
	/**
	 * Constructor which calls all needed methods to set the
	 * window visible
	 */
	public FolderGraphWindow() {
		this.setTitle("JFolderGraph");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit tk = Toolkit.getDefaultToolkit();
		this.setSize(tk.getScreenSize().width / 6 * 5, tk.getScreenSize().height / 6  * 5);
		
		// Adding the Menu
		this.setJMenuBar(new FolderGraphWindowMenu(this));
		
		// Adding the MainPanel
		this.setLayout(new BorderLayout());
		this.add(mainPanel(), BorderLayout.CENTER);

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	

    /**
	 * Creates the left panel with the list of files
	 * @return The left panel
	 */
	private JPanel createLeftPanel() {
		final JPanel panel = new JPanel();
		// The Panel which will contain the left Panel with the file list
		panel.setLayout(new BorderLayout());
		leftPanelContainer = new ClassicFileListPanel(this);
		panel.add(leftPanelContainer, BorderLayout.CENTER);
		// This Panel contains a List to choice the displayed list
		JPanel listChoicePanel = new JPanel();
		listChoicePanel.setBorder(BorderFactory.createEtchedBorder());
		listChoicePanel.add(new JLabel("List-Type:", JLabel.RIGHT));
		final JComboBox choiceBox = new JComboBox();
		choiceBox.setEditable(false);
		choiceBox.addItem("Classic List");
		choiceBox.addItem("Tree View");
		choiceBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listTypeChanged(panel, choiceBox.getSelectedIndex());
			}
		});
		listChoicePanel.add(choiceBox);
		panel.add(listChoicePanel, BorderLayout.SOUTH);
		return panel;
	}
	
	
	/**
	 * This creates the right panel which is the chartside
	 * @return the right Panel
	 */
	private JPanel createRightPanel() {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gbc;
		
		graphPanel = new PieChartPanel(this);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 100;
		gbc.weighty = 100;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		layout.setConstraints(graphPanel, gbc);
		panel.add(graphPanel);
		
		// Adding the InfoPanel to the right Panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 100;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.SOUTHWEST;
		rightInfoPanel = new JPanel();
		layout.setConstraints(rightInfoPanel, gbc);
		panel.add(rightInfoPanel);
		
		return panel;
	}
	
	
	
	/**
	 * Get the InfoPanel on the right side which is displayed below the
	 * Graph
	 * @return the InfoPanel on the right side which is displayed below the
	 */
	public JPanel getRightInfoPanel() {
		return this.rightInfoPanel;
	}
	
    
    /**
	 * Returns the root of the actual project
	 * @return the root of the actual project
	 */
	public ScannedFile getRootOfTheProject() {
		return this.actualProject;
	}

	
	/**
	 * Will be called when the slection of the list-type
	 * has been changed
	 */
	private void listTypeChanged(JPanel panel, int index) {
		panel.invalidate();
		ScannedFile currentDisplayedFile = leftPanelContainer.getCurrentDisplayedFile();
		panel.remove(leftPanelContainer);
		leftPanelContainer = null;
		// leftPanelContainer.removeAll();
		switch (index) {
		case 0:
			leftPanelContainer = new ClassicFileListPanel(this);
			break;
		case 1:
			leftPanelContainer = new TreeViewPanel(this);
			break;
		}
		if (currentDisplayedFile != null) {
			leftPanelContainer.updateListView(currentDisplayedFile);
		} else {
			leftPanelContainer.updateListView(this.getRootOfTheProject());
		}
		panel.add(leftPanelContainer, BorderLayout.CENTER);
		panel.validate();
	}
	
	
	/**
	 * Creates the mainPanel for the GUI
	 * @return The mainPanel
	 */
	private JSplitPane mainPanel() {
		mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		mainPane.add(new JScrollPane(createRightPanel()), JSplitPane.RIGHT);
		mainPane.add(createLeftPanel(), JSplitPane.LEFT);
		
		return mainPane;
	}
	
	
	/**
	 * This should be called to perform a new scan
	 */
	public void performNewProject() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Choose directory to scan");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setMultiSelectionEnabled(false);
		int retint = jfc.showOpenDialog(this);
		switch (retint) {
			case JFileChooser.APPROVE_OPTION:
				File choosenFile = jfc.getSelectedFile();
				// Lets do some tests before start scanning
				// Test: Choosen File should not be null
				if (choosenFile == null) {
					JOptionPane.showMessageDialog(this, "Failed to open selection.", "An Error occures", JOptionPane.ERROR_MESSAGE);
					break;
				}
				// Test: Choosen File should be a Directory
				// We tested this allready via the "directory only" in the JFC, this is for shure!
				if (!choosenFile.isDirectory()) {
					JOptionPane.showMessageDialog(this, "Sorry, selected file is not a directory.", "Information", JOptionPane.INFORMATION_MESSAGE);
					break;
				}
				// Test: Can I read the Directory?
				if (!choosenFile.canRead()) {
					JOptionPane.showMessageDialog(this, "The selected directory can not be read.", "Failed", JOptionPane.ERROR_MESSAGE);
					break;
				}
				// The File has passed all tests. Give it to the scanner now
				ProgressDialog pgd = new ProgressDialog(choosenFile);
				pgd.setVisible(true);
				ScannedFile scanResult = pgd.getScanResult();
				pgd.dispose();
				pgd = null;
				// Scanned... now let us show it in the GUI
				actualProject = scanResult;
				if (scanResult != null) {
					updateGUIForNewScan(scanResult);
				}
				break;
			case JFileChooser.CANCEL_OPTION: 
		}
	}
	
	
	/**
	 * This should be called to save the current project
	 */
	public void performSaveProject() {
		// First we should test for a current project
		if (actualProject == null) {
			JOptionPane.showMessageDialog(this, "It seems there is no active project", "Can't save", JOptionPane.ERROR_MESSAGE);
		} else {
			// Let the user choose a file to save in
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.setFileFilter(new MyFileFilter());
			int chooseRet = jfc.showSaveDialog(this);
			if (chooseRet == JFileChooser.APPROVE_OPTION) {
				if (jfc.getSelectedFile() != null) {
					// Test for the .jtpg file extension and add it if it's not there
					String savePath = jfc.getSelectedFile().getPath();
					if (!savePath.endsWith(".jtgp")) {
						savePath = savePath + ".jtgp";
					}
					File saveFile = new File(savePath);
					// Check: Is the File allready there and ask the User for overwriting, if it is there
					boolean saveFlag = true;
					if (saveFile.exists()) {
						int existRet = JOptionPane.showConfirmDialog(this, "File allready exists. Overwrite it?", "File exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (existRet == JOptionPane.NO_OPTION) {
							saveFlag = false;
						}
					}
					if (saveFlag) {
						// Searching the "highest" ScannedFile Object in the Project to save it
						ScannedFile topScannedFile = actualProject;
						while (topScannedFile.getParent() != null) {
							topScannedFile = topScannedFile.getParent();
						}
						// Now save the file. Saved File will also be compressed
						try {
							FileOutputStream fos = new FileOutputStream(saveFile);
							GZIPOutputStream gzos = new GZIPOutputStream(fos);
							ObjectOutputStream oos = new ObjectOutputStream(gzos);
							oos.writeObject(topScannedFile);
							oos.close();
							fos.close();
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(this, "An error while saving: " + e.getMessage(), "Error while saving!", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Set the actual Project
	 * @param sf The actual project
	 */
	public void setActualProject(ScannedFile sf) {
		this.actualProject = sf;
	}
	
	
	/**
	 * Updates the the graph on the display.
	 * @param sf The ScannedFile which should be displayed
	 */
	public void updateGraphView(ScannedFile sf) {
		if (sf != null) {
			graphPanel.updateGraphView(sf);
		}
	}
	
	
	/**
	 * This Method should be called to inform the MainGUI to update for
	 * a new ScannedFile. It will be called after a scan or when a subdirectory
	 * has entered
	 * @param sf The ScannedFile which should displayed with the GUI
	 * @throws  
	 */
	public void updateGUIForNewScan(ScannedFile sf) {
		// Updating screen
		updateListView(sf);
		updateGraphView(sf);
	}
	
	
	/**
	 * Updates the the List on the display.
	 * @param sf The ScannedFile which should be displayed
	 */
	private void updateListView(ScannedFile sf) {
		if (sf != null) {
			leftPanelContainer.updateListView(sf);
		}
	}

}
