package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.util.WikiTagNode;
import info.bliki.wiki.template.ITemplateFunction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A template parser for the first pass in the parsing of a Wikipedia text
 * 
 * @see WikipediaParser for the second pass
 */
public class TemplateParser extends AbstractParser {
	public final boolean fParseOnlySignature;

	private final boolean fRenderTemplate;

	private boolean fOnlyIncludeFlag;

	public TemplateParser(String stringSource) {
		this(stringSource, false, false);
	}

	public TemplateParser(String stringSource, boolean parseOnlySignature, boolean renderTemplate) {
		super(stringSource);
		fParseOnlySignature = parseOnlySignature;
		fRenderTemplate = renderTemplate;
		fOnlyIncludeFlag = false;
	}

	public static void parse(String rawWikitext, IWikiModel wikiModel, Appendable writer, boolean renderTemplate) throws IOException {
		parse(rawWikitext, wikiModel, writer, false, renderTemplate);
	}

	/**
	 * Parse the wiki texts templates, comments and signatures into the given
	 * <code>StringBuilder</code>.
	 * 
	 * @param rawWikitext
	 * @param wikiModel
	 * @param writer
	 * @param parseOnlySignature
	 *          change only the signature string and ignore templates and comments
	 *          parsing
	 * @param renderTemplate
	 */
	public static void parse(String rawWikitext, IWikiModel wikiModel, Appendable writer, boolean parseOnlySignature,
			boolean renderTemplate) throws IOException {
		parseRecursive(rawWikitext, wikiModel, writer, parseOnlySignature, renderTemplate, null);
	}

	// private static Pattern noinclude =
	// Pattern.compile("<noinclude[^>]*>.*?<\\\\/noinclude[^>]*>");
	//
	// private static Pattern INCLUDEONLY_PATTERN =
	// Pattern.compile("<includeonly[^>]*>(.*?)<\\/includeonly[^>]*>");

	protected static void parseRecursive(String rawWikitext, IWikiModel wikiModel, Appendable writer, boolean parseOnlySignature,
			boolean renderTemplate, HashMap<String, String> templateParameterMap) throws IOException {
		try {
			int level = wikiModel.incrementRecursionLevel();
			if (level > Configuration.PARSER_RECURSION_LIMIT) {
				writer.append("Error - recursion limit exceeded parsing templates.");
				return;
			}
			TemplateParser parser = new TemplateParser(rawWikitext, parseOnlySignature, renderTemplate);
			parser.setModel(wikiModel);
			StringBuilder sb = new StringBuilder(rawWikitext.length());
			parser.runPreprocessParser(sb);
			if (parseOnlySignature) {
				writer.append(sb);
				return;
			}

			StringBuilder parameterBuffer = sb;
			StringBuilder plainBuffer = sb;
			if (templateParameterMap != null && (!templateParameterMap.isEmpty())) {
				String preprocessedContent = parameterBuffer.toString();
				WikipediaScanner scanner = new WikipediaScanner(preprocessedContent);
				scanner.setModel(wikiModel);
				parameterBuffer = scanner.replaceTemplateParameters(preprocessedContent, templateParameterMap);
				if (parameterBuffer != null) {
					plainBuffer = parameterBuffer;
				}
			}
			parser = new TemplateParser(plainBuffer.toString(), parseOnlySignature, renderTemplate);
			parser.setModel(wikiModel);
			// parser.initialize(plainBuffer.toString());
			parser.runParser(writer);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			writer.append(e.getClass().getSimpleName());
		} catch (Error e) {
			e.printStackTrace();
			writer.append(e.getClass().getSimpleName());
		} finally {
			wikiModel.decrementRecursionLevel();
		}
	}

	/**
	 * Preprocess parsing of the <code>&lt;includeonly&gt;</code>,
	 * <code>&lt;onlyinclude&gt;</code> and <code>&lt;noinclude&gt;</code> tags
	 * 
	 * @param writer
	 * @throws IOException
	 */
	protected void runPreprocessParser(StringBuilder writer) throws IOException {
		fWhiteStart = true;
		fWhiteStartPosition = fCurrentPosition;
		try {
			while (true) {
				fCurrentCharacter = fSource[fCurrentPosition++];

				// ---------Identify the next token-------------
				switch (fCurrentCharacter) {
				case '<':
					int htmlStartPosition = fCurrentPosition;
					if (!fParseOnlySignature && parseIncludeWikiTags(writer)) {
						continue;
					}
					fCurrentPosition = htmlStartPosition;
					break;
				case '~':
					int tildeCounter = 0;
					if (fSource[fCurrentPosition] == '~' && fSource[fCurrentPosition + 1] == '~') {
						// parse signatures '~~~', '~~~~' or '~~~~~'
						tildeCounter = 3;
						try {
							if (fSource[fCurrentPosition + 2] == '~') {
								tildeCounter = 4;
								if (fSource[fCurrentPosition + 3] == '~') {
									tildeCounter = 5;
								}
							}
						} catch (IndexOutOfBoundsException e1) {
							// end of scanner text
						}
						appendContent(writer, fWhiteStart, fWhiteStartPosition, 1);
						fWikiModel.appendSignature(writer, tildeCounter);
						fCurrentPosition += (tildeCounter - 1);
						fWhiteStart = true;
						fWhiteStartPosition = fCurrentPosition;
					}
				}

				if (!fWhiteStart) {
					fWhiteStart = true;
					fWhiteStartPosition = fCurrentPosition - 1;
				}

			}
			// -----------------end switch while try--------------------
		} catch (IndexOutOfBoundsException e) {
			// end of scanner text
		}
		try {
			if (!fOnlyIncludeFlag) {
				appendContent(writer, fWhiteStart, fWhiteStartPosition, 1);
			}
		} catch (IndexOutOfBoundsException e) {
			// end of scanner text
		}
	}

	protected void runParser(Appendable writer) throws IOException {
		// int oldCurrentPosition = 0;
		fWhiteStart = true;
		fWhiteStartPosition = fCurrentPosition;
		try {
			while (true) {
				// if (oldCurrentPosition >= fCurrentPosition) {
				// System.out.println("stop stop: " + oldCurrentPosition + "--" +
				// fStringSource);
				// System.exit(-1);
				// }
				fCurrentCharacter = fSource[fCurrentPosition++];

				// oldCurrentPosition = fCurrentPosition;
				// ---------Identify the next token-------------
				switch (fCurrentCharacter) {
				case '{': // wikipedia template handling
					if (!fParseOnlySignature && parseTemplateOrTemplateParameter(writer)) {
						fWhiteStart = true;
						fWhiteStartPosition = fCurrentPosition;
						continue;
					}
					break;

				case '<':
					int htmlStartPosition = fCurrentPosition;
					if (!fParseOnlySignature && parseSpecialWikiTags(writer)) {
						continue;
					}
					fCurrentPosition = htmlStartPosition;
					break;
				case '~':
					int tildeCounter = 0;
					if (fSource[fCurrentPosition] == '~' && fSource[fCurrentPosition + 1] == '~') {
						// parse signatures '~~~', '~~~~' or '~~~~~'
						tildeCounter = 3;
						try {
							if (fSource[fCurrentPosition + 2] == '~') {
								tildeCounter = 4;
								if (fSource[fCurrentPosition + 3] == '~') {
									tildeCounter = 5;
								}
							}
						} catch (IndexOutOfBoundsException e1) {
							// end of scanner text
						}
						appendContent(writer, fWhiteStart, fWhiteStartPosition, 1);
						fWikiModel.appendSignature(writer, tildeCounter);
						fCurrentPosition += (tildeCounter - 1);
						fWhiteStart = true;
						fWhiteStartPosition = fCurrentPosition;
					}
				}

				if (!fWhiteStart) {
					fWhiteStart = true;
					fWhiteStartPosition = fCurrentPosition - 1;
				}

			}
			// -----------------end switch while try--------------------
		} catch (IndexOutOfBoundsException e) {
			// end of scanner text
		}
		try {
			appendContent(writer, fWhiteStart, fWhiteStartPosition, 1);
		} catch (IndexOutOfBoundsException e) {
			// end of scanner text
		}
	}

	/**
	 * See <a href=
	 * "http://en.wikipedia.org/wiki/Help:Template#Controlling_what_gets_transcluded"
	 * >Help:Template#Controlling what gets transcluded</a>
	 * 
	 * @param writer
	 * @return
	 * @throws IOException
	 */
	protected boolean parseIncludeWikiTags(StringBuilder writer) throws IOException {
		try {
			switch (fSource[fCurrentPosition]) {
			case '!': // <!-- html comment -->
				if (parseHTMLCommentTags(writer)) {
					return true;
				}
				break;
			default:

				if (fSource[fCurrentPosition] != '/') {
					// starting tag
					int lessThanStart = fCurrentPosition - 1;
					WikiTagNode tagNode = parseTag(fCurrentPosition);
					if (tagNode != null) {
						fCurrentPosition = tagNode.getEndPosition();
						int tagStart = fCurrentPosition;
						String tagName = tagNode.getTagName();
						if (tagName.equals("nowiki")) {
							if (readUntilIgnoreCase("</", "nowiki>")) {
								return true;
							}
						} else if (tagName.equals("source")) {
							if (readUntilIgnoreCase("</", "source>")) {
								return true;
							}
						} else if (tagName.equals("math")) {
							if (readUntilIgnoreCase("</", "math>")) {
								return true;
							}
						}
						if (!isTemplate()) {
							// not rendering a Template namespace directly
							if (tagName.equals("includeonly")) {
								if (readUntilIgnoreCase("</", "includeonly>")) {
									if (!fOnlyIncludeFlag) {
										appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
									}
									fWhiteStart = true;
									fWhiteStartPosition = tagStart;

									if (!fOnlyIncludeFlag) {
										appendContent(writer, fWhiteStart, fWhiteStartPosition, 2 + "includeonly>".length());
									}
									fWhiteStart = true;
									fWhiteStartPosition = fCurrentPosition;
									return true;
								}

								if (!fOnlyIncludeFlag) {
									appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
								}
								fWhiteStart = false;
								// fWhiteStartPosition = tagStart;
								fCurrentPosition = fStringSource.length();
								return true;

							} else if (tagName.equals("noinclude")) {
								if (readUntilIgnoreCase("</", "noinclude>")) {
									if (!fOnlyIncludeFlag) {
										appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
									}
									fWhiteStart = true;
									fWhiteStartPosition = fCurrentPosition;
									return true;
								}
								appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
								fWhiteStart = true;
								fWhiteStartPosition = tagStart;
								return true;
							} else if (tagName.equals("onlyinclude")) {
								if (readUntilIgnoreCase("</", "onlyinclude>")) {
									if (!fOnlyIncludeFlag) {
										// delete the content, which is already added
										writer.delete(0, writer.length());
										fOnlyIncludeFlag = true;
									}

									// appendContent(writer, fWhiteStart, fWhiteStartPosition,
									// fCurrentPosition - lessThanStart);
									fWhiteStart = true;
									fWhiteStartPosition = tagStart;

									appendContent(writer, fWhiteStart, fWhiteStartPosition, 2 + "onlyinclude>".length());
									fWhiteStart = true;
									fWhiteStartPosition = fCurrentPosition;
									return true;
								}
								appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
								fWhiteStart = true;
								fWhiteStartPosition = tagStart;
								return true;
							}
						} else {
							if (tagName.equals("noinclude")) {
								if (readUntilIgnoreCase("</", "noinclude>")) {
									appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
									fWhiteStart = true;
									fWhiteStartPosition = tagStart;

									appendContent(writer, fWhiteStart, fWhiteStartPosition, 2 + "noinclude>".length());
									fWhiteStart = true;
									fWhiteStartPosition = fCurrentPosition;
									return true;
								}
								appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
								fWhiteStart = true;
								fWhiteStartPosition = tagStart;
								return true;
							} else if (tagName.equals("includeonly")) {
								if (readUntilIgnoreCase("</", "includeonly>")) {
									appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
									fWhiteStart = true;
									fWhiteStartPosition = fCurrentPosition;
									return true;
								}

								appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
								fWhiteStart = false;
								// fWhiteStartPosition = tagStart;
								fCurrentPosition = fStringSource.length();
								return true;
							} else if (tagName.equals("onlyinclude")) {
								if (readUntilIgnoreCase("</", "onlyinclude>")) {
									appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
									fWhiteStart = true;
									fWhiteStartPosition = fCurrentPosition;
									return true;
								}
								appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
								fWhiteStart = true;
								fWhiteStartPosition = tagStart;
								return true;
							}
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// do nothing
		}
		return false;
	}

	protected boolean parseSpecialWikiTags(Appendable writer) throws IOException {
		try {
			switch (fSource[fCurrentPosition]) {
			case '!': // <!-- html comment -->
				if (parseHTMLCommentTags(writer)) {
					return true;
				}
				break;
			default:

				if (fSource[fCurrentPosition] != '/') {
					// starting tag
					WikiTagNode tagNode = parseTag(fCurrentPosition);
					if (tagNode != null) {
						fCurrentPosition = tagNode.getEndPosition();
						String tagName = tagNode.getTagName();
						if (tagName.equals("nowiki")) {
							if (readUntilIgnoreCase("</", "nowiki>")) {
								return true;
							}
						} else if (tagName.equals("source")) {
							if (readUntilIgnoreCase("</", "source>")) {
								return true;
							}
						} else if (tagName.equals("math")) {
							if (readUntilIgnoreCase("</", "math>")) {
								return true;
							}
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// do nothing
		}
		return false;
	}

	protected void appendContent(Appendable writer, boolean whiteStart, final int whiteStartPosition, final int diff)
			throws IOException {
		if (whiteStart) {
			try {
				final int whiteEndPosition = fCurrentPosition - diff;
				int count = whiteEndPosition - whiteStartPosition;
				if (count > 0) {
					writer.append(fStringSource, whiteStartPosition, whiteEndPosition); // count
					// )
					// ;
				}
			} finally {
				fWhiteStart = false;
			}
		}
	}

	private boolean parseTemplateOrTemplateParameter(Appendable writer) throws IOException {
		if (fSource[fCurrentPosition] == '{') {
			appendContent(writer, fWhiteStart, fWhiteStartPosition, 1);
			int startTemplatePosition = ++fCurrentPosition;
			if (fSource[fCurrentPosition] != '{') {
				int templateEndPosition = findNestedTemplateEnd(fSource, fCurrentPosition);
				if (templateEndPosition < 0) {
					fCurrentPosition--;
				} else {
					return parseTemplate(writer, startTemplatePosition, templateEndPosition);
				}
			} else {
				// parse template parameters
				int[] templateEndPosition = findNestedParamEnd(fSource, fCurrentPosition + 1);
				if (templateEndPosition[0] < 0) {
					if (templateEndPosition[1] < 0) {
						--fCurrentPosition;
					} else {
						writer.append('{');
						++fCurrentPosition;
						return parseTemplate(writer, startTemplatePosition + 1, templateEndPosition[1]);
					}
				} else {
					return parseTemplateParameter(writer, startTemplatePosition, templateEndPosition[0]);
				}
			}
		}
		return false;
	}

	/**
	 * Parse a single template {{...}}
	 * 
	 * @param writer
	 * @param startTemplatePosition
	 * @param templateEndPosition
	 * @return
	 * @throws IOException
	 */
	private boolean parseTemplate(Appendable writer, int startTemplatePosition, int templateEndPosition) throws IOException {
		fCurrentPosition = templateEndPosition;
		// insert template handling
		int endPosition = fCurrentPosition;
		String plainContent = null;
		int endOffset = fCurrentPosition - 2;
		String function = checkParserFunction(startTemplatePosition, endOffset);
		if (function != null) {
			// System.out.println("Function: " + function);
			// if (function.contains("[[Template")) {
			// System.out.println("Function: " + fStringSource);
			// }
			ITemplateFunction templateFunction = fWikiModel.getTemplateFunction(function.toLowerCase());
			if (templateFunction != null) {
				// if (function.charAt(0) == '#') {
				// #if:, #ifeq:,...
				plainContent = templateFunction.parseFunction(fSource, fCurrentPosition, endOffset, fWikiModel);
				fCurrentPosition = endPosition;
				if (plainContent != null) {
					TemplateParser.parseRecursive(plainContent, fWikiModel, writer, false, false, null);
					return true;
				}
				return true;
			}
			fCurrentPosition = endOffset + 2;
		}
		Object[] objs = createParameterMap(fSource, startTemplatePosition, fCurrentPosition - startTemplatePosition - 2);
		HashMap<String, String> map = (HashMap<String, String>) objs[0];
		String templateName = ((String) objs[1]).trim();
		// System.out.println("Template name: " + fCurrentPosition + "--" +
		// templateName);
		if (templateName.length() > 0 && templateName.charAt(0) == ':') {
			plainContent = fWikiModel.getRawWikiContent("", templateName.substring(1), map);
		} else {
			fWikiModel.addTemplate(templateName);
			plainContent = fWikiModel.getRawWikiContent(fWikiModel.getTemplateNamespace(), templateName, map);
		}

		fCurrentPosition = endPosition;
		if (plainContent != null) {
			TemplateParser.parseRecursive(plainContent.trim(), fWikiModel, writer, false, false, map);
			return true;
		}
		// if no template found insert plain template name string:
		// writer.append("[[" + fWikiModel.getTemplateNamespace() + ":" +
		// templateName + "]]");
		writer.append("{{" + templateName + "}}");
		return true;
	}

	/**
	 * Parse a single template parameter {{{...}}}
	 * 
	 * @param writer
	 * @param startTemplatePosition
	 * @param templateEndPosition
	 * @return
	 * @throws IOException
	 */
	private boolean parseTemplateParameter(Appendable writer, int startTemplatePosition, int templateEndPosition) throws IOException {
		String plainContent = fStringSource.substring(startTemplatePosition - 2, templateEndPosition);

		if (plainContent != null) {
			fCurrentPosition = templateEndPosition;
			WikipediaScanner scanner = new WikipediaScanner(plainContent);
			scanner.setModel(fWikiModel);
			StringBuilder plainBuffer = scanner.replaceTemplateParameters(plainContent, null);
			if (plainBuffer == null) {
				writer.append(plainContent);
				return true;
			}
			TemplateParser.parseRecursive(plainBuffer.toString().trim(), fWikiModel, writer, false, false, null);
			return true;
		}
		return false;
	}

	/**
	 * Create a map from the parameters defined in a template call
	 * 
	 * @return the templates parameter map at index [0] and the template name at
	 *         index [1]
	 * 
	 */
	private static Object[] createParameterMap(char[] src, int startOffset, int len) {
		Object[] objs = new Object[2];
		HashMap<String, String> map = new HashMap<String, String>();
		objs[0] = map;
		int currOffset = startOffset;
		int endOffset = startOffset + len;
		List<String> resultList = new ArrayList<String>();
		resultList = splitByPipe(src, currOffset, endOffset, resultList);
		if (resultList.size() <= 1) {
			// set the templates name
			objs[1] = new String(src, startOffset, len);
			return objs;
		}
		objs[1] = resultList.get(0);

		for (int i = 1; i < resultList.size(); i++) {
			if (i == resultList.size() - 1) {
				createSingleParameter(i, resultList.get(i), map, true);
			} else {
				createSingleParameter(i, resultList.get(i), map, false);
			}
		}

		return objs;
	}

	/**
	 * Create a single parameter defined in a template call and add it to the
	 * parameters map
	 * 
	 */
	private static void createSingleParameter(int parameterCounter, String srcString, HashMap<String, String> map,
			boolean trimNewlineRight) {
		int currOffset = 0;
		char[] src = srcString.toCharArray();
		int endOffset = srcString.length();
		char ch;
		String parameter = null;
		String value;
		boolean equalCharParsed = false;

		int lastOffset = currOffset;
		int[] temp = new int[] { -1, -1 };
		try {
			while (currOffset < endOffset) {
				ch = src[currOffset++];
				if (ch == '[' && src[currOffset] == '[') {
					currOffset++;
					temp[0] = findNestedEnd(src, '[', ']', currOffset);
					if (temp[0] >= 0) {
						currOffset = temp[0];
					}
				} else if (ch == '{' && src[currOffset] == '{') {
					currOffset++;
					if (src[currOffset] == '{') {
						currOffset++;
						temp = findNestedParamEnd(src, currOffset);
						if (temp[0] >= 0) {
							currOffset = temp[0];
						} else {
							currOffset--;
							temp[0] = findNestedTemplateEnd(src, currOffset);
							if (temp[0] >= 0) {
								currOffset = temp[0];
							}
						}
					} else {
						temp[0] = findNestedTemplateEnd(src, currOffset);
						if (temp[0] >= 0) {
							currOffset = temp[0];
						}
					}
				} else if (ch == '=') {
					if (!equalCharParsed) {
						parameter = srcString.substring(lastOffset, currOffset - 1).trim();
						lastOffset = currOffset;
					}
					equalCharParsed = true;
				}
			}

		} catch (IndexOutOfBoundsException e) {

		} finally {
			if (currOffset > lastOffset) {
				if (trimNewlineRight) {
					value = Utils.trimNewlineRight(srcString.substring(lastOffset, currOffset));
				} else {
					value = srcString.substring(lastOffset, currOffset).trim();
				}
				map.put(Integer.toString(parameterCounter), value);
				if (parameter != null) {
					map.put(parameter, value);
				}
			}
		}
	}

	/**
	 * Check if this template contains a template function
	 * 
	 * Note: repositions this#fCurrentPosition behind the parser function string
	 * if possible
	 * 
	 * @param startOffset
	 * @param endOffset
	 * @return the parser function name (without the # character) or
	 *         <code>null</code> if no parser function can be found in this
	 *         template
	 */
	private String checkParserFunction(int startOffset, int endOffset) {
		// String function = null;
		int currOffset = startOffset;
		int functionStart = startOffset;
		char ch;
		while (currOffset < endOffset) {
			ch = fSource[currOffset++];
			if (Character.isLetter(ch) || ch == '#' || ch == '$') {
				functionStart = currOffset - 1;
				while (currOffset < endOffset) {
					ch = fSource[currOffset++];
					if (ch == ':') {
						fCurrentPosition = currOffset;
						return fStringSource.substring(functionStart, currOffset - 1);
					} else if (!Character.isLetterOrDigit(ch) && ch != '$') {
						return null;
					}
				}
				break;
			}
		}
		return null;
	}

	protected boolean parseHTMLCommentTags(Appendable writer) throws IOException {
		int temp = readWhitespaceUntilStartOfLine(2);
		String htmlCommentString = fStringSource.substring(fCurrentPosition - 1, fCurrentPosition + 3);
		if (htmlCommentString.equals("<!--")) {
			if (temp >= 0) {
				if (!fOnlyIncludeFlag) {
					appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - temp - 1);
				}
			} else {
				if (!fOnlyIncludeFlag) {
					appendContent(writer, fWhiteStart, fWhiteStartPosition, 1);
				}
			}
			fCurrentPosition += 3;
			if (readUntil("-->")) {
				if (temp >= 0) {
					temp = readWhitespaceUntilEndOfLine(0);
					if (temp >= 0) {
						fCurrentPosition++;
					}
				}
				fWhiteStart = true;
				fWhiteStartPosition = fCurrentPosition;
				return true;
			}
		}
		return false;
	}

	@Override
	public void runParser() {
		// do nothing here
	}

	@Override
	public void setNoToC(boolean noToC) {
		// do nothing here
	}

	public boolean isTemplate() {
		return fRenderTemplate;
	}

}
