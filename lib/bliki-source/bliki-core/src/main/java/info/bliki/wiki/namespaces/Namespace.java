package info.bliki.wiki.namespaces;

import info.bliki.Messages;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class Namespace implements INamespace {

	protected final String[] fNamespaces1 = { "Media", "Special", "", "Talk", "User", "User_talk", "Meta", "Meta_talk", "Image",
			"Image_talk", "MediaWiki", "MediaWiki_talk", "Template", "Template_talk", "Help", "Help_talk", "Category", "Category_talk" };

	protected final String[] fNamespaces2 = { "Media", "Special", "", "Talk", "User", "User_talk", "Meta", "Meta_talk", "File",
			"File_talk", "MediaWiki", "MediaWiki_talk", "Template", "Template_talk", "Help", "Help_talk", "Category", "Category_talk" };

	protected final String[] fTalkNamespaces = { null, null, null, null, "User_talk", "User_talk", "Meta_talk", "Meta_talk",
			"Image_talk", "Image_talk", "MediaWiki_talk", "MediaWiki_talk", "Template_talk", "Template_talk", "Help_talk", "Help_talk",
			"Category_talk", "Category_talk" };

	protected final String[] fNamespacesLowercase = { "media", "special", "", "talk", "user", "user_talk", "project", "project_talk",
			"image", "image_talk", "mediawiki", "mediawiki_talk", "template", "template_talk", "help", "help_talk", "category",
			"category_talk" };

	public final Map<String, String> NAMESPACE_MAP = new HashMap<String, String>();

	public final Map<String, String> TALKSPACE_MAP = new HashMap<String, String>();

	protected ResourceBundle fResourceBundle = null;

	public Namespace() {
		this((ResourceBundle) null);
	}

	public Namespace(Locale locale) {
		this(Messages.getResourceBundle(locale));
	}

	public Namespace(ResourceBundle resourceBundle) {
		fResourceBundle = resourceBundle;
		initializeNamespaces();
		for (int i = 0; i < fNamespacesLowercase.length; i++) {
			NAMESPACE_MAP.put(fNamespacesLowercase[i], fNamespaces1[i]);
		}
		for (int i = 0; i < fNamespacesLowercase.length; i++) {
			if (fTalkNamespaces[i] != null) {
				TALKSPACE_MAP.put(fNamespacesLowercase[i], fTalkNamespaces[i]);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getCategory()
	 */
	public String getCategory() {
		return fNamespaces1[16];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getCategory_talk()
	 */
	public String getCategory_talk() {
		return fNamespaces1[17];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getCategory_talk2()
	 */
	public String getCategory_talk2() {
		return fNamespaces2[17];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getCategory2()
	 */
	public String getCategory2() {
		return fNamespaces2[16];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getHelp()
	 */
	public String getHelp() {
		return fNamespaces1[14];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getHelp_talk()
	 */
	public String getHelp_talk() {
		return fNamespaces1[15];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getHelp_talk2()
	 */
	public String getHelp_talk2() {
		return fNamespaces2[15];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getHelp2()
	 */
	public String getHelp2() {
		return fNamespaces2[14];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getImage()
	 */
	public String getImage() {
		return fNamespaces1[8];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getImage_talk()
	 */
	public String getImage_talk() {
		return fNamespaces1[9];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getImage_talk2()
	 */
	public String getImage_talk2() {
		return fNamespaces2[9];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getImage2()
	 */
	public String getImage2() {
		return fNamespaces2[8];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getMedia()
	 */
	public String getMedia() {
		return fNamespaces1[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getMedia2()
	 */
	public String getMedia2() {
		return fNamespaces2[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getMediaWiki()
	 */
	public String getMediaWiki() {
		return fNamespaces1[10];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getMediaWiki_talk()
	 */
	public String getMediaWiki_talk() {
		return fNamespaces1[11];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getMediaWiki_talk2()
	 */
	public String getMediaWiki_talk2() {
		return fNamespaces2[11];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getMediaWiki2()
	 */
	public String getMediaWiki2() {
		return fNamespaces2[10];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getMeta()
	 */
	public String getMeta() {
		return fNamespaces1[6];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getMeta_talk()
	 */
	public String getMeta_talk() {
		return fNamespaces1[7];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getMeta_talk2()
	 */
	public String getMeta_talk2() {
		return fNamespaces2[7];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getMeta2()
	 */
	public String getMeta2() {
		return fNamespaces2[6];
	}

	public String getNamespaceByLowercase(String lowercaseNamespace) {
		return NAMESPACE_MAP.get(lowercaseNamespace);
	}

	public String getNamespaceByNumber(int numberCode) {
		return fNamespaces1[numberCode + 2];
	}

	public ResourceBundle getResourceBundle() {
		return fResourceBundle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getSpecial()
	 */
	public String getSpecial() {
		return fNamespaces1[1];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getSpecial2()
	 */
	public String getSpecial2() {
		return fNamespaces2[1];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getTalk()
	 */
	public String getTalk() {
		return fNamespaces1[3];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getTalk2()
	 */
	public String getTalk2() {
		return fNamespaces2[3];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getTemplate()
	 */
	public String getTemplate() {
		return fNamespaces1[12];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getTemplate_talk()
	 */
	public String getTemplate_talk() {
		return fNamespaces1[13];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getTemplate_talk2()
	 */
	public String getTemplate_talk2() {
		return fNamespaces2[13];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getTemplate2()
	 */
	public String getTemplate2() {
		return fNamespaces2[12];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getUser()
	 */
	public String getUser() {
		return fNamespaces1[4];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getUser_talk()
	 */
	public String getUser_talk() {
		return fNamespaces1[5];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getUser_talk2()
	 */
	public String getUser_talk2() {
		return fNamespaces2[5];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.namespaces.INamespcae#getUser2()
	 */
	public String getUser2() {
		return fNamespaces2[4];
	}

	private void initializeNamespaces() {
		if (fResourceBundle == null) {
			return;
		}
		String ns1, ns2;

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_MEDIA1);
		if (ns1 != null) {
			fNamespaces1[0] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_MEDIA2);
			if (ns2 != null) {
				fNamespaces2[0] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_SPECIAL1);
		if (ns1 != null) {
			fNamespaces1[1] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_SPECIAL2);
			if (ns2 != null) {
				fNamespaces2[1] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_TALK1);
		if (ns1 != null) {
			fNamespaces1[3] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_TALK2);
			if (ns2 != null) {
				fNamespaces2[3] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_USER1);
		if (ns1 != null) {
			fNamespaces1[4] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_USER2);
			if (ns2 != null) {
				fNamespaces2[4] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_USERTALK1);
		if (ns1 != null) {
			fNamespaces1[5] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_USERTALK2);
			if (ns2 != null) {
				fNamespaces2[5] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_META1);
		if (ns1 != null) {
			fNamespaces1[6] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_META2);
			if (ns2 != null) {
				fNamespaces2[6] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_METATALK1);
		if (ns1 != null) {
			fNamespaces1[7] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_METATALK2);
			if (ns2 != null) {
				fNamespaces2[7] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_IMAGE1);
		if (ns1 != null) {
			fNamespaces1[8] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_IMAGE2);
			if (ns2 != null) {
				fNamespaces2[8] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_IMAGETALK1);
		if (ns1 != null) {
			fNamespaces1[9] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_IMAGETALK2);
			if (ns2 != null) {
				fNamespaces2[9] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_MEDIAWIKI1);
		if (ns1 != null) {
			fNamespaces1[10] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_MEDIAWIKI2);
			if (ns2 != null) {
				fNamespaces2[10] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_MEDIAWIKITALK1);
		if (ns1 != null) {
			fNamespaces1[11] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_MEDIAWIKITALK2);
			if (ns2 != null) {
				fNamespaces2[11] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_TEMPLATE1);
		if (ns1 != null) {
			fNamespaces1[12] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_TEMPLATE2);
			if (ns2 != null) {
				fNamespaces2[12] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_TEMPLATETALK1);
		if (ns1 != null) {
			fNamespaces1[13] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_TEMPLATETALK2);
			if (ns2 != null) {
				fNamespaces2[13] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_HELP1);
		if (ns1 != null) {
			fNamespaces1[14] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_HELP2);
			if (ns2 != null) {
				fNamespaces2[14] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_HELPTALK1);
		if (ns1 != null) {
			fNamespaces1[15] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_HELPTALK2);
			if (ns2 != null) {
				fNamespaces2[15] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_CATEGORY1);
		if (ns1 != null) {
			fNamespaces1[16] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_CATEGORY2);
			if (ns2 != null) {
				fNamespaces2[16] = ns2;
			}
		}

		ns1 = Messages.getString(fResourceBundle, Messages.WIKI_API_CATEGORYTALK1);
		if (ns1 != null) {
			fNamespaces1[17] = ns1;
			ns2 = Messages.getString(fResourceBundle, Messages.WIKI_API_CATEGORYTALK2);
			if (ns2 != null) {
				fNamespaces2[17] = ns2;
			}
		}

	}

	public String getTalkspace(String namespace) {
		return TALKSPACE_MAP.get(namespace);
	}
}
