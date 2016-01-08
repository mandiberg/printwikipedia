package info.bliki.extensions.scribunto.engine;

import info.bliki.wiki.filter.ParsedPageName;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public abstract class ScribuntoEngineBase implements ScribuntoEngine {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected final IWikiModel model;

    protected ScribuntoEngineBase(IWikiModel model) {
        this.model = model;
    }

    protected ParsedPageName pageNameForModule(String moduleName) {
        return ParsedPageName.parsePageName(model,
                moduleName,
                model.getNamespace().getModule(),
                false,
                false);
    }

    protected InputStream getRawWikiContentStream(ParsedPageName pageName) throws FileNotFoundException {
        return new ByteArrayInputStream(getRawWikiContent(pageName).getBytes());
    }

    protected String getRawWikiContent(ParsedPageName pageName) throws FileNotFoundException {
        try {
            final String content =  model.getRawWikiContent(pageName, null);
            if (content == null) {
                throw new FileNotFoundException("getRawWikiContent returned null");
            }
            return content;
        } catch (WikiModelContentException ignored) {
        }
        throw new FileNotFoundException("could not find module " + pageName);
    }
}
