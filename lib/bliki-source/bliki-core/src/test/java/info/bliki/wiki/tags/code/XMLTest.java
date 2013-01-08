package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import junit.framework.Test;
import junit.framework.TestSuite;


public class XMLTest extends FilterTestSupport
{
	public XMLTest(String name)
	{
		super(name);
	}

	public static Test suite()
	{
		return new TestSuite(XMLTest.class);
	}

	public void testXml()
	{
		String result = wikiModel.render("\'\'\'XML example:\'\'\'\n" + "<source lang=xml>\n" + "  <extension\n"
				+ "           point=\"org.eclipse.help.toc\">\n" + "        <toc\n" + "              file=\"phphelp.xml\"\n"
				+ "              primary=\"true\">\n" + "     <!-- simple comment -->                      \n" + "        </toc>\n"
				+ "  </extension>\n" + "</source>");

		assertEquals(
				"\n" + 
				"<p><b>XML example:</b>\n" + 
				"</p><pre class=\"code\">\n" + 
				"  <b><span style=\"color:#7F0055; font-weight: bold; \">&#60;extension</span></b>\n" + 
				"           point=<span style=\"color:#2A00FF; \">&#34;org.eclipse.help.toc&#34;</span><span style=\"color:#7F0055; font-weight: bold; \">&#62;</span>\n" + 
				"        <b><span style=\"color:#7F0055; font-weight: bold; \">&#60;toc</span></b>\n" + 
				"              file=<span style=\"color:#2A00FF; \">&#34;phphelp.xml&#34;</span>\n" + 
				"              primary=<span style=\"color:#2A00FF; \">&#34;true&#34;</span><span style=\"color:#7F0055; font-weight: bold; \">&#62;</span>\n" + 
				"     <span style=\"color:#3F7F5F; \">&#60;-- &#60;simple comment --&#62;</span>                      \n" + 
				"        <b><span style=\"color:#7F0055; font-weight: bold; \">&#60;/toc&#62;</span></b>\n" + 
				"  <b><span style=\"color:#7F0055; font-weight: bold; \">&#60;/extension&#62;</span></b>\n" + 
				"</pre>", result);
	}

}
