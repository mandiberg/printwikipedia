package info.bliki.wiki.dump;

/**
 * A class abstracting a Wiki infobox.
 *
 *
 * Copied and modified from the <a
 * href="http://code.google.com/p/wikixmlj/">WikiXMLJ project</a>.
 *
 * @author Delip Rao modified by Axel Kramer
 */
public class InfoBox {
    String infoBoxWikiText = null;

    InfoBox(String infoBoxWikiText) {
        this.infoBoxWikiText = infoBoxWikiText;
    }

    public String dumpRaw() {
        return infoBoxWikiText;
    }
}
