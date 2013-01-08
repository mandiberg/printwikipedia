package info.bliki.wiki.namespaces;

import java.util.ResourceBundle;

/**
 * Namespaces for a specific language.
 * 
 */
public interface INamespace {

	public String getCategory();

	public String getCategory_talk();

	public String getCategory_talk2();

	public String getCategory2();

	public String getHelp();

	public String getHelp_talk();

	public String getHelp_talk2();

	public String getHelp2();

	public String getImage();

	public String getImage_talk();

	public String getImage_talk2();

	public String getImage2();

	/**
	 * Get the &quot;Media&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public String getMedia();

	public String getMedia2();

	public String getMediaWiki();

	public String getMediaWiki_talk();

	public String getMediaWiki_talk2();

	public String getMediaWiki2();

	public String getMeta();

	public String getMeta_talk();

	public String getMeta_talk2();

	public String getMeta2();

	public String getNamespaceByLowercase(String lowercaseNamespace);

	public String getNamespaceByNumber(int numberCode);

	public ResourceBundle getResourceBundle();

	public String getSpecial();

	public String getSpecial2();

	public String getTalk();

	public String getTalk2();

	public String getTemplate();

	public String getTemplate_talk();

	public String getTemplate_talk2();

	public String getTemplate2();

	public String getUser();
	
	public String getUser_talk();
	
	public String getUser_talk2();
	
	public String getUser2();
	
	public String getTalkspace(String namespace);
}