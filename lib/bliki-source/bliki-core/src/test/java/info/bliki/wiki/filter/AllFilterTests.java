package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllFilterTests extends TestCase {
	public AllFilterTests(String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite s = new TestSuite();

		s.addTestSuite(BasicFilterTest.class);
		s.addTestSuite(BBCodeFilterTest.class);
		s.addTestSuite(BoldFilterTest.class);
		s.addTestSuite(DefinitionListFilterTest.class);
		s.addTestSuite(CiteFilterTest.class);
		s.addTestSuite(DivFilterTest.class);
//		s.addTestSuite(EmbedFilterTest.class);
		s.addTestSuite(EntityFilterTest.class);
		s.addTestSuite(FontFilterTest.class);
		s.addTestSuite(HeaderFilterTest.class);
		s.addTestSuite(HRBRTest.class);
		s.addTestSuite(HTMLTableFilterTest.class);
		s.addTestSuite(HTTPUrlFilterTest.class);
		s.addTestSuite(ItalicFilterTest.class);
		s.addTestSuite(MathFilterTest.class);
		s.addTestSuite(PreFilterTest.class);
		s.addTestSuite(PreFormattedFilterTest.class);
		s.addTestSuite(RefFilterTest.class);
		s.addTestSuite(TagFilterTest.class);
		s.addTestSuite(TemplateFilterTest.class);
		s.addTestSuite(TOCFilterTest.class);
		s.addTestSuite(WPImageFilterTest.class);
		s.addTestSuite(WPLinkFilterTest.class);
		s.addTestSuite(WPListFilterTest.class);
		s.addTestSuite(WPTableFilterTest.class);
		s.addTestSuite(WrongTagFilterTest.class);
		s.addTestSuite(WPSemanticLinkTest.class);
		s.addTestSuite(TemplateParserTest.class);
		return s;
	}

}
