package info.bliki.api.query;

import junit.framework.TestCase;
import info.bliki.api.User;

/**
 * Base test case for all query test cases.
 */
public abstract class BaseQueryTest extends TestCase {

    public static final String DEFAULT_MEDIA_WIKI_API_URL = "http://meta.wikimedia.org/w/api.php";

    protected BaseQueryTest() {
    }

    protected BaseQueryTest(String name) {
        super(name);
    }

    protected User getAnonymousUser() {
        // no username and password
        return new User("", "", DEFAULT_MEDIA_WIKI_API_URL);
    }
}
