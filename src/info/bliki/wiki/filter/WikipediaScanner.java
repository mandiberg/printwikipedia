package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.ITableOfContent;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.TableOfContentTag;
import info.bliki.wiki.tags.util.NodeAttribute;
import info.bliki.wiki.tags.util.WikiTagNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WikipediaScanner {

	public final static String TAG_NAME = "$TAG_NAME";

	/**
	 * Return value when the source is exhausted. Has a value of <code>-1</code>.
	 */
	public static final int EOF = -1;

	protected int fScannerPosition;

	protected IWikiModel fWikiModel = null;

	/**
	 * The <code>String</code> of the given raw wiki text
	 */
	protected final String fStringSource;

	/**
	 * The corresponding <code>char[]</code> array for the string source
	 */
	protected final char[] fSource;

	public WikipediaScanner(String src) {
		this(src, 0);
	}

	public WikipediaScanner(String src, int position) {
		fSource = src.toCharArray();
		fStringSource = src;
		fScannerPosition = position;
		// initialize(src, position);
	}

	public void setModel(IWikiModel wikiModel) {
		fWikiModel = wikiModel;
	}

	public int getPosition() {
		return fScannerPosition;
	}

	public void setPosition(int newPos) {
		fScannerPosition = newPos;
	}

	/**
	 * Scan a wikipedia table.
	 * 
	 * See: <a href="http://meta.wikimedia.org/wiki/Help:Table">Help - Table</a>
	 * 
	 * @param tableOfContentTag
	 * @return <code>null</code> if no wiki table was found
	 */
	public WPTable wpTable(ITableOfContent tableOfContentTag) {
		WPTable table = null;
		WPCell cell = null;
		ArrayList<WPCell> cells = new ArrayList<WPCell>();
		WPRow row = new WPRow(cells);
		try {
			if (fScannerPosition < 0) {
				// simulate newline
				fScannerPosition = 0;
			}
			if (fSource[fScannerPosition++] != '{') {
				return null;
			}
			if (fSource[fScannerPosition++] != '|') {
				return null;
			}
			ArrayList<WPRow> rows = new ArrayList<WPRow>();
			table = new WPTable(rows);
			int startPos = fScannerPosition;
			// read parameters until end of line
			nextNewline();
			table.setParams(fStringSource.substring(startPos, fScannerPosition));

			char ch = ' ';

			while (true) {
				ch = fSource[fScannerPosition++];
				switch (ch) {
				case '\n':
					ch = fSource[fScannerPosition++];
					switch (ch) {
					case '|': // "\n|"
						if (cell != null) {
							cell.createTagStack(table, fStringSource, fWikiModel, fScannerPosition - 2);
							cell = null;
						}

						ch = fSource[fScannerPosition++];
						switch (ch) {
						case '-': // new row - "\n|-"
							addTableRow(table, row);
							cells = new ArrayList<WPCell>();
							row = new WPRow(cells);
							startPos = fScannerPosition;
							nextNewline();
							row.setParams(fStringSource.substring(startPos, fScannerPosition));
							break;
						case '+': // new row - "\n|+"
							addTableRow(table, row);
							cells = new ArrayList<WPCell>();
							row = new WPRow(cells);
							row.setType(WPCell.CAPTION);
							cell = new WPCell(fScannerPosition);
							cell.setType(WPCell.CAPTION);
							cells.add(cell);
							nextNewline();
							cell.createTagStack(table, fStringSource, fWikiModel, fScannerPosition);
							cell = null;

							addTableRow(table, row);
							cells = new ArrayList<WPCell>();
							row = new WPRow(cells);
							break;
						case '}': // end of table - "\n|}"
							addTableRow(table, row);
							return table;
						default:
							fScannerPosition--;
							cell = new WPCell(fScannerPosition);
							cells.add(cell);
						}

						break;
					case '!': // "\n!"
						if (cell != null) {
							cell.createTagStack(table, fStringSource, fWikiModel, fScannerPosition - 2);
							cell = null;
						}
						ch = fSource[fScannerPosition++];
						cell = new WPCell(fScannerPosition - 1);
						cell.setType(WPCell.TH);
						cells.add(cell);

						break;
					case '{': // "\n{"
						if (fSource[fScannerPosition] == '|') {
							// nested table
							fScannerPosition = indexEndOfTable();
							break;
						}
						break;
					default:
						fScannerPosition--;
					}
					break;
				case '|':
					ch = fSource[fScannerPosition++];
					if (ch == '|') {
						if (cell != null) {
							cell.createTagStack(table, fStringSource, fWikiModel, fScannerPosition - 2);
							cell = null;
						}
						cell = new WPCell(fScannerPosition);
						cells.add(cell);
					} else {
						fScannerPosition--;
					}
					break;
				case '!':
					ch = fSource[fScannerPosition++];
					if (ch == '!') {
						if (cell != null) {
							cell.createTagStack(table, fStringSource, fWikiModel, fScannerPosition - 2);
							cell = null;
						}
						cell = new WPCell(fScannerPosition);
						cell.setType(WPCell.TH);
						cells.add(cell);
					} else {
						fScannerPosition--;
					}
					break;
				default:
					if (cell == null) {
						cell = new WPCell(fScannerPosition - 1);
						cell.setType(WPCell.UNDEFINED);
						cells.add(cell);
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// ...
			fScannerPosition = fSource.length;
			if (cell != null) {
				cell.createTagStack(table, fStringSource, fWikiModel, fScannerPosition);
				cell = null;
			}
			if (table != null && row != null && row.size() > 0) {
				addTableRow(table, row);
			}
		}
		if (table != null) {
			return table;
		}
		return null;
	}

	/**
	 * Scan a Trac simple wiki table
	 * 
	 * @param tableOfContentTag
	 * @return
	 */
	public WPTable tracTable(TableOfContentTag tableOfContentTag) {
		WPTable table = null;
		WPCell cell = null;
		ArrayList<WPCell> cells = new ArrayList<WPCell>();
		WPRow row = new WPRow(cells);
		try {
			if (fScannerPosition < 0) {
				// simulate newline
				fScannerPosition = 0;
			}
			if (fSource[fScannerPosition++] != '|') {
				return null;
			}
			if (fSource[fScannerPosition++] != '|') {
				return null;
			}
			ArrayList<WPRow> rows = new ArrayList<WPRow>();
			table = new WPTable(rows);
			fScannerPosition -= 2;
			char ch = ' ';

			while (true) {
				ch = fSource[fScannerPosition++];
				switch (ch) {
				case '\n':
					addTableRow(table, row);
					cell = null;
					cells = new ArrayList<WPCell>();
					row = new WPRow(cells);
					if (fSource[fScannerPosition] != '|' || fSource[fScannerPosition + 1] != '|') {
						return table;
					}
					continue;
				case '|':
					ch = fSource[fScannerPosition++];
					if (ch == '|') {
						if (cell != null) {
							cell.createTagStack(table, fStringSource, fWikiModel, fScannerPosition - 2);
							cells.add(cell);
						}
						cell = new WPCell(fScannerPosition);
					} else {
						fScannerPosition--;
					}
					break;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// ...
			fScannerPosition = fSource.length;
			if (cell != null) {
				cell.createTagStack(table, fStringSource, fWikiModel, fScannerPosition);
				cells.add(cell);
			}
			if (table != null && row != null && row.size() > 0) {
				addTableRow(table, row);
			}
		}
		if (table != null) {
			return table;
		}
		return null;
	}

	private void addTableRow(WPTable table, WPRow row) {
		if (row.getParams() != null) {
			table.add(row);
		} else {
			if (row.size() > 0) {
				table.add(row);
			}
		}
	}

	public WPList wpList() {
		WPList list = null;
		WPListElement listElement = null;
		int startPosition;
		try {
			char ch;
			char lastCh = ' ';
			char[] sequence = null;
			int count = 0;

			if (fScannerPosition < 0) {
				// simulate newline
				fScannerPosition = 0;
				ch = '\n';
			} else {
				ch = fSource[fScannerPosition++];
			}

			list = new WPList();

			while (true) {
				if (ch == WPList.DL_DD_CHAR && lastCh == WPList.DL_DT_CHAR && sequence != null) {
					startPosition = fScannerPosition;
					if (listElement != null) {
						listElement.createTagStack(fSource, fWikiModel, fScannerPosition - 1);
						list.add(listElement);
						listElement = null;
					}
					char[] ddSequence = new char[sequence.length];
					System.arraycopy(sequence, 0, ddSequence, 0, sequence.length);
					ddSequence[sequence.length - 1] = WPList.DL_DD_CHAR;
					sequence = ddSequence;

					int startPos;
					while (true) {
						ch = fSource[fScannerPosition++];
						if (!Character.isWhitespace(ch)) {
							startPos = fScannerPosition - 1;
							listElement = new WPListElement(count, sequence, startPos);
							break;
						}
						if (ch == '\n') {
							fScannerPosition--; // to detect next row
							startPos = fScannerPosition;
							listElement = new WPListElement(count, sequence, startPos);
							listElement.createTagStack(fSource, fWikiModel, startPos);
							list.add(listElement);
							listElement = null;
							break;
						}
					}

				}
				if (ch == '\n' || fScannerPosition == 0) {
					startPosition = fScannerPosition;
					if (listElement != null) {
						listElement.createTagStack(fSource, fWikiModel, fScannerPosition - 1);
						list.add(listElement);
						listElement = null;
					}
					ch = fSource[fScannerPosition++];
					switch (ch) {
					case WPList.DL_DD_CHAR:
					case WPList.DL_DT_CHAR:
					case WPList.OL_CHAR:
					case WPList.UL_CHAR:
						count = 1;
						lastCh = ch;
						while (fSource[fScannerPosition] == WPList.UL_CHAR || fSource[fScannerPosition] == WPList.OL_CHAR
								|| fSource[fScannerPosition] == WPList.DL_DD_CHAR || fSource[fScannerPosition] == WPList.DL_DT_CHAR) {
							count++;
							lastCh = fSource[fScannerPosition++];
						}

						sequence = new char[count];
						System.arraycopy(fSource, fScannerPosition - count, sequence, 0, count);

						int startPos;
						while (true) {
							ch = fSource[fScannerPosition++];
							if (!Character.isWhitespace(ch)) {
								startPos = fScannerPosition - 1;
								listElement = new WPListElement(count, sequence, startPos);
								break;
							}
							if (ch == '\n') {
								fScannerPosition--; // to detect next row
								startPos = fScannerPosition;
								listElement = new WPListElement(count, sequence, startPos);
								listElement.createTagStack(fSource, fWikiModel, startPos);
								list.add(listElement);
								listElement = null;
								break;
							}
						}

						break;

					default:
						fScannerPosition = startPosition;
						return list;
					}
				}

				if (ch == '<') {
					int temp = readSpecialWikiTags(fScannerPosition);
					if (temp >= 0) {
						fScannerPosition = temp;
						ch = fSource[fScannerPosition++];
						continue;
					}
				}
				ch = fSource[fScannerPosition++];
			}
		} catch (IndexOutOfBoundsException e) {
			fScannerPosition = fSource.length + 1;
		}
		if (list != null) {
			if (listElement != null) {
				listElement.createTagStack(fSource, fWikiModel, fScannerPosition - 1);
				list.add(listElement);
				listElement = null;
			}
			return list;
		}
		return null;
	}

	public WPList tracList() {
		return wpList();
		// TreeMap<Integer, Integer> levelList= new TreeMap<Integer, Integer>();
		// int level=0;
		// int biggestLevel=level;
		// WPList list = null;
		// WPListElement listElement = null;
		// int startPosition;
		// try {
		// char ch;
		// if (fScannerPosition < 0) {
		// // simulate newline
		// fScannerPosition = 0;
		// ch = '\n';
		// } else {
		// ch = fSource[fScannerPosition++];
		// }
		//
		// list = new WPList();
		// while (true) {
		// if (ch == '\n' || fScannerPosition == 0) {
		// startPosition = fScannerPosition;
		// if (listElement != null) {
		// listElement.createTagStack(fSource, fWikiModel, fScannerPosition - 1);
		// list.add(listElement);
		// listElement = null;
		// }
		// ch = fSource[fScannerPosition++];
		// if (ch != ' ') {
		// fScannerPosition = startPosition;
		// return list;
		// }
		// int count=1;
		// while (true) {
		// ch = fSource[fScannerPosition++];
		// if (ch != ' ') {
		// break;
		// }
		// count++;
		// }
		//
		// switch (ch) {
		// case '#':
		// case '*':
		// int type;
		// char[] sequence;
		// // Integer value = levelList.get(count);
		// // if (value!=null) {
		// //
		// // } else {
		// //
		// // }
		// sequence = new char[count];
		// System.arraycopy(fSource, fScannerPosition - count, sequence, 0, count);
		// if (ch == '#') {
		// type = WPListElement.OL;
		// } else {
		// type = WPListElement.UL;
		// }
		//
		// int startPos;
		// while (true) {
		// ch = fSource[fScannerPosition++];
		// if (!Character.isWhitespace(ch)) {
		// startPos = fScannerPosition - 1;
		// listElement = new WPListElement(type, count, sequence, startPos);
		// break;
		// }
		// if (ch == '\n') {
		// fScannerPosition--; // to detect next row
		// startPos = fScannerPosition;
		// listElement = new WPListElement(type, count, sequence, startPos);
		// listElement.createTagStack(fSource, fWikiModel, startPos);
		// list.add(listElement);
		// listElement = null;
		// break;
		// }
		// }
		//
		// break;
		//
		// default:
		// fScannerPosition = startPosition;
		// return list;
		// }
		// }
		// ch = fSource[fScannerPosition++];
		// }
		// } catch (IndexOutOfBoundsException e) {
		// fScannerPosition = fSource.length + 1;
		// }
		// if (list != null) {
		// if (listElement != null) {
		// listElement.createTagStack(fSource, fWikiModel, fScannerPosition - 1);
		// list.add(listElement);
		// listElement = null;
		// }
		// return list;
		// }
		// return null;
	}

	public int nextNewline() {
		while (true) {
			if (fSource[fScannerPosition++] == '\n') {
				return --fScannerPosition;
			}
		}
	}

	public int indexEndOfComment() {
		char ch;
		try {
			while (true) {
				ch = fSource[fScannerPosition++];
				if (ch == '-' && fSource[fScannerPosition] == '-' && fSource[fScannerPosition + 1] == '>') {
					return fScannerPosition + 2;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// ..
		}
		return -1;
	}

	public int indexOf(char ch) {
		try {
			while (true) {
				if (fSource[fScannerPosition++] == ch) {
					return fScannerPosition - 1;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// ..
		}
		return -1;
	}

	public int indexOf(char ch, char stop) {
		try {
			char c;
			while (true) {
				c = fSource[fScannerPosition];
				if (c == ch) {
					return fScannerPosition;
				}
				if (c == stop) {
					return -1;
				}
				fScannerPosition++;
			}
		} catch (IndexOutOfBoundsException e) {
			// ..
		}
		return -1;
	}

	public int indexEndOfNowiki() {
		char ch;
		try {
			while (true) {
				ch = fSource[fScannerPosition++];
				if (ch == '<' && fSource[fScannerPosition] == '/' && fSource[fScannerPosition + 1] == 'n'
						&& fSource[fScannerPosition + 2] == 'o' && fSource[fScannerPosition + 3] == 'w' && fSource[fScannerPosition + 4] == 'i'
						&& fSource[fScannerPosition + 5] == 'k' && fSource[fScannerPosition + 6] == 'i' && fSource[fScannerPosition + 7] == '>') {
					return fScannerPosition + 8;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// ..
		}
		return -1;
	}

	public int indexEndOfTable() {
		// check nowiki and html comments?
		int count = 1;
		char ch;
		try {
			while (true) {
				ch = fSource[fScannerPosition++];
				if (ch == '<' && fSource[fScannerPosition] == '!' && fSource[fScannerPosition + 1] == '-'
						&& fSource[fScannerPosition + 2] == '-') {
					fScannerPosition = indexEndOfComment();
					if (fScannerPosition == (-1)) {
						return -1;
					}
				} else if (ch == '<' && fSource[fScannerPosition] == 'n' && fSource[fScannerPosition + 1] == 'o'
						&& fSource[fScannerPosition + 2] == 'w' && fSource[fScannerPosition + 3] == 'i' && fSource[fScannerPosition + 4] == 'k'
						&& fSource[fScannerPosition + 5] == 'i' && fSource[fScannerPosition + 6] == '>') {
					fScannerPosition = indexEndOfNowiki();
					if (fScannerPosition == (-1)) {
						return -1;
					}
				} else if (ch == '\n' && fSource[fScannerPosition] == '|' && fSource[fScannerPosition + 1] == '}') {
					count--;
					if (count == 0) {
						return fScannerPosition + 2;
					}
				} else if (ch == '\n' && fSource[fScannerPosition] == '{' && fSource[fScannerPosition + 1] == '|') {
					// assume nested table
					count++;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// ..
		}
		return -1;
	}

	/**
	 * Scan the attributes of a wiki table cell
	 * 
	 * @return
	 */
	public int indexOfAttributes() {
		try {
			// int start = fScannerPosition;
			char ch = fSource[fScannerPosition];
			while (true) {
				// TODO scan for NOWIKI and HTML comments

				if (ch == '[') {
					// scan for Wiki links, which could contain '|' character
					int countBrackets = 1;
					fScannerPosition++;
					while (countBrackets > 0) {
						ch = fSource[fScannerPosition++];
						if (ch == '[') {
							++countBrackets;
						} else if (ch == ']') {
							--countBrackets;
						}
					}
					ch = fSource[fScannerPosition];
					continue;
				} else if (ch == '{') {
					// scan for Wiki templates, which could contain '|' character
					int countCurlyBrackets = 1;
					fScannerPosition++;
					while (countCurlyBrackets > 0) {
						ch = fSource[fScannerPosition++];
						if (ch == '{') {
							++countCurlyBrackets;
						} else if (ch == '}') {
							--countCurlyBrackets;
						}
					}
					ch = fSource[fScannerPosition];
					continue;
				}
				if (ch == '|') {
					if (fSource[fScannerPosition + 1] == '|') {
						return -1;
					}
					return fScannerPosition;
				}
				if (fSource[fScannerPosition] == '\n') {
					return -1;
				}
				ch = fSource[++fScannerPosition];
			}
		} catch (IndexOutOfBoundsException e) {
			// ..
		}
		return -1;
	}

	public void scanWhiteSpace() {
		while (Character.isWhitespace(fSource[fScannerPosition++])) {
		}
		--fScannerPosition;
	}

	/**
	 * Replace the wiki template parameters in the given template string
	 * 
	 * @param template
	 * @param fTemplateParameters
	 * @return <code>null</code> if no replacement could be found
	 */
	public StringBuilder replaceTemplateParameters(String template, Map<String, String> templateParameters) {
		StringBuilder buffer = null;
		int bufferStart = 0;
		try {
			int level = fWikiModel.incrementRecursionLevel();
			if (level > Configuration.PARSER_RECURSION_LIMIT) {
				return null; // no further processing
			}
			char ch;
			int parameterStart = -1;
			StringBuilder recursiveResult;
			while (true) {
				ch = fSource[fScannerPosition++];
				if (ch == '{' && fSource[fScannerPosition] == '{' && fSource[fScannerPosition + 1] == '{') {
					fScannerPosition += 2;
					parameterStart = fScannerPosition;

					int temp[] = findNestedParamEnd(fSource, parameterStart);
					if (temp[0] >= 0) {
						fScannerPosition = temp[0];
						List<String> list = splitByPipe(fSource, parameterStart, fScannerPosition - 3, null);
						if (list.size() > 0) {
							String parameterString = list.get(0);
							String value = null;
							if (templateParameters != null) {
								value = templateParameters.get(parameterString);
							}
							if (value == null && list.size() > 1) {
								value = list.get(1);
							}
							if (value != null) {
								if (buffer == null) {
									buffer = new StringBuilder(template.length() + 128);
								}
								if (bufferStart < fScannerPosition) {
									buffer.append(fSource, bufferStart, parameterStart - bufferStart - 3);
								}

								WikipediaScanner scanner = new WikipediaScanner(value);
								scanner.setModel(fWikiModel);
								recursiveResult = scanner.replaceTemplateParameters(value, templateParameters);
								if (recursiveResult != null) {
									buffer.append(recursiveResult);
								} else {
									buffer.append(value);
								}

								bufferStart = fScannerPosition;
							}
						}
						fScannerPosition = temp[0];
						parameterStart = -1;
					}
				}

			}
		} catch (IndexOutOfBoundsException e) {
			// ignore
		} finally {
			fWikiModel.decrementRecursionLevel();
		}
		if (buffer != null && bufferStart < fScannerPosition) {
			buffer.append(fSource, bufferStart, fScannerPosition - bufferStart - 1);
		}
		return buffer;
	}

	/**
	 * Split the given src string by pipe symbol (i.e. &quot;|&quot;)
	 * 
	 * @param sourceString
	 * @param resultList
	 *          the list which contains the splitted strings
	 * @return
	 */
	public static List<String> splitByPipe(String sourceString, List<String> resultList) {
		// TODO optimize this to avoid new char[] generation inside toCharArray() ?
		return splitByPipe(sourceString.toCharArray(), 0, sourceString.length(), resultList);
	}

	/**
	 * Split the given src character array by pipe symbol (i.e. &quot;|&quot;)
	 * 
	 * @param srcArray
	 * @param currOffset
	 * @param endOffset
	 * @param resultList
	 *          the list which contains the splitted strings
	 * @return
	 */
	public static List<String> splitByPipe(char[] srcArray, int currOffset, int endOffset, List<String> resultList) {
		if (resultList == null) {
			resultList = new ArrayList<String>();
		}
		char ch;
		String value;
		int[] temp = new int[] { -1, -1 };
		;
		int lastOffset = currOffset;
		try {
			while (currOffset < endOffset) {
				ch = srcArray[currOffset++];
				if (ch == '[' && srcArray[currOffset] == '[') {
					currOffset++;
					temp[0] = findNestedEnd(srcArray, '[', ']', currOffset);
					if (temp[0] >= 0) {
						currOffset = temp[0];
					}
				} else if (ch == '{' && srcArray[currOffset] == '{') {
					currOffset++;
					if (srcArray[currOffset] == '{') {
						currOffset++;
						temp = findNestedParamEnd(srcArray, currOffset);
						if (temp[0] >= 0) {
							currOffset = temp[0];
						}
					} else {
						temp[0] = findNestedTemplateEnd(srcArray, currOffset);
						if (temp[0] >= 0) {
							currOffset = temp[0];
						}
					}
				} else if (ch == '|') {
					value = Utils.trimNewlineRight(new String(srcArray, lastOffset, currOffset - lastOffset - 1));
					resultList.add(value);
					lastOffset = currOffset;
				}
			}

			if (currOffset > lastOffset) {
				resultList.add(Utils.trimNewlineRight(new String(srcArray, lastOffset, currOffset - lastOffset)));
			} else if (currOffset == lastOffset) {
				resultList.add("");
			}
		} catch (IndexOutOfBoundsException e) {
			if (currOffset > lastOffset) {
				resultList.add(Utils.trimNewlineRight(new String(srcArray, lastOffset, currOffset - lastOffset)));
			} else if (currOffset == lastOffset) {
				resultList.add("");
			}
		}
		return resultList;
	}

	public static final int findNestedEnd(final char[] sourceArray, final char startCh, final char endChar, int startPosition) {
		char ch;
		int level = 1;
		int position = startPosition;
		try {
			while (true) {
				ch = sourceArray[position++];
				if (ch == startCh && sourceArray[position] == startCh) {
					position++;
					level++;
				} else if (ch == endChar && sourceArray[position] == endChar) {
					position++;
					if (--level == 0) {
						break;
					}
				}
			}
			return position;
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}

	public static final int findNestedTemplateEnd(final char[] sourceArray, int startPosition) {
		char ch;
		// int len = sourceArray.length;
		int countSingleOpenBraces = 0;
		int position = startPosition;
		try {
			while (true) {
				ch = sourceArray[position++];
				if (ch == '{') {
					// if (sourceArray[position] == '{') {
					// position++;
					// if ((len > position) && sourceArray[position] == '{') {
					// // template parameter beginning
					// position++;
					// int[] temp = findNestedParamEnd(sourceArray, position);
					// if (temp[0] < 0) {
					// position--;
					// temp[0] = findNestedTemplateEnd(sourceArray, position);
					// if (temp[0] < 0) {
					// return -1;
					// }
					// }
					// position = temp[0];
					// } else {
					// // template beginning
					// int temp = findNestedTemplateEnd(sourceArray, position);
					// if (temp < 0) {
					// return -1;
					// }
					// position = temp;
					// }
					// } else {
					countSingleOpenBraces++;
					// }
				} else if (ch == '}') {
					if (countSingleOpenBraces > 0) {
						countSingleOpenBraces--;
					} else {
						if (sourceArray[position] == '}') {
							// template ending
							position++;
							break;
						}
					}
				}
			}
			return position;
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}

	public static final int[] findNestedParamEnd(final char[] sourceArray, int startPosition) {
		char ch;
		int len = sourceArray.length;
		int countSingleOpenBraces = 0;
		int parameterPosition = startPosition;
		// int templatePosition = -1;
		// int[] result = new int[] { -1, -1 };
		try {
			while (true) {
				ch = sourceArray[parameterPosition++];
				if (ch == '{') {
					if (sourceArray[parameterPosition] == '{') {
						parameterPosition++;
						if ((len > parameterPosition) && sourceArray[parameterPosition] == '{') {
							// template parameter beginning
							parameterPosition++;
							int[] temp = findNestedParamEnd(sourceArray, parameterPosition);
							if (temp[0] >= 0) {
								parameterPosition = temp[0];
							} else {
								if (temp[1] >= 0) {
									parameterPosition = temp[1];
								} else {
									return new int[] { -1, -1 };
								}
							}
							// if (temp[0] < 0) {
							// // return -1;
							// parameterPosition--;
							// temp[0] = findNestedTemplateEnd(sourceArray,
							// parameterPosition);
							// if (temp[0] < 0) {
							// return new int[] { -1, -1 };
							// }
							// }
							// parameterPosition = temp[0];
						} else {
							// template beginning
							int temp = findNestedTemplateEnd(sourceArray, parameterPosition);
							if (temp < 0) {
								return new int[] { -1, -1 };
							}
							parameterPosition = temp;
						}
					} else {
						countSingleOpenBraces++;
					}
				} else if (ch == '}') {
					if (countSingleOpenBraces > 0) {
						countSingleOpenBraces--;
					} else {
						if (sourceArray[parameterPosition] == '}') {
							if (sourceArray[parameterPosition + 1] == '}') {
								// template parameter ending
								return new int[] { parameterPosition + 2, -1 };
							} else {
								return new int[] { -1, parameterPosition + 1 };
							}
						}
					}

				}
			}
		} catch (IndexOutOfBoundsException e) {
			return new int[] { -1, -1 };
		}
	}

	public static final int findStackedTemplateEnd(final char[] sourceArray, final char startCh, final char endChar, int startPosition) {
		char ch;
		int templateLevel = 0;
		int parameterLevel = 1;
		int len = sourceArray.length;
		int position = startPosition;

		try {
			while (true) {
				ch = sourceArray[position++];
				if (ch == startCh && sourceArray[position] == startCh) {
					position++;
					if ((len > position) && sourceArray[position] == startCh) {
						// template parameter beginning
						position++;
						parameterLevel++;
					} else {
						templateLevel++;
					}
				} else if (ch == endChar && sourceArray[position] == endChar) {
					position++;
					if ((templateLevel == 0) && (len > position) && sourceArray[position] == endChar) {
						// template parameter ending
						position++;
						if (--parameterLevel == 0) {
							break;
						}
					} else {
						templateLevel--;
					}
				}
			}
			return position;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return -1;
		}
	}

	// public static final int findNestedParamEnd(final char[] sourceArray, final
	// char startCh, final char endChar, int startPosition) {
	// char ch;
	// int parameterLevel = 1;
	// int position = startPosition;
	// try {
	// while (true) {
	// ch = sourceArray[position++];
	// if (ch == startCh && sourceArray[position] == startCh &&
	// sourceArray[position + 1] == startCh) {
	// // ending of template parameters {{{
	// position += 2;
	// parameterLevel++;
	// } else if (ch == endChar && sourceArray[position] == endChar &&
	// sourceArray[position + 1] == endChar) {
	// // ending of template parameters }}}
	// position += 2;
	// if (--parameterLevel == 0) {
	// break;
	// }
	// }
	// }
	// return position;
	// } catch (IndexOutOfBoundsException e) {
	// return -1;
	// }
	// }

	/**
	 * Read the characters until the end position of the current template is found
	 * 
	 * @return
	 */
	// protected final int findTemplateEnd(final char[] sourceArray, int
	// startPosition) {
	// char ch;
	// int position = startPosition;
	// int counter = 0;
	// try {
	// while (true) {
	// ch = sourceArray[position++];
	// if (ch == '}') {
	// if ((counter == 0) && sourceArray[position] == '}') {
	// // template ending
	// position++;
	// break;
	// }
	// if (counter > 0) {
	// counter--;
	// }
	// } else if (ch == '{') {
	// counter++;
	// continue;
	// }
	// }
	// return position;
	// } catch (IndexOutOfBoundsException e) {
	// return -1;
	// }
	// }
	/**
	 * Read the characters until the end position of the current template
	 * parameter is found
	 * 
	 * @return
	 */
	// protected final int[] findTemplateParameterEnd(int startPosition) {
	// return findNestedParamEnd(fSource, startPosition);
	// }
	/**
	 * Parse a tag. Parse the name and attributes from a start tag.
	 * <p>
	 * From the <a href="http://www.w3.org/TR/html4/intro/sgmltut.html#h-3.2.2">
	 * HTML 4.01 Specification, W3C Recommendation 24 December 1999</a>
	 * http://www.w3.org/TR/html4/intro/sgmltut.html#h-3.2.2
	 * <p>
	 * <cite> 3.2.2 Attributes
	 * <p>
	 * Elements may have associated properties, called attributes, which may have
	 * values (by default, or set by authors or scripts). Attribute/value pairs
	 * appear before the final ">" of an element's start tag. Any number of
	 * (legal) attribute value pairs, separated by spaces, may appear in an
	 * element's start tag. They may appear in any order.
	 * <p>
	 * In this example, the id attribute is set for an H1 element: <code>
	 * &lt;H1 id="section1"&gt;
	 * </code> This is
	 * an identified heading thanks to the id attribute <code>
	 * &lt;/H1&gt;
	 * </code> By default, SGML
	 * requires that all attribute values be delimited using either double
	 * quotation marks (ASCII decimal 34) or single quotation marks (ASCII decimal
	 * 39). Single quote marks can be included within the attribute value when the
	 * value is delimited by double quote marks, and vice versa. Authors may also
	 * use numeric character references to represent double quotes (&amp;#34;) and
	 * single quotes (&amp;#39;). For doublequotes authors can also use the
	 * character entity reference &amp;quot;.
	 * <p>
	 * In certain cases, authors may specify the value of an attribute without any
	 * quotation marks. The attribute value may only contain letters (a-z and
	 * A-Z), digits (0-9), hyphens (ASCII decimal 45), periods (ASCII decimal 46),
	 * underscores (ASCII decimal 95), and colons (ASCII decimal 58). We recommend
	 * using quotation marks even when it is possible to eliminate them.
	 * <p>
	 * Attribute names are always case-insensitive.
	 * <p>
	 * Attribute values are generally case-insensitive. The definition of each
	 * attribute in the reference manual indicates whether its value is
	 * case-insensitive.
	 * <p>
	 * All the attributes defined by this specification are listed in the
	 * attribute index.
	 * <p>
	 * </cite>
	 * <p>
	 * This method uses a state machine with the following states:
	 * <ol>
	 * <li>state 0 - outside of any attribute</li>
	 * <li>state 1 - within attributre name</li>
	 * <li>state 2 - equals hit</li>
	 * <li>state 3 - within naked attribute value.</li>
	 * <li>state 4 - within single quoted attribute value</li>
	 * <li>state 5 - within double quoted attribute value</li>
	 * <li>state 6 - whitespaces after attribute name could lead to state 2 (=)or
	 * state 0</li>
	 * </ol>
	 * <p>
	 * The starting point for the various components is stored in an array of
	 * integers that match the initiation point for the states one-for-one, i.e.
	 * bookmarks[0] is where state 0 began, bookmarks[1] is where state 1 began,
	 * etc. Attributes are stored in a <code>Vector</code> having one slot for
	 * each whitespace or attribute/value pair. The first slot is for attribute
	 * name (kind of like a standalone attribute).
	 * 
	 * @param start
	 *          The position at which to start scanning.
	 * @return The parsed tag.
	 * @exception ParserException
	 *              If a problem occurs reading from the source.
	 */
	protected WikiTagNode parseTag(int start) {
		boolean done;
		char ch;
		int state;
		int[] bookmarks;

		done = false;
		ArrayList<NodeAttribute> attributes = new ArrayList<NodeAttribute>();
		state = 0;
		fScannerPosition = start;
		bookmarks = new int[8];
		bookmarks[0] = fScannerPosition;
		try {
			while (!done) {
				bookmarks[state + 1] = fScannerPosition;
				ch = fSource[fScannerPosition++];
				switch (state) {
				case 0: // outside of any attribute
					if ((EOF == ch) || ('>' == ch) || ('<' == ch)) {
						if ('<' == ch) {
							// don't consume the opening angle
							bookmarks[state + 1] = --fScannerPosition;
						}
						whitespace(attributes, bookmarks);
						done = true;
					} else if (!Character.isWhitespace(ch)) {
						whitespace(attributes, bookmarks);
						state = 1;
					}
					break;
				case 1: // within attribute name
					if ((EOF == ch) || ('>' == ch) || ('<' == ch)) {
						if ('<' == ch) {
							// don't consume the opening angle
							bookmarks[state + 1] = --fScannerPosition;
						}
						standalone(attributes, bookmarks);
						done = true;
					} else if (Character.isWhitespace(ch)) {
						// whitespaces might be followed by next attribute or an
						// equal sign
						// see Bug #891058 Bug in lexer.
						bookmarks[6] = bookmarks[2]; // setting the
						// bookmark[0]
						// is done in state 6 if
						// applicable
						state = 6;
					} else if ('=' == ch)
						state = 2;
					break;
				case 2: // equals hit
					if ((EOF == ch) || ('>' == ch)) {
						empty(attributes, bookmarks);
						done = true;
					} else if ('\'' == ch) {
						state = 4;
						bookmarks[4] = bookmarks[3];
					} else if ('"' == ch) {
						state = 5;
						bookmarks[5] = bookmarks[3];
					} else if (Character.isWhitespace(ch)) {
						// collect white spaces after "=" into the assignment
						// string;
						// do nothing
						// see Bug #891058 Bug in lexer.
					} else
						state = 3;
					break;
				case 3: // within naked attribute value
					if ((EOF == ch) || ('>' == ch)) {
						naked(attributes, bookmarks);
						done = true;
					} else if (Character.isWhitespace(ch)) {
						naked(attributes, bookmarks);
						bookmarks[0] = bookmarks[4];
						state = 0;
					} else if (ch == '/' && fSource[fScannerPosition] == '>') {
						naked(attributes, bookmarks);
						bookmarks[0] = bookmarks[4];
						fScannerPosition--;
						state = 0;
					}
					break;
				case 4: // within single quoted attribute value
					if (EOF == ch) {
						single_quote(attributes, bookmarks);
						done = true; // complain?
					} else if ('\'' == ch) {
						single_quote(attributes, bookmarks);
						bookmarks[0] = bookmarks[5] + 1;
						state = 0;
					}
					break;
				case 5: // within double quoted attribute value
					if (EOF == ch) {
						double_quote(attributes, bookmarks);
						done = true; // complain?
						// } else if ('\\' == ch && fSource[fScannerPosition] == '"') {
						// fScannerPosition++;
					} else if ('"' == ch) {
						double_quote(attributes, bookmarks);
						bookmarks[0] = bookmarks[6] + 1;
						state = 0;
					}
					break;
				// patch for lexer state correction by
				// Gernot Fricke
				// See Bug # 891058 Bug in lexer.
				case 6: // undecided for state 0 or 2
					// we have read white spaces after an attributte name
					if (EOF == ch) {
						// same as last else clause
						standalone(attributes, bookmarks);
						bookmarks[0] = bookmarks[6];
						// mPage.ungetCharacter(mCursor);
						--fScannerPosition;
						state = 0;
					} else if (Character.isWhitespace(ch)) {
						// proceed
					} else if ('=' == ch) // yepp. the white spaces belonged
					// to the equal.
					{
						bookmarks[2] = bookmarks[6];
						bookmarks[3] = bookmarks[7];
						state = 2;
					} else {
						// white spaces were not ended by equal
						// meaning the attribute was a stand alone attribute
						// now: create the stand alone attribute and rewind
						// the cursor to the end of the white spaces
						// and restart scanning as whitespace attribute.
						standalone(attributes, bookmarks);
						bookmarks[0] = bookmarks[6];
						--fScannerPosition;
						state = 0;
					}
					break;
				default:
					throw new IllegalStateException("how did we get in state " + state);
				}
			}

			return (makeTag(start, fScannerPosition, attributes));
		} catch (IndexOutOfBoundsException e) {

			if (state == 3) {
				// within naked attribute value
				naked(attributes, bookmarks);
			}
		}
		return null;
	}

	protected List<NodeAttribute> parseAttributes(int start, int end) {
		boolean done;
		char ch;
		int state;
		int[] bookmarks;

		done = false;
		ArrayList<NodeAttribute> attributes = new ArrayList<NodeAttribute>();
		state = 0;
		fScannerPosition = start;
		bookmarks = new int[8];
		bookmarks[0] = fScannerPosition;
		try {
			while (!done && fScannerPosition < end) {
				bookmarks[state + 1] = fScannerPosition;
				ch = fSource[fScannerPosition++];
				switch (state) {
				case 0: // outside of any attribute
					if ((EOF == ch) || ('>' == ch) || ('<' == ch)) {
						if ('<' == ch) {
							// don't consume the opening angle
							bookmarks[state + 1] = --fScannerPosition;
						}
						whitespace(attributes, bookmarks);
						done = true;
					} else if (!Character.isWhitespace(ch)) {
						whitespace(attributes, bookmarks);
						state = 1;
					}
					break;
				case 1: // within attribute name
					if ((EOF == ch) || ('>' == ch) || ('<' == ch)) {
						if ('<' == ch) {
							// don't consume the opening angle
							bookmarks[state + 1] = --fScannerPosition;
						}
						standalone(attributes, bookmarks);
						done = true;
					} else if (Character.isWhitespace(ch)) {
						// whitespaces might be followed by next attribute or an
						// equal sign
						// see Bug #891058 Bug in lexer.
						bookmarks[6] = bookmarks[2]; // setting the
						// bookmark[0]
						// is done in state 6 if
						// applicable
						state = 6;
					} else if ('=' == ch)
						state = 2;
					break;
				case 2: // equals hit
					if ((EOF == ch) || ('>' == ch)) {
						empty(attributes, bookmarks);
						done = true;
					} else if ('\'' == ch) {
						state = 4;
						bookmarks[4] = bookmarks[3];
					} else if ('"' == ch) {
						state = 5;
						bookmarks[5] = bookmarks[3];
					} else if (Character.isWhitespace(ch)) {
						// collect white spaces after "=" into the assignment
						// string;
						// do nothing
						// see Bug #891058 Bug in lexer.
					} else
						state = 3;
					break;
				case 3: // within naked attribute value
					if ((EOF == ch) || ('>' == ch)) {
						naked(attributes, bookmarks);
						done = true;
					} else if (Character.isWhitespace(ch)) {
						naked(attributes, bookmarks);
						bookmarks[0] = bookmarks[4];
						state = 0;
					}
					break;
				case 4: // within single quoted attribute value
					if (EOF == ch) {
						single_quote(attributes, bookmarks);
						done = true; // complain?
					} else if ('\'' == ch) {
						single_quote(attributes, bookmarks);
						bookmarks[0] = bookmarks[5] + 1;
						state = 0;
					}
					break;
				case 5: // within double quoted attribute value
					if (EOF == ch) {
						double_quote(attributes, bookmarks);
						done = true; // complain?
						// } else if ('\\' == ch && fSource[fScannerPosition] == '"') {
						// fScannerPosition++;
					} else if ('"' == ch) {
						double_quote(attributes, bookmarks);
						bookmarks[0] = bookmarks[6] + 1;
						state = 0;
					}
					break;
				// patch for lexer state correction by
				// Gernot Fricke
				// See Bug # 891058 Bug in lexer.
				case 6: // undecided for state 0 or 2
					// we have read white spaces after an attributte name
					if (EOF == ch) {
						// same as last else clause
						standalone(attributes, bookmarks);
						bookmarks[0] = bookmarks[6];
						// mPage.ungetCharacter(mCursor);
						--fScannerPosition;
						state = 0;
					} else if (Character.isWhitespace(ch)) {
						// proceed
					} else if ('=' == ch) // yepp. the white spaces belonged
					// to the equal.
					{
						bookmarks[2] = bookmarks[6];
						bookmarks[3] = bookmarks[7];
						state = 2;
					} else {
						// white spaces were not ended by equal
						// meaning the attribute was a stand alone attribute
						// now: create the stand alone attribute and rewind
						// the cursor to the end of the white spaces
						// and restart scanning as whitespace attribute.
						standalone(attributes, bookmarks);
						bookmarks[0] = bookmarks[6];
						--fScannerPosition;
						state = 0;
					}
					break;
				default:
					throw new IllegalStateException("how did we get in state " + state);
				}
			}
			if (state == 3 || state == 4 || state == 5) {
				// within naked attribute value
				bookmarks[state + 1] = fScannerPosition;
				naked(attributes, bookmarks);
			}
			return attributes;
		} catch (IndexOutOfBoundsException e) {

		}
		return null;
	}

	/**
	 * Create a tag node based on the current cursor and the one provided.
	 * 
	 * @param start
	 *          The starting point of the node.
	 * @param end
	 *          The ending point of the node.
	 * @param attributes
	 *          The attributes parsed from the tag.
	 * @exception ParserException
	 *              If the nodefactory creation of the tag node fails.
	 * @return The new Tag node.
	 */
	protected WikiTagNode makeTag(int start, int end, ArrayList<NodeAttribute> attributes) {
		int length;
		length = end - start;
		if (0 != length) { // return tag based on second character, '/', '%',
			// Letter (ch), '!'
			if (2 > length) {
				// this is an error
				return null; // (makeString(start, end));
			}
			// ret = getNodeFactory().createTagNode(this.getPage(), start, end,
			// attributes);
			return new WikiTagNode(start, end, attributes);
		}
		return null;
	}

	/**
	 * Generate a whitespace 'attribute',
	 * 
	 * @param attributes
	 *          The list so far.
	 * @param bookmarks
	 *          The array of positions.
	 */
	private void whitespace(ArrayList<NodeAttribute> attributes, int[] bookmarks) {
		// if (bookmarks[1] > bookmarks[0])
		// attributes.addElement(new PageAttribute(fSource,-1, -1, bookmarks[0],
		// bookmarks[1], (char) 0));
	}

	/**
	 * Generate a standalone attribute -- font.
	 * 
	 * @param attributes
	 *          The list so far.
	 * @param bookmarks
	 *          The array of positions.
	 */
	private void standalone(ArrayList<NodeAttribute> attributes, int[] bookmarks) {
		attributes.add(new NodeAttribute(fSource, bookmarks[1], bookmarks[2], -1, -1, (char) 0));
	}

	/**
	 * Generate an empty attribute -- color=.
	 * 
	 * @param attributes
	 *          The list so far.
	 * @param bookmarks
	 *          The array of positions.
	 */
	private void empty(ArrayList<NodeAttribute> attributes, int[] bookmarks) {
		attributes.add(new NodeAttribute(fSource, bookmarks[1], bookmarks[2], bookmarks[2] + 1, -1, (char) 0));
	}

	/**
	 * Generate an unquoted attribute -- size=1.
	 * 
	 * @param attributes
	 *          The list so far.
	 * @param bookmarks
	 *          The array of positions.
	 */
	private void naked(ArrayList<NodeAttribute> attributes, int[] bookmarks) {
		attributes.add(new NodeAttribute(fSource, bookmarks[1], bookmarks[2], bookmarks[3], bookmarks[4], (char) 0));
	}

	/**
	 * Generate an single quoted attribute -- width='100%'.
	 * 
	 * @param attributes
	 *          The list so far.
	 * @param bookmarks
	 *          The array of positions.
	 */
	private void single_quote(ArrayList<NodeAttribute> attributes, int[] bookmarks) {
		attributes.add(new NodeAttribute(fSource, bookmarks[1], bookmarks[2], bookmarks[4] + 1, bookmarks[5], '\''));
	}

	/**
	 * Generate an double quoted attribute -- CONTENT="Test Development".
	 * 
	 * @param attributes
	 *          The list so far.
	 * @param bookmarks
	 *          The array of positions.
	 */
	private void double_quote(ArrayList<NodeAttribute> attributes, int[] bookmarks) {
		attributes.add(new NodeAttribute(fSource, bookmarks[1], bookmarks[2], bookmarks[5] + 1, bookmarks[6], '"'));
	}

	protected int readSpecialWikiTags(int start) {
		try {
			if (fSource[start] != '/') {
				// starting tag
				WikiTagNode tagNode = parseTag(start);
				if (tagNode != null) {
					String tagName = tagNode.getTagName();
					if (tagName.equals("nowiki")) {
						return readUntilIgnoreCase(fScannerPosition, "</", "nowiki>");
					} else if (tagName.equals("source")) {
						return readUntilIgnoreCase(fScannerPosition, "</", "source>");
					} else if (tagName.equals("math")) {
						return readUntilIgnoreCase(fScannerPosition, "</", "math>");
					} else if (tagName.equals("span")) {
						return readUntilIgnoreCase(fScannerPosition, "</", "span>");
						// <div> could be nested ?
						// } else if (tagName.equals("div")) {
						// return readUntilIgnoreCase(fScannerPosition, "</", "div>");
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// do nothing
		}
		return -1;
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
	protected final int readUntilIgnoreCase(int start, String startString, String endString) {
		int index = Util.indexOfIgnoreCase(fStringSource, startString, endString, start);
		if (index != (-1)) {
			return index + startString.length() + endString.length();
		}
		return -1;
	}

	// protected final int readUntilIgnoreCase(Object processed, int start, String
	// startString, String endString) {
	// int index = Utils.indexOfIgnoreCase(fBMHR, processed, fStringSource,
	// startString, endString, start);
	// if (index != (-1)) {
	// return index + startString.length() + endString.length();
	// }
	// return -1;
	// }
}