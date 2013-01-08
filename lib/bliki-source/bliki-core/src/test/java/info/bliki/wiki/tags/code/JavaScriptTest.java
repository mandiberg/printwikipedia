package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import junit.framework.Test;
import junit.framework.TestSuite;


public class JavaScriptTest extends FilterTestSupport
{
	public JavaScriptTest(String name)
	{
		super(name);
	}

	public static Test suite()
	{
		return new TestSuite(JavaScriptTest.class);
	}
 
	public void testJavaScript()
	{
		String result = wikiModel.render("'''JavaScript Example'''\n" + "<source lang=javascript>\n" + "public class Test {\n" + "< > \" \' &"
				+ "}\n" + "</source>");

		assertEquals("\n" + 
				"<p><b>JavaScript Example</b>\n" + 
				"</p><pre class=\"code\">\n" + 
				"<span style=\"color:#7F0055; font-weight: bold; \">public</span> <span style=\"color:#7F0055; font-weight: bold; \">class</span> Test {\n" + 
				"&#60; &#62; <span style=\"color:#2A00FF; \">&#34; &#39; &#38;}\n" + 
				"</span></pre>", result);
	}
}
