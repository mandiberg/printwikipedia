package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.util.TagStack;

public abstract class AbstractParser extends WikipediaScanner {
	/**
	 * The current scanned character
	 */
	protected char fCurrentCharacter;

	/**
	 * The current offset in the character source array
	 */
	protected int fCurrentPosition;

	protected boolean fWhiteStart = false;

	protected int fWhiteStartPosition = 0;

	public AbstractParser(String stringSource) {
		super(stringSource);
		fCurrentPosition = 0;
		fCurrentCharacter = '\000';
		fWhiteStart = false;
		fWhiteStartPosition = 0;
	}

	// public void initialize(String src) {
	// super.initialize(src, 0);
	// fCurrentPosition = 0;
	// fCurrentCharacter = '\000';
	// fWhiteStart = false;
	// fWhiteStartPosition = 0;
	// }

	/**
	 * Read the characters until the given string is found
	 * 
	 * @param untilString
	 * @return
	 */
	protected final boolean readUntil(String untilString) {
		int index = fStringSource.indexOf(untilString, fCurrentPosition);
		if (index != (-1)) {
			fCurrentPosition = index + untilString.length();
			return true;
		}
		return false;
	}

	/**
	 * Read the characters until the concatenated <i>start</i> and <i>end</i>
	 * substring is found. The end substring is matched ignoring case
	 * considerations.
	 * 
	 * @param startString
	 *          the start string which should be searched in exact case mode
	 * @param endString
	 *          the end string which should be searched in ignore case mode
	 * @return
	 */
	protected final boolean readUntilIgnoreCase(String startString, String endString) {
		int index = Util.indexOfIgnoreCase(fStringSource, startString, endString, fCurrentPosition);
		if (index != (-1)) {
			fCurrentPosition = index + startString.length() + endString.length();
			return true;
		}
		return false;
	}

	/**
	 * Read until character is found
	 * 
	 * @param testedChar
	 *          search the next position of this char
	 * @return <code>true</code> if the tested character can be found
	 */
	protected final boolean readUntilChar(char testedChar) {
		int temp = fCurrentPosition;
		try {
			while ((fCurrentCharacter = fSource[fCurrentPosition++]) != testedChar) {
			}
			return true;
		} catch (IndexOutOfBoundsException e) {
			fCurrentPosition = temp;
			return false;
		}
	}

	/**
	 * Read until character is found or stop at end-of-line
	 * 
	 * @param testedChar
	 *          search the next position of this char
	 * @return <code>true</code> if the tested character can be found
	 */
	protected final boolean readUntilCharOrStopAtEOL(char testedChar) {
		int temp = fCurrentPosition;
		try {
			while ((fCurrentCharacter = fSource[fCurrentPosition++]) != testedChar) {
				if (fCurrentCharacter == '\n' || fCurrentCharacter == '\r') {
					return false;
				}
			}
			return true;
		} catch (IndexOutOfBoundsException e) {
			fCurrentPosition = temp;
			return false;
		}
	}

	/**
	 * Read until the end-of-line characters (i.e. '\r' or '\n') or the end of the
	 * string is reached
	 * 
	 * @param fName
	 * @return <code>true</code> if the end-of-line characters or the end of the
	 *         string is reached
	 * 
	 */
	protected final boolean readUntilEOL() {
		try {
			while (true) {
				fCurrentCharacter = fSource[fCurrentPosition++];
				if (fCurrentCharacter == '\n' || fCurrentCharacter == '\r') {
					return true;
				}
				if (fCurrentCharacter == '<') {
					int newPos = readSpecialWikiTags(fCurrentPosition);
					if (newPos >= 0) {
						fCurrentPosition = newPos;
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			--fCurrentPosition;
			return true;
		}
	}

	// protected boolean readSpecialWikiTags() {
	// try {
	// if (fSource[fCurrentPosition] != '/') {
	// // starting tag
	// WikiTagNode tagNode = parseTag(fCurrentPosition);
	// if (tagNode != null) {
	// String tagName = tagNode.getTagName();
	// if (tagName.equals("nowiki")) {
	// if (readUntilIgnoreCase("</", "nowiki>")) {
	// return true;
	// }
	// } else if (tagName.equals("source")) {
	// if (readUntilIgnoreCase("</", "source>")) {
	// return true;
	// }
	// } else if (tagName.equals("math")) {
	// if (readUntilIgnoreCase("</", "math>")) {
	// return true;
	// }
	// } else if (tagName.equals("span")) {
	// if (readUntilIgnoreCase("</", "span>")) {
	// return true;
	// }
	// } else if (tagName.equals("div")) {
	// if (readUntilIgnoreCase("</", "div>")) {
	// return true;
	// }
	// }
	// }
	// }
	// } catch (IndexOutOfBoundsException e) {
	// // do nothing
	// }
	// return false;
	// }

	protected boolean isEmptyLine(int diff) {
		int temp = fCurrentPosition - diff;
		char ch;
		try {
			while (true) {
				ch = fSource[temp];
				if (!Character.isWhitespace(ch)) {
					return false;
				}
				if (ch == '\n') {
					return true;
				}
				temp++;
			}
		} catch (IndexOutOfBoundsException e) {
			// ..
		}
		return true;
	}

	protected int readWhitespaceUntilEndOfLine(int diff) {
		int temp = fCurrentPosition - diff;
		char ch;
		while (fSource.length > temp) {
			ch = fSource[temp];
			if (!Character.isWhitespace(ch)) {
				return -1;
			}
			if (ch == '\n') {
				fCurrentPosition = temp;
				return temp;
			}
			temp++;
		}
		fCurrentPosition = temp - 1;
		return temp;
	}

	protected int readWhitespaceUntilStartOfLine(int diff) {
		int temp = fCurrentPosition - diff;
		char ch;

		while (temp >= 0) {
			ch = fSource[temp];
			if (!Character.isWhitespace(ch)) {
				return -1;
			}
			if (ch == '\n') {
				return temp;
			}
			temp--;
		}

		return temp;
	}

	protected boolean parsePHPBBCode(String name, StringBuilder bbCode) {
		int index = 1;
		char ch = ' ';

		while (index < name.length()) {
			ch = name.charAt(index++);
			if ('a' <= ch && ch <= 'z') {
				bbCode.append(ch);
			} else {
				break;
			}
		}
		if (ch != '=' && index != name.length()) {
			// no bbcode
			return false;
		}
		String bbStr = bbCode.toString();
		// String bbEndStr = "[/" + bbStr + "]";
		String bbEndStr = bbStr + "]";
		int startPos = fCurrentPosition;

		if (!readUntilIgnoreCase("[/", bbEndStr)) {
			return false;
		}
		String bbAttr = null;
		if (ch == '=') {
			bbAttr = name.substring(index, name.length());
			if (bbAttr != null) {
				bbAttr = bbAttr.trim();
			}
		}

		int endPos = fCurrentPosition - bbEndStr.length() - 2;
		String innerTag = fStringSource.substring(startPos, endPos);

		return createBBCode(bbStr, bbAttr, innerTag);
	}

	private int parsePHPBBCodeRecursive(String rawWikitext, int index) {
		char ch = ' ';
		StringBuilder bbCode = new StringBuilder(10);

		while (index < rawWikitext.length()) {
			ch = rawWikitext.charAt(index++);
			if ('a' <= ch && ch <= 'z') {
				bbCode.append(ch);
			} else {
				break;
			}
		}

		String bbStr = bbCode.toString();
		String bbEndStr = bbStr + "]";
		int startPos = index;

		int endIndex = Util.indexOfIgnoreCase(rawWikitext, "[/", bbEndStr, index);
		if (endIndex != (-1)) {

			String bbAttr = null;
			if (ch == '=') {
				bbAttr = rawWikitext.substring(index, endIndex);
				if (bbAttr != null) {
					bbAttr = bbAttr.trim();
				}
			}

			String innerTag = rawWikitext.substring(startPos, endIndex);

			if (createBBCode(bbStr, bbAttr, innerTag)) {
				return endIndex + 3 + bbStr.length();
			}
		}
		return -1;

	}

	private boolean createBBCode(String bbStr, String bbAttr, String innerTag) {
		if (bbStr.equals("code")) {
			TagNode preTagNode = new TagNode("pre");
			preTagNode.addAttribute("class", "code", true);
			preTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(preTagNode);
			return true;
		} else if (bbStr.equals("color")) {
			if (bbAttr == null) {
				return false;
			}
			TagNode fontTagNode = new TagNode("font");
			fontTagNode.addAttribute("color", bbAttr, true);
			fontTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(fontTagNode);

			return true;
		} else if (bbStr.equals("email")) {
			TagNode aTagNode = new TagNode("a");
			aTagNode.addAttribute("href", "emailto:" + innerTag.trim(), true);
			aTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(aTagNode);

			return true;
		} else if (bbStr.equals("list")) {
			int listStart = 0;
			int listEnd = 0;
			TagNode listTagNode;
			if (bbAttr != null) {
				if (bbAttr.equals("a")) {
					listTagNode = new TagNode("ul");
				} else {
					listTagNode = new TagNode("ol");
				}
			} else {
				listTagNode = new TagNode("ul");
			}
			fWikiModel.pushNode(listTagNode);
			try {
				while (listEnd >= 0) {
					listEnd = innerTag.indexOf("[*]", listStart);
					if (listEnd > listStart) {
						if (listStart == 0) {
							parseNextPHPBBCode(innerTag.substring(0, listEnd));
							// fWikiModel.append(new ContentToken(innerTag.substring(0,
							// listEnd)));
						} else {
							listTagNode = new TagNode("li");
							fWikiModel.pushNode(listTagNode);
							try {
								parseNextPHPBBCode(innerTag.substring(listStart, listEnd));
								// listTagNode.addChild(new
								// ContentToken(innerTag.substring(listStart, listEnd)));
							} finally {
								fWikiModel.popNode();
							}
						}
						listStart = listEnd + 3;
					}
				}
				if (listStart == 0) {
					parseNextPHPBBCode(innerTag);
					// fWikiModel.append(new ContentToken(innerTag));
				} else {
					if (listStart < innerTag.length()) {
						listTagNode = new TagNode("li");
						fWikiModel.pushNode(listTagNode);
						try {
							parseNextPHPBBCode(innerTag.substring(listStart, innerTag.length()));
							// listTagNode.addChild(new
							// ContentToken(innerTag.substring(listStart,
							// innerTag.length())));
							// fWikiModel.append(listTagNode);
						} finally {
							fWikiModel.popNode();
						}
					}
				}

			} finally {
				fWikiModel.popNode();
			}
			return true;
		} else if (bbStr.equals("img")) {
			TagNode imgTagNode = new TagNode("img");
			imgTagNode.addAttribute("src", innerTag.trim(), true);
			imgTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(imgTagNode);

			return true;
		} else if (bbStr.equals("quote")) {
			TagNode quoteTagNode = new TagNode("blockquote");
			// quoteTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.pushNode(quoteTagNode);
			try {
				parseNextPHPBBCode(innerTag);
			} finally {
				fWikiModel.popNode();
			}
			return true;
		} else if (bbStr.equals("size")) {
			if (bbAttr == null) {
				return false;
			}
			TagNode fontTagNode = new TagNode("font");
			fontTagNode.addAttribute("size", bbAttr, true);
			fontTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(fontTagNode);

			return true;
		} else if (bbStr.equals("url")) {
			if (bbAttr != null) {
				TagNode aTagNode = new TagNode("a");
				aTagNode.addAttribute("href", bbAttr, true);
				aTagNode.addChild(new ContentToken(innerTag));
				fWikiModel.append(aTagNode);

				return true;
			} else {
				TagNode aTagNode = new TagNode("a");
				aTagNode.addAttribute("href", innerTag.trim(), true);
				aTagNode.addChild(new ContentToken(innerTag));
				fWikiModel.append(aTagNode);

				return true;
			}
		} else if (bbStr.equals("b")) {
			TagNode boldTagNode = new TagNode("b");
			boldTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(boldTagNode);
			return true;
		} else if (bbStr.equals("i")) {
			TagNode italicTagNode = new TagNode("i");
			italicTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(italicTagNode);
			return true;
		} else if (bbStr.equals("u")) {
			TagNode underlineTagNode = new TagNode("u");
			underlineTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(underlineTagNode);
			return true;
		}

		return false;
	}

	private void parseNextPHPBBCode(String rawWikitext) {
		int index = rawWikitext.indexOf('[');
		int lastIndex = 0;
		int tempIndex = -1;
		if (index < 0) {
			fWikiModel.append(new ContentToken(rawWikitext));
			return;
		}
		try {
			while (index >= 0) {
				String temp = rawWikitext.substring(lastIndex, index);
				if (temp.length() > 0) {
					fWikiModel.append(new ContentToken(temp));
				}
				tempIndex = parsePHPBBCodeRecursive(rawWikitext, index + 1);
				if (tempIndex < 0) {
					lastIndex = index + 1;
					index = rawWikitext.indexOf('[', index + 1);
				} else {
					lastIndex = tempIndex;
					index = rawWikitext.indexOf('[', tempIndex);
				}
			}
		} catch (IndexOutOfBoundsException e) {
			//
		}
		if (lastIndex < rawWikitext.length()) {
			fWikiModel.append(new ContentToken(rawWikitext.substring(lastIndex)));
		}
	}

	public TagStack parseRecursiveInternal(IWikiModel wikiModel, boolean createOnlyLocalStack, boolean noTOC) {
		// local stack for this wiki snippet
		TagStack localStack = new TagStack();
		// global wiki model stack
		TagStack globalWikiModelStack = wikiModel.swapStack(localStack);
		try {
			int level = wikiModel.incrementRecursionLevel();

			if (level > Configuration.PARSER_RECURSION_LIMIT) {
				TagNode error = new TagNode("span");
				error.addAttribute("class", "error", true);
				error.addChild(new ContentToken("Error - recursion limit exceeded parsing wiki tags."));
				localStack.append(error);
				return localStack;
			}
			// WikipediaParser parser = new WikipediaParser(rawWikitext,
			// wikiModel.isTemplateTopic(), wikiModel.getWikiListener());
			setModel(wikiModel);
			setNoToC(noTOC);
			runParser();
			return localStack;
		} catch (Exception e) {
			e.printStackTrace();
			TagNode error = new TagNode("span");
			error.addAttribute("class", "error", true);
			error.addChild(new ContentToken(e.getClass().getSimpleName()));
			localStack.append(error);
		} catch (Error e) {
			e.printStackTrace();
			TagNode error = new TagNode("span");
			error.addAttribute("class", "error", true);
			error.addChild(new ContentToken(e.getClass().getSimpleName()));
			localStack.append(error);
		} finally {
			wikiModel.decrementRecursionLevel();
			if (!createOnlyLocalStack) {
				// append the resursively parsed local stack to the global wiki
				// model
				// stack
				globalWikiModelStack.append(localStack);
			}
			wikiModel.swapStack(globalWikiModelStack);
		}

		return localStack;
	}

	/**
	 * Read the characters until the end position of the current wiki link is
	 * found
	 * 
	 * @return
	 */
	protected final boolean findWikiLinkEnd() {
		char ch;
		int level = 1;
		int position = fCurrentPosition;
		boolean pipeSymbolFound = false;
		try {
			while (true) {
				ch = fSource[position++];
				if (ch == '|') {
					pipeSymbolFound = true;
				} else if (ch == '[' && fSource[position] == '[') {
					if (pipeSymbolFound) {
						level++;
						position++;
					} else {
						return false;
					}
				} else if (ch == ']' && fSource[position] == ']') {
					position++;
					if (--level == 0) {
						break;
					}
				} else if (ch == '{' || ch == '}' || ch == '<' || ch == '>') {
					if (!pipeSymbolFound) {
						// see
						// http://en.wikipedia.org/wiki/Help:Page_name#Special_characters
						return false;
					}
				}

				if ((!pipeSymbolFound) && (ch == '\n' || ch == '\r')) {
					return false;
				}
			}
			fCurrentPosition = position;
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public abstract void setNoToC(boolean noToC);

	public abstract void runParser();

}
