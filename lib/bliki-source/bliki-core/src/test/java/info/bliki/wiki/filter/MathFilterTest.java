package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class MathFilterTest extends FilterTestSupport {
  public MathFilterTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(MathFilterTest.class);
  }
  
  public void testMath() {
		assertEquals("\n" + 
				"<p><span class=\"math\">\\sin x</span></p>", wikiModel.render("<math>\\sin x</math>"));
	}
  
  public void testMath0() {
    assertEquals("\n" + 
    		"<p><span class=\"math\">H(j\\omega)=A_h(\\omega)\\cdot e^{j\\phi_h(\\omega)}={1\\over{1+j\\omega t}}</span></p>", wikiModel.render("<math>H(j\\omega)=A_h(\\omega)\\cdot e^{j\\phi_h(\\omega)}={1\\over{1+j\\omega t}}</math>"));
  }
}