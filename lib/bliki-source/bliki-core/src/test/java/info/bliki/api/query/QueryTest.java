package info.bliki.api.query;


public class QueryTest extends BaseQueryTest {
	public QueryTest(String name) {
		super(name);
	}

	public void test001() {
		RequestBuilder request = Query.create().titles("Main Page", "API");
		assertEquals("action=query&amp;format=xml&amp;titles=Main Page|API", request.toString());
	}

	public void test002() {
		RequestBuilder request = Query.create().list("allpages").apfrom("Java").aplimit(20).format("json");
		assertEquals("action=query&amp;apfrom=20&amp;format=json&amp;list=allpages", request.toString());
	}
}
