package info.bliki.api.query;

/**
 * Query for creation and editing pages.
 * This module requires read rights.
 * This module requires write rights.
 * This module only accepts POST requests.
 * Parameters:
 * title          - Page title
 * section        - Section number. 0 for the top section, 'new' for a new section
 * text           - Page content
 * token          - Edit token. You can get one of these through prop=info
 * summary        - Edit summary. Also section title when section=new
 * minor          - Minor edit
 * notminor       - Non-minor edit
 * bot            - Mark this edit as bot
 * basetimestamp  - Timestamp of the base revision (gotten through prop=revisions&rvprop=timestamp).
 *                  Used to detect edit conflicts; leave unset to ignore conflicts.
 * starttimestamp - Timestamp when you obtained the edit token.
 *                  Used to detect edit conflicts; leave unset to ignore conflicts.
 * recreate       - Override any errors about the article having been deleted in the meantime
 * createonly     - Don't edit the page if it exists already
 * nocreate       - Throw an error if the page doesn't exist
 * captchaword    - Answer to the CAPTCHA
 * captchaid      - CAPTCHA ID from previous request
 * watch          - Add the page to your watchlist
 * unwatch        - Remove the page from your watchlist
 * md5            - The MD5 hash of the text parameter, or the prependtext and appendtext parameters concatenated.
                   If set, the edit won't be done unless the hash is correct
 * prependtext    - Add this text to the beginning of the page. Overrides text.
                   Don't use together with section: that won't do what you expect.
 * appendtext     - Add this text to the end of the page. Overrides text
 * undo           - Undo this revision. Overrides text, prependtext and appendtext
 * undoafter      - Undo all revisions from undo to this one. If not set, just undo one revision
 * TODO: add suuport for remain parameters 
 */
public class Edit extends RequestBuilder{

    private Edit() {
        action("edit");
    }

    public static Edit create() {
        return new Edit();
    }

    public Edit title(String title) {
        put("title", title);
        return this;
    }

    public Edit section(int section) {
        put("section", String.valueOf(section));
        return this;
    }

    public Edit text(String text) {
        put("text", text);
        return this;
    }

    public Edit token(String token) {
        put("token", token);
        return this;
    }
}
