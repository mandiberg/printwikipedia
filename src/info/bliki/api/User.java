package info.bliki.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Manages user data from the <a
 * href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>.
 *
 * See also <a href="https://www.mediawiki.org/wiki/API:Login">Mediawiki
 * API:Login</a>
 */
public class User {
    protected static final String SUCCESS_ID = "Success";
    protected static final String NEED_TOKEN_ID = "NeedToken";
    private static final String ILLEGAL_ID = "Illegal";

    private String result;
    private String userId;

    private final String username;

    private String normalizedUsername;
    private String token;

    private final String password;
    private final String actionUrl;
    private final String domain;

    protected Connector connector;

    /**
     * Create a User for a Mediawiki wiki
     *
     * @param name User Name
     * @param password Password
     * @param mediawikiApiUrl A mediawiki API Url
     *                        (example: <a href="http://meta.wikimedia.org/w/api.php">http://meta.wikimedia.org/w/api.php</a>
     */
    public User(String name, String password, String mediawikiApiUrl) {
        this(name, password, mediawikiApiUrl, "");
    }

    /**
     * Create a User for a Mediawiki wiki
     *
     * @param name User Name
     * @param password Password
     * @param mediawikiApiUrl
     *          A mediawiki API Url (example: <a
     *          href="http://meta.wikimedia.org/w/api.php"
     *          >http://meta.wikimedia.org/w/api.php</a>
     * @param domain Domain (optional)
     */
    public User(String name, String password, String mediawikiApiUrl, String domain) {
        super();
        this.result = ILLEGAL_ID;
        this.userId = "";
        this.username = name;
        this.normalizedUsername = "";
        this.password = password;
        this.domain = domain;
        this.actionUrl = mediawikiApiUrl;
        this.connector = new Connector();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(normalizedUsername, user.normalizedUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(normalizedUsername);
    }

    /**
     * Complete the users login information. The user must contain a username,
     * password and actionURL. See <a
     * href="http://www.mediawiki.org/wiki/API:Login">Mediawiki API:Login</a>
     *
     * @return <code>true</code> if th login was successful; <code>false</code>
     *         otherwise.
     * @see User#getActionUrl()
     */
    public boolean login() {
        return connector.login(this) != null;
    }

    /**
     * Get the content of Mediawiki wiki pages.
     *
     * @param listOfTitleStrings
     *          a list of title Strings "ArticleA,ArticleB,..."
     * @return a list of downloaded Mediawiki pages.
     */
    public List<Page> queryContent(List<String> listOfTitleStrings) {
        return connector.queryContent(this, listOfTitleStrings);
    }

    /**
     * Get the content of Mediawiki wiki pages.
     *
     * @param listOfTitleStrings
     *          a list of title Strings "ArticleA,ArticleB,..."
     * @return a list of downloaded Mediawiki pages.
     */
    public List<Page> queryContent(String... listOfTitleStrings) {
        return queryContent(arrayToList(listOfTitleStrings));
    }

    public List<Page> queryCategories(List<String> listOfTitleStrings) {
        return connector.queryCategories(this, listOfTitleStrings);
    }

    public List<Page> queryCategories(String... listOfTitleStrings) {
        return queryCategories(arrayToList(listOfTitleStrings));
    }

    public List<Page> queryInfo(List<String> listOfTitleStrings) {
        return connector.queryInfo(this, listOfTitleStrings);
    }

    public List<Page> queryInfo(String... listOfTitleStrings) {
        return queryInfo(arrayToList(listOfTitleStrings));
    }

    public List<Page> queryLinks(List<String> listOfTitleStrings) {
        return connector.queryLinks(this, listOfTitleStrings);
    }

    public List<Page> queryLinks(String... listOfTitleStrings) {
        return queryLinks(arrayToList(listOfTitleStrings));
    }

    public List<Page> queryImageinfo(List<String> listOfImageStrings) {
        return connector.queryImageinfo(this, listOfImageStrings);
    }

    public List<Page> queryImageinfo(List<String> listOfImageStrings, int imageWidth) {
        return connector.queryImageinfo(this, listOfImageStrings, imageWidth);
    }

    public List<Page> queryImageinfo(String... listOfImageStrings) {
        return queryImageinfo(arrayToList(listOfImageStrings));
    }

    public List<Page> queryImageinfo(String[] listOfImageStrings, int imageWidth) {
        return queryImageinfo(arrayToList(listOfImageStrings), imageWidth);
    }

    public String getResult() {
        return result;
    }

    protected void setResult(String result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    protected void setToken(String token) {
        this.token = token;
    }

    public String getUserid() {
        return userId;
    }

    protected void setUserid(String userid) {
        this.userId = userid;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Get the Mediawiki API Url defined for this user(example:
     * <a href="http://meta.wikimedia.org/w/api.php" >http://meta.wikimedia.org/w/api.php</a>)
     */
    public String getActionUrl() {
        return actionUrl;
    }

    public String getDomain() {
        return domain;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAuthenticated() {
        return SUCCESS_ID.equals(result);
    }

    @Override
    public String toString() {
        return "Result: " + result + "; UserID: " + userId + "; UserName: " + username + "; NormalizedUsername: "
                + normalizedUsername + "; Token: " + token + "; ActionURL: " + actionUrl;
    }

    public String getNormalizedUsername() {
        return normalizedUsername;
    }

    protected void setNormalizedUsername(String normalizedUsername) {
        this.normalizedUsername = normalizedUsername;
    }

    private List<String> arrayToList(String[] listOfTitleStrings) {
        List<String> list = new ArrayList<>();
        if (listOfTitleStrings != null) {
            Collections.addAll(list, listOfTitleStrings);
        }
        return list;
    }
}
