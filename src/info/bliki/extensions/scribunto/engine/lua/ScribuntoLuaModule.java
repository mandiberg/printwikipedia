package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.extensions.scribunto.ScribuntoException;
import info.bliki.extensions.scribunto.engine.ScribuntoModule;
import info.bliki.extensions.scribunto.template.Frame;
import info.bliki.wiki.filter.ParsedPageName;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScribuntoLuaModule implements ScribuntoModule {
    private static final int SLOW_MODULE_THRESHOLD = 500;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private final ScribuntoLuaEngine engine;
    private Prototype prototype;
    private final ParsedPageName pageName;

    public ScribuntoLuaModule(ScribuntoLuaEngine engine, Prototype prototype, ParsedPageName pageName) {
        if (engine == null) throw new IllegalArgumentException("engine is null");
        if (prototype == null) throw new IllegalArgumentException("prototype is null");
        this.engine = engine;
        this.prototype = prototype;
        this.pageName = pageName;
    }

    @Override public String invoke(String functionName, Frame frame) throws ScribuntoException {
        final LuaValue function = getEngine().loadFunction(functionName, prototype, frame);

        final long execStart = System.currentTimeMillis();
        final String result = getEngine().executeFunctionChunk(function, frame);
        final long execDuration = System.currentTimeMillis() - execStart;
        logExecution(functionName, execDuration);

        return result;
    }

    @Override public String toString() {
        return "ScribuntoLuaModule{" + pageName() + '}';
    }

    protected ScribuntoLuaEngine getEngine() {
        return engine;
    }

    public ParsedPageName pageName() {
        return pageName;
    }

    private void logExecution(String functionName, long execDuration) {
        final boolean slowExecution = execDuration > SLOW_MODULE_THRESHOLD;
        if (slowExecution || logger.isDebugEnabled()) {
            final String message = String.format("execDuration(%s %s):%d ms", toString(), functionName, execDuration);
            if (slowExecution) {
                logger.warn(message);
            } else {
                logger.debug(message);
            }
        }
    }
}
