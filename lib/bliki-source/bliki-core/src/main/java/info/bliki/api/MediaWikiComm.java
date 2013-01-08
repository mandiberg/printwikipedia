package info.bliki.api;

import info.bliki.htmlcleaner.util.DivErrorboxExtractor;
import info.bliki.htmlcleaner.util.HtmlForm;
import info.bliki.htmlcleaner.util.HtmlFormExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.EncodingUtil;

/**
 * Sources adopted from the article <a
 * href="http://www.javaworld.com/javaworld/jw-08-2008/jw-08-java-wiki-extensions.html">Add
 * Java extensions to your wiki</a> with permission from Randall Scarberry.
 * 
 * @author R. Scarberry
 */
public class MediaWikiComm {

	// Names of form data elements in the HTML for the
	// MediaWiki edit tab. (Elements of the form element "editform".)
	private static final String WP_SECTION = "wpSection";

	private static final String WP_STARTTIME = "wpStarttime";

	private static final String WP_EDITTIME = "wpEdittime";

	private static final String WP_SCROLLTOP = "wpScrolltop";

	private static final String WP_TEXTBOX1 = "wpTextbox1";

	private static final String WP_SUMMARY = "wpSummary";

	private static final String WP_SAVE = "wpSave";

	private static final String WP_EDITTOKEN = "wpEditToken";

	private static final String WP_AUTOSUMMARY = "wpAutoSummary";

	// The page URL in the format
	// http://<hostname>/mediawiki/index.php?title=<pagetitle>
	private String fPageURL;

	// Cookies posted in the header of every request. Even-numbered elements are
	// the
	// cookie names, odd-numbered are the values. These are constructed from
	// the UserName, UserID, _session, and CookiePrefix parameters.
	private String[] fCookiePairs;

	// The contents of the form with the name/id "editform". Set by loadData(),
	// several
	// elements must be posted back to the server by saveData().
	private EditFormData fPageData;

	// Object for communication with the server via HTTP, reused in
	// every request.
	private HttpClient fHttpClient = new HttpClient();

	/**
	 * Constructor
	 * 
	 * @param pageURL
	 *          the page URL
	 * @param cookiePairs
	 *          cookies to be included in every server request
	 */
	public MediaWikiComm(String pageURL, String[] cookiePairs) {
		// Ensure required parameters are set.
		if (pageURL == null) {
			throw new NullPointerException();
		}

		fPageURL = pageURL;

		// Make a defensive copy of the cookie pairs.
		fCookiePairs = cookiePairs != null ? ((String[]) cookiePairs.clone()) : new String[0];
	}

	/**
	 * Method which loads downloads the data from the wiki page.
	 * 
	 * @return an instance of EditFormData, which encapsulates the important
	 *         elements of the page's editform.
	 * 
	 * @throws IOException
	 */
	public EditFormData loadEditFormData(String title) throws IOException {

		// Get the HTML from the page's edit tab.
		GetMethod method = new GetMethod(fPageURL + "?title=" + title + "&action=edit");
		int responseCode = HttpStatus.SC_OK;
		String responseBody = null;

		// Tell the HttpClient library not to worry about the cookies. We'll set
		// them manually.
		method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		// Get a string representation of the cookies.
		String cookies = getCookieString();
		if (cookies.length() > 0) {
			// Put the cookies in the request header.
			method.setRequestHeader("Cookie", cookies);
		}

		try {

			// Perform the get.
			responseCode = fHttpClient.executeMethod(method);

			// Returns 200 on success. But techically, anything in the 200s
			// is supposed to mean success.
			if (responseCode >= HttpStatus.SC_OK && responseCode < 300) {

				// Read the entire response and place it into responseBody.
				// This will be the HTML for the entire page. (The same as
				// what you see when you view source on the edit tab in the browser.)

				String charSet = method.getResponseCharSet();

				// method.getResponseBody() returns everything as byte[].
				// method.getResponseBodyAsString() returns everything as a String, but
				// the charSet might be ignored. So we'll use
				// method.getResponseBodyAsStream() and
				// read it line-by-line.
				BufferedReader br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charSet));

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				String line = null;
				while ((line = br.readLine()) != null) {
					pw.println(line);
				}
				pw.flush();

				responseBody = sw.toString();

			} else { // Bad response code -- return an IOException with the status.

				throw new IOException(method.getStatusText() + " (" + responseCode + ")");

			}

		} finally {

			// Always release the connect when done.
			method.releaseConnection();

		}

		// To have fallen through, the response code had to have been ok.
		// Now parse out the editform into the utility class HtmlForm.
		HtmlForm form = new HtmlForm("editform", HtmlForm.POST, "", "");
		// HtmlUtilities.extractForm("editform", responseBody);
		new HtmlFormExtractor(form).extractContent(responseBody);
		String empty = "";
		String startTime = empty, editTime = empty, editText = empty, autoSummary = empty, editToken = empty;

		if (form != null) {

			int sz = form.getElementCount();

			// Step through the form elements to find key items needed to
			// construct an instance of EditFormData.
			for (int i = 0; i < sz; i++) {

				HtmlForm.Element element = form.getElement(i);
				HtmlForm.ElementAttribute nameAttr = element.getElementAttributeByName("name");
				HtmlForm.ElementAttribute valueAttr = element.getElementAttributeByName("value");

				String name = nameAttr != null ? nameAttr.getValue() : null;

				try {

					if (WP_STARTTIME.equals(name)) {
						startTime = valueAttr.getValue();
					} else if (WP_EDITTIME.equals(name)) {
						editTime = valueAttr.getValue();
					} else if (WP_TEXTBOX1.equals(name)) {
						// This item contains what you see in the text area of the edit tab
						// of the browser.
						// The reason this is read line-by-line is to translate
						// carriage-return/linefeeds.
						BufferedReader br2 = new BufferedReader(new StringReader(element.getEmbeddedText()));
						StringWriter sw2 = new StringWriter();
						PrintWriter pw2 = new PrintWriter(sw2);
						String line2 = null;
						while ((line2 = br2.readLine()) != null) {
							pw2.println(line2);
						}
						pw2.close();
						try {
							br2.close();
						} catch (IOException wontHappen) {
						}
						// Set editText.
						editText = sw2.getBuffer().toString();
					} else if (WP_AUTOSUMMARY.equals(name)) {
						autoSummary = valueAttr.getValue();
					} else if (WP_EDITTOKEN.equals(name)) {
						// The edit token must be posted back to the server with saves
						// for them to work. The server maps the edit token to the session
						// in the request cookie.
						editToken = valueAttr.getValue();
					}
				} catch (RuntimeException rte) {
					rte.printStackTrace();
					// Just in case invalid form data results in a
					// NullPointerException, IllegalArgumentException, etc...
					throw new IOException("invalid editform data: " + rte.toString());
				}
			} // for (int i...

		} else { // form == null

			throw new IOException("no edit form on page");

		}

		// Return the data in an instance of EditFormData.
		return new EditFormData(startTime, editTime, editText, autoSummary, editToken);
	}

	/**
	 * Saves data back to the wiki page.
	 * 
	 * @param settings
	 *          an array of <code>ClusteringDemoSetting</code>s object, which
	 *          are converted into a block of XML and embedded on the page.
	 * 
	 * @throws IOException
	 */
	public void saveData(String title, String newEditText) throws IOException {
		if (newEditText == null) {
			// Bail out if the new data could not be generated.
			throw new IOException("could not generate updated page content");
		}

		fPageData = loadEditFormData(title);

		// Now post the data as multipart form data.
		PostMethod method = new PostMethod(fPageURL + "?title=" + title + "&action=submit");
		int responseCode = HttpStatus.SC_OK;

		// Add the cookies to the request header manually.
		method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		String cookies = getCookieString();
		if (cookies.length() > 0) {
			method.setRequestHeader("Cookie", cookies);
		}
		method.addRequestHeader("Content-Type", PostMethod.FORM_URL_ENCODED_CONTENT_TYPE + "; charset=" + Connector.UTF8_CHARSET);

		NameValuePair[] params = new NameValuePair[] {
				new NameValuePair(WP_SECTION, ""), new NameValuePair(WP_STARTTIME, fPageData.getStartTime()),
				new NameValuePair(WP_EDITTIME, fPageData.getEditTime()), new NameValuePair(WP_SCROLLTOP, ""),
				new NameValuePair(WP_TEXTBOX1, newEditText), new NameValuePair(WP_SUMMARY, ""), new NameValuePair(WP_SAVE, "Save page"),
				new NameValuePair(WP_EDITTOKEN, fPageData.getEditToken()), new NameValuePair(WP_AUTOSUMMARY, fPageData.getAutoSummary())
		};
		method.addParameters(params);

		try {

			// Execute the request.
			responseCode = fHttpClient.executeMethod(method);

			// This time, 302 means success. 302 is returned when the server
			// attempts a page redirection.
			// 200 means the save failed probably because another user posted some
			// edits since loadData() was called.
			if (responseCode >= HttpStatus.SC_OK && responseCode < 300) {
				// The calling code will respond by reloading the data, merging, and
				// attempting to save again.
				throw new ConcurrentEditException();
			} else if (responseCode != 302) {
				// Some other error code.
				throw new IOException(method.getStatusText() + " (" + responseCode + ")");
			}
		} finally {

			// Always release the connection.
			method.releaseConnection();
		}
	}

	/**
	 * Returns a cookie string suitable for placing in the Cookie request header.
	 * 
	 * @return
	 */
	private String getCookieString() {
		StringBuffer cookieBuf = new StringBuffer();
		int pairs = fCookiePairs != null ? fCookiePairs.length / 2 : 0;
		for (int i = 0; i < pairs; i += 2) {
			if (i > 0)
				cookieBuf.append("; ");
			cookieBuf.append(fCookiePairs[i]);
			cookieBuf.append('=');
			cookieBuf.append(fCookiePairs[i + 1]);
		}
		return cookieBuf.toString();
	}

	/**
	 * Log-in
	 * 
	 * @param loginUrl
	 * @param actionUrl
	 * @param user
	 * @param password
	 * @param remember
	 * @return <code>true</code> is the login succeeds
	 * @throws UnexpectedAnswerException
	 * @throws MethodException
	 */
	public boolean login(String user, String password, boolean remember) throws UnexpectedAnswerException, MethodException {
		// String url = loginUrl;
		// if (loginUrl != null && loginUrl.length() > 0) {
		// url = loginUrl;
		// }
		PostMethod method = new PostMethod(fPageURL);
		// String domain = "";
		// if (resource != null) {
		// domain = Util.getLDAPDomain(resource);
		// if (domain == null) {
		// domain = "";
		// }
		// }
		method.setFollowRedirects(false);
		method.addRequestHeader("User-Agent", Connector.USER_AGENT);
		NameValuePair[] params = new NameValuePair[] {
				new NameValuePair("title", "Special:Userlogin"), new NameValuePair("action", "submit"), new NameValuePair("wpName", user),
				new NameValuePair("wpPassword", password), new NameValuePair("wpRemember", remember ? "1" : "0"),
				// new NameValuePair("wpDomain", domain),
				new NameValuePair("wpLoginattempt", "submit")
		};
		method.addParameters(params);

		boolean result;
		try {
			int responseCode = fHttpClient.executeMethod(method);
			String responseBody = method.getResponseBodyAsString();

			if ((responseCode == 302 && responseBody.length() == 0)) {
				saveLoginData();

				result = true;
			} else if (responseCode == HttpStatus.SC_OK) {
				// && responseBody.matches(config.getLoginWrongPw())
				// || responseCode == 200
				// && responseBody.matches(config.getLoginNoUser())) {
				StringBuilder buff = new StringBuilder("Probably logout not successful: responseCode == 200: ");
				new DivErrorboxExtractor(buff).extractContent(responseBody);
				result = false;
				// if (responseBody.matches(config.getLoginNoUser())) {
				// throw new UnexpectedAnswerException(
				// "login not successful: wrong user name: " + user);
				// } else if (responseBody.matches(config.getLoginWrongPw())) {
				// throw new UnexpectedAnswerException(
				// "login not successful: wrong password for user: "
				// + user);
				// } else {
				throw new UnexpectedAnswerException(buff.toString());
				// }
			} else {
				throw new UnexpectedAnswerException("login not successful: " + method.getStatusLine());
			}
		} catch (HttpException e) {
			throw new MethodException("method failed", e);
		} catch (IOException e) {
			throw new MethodException("method failed", e);
		} finally {
			method.releaseConnection();
		}
		/*
		 * // display cookies System.err.println("login: " + result); for (var
		 * cookie : client.State.Cookies) { System.err.println("cookie: " + cookie); }
		 */

		// remember state
		// SiteState state = SiteState.siteState(config);
		// state.loggedIn = result;
		// state.userName = user;
		return result;
	}

	private void saveLoginData() {
		Cookie[] cookies = fHttpClient.getState().getCookies();
		fCookiePairs = new String[8];
		for (int i = 0; i < cookies.length; i++) {
			fCookiePairs[i] = "";
		}
		// String cookieNamePrefix = "";
		// String sessionKey = "";
		// String useridKey = "";
		// String usernameKey = "";
		// String tokenKey = "";
		// String sessionValue = "";
		// String useridValue = "";
		// String usernameValue = "";
		// String tokenValue = "";
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			String cookieName = cookie.getName();
			String cookieValue = cookie.getValue();
			String cookieNameLowerCase = cookieName.toLowerCase();
			if (cookieNameLowerCase.endsWith("_session")) {
				fCookiePairs[0] = cookieName;
				fCookiePairs[1] = cookieValue;
				// sessionKey = cookieName;
				// sessionValue = cookieValue;
				// cookieNamePrefix = cookieName.substring(0, cookieName.length() - 8);
			} else if (cookieNameLowerCase.endsWith("userid")) {
				fCookiePairs[2] = cookieName;
				fCookiePairs[3] = cookieValue;
				// useridKey = cookieName;
				// useridValue = cookieValue;
			} else if (cookieNameLowerCase.endsWith("username")) {
				fCookiePairs[4] = cookieName;
				fCookiePairs[5] = cookieValue;
				// usernameKey = cookieName;
				// usernameValue = cookieValue;
			} else if (cookieNameLowerCase.endsWith("token")) {
				fCookiePairs[6] = cookieName;
				fCookiePairs[7] = cookieValue;
				// tokenKey = cookieName;
				// tokenValue = cookieValue;
			}
			// display cookies
			// System.out.println("Cookie: " + cookie.getName() + ", Value: " +
			// cookie.getValue() + ", IsPersistent?: "
			// + cookie.isPersistent() + ", Expiry Date: " + cookie.getExpiryDate() +
			// ", Comment: " + cookie.getComment());
		}
		// System.out.println("prefix: " + cookieNamePrefix);
		// System.out.println("_session: " + sessionKey + " - " + sessionValue);
		// System.out.println("UserID: " + useridKey + " - " + useridValue);
		// System.out.println("UserName: " + usernameKey + " - " + usernameValue);
		// System.out.println("Token: " + tokenKey + " - " + tokenValue);
	}

	public boolean logout() throws UnexpectedAnswerException, MethodException {
		GetMethod method = new GetMethod(fPageURL);
		method.setFollowRedirects(false);
		method.addRequestHeader("User-Agent", Connector.USER_AGENT);
		NameValuePair[] params = new NameValuePair[] {
				new NameValuePair("title", "Special:Userlogout"), new NameValuePair("action", "submit")
		};
		method.setQueryString(EncodingUtil.formUrlEncode(params, Connector.UTF8_CHARSET));

		// printQueryString(method);

		boolean result;
		try {
			int responseCode = fHttpClient.executeMethod(method);
			String responseBody = method.getResponseBodyAsString();
			// log(method);

			if (responseCode == 302 && responseBody.length() == 0 || responseCode == HttpStatus.SC_OK) {
				// && responseBody.matches(config.getLogoutSuccess())) {
				// config.getloggedIn = false;
				result = true;
			} else if (responseCode == HttpStatus.SC_OK) {
				// ### should check for a failure message
				result = false;
				throw new UnexpectedAnswerException("logout not successful: responseCode == 200");
			} else {
				throw new UnexpectedAnswerException("logout not successful: " + method.getStatusLine());
			}
		} catch (HttpException e) {
			throw new MethodException("method failed", e);
		} catch (IOException e) {
			throw new MethodException("method failed", e);
		} finally {
			method.releaseConnection();
		}

		// remember state
		// SiteState state = SiteState.siteState(config);
		// state.loggedIn = false;

		return result;
	}

	/**
	 * Object which encapsulates data parsed from the edit tab page. This data
	 * must be posted to the server in order to successfully post edits.
	 */
	static class EditFormData {

		// The MediaWiki server uses these to
		// determine if another user made edits since this data
		// was downloaded.
		private String mStartTime, mEditTime;

		// The contents of the text area on the edit tab.
		private String mEditText;

		private String mAutoSummary;

		// Token which must be passed to the server to save edits.
		private String mEditToken;

		/**
		 * Constructor.
		 * 
		 * @param startTime
		 * @param editTime
		 * @param editText
		 * @param autoSummary
		 * @param editToken
		 */
		EditFormData(String startTime, String editTime, String editText, String autoSummary, String editToken) {
			this.mStartTime = startTime;
			this.mEditTime = editTime;
			this.mEditText = editText;
			this.mAutoSummary = autoSummary;
			this.mEditToken = editToken;
		}

		// Getter methods.
		//
		String getStartTime() {
			return mStartTime;
		}

		String getEditText() {
			return mEditText;
		}

		String getEditTime() {
			return mEditTime;
		}

		String getAutoSummary() {
			return mAutoSummary;
		}

		String getEditToken() {
			return mEditToken;
		}
	}

	/**
	 * Special case of IOException to be thrown when saving data to the server
	 * fails because another user saved data.
	 */
	public static class ConcurrentEditException extends IOException {

		public ConcurrentEditException(String message) {
			super(message);
		}

		public ConcurrentEditException() {
		}

	}

}
