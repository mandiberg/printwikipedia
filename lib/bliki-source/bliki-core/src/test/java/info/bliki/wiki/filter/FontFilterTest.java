package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class FontFilterTest extends FilterTestSupport {
  public FontFilterTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(FontFilterTest.class);
  }

  public void testFont1() {
    assertEquals("\n" + 
    		"<p><font color=\"red\">Text</font></p>", wikiModel.render("<font color=\"red\">Text</font>"));
  }

  public void testFont4() {
    assertEquals("\n" + 
    		"<p><font color=\"red\">Text</font></p>", wikiModel.render("<font color=red>Text</font>"));
  }
}