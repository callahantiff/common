/*
 Copyright (c) 2012, Regents of the University of Colorado
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
    list of conditions and the following disclaimer.
   
 * Redistributions in binary form must reproduce the above copyright notice, 
    this list of conditions and the following disclaimer in the documentation 
    and/or other materials provided with the distribution.
   
 * Neither the name of the University of Colorado nor the names of its 
    contributors may be used to endorse or promote products derived from this 
    software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.ucdenver.ccp.common.file.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.reader.Line.LineTerminator;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.test.DefaultTestCase;

public class StreamLineReaderTest extends DefaultTestCase {

	private File asciiOnlyFile;
	private File utf8File;
	private String asciiOnlyString = "This file contains only ASCII characters. 12345.";
	private String utf8String = "This file contains a UTF-8 character \u00df (beta).";

	@Before
	public void setUp() throws Exception {
		utf8File = folder.newFile("text.utf8");
		FileWriterUtil.printLines(CollectionsUtil.createList(utf8String), utf8File, CharacterEncoding.UTF_8);
		asciiOnlyFile = folder.newFile("text.ascii");
		FileWriterUtil.printLines(CollectionsUtil.createList(asciiOnlyString), asciiOnlyFile,
				CharacterEncoding.US_ASCII);

	}

	@Test
	public void testAsciiFileRead() throws Exception {
		StreamLineReader reader = new StreamLineReader(asciiOnlyFile, CharacterEncoding.US_ASCII);
		Line line = reader.readLine();
		assertEquals(String.format("Line should match expected ASCII-only line."), asciiOnlyString, line.getText());
	}

	@Test
	public void testUtf8FileRead() throws Exception {
		StreamLineReader reader = new StreamLineReader(utf8File, CharacterEncoding.UTF_8);
		Line line = reader.readLine();
		assertEquals(String.format("Line should match expected line containing utf-8 characters."), utf8String,
				line.getText());
	}

	/**
	 * Character encoding to use when creating/reading sample files in this test
	 */
	private static final CharacterEncoding ENCODING = CharacterEncoding.UTF_8;

	/**
	 * String to use to indicate a line-to-be-skipped in the sample files used in this test
	 */
	private static final String SKIP_LINE_PREFIX = StringConstants.POUND_SIGN;

	/**
	 * Tests that the FileLine line number gets set properly when some lines are skipped.
	 * 
	 * @throws IOException
	 *             if an error occurs reading or writing the sample file
	 */
	@Test
	public void testLineNumbersWhenSkippingLines() throws IOException {
		File sampleFile = populateSampleFile();
		StreamLineReader flr = new StreamLineReader(sampleFile, ENCODING, SKIP_LINE_PREFIX);
		Line line = flr.readLine();
		assertEquals("first line should have been skipped, so this line should be line number 1 (zero offset)", 1,
				line.getLineNumber());
		line = flr.readLine();
		assertEquals("third line should have been skipped, so this line should be line number 3 (zero offset)", 3,
				line.getLineNumber());
		line = flr.readLine();
		assertEquals("this line should be line number 4 (zero offset)", 4, line.getLineNumber());
		assertNull(flr.readLine());
	}

	/**
	 * Tests that the correct line terminators are returned
	 * 
	 * @throws IOException
	 */
	@Test
	public void testGetLineTerminator() throws IOException {
		File sampleFile = populateSampleFile();
		StreamLineReader flr = new StreamLineReader(sampleFile, ENCODING, SKIP_LINE_PREFIX);
		Line line = flr.readLine();
		assertEquals("Terminator on first line returned should be LF", LineTerminator.LF, line.getLineTerminator());
		line = flr.readLine();
		assertEquals("Terminator on second line returned should be CRLF", LineTerminator.CRLF, line.getLineTerminator());
		line = flr.readLine();
		assertEquals("Terminator on third line returned should be LF", LineTerminator.LF, line.getLineTerminator());
	}

	public void testByteOffset() throws IOException {
		File sampleFile = populateSampleFile();
		StreamLineReader flr = new StreamLineReader(sampleFile, ENCODING, SKIP_LINE_PREFIX);
		Line line = flr.readLine();
		assertEquals("Byte offset for first line returned should be eight", 8, line.getByteOffset());
		line = flr.readLine();
		assertEquals("Byte offset for second line returned should be 21", 21, line.getByteOffset());
		line = flr.readLine();
		assertEquals("Byte offset for second line returned should be 28", 28, line.getByteOffset());
	}

	/**
	 * @return a sample file containing 5 lines, lines 1 and 3 are commented out, line 4 contains
	 *         \u00df (beta)
	 * @throws IOException
	 *             if an error occurs while creating the sample file
	 */
	private File populateSampleFile() throws IOException {
		List<String> lines = CollectionsUtil.createList(SKIP_LINE_PREFIX + "line1", "line2", SKIP_LINE_PREFIX
				+ "line3\r", "line\u00df\r", "line5");
		File file = folder.newFile("sample.utf8");
		FileWriterUtil.printLines(lines, file, ENCODING);
		return file;
	}

}