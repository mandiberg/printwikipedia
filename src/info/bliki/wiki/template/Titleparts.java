package info.bliki.wiki.template;

import info.bliki.wiki.model.IWikiModel;

import java.util.List;

/**
 * A template parser function for <code>{{titleparts: ... }}</code>. This
 * function separates a pagetitle into segments based on slashes, then returns
 * some of those segments as output. See <a href=
 * "https://www.mediawiki.org/wiki/Help:Extension:ParserFunctions#.23titleparts"
 * >Mediawiki - Help:Extension:ParserFunctions - Titleparts</a>
 *
 */
public class Titleparts extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Titleparts();


    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) {
        if (list.size() > 0) {
            String pagename = isSubst ? list.get(0) : parseTrim(list.get(0), model);
            // If the number of segments parameter is not specified, it defaults to
            // "0", which returns all the segments from first segment (included).
            int numberOfSegments = 0;
            if (list.size() > 1) {
                try {
                    String str = isSubst ? list.get(1) : parseTrim(list.get(1), model);
                    numberOfSegments = Integer.parseInt(str);
                } catch (NumberFormatException nfe) {

                }
            }
            // If the first segment parameter is not specified or is "0", it
            // defaults to "1":
            int firstSegment = 1;
            if (list.size() > 2) {
                try {
                    String str = isSubst ? list.get(2) : parseTrim(list.get(2), model);
                    firstSegment = Integer.parseInt(str);
                    if (firstSegment == 0) {
                        firstSegment = 1;
                    }
                } catch (NumberFormatException nfe) {

                }
            }
            return getTitleparts(pagename, numberOfSegments, firstSegment);
        }
        return null;
    }

    public static String getTitleparts(String pagename, int numberOfSegments, int firstSegment) {
        int indx = -1;
        if (firstSegment > 0) {
            indx = -1;
            while (firstSegment > 1) {
                indx = pagename.indexOf('/', ++indx);
                if (indx < 0) {
                    return "";
                }
                firstSegment--;
            }
            if (indx > 0) {
                pagename = pagename.substring(indx + 1);
            }
        } else {
            // Negative values for first segment translates to
            // "add this value to the total number of segments", loosely equivalent
            // to "count from the right":
            indx = pagename.length();
            while (firstSegment < 0) {
                indx = pagename.lastIndexOf('/', --indx);
                if (indx < 0) {
                    return pagename;
                }
                firstSegment++;
            }
            if (indx >= 0) {
                pagename = pagename.substring(indx + 1);
            }
        }
        if (numberOfSegments >= 0) {
            indx = -1;
            while (numberOfSegments > 0) {
                indx = pagename.indexOf('/', ++indx);
                if (--numberOfSegments == 0) {
                    if (indx >= 0) {
                        return pagename.substring(0, indx);
                    }
                    return pagename;
                }
                if (indx < 0) {
                    return pagename;
                }
            }
        } else {
            indx = pagename.length();
            while (numberOfSegments < 0) {
                indx = pagename.lastIndexOf('/', --indx);
                if (++numberOfSegments == 0) {
                    if (indx >= 0) {
                        break;
                    }
                    return "";
                }
                if (indx < 0) {
                    return "";
                }
            }
            if (indx >= 0) {
                pagename = pagename.substring(0, indx);
            }
        }
        return pagename;
    }
}
