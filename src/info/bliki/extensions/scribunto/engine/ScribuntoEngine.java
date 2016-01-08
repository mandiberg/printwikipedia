package info.bliki.extensions.scribunto.engine;

import info.bliki.extensions.scribunto.ScribuntoException;

public interface ScribuntoEngine {
    /**
     * Load a module from some parser-defined template loading mechanism and
     * register a parser output dependency.
     *
     * Does not initialize the module, i.e. do not expect it to complain if the module
     * text is garbage or has syntax error.
     *
     * @param moduleName The name of the module
     * @return the module
     * @throws ScribuntoException if the module could not be loaded
     */
    ScribuntoModule fetchModuleFromParser(String moduleName) throws ScribuntoException;
}
