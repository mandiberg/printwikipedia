package info.bliki.htmlcleaner.util;

import info.bliki.htmlcleaner.TagNode;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Extracts a &lt;div class=&quot;errorbox&quot;&gt;...error text...&lt;/div&gt;
 * content.
 * 
 */
public class HtmlFormExtractor extends AbstractHtmlExtractor<HtmlForm> {
	public static final String FORM_TAG = "form";

	public static final String INPUT_TAG = "input";

	public static final String TEXTAREA_TAG = "textarea";

	private final String fFormID;

	public HtmlFormExtractor(HtmlForm resultForm) {
		this(resultForm, "editform");
	}

	public HtmlFormExtractor(HtmlForm resultForm, String formID) {
		super(resultForm);
		fFormID = formID;
	}

	public void appendContent(List nodes) {
		if (nodes != null && !nodes.isEmpty()) {
			Iterator childrenIt = nodes.iterator();
			while (childrenIt.hasNext()) {
				Object item = childrenIt.next();
				if (item != null && (item instanceof TagNode)) {
					TagNode tagNode = (TagNode) item;
					if (tagNode.getName().equalsIgnoreCase(INPUT_TAG) || tagNode.getName().equalsIgnoreCase(TEXTAREA_TAG)) {
						Map<String, String> attributes = tagNode.getAttributes();
						int sz = attributes.size();
						HtmlForm.ElementAttribute[] elementAttributes = new HtmlForm.ElementAttribute[sz];
						Set<Map.Entry<String, String>> eSet = attributes.entrySet();
						int i = 0;
						for (Map.Entry<String, String> entry : eSet) {
							elementAttributes[i++] = new HtmlForm.ElementAttribute(entry.getKey(), "CDATA", entry.getValue());
						}
						fResultObject.addElement(new HtmlForm.Element(tagNode.getName(), elementAttributes));
					}

					Map<String, String> atts = ((TagNode) item).getAttributes();
					String attributeValue = atts.get("class");
					if (attributeValue != null && attributeValue.toLowerCase().equals("errorbox")) {

					} else if (item instanceof List) {
						appendContent((List) item);
					}
				}
			}
		}
	}

	public boolean isFound(TagNode tagNode) {
		String tagName = tagNode.getName();
		if (tagName.equals(FORM_TAG)) {
			Map<String, String> atts = tagNode.getAttributes();
			String attributeValue = atts.get("id");
			if (attributeValue != null && attributeValue.toLowerCase().equals(fFormID)) {
				int method = HtmlForm.POST;
				fResultObject.setID(attributeValue);
				attributeValue = atts.get("name");
				if (attributeValue != null) {
					fResultObject.setName(attributeValue);
				}
				attributeValue = atts.get("method");
				if (attributeValue != null) {
					if ("get".equalsIgnoreCase(attributeValue)) {
						method = HtmlForm.GET;
						fResultObject.setMethod(method);
					}
				}
				attributeValue = atts.get("action");
				if (attributeValue != null) {
					fResultObject.setAction(attributeValue);
				}
				attributeValue = atts.get("enctype");
				if (attributeValue != null) {
					fResultObject.setEncType(attributeValue);
				}
				return true;
			}
		}
		return false;
	}
}
