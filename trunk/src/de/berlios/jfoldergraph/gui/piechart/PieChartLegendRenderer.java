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
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Renderer for the Legend-List in the PieChartPanel
 * @author sebmeyer
 */
public class PieChartLegendRenderer extends JPanel implements ListCellRenderer {

	/**
	 * Generated SVUID
	 */
	private static final long serialVersionUID = -1623931350358860775L;
	
	/**
	 * Will display the name of the entry
	 */
	JLabel name;
	
	/**
	 * The Border for selected/not selected
	 */
	private final static Border NO_FOCUS_BORDER =  new EmptyBorder(1, 1, 1, 1);
	
	/**
	 * Constructs the Renderer
	 */
	public PieChartLegendRenderer() {
		name = new JLabel("", JLabel.LEFT);
		name.setFont(name.getFont().deriveFont(Font.PLAIN));
		createGUI();
	}
	
	/**
	 * Creates the GUI and the Layout
	 */
	private void createGUI() {
		this.setLayout(new BorderLayout());
		this.add(name, BorderLayout.CENTER);
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value instanceof PieData) {
			PieData pieData = (PieData) value;
			name.setText(pieData.getName());
			name.setForeground(pieData.getColor());
		}
		setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
		setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
		setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : NO_FOCUS_BORDER);
		return this;
	}

}
