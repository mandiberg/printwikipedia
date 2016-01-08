package info.bliki.extensions.scribunto.engine.lua.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class MwUstring implements MwInterface {
    @Override
    public String name() {
        return "mw.ustring";
    }

    @Override
    public LuaTable getInterface() {
        return new LuaTable();
    }

    @Override
    public LuaValue getSetupOptions() {
        LuaTable table = new LuaTable();
        // https://www.mediawiki.org/wiki/Manual:$wgMaxArticleSize
        // stringLengthLimit = $wgMaxArticleSize * 1024;
        table.set("stringLengthLimit",  2048 * 1024);
        table.set("patternLengthLimit", 10000);
        return table;
    }
}
