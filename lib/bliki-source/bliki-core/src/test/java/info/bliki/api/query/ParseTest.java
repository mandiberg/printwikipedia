package info.bliki.api.query;

import info.bliki.api.Connector;
import info.bliki.api.ParseData;
import info.bliki.api.User;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Tests Parse query.
 */
public class ParseTest extends BaseQueryTest {

	public void test001() {
		RequestBuilder request = Parse.create().text("{{Project:Sandbox}}").title("A Sandbox Template test");
		assertEquals("action=parse&amp;format=xml&amp;text={{Project:Sandbox}}&amp;title=A Sandbox Template test", request.toString());
	}

	public void testParseQuery() {

		User user = getAnonymousUser();
		Connector connector = new Connector();
		user = connector.login(user);
		System.out.println(user.getToken());
		RequestBuilder request = Parse.create().page("Main Page");
		ParseData parseData = connector.parse(user, request);
		assertNotNull(parseData);
		assertNotNull(parseData.getText());
		String html = StringEscapeUtils.unescapeHtml(parseData.getText());
		System.out.println("Retrieved html text:\n" + html);
	}
}
