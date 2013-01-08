package info.bliki.api;

/**
 * Manages template data from the <a href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
 */
public class Template {
    private String ns;
    private String exists;
    private String body;

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getExists() {
        return exists;
    }

    public void setExists(String exists) {
        this.exists = exists;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
