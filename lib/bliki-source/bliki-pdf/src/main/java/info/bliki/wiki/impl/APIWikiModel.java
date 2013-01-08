package info.bliki.wiki.impl;

import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.api.creator.ImageData;
import info.bliki.api.creator.TopicData;
import info.bliki.api.creator.WikiDB;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.filter.MagicWord;
import info.bliki.wiki.filter.WikipediaParser;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.ImageFormat;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.tags.WPATag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Wiki model implementation which uses the <code>info.bliki.api</code> package
 * for downloading templates ans images from a defined wiki.
 * 
 */
public class APIWikiModel extends WikiModel {
	private WikiDB fWikiDB;

	private final String fImageDirectoryName;
	static {
		TagNode.addAllowedAttribute("style");
	}

	private final User fUser;

	/**
	 * WikiModel which loads the templates and images through the <a
	 * href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
	 * 
	 * @param user
	 *          a user for the <a
	 *          href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
	 * @param wikiDB
	 *          a wiki database to retrieve already downloaded topics and
	 *          templates
	 * @param imageBaseURL
	 *          a url string which must contains a &quot;${image}&quot; variable
	 *          which will be replaced by the image name, to create links to
	 *          images.
	 * @param linkBaseURL
	 *          a url string which must contains a &quot;${title}&quot; variable
	 *          which will be replaced by the topic title, to create links to
	 *          other wiki topics.
	 * @param imageDirectoryName
	 *          a directory for storing downloaded Wikipedia images. The directory
	 *          must already exist.
	 */
	public APIWikiModel(User user, WikiDB wikiDB, String imageBaseURL, String linkBaseURL, String imageDirectoryName) {
		this(user, wikiDB, Locale.ENGLISH, imageBaseURL, linkBaseURL, imageDirectoryName);
	}

	/**
	 * 
	 * @param imageBaseURL
	 * @param linkBaseURL
	 */
	public APIWikiModel(User user, WikiDB wikiDB, Locale locale, String imageBaseURL, String linkBaseURL, String imageDirectoryName) {
		super(Configuration.DEFAULT_CONFIGURATION, locale, imageBaseURL, linkBaseURL);
		fUser = user;
		fWikiDB = wikiDB;
		if (imageDirectoryName.charAt(imageDirectoryName.length() - 1) == '/') {
			fImageDirectoryName = imageDirectoryName;
		} else {
			fImageDirectoryName = imageDirectoryName + "/";
		}
		File file = new File(fImageDirectoryName);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	@Override
	public String getRawWikiContent(String namespace, String articleName, Map<String, String> templateParameters) {
		// String result = super.getRawWikiContent(namespace, articleName,
		// templateParameters);
		// if (result != null) {
		// return result;
		// }
		String result = super.getRawWikiContent(namespace, articleName, templateParameters);
		if (result != null) {
			// found magic word template
			return result;
		}

		String name = articleName;
		if (namespace.equals("Template")) {
			String content = null;
			try {
				TopicData topicData = fWikiDB.selectTopic("Template:" + name);
				if (topicData != null) {
					content = topicData.getContent();
					content = getRedirectedWikiContent(content, templateParameters);
					if (content != null) {
						return content.length() == 0 ? null : content;
					} else {
						return null;
					}
				}

				String[] listOfTitleStrings = { "Template:" + name };
				fUser.login();
				List<Page> listOfPages = fUser.queryContent(listOfTitleStrings);
				for (Page page : listOfPages) {
					content = page.getCurrentContent();
					if (content != null) {
						topicData = new TopicData("Template:" + name, content);
						fWikiDB.insertTopic(topicData);
						content = getRedirectedWikiContent(content, templateParameters);
						if (content != null) {
							content = content.length() == 0 ? null : content;
						}
					}
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return content;
		}
		return null;
	}

	public String getRedirectedWikiContent(String rawWikitext, Map<String, String> templateParameters) {
		if (rawWikitext.length() < 9) {
			// less than "#REDIRECT" string
			return rawWikitext;
		}
		String redirectedLink = WikipediaParser.parseRedirect(rawWikitext, this);
		if (redirectedLink != null) {
			String redirNamespace = "";
			String redirArticle = redirectedLink;
			int index = redirectedLink.indexOf(":");
			if (index > 0) {
				redirNamespace = redirectedLink.substring(0, index);
				if (isNamespace(redirNamespace)) {
					redirArticle = redirectedLink.substring(index + 1);
				} else {
					redirNamespace = "";
				}
			}
			try {
				int level = incrementRecursionLevel();
				if (level > Configuration.PARSER_RECURSION_LIMIT) {
					return "Error - getting content of redirected link: " + redirNamespace + ":" + redirArticle;
				}
				return getRawWikiContent(redirNamespace, redirArticle, templateParameters);
			} finally {
				decrementRecursionLevel();
			}
		}
		return rawWikitext;
	}

	public void appendInternalImageLink(String hrefImageLink, String srcImageLink, ImageFormat imageFormat) {
		try {
			String imageName = imageFormat.getFilename();
			ImageData imageData = fWikiDB.selectImage(imageName);
			if (imageData != null) {
				File file = new File(imageData.getFilename());
				if (file.exists()) {
					super.appendInternalImageLink(hrefImageLink, "file:///" + imageData.getFilename(), imageFormat);
					return;
				}
			}
			String imageNamespace = getImageNamespace();
			String[] listOfTitleStrings = { imageNamespace + ":" + imageName };
			fUser.login();
			List<Page> listOfPages;
			if (imageFormat.getWidth() > 0) {
				listOfPages = fUser.queryImageinfo(listOfTitleStrings, imageFormat.getWidth());
			} else {
				listOfPages = fUser.queryImageinfo(listOfTitleStrings);
			}
			for (Page page : listOfPages) {

				imageData = new ImageData(imageName);

				// download the image to fImageDirectoryName directory
				FileOutputStream os = null;
				try {
					String imageUrl;
					if (imageFormat.getWidth() > 0) {
						imageUrl = page.getImageThumbUrl();
					} else {
						imageUrl = page.getImageUrl();
					}
					String urlImageName = page.getTitle().substring(imageNamespace.length() + 1).replace(' ', '_');
					if (imageUrl != null) {
						int index = imageUrl.lastIndexOf('/');
						if (index > 0) {
							urlImageName = imageUrl.substring(index + 1);
						}
					}
					String filename = fImageDirectoryName + urlImageName;
					os = new FileOutputStream(filename);
					page.downloadImageUrl(os, imageUrl);
					imageData.setUrl(imageUrl);
					imageData.setFilename(filename);
					fWikiDB.insertImage(imageData);
					super.appendInternalImageLink(hrefImageLink, "file:///" + filename, imageFormat);
					return;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		//
		// // Show only descriptions no internal wiki links
		// ContentToken text = new ContentToken(topicDescription);
		// // append(text);
		// aTagNode.addChild(text);

		WPATag aTagNode = new WPATag();
		// append(aTagNode);
		aTagNode.addAttribute("id", "w", true);
		String href = topic;
		if (hashSection != null) {
			href = href + '#' + hashSection;
		}
		aTagNode.addAttribute("href", href, true);
		if (cssClass != null) {
			aTagNode.addAttribute("class", cssClass, true);
		}
		aTagNode.addObjectAttribute("wikilink", topic);
		pushNode(aTagNode);
		WikipediaParser.parseRecursive(topicDescription.trim(), this, false, true);
		popNode();
		// ContentToken text = new ContentToken(topicDescription);
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
			// if (replaceColon()) {
			// imageName = imageName.replaceAll(":", "/");
			// }
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
