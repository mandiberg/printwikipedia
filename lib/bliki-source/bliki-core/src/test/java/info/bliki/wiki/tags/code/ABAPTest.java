package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import junit.framework.Test;
import junit.framework.TestSuite;


public class ABAPTest extends FilterTestSupport
{
	public ABAPTest(String name)
	{
		super(name);
	}

	public static Test suite()
	{
		return new TestSuite(ABAPTest.class);
	} 

	public void testABAP()
	{
		String result = wikiModel.render("'''ABAP Example'''\n" + "<source lang=abap>\n" + "*--- line comment\n"
				+ "WRITE: / '''Hello World''' \"test comment\n" + "< > \" \' &" + "}\n" + "</source>");

		assertEquals(
				"\n" + 
				"<p><b>ABAP Example</b>\n" + 
				"</p><pre class=\"code\"><span style=\"color:#3F7F5F; \">\n" + 
				"*--- line comment\n" + 
				"</span><b><span style=\"color:#7F0055; font-weight: bold; \">WRITE</span></b>: / <span style=\"color:#2A00FF; \">&#39;&#39;&#39;Hello World&#39;&#39;&#39;</span> <span style=\"color:#3F7F5F; \">&#34;test comment\n" + 
				"</span>&#60; &#62; <span style=\"color:#3F7F5F; \">&#34; &#39; &#38;}\n" + 
				"</span></pre>", result);
	}

}
