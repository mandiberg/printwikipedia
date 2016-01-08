package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.TagToken;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.WPBoldItalicTag;
import info.bliki.wiki.tags.WPTag;
import info.bliki.wiki.tags.util.TagStack;

import java.util.Map;

import static info.bliki.wiki.filter.ParsedPageName.parsePageName;

public abstract class AbstractWikipediaParser extends AbstractParser {
    protected static final int TokenIgnore = -1;
    protected static final int TokenSTART = 0;
    protected static final int TokenEOF = 1;
    protected static final int TokenBOLD = 3;
    protected static final int TokenITALIC = 4;
    protected static final int TokenBOLDITALIC = 5;

    protected static final HTMLTag BOLD = new WPTag("b");
    protected static final HTMLTag ITALIC = new WPTag("i");
    protected static final HTMLTag BOLDITALIC = new WPBoldItalicTag();

    public AbstractWikipediaParser(String stringSource) {
        super(stringSource);
    }

    public abstract void runParser();
    protected abstract void setNoToC(boolean noToC);

    public static String getRedirectedRawContent(IWikiModel wikiModel, ParsedPageName parsedPagename,
                                                 Map<String, String> templateParameters) {
        try {
            int level = wikiModel.incrementRecursionLevel();
            if (level > Configuration.PARSER_RECURSION_LIMIT || !parsedPagename.valid) {
                return "<span class=\"error\">Error - getting content of redirected link: " + parsedPagename.namespace + ":"
                        + parsedPagename.pagename + "<span>";
            }
            try {
                return wikiModel.getRawWikiContent(parsedPagename, templateParameters);
            } catch (WikiModelContentException e) {
                return "<span class=\"error\">Error - getting content of redirected link: " + parsedPagename.namespace + ":"
                        + parsedPagename.pagename + "<span>";
            }
        } finally {
            wikiModel.decrementRecursionLevel();
        }
    }

    protected static String getRedirectedTemplateContent(IWikiModel wikiModel, String redirectedLink,
                                                         Map<String, String> templateParameters) {
        final INamespace namespace = wikiModel.getNamespace();
        ParsedPageName parsedPagename = parsePageName(wikiModel, redirectedLink, namespace.getMain(), false, false);
        // note: don't just get redirect content if the namespace is the template namespace!
        if (!parsedPagename.valid) {
            return null;
        }
        return getRedirectedRawContent(wikiModel, parsedPagename, templateParameters);
    }

    protected void reduceTokenStack() {
        while (fWikiModel.stackSize() > 0) {
            fWikiModel.popNode();
        }
    }

    /**
     * Copy the read ahead content in the resulting HTML text token.
     *
     * @param diff
     *          subtract <code>diff</code> form the current parser position to get
     *          the HTML text token end position.
     */
    protected void createContentToken(final int diff) {
        if (fWhiteStart) {
            try {
                final int count = fCurrentPosition - diff - fWhiteStartPosition;
                if (count > 0) {
                    fWikiModel.append(new ContentToken(fStringSource.substring(fWhiteStartPosition, fWhiteStartPosition + count)));
                }
            } finally {
                fWhiteStart = false;
            }
        }
    }

    protected boolean parseHTMLCommentTags() {
        if (fStringSource.startsWith("<!--", fCurrentPosition - 1)) {
            int htmlStartPosition = fCurrentPosition;
            fCurrentPosition += 3;
            readUntil("-->");
            createContentToken(fCurrentPosition - htmlStartPosition + 1);
            return true;
        }
        return false;
    }

    protected void reduceTokenStackBoldItalic() {
        boolean found = false;
        while (fWikiModel.stackSize() > 0) {
            TagToken token = fWikiModel.peekNode();//
            if (token.equals(BOLD) || token.equals(ITALIC) || token.equals(BOLDITALIC)) {
                if (fWhiteStart) {
                    found = true;
                    createContentToken(1);
                }
                fWikiModel.popNode();
            } else {
                return;
            }
        }
        if (found) {
            fWhiteStart = true;
            fWhiteStartPosition = fCurrentPosition;
        }
    }

    /**
     * Reduce the current token stack until the given nodes name is at the top of
     * the stack. Useful for closing HTML tags.
     */
    protected void reduceStackUntilToken(TagToken node) {
        TagToken tag;
        int index = -1;
        String allowedParents = node.getParents();
        while (fWikiModel.stackSize() > 0) {
            tag = fWikiModel.peekNode();
            if (node.getName().equals(tag.getName())) {
                fWikiModel.popNode();
                break;
            }
            if (allowedParents == null) {
                fWikiModel.popNode();
            } else {
                index = allowedParents.indexOf("|" + tag.getName() + "|");
                if (index < 0) {
                    fWikiModel.popNode();
                } else {
                    break;
                }
            }
        }
    }

    protected TagStack parseRecursiveInternal(IWikiModel wikiModel, boolean createOnlyLocalStack, boolean noTOC) {
        // local stack for this wiki snippet
        TagStack localStack = new TagStack();
        // global wiki model stack
        TagStack globalWikiModelStack = wikiModel.swapStack(localStack);
        try {
            // fix for infinite recursion

            int level = wikiModel.incrementRecursionLevel();
            // int parserRecursionLevel = wikiModel.incrementParserRecursionLevel();
            // if (parserRecursionLevel > Configuration.PARSER_RECURSION_LIMIT) {
            // TagNode error = new TagNode("span");
            // error.addAttribute("class", "error", true);
            // error.addChild(new
            // ContentToken("Error - total recursion count limit exceeded parsing wiki tags."));
            // localStack.append(error);
            // return localStack;
            // }

            if (level > Configuration.PARSER_RECURSION_LIMIT) {
                TagNode error = new TagNode("span");
                error.addAttribute("class", "error", true);
                error.addChild(new ContentToken("Error - recursion limit exceeded parsing wiki tags."));
                localStack.append(error);
                return localStack;
            }
            // WikipediaParser parser = new WikipediaParser(rawWikitext,
            // wikiModel.isTemplateTopic(), wikiModel.getWikiListener());
            setModel(wikiModel);
            setNoToC(noTOC);
            runParser();
            return localStack;
        } catch (Exception | Error e) {
            e.printStackTrace();
            TagNode error = new TagNode("span");
            error.addAttribute("class", "error", true);
            error.addChild(new ContentToken(e.getClass().getSimpleName()));
            localStack.append(error);
        } finally {
            wikiModel.decrementRecursionLevel();
            // wikiModel.decrementParserRecursionLevel();
            if (!createOnlyLocalStack) {
                // append the resursively parsed local stack to the global wiki
                // model
                // stack
                globalWikiModelStack.append(localStack);
            }
            wikiModel.swapStack(globalWikiModelStack);
        }

        return localStack;
    }
}
