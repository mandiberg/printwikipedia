package info.bliki.wiki.tags.code;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Syntax highlighting support for JavaScript source codes
 *
 */
public class JavaScriptCodeFilter extends AbstractCPPBasedCodeFilter implements SourceCodeFormatter {

	private static HashMap<String, String> KEYWORD_SET = new HashMap<String, String>();

	private static final String[] KEYWORDS = { "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
			"continue", "debugger", "default", "delete", "do", "double", "else", "enum", "export", "extends", "false", "final",
			"finally", "float", "for", "function", "goto", "if", "implements", "import", "in", "instanceof", "int", "interface", "long",
			"native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "super", "switch",
			"synchronized", "this", "throw", "throws", "transient", "true", "try", "typeof", "var", "void", "volatile", "while", "with" };

	private static HashSet<String> OBJECT_SET = new HashSet<String>();

	static {
		for (int i = 0; i < KEYWORDS.length; i++) {
			createHashMap(KEYWORD_SET, KEYWORDS[i]);
		}
	}

	public JavaScriptCodeFilter() {
	}

	@Override
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
				} else if (currentChar == '\'') { // strings
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
	@Override
	public HashMap<String, String> getKeywordSet() {
		return KEYWORD_SET;
	}

	/**
	 * @return Returns the OBJECT_SET.
	 */
	@Override
	public HashSet<String> getObjectSet() {
		return OBJECT_SET;
	}

}