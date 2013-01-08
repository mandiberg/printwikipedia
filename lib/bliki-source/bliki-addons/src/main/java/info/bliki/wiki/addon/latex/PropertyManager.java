/* Copyright (C) 2006 i3G Institut - Hochschule Heilbronn

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation; either version 2.1 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package info.bliki.wiki.addon.latex;

import info.bliki.wiki.model.WikiModel;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Manages access to the property file. All properties can be prefixed, this
 * must be set in the init() method. Note that this PropertyManager is not
 * limited to access the latex.properties file.
 * 
 * @author Steffen Schramm
 * 
 */ 
public class PropertyManager {

	/** The prefix of all properties */
	private static String prefix = "";

	/** The Properties object */
	private static Properties props = null;

	/** Returns the property for the given key. */
	public static String get(String key) {
		if (props == null) {
			throw new RuntimeException("PropertyManager not initialized");
		}
		return props.getProperty(prefix + key);
	}

	/** Convenience method. Returns a property formatted with the given arg */
	public static String get(String key, Object arg) {
		Object[] args = new Object[] { arg };
		return get(key, args);
	}

	/** Convenience method. Returns a property formatted with the given args */
	public static String get(String key, Object arg1, Object arg2) {
		Object[] args = new Object[] { arg1, arg2 };
		return get(key, args);
	}

	/** Convenience method. Returns a property formatted with the given args */
	public static String get(String key, Object arg1, Object arg2, Object arg3) {
		Object[] args = new Object[] { arg1, arg2, arg3 };
		return get(key, args);
	}

	/** Convenience method. Returns a property formatted with the given args */
	public static String get(String key, Object[] args) {
		String str = null;
		String value = get(key);
		try {
			str = MessageFormat.format(value, args);
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
		return str;
	}

	public static Properties getProperties() {
		return props;
	}

	private static final String RESOURCE_NAME = "latex.properties";

	/** Loads the properties file */
	static {
		try {
			// initialize through input stream:
//			Class config_class = PropertyManager.class;
			final InputStream is = PropertyManager.class.getResourceAsStream(RESOURCE_NAME);
			props = new Properties();
			props.load(is);
			prefix = "latex.";

			// System.out.println("};");
			// props = IOHelper.loadProperties(engine.getServletContext(),
			// propertyFile);
			// } catch (ClassNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) {
	// init();
	// }
	/** Loads the properties file and sets the prefix */
	// public static void init(WikiEngine engine, String propertyFile,
	// String prefix) {
	// init(engine, propertyFile);
	// PropertyManager.prefix = prefix;
	// }
}
