package info.bliki.extensions.scribunto.engine.lua.interfaces;

import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.template.dates.StringToTime;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.Date;

import static info.bliki.extensions.scribunto.engine.lua.interfaces.MwInterface.DefaultFunction.defaultFunction;
import static java.util.Locale.forLanguageTag;

public class MwLanguage implements MwInterface {
    private Languages languages = new Languages();
    private final IWikiModel wikiModel;

    public MwLanguage(IWikiModel wikiModel) {
        this.wikiModel = wikiModel;
    }

    @Override
    public String name() {
        return "mw.language";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable table = new LuaTable();
        table.set("isSupportedLanguage", defaultFunction());
        table.set("isKnownLanguageTag", isKnownLanguageTag());
        table.set("isValidCode", defaultFunction());
        table.set("isValidBuiltInCode", defaultFunction());
        table.set("fetchLanguageName", fetchLanguageName());
        table.set("fetchLanguageNames", fetchLanguageNames());
        table.set("getFallbacksFor", defaultFunction());
        table.set("getContLangCode", getContLangCode());
        table.set("formatDate", formatDate());
        table.set("lc", lc());
        table.set("uc", uc());
        /*
        // TODO
        lcfirst
        ucfirst
        caseFold
        formatNum
        formatDate
        formatDuration
        getDurationIntervals
        convertPlural
        convertGrammar
        gender
        */
        return table;
    }

    private LuaValue formatDate() {
        return new ThreeArgFunction() {
            @Override public LuaValue call(LuaValue lang, LuaValue format, LuaValue date) {
                // http://php.net/manual/en/function.date.php
                final String formatString = format.checkjstring();
                final String dateString = date.checkjstring();
                final Date time = dateString.isEmpty() ? wikiModel.getCurrentTimeStamp() : (Date) StringToTime.date(dateString);

                if ("U".equals(formatString)) {
                    // seconds since epoch
                    return LuaValue.valueOf(String.valueOf(time.getTime() / 1000));
                }
                throw new UnsupportedOperationException();
            }
        };
    }

    private LuaValue lc() {
        return new TwoArgFunction() {
            @Override public LuaValue call(LuaValue code, LuaValue string) {
                return valueOf(string.checkjstring().toLowerCase(forLanguageTag(code.checkjstring())));
            }
        };
    }

    private LuaValue uc() {
        return new TwoArgFunction() {
            @Override public LuaValue call(LuaValue code, LuaValue string) {
                return valueOf(string.checkjstring().toUpperCase(forLanguageTag(code.checkjstring())));
            }
        };
    }

    public LuaValue isKnownLanguageTag() {
        return new OneArgFunction() {
            @Override public LuaValue call(LuaValue code) {
                return LuaValue.TRUE;
            }
        };
    }

    private LuaValue fetchLanguageNames() {
        return new TwoArgFunction() {
            /**
             * Get an array of language names, indexed by code.
             *
             * @param inLanguage null|string: Code of language in which to return the names
             *                   Use null for autonyms (native names)
             * @param include    string:
             *                   'all' all available languages
             *                   'mw' only if the language is defined in MediaWiki or wgExtraLanguageNames (default)
             *                   'mwfile' only if the language is in 'mw' *and* has a message file
             * @return array: language code => language name
             */
            @Override public LuaValue call(LuaValue inLanguage, LuaValue include) {
                return new LuaTable();
            }
        };
    }

    private LuaValue fetchLanguageName() {
        return new TwoArgFunction() {
            @Override public LuaValue call(LuaValue code, LuaValue inLanguage) {
                final String name = languages.getName(code.checkjstring(), inLanguage.optjstring(null));
                return name == null ? LuaValue.NIL : valueOf(name);
            }
        };
    }

    private LuaValue getContLangCode() {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return valueOf("en");
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        return null;
    }
}
