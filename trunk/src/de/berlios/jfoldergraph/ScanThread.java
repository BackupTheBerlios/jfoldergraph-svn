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

package de.berlios.jfoldergraph;

import java.io.File;
import java.io.IOException;

import de.berlios.jfoldergraph.datastruct.ScannedFile;
import de.berlios.jfoldergraph.gui.ProgressDialog;


/**
 * This is the Thread which scans the directories to
 * calculate there size.
 * It runs as a Thread, invoked by the ProgressDialog
 * @author sebmeyer
 */
public class ScanThread extends Thread {
	
	/**
	 * Will be true if the scan ends with an OutOfMemory-Exception
	 */
	public boolean endsWithOOMemoryException = false;
	
	/**
	 * Contains the status for interruption
	 */
	private boolean isInterrupted = false;
	
	/**
	 * The ProgressDialog which has startet the scan
	 */
	ProgressDialog parentDialog;
	
	/**
	 * Contains the result of the scan
	 */
	private ScannedFile scannedFile;
	
	/**
	 * Contains the File where the scan should start
	 */
	private File startFile;
	
	
	/**
	 * Constructor needs the startfile and the parentDialog
	 * @param startFile The File where the Scan should start
	 * @param parentDialog The calling ProgressDialog
	 */
	public ScanThread(File startFile, ProgressDialog parentDialog) {
		this.startFile = startFile;
		this.parentDialog = parentDialog;
	}
	
	/**
	 * To get the reusult of the scan.
	 * On error or after cancel scanning null returns
	 * @return The scanninfresult
	 */
	public ScannedFile getScannedFile() {
		if (isInterrupted) {
			return null;
		}
		return scannedFile;
	}
	
	
	/**
	 * This should be called to interrup the scanner.
	 * The parent progressDialog is callint this method
	 * if the cancel-button is pressed
	 */
	public void interrup() {
		System.out.println("ScanThread has got the interrupt-signal");
		this.isInterrupted=true;
		scannedFile = null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		System.out.println("ScanThread has been started...");
		scannedFile = scan(startFile);
		parentDialog.dispose();
		System.out.println("ScanThread has finished!");
	}
	
	/**
	 * This is the scanner-method which will be
	 * called from the run-method.
	 * All work will here been done.
	 * It calls itself recursiv for all subdirectories
	 * @param file The file which should be scanned
	 * @return The File as ScannedFile-Object
	 */
	private ScannedFile scan(File file) {
		try {
			// Writing the actual scanned path to the dialog
			parentDialog.setActualDir(file.getPath());
			// Creating the ScannedFile that will return
			ScannedFile sf = new ScannedFile();
			// Set the Filename
			sf.setFilename(file.getName());
			// Set the path. It's the parent directory if
			// the file is only a file. If it's a directory
			// it will be the full path of it.
			if (file.isDirectory()) {
				sf.setPath(file.getPath());
			} else {
				sf.setPath(file.getParentFile().getPath());
			}
			// Set the flag if is a directory, or not
			sf.setDirectory(file.isDirectory());
			// Set a size for the Scanned File, but only
			// if the file is a directory, because the result is
			// unknown by the jvm if it is a directory
			if (!sf.isDirectory()) {
				sf.setSize(file.length());
			}
			// If the current scanned file is a directory we
			// will now add the childs by calling this method
			// for every single child and adding the result to
			// this scanned file
			if (file.isDirectory()) {
				// We should test for opening and reading the directory
				// If it is not readable we can not count the filesize in it and we
				// should make it visible tot the user
				if (file.canRead()) {
					File[] childFiles = file.listFiles();
					// Be Carefull: Somentimes File.listFiles returns null.
					//              -> Must be checked!
					for (int i = 0; i < childFiles.length; i++) {
						if (isInterrupted) { 
							break;
						}
						// Testing childFiles[i] for nullpointer.
						// Sometimes the OS count files, but there is nothing
						if (childFiles[i] == null) {
							System.out.println("Info: Nullpointer while scanning");
							System.out.println("      File: " + i + " in " + file.getName());
							continue;
						}
						// is the child only a softlink?
						// when absolute path and canoncial is the same,
						// it is not link
						// 
						try {
							if (childFiles[i].getCanonicalPath().equals(childFiles[i].getAbsolutePath()))
							sf.addChild(scan(childFiles[i]));
						} catch (IOException e) {
							System.out.println("Exception: " + e.getMessage());
							System.out.println("           While scanning::test for link");
							e.printStackTrace();
						}
					}
				} else {
					// The directory is not readable
					sf.setErrorLevel(ScannedFile.FILE_IS_NOT_READABLE);
				}
			}
			return sf;
		} catch (OutOfMemoryError e) {
			// All scannin is runnin in a try/catch-Block to make shure no Ou of Memory connection will
			// bring the program down.
			// If a OutOfMemory-Exception occures, scanning will be interrupted an and the
			// "endsWithOOMemoryException" will be set. The caller-dialog should test for the flag and
			// set the thread to null.
			// Be carefull: I tested to handle the exception here, but it seems it is not enough to set the
			// ScannedFile-root-Object to null. But when the Threas-object is set to null all references are
			// killed.
			System.out.println("OutOfMemoryException while Scanning! : start to handle exception ... ");
			this.interrup();
			this.endsWithOOMemoryException = true;
			System.gc();
			System.out.println("OutOfMemoryException while Scanning! : ScanThread interrupted .... ");
			return new ScannedFile();
		}
		
	}

}
