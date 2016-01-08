package info.bliki.wiki.dump;

import info.bliki.wiki.namespaces.INamespace.NamespaceCode;

/**
 * Represents a single wiki page from a Mediawiki dump.
 *
 */
public class WikiArticle {
    private String id = null;
    private Integer integerNamespace = 0;
    private String namespace = "";
    private String revisionId = null;
    private String text;
    private String timeStamp;

    private String title;

    public WikiArticle() {

    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Get the integer key of the namespace or <code>0</code> if no namespace is
     * associated. For example in an english Mediawiki installation <i>10</i> is
     * typically the <i>Template</i> namespace and <i>14</i> is typically the
     * <i>Category</i> namespace.
     *
     * @return the integerNamespace
     */
    public Integer getIntegerNamespace() {
        return integerNamespace;
    }

    /**
     * @return the namespace.
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * @return the revisionId
     */
    public String getRevisionId() {
        return revisionId;
    }

    public String getText() {
        return text;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Does the title belong to the <i>Category</i> namespace?
     *
     * @return
     */
    public boolean isCategory() {
        return integerNamespace.equals(NamespaceCode.CATEGORY_NAMESPACE_KEY.code);
    }

    public boolean isFile() {
        return integerNamespace.equals(NamespaceCode.FILE_NAMESPACE_KEY.code);
    }

    /**
     * &quot;Real&quot; content articles (i.e. the title has no namespace prefix)?
     *
     * @return
     */
    public boolean isMain() {
        return integerNamespace.equals(NamespaceCode.MAIN_NAMESPACE_KEY.code);
    }

    public boolean isProject() {
        return integerNamespace.equals(NamespaceCode.PROJECT_NAMESPACE_KEY.code);
    }

    /**
     * Does the title belong to the <i>Template</i> namespace?
     *
     * @return
     */
    public boolean isTemplate() {
        return integerNamespace.equals(NamespaceCode.TEMPLATE_NAMESPACE_KEY.code);
    }

    public boolean isModule() {
        return integerNamespace.equals(NamespaceCode.MODULE_NAMESPACE_KEY.code);
    }

    /**
     * The ID of the wiki article to set.
     *
     * @param id
     *          the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param integerNamespace
     *          the integerNamespace to set
     */
    public void setIntegerNamespace(Integer integerNamespace) {
        this.integerNamespace = integerNamespace;
    }

    /**
     * @param namespace
     *          the namespace to set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * The ID of the revision of the wiki article to set.
     *
     * @param revisionId
     *          the revisisonId to set
     */
    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }

    public void setText(String newText) {
        text = newText;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setTitle(String newTitle, Siteinfo siteinfo) {
        title = newTitle;
        if (siteinfo != null) {
            int index = newTitle.indexOf(":");
            if (index > 0) {
                Integer key = siteinfo.getIntegerNamespace(newTitle.substring(0, index));
                if (key != null) {
                    integerNamespace = key;
                    setNamespace(siteinfo.getNamespace(key));
                }
            }

        }
    }

    @Override
    public String toString() {
        return title + "\n" + text;
    }
}
