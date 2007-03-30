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
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import de.berlios.jfoldergraph.ScanThread;
import de.berlios.jfoldergraph.datastruct.ScannedFile;


/**
 * This will be used to scan a folder.
 * This is only the visible side of the scanning, the real scan
 * will be performed by the ScanThread which will be created and
 * invoked by this.
 * @author sebmeyer
 */
public class ProgressDialog extends JDialog {
	
	/**
	 * Generated SVUID
	 */
	private static final long serialVersionUID = -4568732503816876013L;
	
	
	/**
	 * This Textfield should show the actual scanned directory
	 */
	private JTextField actualField;
	
	/**
	 * The Button to cancel the scanning
	 */
	private JButton cancelButton;
	
	/**
	 * Contains the File from where the scan should start
	 */
	private File startFile;
	
	/**
	 * Contains the thread which do the scan
	 */
	private ScanThread thread;
	
	
	/**
	 * Constructer which needs the file to start.
	 * The Thread to scan will be invoked here
	 * @param startFile The file on which the scan should start
	 */
	public ProgressDialog(File startFile) {
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		// WindowListener to interrup the Thread when the window is closing by WindowManager
		this.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				performCancelPressed();	
			}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}
		});
		this.setModal(true);
		this.startFile = startFile;
		createGUI();
		startScannerThread();
	}
	
	
	/**
	 * Creates the panel for the center position
	 * @return The panel for the center position
	 */
	private JPanel centerPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3,1));
		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		panel.add(bar);
		panel.add(new JLabel("Scanning:", JLabel.CENTER));
		actualField = new JTextField();
		actualField.setEditable(false);
		panel.add(actualField);
		return panel;
	}
	
	
	/**
	 * Creates the GUI and do its layout
	 */
	private void createGUI() {
		this.setTitle("Scanning in progress");
		this.setLayout(new BorderLayout());
		this.add(northPanel(), BorderLayout.NORTH);
		this.add(centerPanel(), BorderLayout.CENTER);
		this.add(southPanel(), BorderLayout.SOUTH);
		this.pack();
		this.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 5 * 4, this.getHeight());
		this.setLocationRelativeTo(null);
	}
	
	
	/**
	 * To get the result of the scan. If the scan was canceled
	 * or an error has occured, this will return null
	 * @return The Result of the scan
	 */
	public ScannedFile getScanResult() {
		if (thread == null) {
			return null;
		}
		if (thread.endsWithOOMemoryException) {
			System.out.println("OutOfMemoryException while Scanning! : ParentDirectory trys to handle .... ");
			thread = null;
			System.gc();
			JOptionPane.showMessageDialog(this, 
					"<html>An \"Out of memory-Exception\" occures!<br>Please start the JVM with more memory." +
					"<br>See 'Menu->Help->Help' for more informations", "Out of memory", 
					JOptionPane.ERROR_MESSAGE);
			System.out.println("OutOfMemoryException while Scanning! : All done and exception was catched. Alive again.");
			return null;
		}
		return thread.getScannedFile();
	}
	
	/**
	 * Creates the panel for the north position
	 * @return The panel for the north position
	 */
	private JPanel northPanel() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("Now scanning the directory and it's subdirectories", JLabel.CENTER));
		panel.setBorder(BorderFactory.createRaisedBevelBorder());
		return panel;
	}
	
	
	/**
	 * This should be called if the cancel-button was pressed
	 */
	private void performCancelPressed() {
		cancelButton.setEnabled(false);
		thread.interrup();
	}
	
	/**
	 * This method can be used to write a text in the TextFields
	 * which should show the actual scanned directory.
	 * So this should get the name of the actual scanned directory
	 * from the scannerthread ;)
	 * @param dir The name of the actual scanned directory
	 */
	public void setActualDir(String dir) {
		actualField.setText(dir);
	}
	
	/**
	 * Creates the panel for the south position
	 * @return The panel for the south position
	 */
	private JPanel southPanel() {
		JPanel panel = new JPanel();
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performCancelPressed();
			}
		});
		panel.add(cancelButton);
		panel.setBorder(BorderFactory.createRaisedBevelBorder());
		return panel;
	}
	
	/**
	 * This should be called to start the scannerthread
	 */
	private void startScannerThread() {
		thread = new ScanThread(startFile, this);
		thread.start();
	}

}
