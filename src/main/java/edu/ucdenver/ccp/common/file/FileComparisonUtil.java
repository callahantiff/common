/* Copyright (C) 2010 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This file is part of the CCP common library.
 * The CCP common library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package edu.ucdenver.ccp.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;

/**
 * A simple utility for comparing the contents of text files.
 * 
 * @author Bill Baumgartner
 * 
 */
public class FileComparisonUtil {
	/**
	 * This logger is used to log differences among the files being compared
	 */
	private static final Logger logger = Logger.getLogger(FileComparisonUtil.class);

	/**
	 * File comparisons can specify whether or not line ordering should be considered when comparing
	 * files.
	 * 
	 * @author bill
	 * 
	 */
	public enum LineOrder {
		/**
		 * Signifies that when comparing two files the lines can appear in any order
		 */
		ANY_ORDER,
		/**
		 * Signifies that when comparing two files the appearance of lines in one file must match
		 * the order of appearance in the other file exactly.
		 */
		AS_IN_FILE
	}

	/**
	 * File comparisons can specify whether or not column ordering should be considered when
	 * comparing files.
	 * 
	 * @author bill
	 * 
	 */
	public enum ColumnOrder {
		/**
		 * Signifies that columns can appear in any order when comparing two files
		 */
		ANY_ORDER,
		/**
		 * Signifies that columns must appear in the same order for files to match
		 */
		AS_IN_FILE
	}

	/**
	 * File comparisons can specify whether a line should be trimmed (leading and trailing
	 * whitespace removed) before comparison
	 * 
	 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
	 * 
	 */
	public enum LineTrim {
		/**
		 * 
		 */
		ON,
		OFF
	}

	public static boolean hasExpectedLines(File outputFile, CharacterEncoding encoding, List<String> expectedLines,
			String columnDelimiterRegex, LineOrder lineOrder, ColumnOrder columnOrder) throws IOException {
		return hasExpectedLines(outputFile, encoding, expectedLines, columnDelimiterRegex, lineOrder, columnOrder,
				LineTrim.OFF);
	}

	/**
	 * Returns true if the input list of lines matches those found in the input file, based on the
	 * LineOrder and ColumnOrder properties.
	 * 
	 * @param outputFile
	 * @param expectedLines
	 * @param columnDelimiterRegex
	 * @param lineOrder
	 * @param columnOrder
	 * @return
	 * @throws IOException
	 */
	public static boolean hasExpectedLines(File outputFile, CharacterEncoding encoding, List<String> expectedLines,
			String columnDelimiterRegex, LineOrder lineOrder, ColumnOrder columnOrder, LineTrim lineTrim)
			throws IOException {
		List<String> lines = FileReaderUtil.loadLinesFromFile(outputFile, encoding);
		List<String> remainingExpectedLines = new ArrayList<String>(expectedLines);
		if (lines.size() == 0 && expectedLines.size() > 0)
			logger.info("File contains no output.");

		List<String> trimmedExpectedLines = new ArrayList<String>(expectedLines);
		if (lineTrim.equals(LineTrim.ON)) {
			remainingExpectedLines = new ArrayList<String>();
			trimmedExpectedLines = new ArrayList<String>();
			for (String line : expectedLines) {
				trimmedExpectedLines.add(line.trim());
				remainingExpectedLines.add(line.trim());
			}
		}

		int lineIndex = 0;
		boolean allLinesAsExpected = true;
		for (String line : lines) {
			if (lineTrim.equals(LineTrim.ON))
				line = line.trim();
			if (isAnExpectedLine(line, trimmedExpectedLines, lineIndex, lineOrder, columnOrder, columnDelimiterRegex)) {
				remainingExpectedLines.remove(line);
			} else {
				logger.info(String.format("Line (%d) in file not in expected list: '%s'", lineIndex, line));
				allLinesAsExpected = false;
			}
			lineIndex++;
		}
		boolean hasExpectedLines = (lines.size() == expectedLines.size() && allLinesAsExpected);
		if (!hasExpectedLines) {
			logger.info("File does not contain expected lines. # lines in file: " + lines.size()
					+ " # expected lines: " + expectedLines.size() + " Expected lines matched those in file: "
					+ allLinesAsExpected);
			for (String line : remainingExpectedLines) {
				logger.info(String.format("EXPECTED LINE not in file: '%s'", line));
			}
		}
		return hasExpectedLines;
	}

	/**
	 * Returns true if the input line matches one of the expected lines based on the LineOrder and
	 * ColumnOrder properties.
	 * 
	 * @param line
	 * @param expectedLines
	 * @param lineIndex
	 * @param lineOrder
	 * @param columnOrder
	 * @param delimiterRegex
	 * @return
	 */
	private static boolean isAnExpectedLine(String line, List<String> expectedLines, int lineIndex,
			LineOrder lineOrder, ColumnOrder columnOrder, String delimiterRegex) {
		if (lineOrder == LineOrder.AS_IN_FILE) {
			if (expectedLines.size() > lineIndex)
				return linesAreEquivalent(line, expectedLines.get(lineIndex), columnOrder, delimiterRegex);
			return false;
		} else if (lineOrder == LineOrder.ANY_ORDER) {
			return lineInExpectedSet(line, expectedLines, columnOrder, delimiterRegex);
		} else
			throw new RuntimeException(String.format("Unknown LineOrder: %s", lineOrder.toString()));
	}

	/**
	 * Returns true if the input line is a member of the expected lines list based on the LineOrder
	 * and ColumnOrder properties.
	 * 
	 * @param line
	 * @param expectedLines
	 * @param columnOrder
	 * @param delimiterRegex
	 * @return
	 */
	private static boolean lineInExpectedSet(String line, List<String> expectedLines, ColumnOrder columnOrder,
			String delimiterRegex) {
		if (columnOrder == ColumnOrder.AS_IN_FILE) {
			if (expectedLines.contains(line)) {
				expectedLines.remove(line);
				return true;
			}
			return false;
		} else if (columnOrder == ColumnOrder.ANY_ORDER) {
			String[] lineToks = line.split(delimiterRegex, -1);
			for (String expectedLine : expectedLines) {
				String[] expectedLineToks = expectedLine.split(delimiterRegex, -1);
				if (lineToks.length == expectedLineToks.length) {
					if (CollectionsUtil.array2Set(lineToks).equals(CollectionsUtil.array2Set(expectedLineToks))) {
						expectedLines.remove(expectedLine);
						return true;
					}
				}
			}
			return false;
		} else
			throw new RuntimeException(String.format("Unknown ColumnOrder: %s", columnOrder.toString()));
	}

	/**
	 * Returns true if the input line is as expected, false otherwise. If columnOrder is ANY_ORDER
	 * then columns can be rearranged.
	 * 
	 * @param line
	 * @param expectedLine
	 * @param columnOrder
	 * @param delimiterRegex
	 * @return
	 */
	private static boolean linesAreEquivalent(String line, String expectedLine, ColumnOrder columnOrder,
			String delimiterRegex) {
		if (columnOrder == ColumnOrder.AS_IN_FILE) {
			return line.equals(expectedLine);
		} else if (columnOrder == ColumnOrder.ANY_ORDER) {
			String[] lineToks = line.split(delimiterRegex, -1);
			String[] expectedLineToks = expectedLine.split(delimiterRegex, -1);
			if (lineToks.length == expectedLineToks.length) {
				return CollectionsUtil.array2Set(lineToks).equals(CollectionsUtil.array2Set(expectedLineToks));
			}

			return false;
		} else
			throw new RuntimeException(String.format("Unknown ColumnOrder: %s", columnOrder.toString()));
	}

	/**
	 * Computes the MD5 CheckSum for the input file and writes it to a file in the same directory
	 * called [INPUT_FILE_NAME].md5
	 * 
	 * @param inputFile
	 * @return
	 */
	public static File createMd5ChecksumFile(File inputFile) {
		try {
			String md5sum = computeMd5Checksum(inputFile) + " " + inputFile.getName();
			File checkSumFile = getChecksumFile(inputFile);
			FileWriterUtil.printLines(CollectionsUtil.createList(md5sum), checkSumFile, CharacterEncoding.UTF_8,
					WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
			return checkSumFile;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Simply appends .md5 to the input file name
	 * 
	 * @param inputFile
	 * @return
	 */
	private static File getChecksumFile(File inputFile) {
		File checkSumFile = new File(inputFile.getAbsolutePath() + ".md5");
		return checkSumFile;
	}

	/**
	 * @param inputFile
	 * @param checkSumFile
	 * @return true if the MD5 checksum in the checkSumFile equals the MD5 checksum computed on the
	 *         inputFile, false otherwise
	 */
	public static boolean fileHasExpectedMd5Checksum(File inputFile, File checkSumFile) {
		try {
			String expectedChecksum = FileReaderUtil.loadLinesFromFile(checkSumFile, CharacterEncoding.UTF_8).get(0)
					.split("\\s+")[0];
			return expectedChecksum.equals(computeMd5Checksum(inputFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Assumes there is a file in the same directory as the input file called [INPUT_FILE_NAME].md5
	 * that contains the expected MD5 checksum for the input file.
	 * 
	 * @param inputFile
	 * @return true if the MD5 checksum in the checkSumFile equals the MD5 checksum computed on the
	 *         inputFile, false otherwise
	 */
	public static boolean fileHasExpectedMd5Checksum(File inputFile) {
		try {
			File checkSumFile = getChecksumFile(inputFile);
			String expectedChecksum = FileReaderUtil.loadLinesFromFile(checkSumFile, CharacterEncoding.UTF_8).get(0)
					.split("\\s+")[0];
			return expectedChecksum.equals(computeMd5Checksum(inputFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Computes the MD5 CheckSum for the input file
	 * 
	 * @param inputFile
	 * @return the MD5 CheckSum
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String computeMd5Checksum(File inputFile) throws FileNotFoundException, IOException {
		return DigestUtils.md5Hex(new FileInputStream(inputFile));
	}

}
