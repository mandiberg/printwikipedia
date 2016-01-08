package info.bliki.wiki.tags.code;

import java.util.HashMap;

/**
 * Syntax highlighting support for Python source codes
 *
 */
public class PythonCodeFilter extends AbstractCPPBasedCodeFilter implements SourceCodeFormatter {

    private static HashMap<String, String> KEYWORD_SET = new HashMap<>();

    private static final String[] KEYWORDS = { "False", "None", "True", "and", "as", "assert", "class", "break", "continue", "def",
            "del", "else", "elif", "except", "finally", "for", "from", "global", "is", "import", "in", "if", "lambda", "nonlocal", "not",
            "or", "pass", "raise", "return", "try", "with", "while", "yield" };

    private static final String[] OBJECT_WORDS = {
            // see http://docs.python.org/library/functions.html
            "abs", "all", "any", "basestring", "bin", "bool", "callable", "chr", "classmethod", "cmp", "compile", "complex", "delattr",
            "dict", "dir", "divmod", "enumerate", "eval", "execfile", "file", "filter", "float", "format", "frozenset", "getattr",
            "globals", "hasattr", "hash", "help", "hex", "id", "input", "int", "isinstance", "issubclass", "iter", "len", "list",
            "locals", "long", "map", "max", "min", "next", "object", "oct", "open", "ord", "pow", "print", "property", "range", "raw",
            "reduce", "reload", "repr", "reversed", "round", "set", "setattr", "slice", "sorted", "staticmethod", "str", "sum", "super",
            "tuple", "type", "unichr", "unicode", "vars", "xrange", "zip", "__import__", "apply", "buffer", "coerce", "intern" };

    private static HashMap<String, String> OBJECT_SET = new HashMap<>();

    static {
        for (int i = 0; i < KEYWORDS.length; i++) {
            createHashMap(KEYWORD_SET, KEYWORDS[i]);
        }
        for (int i = 0; i < OBJECT_WORDS.length; i++) {
            createObjectsMap(OBJECT_SET, OBJECT_WORDS[i]);
        }
    }

    public PythonCodeFilter() {
    }

    @Override
    public String filter(String input) {
        char[] source = input.toCharArray();
        int currentPosition = 0;
        int identStart = 0;
        char currentChar = ' ';

        HashMap<String, String> keywordsSet = getKeywordSet();
        HashMap<String, String> objectsSet = getObjectSet();
        StringBuilder result = new StringBuilder(input.length() + input.length() / 4);
        boolean identFound = false;
        // result.append("<font color=\"#000000\">");
        try {
            while (true) {
                currentChar = source[currentPosition++];
                if ((currentChar >= 'A' && currentChar <= 'Z') || (currentChar == '_') || (currentChar >= 'a' && currentChar <= 'z')) {
                    identStart = currentPosition - 1;
                    identFound = true;
                    // start of identifier ?
                    while ((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || currentChar == '_') {
                        currentChar = source[currentPosition++];
                    }
                    currentPosition = appendIdentifier(input, identStart, currentPosition, keywordsSet, objectsSet, result);
                    identFound = false;
                    continue; // while loop
                } else if (currentChar == '\"') { // strings
                    result.append(FONT_STRINGS);
                    appendChar(result, currentChar);
                    while (currentPosition < input.length()) {
                        currentChar = source[currentPosition++];
                        appendChar(result, currentChar);
                        if (currentChar == '\\') {
                            currentChar = source[currentPosition++];
                            appendChar(result, currentChar);
                            continue;
                        }
                        if (currentChar == '\"') {
                            break;
                        }
                    }
                    result.append(FONT_END);
                    continue;
                } else if (currentChar == '\'') { // strings
                    result.append(FONT_STRINGS);
                    appendChar(result, currentChar);
                    while (currentPosition < input.length()) {
                        currentChar = source[currentPosition++];
                        appendChar(result, currentChar);
                        if (currentChar == '\\') {
                            currentChar = source[currentPosition++];
                            appendChar(result, currentChar);
                            continue;
                        }
                        if (currentChar == '\'') {
                            break;
                        }
                    }
                    result.append(FONT_END);
                    continue;
                } else if (currentChar == '#') {
                    // line comment
                    result.append(FONT_COMMENT);
                    appendChar(result, currentChar);
                    appendChar(result, source[currentPosition++]);
                    while (currentPosition < input.length()) {
                        currentChar = source[currentPosition++];
                        appendChar(result, currentChar);
                        if (currentChar == '\n') {
                            break;
                        }
                    }
                    result.append(FONT_END);
                    continue;
                }
                appendChar(result, currentChar);

            }
        } catch (IndexOutOfBoundsException e) {
            if (identFound) {
                currentPosition = appendIdentifier(input, identStart, currentPosition, keywordsSet, null, result);
            }
        }
        // result.append(FONT_END);
        return result.toString();
    }

    /**
     * @return Returns the KEYWORD_SET.
     */
    @Override
    public HashMap<String, String> getKeywordSet() {
        return KEYWORD_SET;
    }

    public String getName() {
        return "python";
    }

    /**
     * @return Returns the OBJECT_SET.
     */
    @Override
    public HashMap<String, String> getObjectSet() {
        return OBJECT_SET;
    }

}
