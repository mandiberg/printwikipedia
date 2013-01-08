package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;
 
public class HTMLTableFilterTest extends FilterTestSupport {
  public HTMLTableFilterTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(HTMLTableFilterTest.class);
  }

  public void testHTMLTable2() {
  assertEquals("\n" + 
  		"<table>\ntest\n</table>", wikiModel.render("<table 250px>test</table>"));
  }
}