package info.bliki.wiki.dump;

import info.bliki.wiki.namespaces.Namespace;
import info.bliki.wiki.namespaces.Namespace.NamespaceValue;

import javax.annotation.Nullable;

/**
 * The site and namespace information found in the header of a Mediawiki dump
 */
public class Siteinfo {
    private String sitename;
    private String base;
    private String generator;
    private String characterCase;
    private final Namespace namespace;

    public Siteinfo() {
        namespace = new Namespace();
    }

    public void addNamespace(String integerKey, String namespaceText) {
        Integer key;
        try {
            key = Integer.parseInt(integerKey);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("invalid key "+integerKey);
        }
        final NamespaceValue nsValue = namespace.getNamespaceByNumber(key);
        if (nsValue != null) {
            nsValue.setTexts(namespaceText == null ? "" : namespaceText);
        }
    }

    /**
     * Get the namespace of this site from the given integer key. For example in
     * an english Mediawiki installation <i>10</i> is typically the
     * <i>Template</i> namespace and <i>14</i> is typically the <i>Category</i>
     * namespace.
     *
     * @param key
     * @return <code>null</code> if no namespace is defined for the given key.
     */
    public String getNamespace(Integer key) {
        return namespace.getNamespaceByNumber(key).getPrimaryText();
    }

    /**
     * Gets the full namespace mappings.
     *
     * @return the namespace object
     */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
     * Get the integer value of this site from the given namespace. For example in
     * an english Mediawiki installation <i>10</i> is typically the
     * <i>Template</i> namespace and <i>14</i> is typically the <i>Category</i>
     * namespace.
     *
     * @param namespace
     * @return <code>null</code> if no integer value is defined for the given
     *         namespace.
     */
    @Nullable public Integer getIntegerNamespace(String namespace) {
        final Namespace.NamespaceValue ns = this.namespace.getNamespace(namespace);
        return ns == null ? null : ns.getCode().code;
    }

    /**
     * Get the "MainPage" URL of the wiki.
     *
     * @return the base
     */
    public String getBase() {
        return base;
    }

    /**
     * Get the character case of the wiki. For example <i>first-letter</i> is set,
     * if the first letter of a wiki title is converted to an upper case letter.
     *
     * @return the characterCase
     */
    public String getCharacterCase() {
        return characterCase;
    }

    /**
     * Get the generator signature of the wiki (i.e. <i>MediaWiki 1.xx.....</i>).
     *
     * @return the generator
     */
    public String getGenerator() {
        return generator;
    }

    /**
     * Get the site name of the wiki.
     *
     * @return the sitename
     */
    public String getSitename() {
        return sitename;
    }

    /**
     * @param base
     *          the base to set
     */
    public void setBase(String base) {
        this.base = base;
    }

    /**
     * @param characterCase
     *          the characterCase to set
     */
    public void setCharacterCase(String characterCase) {
        this.characterCase = characterCase;
    }

    /**
     * @param generator
     *          the generator to set
     */
    public void setGenerator(String generator) {
        this.generator = generator;
    }

    /**
     * @param sitename
     *          the sitename to set
     */
    public void setSitename(String sitename) {
        this.sitename = sitename;
    }
}
