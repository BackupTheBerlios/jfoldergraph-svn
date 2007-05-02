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

package de.berlios.jfoldergraph.gui.piechart;

import java.awt.BorderLayout;
import java.util.Iterator;

import javax.swing.BorderFactory;

import de.berlios.jfoldergraph.datastruct.ScannedFile;
import de.berlios.jfoldergraph.gui.FolderGraphWindow;
import de.berlios.jfoldergraph.gui.GraphPanel;

/**
 * This class displays the piechart in the main window
 * @author sebmeyer
 */
public class PieChartPanel extends GraphPanel {

	/**
	 * Generated SVUID
	 */
	private static final long serialVersionUID = 4967113027015255991L;

	/**
	 * Contains the currently displayed ScannedFile
	 */
	private ScannedFile currentDisplayedScannedFile;

	private PieChartDrawingPanel drawPanel;
	
	/**
	 * This contents the Option Panel with Options for the chart 
	 */
	private GraphOptionPanel graphOptionPanel;
	
	private PieDataSet pieData;
	
	/**
	 * Consturctes the PiecChart-Panel with a reference to the
	 * main-Window
	 * @param mainWindow JFolderGraph's Main Window
	 */
	public PieChartPanel(FolderGraphWindow mainWindow) {
		super(mainWindow);
		pieData = new PieDataSet();
		createGUI();
	}
	
	
	/**
	 * Creates the GUI and the layout
	 */
	private void createGUI() {
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setLayout(new BorderLayout());
		drawPanel = new PieChartDrawingPanel();
		drawPanel.setPieDataSet(pieData);
		this.add(drawPanel, BorderLayout.CENTER);
		graphOptionPanel = new GraphOptionPanel(this);
		this.add(graphOptionPanel, BorderLayout.SOUTH);
	}


	
	/**
	 * Enables or disables the buttons in the chartview
	 * @param bol True to enable, false to disable
	 */
	private void enableButtons(boolean bol) {
		graphOptionPanel.setButtonsEnabled(bol);
	}
	
	
	/**
	 * Returns the current dusplayed ScannedFile
	 * @return the current dusplayed ScannedFile
	 */
	public ScannedFile getCurrentDisplayedScannedFile() {
		return this.currentDisplayedScannedFile;
	}
	
	
	/** 
	 * This updates the current view with a new scanned file
	 * @see de.berlios.jfoldergraph.gui.GraphPanel#updateGraphView(de.berlios.jfoldergraph.datastruct.ScannedFile)
	 */
	public void updateGraphView(ScannedFile sf) {
		this.currentDisplayedScannedFile = sf;
		enableButtons(true);
		// Removing all data from Graph and getting actual data from the ScannedFile
		pieData.removeAll();
		Iterator<ScannedFile> it = sf.getSortedChildFiles(this.graphOptionPanel.getShowFiles());
		// Init Ignored Items
		double ignoredPercent = 0.00;
		double ignoredSize = 0.00;
		// Adding data to the dataset
		while (it.hasNext()) {
			ScannedFile sfc = it.next();
			String type = "";
			if (sfc.isDirectory()) {
				type = "[D]";
			} else {
				type = "[F]";
			}
			if ((graphOptionPanel.getGroupType() == GraphOptionPanel.PERCENT && sfc.getPercentSize() >= graphOptionPanel.getMinSize()) || 
					(graphOptionPanel.getGroupType() == GraphOptionPanel.BYTES && sfc.getSize() >= graphOptionPanel.getMinSize())) {
				pieData.addItem(sfc.getFilename() + " " + type, sfc.getPercentSize());
			} else {
				ignoredPercent = ignoredPercent + sfc.getPercentSize();
				ignoredSize = ignoredSize + sfc.getSize();
			}
		}
		if (ignoredPercent > 0) {
			pieData.addItem("Grouped Items", ignoredPercent);
		}
		drawPanel.fireRepaint();
	}

}
