package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.EndTagToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.TagToken;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.DefaultEventListener;
import info.bliki.wiki.model.IEventListener;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.HTMLBlockTag;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.WPBoldItalicTag;
import info.bliki.wiki.tags.WPTag;
import info.bliki.wiki.tags.util.Attribute;
import info.bliki.wiki.tags.util.IBodyTag;
import info.bliki.wiki.tags.util.INoBodyParsingTag;
import info.bliki.wiki.tags.util.NodeAttribute;
import info.bliki.wiki.tags.util.TagStack;
import info.bliki.wiki.tags.util.WikiTagNode;

import java.util.List;

/**
 * A Wikipedia syntax parser for parsing in wiki preformatted blocks (rendered
 * as &lt;pre&gt;...&lt;/pre&gt;)
 *
 */
public class WikipediaPreTagParser extends AbstractWikipediaParser {
    private boolean fHtmlCodes = true;
    private IEventListener fEventListener;

    public WikipediaPreTagParser(String stringSource) {
        this(stringSource, null);
    }

    public WikipediaPreTagParser(String stringSource, IEventListener wikiListener) {
        super(stringSource);
        if (wikiListener == null) {
            fEventListener = DefaultEventListener.CONST;
        } else {
            fEventListener = wikiListener;
        }
    }

    public int getNextToken() // throws InvalidInputException
    {
        fWhiteStart = true;
        fWhiteStartPosition = fCurrentPosition;
        try {
            while (true) {
                fCurrentCharacter = fSource[fCurrentPosition++];

                // ---------Identify the next token-------------
                switch (fCurrentCharacter) {
                case '\n':
                    // check at the end of line, if there is open wiki bold or italic
                    // markup
                    reduceTokenStackBoldItalic();
                    break;
                }

                // ---------Identify the next token-------------
                switch (fCurrentCharacter) {
                case '[':
                    if (parseWikiLink()) {
                        continue;
                    }
                    break;
                case '\'':
                    if (getNextChar('\'')) {
                        if (getNextChar('\'')) {
                            if (getNextChar('\'')) {
                                if (getNextChar('\'')) {
                                    createContentToken(5);
                                    return TokenBOLDITALIC;
                                }
                                fCurrentPosition -= 1;
                                fWhiteStart = true;
                                createContentToken(3);
                                return TokenBOLD;
                            }
                            createContentToken(3);
                            return TokenBOLD;
                        }
                        createContentToken(2);
                        return TokenITALIC;
                    }
                    break;
                case '<':
                    if (fHtmlCodes) {
                        int htmlStartPosition = fCurrentPosition;
                        // HTML tags are allowed
                        try {
                            switch (fStringSource.charAt(fCurrentPosition)) {
                            case '!': // <!-- HTML comment -->
                                if (parseHTMLCommentTags()) {
                                    continue;
                                }
                                break;
                            default:

                                if (fSource[fCurrentPosition] != '/') {
                                    // opening HTML tag
                                    WikiTagNode tagNode = parseTag(fCurrentPosition);
                                    if (tagNode != null) {
                                        String tagName = tagNode.getTagName();
                                        TagToken tag = fWikiModel.getTokenMap().get(tagName);
                                        if ((tag != null) && !(tag instanceof HTMLBlockTag)) {
                                            tag = (TagToken) tag.clone();

                                            if (tag instanceof TagNode) {
                                                TagNode node = (TagNode) tag;
                                                List<NodeAttribute> attributes = tagNode.getAttributesEx();
                                                Attribute attr;
                                                for (int i = 1; i < attributes.size(); i++) {
                                                    attr = attributes.get(i);
                                                    node.addAttribute(attr.getName(), attr.getValue(), true);
                                                }
                                            }
                                            if (tag instanceof HTMLTag) {
                                                // ((HTMLTag) tag).setTemplate(isTemplate());
                                            }

                                            createContentToken(1);

                                            fCurrentPosition = fScannerPosition;

                                            String allowedParents = tag.getParents();
                                            if (allowedParents != null) {
                                                reduceTokenStack(tag);
                                            }
                                            createTag(tag, tagNode, tagNode.getEndPosition());
                                            return TokenIgnore;

                                        }
                                        break;
                                    }
                                } else {
                                    // closing HTML tag
                                    WikiTagNode tagNode = parseTag(++fCurrentPosition);
                                    if (tagNode != null) {
                                        String tagName = tagNode.getTagName();
                                        TagToken tag = fWikiModel.getTokenMap().get(tagName);
                                        if ((tag != null) && !(tag instanceof HTMLBlockTag)) {
                                            createContentToken(2);
                                            fCurrentPosition = fScannerPosition;

                                            if (fWikiModel.stackSize() > 0) {
                                                TagToken topToken = fWikiModel.peekNode();
                                                if (topToken.getName().equals(tag.getName())) {
                                                    fWikiModel.popNode();
                                                    return TokenIgnore;
                                                } else {
                                                    if (tag.isReduceTokenStack()) {
                                                        reduceStackUntilToken(tag);
                                                    }
                                                }
                                            }
                                            return TokenIgnore;
                                        }
                                        break;
                                    }
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            // do nothing
                        }
                        fCurrentPosition = htmlStartPosition;
                    }
                    break;
                default:
                }

                if (!fWhiteStart) {
                    fWhiteStart = true;
                    fWhiteStartPosition = fCurrentPosition - 1;
                }

            }
            // -----------------end switch while try--------------------
        } catch (IndexOutOfBoundsException e) {
            // end of scanner text
        }
        try {
            createContentToken(1);
        } catch (IndexOutOfBoundsException e) {
            // end of scanner text
        }
        return TokenEOF;
    }

    /**
     * Parse a wiki section starting with a '[' character
     *
     * @return <code>true</code> if a correct link was found
     */
    private boolean parseWikiLink() {
        int startLinkPosition = fCurrentPosition;
        if (getNextChar('[')) {
            return parseWikiTag();
        } else {
            createContentToken(1);
            fWhiteStart = false;

            if (readUntilCharOrStopAtEOL(']')) {
                String name = fStringSource.substring(startLinkPosition, fCurrentPosition - 1);

                // if (handleHTTPLink(name)) {
                // return true;
                // }
            }
            fCurrentPosition = startLinkPosition;
        }
        return false;
    }

    /**
     * Parse a wiki section starting with a '[[' sequence
     *
     * @return <code>true</code> if a correct link was found
     */
    private boolean parseWikiTag() {
        int startLinkPosition = fCurrentPosition;
        int endLinkPosition;
        // wikipedia link style
        createContentToken(2);

        int temp = fCurrentPosition;
        if (findWikiLinkEnd()) {
            endLinkPosition = fCurrentPosition - 2;
            String name = fStringSource.substring(startLinkPosition, endLinkPosition);
            // test for a suffix string behind the Wiki link. Useful for plurals.
            // Example:
            // Dolphins are [[aquatic mammal]]s that are closely related to [[whale]]s
            // and [[porpoise]]s.
            temp = fCurrentPosition;
            String suffix = "";
            try {
                fCurrentCharacter = fSource[fCurrentPosition];
                if (Character.isLowerCase(fCurrentCharacter)) {
                    fCurrentPosition++;
                    StringBuilder suffixBuffer = new StringBuilder(16);
                    suffixBuffer.append(fCurrentCharacter);
                    while (true) {
                        fCurrentCharacter = fSource[fCurrentPosition++];
                        if (!Character.isLowerCase(fCurrentCharacter)) {
                            fCurrentPosition--;
                            break;
                        }
                        suffixBuffer.append(fCurrentCharacter);
                    }
                    suffix = suffixBuffer.toString();
                }
            } catch (IndexOutOfBoundsException e) {
                fCurrentPosition = temp;
            }
            fEventListener.onWikiLink(fSource, startLinkPosition, endLinkPosition, suffix);
            if (!fWikiModel.appendRawWikipediaLink(name, suffix)) {
                fCurrentPosition = temp;
                // this is probably a special image link
                throw new InvalidPreWikiTag("parseWikiTag");
            }
            return true;
        } else {
            fWhiteStart = true;
            fWhiteStartPosition = startLinkPosition - 2;
            fCurrentPosition = temp + 1;
        }
        return false;
    }

    private void createTag(TagToken tag, WikiTagNode tagNode, int startMacroPosition) {
        String endTag;
        String macroBodyString;
        int index0;
        String command = tagNode.getTagName();
        if ((tag != null) && (tag instanceof IBodyTag) && (!tagNode.isEmptyXmlTag())) {
            endTag = command + '>';
            index0 = Util.indexOfIgnoreCase(fStringSource, "</", endTag, startMacroPosition);

            if (index0 >= 0) {
                macroBodyString = fStringSource.substring(startMacroPosition, index0);
                fCurrentPosition = index0 + endTag.length() + 2;
            } else {
                macroBodyString = fStringSource.substring(startMacroPosition, fSource.length);
                fCurrentPosition = fSource.length;
            }
        } else {
            macroBodyString = null;
            fCurrentPosition = startMacroPosition;
        }

        handleTag(tag, tagNode, macroBodyString);
    }

    private void handleTag(TagToken tag, WikiTagNode tagNode, String bodyString) {
        String command = tagNode.getTagName();
        try {
            if (tag instanceof EndTagToken) {
                fWikiModel.append(tag);
            } else {
                fWikiModel.pushNode(tag);
                if (null != bodyString) {
                    if (tag instanceof INoBodyParsingTag) {
                        ((TagNode) tag).addChild(new ContentToken(bodyString));
                    } else {
                        // recursively filter tags within the tags body string
                        WikipediaPreTagParser.parseRecursive(bodyString.trim(), fWikiModel, false, true);
                    }
                }
                if (tag instanceof IBodyTag) {
                    fWikiModel.popNode();
                }
            }
        } catch (IllegalArgumentException e) {
            TagNode divTagNode = new TagNode("div");
            divTagNode.addAttribute("class", "error", true);
            divTagNode.addChild(new ContentToken("IllegalArgumentException: " + command + " - " + e.getMessage()));
            fWikiModel.append(divTagNode);
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
            TagNode divTagNode = new TagNode("div");
            divTagNode.addAttribute("class", "error", true);
            divTagNode.addChild(new ContentToken(command + ": " + e.getMessage()));
            fWikiModel.append(divTagNode);
            e.printStackTrace();
        }
    }

    @Override
    public void runParser() {
        int token;
        while ((token = getNextToken()) != TokenEOF) {
            switch (token) {
            case TokenBOLDITALIC:
                if (fWikiModel.stackSize() > 0 && fWikiModel.peekNode().equals(BOLDITALIC)) {
                    fWikiModel.popNode();
                    // fResultBuffer.append("</i></b>");
                } else if (fWikiModel.stackSize() > 1 && fWikiModel.peekNode().equals(BOLD)
                        && fWikiModel.getNode(fWikiModel.stackSize() - 2).equals(ITALIC)) {
                    fWikiModel.popNode();
                    fWikiModel.popNode();
                    // fResultBuffer.append("</b></i>");
                } else if (fWikiModel.stackSize() > 1 && fWikiModel.peekNode().equals(ITALIC)
                        && fWikiModel.getNode(fWikiModel.stackSize() - 2).equals(BOLD)) {
                    fWikiModel.popNode();
                    fWikiModel.popNode();
                    // fResultBuffer.append("</i></b>");
                } else if (fWikiModel.stackSize() > 0 && fWikiModel.peekNode().equals(BOLD)) {
                    fWikiModel.popNode();
                    fWikiModel.pushNode(new WPTag("i"));
                } else if (fWikiModel.stackSize() > 0 && fWikiModel.peekNode().equals(ITALIC)) {
                    fWikiModel.popNode();
                    fWikiModel.pushNode(new WPTag("b"));
                } else {
                    fWikiModel.pushNode(new WPBoldItalicTag());
                    // fResultBuffer.append("<b><i>");
                }
                break;
            case TokenBOLD:
                if (fWikiModel.stackSize() > 0 && fWikiModel.peekNode().equals(BOLDITALIC)) {
                    fWikiModel.popNode();
                    fWikiModel.pushNode(new WPTag("i"));
                    // fResultBuffer.append("</b>");
                } else if (fWikiModel.stackSize() > 0 && fWikiModel.peekNode().equals(BOLD)) {
                    fWikiModel.popNode();
                    // fResultBuffer.append("</b>");
                } else {
                    fWikiModel.pushNode(new WPTag("b"));
                    // fResultBuffer.append("<b>");
                }
                break;
            case TokenITALIC:
                if (fWikiModel.stackSize() > 0 && fWikiModel.peekNode().equals(BOLDITALIC)) {
                    fWikiModel.popNode();
                    fWikiModel.pushNode(new WPTag("b"));
                    // fResultBuffer.append("</i>");
                } else if (fWikiModel.stackSize() > 0 && fWikiModel.peekNode().equals(ITALIC)) {
                    fWikiModel.popNode();
                    // fResultBuffer.append("</i>");
                } else {
                    fWikiModel.pushNode(new WPTag("i"));
                    // fResultBuffer.append("<i>");
                }
                break;
            }
        }
        reduceTokenStack();

    }

    @Override
    protected void setNoToC(boolean noToC) {
    }

    /**
     * Call the parser on the subsequent recursion levels, where the subtexts (of
     * templates, table cells, list items or image captions) don't contain a table
     * of contents (TOC)
     *
     * <b>Note:</b> the wiki model doesn't call the <code>setUp()</code> or
     * <code>tearDown()</code> methods for the subsequent recursive parser steps.
     *
     * @param rawWikitext
     * @param wikiModel
     */
    public static void parseRecursive(String rawWikitext, IWikiModel wikiModel) {
        parseRecursive(rawWikitext, wikiModel, false, true);
    }

    /**
     * Call the parser on the subsequent recursion levels, where the subtexts (of
     * templates, table cells, list items or image captions) don't contain a table
     * of contents (TOC)
     *
     * <b>Note:</b> the wiki model doesn't call the <code>setUp()</code> or
     * <code>tearDown()</code> methods for the subsequent recursive parser steps.
     *
     * @param rawWikitext
     * @param wikiModel
     * @param noTOC
     * @param appendStack
     */
    public static TagStack parseRecursive(String rawWikitext, IWikiModel wikiModel, boolean createOnlyLocalStack, boolean noTOC) {
        WikipediaPreTagParser parser = new WikipediaPreTagParser(rawWikitext);
        return parser.parseRecursiveInternal(wikiModel, createOnlyLocalStack, noTOC);
    }

    @Override
    public TagStack parseRecursiveInternal(IWikiModel wikiModel, boolean createOnlyLocalStack, boolean noTOC) {
        // local stack for this wiki snippet
        TagStack localStack = new TagStack();
        // global wiki model stack
        TagStack globalWikiModelStack = wikiModel.swapStack(localStack);
        try {
            int level = wikiModel.incrementRecursionLevel();

            if (level > Configuration.PARSER_RECURSION_LIMIT) {
                TagNode error = new TagNode("span");
                error.addAttribute("class", "error", true);
                error.addChild(new ContentToken("Error - recursion limit exceeded parsing wiki tags."));
                localStack.append(error);
                return localStack;
            }
            setModel(wikiModel);
            runParser();
            return localStack;
        } catch (InvalidPreWikiTag ipwt) {
            createOnlyLocalStack = true;
            throw ipwt;
        } catch (Exception | Error e) {
            e.printStackTrace();
            TagNode error = new TagNode("span");
            error.addAttribute("class", "error", true);
            error.addChild(new ContentToken(e.getClass().getSimpleName()));
            localStack.append(error);
        } finally {
            wikiModel.decrementRecursionLevel();
            if (!createOnlyLocalStack) {
                // append the resursively parsed local stack to the global wiki model stack
                globalWikiModelStack.append(localStack);
            }
            wikiModel.swapStack(globalWikiModelStack);
        }

        return localStack;
    }

    /**
     * Reduce the current token stack until an allowed parent is at the top of the
     * stack
     */
    private void reduceTokenStack(TagToken node) {
        String allowedParents = node.getParents();
        if (allowedParents != null) {
            TagToken tag;
            int index;

            while (fWikiModel.stackSize() > 0) {
                tag = fWikiModel.peekNode();
                index = allowedParents.indexOf("|" + tag.getName() + "|");
                if (index < 0) {
                    fWikiModel.popNode();
                    if (tag.getName().equals(node.getName())) {
                        // for wrong nested HTML tags like <table> <tr><td>number
                        // 1<tr><td>number 2</table>
                        break;
                    }
                } else {
                    break;
                }
            }
        } else {
            while (fWikiModel.stackSize() > 0) {
                fWikiModel.popNode();
            }
        }
    }
}
