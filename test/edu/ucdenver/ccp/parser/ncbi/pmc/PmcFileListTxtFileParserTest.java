package edu.ucdenver.ccp.parser.ncbi.pmc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.ucdenver.ccp.util.collections.CollectionsUtil;
import edu.ucdenver.ccp.util.test.DefaultTestCase;
import edu.ucdenver.ccp.util.test.TestUtil;

public class PmcFileListTxtFileParserTest extends DefaultTestCase {

	private File pmcFileListTxtFile;
	private File pmcFileListTxtFile_WithInvalidHeader;
	private File pmcFileListTxtFile_WithInvalidRow;

	@Before
	public void setUp() throws Exception {
		populateSamplePmcFileListTxtFile();
		populateSamplePmcFileListTxtFile_InvalidHeader();
		populateSamplePmcFileListTxtFile_InvalidRow();
	}

	private void populateSamplePmcFileListTxtFile() throws IOException {
		List<String> lines = CollectionsUtil
				.createList(
						"2010-04-01 21:40:15",
						"d1/c3/Nucleic_Acids_Res-10-12-320743.tar.gz\tNucleic Acids Res. 1982 Jun 25; 10(12):3681-3691\tPMC320743",
						"15/9d/Bioinorg_Chem_Appl-1-2-2267055.tar.gz\tBioinorg Chem Appl. 2003; 1(2):123-139\tPMC2267055");
		pmcFileListTxtFile = TestUtil.populateTestFile(folder, "file_list.txt", lines);
	}

	/**
	 * The header is not as expected
	 * 
	 * @throws IOException
	 */
	private void populateSamplePmcFileListTxtFile_InvalidHeader() throws IOException {
		List<String> lines = CollectionsUtil.createList("This is an invalid header.");
		pmcFileListTxtFile_WithInvalidHeader = TestUtil.populateTestFile(folder, "file_list.txt.invalid_header", lines);
	}

	/**
	 * One row contains an extra column
	 * 
	 * @throws IOException
	 */
	private void populateSamplePmcFileListTxtFile_InvalidRow() throws IOException {
		List<String> lines = CollectionsUtil
				.createList(
						"2010-04-01 21:40:15",
						"d1/c3/Nucleic_Acids_Res-10-12-320743.tar.gz\tNucleic Acids Res. 1982 Jun 25; 10(12):3681-3691\tPMC320743\textra column");
		pmcFileListTxtFile_WithInvalidRow = TestUtil.populateTestFile(folder, "file_list.txt_invalid_row", lines);
	}

	private PmcFileListTxtFileData getExpectedRecord1() {
		return new PmcFileListTxtFileData("d1/c3/Nucleic_Acids_Res-10-12-320743.tar.gz",
				"Nucleic Acids Res. 1982 Jun 25; 10(12):3681-3691", "PMC320743");
	}

	private PmcFileListTxtFileData getExpectedRecord2() {
		return new PmcFileListTxtFileData("15/9d/Bioinorg_Chem_Appl-1-2-2267055.tar.gz",
				"Bioinorg Chem Appl. 2003; 1(2):123-139", "PMC2267055");
	}

	@Test
	public void testParse() throws Exception {
		Iterator<PmcFileListTxtFileData> pmcFileListTxtIter = new PmcFileListTxtFileParser(pmcFileListTxtFile);
		assertTrue(String.format("Should be 1st of two pmc id records."), pmcFileListTxtIter.hasNext());
		checkPmcFileListTxtFileData(getExpectedRecord1(), pmcFileListTxtIter.next());
		assertTrue(String.format("Should be 2nd of two pmc id records."), pmcFileListTxtIter.hasNext());
		checkPmcFileListTxtFileData(getExpectedRecord2(), pmcFileListTxtIter.next());
		assertFalse(pmcFileListTxtIter.hasNext());
	}

	@Test
	public void testDataRecordGetFilePathAndFileNameMethods() throws Exception {
		Iterator<PmcFileListTxtFileData> pmcFileListTxtIter = new PmcFileListTxtFileParser(pmcFileListTxtFile);
		PmcFileListTxtFileData dataRecord = pmcFileListTxtIter.next();
		assertEquals(String.format("Should return just the directory path to the file (not the file name)."), "d1/c3",
				dataRecord.getFtpDirectory());
		assertEquals(String.format("Should return just the name of the file (not the directory path)."),
				"Nucleic_Acids_Res-10-12-320743.tar.gz", dataRecord.getFtpFileName());
	}

	private void checkPmcFileListTxtFileData(PmcFileListTxtFileData expected, PmcFileListTxtFileData observed)
			throws Exception {
		TestUtil.conductBeanComparison(expected, observed);
	}

	@Test(expected = IllegalStateException.class)
	public void testParse_WithInvalidHeader() throws Exception {
		new PmcFileListTxtFileParser(pmcFileListTxtFile_WithInvalidHeader);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParse_WithInvalidRow() throws Exception {
		Iterator<PmcFileListTxtFileData> pmcFileListTxtIter = new PmcFileListTxtFileParser(
				pmcFileListTxtFile_WithInvalidRow);
		pmcFileListTxtIter.hasNext();
	}

	@Test
	public void testGetPmcID2FilenameMap() throws Exception {
		Map<String, String> pmcid2FilenameMap = PmcFileListTxtFileParser.getPmcId2FilenameMap(pmcFileListTxtFile);
		Map<String, String> expectedPmcid2FilenameMap = CollectionsUtil.createMap("PMC320743",
				"Nucleic_Acids_Res-10-12-320743.tar.gz", "PMC2267055", "Bioinorg_Chem_Appl-1-2-2267055.tar.gz");
		assertEquals(String.format("Should contain two mappings from pmcid to filename."), expectedPmcid2FilenameMap,
				pmcid2FilenameMap);
	}

}
