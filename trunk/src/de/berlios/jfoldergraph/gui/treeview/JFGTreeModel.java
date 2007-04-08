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

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import de.berlios.jfoldergraph.datastruct.ScannedFile;

/**
 * This is the TreeModel for the Tree-View
 * @author sebmeyer
 */
public class JFGTreeModel implements TreeModel {
	
	
	/**
	 * Contains the root of the model
	 */
	private ScannedFile root;
	
	
	public JFGTreeModel(ScannedFile root) {
		this.root = root;
	}
	

	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object parent, int index) {
		if (!(parent instanceof ScannedFile)) {
			System.out.println("TreeModel.getChild gets an invalid Object!");
			return null;
		} else {
			ScannedFile sf = (ScannedFile) parent;
			if (index < 0 || index > sf.getChildCount()-1) {
				return null;
			} else {
				return sf.getChildAt(index);
			}
		}
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object parent) {
		if (!(parent instanceof ScannedFile)) {
			System.out.println("TreeModel.getChildCount gets an invalid Object!");
			return 0;
		} else {
			ScannedFile sf = (ScannedFile) parent;
			return sf.getChildCount();
		}
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child) {
		if (!(parent instanceof ScannedFile)) {
			System.out.println("TreeModel.getIndexOfChild gets an invalid parent-Object!");
			return -1;
		} else if (!(child instanceof ScannedFile)) {
			System.out.println("TreeModel.getIndexOfChild gets an invalid child-Object!");
			return -1;
		} else {
			ScannedFile parentSF = (ScannedFile) parent;
			ScannedFile childSF = (ScannedFile) child;
			return parentSF.getIndexOfChild(childSF);
		}
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Object getRoot() {
		return root;
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node) {
		if (!(node instanceof ScannedFile)) {
			System.out.println("TreeModel.isLeaf gets an invalid Object!");
			return false;
		} else {
			ScannedFile sf = (ScannedFile) node;
			if (sf.getChildCount() == 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		
	}

	
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
		
	}

}
