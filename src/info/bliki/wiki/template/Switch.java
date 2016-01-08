package info.bliki.wiki.template;

import info.bliki.wiki.filter.WikipediaScanner;
import info.bliki.wiki.model.IWikiModel;

import java.util.List;

/**
 * A template parser function for <code>{{ #switch: ... }}</code> syntax.
 *
 * See <a href
 * ="http://www.mediawiki.org/wiki/Help:Extension:ParserFunctions">Mediwiki's
 * Help:Extension:ParserFunctions</a>
 *
 */
public class Switch extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Switch();

    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) {
        if (list.size() < 2) {
            return null;
        }
        String defaultResult = null;
        final String conditionString = isSubst ? list.get(0) : parseTrim(list.get(0), model);

        boolean valueFound = false;
        for (int i = 1; i < list.size(); i++) {
            List<String> splitByEq = WikipediaScanner.splitByChar('=', list.get(i), null, 2);
            String leftHandSide = splitByEq.get(0); // note: there always is a first part
            final boolean hasRHS = splitByEq.size() == 2;
            if (hasRHS && valueFound) {
                return parseTrim(splitByEq.get(1), model);
            }
            String parsedLHS = isSubst ? leftHandSide : parseTrim(leftHandSide, model);
            if (hasRHS && "#default".equals(parsedLHS)) {
                // explicit default
                defaultResult = splitByEq.get(1);
                continue;
            }
            if (!hasRHS && i == list.size() - 1) {
                // implicit default as the last parameter and nothing matched before
                return parsedLHS;
            }
            if (equalsTypes(conditionString, parsedLHS, checkNumerically(conditionString))) {
                if (hasRHS) {
                    return parseTrim(splitByEq.get(1), model);
                } else {
                    valueFound = true;
                }
            }
        }
        return parseTrim(defaultResult, model);
    }

    private boolean equalsTypes(String first, String second, boolean checkNumerically) {
        if (first.length() == 0) {
            return second.length() == 0;
        } else if (second.length() == 0) {
            return first.length() == 0;
        }
        if (first.charAt(0) == '+') {
            first = first.substring(1);
        }
        if (second.charAt(0) == '+') {
            second = second.substring(1);
        }

        if (checkNumerically) {
            try {
                double d1 = Double.parseDouble(first);
                double d2 = Double.parseDouble(second);
                if (d1 == d2) {
                    return true;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return first.equals(second);
    }

    private static boolean checkNumerically(String condition) {
        if (condition.length() > 0) {
            if (isNumeric0(condition.charAt(0))) {
                return true;
            } else {
                for (int i = 0; i < condition.length(); i++) {
                    if (isNumeric1(condition.charAt(i))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isNumeric0(char c) {
        return c == '+'
                || c == '-'
                || c == '0'
                || c == '.'
                || c == ','
                || c == 'e'
                || c == 'E';
    }

    private static boolean isNumeric1(char c) {
        return c == '+'
                || c == '-'
                || c == '.'
                || c == ','
                || c == 'e'
                || c == 'E';
    }
}
