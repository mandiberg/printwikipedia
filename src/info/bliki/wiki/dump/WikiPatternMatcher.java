package info.bliki.wiki.dump;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Can be used as a stand alone class to pattern match parts of the wiki
 * formatted text.
 *
 *
 * Copied and modified from the <a
 * href="http://code.google.com/p/wikixmlj/">WikiXMLJ project</a>.
 *
 * @author Delip Rao modified by Axel Kramer
 *
 */
public class WikiPatternMatcher {

    private String wikiText = null;
    private List<String> pageCats = null;
    private List<String> pageLinks = null;
    private boolean redirect = false;
    private String redirectString = null;
    private boolean stub = false;
    private boolean disambiguation = false;

    private final static Pattern REDIRECT_PATTERN = Pattern.compile("#REDIRECT\\s+\\[\\[(.*?)\\]\\]");
    private final static Pattern STUB_PATTERN = Pattern.compile("\\-stub\\}\\}");
    private final static Pattern DISAMB_TEMPLATE_PATTERN = Pattern.compile("\\{\\{disambig\\}\\}");
    private final static Pattern CATEGORY_PATTERN = Pattern.compile("\\[\\[Category:(.*?)\\]\\]", Pattern.MULTILINE);
    private final static Pattern LINKS_PATTERN = Pattern.compile("\\[\\[(.*?)\\]\\]", Pattern.MULTILINE);

    private InfoBox infoBox = null;

    public WikiPatternMatcher(String wtext) {
        wikiText = wtext;
        Matcher matcher = REDIRECT_PATTERN.matcher(wikiText);
        if (matcher.find()) {
            redirect = true;
            if (matcher.groupCount() == 1)
                redirectString = matcher.group(1);
        }
        matcher = STUB_PATTERN.matcher(wikiText);
        stub = matcher.find();
        matcher = DISAMB_TEMPLATE_PATTERN.matcher(wikiText);
        disambiguation = matcher.find();
    }

    public boolean isRedirect() {
        return redirect;
    }

    public boolean isStub() {
        return stub;
    }

    public String getRedirectText() {
        return redirectString;
    }

    public String getText() {
        return wikiText;
    }

    public List<String> getCategories() {
        if (pageCats == null)
            parseCategories();
        return pageCats;
    }

    public List<String> getLinks() {
        if (pageLinks == null)
            parseLinks();
        return pageLinks;
    }

    private void parseCategories() {
        pageCats = new ArrayList<>();
        // Pattern catPattern = Pattern.compile("\\[\\[Category:(.*?)\\]\\]",
        // Pattern.MULTILINE);
        Matcher matcher = CATEGORY_PATTERN.matcher(wikiText);
        while (matcher.find()) {
            String[] temp = matcher.group(1).split("\\|");
            pageCats.add(temp[0]);
        }
    }

    private void parseLinks() {
        pageLinks = new ArrayList<>();

        Matcher matcher = LINKS_PATTERN.matcher(wikiText);
        while (matcher.find()) {
            String[] temp = matcher.group(1).split("\\|");
            if (temp == null || temp.length == 0)
                continue;
            String link = temp[0];
            if (link.contains(":") == false) {
                pageLinks.add(link);
            }
        }
    }

    /**
     * Strip wiki formatting characters from the given wiki text.
     *
     * @return
     */
    public String getPlainText() {
        String text = wikiText.replaceAll("&gt;", ">");
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("<ref>.*?</ref>", " ");
        text = text.replaceAll("</?.*?>", " ");
        text = text.replaceAll("\\{\\{.*?\\}\\}", " ");
        text = text.replaceAll("\\[\\[.*?:.*?\\]\\]", " ");
        text = text.replaceAll("\\[\\[(.*?)\\]\\]", "$1");
        text = text.replaceAll("\\s(.*?)\\|(\\w+\\s)", " $2");
        text = text.replaceAll("\\[.*?\\]", " ");
        text = text.replaceAll("\\'+", "");
        return text;
    }

    /**
     * Parse the Infobox template (i.e. parsing a string starting with
     * &quot;{{Infobox&quot; and ending with &quot;}}&quot;)
     *
     * @return <code>null</code> if the Infobox template wasn't found.
     */
    public InfoBox getInfoBox() {
        // parseInfoBox is expensive. Doing it only once like other parse* methods
        if (infoBox == null)
            infoBox = parseInfoBox();
        return infoBox;
    }

    /**
     * Parse the Infobox template (i.e. parsing a string starting with
     * &quot;{{Infobox&quot; and ending with &quot;}}&quot;)
     *
     * @return <code>null</code> if the Infobox template wasn't found.
     */
    private InfoBox parseInfoBox() {
        String INFOBOX_CONST_STR = "{{Infobox";
        int startPos = wikiText.indexOf(INFOBOX_CONST_STR);
        if (startPos < 0)
            return null;
        int bracketCount = 2;
        int endPos = startPos + INFOBOX_CONST_STR.length();

        if (endPos >= wikiText.length()) {
            return null;
        }
        for (; endPos < wikiText.length(); endPos++) {
            switch (wikiText.charAt(endPos)) {
            case '}':
                bracketCount--;
                break;
            case '{':
                bracketCount++;
                break;
            default:
            }
            if (bracketCount == 0)
                break;
        }
        String infoBoxText;
        if (endPos >= wikiText.length()) {
            infoBoxText = wikiText.substring(startPos);
        } else {
            infoBoxText = wikiText.substring(startPos, endPos + 1);
        }
        infoBoxText = stripCite(infoBoxText); // strip clumsy {{cite}} tags
        // strip any html formatting
        infoBoxText = infoBoxText.replaceAll("&gt;", ">");
        infoBoxText = infoBoxText.replaceAll("&lt;", "<");
        infoBoxText = infoBoxText.replaceAll("<ref.*?>.*?</ref>", " ");
        infoBoxText = infoBoxText.replaceAll("</?.*?>", " ");

        return new InfoBox(infoBoxText);
    }

    private String stripCite(String text) {
        String CITE_CONST_STR = "{{cite";
        int startPos = text.indexOf(CITE_CONST_STR);
        if (startPos < 0)
            return text;
        int bracketCount = 2;
        int endPos = startPos + CITE_CONST_STR.length();
        for (; endPos < text.length(); endPos++) {
            switch (text.charAt(endPos)) {
            case '}':
                bracketCount--;
                break;
            case '{':
                bracketCount++;
                break;
            default:
            }
            if (bracketCount == 0)
                break;
        }
        text = text.substring(0, startPos - 1) + text.substring(endPos);
        return stripCite(text);
    }

    public boolean isDisambiguationPage() {
        return disambiguation;
    }

    public String getTranslatedTitle(String languageCode) {
        Pattern translatePattern = Pattern.compile("^\\[\\[" + languageCode + ":(.*?)\\]\\]$", Pattern.MULTILINE);
        Matcher matcher = translatePattern.matcher(wikiText);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
