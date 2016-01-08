package info.bliki.wiki.tags.code;

import java.util.HashMap;

/**
 * Syntax highlighting support for Groovy source codes
 *
 */
public class GroovyCodeFilter extends AbstractCPPBasedCodeFilter implements SourceCodeFormatter {

    private static HashMap<String, String> KEYWORD_SET = new HashMap<>();

    private static final String[] KEYWORDS = { "any", "as", "class", "abstract", "break", "byvalue", "case", "cast", "catch",
            "const", "continue", "def", "default", "do", "else", "extends", "false", "final", "finally", "for", "future", "generic",
            "goto", "if", "implements", "import", "in", "inner", "instanceof", "interface", "mixin", "native", "new", "null", "operator",
            "outer", "package", "private", "property", "protected", "public", "rest", "return", "static", "super", "switch",
            "synchronized", "test", "this", "threadsafe", "throw", "throws", "transient", "true", "try", "using", "var", "volatile",
            "while", "assert", "enum",

            "boolean", "char", "byte", "short", "int", "long", "float", "double", "void" };

    private static final String[] OBJECT_WORDS = { "Boolean", "Byte", "Character", "Class", "ClassLoader", "Cloneable", "Compiler",
            "Double", "Float", "Integer", "Long", "Math", "Number", "Object", "Process", "Runnable", "Runtime", "SecurityManager",
            "Short", "String", "StringBuffer", "System", "Thread", "ThreadGroup", "Void" };

    private static HashMap<String, String> OBJECT_SET = new HashMap<>();

    static {
        for (int i = 0; i < KEYWORDS.length; i++) {
            createHashMap(KEYWORD_SET, KEYWORDS[i]);
        }
        for (int i = 0; i < OBJECT_WORDS.length; i++) {
            createObjectsMap(OBJECT_SET, OBJECT_WORDS[i]);
        }
    }

    public GroovyCodeFilter() {
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
                } else if (currentChar == '\'' && currentPosition < input.length() - 1 && source[currentPosition] == '\''
                        && source[currentPosition + 1] == '\'') {
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
                        if (currentChar == '\'' && currentPosition < input.length() - 1 && source[currentPosition] == '\''
                            && source[currentPosition + 1] == '\'') {
                            break;
                        }
                    }
                    result.append(FONT_END);
                    continue;
                } else if (currentChar == '\'') { // characters
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
                } else if (currentChar == '/' && currentPosition < input.length() && source[currentPosition] == '/') {
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
                } else if (currentChar == '/' && currentPosition < input.length() && source[currentPosition] == '*') {
                    if (currentPosition < (input.length() - 1) && source[currentPosition + 1] == '*') {
                        // javadoc style
                        result.append(FONT_JAVADOC);
                    } else {
                        // multiline comment
                        result.append(FONT_COMMENT);
                    }
                    appendChar(result, currentChar);
                    appendChar(result, source[currentPosition++]);
                    while (currentPosition < input.length()) {
                        currentChar = source[currentPosition++];
                        appendChar(result, currentChar);
                        if (currentChar == '/' && source[currentPosition - 2] == '*') {
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
        return "groovy";
    }

    /**
     * @return Returns the OBJECT_SET.
     */
    @Override
    public HashMap<String, String> getObjectSet() {
        return OBJECT_SET;
    }

}
