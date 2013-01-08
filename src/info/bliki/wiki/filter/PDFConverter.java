package info.bliki.wiki.filter;

/**
 * A converter which renders the internal node representation as HTML text with
 * &lt;a&gt; tags only rendered with the plain link title
 * 
 */
public class PDFConverter extends HTMLConverter {
	public PDFConverter() {
		super(true);
	}
}
