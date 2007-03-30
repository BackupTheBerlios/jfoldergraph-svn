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

package de.berlios.jfoldergraph.datastruct;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


/**
 * This class represents a directory or file in JFolderGraph.
 * All data will be collected by the scanner (ScanThread.java).
 * @author sebmeyer
 */
public class ScannedFile implements Serializable {
	
	
	// ---- Start of final static variables
	
	/**
	 * Status for errorlevel, should be used if the file has a child (or a subchild) which is not readable
	 */
	public final static int FILE_HAS_A_NOTREADABLE_CHILD = 2;
	
	/**
	 * Status for errorlevel, should be used if the file is not readable
	 */
	public final static int FILE_IS_NOT_READABLE = 1;
	
	/**
	 * Status for errorlevel, should be used if the file is readable
	 */
	public final static int FILE_IS_READABLE = 0;
	
	// ---- Eend of final static variables
	
	
	/**
	 * SVUID, this is importand to make possible to save this via a
	 * serialzied object for "saving project"
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Contains all children (files an directories), if
	 * the ScannedFile is a directory 
	 */
	private ArrayList<ScannedFile> childFiles;

	/**
	 * Contains the count of all files in the directory and
	 * all it's subdirectories
	 */
	private double countContainedFiles;
	
	/**
	 * Contains the count of all subdirectories "under" this
	 * file
	 */
	private double countContainedSubfolders;
	
	/**
	 * Contains the errorlevel of the file, which should be one of the static
	 * variables FILE_IS_READABLE, FILE_IS_NOT_READABLE, FILE_HAS_A_NOTREADABLE_CHILD.
	 */
	private int errorlevel = FILE_IS_READABLE;
	
	/**
	 * The name of this file or directory
	 */
	private String filename;
	
	/**
	 * is true if the file is a directory
	 */
	private boolean isDirectory;
	
	/**
	 * Contains the parent ScannedFile.
	 * This is null if the scan has startet in this Scanned File.
	 */
	private ScannedFile parent;
	
	/**
	 * If the ScannedFile is a File, this contains the
	 * name (and path) of the parent directory. If the
	 * ScannedFile is a directory, it contains the full path
	 * and name of the directory
	 */
	private String path;
	
	/**
	 * Contains the size of the file in bytes
	 */
	private double size;
	
	
	/**
	 * Constructor, all memberobjects will bin init
	 */
	public ScannedFile() {
		this.parent = null;
		path = new String();
		size = 0;
		isDirectory = false;
		childFiles = new ArrayList<ScannedFile>();
		countContainedFiles = 0;
		countContainedSubfolders = 0;
	}
	
	
	/**
	 * Add a child to the ScannedFile
	 * @param sfile The child-ScannedFile
	 */
	public void addChild(ScannedFile sfile) {
		// Setting this as the parent ScannedFile of the child
		sfile.setParent(this);
		// The size of the child SF will be added to the size of this SF
		this.setSize(this.getSize()+sfile.getSize());
		// If the child is a directory we will increase the foldercount for this SF by one.
		// We will also count the folder- and filecount from the child
		if (sfile.isDirectory()) {
			this.countContainedSubfolders = this.countContainedSubfolders + 1 + sfile.getCountainedSubfolderCount();
			this.countContainedFiles = this.countContainedFiles + sfile.getContainedFilesCount();
		} else {
			// The child is a file? So we only increase countCointainedFile by 1
			this.countContainedFiles = this.countContainedFiles + 1;
		}
		// Now we set the errorlevel for the file
		if (sfile.getErrorLevel() == FILE_IS_NOT_READABLE || sfile.getErrorLevel() == FILE_HAS_A_NOTREADABLE_CHILD) {
			this.setErrorLevel(FILE_HAS_A_NOTREADABLE_CHILD);
		}
		childFiles.add(sfile);
	}
	
	
	/**
	 * Returns the count of all files "under" this file
	 * @return the count of all files "under" this file
	 */
	public double getContainedFilesCount() {
		return this.countContainedFiles;
	}
	
	
	/**
	 * Returns the count of all folders "under" this file
	 * @return the count of all folders "under" this file
	 */
	public double getCountainedSubfolderCount() {
		return this.countContainedSubfolders;
	}
	
	
	/**
	 * Returns the errorlevel of the file, which should be one of the static
	 * variables FILE_IS_READABLE, FILE_IS_NOT_READABLE, FILE_HAS_A_NOTREADABLE_CHILD.
	 * @return the errorlevel
	 */
	public int getErrorLevel() {
		return errorlevel;
	}
	
	
	/**
	 * To get the Filename
	 * @return The Filename
	 */
	public String getFilename() {
		return filename;
	}
	
	
	/**
	 * To get the size of the file in a human-readable form with the binary prefix.
	 * @return This size of the File in a human-readable form
	 */
	public String getHumanReadableBinarySize() {
		final String[] types = {"Byte", "kiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"};
		int count = 0;
		double value = size;
		while (value > 1000 && count < 8) {
			count++;
			value = value / 1024;
		}
		DecimalFormat df = new DecimalFormat("#,##0.##");
		return (df.format(value) + " " + types[count]);
	}
	
	

	/**
	 * To get the size of the file in a human-readable form with the SI prefix.
	 * @return This size of the File in a human-readable form
	 */
	public String getHumanReadableSISize() {
		final String[] types = {"Byte", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
		int count = 0;
		double value = size;
		while (value > 1000 && count < 8) {
			count++;
			value = value / 1000;
		}
		DecimalFormat df = new DecimalFormat("#,##0.##");
		return (df.format(value) + " " + types[count]);
	}
	
	
	/**
	 * Returns the parent ScannedFile. If there is no ScannedFile  
	 * (because the scan has startet in this directory) the return
	 * is null
	 * @return The parent ScannedFile
	 */
	public ScannedFile getParent() {
		return parent;
	}
	
	
	/**
	 * To get the path of the ScannedFile. If the ScannedFile is
	 * a direcotory the full path and the name of the directory returns.
	 * Otherwise, if the ScannedFile is a file, the full path of the
	 * parent directory returns.<br>
	 * This will be regulatet from the diretory-scanner (ScanThread.java)
	 * @return
	 */
	public String getPath() {
		return path;
	}
	
	
	/**
	 * Returns the percent-size of the file as a part of the parent-directory.<br>
	 * If the parent directory has a size if 20byte and this file has a size of
	 * 15 byte the retun will be 75% 
	 * @return the percent-size of the file as a part of the parent-directory
	 */
	public double getPercentSize() {
		double percent = (((double)this.getSize()) / ((double)this.getParent().getSize()) * ((double)100));
		return percent;
	}
	
	
	/**
	 * Get the size of the ScannedFile.<br>
	 * If the ScannedFile is a File it returns the
	 * size of the file in byte.
	 * @return
	 */
	public double getSize() {
		return size;
	}
	
	
	/**
	 * Returns the children of the scanned file, sorted by size
	 * @return the children of the scanned file, sorted by size
	 */
	@SuppressWarnings("unchecked")
	public Iterator<ScannedFile> getSortedChildFiles(boolean withFiles) {
		if (withFiles) {
			// Let's get all Childs, also the children
			Collections.sort(childFiles, new ScannedFileComperator());
		} else {
			ScannedFile files = new ScannedFile();
			files.setDirectory(false);
			files.setFilename("Files in the folder");
			files.setParent(this);
			ArrayList<ScannedFile> childsWithoutFiles = new ArrayList<ScannedFile>();
			for (int i = 0; i < childFiles.size(); i++) {
				if (childFiles.get(i).isDirectory()) {
					childsWithoutFiles.add(childFiles.get(i));
				} else {
					files.setSize(files.getSize()+childFiles.get(i).getSize());
				}
			}
			childsWithoutFiles.add(files);
			Collections.sort(childsWithoutFiles, new ScannedFileComperator());
			return childsWithoutFiles.iterator();
		}
		return childFiles.iterator();
	}
	
	
	/**
	 * Returns true if the ScannedFile is a directory
	 * @return True if the ScannedFile is a directory
	 */
	public boolean isDirectory() {
		return isDirectory;
	}
	
	/**
	 * Set the information if the ScannedFile is a directory
	 * @param isDirectory Should be true if the ScannedFile is a directory
	 */
	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	
	
	/**
	 * Set the errorlevel of the file, which should be one of the static
	 * variables FILE_IS_READABLE, FILE_IS_NOT_READABLE, FILE_HAS_A_NOTREADABLE_CHILD.
	 * @param errorlevel
	 */
	public void setErrorLevel(int errorlevel) {
		this.errorlevel = errorlevel;
		if ((errorlevel == FILE_IS_NOT_READABLE || errorlevel == FILE_HAS_A_NOTREADABLE_CHILD) && this.getParent() != null ) {
			this.getParent().setErrorLevel(FILE_HAS_A_NOTREADABLE_CHILD);
		}
	}
	
	
	/**
	 * To set the filename
	 * @param filename The new Filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
	/**
	 * To set the parent ScannedFile
	 * @param parent The parent ScannedFile
	 */
	private void setParent(ScannedFile parent) {
		this.parent = parent;
	}
	
	/**
	 * Set the path
	 * @param path The path of the ScannedFile
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Set the size of the ScannedFile
	 * @param size The size of the file
	 */
	public void setSize(double size) {
		if (this.getParent() != null) {
			double oldSize = this.getSize();
			double newSize = size - oldSize;
			if (this.getParent() != null) {
				this.getParent().setSize(this.getParent().getSize()+newSize);
			}
		}
		this.size = size;
	}
	
	
}
