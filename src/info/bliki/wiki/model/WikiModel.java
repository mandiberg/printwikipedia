package info.bliki.wiki.model;

import info.bliki.Messages;
import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagToken;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.filter.HTMLConverter;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.filter.WikipediaPreTagParser;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.PTag;
import info.bliki.wiki.tags.WPATag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import static info.bliki.wiki.tags.WPATag.ANCHOR;
import static info.bliki.wiki.tags.WPATag.CLASS;
import static info.bliki.wiki.tags.WPATag.HREF;
import static info.bliki.wiki.tags.WPATag.TITLE;
import static info.bliki.wiki.tags.WPATag.WIKILINK;

/**
 * Standard model implementation
 *
 */
public class WikiModel extends AbstractWikiModel {
    /**
     * A map for categories and their associated sort keys
     */
    protected Map<String, String> categories;
    protected Set<String> links;
    protected Set<String> templates;
    protected Set<String> includes;
    protected List<SemanticRelation> semanticRelations;
    protected List<SemanticAttribute> semanticAttributes;

    private String fExternalImageBaseURL;
    private String fExternalWikiBaseURL;

    /**
     *
     * @param imageBaseURL
     *            a url string which must contains a &quot;${image}&quot;
     *            variable which will be replaced by the image name, to create
     *            links to images.
     * @param linkBaseURL
     *            a url string which must contains a &quot;${title}&quot;
     *            variable which will be replaced by the topic title, to create
     *            links to other wiki topics.
     */
    public WikiModel(String imageBaseURL, String linkBaseURL) {
        this(Configuration.DEFAULT_CONFIGURATION, imageBaseURL, linkBaseURL);
    }

    public WikiModel(Configuration configuration, String imageBaseURL,
            String linkBaseURL) {
        super(configuration);
        fExternalImageBaseURL = imageBaseURL;
        fExternalWikiBaseURL = linkBaseURL;
    }

    public WikiModel(Configuration configuration, Locale locale,
            String imageBaseURL, String linkBaseURL) {
        super(configuration, locale);
        fExternalImageBaseURL = imageBaseURL;
        fExternalWikiBaseURL = linkBaseURL;
    }

    public WikiModel(Configuration configuration, Locale locale,
            ResourceBundle resourceBundle, INamespace namespace,
            String imageBaseURL, String linkBaseURL) {
        super(configuration, locale, resourceBundle, namespace);
        fExternalImageBaseURL = imageBaseURL;
        fExternalWikiBaseURL = linkBaseURL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCategory(String categoryName, String sortKey) {
        categories.put(categoryName, sortKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLink(String topicName) {
        links.add(topicName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addSemanticAttribute(String attribute, String attributeValue) {
        if (semanticAttributes == null) {
            semanticAttributes = new ArrayList<>();
        }
        semanticAttributes
                .add(new SemanticAttribute(attribute, attributeValue));
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addSemanticRelation(String relation, String relationValue) {
        if (semanticRelations == null) {
            semanticRelations = new ArrayList<>();
        }
        semanticRelations.add(new SemanticRelation(relation, relationValue));
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTemplate(String template) {
        templates.add(template);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInclude(String pageName) {
        includes.add(pageName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void appendInternalLink(String topic, String hashSection,
            String topicDescription, String cssClass, boolean parseRecursive) {
        appendInternalLink(topic, hashSection, topicDescription, cssClass,
                parseRecursive, true);
    }

    protected void appendInternalLink(String topic, String hashSection,
            String topicDescription, String cssClass, boolean parseRecursive,
            boolean topicExists) {
        String hrefLink;
        String description = topicDescription.trim();
        WPATag aTagNode = new WPATag();
        if (topic.length() > 0) {
            String title = Encoder.normaliseTitle(topic, true, ' ', true);
            if (hashSection == null) {
                String pageName = Encoder.normaliseTitle(fPageTitle, true, ' ', true);
                // self link?
                if (title.equals(pageName)) {
                    HTMLTag selfLink = new HTMLTag("strong");
                    selfLink.addAttribute("class", "selflink", false);
                    pushNode(selfLink);
                    selfLink.addChild(new ContentToken(description));
                    popNode();
                    return;
                }
            }

            String encodedtopic = encodeTitleToUrl(topic, true);
            if (replaceColon()) {
                encodedtopic = encodedtopic.replace(':', '/');
            }
            hrefLink = getWikiBaseURL().replace("${title}", encodedtopic);
            if (!topicExists) {
                if (cssClass == null) {
                    cssClass = "new";
                }
                if (hrefLink.indexOf('?') != -1) {
                    hrefLink += "&";
                } else {
                    hrefLink += "?";
                }
                hrefLink += "action=edit&redlink=1";
                String redlinkString = Messages.getString(getResourceBundle(),
                        Messages.WIKI_TAGS_RED_LINK,
                        "${title} (page does not exist)");
                title = redlinkString.replace("${title}", title);
            }
            aTagNode.addAttribute(TITLE, title, true);
        } else {
            // assume, the own topic exists
            if (hashSection != null) {
                hrefLink = "";
                if (description.length() == 0) {
                    description = "&#35;" + hashSection; // #....
                }
            } else {
                hrefLink = getWikiBaseURL().replace("${title}", "");
            }
        }

        String href = hrefLink;
        if (topicExists && hashSection != null) {
            aTagNode.addObjectAttribute(ANCHOR, hashSection);
            href = href + '#' + encodeTitleDotUrl(hashSection, false);
        }
        aTagNode.addAttribute(HREF, href, true);
        if (cssClass != null) {
            aTagNode.addAttribute(CLASS, cssClass, true);
        }
        aTagNode.addObjectAttribute(WIKILINK, topic);

        pushNode(aTagNode);
        if (parseRecursive) {
            WikipediaPreTagParser.parseRecursive(description, this, false, true);
        } else {
            aTagNode.addChild(new ContentToken(description));
        }
        popNode();
    }

    /**
     * Get the set of Wikipedia category names used in this text
     *
     * @return the set of category strings
     */
    public Map<String, String> getCategories() {
        return categories;
    }

    /**
     * Get the set of Wikipedia links used in this text
     *
     * @return the set of category strings
     */
    @Override
    public Set<String> getLinks() {
        return links;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SemanticAttribute> getSemanticAttributes() {
        return semanticAttributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SemanticRelation> getSemanticRelations() {
        return semanticRelations;
    }

    /**
     * Gets the names of all included pages in the template namespace.
     *
     * @return page names without the template namespace prefix
     */
    public Set<String> getTemplates() {
        return templates;
    }

    /**
     * Gets the names of all included pages outside the template namespace.
     *
     * @return page names including their namespace prefix
     */
    public Set<String> getIncludes() {
        return includes;
    }

    /**
     * Append the internal wiki image link to this model.
     *
     * <br/>
     * <br/>
     * <b>Note</b>: the pipe symbol (i.e. &quot;|&quot;) splits the
     * <code>rawImageLink</code> into different segments. The first segment is
     * used as the <code>&lt;image-name&gt;</code> and typically ends with
     * extensions like <code>.png</code>, <code>.gif</code>, <code>.jpg</code>
     * or <code>.jpeg</code>.
     *
     * <br/>
     * <br/>
     * <b>Note</b>: if the image link contains a "width" attribute, the filename
     * is constructed as <code>&lt;size&gt;px-&lt;image-name&gt;</code>,
     * otherwise it's only the <code>&lt;image-name&gt;</code>.
     *
     * <br/>
     * <br/>
     * See <a href="http://en.wikipedia.org/wiki/Image_markup">Image markup</a>
     * and see <a
     * href="http://www.mediawiki.org/wiki/Help:Images">Help:Images</a>
     *
     * @param imageNamespace
     *            the image namespace
     * @param rawImageLink
     *            the raw image link text without the surrounding
     *            <code>[[...]]</code>
     */
    @Override
    public void parseInternalImageLink(String imageNamespace,
            String rawImageLink) {
        String imageSrc = getImageBaseURL();
        if (imageSrc != null) {
            String imageHref = getWikiBaseURL();
            ImageFormat imageFormat = ImageFormat.getImageFormat(rawImageLink,
                    imageNamespace);

            String imageName = createImageName(imageFormat);
            String link = imageFormat.getLink();
            if (link != null) {
                if (link.length() == 0) {
                    imageHref = "";
                } else {
                    String encodedTitle = encodeTitleToUrl(link, true);
                    imageHref = imageHref.replace("${title}", encodedTitle);
                }

            } else {
                if (replaceColon()) {
                    imageHref = imageHref.replace("${title}", imageNamespace
                            + '/' + imageName);
                } else {
                    imageHref = imageHref.replace("${title}", imageNamespace
                            + ':' + imageName);
                }
            }
            imageSrc = imageSrc.replace("${image}", imageName);
            String type = imageFormat.getType();
            TagToken tag = null;
            if ("thumb".equals(type) || "frame".equals(type)) {
                if (fTagStack.size() > 0) {
                    tag = peekNode();
                }
                reduceTokenStack(Configuration.HTML_DIV_OPEN);

            }
            appendInternalImageLink(imageHref, imageSrc, imageFormat);
            if (tag instanceof PTag) {
                pushNode(new PTag());
            }
        }
    }

    protected String createImageName(ImageFormat imageFormat) {
        String imageName = imageFormat.getFilename();
        String sizeStr = imageFormat.getWidthStr();
        if (sizeStr != null) {
            imageName = sizeStr + '-' + imageName;
        }
        if (imageName.endsWith(".svg")) {
            imageName += ".png";
        }
        imageName = Encoder.encodeUrl(imageName);
        if (replaceColon()) {
            imageName = imageName.replace(':', '/');
        }
        return imageName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean replaceColon() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() {
        super.setUp();
        categories = new HashMap<>();
        links = new HashSet<>();
        templates = new HashSet<>();
        includes = new HashSet<>();
        semanticRelations = null;
        semanticAttributes = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public INamespace getNamespace() {
        return fNamespace;
    }

    /**
     * Convert a given text in wiki notation into another format.
     *
     * @param model
     *            a wiki model
     * @param converter
     *            a text converter. <b>Note</b> the converter may be
     *            <code>null</code>, if you only would like to analyze the raw
     *            wiki text and don't need to convert. This speeds up the
     *            parsing process.
     * @param rawWikiText
     *            a raw wiki text
     * @param resultBuffer
     *            the buffer to which to append the resulting HTML code.
     * @param templateTopic
     *            if <code>true</code>, render the wiki text as if a template
     *            topic will be displayed directly, otherwise render the text as
     *            if a common wiki topic will be displayed.
     * @param parseTemplates
     *            parses the template expansion step (parses include,
     *            onlyinclude, includeonly etc)
     * @throws IOException
     */
    public static void toText(IWikiModel model, ITextConverter converter,
            String rawWikiText, Appendable resultBuffer, boolean templateTopic,
            boolean parseTemplates) throws IOException {
        model.render(converter, rawWikiText, resultBuffer, templateTopic,
                parseTemplates);
    }

    /**
     * Convert a given text in wiki notation into HTML text.
     *
     * @param rawWikiText
     *            a raw wiki text
     * @param resultBuffer
     *            the buffer to which to append the resulting HTML code.
     * @param imageBaseURL
     *            a url string which must contains a &quot;${image}&quot;
     *            variable which will be replaced by the image name, to create
     *            links to images.
     * @param linkBaseURL
     *            a url string which must contains a &quot;${title}&quot;
     *            variable which will be replaced by the topic title, to create
     *            links to other wiki topics.
     * @throws IOException
     */
    public static void toHtml(String rawWikiText, Appendable resultBuffer,
            String imageBaseURL, String linkBaseURL) throws IOException {
        toText(new WikiModel(imageBaseURL, linkBaseURL), new HTMLConverter(),
                rawWikiText, resultBuffer, false, false);
    }

    /**
     * Convert a given text in wiki notation into HTML text.
     *
     * @param rawWikiText
     *            a raw wiki text
     * @param resultBuffer
     *            the buffer to which to append the resulting HTML code.
     * @throws IOException
     */
    public static void toHtml(String rawWikiText, Appendable resultBuffer)
            throws IOException {
        toText(new WikiModel("/${image}", "/${title}"), new HTMLConverter(),
                rawWikiText, resultBuffer, false, false);
    }

    /**
     * Convert a given text in wiki notation into HTML text.
     *
     * @param rawWikiText
     *            a raw wiki text
     * @return the resulting HTML text; nay returns <code>null</code>, if an
     *         <code>IOException</code> occured.
     */
    public static String toHtml(String rawWikiText) {
        try {
            StringBuilder resultBuffer = new StringBuilder(rawWikiText.length()
                    + rawWikiText.length() / 10);
            toText(new WikiModel("/${image}", "/${title}"),
                    new HTMLConverter(), rawWikiText, resultBuffer, false,
                    false);
            return resultBuffer.toString();
        } catch (IOException ignored) {
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImageBaseURL() {
        return fExternalImageBaseURL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWikiBaseURL() {
        return fExternalWikiBaseURL;
    }
}
