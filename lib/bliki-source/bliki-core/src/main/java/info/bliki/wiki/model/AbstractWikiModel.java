package info.bliki.wiki.model;

import info.bliki.Messages;
import info.bliki.htmlcleaner.BaseToken;
import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.TagToken;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.AbstractParser;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.filter.HTMLConverter;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.filter.MagicWord;
import info.bliki.wiki.filter.PDFConverter;
import info.bliki.wiki.filter.StringPair;
import info.bliki.wiki.filter.TemplateParser;
import info.bliki.wiki.filter.WikipediaParser;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.namespaces.Namespace;
import info.bliki.wiki.tags.TableOfContentTag;
import info.bliki.wiki.tags.WPATag;
import info.bliki.wiki.tags.WPTag;
import info.bliki.wiki.tags.code.SourceCodeFormatter;
import info.bliki.wiki.tags.util.TagStack;
import info.bliki.wiki.template.ITemplateFunction;
import info.bliki.wiki.template.extension.AttributeList;
import info.bliki.wiki.template.extension.AttributeRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Standard model implementation for the Wikipedia syntax
 * 
 */
public abstract class AbstractWikiModel implements IWikiModel, IContext {
	private static int fNextNumberCounter = 0;

	protected ArrayList<Reference> fReferences;

	protected Map<String, Integer> fReferenceNames;

	protected int fRecursionLevel;

	protected boolean fReplaceColon;

	protected TagStack fTagStack;

	private boolean fInitialized;

	private IConfiguration fConfiguration;

	private IEventListener fWikiListener = null;

	protected INamespace fNamespace;

	// private ResourceBundle fResourceBundle;

	protected String fRedirectLink = null;

	protected String fPageTitle = "PAGENAME";

	protected int fSectionCounter;

	/**
	 * A tag that manages the &quot;table of content&quot;
	 * 
	 */
	protected TableOfContentTag fTableOfContentTag = null;
	/**
	 * &quot;table of content&quot;
	 * 
	 */
	protected List<Object> fTableOfContent = null;

	/**
	 * Contains all anchor strings to create unique anchors
	 */
	protected HashSet<String> fToCSet;

	/**
	 * Map an attribute name to its value(s). These values are set by outside code
	 * via st.setAttribute(name, value). StringTemplate is like self in that a
	 * template is both the "class def" and "instance". When you create a
	 * StringTemplate or setTemplate, the text is broken up into chunks (i.e.,
	 * compiled down into a series of chunks that can be evaluated later). You can
	 * have multiple
	 */
	protected Map attributes;

	/**
	 * A Map<Class,Object> that allows people to register a renderer for a
	 * particular kind of object to be displayed in this template. This overrides
	 * any renderer set for this template's group.
	 * 
	 * Most of the time this map is not used because the StringTemplateGroup has
	 * the general renderer map for all templates in that group. Sometimes though
	 * you want to override the group's renderers.
	 */
	protected Map<Class, Object> attributeRenderers;

	public AbstractWikiModel() {
		this(Configuration.DEFAULT_CONFIGURATION);
	}

	public AbstractWikiModel(Configuration configuration) {
		this(configuration, Locale.ENGLISH);
	}

	public AbstractWikiModel(Configuration configuration, Locale locale) {
		this(configuration, Messages.getResourceBundle(locale), new Namespace(locale));
	}

	public AbstractWikiModel(Configuration configuration, ResourceBundle resourceBundle, INamespace namespace) {
		fInitialized = false;
		fConfiguration = configuration;
		// fResourceBundle = resourceBundle;
		fNamespace = namespace;
		// initializeNamespaces();
		initialize();
	}

	// private void initializeNamespaces() {
	// String ns1, ns2;
	//
	// ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_MEDIA1);
	// if (ns1 != null) {
	// fImageNamespaces[0] = ns1;
	// ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_MEDIA2);
	// if (ns2 != null) {
	// fImageNamespaces[1] = ns2;
	// }
	// }
	//
	// ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_IMAGE1);
	// if (ns1 != null) {
	// fImageNamespaces[0] = ns1;
	// ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_IMAGE2);
	// if (ns2 != null) {
	// fImageNamespaces[1] = ns2;
	// }
	// }
	//
	// ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_TEMPLATE1);
	// if (ns1 != null) {
	// fTemplateNamespaces[0] = ns1;
	// ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_TEMPLATE2);
	// if (ns2 != null) {
	// fTemplateNamespaces[1] = ns2;
	// }
	// }
	//
	// ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_CATEGORY1);
	// if (ns1 != null) {
	// fCategoryNamespaces[0] = ns1;
	// ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_CATEGORY2);
	// if (ns2 != null) {
	// fCategoryNamespaces[1] = ns2;
	// }
	// }
	//
	// }

	public void addCategory(String categoryName, String sortKey) {

	}

	public SourceCodeFormatter addCodeFormatter(String key, SourceCodeFormatter value) {
		return fConfiguration.addCodeFormatter(key, value);
	}

	public String addInterwikiLink(String key, String value) {
		return fConfiguration.addInterwikiLink(key, value);
	}

	public void addLink(String topicName) {

	}

	public boolean addSemanticAttribute(String attribute, String attributeValue) {
		return false;
	}

	public boolean addSemanticRelation(String relation, String relationValue) {
		return false;
	}

	public void addTemplate(String template) {

	}

	public ITemplateFunction addTemplateFunction(String key, ITemplateFunction value) {
		return fConfiguration.addTemplateFunction(key, value);
	}

	public TagToken addTokenTag(String key, TagToken value) {
		return fConfiguration.addTokenTag(key, value);
	}

	public String[] addToReferences(String reference, String nameAttribute) {
		String[] result = new String[2];
		result[1] = null;
		if (fReferences == null) {
			fReferences = new ArrayList<Reference>();
			fReferenceNames = new HashMap<String, Integer>();
		}
		if (nameAttribute != null) {
			Integer index = fReferenceNames.get(nameAttribute);
			if (index != null) {
				result[0] = index.toString();
				Reference ref = fReferences.get(index - 1);
				int count = ref.incCounter();
				if (count >= Reference.CHARACTER_REFS.length()) {
					result[1] = nameAttribute + '_' + 'Z';
				} else {
					result[1] = nameAttribute + '_' + Reference.CHARACTER_REFS.charAt(count);
				}
				return result;
			}
		}

		if (nameAttribute != null) {
			fReferences.add(new Reference(reference, nameAttribute));
			Integer index = Integer.valueOf(fReferences.size());
			fReferenceNames.put(nameAttribute, index);
			result[1] = nameAttribute + "_a";
		} else {
			fReferences.add(new Reference(reference));
		}
		result[0] = Integer.toString(fReferences.size());
		return result;
	}

	public void append(BaseToken contentNode) {
		fTagStack.append(contentNode);
	}

	public void appendExternalImageLink(String imageSrc, String imageAltText) {
		TagNode spanTagNode = new TagNode("span");
		append(spanTagNode);
		spanTagNode.addAttribute("class", "image", true);
		TagNode imgTagNode = new TagNode("img");
		spanTagNode.addChild(imgTagNode);
		imgTagNode.addAttribute("src", imageSrc, true);
		imgTagNode.addAttribute("alt", imageAltText, true);
		// "nofollow" keyword is not allowed for XHTML
		// imgTagNode.addAttribute("rel", "nofollow", true);
	}

	/**
	 * Append an external link (starting with http, https, ftp,...) as described
	 * in <a href="http://en.wikipedia.org/wiki/Help:Link#External_links">Help
	 * Links</a>
	 * 
	 * @param link
	 *          the external link with <code>http://, https:// or ftp://</code>
	 *          prefix
	 * @param linkName
	 *          the link name which is separated from the URL by a space
	 * @param withoutSquareBrackets
	 *          if <code>true</code> a link with no square brackets around the
	 *          link was parsed
	 * @deprecated use
	 *             {@link IWikiModel#appendExternalLink(String, String, String, boolean)}
	 *             instead.
	 */
	public void appendExternalLink(String link, String linkName, boolean withoutSquareBrackets) {
		appendExternalLink("", link, linkName, withoutSquareBrackets);
	}

	/**
	 * Append an external link (starting with http, https, ftp,...) as described
	 * in <a href="http://en.wikipedia.org/wiki/Help:Link#External_links">Help
	 * Links</a>
	 * 
	 * @param uriSchemeName
	 *          the top level URI (Uniform Resource Identifier) scheme name
	 *          (without the following colon character ":"). Example "ftp",
	 *          "http", "https". See <a
	 *          href="http://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
	 * @param link
	 *          the external link with <code>http://, https:// or ftp://</code>
	 *          prefix
	 * @param linkName
	 *          the link name which is separated from the URL by a space
	 * @param withoutSquareBrackets
	 *          if <code>true</code> a link with no square brackets around the
	 *          link was parsed
	 */
	public void appendExternalLink(String uriSchemeName, String link, String linkName, boolean withoutSquareBrackets) {
		link = Utils.escapeXml(link, true, false, false);
		// is the given link an image?
		// int indx = link.lastIndexOf(".");
		// if (indx > 0 && indx < (link.length() - 3)) {
		// String ext = link.substring(indx + 1);
		// if (ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png") ||
		// ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg")
		// || ext.equalsIgnoreCase("bmp")) {
		// appendExternalImageLink(link, linkName);
		// return;
		// }
		// }
		TagNode aTagNode = new TagNode("a");
		aTagNode.addAttribute("href", link, true);
		aTagNode.addAttribute("class", "externallink", true);
		aTagNode.addAttribute("title", link, true);
		aTagNode.addAttribute("rel", "nofollow", true);
		if (withoutSquareBrackets) {
			append(aTagNode);
			aTagNode.addChild(new ContentToken(linkName));
		} else {
			String trimmedText = linkName.trim();
			if (trimmedText.length() > 0) {
				pushNode(aTagNode);
				WikipediaParser.parseRecursive(trimmedText, this, false, true);
				popNode();
			}
		}
	}

	public void appendInternalImageLink(String hrefImageLink, String srcImageLink, ImageFormat imageFormat) {
		int pxWidth = imageFormat.getWidth();
		int pxHeight = imageFormat.getHeight();
		String caption = imageFormat.getCaption();
		TagNode divTagNode = new TagNode("div");
		divTagNode.addAttribute("id", "image", false);
		// String link = imageFormat.getLink();
		// if (link != null) {
		// String href = encodeTitleToUrl(link, true);
		// divTagNode.addAttribute("href", href, false);
		// } else {
		if (hrefImageLink.length() != 0) {
			divTagNode.addAttribute("href", hrefImageLink, false);
		}
		// }
		divTagNode.addAttribute("src", srcImageLink, false);
		divTagNode.addObjectAttribute("wikiobject", imageFormat);
		if (pxHeight != -1) {
			if (pxWidth != -1) {
				divTagNode.addAttribute("style", "height:" + pxHeight + "px; " + "width:" + pxWidth + "px", false);
			} else {
				divTagNode.addAttribute("style", "height:" + pxHeight + "px", false);
			}
		} else {
			if (pxWidth != -1) {
				divTagNode.addAttribute("style", "width:" + pxWidth + "px", false);
			}
		}
		pushNode(divTagNode);

		String imageType = imageFormat.getType();
		// TODO: test all these cases
		if (caption != null && caption.length() > 0
				&& ("frame".equals(imageType) || "thumb".equals(imageType) || "thumbnail".equals(imageType))) {

			TagNode captionTagNode = new TagNode("div");
			String clazzValue = "caption";
			String type = imageFormat.getType();
			if (type != null) {
				clazzValue = type + clazzValue;
			}
			captionTagNode.addAttribute("class", clazzValue, false);
			//			
			TagStack localStack = WikipediaParser.parseRecursive(caption, this, true, true);
			captionTagNode.addChildren(localStack.getNodeList());
			String altAttribute = imageFormat.getAlt();
			if (altAttribute == null) {
				altAttribute = captionTagNode.getBodyString();
				imageFormat.setAlt(altAttribute);
			}
			pushNode(captionTagNode);
			// WikipediaParser.parseRecursive(caption, this);
			popNode();
		}

		popNode(); // div

	}

	public String encodeTitleToUrl(String wikiTitle, boolean firstCharacterAsUpperCase) {
		return Encoder.encodeTitleToUrl(wikiTitle, firstCharacterAsUpperCase);
	}

	public String encodeTitleDotUrl(String wikiTitle, boolean firstCharacterAsUpperCase) {
		return Encoder.encodeTitleDotUrl(wikiTitle, firstCharacterAsUpperCase);
	}

	public void appendInternalLink(String topic, String hashSection, String topicDescription, String cssClass, boolean parseRecursive) {
		WPATag aTagNode = new WPATag();
		// append(aTagNode);
		// aTagNode.addAttribute("id", "w", true);
		String href = encodeTitleToUrl(topic, true);
		if (hashSection != null) {
			href = href + '#' + encodeTitleDotUrl(hashSection, true);
		}
		aTagNode.addAttribute("href", href, true);
		if (cssClass != null) {
			aTagNode.addAttribute("class", cssClass, true);
		}
		aTagNode.addObjectAttribute("wikilink", topic);

		pushNode(aTagNode);
		if (parseRecursive) {
			WikipediaParser.parseRecursive(topicDescription.trim(), this, false, true);
		} else {
			aTagNode.addChild(new ContentToken(topicDescription));
		}
		popNode();

		// ContentToken text = new ContentToken(topicDescription);
		// aTagNode.addChild(text);
	}

	public void appendInterWikiLink(String namespace, String title, String linkText) {
		String hrefLink = getInterwikiMap().get(namespace.toLowerCase());
		if (hrefLink == null) {
			// shouldn't really happen
			hrefLink = "#";
		}

		// false -> don't convert first character to uppercase for interwiki links
		String encodedtopic = encodeTitleToUrl(title, false);
		if (replaceColon()) {
			encodedtopic = encodedtopic.replace(':', '/');
		}
		hrefLink = hrefLink.replace("${title}", encodedtopic);

		TagNode aTagNode = new TagNode("a");
		// append(aTagNode);
		aTagNode.addAttribute("href", hrefLink, true);
		// aTagNode.addChild(new ContentToken(linkText));
		pushNode(aTagNode);
		WikipediaParser.parseRecursive(linkText.trim(), this, false, true);
		popNode();
	}

	public void appendISBNLink(String isbnPureText) {
		StringBuffer isbnUrl = new StringBuffer(isbnPureText.length() + 100);
		isbnUrl.append("http://www.amazon.com/exec/obidos/ASIN/");

		for (int index = 0; index < isbnPureText.length(); index++) {
			if (isbnPureText.charAt(index) >= '0' && isbnPureText.charAt(index) <= '9') {
				isbnUrl.append(isbnPureText.charAt(index));
			}
		}

		String isbnString = isbnUrl.toString();
		TagNode aTagNode = new TagNode("a");
		append(aTagNode);
		aTagNode.addAttribute("href", isbnString, true);
		aTagNode.addAttribute("class", "external text", true);
		aTagNode.addAttribute("title", isbnString, true);
		aTagNode.addAttribute("rel", "nofollow", true);
		aTagNode.addChild(new ContentToken(isbnPureText));
	}

	public void appendMailtoLink(String link, String linkName, boolean withoutSquareBrackets) {
		// is it an image?
		// link = Utils.escapeXml(link, true, false, false);
		// int indx = link.lastIndexOf(".");
		// if (indx > 0 && indx < (link.length() - 3)) {
		// String ext = link.substring(indx + 1);
		// if (ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png") ||
		// ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg")
		// || ext.equalsIgnoreCase("bmp")) {
		// appendExternalImageLink(link, linkName);
		// return;
		// }
		// }
		TagNode aTagNode = new TagNode("a");
		append(aTagNode);
		aTagNode.addAttribute("href", link, true);
		aTagNode.addAttribute("class", "external free", true);
		aTagNode.addAttribute("title", link, true);
		aTagNode.addAttribute("rel", "nofollow", true);
		aTagNode.addChild(new ContentToken(linkName));
	}

	public void appendRawWikipediaLink(String rawLinkText, String suffix) {
		String rawTopicName = rawLinkText;
		if (rawTopicName != null) {
			// trim the name for whitespace characters on the left side
			int trimLeftIndex = 0;
			while ((trimLeftIndex < rawTopicName.length()) && (rawTopicName.charAt(trimLeftIndex) <= ' ')) {
				trimLeftIndex++;
			}
			if (trimLeftIndex > 0) {
				rawTopicName = rawTopicName.substring(trimLeftIndex);
			}
			// Is there an alias like [alias|link] ?
			int pipeIndex = rawTopicName.lastIndexOf('|');
			String alias = "";
			if (-1 != pipeIndex) {
				alias = rawTopicName.substring(pipeIndex + 1);
				rawTopicName = rawTopicName.substring(0, pipeIndex);
				if (alias.length() == 0) {
					// special cases like: [[Test:hello world|]] or [[Test(hello
					// world)|]]
					// or [[Test, hello world|]]
					alias = rawTopicName;
					int index = alias.indexOf(':');
					if (index != -1) {
						alias = alias.substring(index + 1).trim();
					} else {
						index = alias.indexOf('(');
						if (index != -1) {
							alias = alias.substring(0, index).trim();
						} else {
							index = alias.indexOf(',');
							if (index != -1) {
								alias = alias.substring(0, index).trim();
							}
						}
					}
				}
			}

			int hashIndex = rawTopicName.lastIndexOf('#');

			String hash = "";
			if (-1 != hashIndex && hashIndex != rawTopicName.length() - 1) {
				hash = rawTopicName.substring(hashIndex + 1);
				rawTopicName = rawTopicName.substring(0, hashIndex);
			}

			// trim the name for whitespace characters on the right side
			int trimRightIndex = rawTopicName.length() - 1;
			while ((trimRightIndex >= 0) && (rawTopicName.charAt(trimRightIndex) <= ' ')) {
				trimRightIndex--;
			}
			if (trimRightIndex != rawTopicName.length() - 1) {
				rawTopicName = rawTopicName.substring(0, trimRightIndex + 1);
			}

			rawTopicName = Encoder.encodeHtml(rawTopicName);
			String viewableLinkDescription;
			if (-1 != pipeIndex) {
				viewableLinkDescription = alias + suffix;
			} else {
				if (rawTopicName.length() > 0 && rawTopicName.charAt(0) == ':') {
					viewableLinkDescription = rawTopicName.substring(1) + suffix;
				} else {
					viewableLinkDescription = rawTopicName + suffix;
				}
			}

			if (appendRawNamespaceLinks(rawTopicName, viewableLinkDescription, pipeIndex == (-1))) {
				return;
			}

			int indx = rawTopicName.indexOf(':');
			String namespace = null;
			if (indx >= 0) {
				namespace = rawTopicName.substring(0, indx);
			}
			if (namespace != null && isImageNamespace(namespace)) {
				parseInternalImageLink(namespace, rawLinkText);
				return;
			} else {
				if (rawTopicName.length() > 0 && rawTopicName.charAt(0) == ':') {
					rawTopicName = rawTopicName.substring(1);
				}
				if (rawTopicName.length() > 0 && rawTopicName.charAt(0) == ':') {
					rawTopicName = rawTopicName.substring(1);
				}
				addLink(rawTopicName);
				if (-1 != hashIndex) {
					appendInternalLink(rawTopicName, hash, viewableLinkDescription, null, true);
				} else {
					appendInternalLink(rawTopicName, null, viewableLinkDescription, null, true);
				}
			}
		}
	}

	public boolean appendRawNamespaceLinks(String rawNamespaceTopic, String viewableLinkDescription, boolean containsNoPipe) {
		int colonIndex = rawNamespaceTopic.indexOf(':');

		if (colonIndex != (-1)) {
			String nameSpace = rawNamespaceTopic.substring(0, colonIndex);

			if (isSemanticWebActive() && (rawNamespaceTopic.length() > colonIndex + 1)) {
				// See <a
				// href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
				// MediaWiki</a> for more information.
				if (rawNamespaceTopic.charAt(colonIndex + 1) == ':') {
					// found an SMW relation
					String relationValue = rawNamespaceTopic.substring(colonIndex + 2);

					if (addSemanticRelation(nameSpace, relationValue)) {
						if (containsNoPipe) {
							viewableLinkDescription = relationValue;
						}
						if (viewableLinkDescription.trim().length() > 0) {
							appendInternalLink(relationValue, null, viewableLinkDescription, "interwiki", true);
						}
						return true;
					}
				} else if (rawNamespaceTopic.charAt(colonIndex + 1) == '=') {
					// found an SMW attribute
					String attributeValue = rawNamespaceTopic.substring(colonIndex + 2);
					if (addSemanticAttribute(nameSpace, attributeValue)) {
						append(new ContentToken(attributeValue));
						return true;
					}
				}

			}
			if (isCategoryNamespace(nameSpace)) {
				// add the category to this texts metadata
				String category = rawNamespaceTopic.substring(colonIndex + 1).trim();
				if (category != null && category.length() > 0) {
					// TODO implement more sort-key behaviour
					// http://en.wikipedia.org/wiki/Wikipedia:Categorization#
					// Category_sorting
					addCategory(category, viewableLinkDescription);
					return true;
				}
			} else if (isInterWiki(nameSpace)) {
				String title = rawNamespaceTopic.substring(colonIndex + 1);
				if (title != null && title.length() > 0) {
					appendInterWikiLink(nameSpace, title, viewableLinkDescription);
					return true;
				}
			}
		}
		return false;
	}

	public boolean appendRedirectLink(String redirectLink) {
		fRedirectLink = redirectLink;
		return true;
	}

	public void appendSignature(Appendable writer, int numberOfTildes) throws IOException {
		switch (numberOfTildes) {
		case 3:
			writer.append("~~~");
			break;
		case 4:
			writer.append("~~~~");
			break;
		case 5:
			writer.append("~~~~~");
			break;
		}
	}

	public void appendStack(TagStack stack) {
		if (stack != null) {
			fTagStack.append(stack);
		}
	}

	public void buildEditLinkUrl(int section) {
	}

	public int decrementRecursionLevel() {
		return --fRecursionLevel;
	}

	public String get2ndCategoryNamespace() {
		return fNamespace.getCategory2();
	}

	public String get2ndImageNamespace() {
		return fNamespace.getImage2();
	}

	public String get2ndTemplateNamespace() {
		return fNamespace.getTemplate2();
	}

	public String getCategoryNamespace() {
		return fNamespace.getCategory();
	}

	public Map<String, SourceCodeFormatter> getCodeFormatterMap() {
		return fConfiguration.getCodeFormatterMap();
	}

	public String getImageNamespace() {
		return fNamespace.getImage();
	}

	public Map<String, String> getInterwikiMap() {
		return fConfiguration.getInterwikiMap();
	}

	public Set<String> getUriSchemeSet() {
		return fConfiguration.getUriSchemeSet();
	}

	public synchronized int getNextNumber() {
		return fNextNumberCounter++;
	}

	public TagToken getNode(int offset) {
		return fTagStack.get(offset);
	}

	public String getRawWikiContent(String namespace, String templateName, Map<String, String> templateParameters) {
		// String name = Encoder.encodeTitleUrl(articleName);
		if (isTemplateNamespace(namespace)) {
			String magicWord = templateName;
			String parameter = "";
			int index = magicWord.indexOf(':');
			if (index > 0) {
				parameter = magicWord.substring(index + 1).trim();
				magicWord = magicWord.substring(0, index);
			}
			if (MagicWord.isMagicWord(magicWord)) {
				return MagicWord.processMagicWord(magicWord, parameter, this);
			}
		}
		return null;
	}

	public int getRecursionLevel() {
		return fRecursionLevel;
	}

	public String getRedirectLink() {
		return fRedirectLink;
	}

	public List<Reference> getReferences() {
		return fReferences;
	}

	public List<SemanticAttribute> getSemanticAttributes() {
		return null;
	}

	public List<SemanticRelation> getSemanticRelations() {
		return null;
	}

	// public TableOfContentTag getTableOfContentTag(boolean isTOCIdentifier) {
	// if (fTableOfContentTag == null) {
	// TableOfContentTag tableOfContentTag = new TableOfContentTag("div");
	// tableOfContentTag.addAttribute("id", "tableofcontent", true);
	// tableOfContentTag.setShowToC(false);
	// tableOfContentTag.setTOCIdentifier(isTOCIdentifier);
	// fTableOfContentTag = tableOfContentTag;
	// } else {
	// if (isTOCIdentifier) {
	// // try {
	// TableOfContentTag tableOfContentTag = (TableOfContentTag)
	// fTableOfContentTag.clone();
	// fTableOfContentTag.setShowToC(false);
	// tableOfContentTag.setShowToC(true);
	// tableOfContentTag.setTOCIdentifier(isTOCIdentifier);
	// fTableOfContentTag = tableOfContentTag;
	// // } catch (CloneNotSupportedException e) {
	// // e.printStackTrace();
	// // }
	// } else {
	// return fTableOfContentTag;
	// }
	// }
	// this.append(fTableOfContentTag);
	// return fTableOfContentTag;
	// }

	public ITemplateFunction getTemplateFunction(String name) {
		return getTemplateMap().get(name);
	}

	public Map<String, ITemplateFunction> getTemplateMap() {
		return fConfiguration.getTemplateMap();
	}

	public String getTemplateNamespace() {
		return fNamespace.getTemplate();
	}

	public Map<String, TagToken> getTokenMap() {
		return fConfiguration.getTokenMap();
	}

	public IEventListener getWikiListener() {
		return fWikiListener;
	}

	public int incrementRecursionLevel() {
		return ++fRecursionLevel;
	}

	protected void initialize() {
		if (!fInitialized) {
			fWikiListener = null;
			fToCSet = null;
			fTableOfContent = null;
			fTagStack = new TagStack();
			fReferences = null;
			fReferenceNames = null;
			fRecursionLevel = 0;
			fSectionCounter = 0;
			fReplaceColon = false;
			fInitialized = true;
		}
	}

	public boolean isCamelCaseEnabled() {
		return false;
	}

	public boolean isCategoryNamespace(String namespace) {
		return namespace.equalsIgnoreCase(fNamespace.getCategory()) || namespace.equalsIgnoreCase(fNamespace.getCategory2());
	}

	public boolean isEditorMode() {
		return false;
	}

	public boolean isImageNamespace(String namespace) {
		return namespace.equalsIgnoreCase(fNamespace.getImage()) || namespace.equalsIgnoreCase(fNamespace.getImage2());
	}

	public boolean isValidUriScheme(String uriScheme) {
		return getUriSchemeSet().contains(uriScheme);
	}

	public boolean isValidUriSchemeSpecificPart(String uriScheme, String uriSchemeSpecificPart) {
		if (uriScheme.equals("ftp") || uriScheme.equals("http") || uriScheme.equals("https")) {
			if (uriSchemeSpecificPart.length() >= 2 && uriSchemeSpecificPart.substring(0, 2).equals("//")) {
				return true;
			}
			return false;
		}
		return true;
	}

	public boolean isInterWiki(String namespace) {
		return getInterwikiMap().containsKey(namespace.toLowerCase());
	}

	public boolean isMathtranRenderer() {
		return false;
	}

	public boolean isNamespace(String namespace) {
		return isImageNamespace(namespace) || isTemplateNamespace(namespace) || isCategoryNamespace(namespace);
	}

	public boolean isPreviewMode() {
		return false;
	}

	public boolean isSemanticWebActive() {
		return false;
	}

	public boolean isTemplateNamespace(String namespace) {
		return namespace.equalsIgnoreCase(fNamespace.getTemplate()) || namespace.equalsIgnoreCase(fNamespace.getTemplate2());
	}

	public boolean isTemplateTopic() {
		return false;
	}

	public boolean parseBBCodes() {
		return false;
	}

	public void parseEvents(IEventListener listener, String rawWikiText) {
		initialize();
		if (rawWikiText == null) {
			return;
		}
		fWikiListener = listener;
		WikipediaParser.parse(rawWikiText, this, false, null);
		fInitialized = false;
	}

	public String parseTemplates(String rawWikiText) {
		return parseTemplates(rawWikiText, false);
	}

	public String parseTemplates(String rawWikiText, boolean parseOnlySignature) {
		if (rawWikiText == null) {
			return "";
		}
		if (!parseOnlySignature) {
			initialize();
		}
		StringBuilder buf = new StringBuilder(rawWikiText.length() + rawWikiText.length() / 10);
		try {
			TemplateParser.parse(rawWikiText, this, buf, parseOnlySignature, true);
		} catch (Exception ioe) {
			ioe.printStackTrace();
			buf.append("<span class=\"error\">TemplateParser exception: " + ioe.getClass().getSimpleName() + "</span>");
		}
		return buf.toString();
	}

	public TagToken peekNode() {
		return fTagStack.peek();
	}

	public TagToken popNode() {
		return fTagStack.pop();
	}

	public boolean pushNode(TagToken node) {
		return fTagStack.push(node);
	}

	public String render(ITextConverter converter, String rawWikiText) {
		initialize();
		if (rawWikiText == null) {
			return "";
		}
		WikipediaParser.parse(rawWikiText, this, true, null);
		if (converter != null) {
			StringBuilder buf = new StringBuilder(rawWikiText.length() + rawWikiText.length() / 10);
			List<BaseToken> list = fTagStack.getNodeList();

			try {
				converter.nodesToText(list, buf, this);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} finally {
				fInitialized = false;
			}
			return buf.toString();
		}
		fInitialized = false;
		return null;
	}

	public String render(String rawWikiText) {
		return render(new HTMLConverter(), rawWikiText);
	}

	public String renderPDF(String rawWikiText) {
		return render(new PDFConverter(), rawWikiText);
	}

	public boolean replaceColon() {
		return true;
	}

	public void setSemanticWebActive(boolean semanticWeb) {

	}

	public void setUp() {
		fRecursionLevel = 0;
	}

	public boolean showSyntax(String tagName) {
		return true;
	}

	public int stackSize() {
		return fTagStack.size();
	}

	public TagStack swapStack(TagStack stack) {
		TagStack temp = fTagStack;
		fTagStack = stack;
		return temp;
	}

	public void tearDown() {

	}

	public List<BaseToken> toNodeList(String rawWikiText) {
		initialize();
		if (rawWikiText == null) {
			return new ArrayList<BaseToken>();
		}
		WikipediaParser.parse(rawWikiText, this, true, null);
		fInitialized = false;
		return fTagStack.getNodeList();
	}

	public ResourceBundle getResourceBundle() {
		return fNamespace.getResourceBundle();
	}

	public AbstractParser createNewInstance(String rawWikitext) {
		return new WikipediaParser(rawWikitext, isTemplateTopic(), getWikiListener());
	}

	public void setPageName(String pageTitle) {
		fPageTitle = pageTitle;
	}

	public String getPageName() {
		return fPageTitle;
	}

	/**
	 * Resolve an attribute reference. It can be in four possible places:
	 * 
	 * 1. the attribute list for the current template 2. if self is an embedded
	 * template, somebody invoked us possibly with arguments--check the argument
	 * context 3. if self is an embedded template, the attribute list for the
	 * enclosing instance (recursively up the enclosing instance chain) 4. if
	 * nothing is found in the enclosing instance chain, then it might be a map
	 * defined in the group or the its supergroup etc...
	 * 
	 * Attribute references are checked for validity. If an attribute has a value,
	 * its validity was checked before template rendering. If the attribute has no
	 * value, then we must check to ensure it is a valid reference. Somebody could
	 * reference any random value like $xyz$; formal arg checks before rendering
	 * cannot detect this--only the ref can initiate a validity check. So, if no
	 * value, walk up the enclosed template tree again, this time checking formal
	 * parameters not attributes Map. The formal definition must exist even if no
	 * value.
	 * 
	 * To avoid infinite recursion in toString(), we have another condition to
	 * check regarding attribute values. If your template has a formal argument,
	 * foo, then foo will hide any value available from "above" in order to
	 * prevent infinite recursion.
	 * 
	 * This method is not static so people can override functionality.
	 */
	public Object getAttribute(String attribute) { // StringTemplate self, String
		// attribute) {
		//System.out.println("### get("+self.getEnclosingInstanceStackString()+", "+
		// attribute+")");
		// System.out.println("attributes="+(self.attributes!=null?self.attributes.
		// keySet().toString():"none"));
		// if ( self==null ) {
		// return null;
		// }
		//
		// if ( lintMode ) {
		// self.trackAttributeReference(attribute);
		// }

		// is it here?
		Object o = null;
		if (attributes != null) {
			o = attributes.get(attribute);
		}

		// // nope, check argument context in case embedded
		// if ( o==null ) {
		// Map argContext = self.getArgumentContext();
		// if ( argContext!=null ) {
		// o = argContext.get(attribute);
		// }
		// }
		//
		// if ( o==null &&
		// !self.passThroughAttributes &&
		// self.getFormalArgument(attribute)!=null )
		// {
		// // if you've defined attribute as formal arg for this
		// // template and it has no value, do not look up the
		// // enclosing dynamic scopes. This avoids potential infinite
		// // recursion.
		// return null;
		// }
		//
		// // not locally defined, check enclosingInstance if embedded
		// if ( o==null && self.enclosingInstance!=null ) {
		// /*
		// System.out.println("looking for "+getName()+"."+attribute+" in super="+
		// enclosingInstance.getName());
		// */
		// Object valueFromEnclosing = get(self.enclosingInstance, attribute);
		// if ( valueFromEnclosing==null ) {
		// checkNullAttributeAgainstFormalArguments(self, attribute);
		// }
		// o = valueFromEnclosing;
		// }
		//
		// // not found and no enclosing instance to look at
		// else if ( o==null && self.enclosingInstance==null ) {
		// // It might be a map in the group or supergroup...
		// o = self.group.getMap(attribute);
		// }

		return o;
	}

	/**
	 * Map a value to a named attribute. Throw NoSuchElementException if the named
	 * attribute is not formally defined in self's specific template and a formal
	 * argument list exists.
	 */
	protected void rawSetAttribute(Map attributes, String name, Object value) {
		// if ( formalArguments!=FormalArgument.UNKNOWN &&
		// getFormalArgument(name)==null )
		// {
		// // a normal call to setAttribute with unknown attribute
		// throw new NoSuchElementException("no such attribute: "+name+
		// " in template context "+
		// getEnclosingInstanceStackString());
		// }
		if (value == null) {
			return;
		}
		attributes.put(name, value);
	}

	/**
	 * Set an attribute for this template. If you set the same attribute more than
	 * once, you get a multi-valued attribute. If you send in a StringTemplate
	 * object as a value, it's enclosing instance (where it will inherit values
	 * from) is set to 'this'. This would be the normal case, though you can set
	 * it back to null after this call if you want. If you send in a List plus
	 * other values to the same attribute, they all get flattened into one List of
	 * values. This will be a new list object so that incoming objects are not
	 * altered. If you send in an array, it is converted to an ArrayIterator.
	 */
	public void setAttribute(String name, Object value) {
		if (value == null || name == null) {
			return;
		}
		if (name.indexOf('.') >= 0) {
			throw new IllegalArgumentException("cannot have '.' in attribute names");
		}
		if (attributes == null) {
			attributes = new HashMap();
		}

		// if (value instanceof StringTemplate) {
		// ((StringTemplate) value).setEnclosingInstance(this);
		// } else {
		// // convert value if array
		// value = ASTExpr.convertArrayToList(value);
		// }

		// convert plain collections
		// get exactly in this scope (no enclosing)

		// it will be a multi-value attribute
		// System.out.println("exists: "+name+"="+o);
		AttributeList v = null;

		Object o = this.attributes.get(name);
		if (o == null) { // new attribute
			if (value instanceof List) {
				v = new AttributeList(((List) value).size());
				// flatten incoming list into existing
				v.addAll((List) value);
				rawSetAttribute(this.attributes, name, v);
				return;
			}
			rawSetAttribute(this.attributes, name, value);
			return;
		}

		if (o.getClass() == AttributeList.class) { // already a list made by ST
			v = (AttributeList) o;
		} else if (o instanceof List) { // existing attribute is non-ST List
			// must copy to an ST-managed list before adding new attribute
			List listAttr = (List) o;
			v = new AttributeList(listAttr.size());
			v.addAll(listAttr);
			rawSetAttribute(this.attributes, name, v); // replace attribute w/list
		} else {
			// non-list second attribute, must convert existing to ArrayList
			v = new AttributeList(); // make list to hold multiple values
			// make it point to list now
			rawSetAttribute(this.attributes, name, v); // replace attribute w/list
			v.add(o); // add previous single-valued attribute
		}
		if (value instanceof List) {
			// flatten incoming list into existing
			if (v != value) { // avoid weird cyclic add
				v.addAll((List) value);
			}
		} else {
			v.add(value);
		}
	}

	/**
	 * What renderer is registered for this attributeClassType for this template.
	 * If not found, the template's group is queried.
	 */
	public AttributeRenderer getAttributeRenderer(Class attributeClassType) {
		AttributeRenderer renderer = null;
		if (attributeRenderers != null) {
			renderer = (AttributeRenderer) attributeRenderers.get(attributeClassType);
		}
		if (renderer != null) {
			// found it!
			return renderer;
		}

		// we have no renderer overrides for the template or none for class arg
		// check parent template if we are embedded
		// if ( enclosingInstance!=null ) {
		// return enclosingInstance.getAttributeRenderer(attributeClassType);
		// }
		// // else check group
		// return group.getAttributeRenderer(attributeClassType);
		return null;
	}

	/**
	 * Specify a complete map of what object classes should map to which renderer
	 * objects.
	 */
	public void setAttributeRenderers(Map renderers) {
		this.attributeRenderers = renderers;
	}

	/**
	 * Register a renderer for all objects of a particular type. This overrides
	 * any renderer set in the group for this class type.
	 */
	public void registerRenderer(Class attributeClassType, AttributeRenderer renderer) {
		if (attributeRenderers == null) {
			attributeRenderers = new HashMap();
		}
		attributeRenderers.put(attributeClassType, renderer);
	}

	public TagNode appendToCAnchor(String anchor) {
		TagNode aTagNode = new TagNode("a");
		aTagNode.addAttribute("name", anchor, true);
		aTagNode.addAttribute("id", anchor, true);
		return aTagNode;
	}

	/**
	 * handle head for table of content
	 * 
	 * @param rawHead
	 * @param headLevel
	 */
	public ITableOfContent appendHead(String rawHead, int headLevel, boolean noToC, int headCounter) {
		TagStack localStack = WikipediaParser.parseRecursive(rawHead.trim(), this, true, true);

		WPTag headTagNode = new WPTag("h" + headLevel);
		headTagNode.addChildren(localStack.getNodeList());
		String tocHead = headTagNode.getBodyString();
		String anchor = Encoder.encodeDotUrl(tocHead);
		createTableOfContent(false);
		if (!noToC && (headCounter > 3)) {
			fTableOfContentTag.setShowToC(true);
		}
		if (fToCSet.contains(anchor)) {
			String newAnchor = anchor;
			for (int i = 2; i < Integer.MAX_VALUE; i++) {
				newAnchor = anchor + '_' + Integer.toString(i);
				if (!fToCSet.contains(newAnchor)) {
					break;
				}
			}
			anchor = newAnchor;
		}
		addToTableOfContent(fTableOfContent, tocHead, anchor, headLevel);
		if (getRecursionLevel() == 1) {
			buildEditLinkUrl(fSectionCounter++);
		}
		TagNode aTagNode = new TagNode("a");
		aTagNode.addAttribute("name", anchor, true);
		aTagNode.addAttribute("id", anchor, true);
		append(aTagNode);

		append(headTagNode);
		return fTableOfContentTag;
	}

	private void addToTableOfContent(List<Object> toc, String head, String anchor, int headLevel) {
		if (headLevel == 1) {
			toc.add(new StringPair(head, anchor));
		} else {
			if (toc.size() > 0) {
				if (toc.get(toc.size() - 1) instanceof List) {
					addToTableOfContent((List<Object>) toc.get(toc.size() - 1), head, anchor, --headLevel);
					return;
				}
			}
			ArrayList<Object> list = new ArrayList<Object>();
			toc.add(list);
			addToTableOfContent(list, head, anchor, --headLevel);
		}
	}

	/**
	 * 
	 * @param isTOCIdentifier
	 *          <code>true</code> if the __TOC__ keyword was parsed
	 */
	public ITableOfContent createTableOfContent(boolean isTOCIdentifier) {
		if (fTableOfContentTag == null) {
			TableOfContentTag tableOfContentTag = new TableOfContentTag("div");
			tableOfContentTag.addAttribute("id", "tableofcontent", true);
			tableOfContentTag.setShowToC(false);
			tableOfContentTag.setTOCIdentifier(isTOCIdentifier);
			fTableOfContentTag = tableOfContentTag;
			this.append(fTableOfContentTag);
		} else {
			if (isTOCIdentifier) {
				// try {
				TableOfContentTag tableOfContentTag = (TableOfContentTag) fTableOfContentTag.clone();
				fTableOfContentTag.setShowToC(false);
				tableOfContentTag.setShowToC(true);
				tableOfContentTag.setTOCIdentifier(isTOCIdentifier);
				fTableOfContentTag = tableOfContentTag;
				// } catch (CloneNotSupportedException e) {
				// e.printStackTrace();
				// }
				this.append(fTableOfContentTag);
			} else {
			}
		}

		if (fTableOfContentTag != null) {
			if (fTableOfContent == null) {
				fTableOfContent = fTableOfContentTag.getTableOfContent();
			}
		}
		if (fToCSet == null) {
			fToCSet = new HashSet<String>();
		}
		return fTableOfContentTag;
	}

}
