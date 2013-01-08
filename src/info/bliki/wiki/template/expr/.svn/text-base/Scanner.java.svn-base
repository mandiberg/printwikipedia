package info.bliki.wiki.template.expr;

import info.bliki.wiki.template.expr.ast.IParserFactory;

import java.util.List;

public class Scanner {
	/**
	 * Current parser input string
	 */
	protected String fInputString;

	/**
	 * Current input character
	 */
	protected char fCurrentChar;

	/**
	 * The position of the current character in the input string
	 */
	protected int fCurrentPosition;

	/**
	 * Current input token
	 */
	protected int fToken;

	/**
	 * The last determined operator string
	 */
	protected String fOperatorString;

	/**
	 * protected List<Operator> fOperList;
	 */
	protected List fOperList;

	/**
	 * Row counter for syntax errors.
	 */
	protected int rowCount;

	/**
	 * Column counter for syntax errors
	 */
	protected int fCurrentColumnStartPosition;

	/**
	 * Token type: End-of_File
	 */
	final static public int TT_EOF = 0;

	/**
	 * Token type: floating point number
	 */
	final static public int TT_FLOATING_POINT = 10;

	/**
	 * Token type: opening bracket '(' for sub-formulas with higher precedence
	 */
	final static public int TT_PRECEDENCE_OPEN = 14;

	/**
	 * Token type: closing bracket ')' for sub-formulas with higher precedence
	 */
	final static public int TT_PRECEDENCE_CLOSE = 15;

	/**
	 * Token type: constant found in input string
	 */
	final static public int TT_CONSTANT = 20;

	/**
	 * Token type: operator found in input string
	 */
	final static public int TT_OPERATOR = 30;

	/**
	 * Token type: string surrounded by &quot;....&quot;
	 */
	// final static public int TT_STRING = 136;
	/**
	 * Token type: digit 0,1,2,3,4,5,6,7,8,9
	 */
	final static public int TT_DIGIT = 139;

	protected int numFormat = 0;

	protected IParserFactory fFactory;

	/**
	 * Initialize Scanner without a math-expression
	 * 
	 */
	public Scanner() {
		initializeNullScanner();
	}

	protected void initialize(final String s) throws SyntaxError {
		initializeNullScanner();
		fInputString = s;
		if (s != null) {
			getNextToken();
		}
	}

	private void initializeNullScanner() {
		fInputString = null;
		fToken = TT_EOF;
		fCurrentPosition = 0;
		rowCount = 0;
		fCurrentColumnStartPosition = 0;
	}

	/**
	 * get the next Character from the input string
	 * 
	 */
	private void getChar() {
		if (fInputString.length() > fCurrentPosition) {
			fCurrentChar = fInputString.charAt(fCurrentPosition++);
			return;
		}

		fCurrentPosition = fInputString.length() + 1;
		fCurrentChar = ' ';
		fToken = TT_EOF;
	}

	protected List getOperator() {
		final int startPosition = fCurrentPosition - 1;
		fOperatorString = fInputString.substring(startPosition, fCurrentPosition);
		List list = fFactory.getOperatorList(fOperatorString);
		List lastList = null;
		int lastOperatorPosition = -1;
		if (list != null) {
			lastList = list;
			lastOperatorPosition = fCurrentPosition;
		}
		getChar();
		while (fFactory.getOperatorCharacters().indexOf(fCurrentChar) >= 0) {
			fOperatorString = fInputString.substring(startPosition, fCurrentPosition);
			list = fFactory.getOperatorList(fOperatorString);
			if (list != null) {
				lastList = list;
				lastOperatorPosition = fCurrentPosition;
			}
			getChar();
		}
		if (lastOperatorPosition > 0) {
			fCurrentPosition = lastOperatorPosition;
			return lastList;
		}
		final int endPosition = fCurrentPosition--;
		fCurrentPosition = startPosition;
		throwSyntaxError("Operator token not found: " + fInputString.substring(startPosition, endPosition - 1));
		return null;
	}

	/**
	 * Get the next token from the input string
	 */
	protected void getNextToken() throws SyntaxError {

		while (fInputString.length() > fCurrentPosition) {
			fCurrentChar = fInputString.charAt(fCurrentPosition++);
			fToken = TT_EOF;
			if (fFactory.getOperatorCharacters().indexOf(fCurrentChar) >= 0) {
				fOperList = getOperator();
				fToken = TT_OPERATOR;
				return;
			}
			if (((fCurrentChar >= 'a') && (fCurrentChar <= 'z')) || ((fCurrentChar >= 'A') && (fCurrentChar <= 'Z'))) {
				String ident = getIdentifier().toLowerCase();
				fOperList = fFactory.getOperatorList(ident);
				if (fOperList == null || fOperList.size() == 0) {
					String constant = fFactory.getConstantSymbol(ident);
					if (constant != null) {
						fOperatorString = constant;
						fToken = TT_CONSTANT;
						return;
					}
					throwSyntaxError("unexpected operator: '" + ident + "'");
				}
				fOperatorString = ident;
				fToken = TT_OPERATOR;
				return;
			}
			if ((fCurrentChar != '\t') && (fCurrentChar != '\r') && (fCurrentChar != ' ')) {
				if (fCurrentChar == '\n') {
					rowCount++;
					fCurrentColumnStartPosition = fCurrentPosition;
					continue; // while loop
				}
				if ((fCurrentChar >= '0') && (fCurrentChar <= '9')) {
					fToken = TT_DIGIT;

					return;
				}
				switch (fCurrentChar) {

				case '(':
					fToken = TT_PRECEDENCE_OPEN;

					break;
				case ')':
					fToken = TT_PRECEDENCE_CLOSE;

					break;

				case '.':
					// token = TT_DOT;
					if (fInputString.length() > fCurrentPosition) {
						if ((fInputString.charAt(fCurrentPosition) >= '0') && (fInputString.charAt(fCurrentPosition) <= '9')) {
							// don't increment fCurrentPosition (see
							// getNumberString())
							// fCurrentPosition++;
							fToken = TT_DIGIT; // floating-point number
							break;
						}
					}

					break;
				// case '"':
				// fToken = TT_STRING;
				//
				// break;
				default:
					throwSyntaxError("Unrecognised punctuation character:\"" + fCurrentChar + "\"");
				}

				if (fToken == TT_EOF) {
					throwSyntaxError("token not found");
				}

				return;
			}
		}

		fCurrentPosition = fInputString.length() + 1;
		fCurrentChar = ' ';
		fToken = TT_EOF;
	}

	protected void throwSyntaxError(final String error) throws SyntaxError {
		throw new SyntaxError(fCurrentPosition - 1, rowCount, fCurrentPosition - fCurrentColumnStartPosition, getErrorLine(), error, 1);
	}

	protected void throwSyntaxError(final String error, final int errorLength) throws SyntaxError {
		throw new SyntaxError(fCurrentPosition - errorLength, rowCount, fCurrentPosition - fCurrentColumnStartPosition, getErrorLine(),
				error, errorLength);
	}

	private String getErrorLine() {
		if (fInputString.length() < fCurrentPosition) {
			fCurrentPosition--;
		}
		// read until end-of-line after the current fError
		int eol = fCurrentPosition;
		while (fInputString.length() > eol) {
			fCurrentChar = fInputString.charAt(eol++);
			if (fCurrentChar == '\n') {
				eol--;
				break;
			}
		}
		final String line = fInputString.substring(fCurrentColumnStartPosition, eol);
		return line;
	}

	protected String getIdentifier() {
		final int startPosition = fCurrentPosition - 1;
		getChar();
		while (((fCurrentChar >= 'a') && (fCurrentChar <= 'z')) || ((fCurrentChar >= 'A') && (fCurrentChar <= 'Z'))) {
			getChar();
		}

		int endPosition = fCurrentPosition--;
		final int length = (--endPosition) - startPosition;

		return fInputString.substring(startPosition, endPosition);
	}

	protected Object[] getNumberString() {
		final Object[] result = new Object[2];
		numFormat = 10;
		int startPosition = fCurrentPosition - 1;
		final char firstCh = fCurrentChar;
		char dFlag = ' ';
		// first digit
		if (fCurrentChar == '.') {
			dFlag = fCurrentChar;
		}
		getChar();
		if (firstCh == '0') {
			switch (fCurrentChar) {
			case 'b': // binary format
				numFormat = 2;
				startPosition = fCurrentPosition;
				getChar();
				break;
			case 'B': // binary format
				numFormat = 2;
				startPosition = fCurrentPosition;
				getChar();
				break;
			case 'o': // octal format
				numFormat = 8;
				startPosition = fCurrentPosition;
				getChar();
				break;
			case 'O': // octal format
				numFormat = 8;
				startPosition = fCurrentPosition;
				getChar();
				break;
			case 'x': // hexadecimal format
				numFormat = 16;
				startPosition = fCurrentPosition;
				getChar();
				break;
			case 'X': // hexadecimal format
				numFormat = 16;
				startPosition = fCurrentPosition;
				getChar();
				break;
			}
		}

		if (numFormat == 2) {
			while ((fCurrentChar >= '0') && (fCurrentChar <= '1')) {
				getChar();
			}
		} else if (numFormat == 8) {
			while ((fCurrentChar >= '0') && (fCurrentChar <= '7')) {
				getChar();
			}
		} else if (numFormat == 16) {
			while (((fCurrentChar >= '0') && (fCurrentChar <= '9')) || ((fCurrentChar >= 'a') && (fCurrentChar <= 'f'))
					|| ((fCurrentChar >= 'A') && (fCurrentChar <= 'F'))) {
				getChar();
			}
		} else {
			while (((fCurrentChar >= '0') && (fCurrentChar <= '9')) || (fCurrentChar == '.')) {
				// if ((ch == '.') || (ch == 'E') || (ch == 'e')) {
				if (fCurrentChar == '.') {
					if ((fCurrentChar == '.') && (dFlag != ' ')) {
						break;
					}
					dFlag = fCurrentChar;
					getChar();
				} else {
					getChar();
				}
			}
			if (dFlag != ' ') {
				numFormat = -1;
			}
		}
		if (numFormat < 0) {
			if ((fCurrentChar == 'E') || (fCurrentChar == 'e')) {
				getChar();
				if ((fCurrentChar == '+') || (fCurrentChar == '-')) {
					getChar();
				}
				while (((fCurrentChar >= '0') && (fCurrentChar <= '9'))) {
					getChar();
				}
			}
		}
		// }
		int endPosition = fCurrentPosition--;
		result[0] = fInputString.substring(startPosition, --endPosition);
		result[1] = Integer.valueOf(numFormat);
		return result;
	}

	protected StringBuffer getStringBuffer() throws SyntaxError {
		final StringBuffer ident = new StringBuffer();

		getChar();

		if ((fCurrentChar == '\n') || (fToken == TT_EOF)) {
			throwSyntaxError("string -" + ident.toString() + "- contains no character.");
		}

		while (fCurrentChar != '"') {
			if ((fCurrentChar == '\\')) {
				getChar();

				switch (fCurrentChar) {

				case '\\':
					ident.append(fCurrentChar);

					break;
				case 'n':
					ident.append("\n");

					break;
				case 't':
					ident.append("\t");

					break;
				default:
					throwSyntaxError("string - unknown character after back-slash.");
				}

				getChar();
			} else {
				if ((fCurrentChar != '"') && ((fCurrentChar == '\n') || (fToken == TT_EOF))) {
					throwSyntaxError("string -" + ident.toString() + "- not closed.");
				}

				ident.append(fCurrentChar);
				getChar();
			}
		}

		return ident;
	}
}
