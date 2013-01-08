package info.bliki.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Manages page data from the <a
 * href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
 */
public class Page {
	List<Link> links;

	List<Category> categories;

	String pageid;

	String ns;

	String title;

    String editToken;

	// imageinfo
	String imageUrl;

	String imageThumbUrl;

	Revision revision = null;

	public Page() {
		this.pageid = "";
		this.ns = "";
		this.title = "";
		this.imageUrl = "";
		this.links = new ArrayList<Link>();
		this.categories = new ArrayList<Category>();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Category) {
			return title.equals(((Category) obj).title) && ns.equals(((Category) obj).ns);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return title.hashCode();
	}

	public String getNs() {
		return ns;
	}

	/**
	 * Set the <code>imageUrl</code> of this page if this is an
	 * <code>[[Image:...]]</code> page. Information retrieved with
	 * <code>prop=imageinfo</code>
	 * 
	 * @param fImageUrl
	 *          the image url if possible; the empty string <code>""</code>
	 *          otherwise.
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	public String getImageThumbUrl() {
		return imageThumbUrl;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public String getPageid() {
		return pageid;
	}

	public void setPageid(String pageid) {
		this.pageid = pageid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Set the <code>imageUrl</code> of this page if this is an
	 * <code>[[Image:...]]</code> page. Information retrieved with
	 * <code>prop=imageinfo</code>
	 * 
	 * @param imageUrl
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setImageThumbUrl(String imageThumbUrl) {
		this.imageThumbUrl = imageThumbUrl;
	}

	@Override
	public String toString() {
		String content = "";
		if (revision != null) {
			content = revision.getContent();
		}
		return "PageID: " + pageid + "; NS: " + ns + "; Title: " + title + "; \nImage url: " + imageUrl + "\nContent:\n" + content;
	}

	public boolean addCategory(Category arg0) {
		return categories.add(arg0);
	}

	public boolean containsCategory(Category o) {
		return categories.contains(o);
	}

	public Category getCategory(int index) {
		return categories.get(index);
	}

	public int sizeOfCategoryList() {
		return categories.size();
	}

	public boolean addLink(Link arg0) {
		return links.add(arg0);
	}

	public boolean containsLink(Link o) {
		return links.contains(o);
	}

	public Link getLink(int index) {
		return links.get(index);
	}

    public String getEditToken() {
        return editToken;
    }

    public void setEditToken(String editToken) {
        this.editToken = editToken;
    }

    protected final static int BLOCK_SIZE = 8192;

	/**
	 * If this page was created with User#queryImageinfo() you can download the
	 * image with this method. <br/> <b>Note:</b> this method doesn't close the
	 * given output stream!
	 * 
	 * @param outputStream
	 *          the output stream where the image should be written to. For
	 *          example, if you would save the image in a file, you can use
	 *          <code>FileOutputStream</code>.
	 */
	public void downloadImageUrl(OutputStream outputStream) {
		downloadImageUrl(outputStream, imageUrl);
	}

	/**
	 * If this page was created with User#queryImageinfo() you can download the
	 * image with this method. <br/> <b>Note:</b> this method doesn't close the
	 * given output stream!
	 * 
	 * @param outputStream
	 *          the output stream where the image should be written to. For
	 *          example, if you would save the image in a file, you can use
	 *          <code>FileOutputStream</code>.
	 */
	public void downloadImageUrl(OutputStream outputStream, String url) {
		if (url != null && url.length() > 3) {
			BufferedInputStream bis = null;
			GetMethod method = null;
			try {
				HttpClient client = new HttpClient();
				client = new HttpClient(new MultiThreadedHttpConnectionManager());
				client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);

				String extension = "jpg";
				int index = url.lastIndexOf('.');
				if (index > 0) {
					String extension2 = url.substring(index + 1).toLowerCase();
					if (extension2.equals("svg") || extension2.equals("gif") || extension2.equals("png") || extension2.equals("jpg")
							|| extension2.equals("jpeg")) {
						extension = extension2;
					}
				}
				method = new GetMethod(url);
				method.setFollowRedirects(false);
				method.setRequestHeader("accept", "image/" + extension);
				method.setRequestHeader("User-Agent", Connector.USER_AGENT);
				method.setFollowRedirects(false);

				// Execute the GET method
				int statusCode = client.executeMethod(method);
				if (statusCode == 200) {
					InputStream is = method.getResponseBodyAsStream();
					bis = new BufferedInputStream(is);
					byte[] b = new byte[BLOCK_SIZE];
					int count = bis.read(b);
					while (count != -1 && count <= BLOCK_SIZE) {
						outputStream.write(b, 0, count);
						count = bis.read(b);
					}
					if (count != -1) {
						outputStream.write(b, 0, count);
					}
				}
				// System.out.println(statusCode);
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (method != null) {
					method.releaseConnection();
				}
			}
		}
	}

	public int sizeOfLinksList() {
		return links.size();
	}

	public Revision getCurrentRevision() {
		return revision;
	}

	public void setCurrentRevision(Revision revision) {
		this.revision = revision;
	}

	public String getCurrentContent() {
		String content = "";
		if (revision != null) {
			content = revision.getContent();
		}
		return content;
	}
}
