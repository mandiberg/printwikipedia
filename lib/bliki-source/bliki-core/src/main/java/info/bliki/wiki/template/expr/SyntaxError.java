package info.bliki.wiki.template.expr;

/**
 * Exception for a syntax error detected by the parser for a Mediawiki template
 * expression.
 * 
 */
public class SyntaxError extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1849387697719679119L;

	/**
	 * offset where the error occurred
	 */
	int fStartOffset;

	/**
	 * row index where the error occurred2
	 */
	int fRowIndex;

	/**
	 * column index where the error occurred (offset relative to rowIndex)
	 */
	int fColumnIndex;

	/**
	 * length of the error
	 */
	int fLength;

	String fCurrentLine;

	String fError;

	/**
	 * SyntaxError exception
	 * 
	 * @param startOffset
	 * @param length
	 * 
	 * @see
	 */
	public SyntaxError(final int startOffset, final int rowIndx, final int columnIndx, final String currentLine, final String error,
			final int length) {
		fStartOffset = startOffset;
		fRowIndex = rowIndx;
		fColumnIndex = columnIndx;
		fCurrentLine = currentLine;
		fError = error;
		fLength = length;
	}

	public String getMessage() {
		final StringBuffer buf = new StringBuffer(256);
		buf.append("Syntax error in line: ");
		buf.append(fRowIndex + 1);
		buf.append(" - " + fError + "\n");
		buf.append(fCurrentLine + "\n");
		for (int i = 0; i < (fColumnIndex - 1); i++) {
			buf.append(' ');
		}
		buf.append('^');
		return buf.toString();
	}

	/**
	 * offset where the error occurred
	 */
	public int getStartOffset() {
		return fStartOffset;
	}

	/**
	 * column index where the error occurred (offset relative to rowIndex)
	 */
	public int getColumnIndex() {
		return fColumnIndex;
	}

	/**
	 * source code line, where the error occurred
	 */
	public String getCurrentLine() {
		return fCurrentLine;
	}

	/**
	 * the error string
	 */
	public String getError() {
		return fError;
	}

	/**
	 * length of the error
	 */
	public int getLength() {
		return fLength;
	}

	/**
	 * row index where the error occurred
	 */
	public int getRowIndex() {
		return fRowIndex;
	}
}
