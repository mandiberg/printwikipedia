package info.bliki.extensions.scribunto.engine.lua.interfaces;

import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.namespaces.Namespace;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import static info.bliki.wiki.namespaces.INamespace.INamespaceValue;
import static org.luaj.vm2.LuaValue.NIL;

public class MwSite implements MwInterface {
    private final IWikiModel wikiModel;

    public MwSite(IWikiModel wikiModel) {
        assert(wikiModel != null);
        this.wikiModel = wikiModel;
    }

    @Override
    public String name() {
        return "mw.site";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable table = new LuaTable();
        table.set("getNsIndex", getNsIndex());
        table.set("pagesInCategory", pagesInCategory());
        table.set("pagesInNamespace", pagesInNamespace());
        table.set("usersInGroup", usersInGroup());
        table.set("interwikiMap", interwikiMap());
        return table;
    }

    private LuaValue interwikiMap() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue) {
                if (luaValue.isnil() || "string".equals(luaValue.typename())) {
                    return new LuaTable();
                } else {
                    throw new LuaError("bad argument #1 to 'interwikiMap' (string expected, got " + luaValue.typename() + ")");
                }
            }
        };
    }

    private LuaValue usersInGroup() {
        return new OneArgFunction() {
            @Override public LuaValue call(LuaValue group) {
                return LuaValue.valueOf(0);
            }
        };
    }

    private LuaValue pagesInNamespace() {
        return new OneArgFunction() {
            @Override public LuaValue call(LuaValue ns) {
                return LuaValue.valueOf(0);
            }
        };
    }

    private LuaValue pagesInCategory() {
        return new TwoArgFunction() {
            @Override public LuaValue call(LuaValue category, LuaValue which) {
                return LuaValue.valueOf(0);
            }
        };
    }

    private LuaValue getNsIndex() {
        return new OneArgFunction() {
            @Override
            /**
             * Get a namespace key by value, case insensitive.  Canonical namespace
             * names override custom ones defined for the current language.
             *
             * @param name String
             * @return mixed An integer if $text is a valid value otherwise false
             */
            public LuaValue call(LuaValue name) {
                INamespaceValue ns = wikiModel.getNamespace().getNamespace(name.tojstring());
                if (ns != null) {
                    return LuaValue.valueOf(ns.getCode().code);
                } else {
                    return FALSE;
                }
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        LuaTable table = new LuaTable();
        table.set("siteName", "test");      // $GLOBALS['wgSitename'],
        table.set("server", "server");      // $GLOBALS['wgServer'],
        table.set("scriptPath", "");        // $GLOBALS['wgScriptPath'],
        table.set("stylePath",  "");        // $GLOBALS['wgStylePath'],
        table.set("currentVersion", "1.0"); // SpecialVersion::getVersion(),
        table.set("stats", stats());
        table.set("namespaces", namespaces());
        return table;
    }

    private LuaTable stats() {
        LuaTable stats = new LuaTable();

        stats.set("pages", 0);        // (int)SiteStats::pages(),
        stats.set("articles", 0);     // (int)SiteStats::articles(),
        stats.set("files", 0);        // (int)SiteStats::images(),
        stats.set("edits", 0);        // (int)SiteStats::edits(),
        stats.set("views", NIL);      // $wgDisableCounters ? null : (int)SiteStats::views(),
        stats.set("users", 0);        // (int)SiteStats::users(),
        stats.set("activeUsers", 0);  // (int)SiteStats::activeUsers(),
        stats.set("admins", 0);       // (int)SiteStats::numberingroup( 'sysop' ),
        return stats;
    }

    private LuaTable namespaces() {
        LuaTable table = new LuaTable();
        for (INamespace.NamespaceCode code : INamespace.NamespaceCode.values()) {
            INamespaceValue namespaceValue = wikiModel.getNamespace().getNamespaceByNumber(code);
            table.set(code.code, luaDataForNamespace(namespaceValue));
        }
        return table;
    }

    private LuaTable luaDataForNamespace(INamespaceValue namespaceValue) {
        LuaTable ns = new LuaTable();
        ns.set("id", namespaceValue.getCode().code);
        ns.set("name", namespaceValue.getPrimaryText().replace('_', ' '));
        ns.set("canonicalName", namespaceValue.getCanonicalName().replace('_', ' '));
        ns.set("hasSubpages", LuaValue.valueOf(namespaceValue.hasSubpages()));
        ns.set("hasGenderDistinction", LuaValue.valueOf(namespaceValue.hasGenderDistinction()));
        ns.set("isCapitalized", LuaValue.valueOf(namespaceValue.isCapitalized()));
        ns.set("isContent", LuaValue.valueOf(namespaceValue.isContent()));
        ns.set("isIncludable", LuaValue.valueOf(namespaceValue.isIncludable()));
        ns.set("isMovable", LuaValue.valueOf(namespaceValue.isMovable()));
        ns.set("isSubject", LuaValue.valueOf(namespaceValue.isSubject()));
        ns.set("isTalk", LuaValue.valueOf(namespaceValue.isTalk()));
        ns.set("defaultContentModel", NIL);

        LuaValue[] aliases = new LuaValue[namespaceValue.getTexts().size()-1];
        for (int i=0; i<namespaceValue.getTexts().size()-1; i++) {
            aliases[i] = LuaValue.valueOf(namespaceValue.getTexts().get(i+1));
        }
        ns.set("aliases", LuaValue.listOf(aliases));

        if (namespaceValue.getCode().code >= INamespace.NamespaceCode.MAIN_NAMESPACE_KEY.code) {
            if (namespaceValue.getContentspace() != null) {
                ns.set("subject", namespaceValue.getContentspace().getCode().code);
            }
            ns.set("talk", namespaceValue.getTalkspace().getCode().code);
            Namespace.NamespaceValue associated = namespaceValue.getAssociatedspace();
            if (associated != null) {
                ns.set("associated", associated.getCode().code);
            } else {
                ns.set("associated", NIL);
            }
        } else {
            ns.set("subject", namespaceValue.getCode().code);
        }
        return ns;
    }
}
