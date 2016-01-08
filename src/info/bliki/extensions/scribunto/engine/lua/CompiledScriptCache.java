package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.wiki.filter.ParsedPageName;
import org.luaj.vm2.Prototype;

import java.util.HashMap;
import java.util.Map;

public class CompiledScriptCache {
    private Map<ParsedPageName, Prototype> compileCache = new HashMap<>();

    public static final CompiledScriptCache DONT_CACHE = new CompiledScriptCache() {
        @Override public Prototype getPrototypeForChunkname(ParsedPageName chunkname) {
            return null;
        }
    };

    public Prototype getPrototypeForChunkname(ParsedPageName chunkname) {
        return compileCache.get(chunkname);
    }

    public void cachePrototype(ParsedPageName chunkName, Prototype prototype) {
        compileCache.put(chunkName, prototype);
    }
}
