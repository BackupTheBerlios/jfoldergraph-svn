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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import de.berlios.jfoldergraph.gui.ConfirmFocusListener;

/**
 * This class displays the optiosn for the chart in the main window
 * @author sebmeyer
 */
public class GraphOptionPanel extends JPanel {
	
	/**
	 * Final static int to get the type which to group by for
	 * bytes
	 */
	public static final int BYTES = 1;

	/**
	 * Final static int to get the type which to group by for
	 * percent
	 */
	public static final int PERCENT = 0;
	
	/**
	 * generated SVUID
	 */
	private static final long serialVersionUID = 2867371331782003613L;
	
	/**
	 * The Radio button to group py bytesize
	 */
	private JRadioButton byteBut;
	
	/**
	 * Contains the RadioButtons for type of grouping
	 */
	private ButtonGroup groupChoice;
	
	/** 
	 * Label for the textfield of percent size
	 */
	private JLabel ignoreSmallerThenLabel;
	
	/**
	 * This will represent the textfield which ask for the
	 * percent size. All Entries smaller will  be ignored
	 */
	private JTextField ignoreSmallerThenTextField;
	
	/**
	 * Contains the info label which informs about the optional
	 * format to enter a value in the minSizeTextField
	 */
	private JLabel infoLabel;
	
	/**
	 * The Radio button to group py percent
	 */
	private JRadioButton percentBut;
	
	/**
	 * Reference the application's main window
	 */
	private PieChartPanel piePanel;
	
	/**
	 * Button to redraw the chart
	 */
	private JButton redrawChartBut;
	
	/**
	 * Checkbox will ask for use files or not
	 */
	private JCheckBox showFilesCheckBox;
	
	/**
	 * Construct the Panel with a reference to the mainWindow
	 * @param mainWindow The mainWindow
	 */
	public GraphOptionPanel(PieChartPanel piePanel) {
		this.piePanel = piePanel;
		createGUI();
	}
	
	
	/**
	 * This creates the GUI and Layout for this panel
	 */
	private void createGUI() {
		this.setBorder(BorderFactory.createTitledBorder("Chart Options"));
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		GridBagConstraints gbc;
		
		// Use files or not?
		showFilesCheckBox = new JCheckBox("Show files", true);
		showFilesCheckBox.setEnabled(false);
		gbc = makegbc(0, 0, 1, 1);
		gbc.anchor = GridBagConstraints.CENTER;
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 100;
		// showFilesCheckBox.setBorder(BorderFactory.createTitledBorder("Test"));
		layout.setConstraints(showFilesCheckBox, gbc);
		this.add(showFilesCheckBox);
		
		// Group files
		JPanel groupFilesPanel = new JPanel();
		ignoreSmallerThenLabel = new JLabel("Group Items < ", JLabel.LEFT);
		ignoreSmallerThenLabel.setEnabled(false);
		groupFilesPanel.add(ignoreSmallerThenLabel);
		ignoreSmallerThenTextField = new JTextField(10);
		ignoreSmallerThenTextField.addFocusListener(new ConfirmFocusListener(ConfirmFocusListener.KMGT, "0"));
		ignoreSmallerThenTextField.setText("0");
		ignoreSmallerThenTextField.setEnabled(false);
		groupFilesPanel.add(ignoreSmallerThenTextField);
		groupChoice = new ButtonGroup();
		percentBut = new JRadioButton("%");
		percentBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infoLabel.setVisible(!percentBut.isSelected());
			}
		});
		percentBut.setSelected(true);
		percentBut.setEnabled(false);
		groupChoice.add(percentBut);
		groupFilesPanel.add(percentBut);
		byteBut = new JRadioButton("Byte");
		byteBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infoLabel.setVisible(byteBut.isSelected());
			}
		});
		byteBut.setEnabled(false);
		groupChoice.add(byteBut);
		groupFilesPanel.add(byteBut);
		JPanel groupFilesAllPanel = new JPanel();
		groupFilesAllPanel.setLayout(new BorderLayout());
		groupFilesAllPanel.add(groupFilesPanel, BorderLayout.CENTER);
		infoLabel = new JLabel("<html><small>You can enter something like 500k or 12M</small></html>", JLabel.CENTER);
		infoLabel.setVisible(false);
		infoLabel.setFont(infoLabel.getFont().deriveFont(Font.PLAIN));
		groupFilesAllPanel.add(infoLabel, BorderLayout.SOUTH);
		gbc = makegbc(1, 0, 1, 1);
		gbc.anchor = GridBagConstraints.CENTER;
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 100;
		layout.setConstraints(groupFilesAllPanel, gbc);
		this.add(groupFilesAllPanel);
				
		// Button to redraw the chart
		redrawChartBut = new JButton("Redraw chart");
		redrawChartBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performRedrawGraphPressed();
			}
		});
		redrawChartBut.setEnabled(false);
		gbc = makegbc(0, 1, 2, 1);
		layout.setConstraints(redrawChartBut, gbc);
		this.add(redrawChartBut);
	}
	
	
	/**
	 * To get the type on which should be grouped.
	 * @return the type on which should be grouped.
	 */
	public int getGroupType() {
		Enumeration<AbstractButton> e = groupChoice.getElements();
		for (int i = 0; i < groupChoice.getButtonCount(); i++) {
			AbstractButton button = e.nextElement();
			if (button.isSelected()) {
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * Get the value of the TextField for the minimum % size of items.<br>
	 * It also work with a k,m,g,t suffix
	 * @return the value of the TextField for the minimum % size of items
	 */
	public double getMinSize() {
		String value = ignoreSmallerThenTextField.getText();
		double ret = 0;
		try {
			ret = Double.parseDouble(value);
		} catch (Exception e) {
			String valueLower = value.toLowerCase();
			if (valueLower.endsWith("k") || valueLower.endsWith("m") || valueLower.endsWith("g") || valueLower.endsWith("t")) {
				String number = value.substring(0, value.length()-1);
				try {
					Double d =Double.parseDouble(number);
					if (valueLower.endsWith("k")) {
						d = d * 1000;
					} else if (valueLower.endsWith("m")) {
						d = d * 1000 * 1000;
					} else if (valueLower.endsWith("g")) {
						d = d * 1000 * 1000 * 1000;
					} else if (valueLower.endsWith("t")) {
						d = d * 1000 * 1000 * 1000 * 1000;
					}
					return d;
				} catch (Exception ex) {
					System.out.println("Failed to get the number... found: " + number);
				}
			} else {
				System.out.println("Sorry, can not find a suffix. Returning '0'");
			}
		}
		return ret;
	}
	
	
	/**
	 * Get the status if the checkbox "Show every single file" 
	 * @return
	 */
	public boolean getShowFiles() {
		return this.showFilesCheckBox.isSelected();
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
	 * Will be called when pressing the redrawGraphButton
	 */
	private void performRedrawGraphPressed() {
		piePanel.updateGraphView(piePanel.getCurrentDisplayedScannedFile());
	}
	
	
	/**
	 * Enable all buttons or disable them
	 * @param bol True for enable, false to disable
	 */
	public void setButtonsEnabled(boolean bol) {
		this.redrawChartBut.setEnabled(bol);
		this.showFilesCheckBox.setEnabled(bol);
		this.ignoreSmallerThenTextField.setEnabled(bol);
		this.ignoreSmallerThenLabel.setEnabled(bol);
		this.percentBut.setEnabled(bol);
		this.byteBut.setEnabled(bol);
	}

}
 