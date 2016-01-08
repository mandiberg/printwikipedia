package info.bliki.wiki.tags.code;

import java.util.HashMap;


/**
 * Syntax highlighting support for Java source codes
 *
 */
public class JavaCodeFilter extends AbstractCPPBasedCodeFilter implements SourceCodeFormatter {

  private static HashMap<String, String> KEYWORD_SET = new HashMap<>();

  private static final String[] KEYWORDS =
    {
      "class",
      "abstract",
      "break",
      "byvalue",
      "case",
      "cast",
      "catch",
      "const",
      "continue",
      "default",
      "do",
      "else",
      "extends",
      "false",
      "final",
      "finally",
      "for",
      "future",
      "generic",
      "goto",
      "if",
      "implements",
      "import",
      "inner",
      "instanceof",
      "interface",
      "native",
      "new",
      "null",
      "operator",
      "outer",
      "package",
      "private",
      "protected",
      "public",
      "rest",
      "return",
      "static",
      "super",
      "switch",
      "synchronized",
      "this",
      "throw",
      "throws",
      "transient",
      "true",
      "try",
      "var",
      "volatile",
      "while",
      "assert",
      "enum",

      "boolean",
      "char",
      "byte",
      "short",
      "int",
      "long",
      "float",
      "double",
      "void"};

  private static final String[] OBJECT_WORDS =
    {
      "Boolean",
      "Byte",
      "Character",
      "Class",
      "ClassLoader",
      "Cloneable",
      "Compiler",
      "Double",
      "Float",
      "Integer",
      "Long",
      "Math",
      "Number",
      "Object",
      "Process",
      "Runnable",
      "Runtime",
      "SecurityManager",
      "Short",
      "String",
      "StringBuffer",
      "System",
      "Thread",
      "ThreadGroup",
      "Void"
      };

  private static HashMap<String, String> OBJECT_SET = new HashMap<>();

  static {
    for (int i = 0; i < KEYWORDS.length; i++) {
      createHashMap(KEYWORD_SET, KEYWORDS[i]);
    }
    for (int i = 0; i < OBJECT_WORDS.length; i++) {
      createObjectsMap(OBJECT_SET, OBJECT_WORDS[i]);
    }
  }

  public JavaCodeFilter() {
  }

  /**
   * @return Returns the KEYWORD_SET.
   */
  @Override
    public HashMap<String, String> getKeywordSet() {
    return KEYWORD_SET;
  }

  public String getName() {
    return "java";
  }

  /**
   * @return Returns the OBJECT_SET.
   */
  @Override
    public HashMap<String, String> getObjectSet() {
    return OBJECT_SET;
  }

}
