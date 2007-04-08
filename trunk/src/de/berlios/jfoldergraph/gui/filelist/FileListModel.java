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

import java.util.Vector;

import javax.swing.AbstractListModel;

/**
 * This ist the ListModel for the FileList in the mainGUI.
 * The DefaultListModel in the java-default-library update
 * the GUI after every single addElement. This update is to
 * expensive and much to slow.<br>
 * This ListModel needs to be notified after all new Object
 * are added to it. So the layout must only done one time
 * after all adds... not after every add. 
 * @author sebmeyer
 */
public class FileListModel extends AbstractListModel {
	
	
	/**
	 * Generated SVUID
	 */
	private static final long serialVersionUID = 9114538403356146045L;
	
	
	/**
	 * This vector contains the data
	 */
	private Vector delegate = new Vector();
	

	/**
	 * Adds a new Element to the end of the list.
	 * @param o The new Element
	 */
	@SuppressWarnings("unchecked")
	public void addElement(Object o) {
		delegate.addElement(o);
	}

	
	/**
	 * This will update the list after the new entries
	 * were added. The DefaultListModel update the view
	 * after every single add, so it is very slow.
	 * This model does not update the view automatic after
	 * adding an entry, it must be called and notfied after
	 * the last add manual by calling this method 
	 */
	public void fireEntryChanged() {
		this.fireIntervalAdded(this, 0, delegate.size());
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	public Object getElementAt(int index) {
		Object o = delegate.get(index);
		return o;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		int i = delegate.size();
		return i;
	}
	
	
	/**
	 * Removes all Ellements from the list
	 */
	public void removeAllElements() {
		int oldsize = delegate.size();
		delegate.removeAllElements();
		this.fireIntervalRemoved(this, 0, oldsize);
	}

}
