package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PreFormattedFilterTest extends FilterTestSupport
{
	public PreFormattedFilterTest(String name)
	{
		super(name);
	}

	public static Test suite()
	{
		return new TestSuite(PreFormattedFilterTest.class);
	}

	public void testPreformattedInput1()
	{
		assertEquals("\n" + 
				"<pre>\nThis is some\n" + 
				"Preformatted text\n" + 
				"With <i>italic</i>\n" + 
				"And <b>bold</b>\n" + 
				"And a <a href=\"http://www.bliki.info/wiki/Main_Page\" title=\"Main Page\">link</a>\n</pre>", wikiModel.render(
				" This is some\n" + " Preformatted text\n" + " With \'\'italic\'\'\n" + " And \'\'\'bold\'\'\'\n"
						+ " And a [[Main Page|link]]"));
	}

}