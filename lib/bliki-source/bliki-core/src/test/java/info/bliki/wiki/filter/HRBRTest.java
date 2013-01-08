package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;
 
public class HRBRTest extends FilterTestSupport {
  public HRBRTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(HRBRTest.class);
  }
  
  public void testNewline0() {
    assertEquals("\n" + 
    		"<p>&#60;br ===</p>", wikiModel.render("<br ==="));
  }
  
  public void testNewline1() {
    assertEquals("\n" + 
    		"<p><br/></p>", wikiModel.render("<br>"));
  }
  public void testNewline2() {
    assertEquals("\n" + 
    		"<p><br/></p>", wikiModel.render("<br/>"));
  }
  public void testNewline3() {
    assertEquals("\n" + 
    		"<p><br/></p>", wikiModel.render("< br >"));
  }
  public void testNewline4() {
    assertEquals("\n" + 
    		"<p><br/></p>", wikiModel.render("< br / >"));
  }

  public void testHR01() {
    assertEquals("<hr/>\n" + 
    		"<p>test</p>", wikiModel.render("\n----\ntest"));
  }
  
  public void testHR02() {
    assertEquals("<blockquote><hr/>\n" + 
    		"<p>test\n" + 
    		"</p></blockquote>", wikiModel.render("\n<blockquote>\n----\ntest\n</blockquote>"));
  }
  
  public void testHR03() {
    assertEquals("<hr/>\n" + 
    		"<p>test</p>", wikiModel.render("\n------  \ntest"));
  }
  
  public void testHR04() {
    assertEquals("\n" + 
    		"<p>Start of text</p><hr/>  \n" + 
    		"<p>test</p>", wikiModel.render("Start of text<HR>  \ntest"));
  }
}