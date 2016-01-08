package info.bliki.api;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Manages page data from the <a
 * href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
 */
public class Page extends PageInfo {
    private List<Link> links;
    private List<PageInfo> categories;
    private String editToken;
    private String imageUrl;
    private String imageThumbUrl;
    private boolean missing;
    private boolean invalid;

    private Revision revision;

    public Page() {
        super();
        this.links = new ArrayList<>();
        this.categories = new ArrayList<>();
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

    @Override public String toString() {
        return "Page{" +
            "ns=" + ns +
            ", title=" + title +
            ", id=" + pageid +
            ", links=" + links +
            ", categories=" + categories +
            ", editToken='" + editToken + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", imageThumbUrl='" + imageThumbUrl + '\'' +
            ", missing=" + missing +
            ", invalid=" + invalid +
            ", revision=" + revision +
            '}';
    }

    public boolean addCategory(PageInfo arg0) {
        return categories.add(arg0);
    }

    public boolean containsCategory(PageInfo o) {
        return categories.contains(o);
    }

    public PageInfo getCategory(int index) {
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
    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public boolean isMissing() {
        return missing;
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
    }

    private final static int BLOCK_SIZE = 8192;

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
            HttpGet request = null;
            try {
                HttpClient client = HttpClientBuilder
                        .create()
                        .setDefaultRequestConfig(
                            RequestConfig.custom()
                                    .setConnectTimeout(30000)
                                    .setRedirectsEnabled(false)
                                    .build())
                        .build();

                String extension = "jpg";
                int index = url.lastIndexOf('.');
                if (index > 0) {
                    String extension2 = url.substring(index + 1).toLowerCase();
                    if (extension2.equals("svg") || extension2.equals("gif") || extension2.equals("png") || extension2.equals("jpg")
                            || extension2.equals("jpeg")) {
                        extension = extension2;
                    }
                }
                request = new HttpGet(url);
                request.setHeader("accept", "image/" + extension);
                request.setHeader("User-Agent", Connector.USER_AGENT);

                // Execute the GET request
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    InputStream is = response.getEntity().getContent();
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
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (request != null) {
                    request.reset();
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
        if (revision != null) {
            return revision.getContent();
        } else {
            return null;
        }
    }
}
