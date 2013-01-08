package info.bliki;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Handles the <code>Messages_XX.properties</code> files for I18N support.
 * 
 */
public class Messages {
	public final static String RESOURCE_BUNDLE = "Messages";//$NON-NLS-1$

	private static ResourceBundle resourceBundle = null;

	public final static String WIKI_TAGS_TOC_CONTENT = "wiki.tags.toc.content";

	public final static String WIKI_API_URL = "wiki.api.url";

	public final static String WIKI_API_MEDIA1 = "wiki.api.media1";
	public final static String WIKI_API_MEDIA2 = "wiki.api.media2";

	public final static String WIKI_API_SPECIAL1 = "wiki.api.special1";
	public final static String WIKI_API_SPECIAL2 = "wiki.api.special2";

	public final static String WIKI_API_TALK1 = "wiki.api.talk1";
	public final static String WIKI_API_TALK2 = "wiki.api.talk2";

	public final static String WIKI_API_USER1 = "wiki.api.user1";
	public final static String WIKI_API_USER2 = "wiki.api.user2";

	public final static String WIKI_API_USERTALK1 = "wiki.api.usertalk1";
	public final static String WIKI_API_USERTALK2 = "wiki.api.usertalk2";

	public final static String WIKI_API_META1 = "wiki.api.meta1";
	public final static String WIKI_API_META2 = "wiki.api.meta2";

	public final static String WIKI_API_METATALK1 = "wiki.api.metatalk1";
	public final static String WIKI_API_METATALK2 = "wiki.api.metatalk2";

	public final static String WIKI_API_IMAGE1 = "wiki.api.image1";
	public final static String WIKI_API_IMAGE2 = "wiki.api.image2";

	public final static String WIKI_API_IMAGETALK1 = "wiki.api.imagetalk1";
	public final static String WIKI_API_IMAGETALK2 = "wiki.api.imagetalk2";

	public final static String WIKI_API_MEDIAWIKI1 = "wiki.api.mediawiki1";
	public final static String WIKI_API_MEDIAWIKI2 = "wiki.api.mediawiki2";
	
	public final static String WIKI_API_MEDIAWIKITALK1 = "wiki.api.mediawikitalk1";
	public final static String WIKI_API_MEDIAWIKITALK2 = "wiki.api.mediawikitalk2";
	
	public final static String WIKI_API_TEMPLATE1 = "wiki.api.template1";
	public final static String WIKI_API_TEMPLATE2 = "wiki.api.template2";

	public final static String WIKI_API_TEMPLATETALK1 = "wiki.api.templatetalk1";
	public final static String WIKI_API_TEMPLATETALK2 = "wiki.api.templatetalk2";

	public final static String WIKI_API_HELP1 = "wiki.api.help1";
	public final static String WIKI_API_HELP2 = "wiki.api.help2";

	public final static String WIKI_API_HELPTALK1 = "wiki.api.helptalk1";
	public final static String WIKI_API_HELPTALK2 = "wiki.api.helptalk2";

	public final static String WIKI_API_CATEGORY1 = "wiki.api.category1";
	public final static String WIKI_API_CATEGORY2 = "wiki.api.category2";

	public final static String WIKI_API_CATEGORYTALK1 = "wiki.api.categorytalk1";
	public final static String WIKI_API_CATEGORYTALK2 = "wiki.api.categorytalk2";

	public Messages() {
	}

	public static ResourceBundle getResourceBundle(Locale locale) {
		try {
			resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE, locale);
			return resourceBundle;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getString(final ResourceBundle bundle, final String key) {
		try {
			return bundle.getString(key);
		} catch (final Exception e) {
			return "!" + key + "!";//$NON-NLS-2$ //$NON-NLS-1$
		}
	}
}
