package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import junit.framework.Test;
import junit.framework.TestSuite;


public class CSharpTest extends FilterTestSupport
{
	public CSharpTest(String name)
	{
		super(name);
	}

	public static Test suite()
	{
		return new TestSuite(CSharpTest.class);
	}

	public void testCSharp()
	{
		String result = wikiModel.render("'''C# Example'''\n" + "<source lang=csharp>\n" + "public class Test {\n" + "< > \" \' &" + "}\n"
				+ "</source>");

		assertEquals("\n" + 
				"<p><b>C# Example</b>\n" + 
				"</p><pre class=\"code\">\n" + 
				"<span style=\"color:#7F0055; font-weight: bold; \">public</span> <span style=\"color:#7F0055; font-weight: bold; \">class</span> Test {\n" + 
				"&#60; &#62; <span style=\"color:#2A00FF; \">&#34; &#39; &#38;}\n" + 
				"</span></pre>", result);
	}
}
