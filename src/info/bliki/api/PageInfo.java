package info.bliki.api;

public class PageInfo {
    protected String pageid;
    protected String ns;
    protected String title;


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PageInfo) {
            return pageid.equals(((PageInfo) obj).pageid) && title.equals(((PageInfo) obj).title) && ns.equals(((PageInfo) obj).ns);
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

    @Override
    public String toString() {
        return "PageID: " + pageid + "; NS: " + ns + "; Title: " + title;
    }
}
