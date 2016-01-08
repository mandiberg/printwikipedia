package info.bliki.extensions.scribunto.engine.lua.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class MwText implements MwInterface {
    @Override
    public String name() {
        return "mw.text";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable table = new LuaTable();

        table.set("unstrip", unstrip());
        table.set("unstripNoWiki", unstripNoWiki());
        table.set("killMarkers", killMarkers());
        table.set("getEntityTable", getEntityTable());
        table.set("jsonEncode", jsonEncode());
        table.set("jsonDecode", jsonDecode());
        return table;
    }

    private LuaValue unstripNoWiki() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return NIL;
            }
        };
    }

    private LuaValue killMarkers() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return NIL;
            }
        };
    }

    // includes/json/FormatJson.php, mostly wrapper around PHP's json_encode
    private LuaValue jsonEncode() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue value, LuaValue flags) {
                return null;
            }
        };
    }

    // includes/json/FormatJson.php
    private LuaValue jsonDecode() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue json, LuaValue flags) {
                return null;
            }
        };
    }

    private LuaValue getEntityTable() {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return NIL;
            }
        };
    }

    private LuaValue unstrip() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return NIL;
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        LuaTable table = new LuaTable();
        table.set("nowiki_protocols", new LuaTable());
        table.set("comma", ", ");
        table.set("and", " and ");
        table.set("ellipsis", "...");
        return table;
    }
}
