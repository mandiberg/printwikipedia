package info.bliki.html;

import info.bliki.html.wikipedia.IHTMLToWiki;
import info.bliki.htmlcleaner.HtmlCleaner;
import info.bliki.htmlcleaner.TagNode;

import java.io.IOException;

/**
 * Converts a given HTML string into a wiki text string
 *
 */
public class HTML2WikiConverter {
    String fInputHTML;

    public HTML2WikiConverter() {
        this(null);
    }

    public HTML2WikiConverter(String inputHTML) {
        fInputHTML = inputHTML;
    }

    /**
     * Converts a given HTML string into a wiki text string
     *
     * @param converter
     *          for creating the resulting wiki text string
     * @return
     */
    public String toWiki(IHTMLToWiki converter) {
        HtmlCleaner cleaner = null;
        StringBuilder resultBuffer = new StringBuilder(fInputHTML.length());
        try {
            cleaner = new HtmlCleaner(fInputHTML);
            cleaner.clean();
            // resultBuffer.append(cleaner.getXmlAsString());
            TagNode body = cleaner.getBodyNode();
            converter.nodeToWiki(body, resultBuffer);
        } catch (IOException e) {
        }
        int indx = resultBuffer.indexOf("<br>");
        char ch;
        if (indx >= 0) {
            int lastIndx = 0;
            StringBuilder tempBuffer = new StringBuilder(resultBuffer.length() + resultBuffer.length() / 10);
            while (indx > 0) {
                indx += 4;
                tempBuffer.append(resultBuffer.substring(lastIndx, indx));
                if (indx < resultBuffer.length()) {
                    ch = resultBuffer.charAt(indx);
                    if (ch != '\n' && ch != '\r' && ch != ' ' && ch != '#' && ch != '=' && ch != '*' && ch != ':' && ch != ';' && ch != '{'
                            && ch != '|') {
                        tempBuffer.append('\n');
                    }
                    lastIndx = indx;
                    indx = resultBuffer.indexOf("<br>", lastIndx);
                } else {
                    break;
                }
            }
            if (lastIndx < resultBuffer.length()) {
                tempBuffer.append(resultBuffer.substring(lastIndx));
            }
            return tempBuffer.toString();
        }
        return resultBuffer.toString();
    }

    public String getInputHTML() {
        return fInputHTML;
    }

    public void setInputHTML(String inputHTML) {
        this.fInputHTML = inputHTML;
    }
}
