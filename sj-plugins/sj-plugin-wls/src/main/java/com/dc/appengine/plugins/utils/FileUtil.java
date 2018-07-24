package com.dc.appengine.plugins.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class FileUtil {
	private static Logger log = Logger.getLogger(FileUtil.class);

	public static void delFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else {
				File[] files = file.listFiles();
				for (File f : files) {
					delFile(f);
				}
				file.delete();
			}
		}
	}

	public static void delFile(String path) {
		delFile(new File(path));
	}

	/**
	 * 删除临时目录及其下所有文件
	 * 
	 * @param dir
	 * @return
	 */
	public static void delDirectory(File dir) {

		if (dir.isDirectory()) {
			File[] delFile = dir.listFiles();
			if (delFile.length == 0) { // 若目录下没有文件则直接删除
				dir.delete();
			} else {
				for (File subFile : delFile) {
					if (subFile.isDirectory()) {
						delDirectory(subFile); // 递归删除 (若有子目录的话)
					} else {
						subFile.delete();
					}
				}
			}
		}
		dir.delete();

	}

	/**
	 * 创建zipfile
	 * 
	 * @param pathStr
	 * @return
	 */

	// 非递归方式实现
	public static List<String> getAssignDirectoryFiles(File tmpFile) {
		List<String> fileList1 = new ArrayList<String>();
		LinkedList<File> linkedList = new LinkedList<File>();
		String filePath = "";
		if (!tmpFile.exists())
			return fileList1;
		// if(matched!=null&&!matched.equals(""))
		// matched = matched.toLowerCase();

		linkedList.addLast(tmpFile); // 构造一个栈
		while (linkedList.size() > 0) {
			File file = linkedList.removeFirst();
			File[] childFiles = file.listFiles();
			if (childFiles != null) {
				for (File f : childFiles) {
					if (f.isDirectory()) {
						linkedList.addLast(f);
					} else {
						filePath = f.toString();

						fileList1.add(filePath);
					}
				}
			}
		}

		return fileList1;

	}

	public static void deleteDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			return;
		}

		cleanDirectory(directory);
		if (!directory.delete()) {
			String message = "Unable to delete directory " + directory + ".";
			throw new IOException(message);
		}
	}

	/**
	 * Cleans a directory without deleting it.
	 *
	 * @param directory
	 *            directory to clean
	 * @throws IOException
	 *             in case cleaning is unsuccessful
	 */
	public static void cleanDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
			String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try {
				forceDelete(file);
			} catch (IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception) {
			throw exception;
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Deletes a file. If file is a directory, delete it and all
	 * sub-directories.
	 * <p>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li>You get exceptions when a file or directory cannot be deleted.
	 * (java.io.File methods returns a boolean)</li>
	 * </ul>
	 *
	 * @param file
	 *            file or directory to delete, must not be <code>null</code>
	 * @throws NullPointerException
	 *             if the directory is <code>null</code>
	 * @throws FileNotFoundException
	 *             if the file was not found
	 * @throws IOException
	 *             in case deletion is unsuccessful
	 */
	public static void forceDelete(File file) throws IOException {
		if (!file.exists())
			return;
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			if (!file.delete()) {
				String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}

	/**
	 * Schedules a file to be deleted when JVM exits. If file is directory
	 * delete it and all sub-directories.
	 *
	 * @param file
	 *            file or directory to delete, must not be <code>null</code>
	 * @throws NullPointerException
	 *             if the file is <code>null</code>
	 * @throws IOException
	 *             in case deletion is unsuccessful
	 */
	public static void forceDeleteOnExit(File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectoryOnExit(file);
		} else {
			file.deleteOnExit();
		}
	}

	/**
	 * Schedules a directory recursively for deletion on JVM exit.
	 *
	 * @param directory
	 *            directory to delete, must not be <code>null</code>
	 * @throws NullPointerException
	 *             if the directory is <code>null</code>
	 * @throws IOException
	 *             in case deletion is unsuccessful
	 */
	private static void deleteDirectoryOnExit(File directory) throws IOException {
		if (!directory.exists()) {
			return;
		}

		cleanDirectoryOnExit(directory);
		directory.deleteOnExit();
	}

	/**
	 * Cleans a directory without deleting it.
	 *
	 * @param directory
	 *            directory to clean, must not be <code>null</code>
	 * @throws NullPointerException
	 *             if the directory is <code>null</code>
	 * @throws IOException
	 *             in case cleaning is unsuccessful
	 */
	private static void cleanDirectoryOnExit(File directory) throws IOException {
		if (!directory.exists()) {
			String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
			String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try {
				forceDeleteOnExit(file);
			} catch (IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception) {
			throw exception;
		}
	}

}
