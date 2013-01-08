package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class WrongTagFilterTest extends FilterTestSupport {
  public WrongTagFilterTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(WrongTagFilterTest.class);
  } 
  
//   public void testWrongTag0() {
//    assertEquals("<p>[[Dülmener Wildpferde im Merfelder Bruch]]\n" + 
//    		"</p>", wikiModel.render("[[Image:Merfelder_Wildpferde.jpg|thumb|right|250px|[[Dülmener Wildpferd]]e im Merfelder Bruch]]", wikiModel));
//  }
  public void testWrongTag1() {
    assertEquals("\n" + 
    		"<p>&#60;blubber&#62;...</p>", wikiModel.render("<blubber>..."));
  }
}