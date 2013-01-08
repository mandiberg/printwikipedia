package info.bliki.wiki.tags.code;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Syntax highlighting support for C++ based source codes
 * 
 */
abstract public class AbstractCPPBasedCodeFilter implements SourceCodeFormatter {
	public final static String FONT_KEYWORD = "<span style=\"color:#7F0055; font-weight: bold; \">";
	// "<font color=\"#7F0055\">";

	public final static String FONT_COMMENT = "<span style=\"color:#3F7F5F; \">";
	// = "<font color=\"#3F7F5F\">";

	public final static String FONT_STRINGS = "<span style=\"color:#2A00FF; \">";
	// = "<font color=\"#2A00FF\">";

	public final static String FONT_JAVADOC = "<span style=\"color:#3F5FBF; \">";
	// = "<font color=\"#3F5FBF\">";

	public final static String FONT_END = "</span>";

	public static void appendChar(StringBuilder result, char currentChar) {
		switch (currentChar) {
		case '\"': // special html escape character
			result.append("&#34;");
			break;
		case '<': // special html escape character
			result.append("&#60;");
			break;
		case '>': // special html escape character
			result.append("&#62;");
			break;
		case '&': // special html escape character
			result.append("&#38;");
			break;
		case '\'': // special html escape character
			result.append("&#39;");
			break;
		default:
			result.append(currentChar);
		}
	}

	public static void createHashMap(HashMap<String, String> map, String str) {
		map.put(str, FONT_KEYWORD + str + FONT_END);
	}

	public AbstractCPPBasedCodeFilter() {
	}

	protected int appendIdentifier(String input, int identStart, int currentPosition, HashMap<String, String> keywords,
			HashSet<String> objectWords, StringBuilder result) {
		String originalIdent = input.substring(identStart, --currentPosition);
		String keywordIdent = originalIdent;
		if (!isKeywordLowerCase()) {
			keywordIdent = keywordIdent.toLowerCase();
		}
		String keywordValue = (String) keywords.get(keywordIdent);
		if (keywordValue != null) {
			result.append(keywordValue);
		} else {
			result.append(originalIdent);
		}
		return currentPosition;
	}

	public String filter(String input) {
		char[] source = input.toCharArray();
		int currentPosition = 0;
		int identStart = 0;
		char currentChar = ' ';

		HashMap<String, String> keywordsSet = getKeywordSet();
		HashSet<String> objectsSet = getObjectSet();
		StringBuilder result = new StringBuilder(input.length() + input.length() / 4);
		boolean identFound = false;
		// result.append("<font color=\"#000000\">");
		try {
			while (true) {
				currentChar = source[currentPosition++];
				if ((currentChar >= 'A' && currentChar <= 'Z') || (currentChar == '_') || (currentChar >= 'a' && currentChar <= 'z')) {
					identStart = currentPosition - 1;
					identFound = true;
					// start of identifier ?
					while ((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || currentChar == '_') {
						currentChar = source[currentPosition++];
					}
					currentPosition = appendIdentifier(input, identStart, currentPosition, keywordsSet, objectsSet, result);
					identFound = false;
					continue; // while loop
				} else if (currentChar == '\"') { // strings
					result.append(FONT_STRINGS);
					appendChar(result, currentChar);
					while (currentPosition < input.length()) {
						currentChar = source[currentPosition++];
						appendChar(result, currentChar);
						if (currentChar == '\\') {
							currentChar = source[currentPosition++];
							appendChar(result, currentChar);
							continue;
						}
						if (currentChar == '\"') {
							break;
						}
					}
					result.append(FONT_END);
					continue;
				} else if (currentChar == '\'') { // characters
					result.append(FONT_STRINGS);
					appendChar(result, currentChar);
					while (currentPosition < input.length()) {
						currentChar = source[currentPosition++];
						appendChar(result, currentChar);
						if (currentChar == '\\') {
							currentChar = source[currentPosition++];
							appendChar(result, currentChar);
							continue;
						}
						if (currentChar == '\'') {
							break;
						}
					}
					result.append(FONT_END);
					continue;
				} else if (currentChar == '/' && currentPosition < input.length() && source[currentPosition] == '/') {
					// line comment
					result.append(FONT_COMMENT);
					appendChar(result, currentChar);
					appendChar(result, source[currentPosition++]);
					while (currentPosition < input.length()) {
						currentChar = source[currentPosition++];
						appendChar(result, currentChar);
						if (currentChar == '\n') {
							break;
						}
					}
					result.append(FONT_END);
					continue;
				} else if (currentChar == '/' && currentPosition < input.length() && source[currentPosition] == '*') {
					if (currentPosition < (input.length() - 1) && source[currentPosition + 1] == '*') {
						// javadoc style
						result.append(FONT_JAVADOC);
					} else {
						// multiline comment
						result.append(FONT_COMMENT);
					}
					appendChar(result, currentChar);
					appendChar(result, source[currentPosition++]);
					while (currentPosition < input.length()) {
						currentChar = source[currentPosition++];
						appendChar(result, currentChar);
						if (currentChar == '/' && source[currentPosition - 2] == '*') {
							break;
						}
					}
					result.append(FONT_END);
					continue;
				} else if (currentChar == '<' && isPHPTag() && currentPosition + 3 < input.length() && source[currentPosition] == '?'
						&& source[currentPosition + 1] == 'p' && source[currentPosition + 2] == 'h' && source[currentPosition + 3] == 'p') {
					// php start tag
					currentPosition += 4;
					result.append(FONT_KEYWORD + "&#60;?php" + FONT_END);
					continue;
				} else if (currentChar == '?' && isPHPTag() && currentPosition < input.length() && source[currentPosition] == '>') {
					// php start tag
					currentPosition++;
					result.append(FONT_KEYWORD + "?&#62;" + FONT_END);
					continue;
				}
				appendChar(result, currentChar);

			}
		} catch (IndexOutOfBoundsException e) {
			if (identFound) {
				currentPosition = appendIdentifier(input, identStart, currentPosition, keywordsSet, null, result);
			}
		}
		// result.append(FONT_END);
		return result.toString();
	}

	/**
	 * @return Returns the KEYWORD_SET.
	 */
	abstract public HashMap<String, String> getKeywordSet();

	/**
	 * @return Returns the OBJECT_SET.
	 */
	abstract public HashSet<String> getObjectSet();

	/**
	 * @return Returns the KEYWORD_MAP.
	 */
	public boolean isKeywordLowerCase() {
		return true;
	}

	/**
	 * 
	 */
	public boolean isPHPTag() {
		return false;
	}
}