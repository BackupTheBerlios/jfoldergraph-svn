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
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

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
	
	/**
	 * The chart which will be displayed
	 */
	JFreeChart chart;
	
	/**
	 * Contains the dataset for the chart
	 */
	private DefaultPieDataset dataset = new DefaultPieDataset();
	
	/**
	 * This contents the Option Panel with Options for the chart 
	 */
	private GraphOptionPanel graphOptionPanel;
	
	/**
	 * Consturctes the PiecChart-Panel with a reference to the
	 * main-Window
	 * @param mainWindow JFolderGraph's Main Window
	 */
	public PieChartPanel(FolderGraphWindow mainWindow) {
		super(mainWindow);
		createGUI();
	}
	
	
	/**
     * Creates the Chart
     * @param dataset The dataset for the chart
     * @return The chart
     */
    private JFreeChart createChart(DefaultPieDataset dataset) {
        chart = ChartFactory.createPieChart(
            "Folder overview (PieChart)",  // chart title
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
	 * Creates the GUI and the layout
	 */
	private void createGUI() {
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setLayout(new BorderLayout());
		this.add(this.createChartPanel(), BorderLayout.CENTER);
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
		chart.setTitle(sf.getFilename());
		this.currentDisplayedScannedFile = sf;
		enableButtons(true);
		DecimalFormat df = new DecimalFormat("##0.#");
		// Removing all data from Graph and getting actual data from the ScannedFile
		dataset.clear();
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
				dataset.setValue(sfc.getFilename() + " " + type + " (" + sfc.getHumanReadableSISize() + ")  " 
						+ df.format(sfc.getPercentSize()) + "%", sfc.getPercentSize());
			} else {
				ignoredPercent = ignoredPercent + sfc.getPercentSize();
				ignoredSize = ignoredSize + sfc.getSize();
			}
		}
		if (ignoredPercent > 0) {
			dataset.setValue("Grouped Items: (" + ignoredSize + ") " + df.format(ignoredPercent) + "%", ignoredPercent);
		}
	}

}
