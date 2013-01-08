package info.bliki.wiki.model;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.ImageFormat;
import info.bliki.wiki.model.WikiModel; //import info.bliki.wiki.tags.WPATag;

import java.util.Locale;
import java.util.Map;


/**
 * PDF Wiki model implementation
 * 
 * @deprecated use an extended APIWikiModel instead; see PDFCreatorTest in the
 *             test sources
 */
public class PDFWikiModel extends WikiModel {

	static {
		TagNode.addAllowedAttribute("style");
	}

	public PDFWikiModel(String imageBaseURL, String linkBaseURL) {
		this(Locale.ENGLISH, imageBaseURL, linkBaseURL);
	}

	/**
	 * 
	 * @param imageBaseURL
	 * @param linkBaseURL
	 */
	public PDFWikiModel(Locale locale, String imageBaseURL, String linkBaseURL) {
		super(Configuration.DEFAULT_CONFIGURATION, locale, imageBaseURL, linkBaseURL);
	}

	/**
	 * Add templates: &quot;Test&quot;, &quot;Templ1&quot;, &quot;Templ2&quot;,
	 * &quot;Include Page&quot;
	 * 
	 */
	@Override
	public String getRawWikiContent(String namespace, String articleName, Map<String, String> map) {
		String result = super.getRawWikiContent(namespace, articleName, map);
		if (result != null) {
			return result;
		}
		String name = encodeTitleToUrl(articleName, true);
		if (namespace.equals("Template")) {
			// if (name.equals("Mytemplate")) {
			// return MYTEMPLATE_TEXT;
			// }
		} else {
			if (name.equals("Include_Page")) {
				// return "an include page";
			}
		}
		return null;
	}

	public void appendInternalLink(String topic, String hashSection, String topicDescription, String cssClass, boolean parseRecursive) {
		// WPATag aTagNode = new WPATag();
		// append(aTagNode);
		// aTagNode.addAttribute("id", "w", true);
		// String href = topic;
		// if (hashSection != null) {
		// href = href + '#' + hashSection;
		// }
		// aTagNode.addAttribute("href", href, true);
		// if (cssClass != null) {
		// aTagNode.addAttribute("class", cssClass, true);
		// }
		// aTagNode.addObjectAttribute("wikilink", topic);

		// Show only descriptions no internal wiki links
		ContentToken text = new ContentToken(topicDescription);
		append(text);
		// aTagNode.addChild(text);
	}

	public void parseInternalImageLink(String imageNamespace, String rawImageLink) {
		if (fExternalImageBaseURL != null) {
			String imageHref = fExternalWikiBaseURL;
			String imageSrc = fExternalImageBaseURL;
			ImageFormat imageFormat = ImageFormat.getImageFormat(rawImageLink, imageNamespace);

			String imageName = imageFormat.getFilename();
			// String sizeStr = imageFormat.getSizeStr();
			// if (sizeStr != null) {
			// imageName = sizeStr + '-' + imageName;
			// }
			// if (imageName.endsWith(".svg")) {
			// imageName += ".png";
			// }
			imageName = Encoder.encodeUrl(imageName);
 
			if (replaceColon()) {
				imageHref = imageHref.replace("${title}", imageNamespace + '/' + imageName);
				imageSrc = imageSrc.replace("${image}", imageName);
			} else {
				imageHref = imageHref.replace("${title}", imageNamespace + ':' + imageName);
				imageSrc = imageSrc.replace("${image}", imageName);
			}
			appendInternalImageLink(imageHref, imageSrc, imageFormat);
		}
	}
}
