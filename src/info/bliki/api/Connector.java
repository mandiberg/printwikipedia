package info.bliki.api;

import info.bliki.api.query.Query;
import info.bliki.api.query.RequestBuilder;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Manages the queries for the <a
 * href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>. See the <a
 * href="http://www.mediawiki.org/wiki/API:Main_page">API Documentation</a>.
 *
 * The queries set their own user-agent string. See <a
 * href="http://meta.wikimedia.org/wiki/User-Agent_policy">User-Agent policy</a>
 */
public class Connector {
    protected final static String USER_AGENT = "JavaWikipediaAPI/3.1-SNAPSHOT https://bitbucket.org/axelclk/info.bliki.wiki/";

    private final static String PARAM_LOGIN_USERNAME = "lgusername";
    private final static String PARAM_LOGIN_NAME     = "lgname";
    private final static String PARAM_LOGIN_USERID   = "lguserid";
    private final static String PARAM_LOGIN_PASSWORD = "lgpassword";
    private final static String PARAM_LOGIN_TOKEN    = "lgtoken";
    private final static String PARAM_LOGIN_DOMAIN   = "lgdomain";

    private final static String PARAM_FORMAT   = "format";
    private final static String PARAM_ACTION   = "action";
    private final static String PARAM_TITLES   = "titles";

    /**
     * See <a href="https://www.mediawiki.org/wiki/API:Query#Generators_and_continuation">Generators and continuation</a>.
     */
    private final static String PARAM_CONTINUE = "continue";

    private final static String ACTION_LOGIN = "login";
    private final static String ACTION_QUERY = "query";

    private final static String FORMAT_XML   = "xml";

    private static final String LINKS      = "links";
    private static final String IMAGEINFO  = "imageinfo";
    private static final String IIPROP     = "iiprop";
    private static final String URL        = "url";
    private static final String PROP       = "prop";
    private static final String IIURLWIDTH = "iiurlwidth";
    private static final String CATEGORIES = "categories";
    private static final String REVISIONS  = "revisions";
    private static final String RVPROP     = "rvprop";
    private static final String INFO       = "info";

    private HttpClient client;
    private Logger logger = LoggerFactory.getLogger(getClass());

    protected static HttpClientBuilder DEFAULT_HTTPCLIENT_BUILDER = HttpClientBuilder
        .create()
        .disableRedirectHandling()
        .setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()));

    public Connector() {
        this(DEFAULT_HTTPCLIENT_BUILDER);
    }

    public Connector(HttpClientBuilder builder) {
        client = builder.build();
    }

    /**
     * Complete the users login information. The user must contain a username,
     * password and actionURL. See <a
     * href="http://www.mediawiki.org/wiki/API:Login">Mediawiki API:Login</a>
     *
     * @param user a user account from a Mediawiki installation with filled out user
     *             name, password and the installations API url.
     * @return the completed user information or <code>null</code>, if the login fails
     */
    public User login(User user) {
        // The first pass gets the secret token and the second logs the user in
        for (int i = 0; i < 2; i++) {
            String userName = user.getUsername();

            if (userName == null || userName.trim().length() == 0) {
                // no nothing for dummy users
                return user;
            }

            HttpPost request = new HttpPost(user.getActionUrl());
            request.setHeader(HttpHeaders.USER_AGENT, USER_AGENT);
            String lgDomain = user.getDomain();
            List<NameValuePair> params = new ArrayList<>();
            params.addAll(Arrays.asList(
                new BasicNameValuePair(PARAM_ACTION, ACTION_LOGIN),
                new BasicNameValuePair(PARAM_FORMAT, FORMAT_XML),
                new BasicNameValuePair(PARAM_LOGIN_NAME, userName),
                new BasicNameValuePair(PARAM_LOGIN_PASSWORD, user.getPassword())
            ));

            if (lgDomain != null && lgDomain.length() > 0) {
                params.add(new BasicNameValuePair(PARAM_LOGIN_DOMAIN, lgDomain));
            }
            if (i > 0) {
                params.add(new BasicNameValuePair(PARAM_LOGIN_TOKEN, user.getToken()));
            }
            request.setEntity(new UrlEncodedFormEntity(params, (Charset) null));
            try {
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    final String responseBody = getAsXmlString(response);

                    XMLUserParser parser = new XMLUserParser(user, responseBody);
                    parser.parse();
                    if (i == 0 && User.NEED_TOKEN_ID.equals(user.getResult())) {
                        logger.debug("need_token_id");
                    } else if (User.SUCCESS_ID.equals(user.getResult())) {
                        return user;
                    } else {
                        break;
                    }
                }
            } catch (IOException | SAXException e) {
                logger.error(null, e);
            } finally {
                request.releaseConnection();
            }
        }
        logger.warn("could not log user in");
        return null;
    }

    /**
     * Get the content of Mediawiki wiki pages.
     *
     * @param user
     *          user login data
     * @param listOfTitleStrings
     *          a list of title Strings "ArticleA,ArticleB,..."
     * @return a list of downloaded Mediawiki pages.
     */
    public List<Page> queryContent(User user, List<String> listOfTitleStrings) {
        return query(user, listOfTitleStrings,
                PROP, REVISIONS, RVPROP, "timestamp|user|comment|content");
    }

    /**
     * List all categories the page(s) belong to.
     *
     * @param user
     *          user login data
     * @param listOfTitleStrings
     *          a list of title Strings "ArticleA,ArticleB,..."
     * @return page list
     */
    public List<Page> queryCategories(User user, List<String> listOfTitleStrings) {
        return query(user, listOfTitleStrings, PROP, CATEGORIES);
    }

    /**
     * Get basic page information such as namespace, title, last touched date, ..
     *
     * @param user
     *          user login data
     * @param listOfTitleStrings
     *          a list of title Strings "ArticleA,ArticleB,..."
     * @return page list
     */
    public List<Page> queryInfo(User user, List<String> listOfTitleStrings) {
        return query(user, listOfTitleStrings, PROP, INFO);
    }

    /**
     * Returns all links from the given page(s).
     *
     * @param user
     *          user login data
     * @param listOfTitleStrings
     *          a list of title Strings "ArticleA,ArticleB,..."
     * @return page list
     */
    public List<Page> queryLinks(User user, List<String> listOfTitleStrings) {
        return query(user, listOfTitleStrings, PROP, LINKS);
    }

    /**
     * Returns image information and upload history. Only a URL to an unscaled
     * image will be returned in the page data. Use
     * {@link #queryImageinfo(User, List, int)} if you need additional information
     * about the URL of the scaled image.
     *
     * @param user
     *          user login data
     * @param listOfImageStrings
     *          a list of title Strings "ArticleA,ArticleB,..."
     * @return page list
     */
    public List<Page> queryImageinfo(User user, List<String> listOfImageStrings) {
        return query(user, listOfImageStrings, PROP, IMAGEINFO, IIPROP, URL);
    }

    /**
     * Returns image information and upload history
     *
     * @param user
     *          user login data
     * @param listOfImageStrings
     *          list of image title strings
     * @param imageWidth
     *          a URL to an image scaled to this width will be returned. Only the
     *          current version of the image can be scaled.
     * @return page list
     */
    public List<Page> queryImageinfo(User user, List<String> listOfImageStrings, int imageWidth) {
        return query(user, listOfImageStrings, PROP, IMAGEINFO, IIPROP, URL, IIURLWIDTH,
                Integer.toString(imageWidth));
    }

    /**
     * Query the Mediawiki API for some wiki pages.
     *
     * @param user
     *          user login data
     * @param query
     *          a user defined query
     * @return page list
     */
    public List<Page> query(User user, Query query) {
        String response = sendXML(user, query);
        try {
            return parsePageBody(response).getPagesList();
        } catch (SAXException | IOException e) {
            logger.error(null, e);
        }
        return null;
    }

    /**
     * Query the Mediawiki API for some wiki pages.
     *
     * @param user
     *          user login data
     * @param listOfTitleStrings
     *          a list of title Strings "ArticleA,ArticleB,..."
     * @param valuePairs
     *          pairs of query strings which should be appended to the Mediawiki
     *          API URL
     * @return page list
     */
    private List<Page> query(User user, List<String> listOfTitleStrings, String... valuePairs) {
        try {
            String responseBody = queryXML(user, listOfTitleStrings, valuePairs);
            if (responseBody != null) {
                return parsePageBody(responseBody).getPagesList();
            }
        } catch (IOException | SAXException e) {
            logger.error(null, e);
        }
        // no pages parsed!?
        return new ArrayList<>();
    }

    private String queryXML(User user, List<String> listOfTitleStrings, String[] valuePairs) {
        String titlesString = formatTitleString(listOfTitleStrings);
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(PARAM_ACTION, ACTION_QUERY));
        parameters.add(new BasicNameValuePair(PARAM_CONTINUE, ""));

        if (titlesString.length() > 0) {
            // don't encode the title for the NameValuePair !
            parameters.add(new BasicNameValuePair(PARAM_TITLES, titlesString));
        }
        if (valuePairs != null && valuePairs.length > 0) {
            for (int i = 0; i < valuePairs.length; i += 2) {
                parameters.add(new BasicNameValuePair(valuePairs[i], valuePairs[i + 1]));
            }
        }
        return executeHttpMethod(
                createAuthenticatedRequest(user, parameters.toArray(new NameValuePair[parameters.size()])));
    }

    private String formatTitleString(List<String> titles) {
        StringBuilder titlesString = new StringBuilder();
        for (int i = 0; i < titles.size(); i++) {
            titlesString.append(titles.get(i));
            if (i < titles.size() - 1) {
                titlesString.append("|");
            }
        }
        return titlesString.toString();
    }

    private String sendXML(User user, RequestBuilder requestBuilder) {
        return executeHttpMethod(createAuthenticatedRequest(user, requestBuilder.getParameters()));
    }

    private HttpRequestBase createAuthenticatedRequest(User user, NameValuePair[] parameters) {
        final String actionUrl = user.getActionUrl();

        if (actionUrl == null || actionUrl.trim().length() == 0) {
            throw new IllegalArgumentException("no action url");
        }
        List<NameValuePair> parameterList = new ArrayList<>();
        parameterList.add(new BasicNameValuePair(PARAM_FORMAT, FORMAT_XML));
        Collections.addAll(parameterList, parameters);

        if (user.isAuthenticated()) {
            // TODO is this really correct?
            parameterList.addAll(Arrays.asList(
                new BasicNameValuePair(PARAM_LOGIN_USERNAME, user.getUserid()),
                new BasicNameValuePair(PARAM_LOGIN_USERID, user.getNormalizedUsername()),
                new BasicNameValuePair(PARAM_LOGIN_TOKEN, user.getToken())
            ));
        }

        URIBuilder uriBuilder = new URIBuilder(URI.create(user.getActionUrl()));
        HttpGet request = new HttpGet(uriBuilder.addParameters(parameterList).toString());
        request.setHeader(HTTP.USER_AGENT, USER_AGENT);
        return request;
    }

    private static String getAsXmlString(HttpResponse response) throws IOException {
        ContentType type = ContentType.get(response.getEntity());
        if (!type.getMimeType().startsWith("text/xml")) {
            throw new IOException("Invalid content-type: "+type);
        }

        String responseBody = EntityUtils.toString(response.getEntity());
        if (responseBody.length() > 0 && responseBody.charAt(0) != '<') {
            // try to find XML.
            int index = responseBody.indexOf("<?xml");
            if (index > 0) {
                responseBody = responseBody.substring(index);
            }
        }
        return responseBody;
    }

    private String executeHttpMethod(HttpRequestBase request) {
        try {
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return getAsXmlString(response);
            }
        } catch (IOException e) {
            logger.error("error fetching data", e);
        } finally {
            request.reset();
        }
        return null;
    }

    private XMLPagesParser parsePageBody(String responseBody) throws SAXException, IOException {
        XMLPagesParser parser = new XMLPagesParser(responseBody);
        parser.parse();
        List<String> warnings = parser.getWarnings();
        if (!warnings.isEmpty()) {
            logger.warn("parser warnings: "+warnings);
        }
        return parser;
    }
}
