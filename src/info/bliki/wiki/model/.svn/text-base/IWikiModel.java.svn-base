package info.bliki.wiki.model;

import info.bliki.htmlcleaner.BaseToken;
import info.bliki.htmlcleaner.TagToken;
import info.bliki.wiki.filter.AbstractParser;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.tags.util.TagStack;
import info.bliki.wiki.template.ITemplateFunction;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Interface for rendering a wiki model
 * 
 */
public interface IWikiModel extends IConfiguration {

	/**
	 * When an article contains a token indicating that the article belongs to a
	 * specific category this method should be called to add that category to the
	 * output metadata. For the <code>sortKey</code> see also <a href=
	 * "http://en.wikipedia.org/wiki/Wikipedia:Categorization#Category_sorting"
	 * >Wikipedia:Categorization#Category_sorting</a>
	 * 
	 * @param categoryName
	 *          The name of the category that the document belongs to.
	 * @param sortKey
	 *          The sort key for the category, or <code>null</code> if no sort key
	 *          has been specified. The sort key determines what order categories
	 *          are sorted on category index pages, so a category for "John Doe"
	 *          might be given a sort key of "Doe, John".
	 */
	public void addCategory(String categoryName, String sortKey);

	/**
	 * When a document contains a token indicating that the document links to
	 * another Wiki topic this method should be called to add that topic link to
	 * the output metadata.
	 * 
	 * @param topicName
	 *          The name of the topic that is linked to.
	 */
	public void addLink(String topicName);

	/**
	 * See <a href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
	 * MediaWiki</a> for more information.
	 * 
	 * @param attribute
	 * @param attributeValue
	 * @return
	 */
	public boolean addSemanticAttribute(String attribute, String attributeValue);

	/**
	 * See <a href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
	 * MediaWiki</a> for more information.
	 * 
	 * @param relation
	 * @param relationValue
	 * @return
	 */
	public boolean addSemanticRelation(String relation, String relationValue);

	/**
	 * When a document contains a token indicating that the document includes a
	 * Wiki template this method should be called to add that template to the
	 * output metadata.
	 * 
	 * @param template
	 *          The name of the template that is being included.
	 */
	public void addTemplate(String template);

	/**
	 * Add a reference (i.e. footnote) to the internal list
	 * 
	 * @param reference
	 *          the rendered HTML code of the ref-Tag body
	 * @param nameAttribute
	 *          the value of the <code>name</code> attribute or <code>null</code>
	 * @return the current offset (i.e. size()-1) of the element in the list
	 */
	public String[] addToReferences(String reference, String nameAttribute);

	/**
	 * Append the content as a child on the top node of the internal stack
	 * 
	 * @param contentNode
	 * @return <code>true</code> if the append was successful
	 */
	public void append(BaseToken contentNode);

	/**
	 * Append an external wiki image link (starting with http, https,... and
	 * ending with gif, png, jpg, bmp)
	 * 
	 * @param imageSrc
	 * @param hashSection
	 * @param imageAltText
	 */
	public void appendExternalImageLink(String imageSrc, String imageAltText);

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
	public void appendExternalLink(String link, String linkName, boolean withoutSquareBrackets);

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
	public void appendExternalLink(String uriSchemeName, String link, String linkName, boolean withoutSquareBrackets);

	/**
	 * Add a single wiki head (i.e. ==...==, ===...===, ====...====,...) to the
	 * table of content
	 * 
	 * @param rawHead
	 *          the unparsed header string
	 * @param headLevel
	 *          level of header (i.e. h1, h2, h3, h4, 5h,..)
	 * @param noToc
	 *          don't show the &quot;table of content&quot;
	 * @param headCounter
	 *          the total number of headers parsed
	 * @return the &quot;table of content&quot; tag
	 */
	public ITableOfContent appendHead(String rawHead, int headLevel, boolean noToC, int headCounter);

	/**
	 * Append this internal wiki image link
	 * 
	 * @param hrefImageLink
	 * @param srcImageLink
	 * @param imageFormat
	 */
	public void appendInternalImageLink(String hrefImageLink, String srcImageLink, ImageFormat imageFormat);

	/**
	 * Append an internal wikilink as described in <a
	 * href="http://en.wikipedia.org/wiki/Help:Link#Wikilinks">Help Links</a>
	 * 
	 * @param topic
	 * @param hashSection
	 * @param topicDescription
	 * @param cssClass
	 *          the links CSS class style
	 * @param parseRecursive
	 *          TODO
	 */
	public void appendInternalLink(String topic, String hashSection, String topicDescription, String cssClass, boolean parseRecursive);

	/**
	 * Append an InterWiki link
	 * 
	 * @param namespace
	 * @param title
	 * @param linkText
	 */
	public void appendInterWikiLink(String namespace, String title, String linkText);

	/**
	 * Append an external ISBN link (starting with <code>ISBN </code>) as
	 * described in <a
	 * href="http://en.wikipedia.org/wiki/Wikipedia:ISBN">Wikipedia:ISBN</a>
	 * 
	 * <br/><br/><b>Note:</b> The default implementation in the
	 * <code>AbstractWikiModel</code> class creates a link to <a
	 * href="http://amazon.com">Amazon.com</a>.
	 * 
	 * @param isbnPureText
	 *          the pure ISBN string which contains the ISBN prefix and optional
	 *          dashes in the ISBN number
	 */
	public void appendISBNLink(String isbnPureText);

	/**
	 * Append an external link (starting with mailto:...) as described in <a href
	 * ="http://en.wikipedia.org/wiki/Help:Wikitext#Links">Help:Wikitext#Links
	 * </a>
	 * 
	 * @param link
	 *          the external link with <code>mailto:</code> prefix
	 * @param linkName
	 *          the link name which is separated from the URL by a space
	 * @param withoutSquareBrackets
	 *          if <code>true</code> a mailto link with no square brackets around
	 *          the link was parsed
	 */
	public void appendMailtoLink(String link, String linkName, boolean withoutSquareBrackets);

	/**
	 * Check if the topic is a special namespace topic. In the
	 * <code>AbstractWikiModel</code> defaults implementation this namespace topic
	 * is parsed and checks for various namespaces, like <code>Categories</code>
	 * and <code>Interwiki</code> links.
	 * 
	 * @param rawNamespaceTopic
	 *          the text between the [[...]] square brackets of a wiki link before
	 *          the pipe symbol
	 * @param viewableLinkDescription
	 * @param containsNoPipe
	 *          set to <code>true</code> if the rawLinkText contained no pipe
	 *          symbol.
	 * @return <code>true</code> if the topic is a special namespace topic
	 */
	public boolean appendRawNamespaceLinks(String rawNamespaceTopic, String viewableLinkDescription, boolean containsNoPipe);

	/**
	 * Main entry method for parsing a raw wiki link (i.e. the text between the
	 * [[...]] square brackets). In the <code>AbstractWikiModel</code> defaults
	 * implementation this link is parsed and the various other
	 * <code>append...</code> methods of the model are called for the different
	 * cases.
	 * 
	 * @param rawLinkText
	 *          the text between the [[...]] square brackets of a wiki link
	 * @param suffix
	 *          a String of lowercase letters which directly follow the link after
	 *          the closing ]] brackets. Useful for topic plurals.
	 */
	public void appendRawWikipediaLink(String rawLinkText, String suffix);

	/**
	 * Append the redirect link to the model
	 * 
	 * @param redirectLink
	 *          the raw string between the wikilink tags <code>[[ ]]</code>
	 * @return <code>true</code> if the the wikitext shouldn't be parsed
	 */
	public boolean appendRedirectLink(String redirectLink);

	/**
	 * Append the user signature to the writer (i.e. '~~~', '~~~~' or '~~~~').
	 * This method is used in the first pass of parsing the wiki text (together
	 * with template parsing). You can append a [[User:xxxx]] string according to
	 * your model.
	 * 
	 * 
	 * @param writer
	 * @param numberOfTildes
	 *          an <code>int</code> value between 3 and 5
	 * @return
	 * @throws IOException
	 */
	public void appendSignature(Appendable writer, int numberOfTildes) throws IOException;

	/**
	 * Append the given tag stack to the current tag stack of the model.
	 * 
	 * @param stack
	 */
	public void appendStack(TagStack stack);

	/**
	 * Build the link to edit a section of the wikipedia article
	 * 
	 * @param section
	 */
	public void buildEditLinkUrl(int section);

	/**
	 * Create a new parser instance
	 * 
	 * @param rawWikitext
	 * @return
	 */
	public AbstractParser createNewInstance(String rawWikitext);

	/**
	 * Create the &quot;table of content&quot; placeholder
	 * 
	 * @param isTOCIdentifier
	 *          <code>true</code> if the __TOC__ keyword was parsed
	 * @return the &quot;table of content&quot; tag
	 */
	public ITableOfContent createTableOfContent(boolean isTOCIdentifier);

	/**
	 * Decrement the current recursion level of the parser. The recursion level is
	 * used to prevent infinite nesting of templates, tables, lists and other
	 * parser objects.
	 * 
	 * @return the decremented recursion level
	 */
	public int decrementRecursionLevel();

	/**
	 * Encode the <i>wiki links title</i> into a URL for HTML hyperlinks (i.e.
	 * create the <i>href</i> attribute representation for the <i>a</i> tag). This
	 * method uses the '.' character to encode special characters. To get the <a
	 * href="http://meta.wikimedia.org/wiki/Help:Page_name#Case-sensitivity"
	 * >behavior of the MediaWiki software</a>, which is configured to convert the
	 * first letter to upper case, the <code>firstCharacterAsUpperCase</code>
	 * parameters must be set to <code>true</code>. For an example encoding
	 * routine see:
	 * {@link info.bliki.wiki.filter.Encoder#encodeTitleDotUrl(String, boolean)}
	 * 
	 * @param firstCharacterAsUpperCase
	 *          if <code>true</code> convert the first of the title to uppercase
	 * 
	 * @see info.bliki.wiki.filter.Encoder#encodeTitleToUrl(String, boolean)
	 */
	public String encodeTitleDotUrl(String wikiTitle, boolean firstCharacterAsUpperCase);

	/**
	 * Encode the <i>wiki links title</i> into a URL for HTML hyperlinks (i.e.
	 * create the <i>href</i> attribute representation for the <i>a</i> tag). This
	 * method uses the '%' character to encode special characters. To get the <a
	 * href="http://meta.wikimedia.org/wiki/Help:Page_name#Case-sensitivity"
	 * >behavior of the MediaWiki software</a>, which is configured to convert the
	 * first letter to upper case, the <code>firstCharacterAsUpperCase</code>
	 * parameters must be set to <code>true</code>. For an example encoding
	 * routine see
	 * {@link info.bliki.wiki.filter.Encoder#encodeTitleToUrl(String, boolean)}
	 * 
	 * 
	 * @param firstCharacterAsUpperCase
	 *          if <code>true</code> convert the first of the title to uppercase
	 * 
	 * @see info.bliki.wiki.filter.Encoder#encodeTitleToUrl(String, boolean)
	 */
	public String encodeTitleToUrl(String wikiTitle, boolean firstCharacterAsUpperCase);

	/**
	 * Get the secondary namespace (i.e. the namespace for a non-englich locale)
	 * for categories in this wiki
	 * 
	 * @return the secondary category namespace
	 */
	public String get2ndCategoryNamespace();

	/**
	 * Get the secondary namespace (i.e. the namespace for a non-englich locale)
	 * for images in this wiki
	 * 
	 * @return the secondary image namespace
	 */
	public String get2ndImageNamespace();

	/**
	 * Get the secondary namespace (i.e. the namespace for a non-englich locale)
	 * for templates in this wiki
	 * 
	 * @return the secondary template namespace
	 */
	public String get2ndTemplateNamespace();

	/**
	 * Get the primary namespace for categories in this wiki
	 * 
	 * @return the primary category namespace
	 */
	public String getCategoryNamespace();

	/**
	 * Get the primary namespace for images in this wiki
	 * 
	 * @return the primary image namespace
	 */
	public String getImageNamespace();

	/**
	 * Get the set of Wikipedia link names
	 * 
	 * @return the <code>Set</code> of link names 8i.e. [[...]] links)
	 */
	public Set<String> getLinks();

	/**
	 * Get the next unique number
	 * 
	 * @return the next <code>int</code> number.
	 */
	public int getNextNumber();

	/**
	 * Get the node at the given offset on the internal stack For example
	 * <code>getNode(fWikiModel.stackSize() - 2)</code> returns the node before
	 * the node at top of the stack.
	 * 
	 * @param offset
	 * @return the node at the given offset
	 */
	public TagToken getNode(int offset);

	/**
	 * Get the title of the current wiki article.
	 * 
	 * @return
	 */
	public String getPageName();

	/**
	 * Get the raw wiki text for the given namespace and article name
	 * 
	 * @param namespace
	 *          the namespace of this article
	 * @param templateName
	 *          the name of the template
	 * @param templateParameters
	 *          if the namespace is the <b>Template</b> namespace, the current
	 *          template parameters are stored as <code>String</code>s in this map
	 * 
	 * @return <code>null</code> if no content was found
	 */
	public String getRawWikiContent(String namespace, String templateName, Map<String, String> templateParameters);

	/**
	 * Get the current recursion level of the parser. The recursion level is used
	 * to prevent infinite nesting of templates, tables, lists and other parser
	 * objects.
	 * 
	 * @return the current recursion level counter
	 */
	public int getRecursionLevel();

	/**
	 * Get the redirect link.
	 * 
	 * @return the raw string between the wikilink tags <code>[[ ]]</code> or
	 *         <code>null</code> if no redirect exists
	 */
	public String getRedirectLink();

	/**
	 * Get the internal list of references (i.e. footnotes)
	 * 
	 * 
	 * @return the list of references or <code>null</code> if no reference exists
	 * @see Reference
	 */
	public List<Reference> getReferences();

	/**
	 * Get the resource bundle associated with this model for I18N support
	 * 
	 * @return the currently used resource bundle for this wiki model
	 */
	public ResourceBundle getResourceBundle();

	/**
	 * Get the list of SemanticAttributes
	 * 
	 * @return the list of SemanticAttributes or <code>null</code> if no
	 *         SemanticAttribute exists
	 */
	public List<SemanticAttribute> getSemanticAttributes();

	/**
	 * Get the list of SemanticRelations.
	 * 
	 * See <a href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
	 * MediaWiki</a> for more information.
	 * 
	 * @return the list of SemanticRelations or <code>null</code> if no
	 *         SemanticRelation exists
	 */
	public List<SemanticRelation> getSemanticRelations();

	/**
	 * Get a template parser function (i.e. <code>{{ #if: ... }}</code> )
	 * implementation.
	 * 
	 * @param name
	 *          the name of the function without the &quot;#&quot; and
	 *          &quot;:&quot; delimiters
	 * @return the parser function or <code>null</code> if no function is
	 *         available for the given name
	 */
	public ITemplateFunction getTemplateFunction(String name);

	/**
	 * Get the primary namespace for templates in this wiki model
	 * 
	 * @return the primary namespace for templates
	 */
	public String getTemplateNamespace();

	/**
	 * Get the current defined wiki listener
	 * 
	 * @return the wiki listener or <code>null</code> if no listener is defined
	 */
	public IEventListener getWikiListener();

	/**
	 * Increment the current recursion level of the parser. The recursion level is
	 * used to prevent infinite nesting of templates, tables, lists and other
	 * parser objects.
	 * 
	 * @return the current recursion level counter
	 */
	public int incrementRecursionLevel();

	/**
	 * Checks if <a href="http://en.wikipedia.org/wiki/CamelCase">CamelCase</a>
	 * words should also be used as wiki links.
	 * 
	 * 
	 * @return <code>true</code> if CamelCase words should also be used as wiki
	 *         links
	 */
	public boolean isCamelCaseEnabled();

	/**
	 * Check if the given namespace is a category namespace
	 * 
	 * @param namespace
	 * @return <code>true</code> if the namespace is a category namespace.
	 */
	public boolean isCategoryNamespace(String namespace);

	/**
	 * The current model is used to render a wikipage in editor mode
	 * 
	 * @return <code>true</code> if your model is used in an editor mode
	 */
	public boolean isEditorMode();

	/**
	 * Check if the given namespace is a image namespace
	 * 
	 * @param namespace
	 * @return <code>true</code> if the namespace is a image namespace.
	 */
	public boolean isImageNamespace(String namespace);

	/**
	 * Check if the given namespace for an interwiki link
	 * 
	 * @param namespace
	 * @return <code>true</code> if the namespace is a interwiki namespace (i.e.
	 *         prefix).
	 */
	public boolean isInterWiki(String namespace);

	/**
	 * If the <code>&lt;math&gt;</code> tag should be rendered for the <a
	 * href="http://www.mathtran.org">www.mathtran.org</a> service, then return
	 * <code>true</code>.
	 * 
	 * @return <code>true</code> the <code>&lt;math&gt;</code> tag should be
	 *         rendered fro mathtran.org.
	 */
	public boolean isMathtranRenderer();

	/**
	 * Check if the given namespace is a namespace in this model
	 * 
	 * @param namespace
	 * @return <code>true</code> if the namespace is a namespace in this model
	 */
	public boolean isNamespace(String namespace);

	/**
	 * The current model is used to render a wikipage in preview mode
	 * 
	 * @return <code>true</code> if your model is used in a preview mode
	 */
	public boolean isPreviewMode();

	/**
	 * Allow the parsing of semantic mediawiki (SMW) links. See <a
	 * href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
	 * MediaWiki</a> for more information.
	 * 
	 * @param namespace
	 * @return <code>true</code> if parsing of semantic mediawiki (SMW) links is
	 *         enabled
	 */
	public boolean isSemanticWebActive();

	/**
	 * Check if the given namespace is a template namespace
	 * 
	 * @param namespace
	 * @return <code>true</code> if the namespace is a template namespace.
	 */
	public boolean isTemplateNamespace(String namespace);

	/**
	 * Determine if the currently parsed wiki text is a template.
	 * 
	 * @return <code>true</code> if the currently parsed wiki text is a template
	 */
	public boolean isTemplateTopic();

	/**
	 * Check if the top level URI (Uniform Resource Identifier) scheme name is
	 * valid in this model.
	 * 
	 * See <a href="http://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
	 * 
	 * @param uriScheme
	 *          the top level URI (Uniform Resource Identifier) scheme name
	 *          (without the following colon character ":")
	 * @return <code>true</code> if the specified URI scheme is valid.
	 * @see IWikiModel#isValidUriSchemeSpecificPart(String, String)
	 */
	public boolean isValidUriScheme(String uriScheme);

	/**
	 * Check if the scheme-specific part for a given top level URI (Uniform
	 * Resource Identifier) scheme name is valid in this model.
	 * 
	 * See <a href="http://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
	 * 
	 * @param uriScheme
	 *          the top level URI (Uniform Resource Identifier) scheme name
	 *          (without the following colon character ":")
	 * @param uriSchemeSpecificPart
	 *          the URI (Uniform Resource Identifier) scheme part following the
	 *          top level scheme name and the colon character ":"
	 * @return <code>true</code> if the specified URI scheme is valid.
	 * @see IWikiModel#isValidUriScheme(String)
	 */
	public boolean isValidUriSchemeSpecificPart(String uriScheme, String uriSchemeSpecificPart);

	/**
	 * Parse BBCode (Bulletin Board Code), like syntax codes in this wiki. See <a
	 * href="http://en.wikipedia.org/wiki/BBCode">http://en.wikipedia.org/wiki
	 * /BBCode</a> for more information.
	 * 
	 * @return <code>true</code> if the bbCodes should be parsed
	 */
	public boolean parseBBCodes();

	/**
	 * Parse the raw Wikipedia text and notify the listener
	 * 
	 * @param listener
	 *          an event listener
	 * @param rawWikiText
	 *          the raw wiki text
	 */
	public void parseEvents(IEventListener listener, String rawWikiText);

	/**
	 * Append the internal wiki image link to this model. <br/><br/>See <a
	 * href="http://en.wikipedia.org/wiki/Image_markup">Image markup</a> and see
	 * <a href="http://www.mediawiki.org/wiki/Help:Images">Help:Images</a>.
	 * 
	 * @param imageNamespace
	 *          the image namespace
	 * @param rawImageLink
	 *          the raw image link text without the surrounding
	 *          <code>[[...]]</code>
	 */
	public void parseInternalImageLink(String imageNamespace, String rawImageLink);

	/**
	 * Parse the templates in a raw wikipedia text into a resulting wikipedia
	 * text.
	 * 
	 * @param rawWikiText
	 * @return
	 */
	public String parseTemplates(String rawWikiText);

	/**
	 * Parse the templates in a raw wikipedia text into a resulting wikipedia
	 * text.
	 * 
	 * @param rawWikiText
	 * @param parseOnlySignature
	 *          if <code>true</code> parse only the signature wiki texts, no
	 *          templates and wiki comment parsing
	 * @return
	 */
	public String parseTemplates(String rawWikiText, boolean parseOnlySignature);

	/**
	 * Get the current TagNode on top of the internal stack
	 * 
	 * @return the current node
	 */
	public TagToken peekNode();

	/**
	 * Pop the current TagNode from top of the internal stack
	 * 
	 */
	public TagToken popNode();

	/**
	 * Push the given TagNode on top of the internal stack
	 * 
	 * @return <code>true</code> if the push on the internal stack was successful
	 */
	public boolean pushNode(TagToken node);

	/**
	 * Render the raw Wikipedia text into a string for a given converter
	 * 
	 * @param converter
	 *          a text converter. <b>Note</b> the converter may be
	 *          <code>null</code>, if you only would like to analyze the raw wiki
	 *          text and don't need to convert. This speeds up the parsing
	 *          process.
	 * @param rawWikiText
	 *          a raw wiki text
	 * @return <code>null</code> if an IOException occurs or
	 *         <code>converter==null</code>
	 * @return
	 */
	public String render(ITextConverter converter, String rawWikiText);

	/**
	 * Render the raw Wikipedia text into an HTML string and use the default
	 * HTMLConverter
	 * 
	 * @param rawWikiText
	 * @return <code>null</code> if an IOException occurs
	 */
	public String render(String rawWikiText);

	/**
	 * Render the raw Wikipedia text into an HTML string and use the default
	 * PDFConverter. The resulting XHTML could be used as input for the Flying
	 * Saucer PDF renderer
	 * 
	 * @param rawWikiText
	 * @return <code>null</code> if an IOException occurs
	 */
	public String renderPDF(String rawWikiText);

	/**
	 * Replace a colon ':' with a slash '/' in wiki names (i.e. links, categories,
	 * templates)
	 * 
	 * @return
	 */
	public boolean replaceColon();

	/**
	 * Set the title of the curretly rendered page data.
	 * 
	 * @param pageTitle
	 */
	public void setPageName(String pageTitle);

	/**
	 * Activate the parsing of semantic Mediawiki (SMW) links See <a
	 * href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
	 * MediaWiki</a> for more information.
	 * 
	 */
	public void setSemanticWebActive(boolean semanticWeb);

	/**
	 * Prepare or initialize the wiki model before rendering the wikipedia text
	 * 
	 */
	public void setUp();

	/**
	 * Show the syntax highlighting of the source code
	 * 
	 * @return
	 */
	public boolean showSyntax(String tagName);

	/**
	 * The size of the internal stack
	 * 
	 * @return
	 */
	public int stackSize();

	public TagStack swapStack(TagStack stack);

	/**
	 * Clean up (i.e. free internal resources) in the wiki model after rendering
	 * the wikipedia text, if necessary
	 * 
	 */
	public void tearDown();

	public INamespace getNamespace();
	
}
