package info.bliki.wiki.template;

import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.ParsedPageName;
import info.bliki.wiki.filter.TemplateParser;
import info.bliki.wiki.filter.Util;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A template parser function for <code>{{safesubst: ... }}</code>. See
 * <a href="http://en.wikipedia.org/wiki/en:Help:Substitution#safesubst:">Wikipedia-Help:Substitution</a>
 */
public class Safesubst extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Safesubst();

    @Override
    public String parseFunction(List<String> parts1, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) {
        String substArg = new String(src, beginIndex, endIndex - beginIndex);
        String substituted = Safesubst.parsePreprocess(substArg, model, null);
        char[] src2 = substituted.toCharArray();

        Object[] objs = TemplateParser.createParameterMap(src2, 0, src2.length);
        @SuppressWarnings("unchecked")
        List<String> parts = (List<String>) objs[0];
        String templateName = ((String) objs[1]);

        int currOffset = TemplateParser.checkParserFunction(substituted);
        if (currOffset > 0) {
            String function = substituted.substring(0, currOffset - 1).trim();
            ITemplateFunction templateFunction = model.getTemplateFunction(function);
            if (templateFunction != null) {
                parts.set(0, templateName.substring(currOffset));
                String plainContent;
                try {
                    plainContent = templateFunction.parseFunction(parts, model, src2, currOffset, src2.length, isSubst);
                    if (plainContent != null) {
                        return plainContent;
                    }
                } catch (IOException ignored) {
                }
            }
            return "";
        }

        LinkedHashMap<String, String> parameterMap = new LinkedHashMap<>();
        List<String> unnamedParameters = new ArrayList<>();
        for (int i = 1; i < parts.size(); i++) {
            if (i == parts.size() - 1) {
                TemplateParser.createSingleParameter(parts.get(i), model, parameterMap, unnamedParameters);
            } else {
                TemplateParser.createSingleParameter(parts.get(i), model, parameterMap, unnamedParameters);
            }
        }
        TemplateParser.mergeParameters(parameterMap, unnamedParameters);

        final INamespace namespace = model.getNamespace();
        // TODO: remove trailing "#section"?!
        ParsedPageName parsedPagename  = ParsedPageName.parsePageName(model, templateName, namespace.getTemplate(), true, false);
        if (!parsedPagename.valid) {
            return "{{" + parsedPagename.pagename + "}}";
        }

        String plainContent = null;
        try {
            plainContent = model.getRawWikiContent(parsedPagename, parameterMap);
        } catch (WikiModelContentException e) {
        }
        if (plainContent != null) {
            return Safesubst.parsePreprocess(plainContent, model, parameterMap);
        }
        return "";
    }

    /**
     * Parse the preprocess step for the given content string with the template
     * parser and <code>Utils#trimNewlineLeft()</code> the resulting string.
     *
     * @param content
     * @param model
     * @return parsed content
     */
    public static String parsePreprocess(String content, IWikiModel model, Map<String, String> templateParameterMap) {
        if (content == null || content.length() == 0) {
            return "";
        }
        int startIndex = Util.indexOfTemplateParsing(content);
        if (startIndex < 0) {
            return Utils.trimNewlineLeft(content);
        }
        StringBuilder buf = new StringBuilder(content.length());
        try {
            TemplateParser.parsePreprocessRecursive(startIndex, content, model, buf, false, false, templateParameterMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Utils.trimNewlineLeft(buf.toString());
    }
}
