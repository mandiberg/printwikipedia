package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import junit.framework.Test;
import junit.framework.TestSuite;

public class JavaTest extends FilterTestSupport {
	public JavaTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(JavaTest.class);
	}

	public void testJava001() {
		String result = wikiModel.render("'''Java Example'''\n" + "<source lang=java>\n" + "public class Test {\n" + "< > \" \' &"
				+ "}\n" + "</source>");
		assertEquals("\n" + 
				"<p><b>Java Example</b>\n" + 
				"</p><pre class=\"code\">\n" + 
				"<span style=\"color:#7F0055; font-weight: bold; \">public</span> <span style=\"color:#7F0055; font-weight: bold; \">class</span> Test {\n" + 
				"&#60; &#62; <span style=\"color:#2A00FF; \">&#34; &#39; &#38;}\n" + 
				"</span></pre>", result);
	}

	public void testJava002() {
		String result = wikiModel.render("'''Java Example'''\n" + "<source lang=java>"
				+ "Util util = new Util(\"c:\\\\temp\\\\\");\n" +"util.doIt();"+ "</source>");
		
		assertEquals("\n" + 
				"<p><b>Java Example</b>\n" + 
				"</p><pre class=\"code\">Util util = <span style=\"color:#7F0055; font-weight: bold; \">new</span> Util(<span style=\"color:#2A00FF; \">&#34;c:\\\\temp\\\\&#34;</span>);\n" + 
				"util.doIt();</pre>", result);
	}
}
