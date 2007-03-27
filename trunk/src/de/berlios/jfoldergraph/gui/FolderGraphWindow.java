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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import de.berlios.jfoldergraph.datastruct.ScannedFile;


/**
 * This is the main window, with it's integrated panels
 * @author sparrow
 */
public class FolderGraphWindow extends JFrame {
	
	/**
	 * Generated SVUID
	 */
	private static final long serialVersionUID = 4426940537269141174L;
	
	/**
	 * Will show the current directory in the main window 
	 */
	private JTextField currentDirectory;
	
	/**
	 * Contains the actual displayed ScannedFile
	 */
	private ScannedFile currentDisplayedScannedFile;
	
	/**
	 * Contains the fileInfoPanel which shows some informationsa
	 * about the current active file
	 */
	private FileInfoPanel currentInfoPanel = new FileInfoPanel("Some informations about the current file");
	
	/**
	 * Contains the dataset for the chart
	 */
	private DefaultPieDataset dataset = new DefaultPieDataset();
	
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
	 * The left Panel in the splitted pane in the main Window
	 */
	private JPanel leftPanel;
	
	/**
	 * The pane which contains the splitted pain in the main window
	 */
	private JSplitPane mainPane;
	
	/**
	 * The Button to open a folder, in the left panel
	 */
	private JButton openFolderButton;
	
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
     * Creates the Chart
     * @param dataset The dataset for the chart
     * @return The chart
     */
    private JFreeChart createChart(DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
            "Folder overview",  // chart title
            dataset,             // data
            false,               // include legend
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionOutlinesVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);
        plot.setLabelGap(0.02);
        return chart;
        
    }
	
	
	/**
	 * This creates the Panel which contains the chart
	 * @return
	 */
	private JPanel createChartPanel() {
		JFreeChart chart = createChart(dataset);
		return new ChartPanel(chart);
	}
	
	
	/**
	 * Creates the left panel with the list of files
	 * @return The left panel
	 */
	private JPanel createLeftPanel() {
		// Creating left Panel
		leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		// Creating leftNorth Panel with directory info
		JPanel leftNorth = new JPanel();
		leftNorth.setLayout(new BorderLayout());
		leftNorth.add(new JLabel("Current Folder:", JLabel.CENTER), BorderLayout.NORTH);
		currentDirectory = new JTextField();
		currentDirectory.setEditable(false);
		leftNorth.add(currentDirectory, BorderLayout.CENTER);
		IconButton folderUpButton = new IconButton(new ImageIcon(this.getClass().getResource("icons/folder_up.png")));
		folderUpButton.setToolTipText("Change to parent folder");
		folderUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performFolderUp();
			}
		});
		leftNorth.add(folderUpButton, BorderLayout.EAST);
		leftPanel.add(leftNorth, BorderLayout.NORTH);
		// Creating leftCenter Panel with File list
		fileList = new JList(new FileListModel());
		fileList.setCellRenderer(new FileListItemRenderer());
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				performListSelectionChanged();
			}
		});
		JScrollPane scrollPane = new JScrollPane(fileList);
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		//  Creating the south panel with the buttons
		JPanel southPanel = new JPanel();
		openFolderButton = new JButton("Open Directory");
		openFolderButton.setEnabled(false);
		openFolderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performOpenFolder();
			}
		});
		southPanel.add(openFolderButton);
		leftPanel.add(southPanel, BorderLayout.SOUTH);
		
		return leftPanel;
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
		
		// Adding the chart
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 100;
		gbc.weighty = 100;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JPanel chartPanel = this.createChartPanel();
		layout.setConstraints(chartPanel, gbc);
		panel.add(chartPanel);

		// Adding the FileInfoPanel for the current file
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 100;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.SOUTHWEST;
		layout.setConstraints(currentInfoPanel, gbc);
		panel.add(currentInfoPanel);
		
		// Adding the FileInfoPanel for the selected file
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 100;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.SOUTHWEST;
		layout.setConstraints(fileInfoPanel, gbc);
		panel.add(fileInfoPanel);
		
		return panel;
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
	 * Will be called to change to the parent's folder on the FileList
	 */
	private void performFolderUp() {
		if (this.currentDisplayedScannedFile == null) {
			JOptionPane.showMessageDialog(this, "There is no actual project.", "Failed", JOptionPane.INFORMATION_MESSAGE);
		} else if (this.currentDisplayedScannedFile.getParent() == null) {
			JOptionPane.showMessageDialog(this, "Sorry, you can not change to a folder \"above\" the folder you scanned.", "Failed", JOptionPane.INFORMATION_MESSAGE);
		} else {
			this.updateGUIForNewScan(this.currentDisplayedScannedFile.getParent());
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
				// Scanned... now let us show it in the GUI
				if (scanResult != null) {
					updateGUIForNewScan(scanResult);
				}
				break;
			case JFileChooser.CANCEL_OPTION: 
		}
	}
	
	
	/**
	 * Will be called to open an subfolder, by clicking "open folder" in the mainframe
	 */
	private void performOpenFolder() {
		if (fileList.getSelectedIndex() >= 0) {
			ScannedFile sf = (ScannedFile) fileList.getSelectedValue();
			if (sf.isDirectory()) {
				this.updateGUIForNewScan(sf);
			} else {
				JOptionPane.showMessageDialog(this, "The selected item is not a directory", "Failed", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	
	
    /**
	 * This should be called to save the current project
	 */
	public void performSaveProject() {
		// First we should test for a current project
		if (this.currentDisplayedScannedFile == null) {
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
						ScannedFile topScannedFile = this.currentDisplayedScannedFile;
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
	 * This Method should be called to inform the MainGUI to update for
	 * a new ScannedFile. It will be called after a scan or when a subdirectory
	 * has entered
	 * @param sf The ScannedFile which should displayed with the GUI
	 * @throws  
	 */
	public void updateGUIForNewScan(ScannedFile sf) {
		// Removing all entries of the list and the chart
		FileListModel model = (FileListModel) fileList.getModel();
		model.removeAllElements();
		dataset.clear();
		// adding the entries to the lists
		Iterator<ScannedFile> it = sf.getSortedChildFiles();
		while (it.hasNext()) {
			ScannedFile sfc = it.next();
			model.addElement(sfc);
			dataset.setValue(sfc.getFilename() + " [" + sfc.getHumanReadableSISize() + "]", sfc.getPercentSize());
		}
		// Setting datafields
		this.currentDirectory.setText(sf.getPath());
		this.currentDisplayedScannedFile = sf;
		this.currentInfoPanel.setDisplayedScannedFile(sf);
		// Nor notify the FileList
		model.fireEntryChanged();
	}

}
