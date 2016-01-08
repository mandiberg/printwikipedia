package info.bliki.extensions.scribunto.engine;

import info.bliki.extensions.scribunto.ScribuntoException;
import info.bliki.extensions.scribunto.template.Frame;
import info.bliki.wiki.filter.ParsedPageName;

public interface ScribuntoModule {
    /**
     * Invoke the function with the specified name.
     *
     * @return string
     */
    String invoke(String functionName, Frame frame) throws ScribuntoException;

    ParsedPageName pageName();
}
