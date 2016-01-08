package info.bliki.wiki.template;

import info.bliki.wiki.model.IWikiModel;

import java.util.List;

/**
 * A template parser function for <code>{{nse: ... }}</code> <i>namespace/i>
 * syntax
 *
 * From <a href="https://www.mediawiki.org/wiki/Help:Magic_words#Namespaces_2">
 * MediaWiki</a>:
 *
 * {{ns:}} returns the current localized name for the namespace with that index,
 * canonical name, or local alias. Thus {{ns:6}}, {{ns:File}}, and {{ns:Image}}
 * (an old name for the File namespace) all return "File". On a wiki where the
 * content language was French, {{ns:Fichier}} would also be valid, but
 * {{ns:Datei}} (the localisation of "File" into German) would not.
 *
 * {{nse:}} is the equivalent encoded for MediaWiki URLs. It does the same, but
 * it replaces spaces with underscores, making it usable in external links.
 */
public class NSE extends NS {
    public final static ITemplateFunction CONST = new NSE();

    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) {
        String namespace = super.parseFunction(list, model, src, beginIndex, endIndex, isSubst);
        if (namespace != null) {
            namespace = namespace.replace(' ', '_');
        }
        return namespace;
    }

}
