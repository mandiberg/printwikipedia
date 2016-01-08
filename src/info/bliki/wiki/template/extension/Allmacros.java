package info.bliki.wiki.template.extension;

import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.template.AbstractTemplateFunction;
import info.bliki.wiki.template.ITemplateFunction;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A template parser function for <code>{{ #all_macros: ... }}</code> syntax.
 * The function lists all available template functions documentation. To show
 * the documentation of a function, the ITemplateFunction#getFunctionDoc() must
 * be implemented.
 *
 * This function isn't included in the <i>default parser functions map</i>. Use
 * Configuration.addTemplateFunction(&quot;#all_macros&quot;, Allmacros.CONST)
 * to add the template to your configuration.
 */
public class Allmacros extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Allmacros();

    public Allmacros() {

    }

    @Override
    public String getFunctionDoc() {
        return "Returns all macros";
    }

    @Override
    public String parseFunction(List<String> parts, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) throws IOException {
        Map<String, ITemplateFunction> t = model.getTemplateMap();
        StringBuilder sb = new StringBuilder(16);
        String doc;
        sb.append("<table border=\"1\"><th>Template name</th><th>Implemented in class</th><th>Description</th>");
        for (Map.Entry<String, ITemplateFunction> e : t.entrySet()) {
            sb.append("<tr><td>");
            sb.append(e.getKey());
            sb.append("</td><td>");
            ITemplateFunction tf = e.getValue();
            Class<?> cl1 = tf.getClass().getEnclosingClass();
            if (cl1 == null)
                cl1 = tf.getClass();
            String s1 = cl1.getCanonicalName();
            sb.append(s1);
            sb.append("</td><td>");
            doc = tf.getFunctionDoc();
            if (doc == null) {
                sb.append("No documentation available");
            } else {
                sb.append(doc);
            }
            sb.append("</td></tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
}
