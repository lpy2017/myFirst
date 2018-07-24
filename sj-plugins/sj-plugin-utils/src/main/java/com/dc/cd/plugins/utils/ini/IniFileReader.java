package com.dc.cd.plugins.utils.ini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.dtools.ini.Commentable;
import org.dtools.ini.FormatException;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;
import org.dtools.ini.InvalidNameException;

public class IniFileReader {
	private File file;
	private IniFile ini;

	static String getEndLineComment(String line) {
		if (!isSection(line) && !isItem(line)) {
			throw new FormatException(
					"getEndLineComment(String) is unable to return the comment from the given string (\"" + line
							+ "\" as it is not an item nor a section.");

		} else {
			int pos = line.indexOf(59);

			return pos == -1 ? "" : line.substring(pos + 1).trim();
		}
	}

	static String getItemName(String line) {
		if (!isItem(line)) {
			throw new FormatException(
					"getItemName(String) is unable to return the name of the item as the given string (\"" + line
							+ "\" is not an item.");

		} else {
			int pos = line.indexOf(61);

			return pos == -1 ? "" : line.substring(0, pos).trim();
		}
	}

	static String getItemValue(String line) {
		if (!isItem(line)) {
			throw new FormatException(
					"getItemValue(String) is unable to return the value of the item as the given string (\"" + line
							+ "\" is not an item.");

		} else {
			int posEquals = line.indexOf(61);
			int posComment = line.indexOf(59);

			return posEquals == -1 ? (posComment == -1 ? line : line.substring(0, posComment).trim())
					: (posComment == -1 ? line.substring(posEquals + 1).trim()
							: line.substring(posEquals + 1, posComment).trim());
		}
	}

	static String getSectionName(String line) {
		if (!isSection(line)) {
			throw new FormatException(
					"getSectionName(String) is unable to return the name of the section as the given string (\"" + line
							+ "\" is not a section.");

		} else {
			int firstPos = line.indexOf(91);
			int lastPos = line.indexOf(93);

			return line.substring(firstPos + 1, lastPos).trim();
		}
	}

	static boolean isComment(String line) {
		line = line.trim();

		if (line.isEmpty()) {
			return false;

		} else {
			char firstChar = line.charAt(0);
			return firstChar == 59;
		}
	}

	static boolean isItem(String line) {
		line = removeComments(line);

		if (line.isEmpty()) {
			return false;

		} else {
			int pos = line.indexOf(61);

			if (pos != -1) {

				String name = line.substring(0, pos).trim();

				return name.length() > 0;

			} else {
				return false;
			}
		}
	}

	static boolean isSection(String line) {
		line = removeComments(line);

		if (line.isEmpty()) {
			return false;

		} else {
			char firstChar = line.charAt(0);
			char lastChar = line.charAt(line.length() - 1);

			return firstChar == 91 && lastChar == 93;
		}
	}

	static String removeComments(String line) {
		return line.contains(String.valueOf(';')) ? line.substring(0, line.indexOf(59)).trim() : line.trim();
	}

	public IniFileReader(IniFile ini, File file) {
		if (ini == null) {
			throw new NullPointerException("The given IniFile cannot be null.");

		} else if (file == null) {
			throw new NullPointerException("The given File cannot be null.");

		} else {
			this.file = file;
			this.ini = ini;
		}
	}

	public void read(String encoding) throws IOException {
		IniSection currentSection = null;
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), encoding));

			String comment = "";
			Object lastCommentable = null;
			String line;
			while ((line = reader.readLine()) != null) {

				line = line.trim();

				if (line.isEmpty()) {

					if (!comment.isEmpty() && lastCommentable != null) {
						((Commentable) lastCommentable).setPostComment(comment);
						comment = "";

					}
				} else {
					String itemName;
					if (isComment(line)) {

						itemName = line.substring(1).trim();

						if (comment.isEmpty()) {
							comment = itemName;

						} else {
							comment = comment + "\n" + itemName;

						}
					} else {
						String itemValue;
						if (isSection(line)) {

							itemName = getSectionName(line);

							itemValue = getEndLineComment(line);

							if (this.ini.hasSection(itemName)) {
								currentSection = this.ini.getSection(itemName);

							} else {
								currentSection = this.ini.addSection(itemName);

							}

							currentSection.setEndLineComment(itemValue);

							if (!comment.isEmpty()) {
								currentSection.setPreComment(comment);
								comment = "";

							}

							lastCommentable = currentSection;

						} else if (isItem(line)) {

							if (currentSection == null) {
								throw new FormatException("An Item has been read,before any section.");

							}

							itemName = getItemName(line);
							itemValue = getItemValue(line);
							String endLineComment = getEndLineComment(line);

							IniItem item;
							if (currentSection.hasItem(itemName)) {
								item = currentSection.getItem(itemName);

							} else {
								try {
									item = currentSection.addItem(itemName);

								} catch (InvalidNameException arg10) {
									throw new FormatException(
											"The string \"" + itemName + "\" is an invalid name for an " + "IniItem.");

								}
							}

							item.setValue(itemValue);
							item.setEndLineComment(endLineComment);

							if (!comment.isEmpty()) {
								item.setPreComment(comment);
								comment = "";

							}

							lastCommentable = item;

						}
					}
				}
			}

			if (!comment.isEmpty() && lastCommentable != null) {
				((Commentable) lastCommentable).setPostComment(comment);
				comment = "";
			}

//			reader.close();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

	}
}