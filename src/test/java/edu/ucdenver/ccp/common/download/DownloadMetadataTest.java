package edu.ucdenver.ccp.common.download;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2017 Regents of the University of Colorado
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Properties;

import org.junit.Test;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileReaderUtil;
import edu.ucdenver.ccp.common.test.DefaultTestCase;

public class DownloadMetadataTest extends DefaultTestCase {

	@Test
	public void testPropertiesFileRoundTrip() throws ParseException, IOException, URISyntaxException {

		Calendar downloadDate = Calendar.getInstance();
		downloadDate.setTime(DownloadMetadata.DATE_FORMATTER.parse("12/29/2015"));

		Calendar lastModDate = Calendar.getInstance();
		lastModDate.setTime(DownloadMetadata.DATE_FORMATTER.parse("12/01/2015"));

		File downloadedFile = new File("/tmp/file.txt");

		long fileSizeInBytes = 12345;

		URL downloadUrl = new URL("ftp://ftp.some.server/path/file.txt");

		DownloadMetadata metadata = new DownloadMetadata(downloadDate, downloadedFile, fileSizeInBytes, lastModDate,
				downloadUrl);

		File propertiesFile = folder.newFile("dload.properties");

		metadata.writePropertiesFile(propertiesFile);

		assertTrue(propertiesFile.exists());

		DownloadMetadata roundTripMetadata = DownloadMetadata.loadFromPropertiesFile(propertiesFile);

		assertEquals(metadata, roundTripMetadata);

		/* check the validity of the file_age_in_days calculation */
		assertEquals(28, metadata.getFileAgeInDays());

	}

}
