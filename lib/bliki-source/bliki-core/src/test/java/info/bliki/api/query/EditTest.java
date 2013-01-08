package info.bliki.api.query;

import info.bliki.api.Connector;
import info.bliki.api.UnexpectedAnswerException;
import info.bliki.api.User;

/**
 * Tests edit query.
 */
public class EditTest extends BaseQueryTest {

    public void testSmokeTest() {
        User user = getAnonymousUser();
        Connector connector = new Connector();
        connector.login(user);

        try {
            // there is no rights to do this action. The error must be received.
            connector.edit(user, Edit.create().title("MainTestTest").text("blabla"));
            fail("UnexpectedAnswerException must be thrown");
        } catch (UnexpectedAnswerException e) {
            // ok
        }
    }

}
