package info.bliki.wiki.filter;

import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.template.AbstractTemplateFunction;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Represents the result of parsing a (potential) page name.
 *
 * Note that {@link #magicWord} takes precedence over {@link #pagename}, i.e.
 * if {@link #magicWord} if not <tt>null</tt>, the parsed name is a magic
 * word!
 *
 * @author Nico Kruber, kruber@zib.de
 */
public class ParsedPageName {
    /**
     * The namespace the page is in.
     */
    public final INamespace.INamespaceValue namespace;
    /**
     * The name of the page (without the namespace).
     */
    public final String pagename;
    /**
     * Whether this pagename was successfully parsed or not.
     */
    public final boolean valid;

    /**
     * If the pagename was a magic word it will be this, otherwise <tt>null</tt>.
     *
     * The object type depends on the concrete
     * {@link MagicWord} implementation used by the
     * {@link info.bliki.wiki.model.IWikiModel}, e.g.
     * {@link info.bliki.wiki.filter.MagicWord.MagicWordE} in case
     * {@link MagicWord} is used.
     */
    public final Object magicWord;

    /**
     * Parameters of the magic word (<tt>null</tt> if not supplied).
     */
    public final String magicWordParameter;

    /**
     * Creates a new parsed page name object (no magic word).
     *
     * @param namespace
     *          the namespace the page is in
     * @param pagename
     *          the name of the page (without the namespace)
     * @param valid
     *          whether this pagename was successfully parsed or not
     */
    public ParsedPageName(INamespace.INamespaceValue namespace, String pagename, boolean valid) {
        this.namespace = namespace;
        this.pagename = pagename;
        this.valid = valid;
        this.magicWord = null;
        this.magicWordParameter = null;
    }

    /**
     * Creates a new parsed page name object.
     *
     * @param namespace
     *          the namespace the page is in
     * @param pagename
     *          the name of the page (without the namespace)
     * @param magicWord
     *          the magic word object if the pagename was a magic word,
     *          otherwise <tt>null</tt>
     * @param magicWordParameter
     *          parameters of the magic word (<tt>null</tt> if not supplied)
     * @param valid
     *          whether this pagename was successfully parsed or not
     */
    public ParsedPageName(INamespace.INamespaceValue namespace, String pagename, Object magicWord, String magicWordParameter, boolean valid) {
        this.namespace = namespace;
        this.pagename = pagename;
        this.valid = valid;
        this.magicWord = magicWord;
        this.magicWordParameter = magicWordParameter;
    }

    /**
     * Parses a given page name into its components, e.g. namespace and pagename
     * or magic word and parameters.
     *
     * @param wikiModel the wiki model to use
     * @param pagename the name in wiki text
     * @param namespace the default namespace to use if there is no namespace in the
     *                  pagename
     * @param magicWordAllowed
     *          whether the <tt>pagename</tt> may be a magic word or not (if it is
     *          a magic word and this is set to <tt>false</tt>, it will be parsed
     *          as if it is a page name)
     *
     * @return a parsed page name
     */
    @Nonnull public static ParsedPageName parsePageName(@Nonnull IWikiModel wikiModel,
                                                        @Nonnull String pagename, INamespace.INamespaceValue namespace,
                                                        boolean magicWordAllowed, boolean stripOffSection) {
        // if a magic word is recognised, it will be non-null:
        Object magicWord = null;
        String magicWordParameter = null;
        if (pagename.length() > 0 && pagename.charAt(0) == ':') {
            magicWordAllowed = false; // this is not allowed for magic words
            if (pagename.length() > 1 && pagename.charAt(1) == ':') {
                // double "::" are not parsed as template/transclusion
                return new ParsedPageName(namespace, pagename, false);
            }
            // assume main namespace for now:
            namespace = wikiModel.getNamespace().getMain();
            pagename = pagename.substring(1);
        }

        if (stripOffSection) {
            // parse away any "#label" markers which are not supported
            int hashIndex = pagename.indexOf('#');
            if (hashIndex != (-1)) {
                pagename = pagename.substring(0, hashIndex);
            }
        }

        final int index = pagename.indexOf(':');
        if (index > 0) {
            String maybeNamespaceStr0 = pagename.substring(0, index);
            INamespace.INamespaceValue maybeNamespace = wikiModel.getNamespace().getNamespace(maybeNamespaceStr0);
            if (maybeNamespace != null) {
                return new ParsedPageName(maybeNamespace, pagename.substring(index + 1), true);
            } else if (magicWordAllowed) {
                // no namespace? maybe a magic word with a parameter?
                magicWord = wikiModel.getMagicWord(maybeNamespaceStr0);
                if (magicWord != null) {
                    magicWordParameter = pagename.substring(index + 1);
                    if (magicWordParameter.length() != 0) {
                        magicWordParameter = AbstractTemplateFunction.parseTrim(magicWordParameter, wikiModel);
                    }
                }
            }
        } else if (magicWordAllowed && namespace.isType(INamespace.NamespaceCode.TEMPLATE_NAMESPACE_KEY)) {
            Object maybeMagicWord = wikiModel.getMagicWord(pagename);
            if (maybeMagicWord != null) {
                magicWord = maybeMagicWord;
            }
        }
        return new ParsedPageName(namespace, pagename, magicWord, magicWordParameter, true);
    }


    @Override
    public String toString() {
        return "ParsedPageName{" +
                "namespace=" + namespace +
                ", pagename='" + pagename + '\'' +
                '}';
    }

    public String fullPagename() {
        return namespace.makeFullPagename(pagename);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsedPageName pageName = (ParsedPageName) o;
        return Objects.equals(valid, pageName.valid) &&
                Objects.equals(namespace, pageName.namespace) &&
                Objects.equals(pagename, pageName.pagename) &&
                Objects.equals(magicWord, pageName.magicWord) &&
                Objects.equals(magicWordParameter, pageName.magicWordParameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, pagename, valid, magicWord, magicWordParameter);
    }
}
