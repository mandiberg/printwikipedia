package info.bliki.extensions.scribunto.template;

import info.bliki.wiki.filter.ParsedPageName;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;

public class Frame {
    private final ParsedPageName page;
    private final Map<String, String> templateParameters;
    private final Frame parent;
    private boolean isSubst;

    public Frame(ParsedPageName page, Map<String, String> templateParameters, Frame parent, boolean isSubst) {
        this.templateParameters = templateParameters;
        this.page = page;
        this.parent = parent;
        this.isSubst = isSubst;
    }

    public Frame newChild(ParsedPageName pageName, Map<String, String> templateParameters, boolean isSubst) {
        return new Frame(pageName, templateParameters, this, isSubst);
    }

    public LuaValue getArgument(String name) {
        String value = templateParameters != null ? templateParameters.get(name) : null;
        if (value != null) {
            return LuaValue.valueOf(value);
        } else {
            return LuaValue.NIL;
        }
    }

    public Map<String, String> getTemplateParameters() {
        return new HashMap<>(templateParameters);
    }

    public LuaValue getAllArguments() {
        LuaTable table = new LuaTable();
        for (Map.Entry<String, String> entry: templateParameters.entrySet()) {
            try {
                final int numberedParam = Integer.parseInt(entry.getKey());
                table.set(LuaValue.valueOf(numberedParam), LuaValue.valueOf(entry.getValue()));
            } catch (NumberFormatException e) {
                table.set(LuaValue.valueOf(entry.getKey()), LuaValue.valueOf(entry.getValue()));
            }
        }
        return table;
    }

    public Frame getParent() {
        return parent;
    }

    public String getTitle() {
        return page.pagename;
    }

    public boolean isSubsting() {
        return isSubst;
    }
}
