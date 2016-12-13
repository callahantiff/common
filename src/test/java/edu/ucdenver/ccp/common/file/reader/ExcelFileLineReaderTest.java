package edu.ucdenver.ccp.common.file.reader;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2016 Regents of the University of Colorado
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import edu.ucdenver.ccp.common.io.ClassPathUtil;

public class ExcelFileLineReaderTest {

	@Test
	public void testReadXlsxFile() throws IOException {
		InputStream xlsxStream = ClassPathUtil.getResourceStreamFromClasspath(getClass(), "sample.xlsx");
		ExcelFileLineReader lineReader = new ExcelFileLineReader(xlsxStream, null);
		Line line = lineReader.readLine();
		assertEquals("a\tb\tc\td", line.getText());
		line = lineReader.readLine();
		assertEquals("a\t\tc\td", line.getText());
		line = lineReader.readLine();
		assertEquals("\tb\tc\td", line.getText());
		line = lineReader.readLine();
		assertEquals("\t\tc\t", line.getText());
		lineReader.close();
	}

	@Test
	public void testReadXlsFile() throws IOException {
		InputStream xlsxStream = ClassPathUtil.getResourceStreamFromClasspath(getClass(), "sample.xls");
		ExcelFileLineReader lineReader = new ExcelFileLineReader(xlsxStream, null);
		Line line = lineReader.readLine();
		assertEquals("a\tb\tc\td", line.getText());
		line = lineReader.readLine();
		assertEquals("a\t\tc\td", line.getText());
		line = lineReader.readLine();
		assertEquals("\tb\tc\td", line.getText());
		line = lineReader.readLine();
		assertEquals("\t\tc\t", line.getText());
		lineReader.close();
	}
	
}
