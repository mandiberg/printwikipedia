package info.bliki.wiki.template.extension;

import info.bliki.wiki.model.IContext;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.template.AbstractTemplateFunction;
import info.bliki.wiki.template.ITemplateFunction;

import java.io.IOException;
import java.util.List;

/**
 * A template parser function for Java attributes. You can use the following
 * syntax: <code>{{#$:object|formatstring|separator|null-value}}</code>.
 *
 * Example:
 *
 * <pre>
 * public void testRendererForST() throws Exception {
 *     wikiModel.setAttribute(&quot;created&quot;, new GregorianCalendar(2005, 07 - 1, 05));
 *     wikiModel.registerRenderer(GregorianCalendar.class, wikiModel.new DateRenderer());
 *     String expecting = &quot;date: 2005.07.05&quot;;
 *     assertEquals(expecting, wikiModel.parseTemplates(&quot;date: {{#$:created}}&quot;));
 * }
 *
 * public class DateRenderer implements AttributeRenderer {
 *     public String toString(Object o) {
 *         SimpleDateFormat f = new SimpleDateFormat(&quot;yyyy.MM.dd&quot;);
 *         return f.format(((Calendar) o).getTime());
 *     }
 *
 *     public String toString(Object o, String formatString) {
 *         return toString(o);
 *     }
 * }
 *</pre>
 */
public class DollarContext extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new DollarContext();

    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) throws IOException {
        if (model instanceof IContext) {
            IContext context = (IContext) model;
            if (list.size() > 0) {
                // name of the attribute
                String attribute = parseTrim(list.get(0), model);
                if (attribute.length() > 0) {
                    String formatString = null;
                    String separatorString = null;
                    String nullvalueString = null;
                    if (list.size() > 1) {
                        // format string of the attribute
                        formatString = parseTrim(list.get(1), model);
                    }
                    if (list.size() > 2) {
                        separatorString = parseTrim(list.get(2), model);
                        if (separatorString.length() > 1) {
                            int begin = 0;
                            int end = separatorString.length();
                            if (separatorString.charAt(0) == '\'') {
                                begin = 1;
                                if (separatorString.charAt(end - 1) == '\'') {
                                    end--;
                                } else {
                                    begin = 0;
                                }
                            }
                            separatorString = separatorString.substring(begin, end);
                        }
                    }
                    if (list.size() > 3) {
                        nullvalueString = parseTrim(list.get(3), model);
                    }
                    // get the assigned value of the attribute
                    Object value = context.getAttribute(attribute);
                    if (value != null) {
                        StringBuilder builder;
                        if (value instanceof AttributeList && ((AttributeList) value).size() > 0) {
                            // multiple attribute values assigned
                            AttributeList attrList = (AttributeList) value;
                            builder = new StringBuilder(attrList.size() * 16);
                            builder.append(toString(context, attrList.get(0), formatString, nullvalueString));
                            for (int i = 1; i < attrList.size(); i++) {
                                if (separatorString != null) {
                                    builder.append(separatorString);
                                }
                                builder.append(toString(context, attrList.get(i), formatString, nullvalueString));
                            }
                            return builder.toString();

                        } else {
                            builder = new StringBuilder(16);
                            // single attribute values assigned
                            builder.append(toString(context, value, formatString, nullvalueString));
                            return builder.toString();
                        }
                    }
                }
            }
        }
        return null;
    }

    private String toString(IContext context, Object value, String formatString, String nullvalueString) {
        AttributeRenderer renderer;
        if (value == null) {
            renderer = context.getAttributeRenderer(String.class);
            value = nullvalueString;
        } else {
            renderer = context.getAttributeRenderer(value.getClass());
        }
        if (renderer != null) {
            return renderer.toString(value, formatString);
        } else {
            return value.toString();
        }
    }

}
