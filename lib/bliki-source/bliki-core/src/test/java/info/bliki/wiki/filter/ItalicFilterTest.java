package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;
 
public class ItalicFilterTest extends FilterTestSupport {
  public ItalicFilterTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(ItalicFilterTest.class);
  }
  
  
  public void testEM() {
	    assertEquals("\n" + 
	    		"<p>a <em> project </em>.</p>", wikiModel.render("a <em> project </em>."));
	  }
  
  public void testItalic1() {
    assertEquals("\n" + 
    		"<p><i>Text</i></p>", wikiModel.render("''Text''"));
  }

  public void testItalicWithPunctuation() {
    assertEquals("\n" + 
    		"<p><i>Text</i>:</p>", wikiModel.render("''Text'':"));
  }
}
