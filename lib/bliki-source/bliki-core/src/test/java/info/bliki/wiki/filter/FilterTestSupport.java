package info.bliki.wiki.filter;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;

import java.util.Locale;

import junit.framework.TestCase;

/**
 * Support class for defining JUnit FilterTests.
 * 
 */
public class FilterTestSupport extends TestCase {
	public static final String WINDOWS_NEWLINE = "\r\n";

	public static final String UNIX_NEWLINE = "\n";

	public static final String NEWLINE = WINDOWS_NEWLINE;

	protected WikiModel wikiModel = null;
	
	public FilterTestSupport(String s) {
		super(s);
	}

	/**
	 * Set up a test model, which contains predefined templates
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		wikiModel = new WikiTestModel(Locale.ENGLISH,"http://www.bliki.info/wiki/${image}", "http://www.bliki.info/wiki/${title}");
		wikiModel.setUp();
	}

	/**
	 * simple example
	 */
	public static void main(String[] args) {
		WikiModel wikiModel = new WikiModel(Configuration.DEFAULT_CONFIGURATION,Locale.GERMAN,"http://www.bliki.info/wiki/${image}", "http://www.bliki.info/wiki/${title}");
		String htmlStr = wikiModel.render("This is a simple [[Hello World]] wiki tag");
		System.out.print(htmlStr);
	}
}
