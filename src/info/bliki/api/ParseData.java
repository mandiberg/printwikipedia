package info.bliki.api;

import java.util.List;

/**
 * Manages parse data from the <a href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
 */
public class ParseData {
    private String displaytitle;
    private String text;
    private List<Link> links;
    private List<PageInfo> categories;
    private List<Template> templates;

    //TODO: add all elements

    public String getDisplaytitle() {
        return displaytitle;
    }

    public void setDisplaytitle(String displaytitle) {
        this.displaytitle = displaytitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<PageInfo> getCategories() {
        return categories;
    }

    public void setCategories(List<PageInfo> categories) {
        this.categories = categories;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }
}
