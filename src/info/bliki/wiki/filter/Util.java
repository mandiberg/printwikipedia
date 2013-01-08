package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.tags.util.NodeAttribute;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Util {

	private Util() {
	}

	/**
	 * Scan the attributes string and add the attributes to the given node
	 * 
	 * @param node
	 * @param attributesString
	 */
	public static NodeAttribute addAttributes(TagNode node, String attributesString) {
		if (attributesString != null) {
			List<NodeAttribute> attributes = getNodeAttributes(attributesString);
			if (attributes != null) {
				NodeAttribute attr = null;
				for (int i = 0; i < attributes.size(); i++) {
					attr = attributes.get(i);
					node.addAttribute(attr.getName(), attr.getValue(), true);
				}
				return attr;
			}
		}
		return null;
	}

	public static List<NodeAttribute> getNodeAttributes(String attributesString) {
		List<NodeAttribute> attributes = null;
		if (attributesString != null) {
			String trimmed = attributesString.trim();
			if (trimmed.length() != 0) {
				WikipediaScanner scanner = new WikipediaScanner(trimmed);
				attributes = scanner.parseAttributes(0, trimmed.length());
			}
		}
		return attributes;
	}

	public static Map<String, String> getAttributes(String attributesString) {
		TreeMap<String, String> map = null;
		if (attributesString != null) {
			String trimmed = attributesString.trim();
			if (trimmed.length() != 0) {
				map = new TreeMap<String, String>();
				WikipediaScanner scanner = new WikipediaScanner(trimmed);
				List<NodeAttribute> attributes = scanner.parseAttributes(0, trimmed.length());
				NodeAttribute attr;
				for (int i = 0; i < attributes.size(); i++) {
					attr = attributes.get(i);
					map.put(attr.getName(), attr.getValue());
				}
			}
		}
		return map;
	}
	
	/**
	 * Returns the index within the seachable string of the first occurrence of
	 * the concatenated <i>start</i> and <i>end</i> substring. The end substring
	 * is matched ignoring case considerations.
	 * 
	 * @param searchableString
	 *          the searchable string
	 * @param startPattern
	 *          the start string which should be searched in exact case mode
	 * @param endPattern
	 *          the end string which should be searched in ignore case mode
	 * @param fromIndex
	 *          the index from which to start the search.
	 * @return the index within this string of the first occurrence of the
	 *         specified substring, starting at the specified index.
	 */
	public static int indexOfIgnoreCase(String searchableString, String startPattern, String endPattern, int fromIndex) {
//		return indexOfIgnoreCase(searchableString, startPattern, null, endPattern, fromIndex);
		 int n = endPattern.length();
		 int index;
		 int len = startPattern.length();
		 while (searchableString.length() > ((fromIndex + n) - 1)) {
		 index = searchableString.indexOf(startPattern, fromIndex);
		 if (index >= 0) {
		 fromIndex = index + len;
		 if (searchableString.regionMatches(true, fromIndex, endPattern, 0, n)) {
		 return fromIndex - len;
		 }
		 }
		 fromIndex++;
		 }
		
		 return -1;
	}
}
