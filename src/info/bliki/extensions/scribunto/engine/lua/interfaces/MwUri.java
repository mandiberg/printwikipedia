package info.bliki.extensions.scribunto.engine.lua.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class MwUri implements MwInterface {
    private String wgServer;
    private String wgCanonicalServer;
    private boolean wgUsePathInfo;
    private String wgScript;
    private String wgScriptPath;
    private String wgArticlePath;

    public MwUri() {
        wgServer = "//wiki.local";
        wgCanonicalServer = "http://wiki.local";
        wgUsePathInfo = true;
        wgScript = "/w/index.php";
        wgScriptPath = "/w";
        wgArticlePath = "/wiki/$1";
    }

    @Override
    public String name() {
        return "mw.uri";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable iface = new LuaTable();
        iface.set("anchorEncode", anchorEncode());
        iface.set("localUrl", localUrl());
        iface.set("fullUrl", fullUrl());
        iface.set("canonicalUrl", canonicalUrl());
        return iface;
    }

    private LuaValue anchorEncode() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.EMPTYSTRING;
            }
        };
    }

    private LuaValue canonicalUrl() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue page, LuaValue query) {
                if (query.isnil()) {
                    return LuaValue.valueOf(wgCanonicalServer + pagePath(page));
                } else {
                    return LuaValue.valueOf(wgCanonicalServer + formatQuery(page, query));
                }
            }
        };
    }

    private LuaValue fullUrl() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue page, LuaValue query) {
                if (query.isnil()) {
                    return LuaValue.valueOf(wgServer + pagePath(page));
                } else {
                    return LuaValue.valueOf(wgServer + formatQuery(page, query));
                }
            }
        };
    }

    private LuaValue localUrl() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue page, LuaValue query) {
                if (query.isnil()) {
                    return LuaValue.valueOf(pagePath(page));
                } else {
                    return LuaValue.valueOf(formatQuery(page, query));
                }
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        LuaTable options = new LuaTable();
        return options;
    }

    private String pagePath(LuaValue page) {
        return wgArticlePath.replace("$1", page.tojstring());
    }

    private String formatQuery(LuaValue page, LuaValue query) {
        if (query.isstring()) {
            return wgScript + "?title="+page.checkstring()+"&"+query.checkjstring();
        } else if (query.istable()) {
            LuaTable params = query.checktable();

            String base = wgScript + "?title="+page.checkstring()+"&";
            for (LuaValue key : params.keys()) {
                base += (key.tojstring() + "=" + params.get(key).tojstring());
            }
            return base;
        } else {
            throw new AssertionError("unexpected type: "+query);
        }
    }
}
