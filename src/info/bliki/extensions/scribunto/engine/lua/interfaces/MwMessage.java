package info.bliki.extensions.scribunto.engine.lua.interfaces;

import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.text.NumberFormat;
import java.util.Locale;

public class MwMessage implements MwInterface {
    @Override
    public String name() {
        return "mw.message";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable iface = new LuaTable();
        iface.set("plain", messagePlain());
        iface.set("check", messageCheck());
        return iface;
    }

    private LuaValue messageCheck() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue what, LuaValue data) {
                return FALSE;
            }
        };
    }

    private LuaValue messagePlain() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue data) {
                return new Message(data).plain();
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        LuaTable options = new LuaTable();

        options.set("lang", "en");
        return options;
    }

    private static class Message {
        final boolean useDB;
        final String lang;
        final LuaTable params;
        final LuaTable keys;
        final LuaString rawMessage;

        Message(LuaValue data) {
            useDB  = data.get("useDB").optboolean(false);
            lang   = data.get("lang").optjstring("en");
            keys   = data.get("keys").opttable(new LuaTable());
            params = data.get("params").opttable(new LuaTable());
            rawMessage = data.get("rawMessage").optstring(LuaString.valueOf(""));
        }

        public LuaValue plain() {
            String msg = rawMessage.tojstring();
            return LuaValue.valueOf(replace(msg, params));
        }

        private String replace(String msg, LuaTable params) {
            for (int i=1; i<params.length()+1; i++) {
                LuaValue param = params.get(i);
                String actualParam;
                if (param.istable()) {

                    if (!param.get("raw").isnil()) {
                        actualParam = param.get("raw").checkjstring();
                    } else if (!param.get("num").isnil()) {
                        if (param.get("num").isnumber()) {
                            LuaNumber number = param.get("num").checknumber();
                            NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag(lang));
                            actualParam = nf.format(number.todouble());
                        } else {
                            actualParam = param.get("num").tojstring();
                        }
                    } else {
                        actualParam = "unknown";
                    }
                } else {
                    actualParam = param.checkjstring();
                }
                msg = msg.replace("$"+i, actualParam);
            }
            return msg;
        }
    }
}
