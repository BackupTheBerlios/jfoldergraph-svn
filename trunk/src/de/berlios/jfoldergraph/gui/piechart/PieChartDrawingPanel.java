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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * This is the panel on which the the chart will be drawn
 * @author sebmeyer
 */
public class PieChartDrawingPanel extends JSplitPane {

	/**
	 * Generated SVUID 
	 */
	private static final long serialVersionUID = -6507280055305681459L;
	
	/**
	 * On this panel the chart will be drawn
	 */
	JPanel chartPanel;
	
	/**
	 * The List which will be displayed as the legend
	 */
	JList legendList;
	
	/**
	 * The data for the chart
	 */
	private PieDataSet pieDataSet;
	
	/**
	 * Constructs the panel and set the sizes
	 */
	public PieChartDrawingPanel() {
		chartPanel = createChartPanel();
		chartPanel.setMinimumSize(new Dimension(200, 200));
		this.setLeftComponent(chartPanel);
		legendList = createLegendList();
		this.setRightComponent(new JScrollPane(legendList));
		this.setDividerLocation(600);
		this.setPreferredSize(new Dimension(700, 400));
	}
	
	/**
	 * This creates the JPanel on which the chart will be drawn
	 * @return the Panel for the chart
	 */
	private JPanel createChartPanel() {
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics g) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				int size = 0;
				int x;
				int y;
				if (this.getWidth() <= this.getHeight()) {
					size = this.getWidth() / 5 * 4;
				} else if (this.getWidth() > this.getHeight()) {
					size = this.getHeight() / 5 * 4;
				}
				x = (this.getWidth() - size) / 2;
				y = (this.getHeight() - size) / 2;
				if (pieDataSet != null) {
					int drawnDegrees = 90;
					for (int i = 0; i < pieDataSet.getSize(); i++) {
						if (drawnDegrees <= -270) {
							break;
						}
						Color color = pieDataSet.getColorAt(i);
						g.setColor(color);
						int arcAngle = (((int)Math.round(((double)360 / (double)100 * pieDataSet.getValueAt(i)))*-1)-1);
						g.fillArc(x, y, size, size, drawnDegrees, arcAngle);
						drawnDegrees = drawnDegrees + arcAngle;
					}
				}	
			}
		};
		return panel;
	}
	
	/**
	 * Creates the JList for the legend
	 * @return The JList for the legend
	 */
	private JList createLegendList() {
		JList list = new JList();
		list.setCellRenderer(new PieChartLegendRenderer());
		list.setModel(new LegendListModel());
		return list;
	}
	
	/**
	 * Should be called to force a repaint
	 */
	public void fireRepaint() {
		chartPanel.repaint();
		LegendListModel model = (LegendListModel) legendList.getModel();
		model.removeAllElements();
		int degrees = 90;
		for (int i = 0; i < pieDataSet.getSize(); i++) {
			if (degrees <= -270) {
				break;
			}
			int arcAngle = (((int)Math.round(((double)360 / (double)100 * pieDataSet.getValueAt(i)))*-1)-1);
			degrees = degrees + arcAngle;
			model.addElement(pieDataSet.getPieDataEntryAt(i));
		}
		model.fireEntryChanged();
	}
	
		
	/**
	 * Draws the chart
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		int size = 0;
		int x;
		int y;
		if (this.getWidth() <= this.getHeight()) {
			size = this.getWidth() / 5 * 4;
		} else if (this.getWidth() > this.getHeight()) {
			size = this.getHeight() / 5 * 4;
		}
		x = (this.getWidth() - size) / 2;
		y = (this.getHeight() - size) / 2;
		if (pieDataSet != null) {
			int drawnDegrees = 90;
			for (int i = 0; i < pieDataSet.getSize(); i++) {
				if (drawnDegrees <= -270) {
					break;
				}
				Color color = pieDataSet.getColorAt(i);
				g.setColor(color);
				pieDataSet.setColorAt(i, color);
				int arcAngle = (((int)Math.round(((double)360 / (double)100 * pieDataSet.getValueAt(i)))*-1)-1);
				g.fillArc(x, y, size, size, drawnDegrees, arcAngle);
				drawnDegrees = drawnDegrees + arcAngle;
			}
		}
	}
	
	
	/**
	 * Sets the data-object which for the chart
	 * @param pieData the dataset-object
	 */
	public void setPieDataSet(PieDataSet pieDataSet) {
		this.pieDataSet = pieDataSet;
	}
	
	

}
