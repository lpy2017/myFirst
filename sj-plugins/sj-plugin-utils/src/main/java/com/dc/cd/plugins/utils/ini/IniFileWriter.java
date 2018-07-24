package com.dc.cd.plugins.utils.ini;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.dtools.ini.IniFile;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;

public class IniFileWriter {
	public static final String ENCODING = "ASCII";
	private IniFile ini;
	private File file;
	private boolean sectionLineSeparator;
	private boolean includeSpaces;
	private boolean itemLineSeparator;

	public IniFileWriter(IniFile ini, File file) {
		if (ini == null) {
			throw new IllegalArgumentException("Cannot write a null IniFile");

		} else if (file == null) {
			throw new IllegalArgumentException("Cannot write an IniFile to a null file");

		} else {
			this.ini = ini;
			this.file = file;

			this.setIncludeSpaces(false);
			this.setItemLineSeparator(false);
			this.setSectionLineSeparator(false);
		}
	}

	private String iniToString(IniFile ini) {
		StringBuilder builder = new StringBuilder();

		int size = ini.getNumberOfSections();

		for (int i = 0; i < size; ++i) {
			IniSection section = ini.getSection(i);

			builder.append(this.sectionToString(section));
			builder.append("\r\n");

		}

		return builder.toString();
	}

	private String formatComment(String comment, boolean prefixNewLine) {
		StringBuilder sb = new StringBuilder();

		if (comment.contains("\n")) {

			String[] comments = comment.split("\n");
			String[] arg7 = comments;
			int arg6 = comments.length;
			for (int arg5 = 0; arg5 < arg6; ++arg5) {
				String aComment = arg7[arg5];
				if (prefixNewLine) {
					sb.append("\r\n");
				}
				sb.append(';' + aComment);

				if (!prefixNewLine) {
					sb.append("\r\n");
				}
			}
		} else {
			if (prefixNewLine) {
				sb.append("\r\n");
			}
			sb.append(';' + comment);

			if (!prefixNewLine) {
				sb.append("\r\n");
			}
		}

		return sb.toString();
	}

	private String itemToString(IniItem item) {
		StringBuilder builder = new StringBuilder();

		String comment = item.getPreComment();

		if (!comment.equals("")) {
			builder.append(this.formatComment(comment, false));

		}

		if (this.includeSpaces) {
			builder.append(item.getName() + " = ");

		} else {
			builder.append(item.getName() + "=");
		}

		if (item.getValue() != null) {
			builder.append(item.getValue());

		}

		if (!item.getEndLineComment().equals("")) {
			builder.append(" ;" + item.getEndLineComment());

		}

		comment = item.getPostComment();

		if (!comment.equals("")) {
			builder.append(this.formatComment(comment, true));
			builder.append("\r\n");

		} else if (this.itemLineSeparator) {
			builder.append("\r\n");

		}

		return builder.toString();
	}

	private String sectionToString(IniSection section) {
		StringBuilder builder = new StringBuilder();

		if (this.sectionLineSeparator) {
			builder.append("\r\n");

		}

		String comment = section.getPreComment();

		if (!comment.equals("")) {
			builder.append(this.formatComment(comment, false));

		}

		builder.append("[" + section.getName() + "]");

		comment = section.getEndLineComment();

		if (!comment.equals("")) {
			builder.append(" ;" + comment);

		}

		comment = section.getPostComment();

		if (!comment.equals("")) {
			builder.append(this.formatComment(comment, true));
			builder.append("\r\n");

		} else if (this.sectionLineSeparator) {
			builder.append("\r\n");

		}

		int size = section.getNumberOfItems();

		for (int i = 0; i < size; ++i) {
			IniItem item = section.getItem(i);
			builder.append("\r\n");
			builder.append(this.itemToString(item));

		}

		return builder.toString();
	}

	public void setIncludeSpaces(boolean value) {
		this.includeSpaces = value;
	}

	public void setItemLineSeparator(boolean value) {
		this.itemLineSeparator = value;
	}

	public void setSectionLineSeparator(boolean value) {
		this.sectionLineSeparator = value;
	}

	public void write(String encoding) throws IOException {
		BufferedWriter bufferWriter = null;

		FileOutputStream fos = new FileOutputStream(this.file);
		OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
		bufferWriter = new BufferedWriter(osw);

		bufferWriter.write(this.iniToString(this.ini));
		bufferWriter.close();
	}
}
