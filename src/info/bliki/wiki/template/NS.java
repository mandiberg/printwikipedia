package info.bliki.wiki.template;

import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.namespaces.INamespace.INamespaceValue;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * A template parser function for <code>{{ns: ... }}</code> <i>namespace/i>
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
 */
public class NS extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new NS();

    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) {
        if (list.size() > 0) {
            String arg0 = isSubst ? list.get(0) : parseTrim(list.get(0), model);
            INamespace namespace = model.getNamespace();
            try {
                int numberCode = Integer.valueOf(arg0).intValue();
                try {
                    return namespace.getNamespaceByNumber(numberCode).toString();
                } catch (InvalidParameterException ipe) {
                    // nothing to do
                }
            } catch (NumberFormatException nfe) {
                // the given argument could not be parsed as integer number
                INamespaceValue value = namespace.getNamespace(arg0);
                if (value != null) {
                    return value.getPrimaryText();
                }
                return "[[:" + namespace.getTemplate().getPrimaryText() + ":Ns:" + arg0 + "]]";
            }
        }
        return null;
    }

}
