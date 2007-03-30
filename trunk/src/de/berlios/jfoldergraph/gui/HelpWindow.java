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

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import de.berlios.jfoldergraph.FolderGraph;


/**
 * This Class displays the "Help-Window"
 * @author sebmeyer
 */
public class HelpWindow extends JFrame {

	/**
	 * This is the text which will be displayed in the frame
	 */
	private final static String HELPTEXT =
		"<html>" +
		"<u><b><big> Help and FAQs</u></b></big><br><br>" +
		FolderGraph.PROGRAM_NAME + " is only a small tool, so this is only a little Help which should guide you to "+
		"your scans.<br><br>" +
		"<b> 1. What is " + FolderGraph.PROGRAM_NAME + "<br>" +
		"2. How can I start a scan?<br>" +
		"3. What is the difference between green, red and orange folders?<br>" +
		"4. How can I save and open a project?<br>" +
		"5. What should I do when a \"Out of Memory-Exception\" occures?</b><br><br>" +
		"<u><b><i>1. What is " + FolderGraph.PROGRAM_NAME + "</u></b></i><br>" +
		FolderGraph.PROGRAM_NAME + " is a little tool which can help you to find hidden big files or directories. It shows the size of " +
		"a directory and all the files in it in a chart.<br>It is also possible to save a scan and load it anytime. That's interesting if " +
		"someone will help another user. The user who needs help can perform a scan, save the project and send it to the other user. He can " +
		"open the project and take a look to the size of the files.<br><br>"+
		"<u><b><i>2. How can I start a scan?</u></b></i><br>" +
		"That's easy. Choose <i>Project->New Project</i> and choose a directory. After that, " + FolderGraph.PROGRAM_NAME + " will start to scan the directory and recursiv all " +
		"the files and directoeries in it. After the scan has finished, the program has collected the size of all files and display it. You can also " +
		"change into a subdirectory without a new scan because " + FolderGraph.PROGRAM_NAME + " has allready all information it needs to display.<br><br>" +
		"<u><b><i>3. What is the difference between green, red and orange folders?</u></b></i><br>" +
		"After a scan directories will have a green, red or orange folder-icon.<br>When a directory has a <FONT COLOR=\"#008000\">green icon</FONT> " +
		"all it's subdirectoris could be opened while scanning and all files where analized to calculate the size of the directory. Only " +
		"the size of directories with a green folder are the real size, because all needed files could be analized.<br>" +
		"A directory with an <FONT COLOR=\"#ff0000\">red icon</FONT> are not readable. That means: " + FolderGraph.PROGRAM_NAME + " could not open it to " +
		"analize the files in it. So it can say nothing about the size of the directory. Most it's a problem with the permission.<br>" +
		"Directories with an <FONT COLOR=\"#ff6633\">orange icon</FONT> has one or more subdirectories which are not readable. So the " +
		"displayed size was calculated without the files in these directories.<br><br>" +
		"<u><b><i>How can I save and open a project?</u></b></i><br>" +
		"After you have scanned a new project it can be saved by clicking <i>Project->Save Project</i>. " + FolderGraph.PROGRAM_NAME + " saves all collected " +
		"data in a file and compress it.<br>To load a saved project use <i>Project->Open Project</i>.<br>It is possible, that the fomrat of the " +
		"saved data will changed in the future, so it's not shure for coming versions to open saved data from this version.<br><br>" +
		"<u><b><i>5. What should I do when a \"Out of Memory-Exception\" occures?</u></b></i><br>" +
		FolderGraph.PROGRAM_NAME + " is a program written in Java. So it runs only in the Java-Virtual-Machin (JVM). The JVM does not use all your mermory, " +
		"so large scans (f.e. a whole system) can be to big for the memory the JVM has reserved for the program. The script-files " +
		"which are starting the program has allready some settings to start the JVM with more memory. To enlarge it, please edit the " +
		"scriptfiles and change the value at the <i>-Xmx</i> argument, which specify the maximum used memory." +
		"</html>";
	
	
	/**
	 * Generated svuid
	 */
	private static final long serialVersionUID = -419478713428536148L;
	
	
	/**
	 * Constructer which will show the Help-Window autonom
	 */
	public HelpWindow() {
		this.setTitle(FolderGraph.PROGRAM_NAME + " - Help");
		this.createGUI();
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	/**
	 * Creates the GUI for the Window
	 */
	private void createGUI() {
		this.setLayout(new BorderLayout());
		// Construct the field wich will display the Help
		JEditorPane textField = new JEditorPane();
		textField.setEditable(false);
		textField.setContentType("text/html");
		JScrollPane scrollPane = new JScrollPane(textField);
		textField.setText(HELPTEXT);
		textField.setCaretPosition(0);
		this.add(scrollPane, BorderLayout.CENTER);
	}

}
