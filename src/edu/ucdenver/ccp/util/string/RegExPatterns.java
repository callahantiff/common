package edu.ucdenver.ccp.util.string;

public class RegExPatterns {

	/**
	 * Matches a string that contains only digits, but must have at least one digit
	 */
	public static final String HAS_NUMBERS_ONLY = "^\\d+$";

	/**
	 * Matches a string that contains only digits, but may have an optional negative sign at the
	 * beginning
	 */
	public static final String HAS_NUMBERS_ONLY_OPT_NEG = "^-?\\d+$";

	/**
	 * Matches a string that contains only digits with the first digit being a zero, and may have an
	 * optional negative sign at the beginning
	 */
	public static final String HAS_NUMBERS_ONLY_OPT_NEG_ZERO_START = "^-?0\\d*$";

	/**
	 * Matches a tab character.
	 */
	public static final String TAB = "\\t";

	private RegExPatterns() {
		// this class should not be instantiated
	}
}