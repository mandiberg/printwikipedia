package info.bliki.api.query;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.httpclient.NameValuePair;

public class RequestBuilder {
	Map<String, String> fQueryMap = new TreeMap<String, String>();

	public void clear() {
		fQueryMap.clear();
	}

	public boolean containsKey(String arg0) {
		return fQueryMap.containsKey(arg0);
	}

	public boolean containsValue(String arg0) {
		return fQueryMap.containsValue(arg0);
	}

	public Set<Entry<String, String>> entrySet() {
		return fQueryMap.entrySet();
	}

	public boolean equals(Object arg0) {
		if (arg0 instanceof RequestBuilder) {
			return fQueryMap.equals(arg0);
		}
		return false;
	}

	public String get(String arg0) {
		return fQueryMap.get(arg0);
	}

	public int hashCode() {
		return fQueryMap.hashCode();
	}

	public boolean isEmpty() {
		return fQueryMap.isEmpty();
	}

	public Set<String> keySet() {
		return fQueryMap.keySet();
	}

	public String put(String arg0, String arg1) {
		return fQueryMap.put(arg0, arg1);
	}

	public void putAll(Map<? extends String, ? extends String> arg0) {
		fQueryMap.putAll(arg0);
	}

	public String remove(String arg0) {
		return fQueryMap.remove(arg0);
	}

	public int size() {
		return fQueryMap.size();
	}

	public Collection<String> values() {
		return fQueryMap.values();
	}

	/**
	 * Constructor sets the following default values:
	 * <ol>
	 * <li>output format to 'xml'</li>
	 * <li>action to 'query'</li>
	 * </ol>
	 */
	public RequestBuilder() {
		format("xml");
		action("query");
	}

	public static RequestBuilder create() {
		return new RequestBuilder();
	}

	/**
	 * The format of the output One value: json, jsonfm, php, phpfm, wddx, wddxfm,
	 * xml, xmlfm, yaml, yamlfm, rawfm, txt, txtfm, dbg, dbgfm Default: xmlfm
	 * 
	 * @param lType
	 * @return
	 */
	public RequestBuilder format(String format) {
		put("format", format);
		return this;
	}

	/**
	 * What action you would like to perform One value: sitematrix, opensearch,
	 * login, logout, query, expandtemplates, parse, feedwatchlist, help,
	 * paraminfo, purge, rollback, delete, undelete, protect, block, unblock,
	 * move, edit, emailuser, watch, patrol Default: help
	 * 
	 * @param lType
	 * @return
	 */
	public RequestBuilder action(String action) {
		put("action", action);
		return this;
	}

	/**
	 * When showing help, include version for each module
	 * 
	 * @param lType
	 * @return
	 */
	public RequestBuilder version(String version) {
		put("version", version);
		return this;
	}

	/**
	 * Maximum lag
	 * 
	 * @param maxlag
	 * @return
	 */
	public RequestBuilder maxlag(int maxlag) {
		put("maxlag", Integer.toString(maxlag));
		return this;
	}

	/**
	 * Set the s-maxage header to this many seconds. Errors are never cached
	 * Default: 0
	 * 
	 * @param smaxage
	 * @return
	 */
	public RequestBuilder smaxage(int smaxage) {
		put("smaxage", Integer.toString(smaxage));
		return this;
	}

	/**
	 * Set the max-age header to this many seconds. Errors are never cached
	 * Default: 0
	 * 
	 * @param maxage
	 * @return
	 */
	public RequestBuilder maxage(int maxage) {
		put("maxage", Integer.toString(maxage));
		return this;
	}

	/**
	 * Request ID to distinguish requests. This will just be output back to you
	 * 
	 * @param requestid
	 * @return
	 */
	public RequestBuilder requestid() {
		put("requestid", null);
		return this;
	}

	public RequestBuilder putPipedString(String parameter, String... pipedValues) {
		put(parameter, toPipedString(pipedValues));
		return this;
	}

	public RequestBuilder putPipedString(String parameter, int... pipedValues) {
		put(parameter, toPipedString(pipedValues));
		return this;
	}

	/**
	 * Utility method to convert an array of strings into a piped
	 * separated/formatted string
	 * 
	 * @param pipedValues
	 * @return
	 */
	public static String toPipedString(String... pipedValues) {
		int len = 0;
		int max = -1;
		for (int i = 0; i < pipedValues.length; i++) {
			if (pipedValues[i] != null) {
				max++;
				len += pipedValues[i].length() + 1;
			}
		}
		StringBuilder buff = new StringBuilder(len);
		for (int i = 0; i < pipedValues.length; i++) {
			if (pipedValues[i] != null) {
				buff.append(pipedValues[i]);
				if (i < max) {
					buff.append('|');
				}
			}
		}
		return buff.toString();
	}

	public static String toPipedString(int... pipedValues) {
		int len = 0;
		int max = -1;
		for (int i = 0; i < pipedValues.length; i++) {
			if (pipedValues[i] >= 0) {
				max++;
				len += (pipedValues[i] / 10) + 1;
			}
		}
		StringBuilder buff = new StringBuilder(len);
		for (int i = 0; i < pipedValues.length; i++) {
			if (pipedValues[i] >= 0) {
				buff.append(Integer.toString(pipedValues[i]));
				if (i < max) {
					buff.append('|');
				}
			}
		}
		return buff.toString();
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder(256);
		int i = 0;
		for (Map.Entry<String, String> entry : fQueryMap.entrySet()) {
			if (i > 0) {
				buff.append("&amp;");
			}
			i++;
			buff.append(entry.getKey());
			if (entry.getValue() != null && entry.getValue().length() > 0) {
				buff.append("=");
				buff.append(entry.getValue());
			}
		}
		return buff.toString();
	}

	public NameValuePair[] getParameters() {
		Set<Map.Entry<String, String>> set = fQueryMap.entrySet();
		NameValuePair[] nameValuePairs = new NameValuePair[set.size()];
		int count = 0;
		for (Map.Entry<String, String> entry : set) {
			if (entry.getValue() != null && entry.getValue().length() > 0) {
				nameValuePairs[count++] = new NameValuePair(entry.getKey(), entry.getValue());
			} else {
				nameValuePairs[count++] = new NameValuePair(entry.getKey(), "");
			}
		}
		return nameValuePairs;
	}
}
