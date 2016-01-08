package info.bliki.wiki.tags.code;

import java.util.HashMap;


/**
 * Syntax highlighting support for PHP source codes
 *
 */
public class PHPCodeFilter extends AbstractCPPBasedCodeFilter implements SourceCodeFormatter {

  private static HashMap<String, String> KEYWORD_SET = new HashMap<>();

  private static final String[] KEYWORDS =
    {
      "if",
      "elseif",
      "else",
      "endif",
      "for",
      "endfor",
      "while",
      "endwhile",
      "switch",
      "case",
      "endswitch",
      "break",
      "continue",
      "return",
      "include",
      "include_once",
      "require",
      "require_once",
      "function",
      "class",
      "new",
      "do",
      "old_function",
      "default",
      "global",
      "static",
      "foreach",
      "endforeach",
      "extends",
      "empty",
      "array",
      "echo",
      "var",
      "as",
      "print",
      "unset",
      "exit",
      "and",
      "or",
      "xor",
      "list",
      "null",
      "false",
      "true",
      "abstract",
      "catch",
      "finally",
      "try",
      "private",
      "protected",
      "public",
      "interface",
      "implements",
      "instanceof",
      "super",
      "throw",
      "const",
      "declare",
      "enddeclare",
      "eval",
      "use",
      "isset",
      "final" };

  static  {
    for (int i = 0; i < KEYWORDS.length; i++) {
      createHashMap(KEYWORD_SET, KEYWORDS[i]);
    }

  }

  public PHPCodeFilter() {
  }

  /**
   * @return Returns the KEYWORD_SET.
   */
  @Override
    public HashMap<String, String> getKeywordSet() {
    return KEYWORD_SET;
  }

  /**
   * @return Returns the OBJECT_SET.
   */
  @Override
    public HashMap<String, String> getObjectSet() {
    return null;
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
//    result.append("<font color=\"#000000\">");
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
        } else if (currentChar == '\"') { //strings
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
        } else if (currentChar == '\'') { //strings
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
        } else if (currentChar == '<' && isPHPTag() && currentPosition + 3 < input.length() && source[currentPosition] == '?'
            && source[currentPosition + 1] == 'p' && source[currentPosition + 2] == 'h' && source[currentPosition + 3] == 'p') {
          // php start tag
          currentPosition += 4;
          result.append(FONT_KEYWORD+"&#60;?php"+FONT_END);
          continue;
        } else if (currentChar == '?' && isPHPTag() && currentPosition < input.length() && source[currentPosition] == '>') {
          // php start tag
          currentPosition++;
          result.append(FONT_KEYWORD+"?&#62;"+FONT_END);
          continue;
        }
        appendChar(result, currentChar);

      }
    } catch (IndexOutOfBoundsException e) {
      if (identFound) {
        currentPosition = appendIdentifier(input, identStart, currentPosition, keywordsSet, null, result);
      }
    }
    return result.toString();
  }

  @Override
    public boolean isKeywordCaseSensitive() {
    return false;
  }

  @Override
    public boolean isPHPTag() {
    return true;
  }
}
