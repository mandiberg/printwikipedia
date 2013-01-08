package info.bliki.wiki.tags.code;

import java.util.HashMap;
import java.util.HashSet;

 
/**
 * Syntax highlighting support for Groovy source codes
 * 
 */
public class GroovyCodeFilter extends AbstractCPPBasedCodeFilter implements SourceCodeFormatter {

  private static HashMap<String, String> KEYWORD_SET = new HashMap<String, String>();

  private static final String[] KEYWORDS =
    {
    	"any",
  	  "as",
      "class",
      "abstract",
      "break",
      "byvalue",
      "case",
      "cast",
      "catch",
      "const",
      "continue",
      "def",
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
      "in",
      "inner",
      "instanceof",
      "interface",
      "mixin",
      "native",
      "new",
      "null",
      "operator",
      "outer",
      "package",
      "private",
      "property",
      "protected",
      "public",
      "rest",
      "return",
      "static",
      "super",
      "switch",
      "synchronized",
      "test",
      "this",
      "threadsafe",
      "throw",
      "throws",
      "transient",
      "true",
      "try",
      "using",
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

  private static HashSet<String> OBJECT_SET = new HashSet<String>();

  static {
    for (int i = 0; i < KEYWORDS.length; i++) {
      createHashMap(KEYWORD_SET, KEYWORDS[i]);
    }
    for (int i = 0; i < OBJECT_WORDS.length; i++) {
      OBJECT_SET.add(OBJECT_WORDS[i]);
    }
  }

  public GroovyCodeFilter() {
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
	public HashSet<String> getObjectSet() {
    return OBJECT_SET;
  }

}