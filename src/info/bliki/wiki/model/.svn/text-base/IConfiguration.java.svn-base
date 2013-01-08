package info.bliki.wiki.model;

import info.bliki.htmlcleaner.TagToken;
import info.bliki.wiki.tags.code.SourceCodeFormatter;
import info.bliki.wiki.template.ITemplateFunction;

import java.util.Map;
import java.util.Set;

/**
 * Configuration interface for extensions similar to the Mediawiki <a
 * href="http://www.mediawiki.org/wiki/Manual:Tag_extensions">HTML styled tag
 * extension</a>, <a
 * href="http://www.mediawiki.org/wiki/Manual:Parser_functions">template parser
 * functions</a> and <a href="http://www.mediawiki.org/wiki/Interwiki">interwiki
 * links</a>
 * 
 * @see info.bliki.wiki.model.Configuration
 */
public interface IConfiguration {
	/**
	 * Get the <a href="http://www.mediawiki.org/wiki/Interwiki">interwiki
	 * links</a> map for converting interwiki links into external URLs.
	 * 
	 * Example: maps the interwiki shortcut &quot;de&quot; to
	 * &quot;http://de.wikipedia.org/wiki/${title}&quot;
	 * 
	 * @return
	 */
	public Map<String, String> getInterwikiMap();

	/**
	 * Get the <a
	 * href="http://www.mediawiki.org/wiki/Manual:Parser_functions">template
	 * parser functions</a> map of built-in template functions
	 * 
	 * @return
	 */
	public Map<String, ITemplateFunction> getTemplateMap();

	/**
	 * Get the <a href="http://www.mediawiki.org/wiki/Manual:Tag_extensions">HTML
	 * styled tag</a> map for built-in tags
	 * 
	 * @return
	 */
	public Map<String, TagToken> getTokenMap();

	/**
	 * Get the set of all allowed URI scheme shortcuts like http, https, ftp,...
	 * 
	 * See <a href="http://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
	 * 
	 */
	public Set<String> getUriSchemeSet();

	/**
	 * Add another Interwiki link to the configuration. The value string must
	 * contain the <code>${title}</code> placeholder for the used wiki article
	 * link.
	 * 
	 * @param key
	 *          the prefix used in the interwiki link (i.e. [[prefix:...]] )
	 * @param value
	 *          the external link which should be generated from the interwiki
	 *          link
	 * @return
	 */
	public String addInterwikiLink(String key, String value);

	/**
	 * Add a new <a href="http://www.mediawiki.org/wiki/Interwiki">interwiki
	 * link</a> to the configuration
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public ITemplateFunction addTemplateFunction(String key, ITemplateFunction value);

	/**
	 * Add a new <a
	 * href="http://www.mediawiki.org/wiki/Manual:Tag_extensions">HTML styled
	 * tag</a> to the configuration
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public TagToken addTokenTag(String key, TagToken value);

	/**
	 * Get the <a
	 * href="http://www.mediawiki.org/wiki/Extension:SyntaxHighlight_GeSHi">source
	 * code formatter</a> map of built-in source code formatters
	 * 
	 * @return
	 */
	public Map<String, SourceCodeFormatter> getCodeFormatterMap();

	/**
	 * Add a new <a
	 * href="http://www.mediawiki.org/wiki/Extension:SyntaxHighlight_GeSHi">source
	 * code formatter</a> to the configuration
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public SourceCodeFormatter addCodeFormatter(String key, SourceCodeFormatter value);
}
