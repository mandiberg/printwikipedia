package info.bliki.wiki.model;

import info.bliki.extensions.scribunto.engine.ScribuntoEngine;
import info.bliki.extensions.scribunto.template.Frame;
import info.bliki.htmlcleaner.BaseToken;
import info.bliki.htmlcleaner.TagToken;
import info.bliki.wiki.filter.AbstractParser;
import info.bliki.wiki.filter.AbstractWikipediaParser;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.filter.ParsedPageName;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.namespaces.INamespace.INamespaceValue;
import info.bliki.wiki.tags.util.TagStack;
import info.bliki.wiki.template.ITemplateFunction;

import javax.annotation.Nullable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
     * specific category this method should be called to add that category to
     * the output metadata. For the <code>sortKey</code> see also <a href=
     * "http://en.wikipedia.org/wiki/Wikipedia:Categorization#Category_sorting"
     * >Wikipedia:Categorization#Category_sorting</a>
     *
     * @param categoryName
     *            The name of the category that the document belongs to.
     * @param sortKey
     *            The sort key for the category, or <code>null</code> if no sort
     *            key has been specified. The sort key determines what order
     *            categories are sorted on category index pages, so a category
     *            for "John Doe" might be given a sort key of "Doe, John".
     */
    void addCategory(String categoryName, String sortKey);

    /**
     * When a document contains a token indicating that the document links to
     * another Wiki topic this method should be called to add that topic link to
     * the output metadata.
     *
     * @param topicName
     *            The name of the topic that is linked to.
     */
    void addLink(String topicName);

    /**
     * See <a href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
     * MediaWiki</a> for more information.
     *
     * @param attribute
     * @param attributeValue
     * @return
     */
    boolean addSemanticAttribute(String attribute, String attributeValue);

    /**
     * See <a href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
     * MediaWiki</a> for more information.
     *
     * @param relation
     * @param relationValue
     * @return
     */
    boolean addSemanticRelation(String relation, String relationValue);

    /**
     * When a document contains a token indicating that the document includes a
     * Wiki template this method should be called to add that template to the
     * output metadata.
     *
     * @param template
     *            The name of the template that is being included (excluding the
     *            template namespace).
     */
    void addTemplate(String template);

    /**
     * When a document contains a token indicating that the document includes an
     * other Wiki page, i.e. transclusion, this method should be called to add
     * that page to the output metadata.
     *
     * This excludes transcluded template pages.
     *
     * @param pageName
     *            The name of the page that is being included (including its
     *            namespace).
     *
     * @see #addTemplate(String)
     */
    void addInclude(String pageName);

    /**
     * Add a reference (i.e. footnote) to the internal list
     *
     * @param reference
     *            the rendered HTML code of the ref-Tag body
     * @param nameAttribute
     *            the value of the <code>name</code> attribute or
     *            <code>null</code>
     * @return the current offset (i.e. size()-1) of the element in the list
     */
    String[] addToReferences(String reference, String nameAttribute);

    /**
     * Append the content as a child on the top node of the internal stack
     *
     * @param contentNode
     * @return <code>true</code> if the append was successful
     */
    void append(BaseToken contentNode);

    /**
     * Append an external wiki image link (starting with http, https,... and
     * ending with gif, png, jpg, bmp)
     *
     * @param imageSrc
     * @param hashSection
     * @param imageAltText
     */
    void appendExternalImageLink(String imageSrc, String imageAltText);

    /**
     * Append an external link (starting with http, https, ftp,...) as described
     * in <a href="http://en.wikipedia.org/wiki/Help:Link#External_links">Help
     * Links</a>
     *
     * @param uriSchemeName
     *            the top level URI (Uniform Resource Identifier) scheme name
     *            (without the following colon character ":"). Example "ftp",
     *            "http", "https". See <a
     *            href="http://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
     * @param link
     *            the external link with
     *            <code>http://, https:// or ftp://</code> prefix
     * @param linkName
     *            the link name which is separated from the URL by a space
     * @param withoutSquareBrackets
     *            if <code>true</code> a link with no square brackets around the
     *            link was parsed
     */
    void appendExternalLink(String uriSchemeName, String link,
                            String linkName, boolean withoutSquareBrackets);

    /**
     * Add a single wiki head (i.e. ==...==, ===...===, ====...====,...) to the
     * table of content
     *
     * @param rawHead
     *            the unparsed header string
     * @param headLevel
     *            level of header (i.e. h1, h2, h3, h4, 5h,..)
     * @param noToc
     *            don't show the &quot;table of content&quot;
     * @param headCounter
     *            the total number of headers parsed
     * @param startPosition
     *            the start index in the text where the header line of the
     *            section begins
     * @param endPosition
     *            the start index in the text where the header line of the
     *            section ends
     * @return the &quot;table of content&quot; tag
     */
    ITableOfContent appendHead(String rawHead, int headLevel,
                               boolean noToC, int headCounter, int startPosition, int endPosition);

    /**
     * Append this internal wiki image link. In the current implementation some
     * parts of the image rendering are moved to the
     * <code>HTMLConverter#imageNodeToText()</code> method.
     *
     * @param hrefImageLink
     * @param srcImageLink
     * @param imageFormat
     */
    void appendInternalImageLink(String hrefImageLink,
                                 String srcImageLink, ImageFormat imageFormat);

    /**
     * Append an internal wikilink as described in <a
     * href="http://en.wikipedia.org/wiki/Help:Link#Wikilinks">Help Links</a>
     *
     * @param topic
     * @param hashSection
     * @param topicDescription
     * @param cssClass
     *            the links CSS class style
     * @param parseRecursive
     *            TODO
     */
    void appendInternalLink(String topic, String hashSection,
                            String topicDescription, String cssClass, boolean parseRecursive);

    /**
     * Append an InterWiki link
     *
     * @param namespace
     * @param title
     * @param linkText
     */
    void appendInterWikiLink(String namespace, String title,
                             String linkText);

    /**
     * Append an external ISBN link (starting with <code>ISBN </code>) as
     * described in <a
     * href="http://en.wikipedia.org/wiki/Wikipedia:ISBN">Wikipedia:ISBN</a>
     *
     * <br/>
     * <br/>
     * <b>Note:</b> The default implementation in the
     * <code>AbstractWikiModel</code> class creates a link to <a
     * href="http://amazon.com">Amazon.com</a>.
     *
     * @param isbnPureText
     *            the pure ISBN string which contains the ISBN prefix and
     *            optional dashes in the ISBN number
     */
    void appendISBNLink(String isbnPureText);

    /**
     * Append an external link (starting with mailto:...) as described in <a
     * href
     * ="http://en.wikipedia.org/wiki/Help:Wikitext#Links">Help:Wikitext#Links
     * </a>
     *
     * @param link
     *            the external link with <code>mailto:</code> prefix
     * @param linkName
     *            the link name which is separated from the URL by a space
     * @param withoutSquareBrackets
     *            if <code>true</code> a mailto link with no square brackets
     *            around the link was parsed
     */
    void appendMailtoLink(String link, String linkName,
                          boolean withoutSquareBrackets);

    /**
     * Check if the topic is a special namespace topic. In the
     * <code>AbstractWikiModel</code> defaults implementation this namespace
     * topic is parsed and checks for various namespaces, like
     * <code>Categories</code> and <code>Interwiki</code> links.
     *
     * @param rawNamespaceTopic
     *            the text between the [[...]] square brackets of a wiki link
     *            before the pipe symbol
     * @param viewableLinkDescription
     * @param containsNoPipe
     *            set to <code>true</code> if the rawLinkText contained no pipe
     *            symbol.
     * @return <code>true</code> if the topic is a special namespace topic
     */
    boolean appendRawNamespaceLinks(String rawNamespaceTopic,
                                    String viewableLinkDescription, boolean containsNoPipe);

    /**
     * Main entry method for parsing a raw wiki link (i.e. the text between the
     * [[...]] square brackets). In the <code>AbstractWikiModel</code> defaults
     * implementation this link is parsed and the various other
     * <code>append...</code> methods of the model are called for the different
     * cases.
     *
     * @param rawLinkText
     *            the text between the [[...]] square brackets of a wiki link
     * @param suffix
     *            a String of lowercase letters which directly follow the link
     *            after the closing ]] brackets. Useful for topic plurals.
     * @return <code>true</code> if the method used the suffix for rendering the
     *         wiki link. If <code>false</code> the parser should append the
     *         suffix as normal text after the wiki link (i.e. in the case of an
     *         image of file wiki link).
     */
    boolean appendRawWikipediaLink(String rawLinkText, String suffix);

    /**
     * Append the redirect link to the model
     *
     * @param redirectLink
     *            the raw string between the wikilink tags <code>[[ ]]</code>
     * @return <code>true</code> if the the wikitext shouldn't be parsed
     */
    boolean appendRedirectLink(String redirectLink);

    /**
     * Append the user signature to the writer (i.e. '~~~', '~~~~' or '~~~~').
     * This method is used in the first pass of parsing the wiki text (together
     * with template parsing). You can append a [[User:xxxx]] string according
     * to your model.
     *
     *
     * @param writer
     * @param numberOfTildes
     *            an <code>int</code> value between 3 and 5
     * @return
     * @throws IOException
     */
    void appendSignature(Appendable writer, int numberOfTildes)
            throws IOException;

    /**
     * Append the given tag stack to the current tag stack of the model.
     *
     * @param stack
     */
    void appendStack(TagStack stack);

    /**
     * Build the link to edit a section of the wikipedia article
     *
     * @param section
     */
    void buildEditLinkUrl(int section);

    /**
     * Create a new parser instance
     *
     * @param rawWikitext
     * @return
     */
    AbstractWikipediaParser createNewInstance(String rawWikitext);

    /**
     * Create the &quot;table of content&quot; placeholder
     *
     * @param isTOCIdentifier
     *            <code>true</code> if the __TOC__ keyword was parsed
     * @return the &quot;table of content&quot; tag
     */
    ITableOfContent createTableOfContent(boolean isTOCIdentifier);

    /**
     * Decrement the current recursion level of the parser. The recursion level
     * is used to prevent infinite nesting of templates, tables, lists and other
     * parser objects.
     *
     * @return the decremented recursion level
     */
    int decrementRecursionLevel();

    /**
     * Decrement the current template recursion level of the temlate parser. The
     * recursion level is used to prevent infinite nesting of templates, tables,
     * lists and other parser objects.
     *
     * @return the decremented recursion level
     */
    int decrementTemplateRecursionLevel();

    /**
     * Encode the <i>wiki links title</i> into a URL for HTML hyperlinks (i.e.
     * create the <i>href</i> attribute representation for the <i>a</i> tag).
     * This method uses the '.' character to encode special characters. To get
     * the <a
     * href="http://meta.wikimedia.org/wiki/Help:Page_name#Case-sensitivity"
     * >behavior of the MediaWiki software</a>, which is configured to convert
     * the first letter to upper case, the
     * <code>firstCharacterAsUpperCase</code> parameters must be set to
     * <code>true</code>. For an example encoding routine see:
     * {@link info.bliki.wiki.filter.Encoder#encodeTitleDotUrl(String, boolean)}
     *
     * @param firstCharacterAsUpperCase
     *            if <code>true</code> convert the first of the title to
     *            uppercase
     *
     * @see info.bliki.wiki.filter.Encoder#encodeTitleToUrl(String, boolean)
     */
    String encodeTitleDotUrl(String wikiTitle,
                             boolean firstCharacterAsUpperCase);

    /**
     * Encode the <i>wiki links title</i> into a URL for HTML hyperlinks (i.e.
     * create the <i>href</i> attribute representation for the <i>a</i> tag).
     * This method uses the '%' character to encode special characters. To get
     * the <a
     * href="http://meta.wikimedia.org/wiki/Help:Page_name#Case-sensitivity"
     * >behavior of the MediaWiki software</a>, which is configured to convert
     * the first letter to upper case, the
     * <code>firstCharacterAsUpperCase</code> parameters must be set to
     * <code>true</code>. For an example encoding routine see
     * {@link info.bliki.wiki.filter.Encoder#encodeTitleToUrl(String, boolean)}
     *
     *
     * @param firstCharacterAsUpperCase
     *            if <code>true</code> convert the first of the title to
     *            uppercase
     *
     * @see info.bliki.wiki.filter.Encoder#encodeTitleToUrl(String, boolean)
     */
    String encodeTitleToUrl(String wikiTitle,
                            boolean firstCharacterAsUpperCase);

    /**
     * Get the current time stamp. This is the value for the magic word
     * &quot;CURRENTTIMESTAMP&quot;.
     *
     * This method typically returns the value
     * <code>new Date(System.currentTimeMillis());</code>.
     *
     * @return a date value
     */
    Date getCurrentTimeStamp();

    /**
     * Return a URL string which contains a &quot;${image}&quot; variable, which
     * will be replaced by the image name, to create links to images.
     *
     * @return the wiki images URL
     *
     * @see #getWikiBaseURL()
     * @see #getWikiBaseEditURL()
     */
    String getImageBaseURL();

    /**
     * Get the set of Wikipedia link names
     *
     * @return the <code>Set</code> of link names 8i.e. [[...]] links)
     */
    Set<String> getLinks();

    /**
     * Get the locale of this model.
     *
     * @return the locale for this model.
     */
    Locale getLocale();

    /**
     * Get the namespace of this model.
     *
     * @return the namespace for this model
     */
    INamespace getNamespace();

    /**
     * Get the namespace name of this model for the current locale.
     *
     * @return the namespace in the current locale for this model
     */
    String getNamespaceName();

    /**
     * Get the next unique number
     *
     * @return the next <code>int</code> number.
     */
    int getNextNumber();

    /**
     * Get the node at the given offset on the internal stack For example
     * <code>getNode(fWikiModel.stackSize() - 2)</code> returns the node before
     * the node at top of the stack.
     *
     * @param offset
     * @return the node at the given offset
     */
    TagToken getNode(int offset);

    // scribunto stuff
    Frame getFrame();
    void setFrame(Frame frame);

    ScribuntoEngine createScribuntoEngine();

    /**
     * Get the title of the current wiki article.
     *
     * @return
     */
    String getPageName();

    /**
     * Get the raw wiki text for the given namespace and article name
     *
     * @param templateName
     *            the parsed template name
     * @param templateParameters
     *            if the namespace is the <b>Template</b> namespace, the current
     *            template parameters are stored as <code>String</code>s in this
     *            map
     *
     * @return <code>null</code> if no content was found
     * @see AbstractParser#parsePageName(IWikiModel, String, INamespaceValue,
     *      boolean)
     */
    @Nullable String getRawWikiContent(ParsedPageName templateName,
                                       Map<String, String> templateParameters)
            throws WikiModelContentException;

    /**
     * Get the current recursion level of the parser. The recursion level is
     * used to prevent infinite nesting of templates, tables, lists and other
     * parser objects.
     *
     * @return the current recursion level counter
     */
    int getRecursionLevel();

    /**
     * Get the redirect link.
     *
     * @return the raw string between the wikilink tags <code>[[ ]]</code> or
     *         <code>null</code> if no redirect exists
     */
    String getRedirectLink();

    /**
     * Get the internal list of references (i.e. footnotes)
     *
     *
     * @return the list of references or <code>null</code> if no reference
     *         exists
     * @see Reference
     */
    List<Reference> getReferences();

    /**
     * Get the resource bundle associated with this model for I18N support
     *
     * @return the currently used resource bundle for this wiki model
     */
    ResourceBundle getResourceBundle();

    /**
     * Get the list of SemanticAttributes
     *
     * @return the list of SemanticAttributes or <code>null</code> if no
     *         SemanticAttribute exists
     */
    List<SemanticAttribute> getSemanticAttributes();

    /**
     * Get the list of SemanticRelations.
     *
     * See <a href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
     * MediaWiki</a> for more information.
     *
     * @return the list of SemanticRelations or <code>null</code> if no
     *         SemanticRelation exists
     */
    List<SemanticRelation> getSemanticRelations();

    /**
     * Get a simple date formatter.
     *
     *
     * @return a simple date formatter
     */
    SimpleDateFormat getSimpleDateFormat();

    /**
     * Get the parsed &quot;table of content&quot; data after parsing the
     * Wikipedia text.
     *
     * @return
     */
    ITableOfContent getTableOfContent();

    /**
     * Get a template parser function (i.e. <code>{{ #if: ... }}</code> )
     * implementation.
     *
     * @param name
     *            the name of the function without the &quot;#&quot; and
     *            &quot;:&quot; delimiters
     * @return the parser function or <code>null</code> if no function is
     *         available for the given name
     */
    ITemplateFunction getTemplateFunction(String name);

    /**
     * Return a URL string which contains, a &quot;${title}&quot; variable which
     * will be replaced by the topic title, to create links edit pages of wiki
     * topics.
     *
     * For the english Wikipedia this URL would look like:
     *
     * <pre>
     * http://en.wikipedia.org/w/index.php?title=${title}
     * </pre>
     *
     * @return the wiki articles edit action URL
     *
     * @see #getWikiBaseURL()
     * @see #getImageBaseURL()
     */
    String getWikiBaseEditURL();

    /**
     * Return a URL string which contains, a &quot;${title}&quot; variable which
     * will be replaced by the topic title, to create links to other wiki
     * topics.
     *
     * For the english Wikipedia this URL would look like:
     *
     * <pre>
     * http://en.wikipedia.org/wiki/${title}
     * </pre>
     *
     * @return the wiki articles URL
     *
     * @see #getImageBaseURL()
     * @see #getWikiBaseEditURL()
     */
    String getWikiBaseURL();

    /**
     * Get the current defined wiki listener
     *
     * @return the wiki listener or <code>null</code> if no listener is defined
     */
    IEventListener getWikiListener();

    /**
     * Increment the current recursion level of the parser. The recursion level
     * is used to prevent infinite nesting of templates, tables, lists and other
     * parser objects.
     *
     * @return the current recursion level counter
     */
    int incrementRecursionLevel();

    /**
     * Increment the current recursion level of the template parser. The
     * recursion level is used to prevent infinite nesting of templates.
     *
     * @return the current recursion level counter
     */
    int incrementTemplateRecursionLevel();

    /**
     * Checks if <a href="http://en.wikipedia.org/wiki/CamelCase">CamelCase</a>
     * words should also be used as wiki links.
     *
     *
     * @return <code>true</code> if CamelCase words should also be used as wiki
     *         links
     */
    boolean isCamelCaseEnabled();

    /**
     * The current model is used to render a wikipage in editor mode
     *
     * @return <code>true</code> if your model is used in an editor mode
     */
    boolean isEditorMode();

    /**
     * Check if the rendering of the &quot;table of contents&quot; is disabled
     * globally.
     *
     * @return <code>true</code> if the rendering of the &quot;table of
     *         contents&quot; is disabled globally.
     */
    boolean isNoToc();

    /**
     * Check if the given namespace is an interwiki link prefix.
     *
     * @param namespace
     * @return <code>true</code> if the namespace is a interwiki namespace (i.e.
     *         prefix).
     */
    boolean isInterWiki(String namespace);

    /**
     * If the <code>&lt;math&gt;</code> tag should be rendered for the <a
     * href="http://www.mathtran.org">www.mathtran.org</a> service, then return
     * <code>true</code>.
     *
     * @return <code>true</code> the <code>&lt;math&gt;</code> tag should be
     *         rendered fro mathtran.org.
     */
    boolean isMathtranRenderer();

    /**
     * Check if the given namespace is a namespace in this model
     *
     * @param namespace
     * @return <code>true</code> if the namespace is a namespace in this model
     */
    boolean isNamespace(String namespace);

    /**
     * The current model currently renders a template parameter value
     *
     * @return <code>true</code> if your model renders a template parameter
     *         value
     */
    boolean isParameterParsingMode();

    /**
     * The current model is used to render a wikipage in preview mode
     *
     * @return <code>true</code> if your model is used in a preview mode
     */
    boolean isPreviewMode();

    /**
     * Allow the parsing of semantic mediawiki (SMW) links. See <a
     * href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
     * MediaWiki</a> for more information.
     *
     * @param namespace
     * @return <code>true</code> if parsing of semantic mediawiki (SMW) links is
     *         enabled
     */
    boolean isSemanticWebActive();

    /**
     * Determine if the currently parsed wiki text is a template.
     *
     * @return <code>true</code> if the currently parsed wiki text is a
     *         template.
     */
    boolean isTemplateTopic();

    /**
     * Check if the top level URI (Uniform Resource Identifier) scheme name is
     * valid in this model.
     *
     * See <a href="http://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
     *
     * @param uriScheme
     *            the top level URI (Uniform Resource Identifier) scheme name
     *            (without the following colon character ":")
     * @return <code>true</code> if the specified URI scheme is valid.
     * @see IWikiModel#isValidUriSchemeSpecificPart(String, String)
     */
    boolean isValidUriScheme(String uriScheme);

    /**
     * Check if the scheme-specific part for a given top level URI (Uniform
     * Resource Identifier) scheme name is valid in this model.
     *
     * See <a href="http://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
     *
     * @param uriScheme
     *            the top level URI (Uniform Resource Identifier) scheme name
     *            (without the following colon character ":")
     * @param uriSchemeSpecificPart
     *            the URI (Uniform Resource Identifier) scheme part following
     *            the top level scheme name and the colon character ":"
     * @return <code>true</code> if the specified URI scheme is valid.
     * @see IWikiModel#isValidUriScheme(String)
     */
    boolean isValidUriSchemeSpecificPart(String uriScheme,
                                         String uriSchemeSpecificPart);

    /**
     * Parse a behavior switch (i.e. an identifier with two leading and trailing
     * underscores &quot;__&quot; like for example
     * <code>__NOEDITSECTION__</code> ). See <a href
     * ="http://www.mediawiki.org/wiki/Help:Magic_words#Behavior_switches">Help
     * :Magic_words#Behavior_switches</a>.
     *
     * @param identifier
     *            the identifier without the leading and trailing underscores
     *            (&quot;__&quot;)
     * @return <code>true</code> if the switch was parsed
     */
    boolean parseBehaviorSwitch(String identifier);

    /**
     * Parse the raw Wikipedia text and notify the listener
     *
     * @param listener
     *            an event listener
     * @param rawWikiText
     *            the raw wiki text
     */
    void parseEvents(IEventListener listener, String rawWikiText);

    /**
     * Append the internal wiki image link to this model. <br/>
     * <br/>
     * See <a href="http://en.wikipedia.org/wiki/Image_markup">Image markup</a>
     * and see <a
     * href="http://www.mediawiki.org/wiki/Help:Images">Help:Images</a>.
     *
     * @param imageNamespace
     *            the image namespace
     * @param rawImageLink
     *            the raw image link text without the surrounding
     *            <code>[[...]]</code>
     */
    void parseInternalImageLink(String imageNamespace,
                                String rawImageLink);

    /**
     * Parse the templates in a raw wikipedia text into a resulting wikipedia
     * text.
     *
     * @param rawWikiText
     * @return
     */
    String parseTemplates(String rawWikiText);

    /**
     * Parse the templates in a raw wikipedia text into a resulting wikipedia
     * text.
     *
     * @param rawWikiText
     * @param parseOnlySignature
     *            if <code>true</code> parse only the signature wiki texts, no
     *            templates and wiki comment parsing
     * @return
     */
    String parseTemplates(String rawWikiText, boolean parseOnlySignature);

    /**
     * Get the current TagNode on top of the internal stack
     *
     * @return the current node
     */
    TagToken peekNode();

    /**
     * Pop the current TagNode from top of the internal stack
     *
     */
    TagToken popNode();

    /**
     * Push the given TagNode on top of the internal stack
     *
     * @return <code>true</code> if the push on the internal stack was
     *         successful
     */
    boolean pushNode(TagToken node);

    /**
     * Reduce the current token stack until an allowed parent is at the top of
     * the stack
     */
    void reduceTokenStack(TagToken node);

    /**
     * Render the raw Wikipedia text into a string for a given converter
     *
     * @param converter
     *            a text converter. <b>Note</b> the converter may be
     *            <code>null</code>, if you only would like to analyze the raw
     *            wiki text and don't need to convert. This speeds up the
     *            parsing process.
     * @param rawWikiText
     *            a raw wiki text
     * @return <code>null</code> if an IOException occurs or
     *         <code>converter==null</code>
     * @return
     */
    String render(ITextConverter converter, String rawWikiText);

    /**
     * Render the raw Wikipedia text into a string for a given converter
     *
     * @param converter
     *            a text converter. <b>Note</b> the converter may be
     *            <code>null</code>, if you only would like to analyze the raw
     *            wiki text and don't need to convert. This speeds up the
     *            parsing process.
     * @param rawWikiText
     *            a raw wiki text
     * @param buffer
     *            write to this buffer
     * @param templateTopic
     *            if <code>true</code>, render the wiki text as if a template
     *            topic will be displayed directly, otherwise render the text as
     *            if a common wiki topic will be displayed.
     * @param parseTemplates
     *            parses the template expansion step (parses include,
     *            onlyinclude, includeonly etc)
     */
    void render(ITextConverter converter, String rawWikiText,
                Appendable buffer, boolean templateTopic, boolean parseTemplates)
            throws IOException;

    /**
     * Render the raw Wikipedia text into a string for a given converter
     *
     * @param converter
     *            a text converter. <b>Note</b> the converter may be
     *            <code>null</code>, if you only would like to analyze the raw
     *            wiki text and don't need to convert. This speeds up the
     *            parsing process.
     * @param rawWikiText
     *            a raw wiki text
     * @param templateTopic
     *            if <code>true</code>, render the wiki text as if a template
     *            topic will be displayed directly, otherwise render the text as
     *            if a common wiki topic will be displayed.
     * @return <code>null</code> if an IOException occurs or
     *         <code>converter==null</code>
     * @return
     */
    String render(ITextConverter converter, String rawWikiText,
                  boolean templateTopic);

    /**
     * Render the raw Wikipedia text into an HTML string and use the default
     * HTMLConverter
     *
     * @param rawWikiText
     * @return <code>null</code> if an IOException occured
     */
    String render(String rawWikiText);

    /**
     * Render the raw Wikipedia text into an HTML string and use the default
     * HTMLConverter
     *
     * @param rawWikiText
     * @param templateTopic
     *            if <code>true</code>, render the wiki text as if a template
     *            topic will be displayed directly, otherwise render the text as
     *            if a common wiki topic will be displayed.
     * @return <code>null</code> if an IOException occurs
     */
    String render(String rawWikiText, boolean templateTopic);

    /**
     * Render the raw Wikipedia text into an HTML string and use the default
     * PDFConverter. The resulting XHTML could be used as input for the Flying
     * Saucer PDF renderer
     *
     * @param rawWikiText
     * @return <code>null</code> if an IOException occurs
     */
    String renderPDF(String rawWikiText);

    /**
     * Replace a colon ':' with a slash '/' in wiki names (i.e. links,
     * categories, templates)
     *
     * @return
     */
    boolean replaceColon();

    /**
     * Set the "lower-case" namespace name of the article rendered with this
     * model. This name will be converted with the Namespace#getNamespace()
     * method to a string in the current Locale.
     *
     * @param namespaceLowercase
     *            the lowercase key for the namespace.
     * @return the namespace for this model
     * @see java.util.Locale
     * @see info.bliki.wiki.namespaces.Namespace#getNamespace(String)
     */
    void setNamespaceName(String namespaceLowercase);

    /**
     * Set to <code>true</code> if the rendering of the &quot;table of
     * contents&quot; should be disabled globally.
     *
     * @param disableToc
     *            set to <code>true</code> if the rendering of the &quot;table
     *            of contents&quot; should be disabled globally.
     */
    void setNoToc(boolean disableToc);

    /**
     * Set the title of the currently rendered page data.
     *
     * @param pageTitle
     */
    void setPageName(String pageTitle);

    /**
     * Activate the mode for rendering a template parameter value
     *
     */
    void setParameterParsingMode(boolean parameterParsingMode);

    /**
     * Activate the parsing of semantic Mediawiki (SMW) links See <a
     * href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
     * MediaWiki</a> for more information.
     *
     */
    void setSemanticWebActive(boolean semanticWeb);

    /**
     * Prepare or initialize the wiki model before rendering the wikipedia text
     *
     */
    void setUp();

    /**
     * Show the syntax highlighting of the source code
     *
     * @return
     */
    boolean showSyntax(String tagName);

    /**
     * The size of the internal stack
     *
     * @return
     */
    int stackSize();

    /**
     * Substitute the template name by the template content and parameters and
     * append the new content to the writer.
     *
     * @param templateName
     *            the name of the template
     * @param parameterMap
     *            the templates parameter <code>java.util.SortedMap</code>
     * @param writer
     *            the buffer to append the substituted template content
     * @throws IOException
     */
    void substituteTemplateCall(String templateName,
                                Map<String, String> parameterMap, Appendable writer)
            throws IOException;

    TagStack swapStack(TagStack stack);

    /**
     * Clean up (i.e. free internal resources) in the wiki model after rendering
     * the wikipedia text, if necessary
     *
     */
    void tearDown();

    /**
     * Gets the magic word object for the given string.
     *
     * @param name
     *            the (potential) magic word
     *
     * @return a magic word object (e.g.
     *         {@link info.bliki.wiki.filter.MagicWord.MagicWordE} in case
     *         {@link info.bliki.wiki.filter.MagicWord} is used) or
     *         <tt>null</tt> if this is no valid magic word
     */
    Object getMagicWord(String name);

    /**
     * Splits the given full title into its namespace and page title components
     * and normalises both components using
     * {@link Encoder#normaliseTitle(String, boolean, char)} keeping
     * underscores.
     *
     * @param fullTitle
     *            the (full) title including a namespace (if present)
     *
     * @return a 2-element array with the namespace (index 0) and the page title
     *         (index 1)
     */
    String[] splitNsTitle(String fullTitle);
}
