package info.bliki.extensions.scribunto.engine.lua.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public interface MwInterface {

    /**
     * @return the name of this interface
     */
    String name();

    /**
     * @return a table with all methods exposed
     */
    LuaTable getInterface();


    /**
     * @return options needed for setup
     */
    LuaValue getSetupOptions();

    class DefaultFunction {
        public static OneArgFunction defaultFunction() {
            return defaultFunction(null);
        }

        public static OneArgFunction defaultFunction(final String argName) {
            return new OneArgFunction() {
                @Override
                public LuaValue call(LuaValue arg) {
                    // logger.warn("defaultFunction " + argName + " called");
                    return LuaValue.NIL;
                }
            };
        }
    }
}
