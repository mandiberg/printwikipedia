package info.bliki.wiki.tags.code;

import java.util.HashMap;
import java.util.HashSet;


/**
 * Syntax highlighting support for C# source codes
 *   
 */
public class CSharpCodeFilter extends AbstractCPPBasedCodeFilter implements SourceCodeFormatter {

  private static HashMap<String, String> KEYWORD_SET = new HashMap<String, String>();

  private static final String[] KEYWORDS =
    {
      "class",
      "abstract",
      "event",
      "new",
      "struct",
      "as",
      "explicit",
      "null",
      "switch",
      "base",
      "extern",
      "this",
      "false",
      "operator",
      "throw",
      "break",
      "finally",
      "out",
      "true",
      "fixed",
      "override",
      "try",
      "case",
      "float",
      "params",
      "typeof",
      "catch",
      "for",
      "private",
      "foreach",
      "protected",
      "checked",
      "goto",
      "public",
      "unchecked",
      "if",
      "readonly",
      "unsafe",
      "const",
      "implicit",
      "ref",
      "continue",
      "in",
      "return",
      "using",
      "virtual",
      "default",
      "interface",
      "sealed",
      "volatile",
      "delegate",
      "internal",
      "do",
      "is",
      "sizeof",
      "while",
      "lock",
      "stackalloc",
      "else",
      "static",
      "enum",
      "namespace",
      };

  private static final String[] OBJECT_WORDS =
    {
      "object",
      "bool",
      "byte",
      "float",
      "uint",
      "char",
      "ulong",
      "ushort",
      "decimal",
      "int",
      "sbyte",
      "short",
      "void",
      "double",
      "long",
      "string" };
  
  private static HashSet<String> OBJECT_SET = new HashSet<String>();

  static {
    for (int i = 0; i < KEYWORDS.length; i++) {
      createHashMap(KEYWORD_SET, KEYWORDS[i]);
    }
    for (int i = 0; i < OBJECT_WORDS.length; i++) {
      OBJECT_SET.add(OBJECT_WORDS[i]);
    }
  }

  public CSharpCodeFilter() {
  }

  /**
   * @return Returns the KEYWORD_SET.
   */
  @Override
	public HashMap<String, String> getKeywordSet() {
    return KEYWORD_SET;
  }

  public String getName() {
    return "csharp";
  }

  /**
   * @return Returns the OBJECT_SET.
   */
  @Override
	public HashSet<String> getObjectSet() {
    return OBJECT_SET;
  }

}