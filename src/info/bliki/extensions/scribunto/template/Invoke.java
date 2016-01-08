package info.bliki.extensions.scribunto.template;

import info.bliki.extensions.scribunto.ScribuntoException;
import info.bliki.extensions.scribunto.engine.ScribuntoEngine;
import info.bliki.extensions.scribunto.engine.ScribuntoModule;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.template.AbstractTemplateFunction;
import info.bliki.wiki.template.ITemplateFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static info.bliki.wiki.filter.TemplateParser.createSingleParameter;
import static info.bliki.wiki.filter.TemplateParser.mergeParameters;

/**
 * A template parser function for <code>{{ #invoke: ... }}</code> syntax.
 *
 * scribunto/common/Hooks.php
 */
public class Invoke extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Invoke();
    private static Logger logger = LoggerFactory.getLogger(Invoke.class);

    @Override
    public String parseFunction(List<String> parts, IWikiModel model,
                                char[] src, int beginIndex, int endIndex, boolean isSubst) throws IOException {
        if (parts.size() < 2) {
            throw new AssertionError("not enough arguments");
        }

        ScribuntoEngine engine = model.createScribuntoEngine();

        if (engine == null) {
            throw new AssertionError("no scribuntoEngine defined");
        }

        final String moduleName   = parts.get(0).trim();  // TODO trim( $frame->expand( $args[0] ) );
        final String functionName = parts.get(1).trim();  // TODO trim( $frame->expand( $args[1] ) );

        Frame parent = model.getFrame();
        try {
            ScribuntoModule module = engine.fetchModuleFromParser(moduleName);
            final Frame frame = parent.newChild(module.pageName(), getParameters(parts, model), isSubst);

            return module.invoke(functionName, frame);
        } catch (ScribuntoException e) {
            // TODO handle
            logger.error("error invoking function", e);
            return null;
        } finally {
            model.setFrame(parent);
        }
    }

    private Map<String, String> getParameters(List<String> parts, IWikiModel model) {
        LinkedHashMap<String, String> parameterMap = new LinkedHashMap<>();
        if (parts.size() > 2) {
            List<String> unnamedParameters = new ArrayList<>();
            for (int i = 2; i < parts.size(); i++) {
                createSingleParameter(parts.get(i), model, parameterMap, unnamedParameters);
            }
            mergeParameters(parameterMap, unnamedParameters);
        }
        return parameterMap;
    }

    @Override
    public String getFunctionDoc() {
        return null;
    }
}
