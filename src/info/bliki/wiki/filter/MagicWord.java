/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, version 2.1, dated February 1999.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the latest version of the GNU Lesser General
 * Public License as published by the Free Software Foundation;
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (LICENSE.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package info.bliki.wiki.filter;

import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.namespaces.INamespace.INamespaceValue;
import info.bliki.wiki.template.Titleparts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * See <a href="https://www.mediawiki.org/wiki/Help:Magic_words">Help:Magic
 * words</a> for a list of Mediawiki magic words.
 */
public class MagicWord {

    /**
     * Type of storing user contributions in the DB.
     */
    public enum MagicWordE {

        // current date values
        MAGIC_CURRENT_DAY("CURRENTDAY"),
        MAGIC_CURRENT_DAY2("CURRENTDAY2"),
        MAGIC_CURRENT_DAY_NAME("CURRENTDAYNAME"),
        MAGIC_CURRENT_DAY_OF_WEEK("CURRENTDOW"),
        MAGIC_CURRENT_MONTH("CURRENTMONTH"),
        MAGIC_CURRENT_MONTH_ABBR("CURRENTMONTHABBREV"),
        MAGIC_CURRENT_MONTH_NAME("CURRENTMONTHNAME"),
        MAGIC_CURRENT_TIME("CURRENTTIME"),
        MAGIC_CURRENT_HOUR("CURRENTHOUR"),
        MAGIC_CURRENT_WEEK("CURRENTWEEK"),
        MAGIC_CURRENT_YEAR("CURRENTYEAR"),
        MAGIC_CURRENT_TIMESTAMP("CURRENTTIMESTAMP"),

        // local date values
        MAGIC_LOCAL_DAY("LOCALDAY"),
        MAGIC_LOCAL_DAY2("LOCALDAY2"),
        MAGIC_LOCAL_DAY_NAME("LOCALDAYNAME"),
        MAGIC_LOCAL_DAY_OF_WEEK("LOCALDOW"),
        MAGIC_LOCAL_MONTH("LOCALMONTH"),
        MAGIC_LOCAL_MONTH_ABBR("LOCALMONTHABBREV"),
        MAGIC_LOCAL_MONTH_NAME("LOCALMONTHNAME"),
        MAGIC_LOCAL_TIME("LOCALTIME"),
        MAGIC_LOCAL_HOUR("LOCALHOUR"),
        MAGIC_LOCAL_WEEK("LOCALWEEK"),
        MAGIC_LOCAL_YEAR("LOCALYEAR"),
        MAGIC_LOCAL_TIMESTAMP("LOCALTIMESTAMP"),

        // statistics
        MAGIC_CURRENT_VERSION("CURRENTVERSION"),
        MAGIC_NUMBER_ARTICLES("NUMBEROFARTICLES"),
        MAGIC_NUMBER_PAGES("NUMBEROFPAGES"),
        MAGIC_NUMBER_FILES("NUMBEROFFILES"),
        MAGIC_NUMBER_USERS("NUMBEROFUSERS"),
        MAGIC_NUMBER_ADMINS("NUMBEROFADMINS"),
        MAGIC_PAGES_IN_CATEGORY("PAGESINCATEGORY"),
        MAGIC_PAGES_IN_CAT("PAGESINCAT"),
        MAGIC_PAGES_IN_NAMESPACE("PAGESINNAMESPACE"),
        MAGIC_PAGES_IN_NAMESPACE_NS("PAGESINNS"),
        MAGIC_PAGE_SIZE("PAGESIZE"),

        // page values
        MAGIC_PAGE_NAME("PAGENAME"),
        MAGIC_PAGE_NAME_E("PAGENAMEE"),
        MAGIC_SUB_PAGE_NAME("SUBPAGENAME"),
        MAGIC_SUB_PAGE_NAME_E("SUBPAGENAMEE"),
        MAGIC_BASE_PAGE_NAME("BASEPAGENAME"),
        MAGIC_BASE_PAGE_NAME_E("BASEPAGENAMEE"),
        MAGIC_NAMESPACE("NAMESPACE"),
        MAGIC_NAMESPACE_E("NAMESPACEE"),
        MAGIC_NAMESPACENUMBER("NAMESPACENUMBER"),
        MAGIC_FULL_PAGE_NAME("FULLPAGENAME"),
        MAGIC_FULL_PAGE_NAME_E("FULLPAGENAMEE"),

        // A protocol-relative path to the title. This will also resolve interwiki prefixes.
        // {{fullurl:Category:Top level}} -> //www.mediawiki.org/wiki/Category:Top_level
        // {{fullurl:Category:Top level|action=edit}} -> //www.mediawiki.org/w/index.php?title=Category:Top_level&action=edit
        MAGIC_FULL_URL("FULLURL"),
        MAGIC_FULL_URL_E("FULLURLE"),

        MAGIC_TALK_SPACE("TALKSPACE"),
        MAGIC_TALK_SPACE_E("TALKSPACEE"),
        MAGIC_SUBJECT_SPACE("SUBJECTSPACE"),
        MAGIC_SUBJECT_SPACE_E("SUBJECTSPACEE"),
        MAGIC_ARTICLE_SPACE("ARTICLESPACE"),
        MAGIC_ARTICLE_SPACE_E("ARTICLESPACEE"),
        MAGIC_TALK_PAGE_NAME("TALKPAGENAME"),
        MAGIC_TALK_PAGE_NAME_E("TALKPAGENAMEE"),
        MAGIC_SUBJECT_PAGE_NAME("SUBJECTPAGENAME"),
        MAGIC_SUBJECT_PAGE_NAME_E("SUBJECTPAGENAMEE"),
        MAGIC_ARTICLE_PAGE_NAME("ARTICLEPAGENAME"),
        MAGIC_ARTICLE_PAGE_NAME_E("ARTICLEPAGENAMEE"),

        // revision data
        MAGIC_REVISION_ID("REVISIONID"),
        /** Day edit was made (unpadded number). (1-31) */
        MAGIC_REVISION_DAY("REVISIONDAY"),
        /** Day edit was made (zero-padded number) (01-31) */
        MAGIC_REVISION_DAY2("REVISIONDAY2"),
        /** Month edit was made (zero-padded number as of 1.17+, unpadded number in prior versions). */
        MAGIC_REVISION_MONTH("REVISIONMONTH"),
        /** Month edit was made (unpadded number).  */
        MAGIC_REVISION_MONTH1("REVISIONMONTH1"),
        /** Year edit was made: 2013 */
        MAGIC_REVISION_YEAR("REVISIONYEAR"),
        /** Timestamp of the last edit made: 20130314092713 */
        MAGIC_REVISION_TIMESTAMP("REVISIONTIMESTAMP"),
        /** The username of the user who made the most recent edit to the page, or the current user when previewing an edit. */
        MAGIC_REVISION_USER("REVISIONUSER"),

        MAGIC_PROTECTION_LEVEL("PROTECTIONLEVEL"),
        MAGIC_DISPLAY_TITLE("DISPLAYTITLE"),
        MAGIC_DEFAULT_SORT("DEFAULTSORT"),
        MAGIC_DEFAULT_SORT_KEY("DEFAULTSORTKEY"),
        MAGIC_DEFAULT_CATEGORY_SORT("DEFAULTCATEGORYSORT"),
        MAGIC_SITE_NAME("SITENAME"),
        MAGIC_SERVER("SERVER"),
        MAGIC_SCRIPT_PATH("SCRIPTPATH"),
        MAGIC_SERVER_NAME("SERVERNAME"),
        MAGIC_STYLE_PATH("STYLEPATH"),
        MAGIC_CONTENT_LANGUAGE("CONTENTLANGUAGE"),
        MAGIC_CONTENT_LANG("CONTENTLANG"),

        /** Used to include a pipe character as part of a template argument or table cell contents. */
        MAGIC_BANG("!");

        private final String text;

        MagicWordE(String text) {
            this.text = text;
            MAGIC_WORDS.put(text.toLowerCase(), this);
        }

        /**
         * Converts the enum to text.
         */
        @Override
        public String toString() {
            return this.text;
        }

        /**
         * Tries to convert a text to the according enum value.
         *
         * @param text the text to convert
         *
         * @return the according enum value
         */
        public static MagicWordE fromString(String text) {
            if (text == null) {
                return null;
            }
            return MAGIC_WORDS.get(text.toLowerCase());
        }
    }
    /**
     * Magic words in lower case.
     *
     * Note: MediaWiki tolerates some variations in the case but they do not
     * seem consistent, e.g.
     * <code>{{CURRENTYEAR}} {{currentyear}} {{Currentyear}}</code>
     * return the current year,
     * <code>{{CURRENTYeAR}} {{cURRENTYEAR}}</code> don't.
     * The following variations of {{SERVERNAME}} however all return the server name:
     * <code>{{SERVERNAME}} {{SERVeRNAMe}} {{sERVERNAME}} {{servername}} {{Servername}}</code>
     *
     * Therefore, tolerate any case here and use lower-case in this hashmap.
     */
    protected final static HashMap<String, MagicWordE> MAGIC_WORDS = new HashMap<>(100);

    /**
     * Determine if a template name corresponds to a magic word requiring special
     * handling. See <a
     * href="http://www.mediawiki.org/wiki/Help:Magic_words">Help:Magic words</a>
     * for a list of Mediawiki magic words.
     *
     * @param name
     *            the potential magic word
     * @return <tt>true</tt> if <tt>name</tt> was a magic word,
     *         <tt>false</tt> otherwise
     */
    public static boolean isMagicWord(String name) {
        return MAGIC_WORDS.containsKey(name.toLowerCase());
    }

    /**
     * Determine if a template name corresponds to a magic word requiring
     * special handling. See <a
     * href="http://www.mediawiki.org/wiki/Help:Magic_words">Help:Magic words</a>
     * for a list of Mediawiki magic words.
     *
     * @param name
     *            the potential magic word
     * @return if <tt>name</tt> was a magic word: the corresponding
     *         {@link MagicWordE}, otherwise <tt>null</tt>
     */
    public static MagicWordE getMagicWord(String name) {
        return MagicWordE.fromString(name);
    }

    /**
     * Process a magic word, returning the value corresponding to the magic word
     * value. See <a
     * href="http://www.mediawiki.org/wiki/Help:Magic_words">Help:Magic words</a>
     * for a list of Mediawiki magic words.
     *
     * @param magicWord
     *            the magic word to process
     * @param parameter
     *            the parameter of the magic word (may be <tt>null</tt> if no parameter supplied)
     * @param model
     *            the wiki model to use while rendering
     *
     * @return the processed magic word content or its name if unprocessed
     */
    public static String processMagicWord(MagicWordE magicWord, String parameter, IWikiModel model) {
        assert(magicWord != null);
        SimpleDateFormat formatter = model.getSimpleDateFormat();
        // TODO: assume this is non-null!
        Date current = model.getCurrentTimeStamp();
        if (current == null) {
            // set a default value
            current = new Date(System.currentTimeMillis());
        }
        final Date revision = current;
        // local date values
        switch (magicWord) {
            case MAGIC_LOCAL_DAY:
                formatter.applyPattern("d");
                return formatter.format(current);
            case MAGIC_LOCAL_DAY2:
                formatter.applyPattern("dd");
                return formatter.format(current);
            case MAGIC_LOCAL_DAY_NAME:
                formatter.applyPattern("EEEE");
                return formatter.format(current);
            case MAGIC_LOCAL_DAY_OF_WEEK:
                formatter.applyPattern("F");
                return formatter.format(current);
            case MAGIC_LOCAL_MONTH:
                formatter.applyPattern("MM");
                return formatter.format(current);
            case MAGIC_LOCAL_MONTH_ABBR:
                formatter.applyPattern("MMM");
                return formatter.format(current);
            case MAGIC_LOCAL_MONTH_NAME:
                formatter.applyPattern("MMMM");
                return formatter.format(current);
            case MAGIC_LOCAL_TIME:
                formatter.applyPattern("HH:mm");
                return formatter.format(current);
            case MAGIC_LOCAL_HOUR:
                formatter.applyPattern("HH");
                return formatter.format(current);
            case MAGIC_LOCAL_WEEK:
                formatter.applyPattern("w");
                return formatter.format(current);
            case MAGIC_LOCAL_YEAR:
                formatter.applyPattern("yyyy");
                return formatter.format(current);
            case MAGIC_LOCAL_TIMESTAMP:
                formatter.applyPattern("yyyyMMddHHmmss");
                return formatter.format(current);
                // current date values

            case MAGIC_CURRENT_DAY:
                formatter.applyPattern("d");
                return formatter.format(current);
            case MAGIC_CURRENT_DAY2:
                formatter.applyPattern("dd");
                return formatter.format(current);
            case MAGIC_CURRENT_DAY_NAME:
                formatter.applyPattern("EEEE");
                return formatter.format(current);
            case MAGIC_CURRENT_DAY_OF_WEEK:
                formatter.applyPattern("F");
                return formatter.format(current);
            case MAGIC_CURRENT_MONTH:
                formatter.applyPattern("MM");
                return formatter.format(current);
            case MAGIC_CURRENT_MONTH_ABBR:
                formatter.applyPattern("MMM");
                return formatter.format(current);
            case MAGIC_CURRENT_MONTH_NAME:
                formatter.applyPattern("MMMM");
                return formatter.format(current);
            case MAGIC_CURRENT_TIME:
                formatter.applyPattern("HH:mm");
                return formatter.format(current);
            case MAGIC_CURRENT_HOUR:
                formatter.applyPattern("HH");
                return formatter.format(current);
            case MAGIC_CURRENT_WEEK:
                formatter.applyPattern("w");
                return formatter.format(current);
            case MAGIC_CURRENT_YEAR:
                formatter.applyPattern("yyyy");
                return formatter.format(current);
            case MAGIC_CURRENT_TIMESTAMP:
                formatter.applyPattern("yyyyMMddHHmmss");
                return formatter.format(current);
            case MAGIC_REVISION_YEAR:
                formatter.applyPattern("yyyy");
                return formatter.format(revision);
            case MAGIC_REVISION_DAY:
                formatter.applyPattern("d");
                return formatter.format(revision);
            case MAGIC_REVISION_DAY2:
                formatter.applyPattern("dd");
                return formatter.format(revision);
            case MAGIC_REVISION_MONTH:
                formatter.applyPattern("MM");
                return formatter.format(revision);
            case MAGIC_REVISION_MONTH1:
                formatter.applyPattern("M");
                return formatter.format(revision);
            case MAGIC_REVISION_TIMESTAMP:
                formatter.applyPattern("yyyyMMddHHmmss");
                return formatter.format(revision);
            case MAGIC_REVISION_USER:
                return "";
            case MAGIC_PAGE_NAME: {
                    String pagename = getPagenameHelper(parameter, model);
                    if (pagename != null) {
                        return pagename;
                    } else {
                        return "";
                    }
                }
            case MAGIC_PAGE_NAME_E: {
                    String pagename = getPagenameHelper(parameter, model);
                    if (pagename != null) {
                        return model.encodeTitleToUrl(pagename, true);
                    } else {
                        return "";
                    }
                }
            case MAGIC_NAMESPACE:
                return getNamespace(parameter, model);
            case MAGIC_NAMESPACE_E:
                return model.encodeTitleToUrl(getNamespace(parameter, model), true);
            case MAGIC_NAMESPACENUMBER: {
                final INamespaceValue namespaceHelper = getNamespaceHelper(parameter, model);
                if (namespaceHelper != null) {
                  return namespaceHelper.getCode().code.toString();
                } else {
                    return null;
                }
            }
            case MAGIC_TALK_SPACE:
                return getTalkspace(parameter, model);
            case MAGIC_TALK_SPACE_E:
                return model.encodeTitleToUrl(getTalkspace(parameter, model), true);
            case MAGIC_SUBJECT_SPACE:
            case MAGIC_ARTICLE_SPACE:
                return getSubjectSpace(parameter, model);
            case MAGIC_SUBJECT_SPACE_E:
            case MAGIC_ARTICLE_SPACE_E:
                return model.encodeTitleToUrl(getSubjectSpace(parameter, model), true);
            case MAGIC_FULL_PAGE_NAME:
                return getFullpagename(parameter, model);
            case MAGIC_FULL_PAGE_NAME_E:
                return model.encodeTitleToUrl(getFullpagename(parameter, model), true);
            case MAGIC_FULL_URL:
                return fullUrl(model, parameter, false);
            case MAGIC_FULL_URL_E:
                return fullUrl(model, parameter, true);
            case MAGIC_TALK_PAGE_NAME:
                return getTalkpage(parameter, model);
            case MAGIC_TALK_PAGE_NAME_E:
                return model.encodeTitleToUrl(getTalkpage(parameter, model), true);
            case MAGIC_SUBJECT_PAGE_NAME:
            case MAGIC_ARTICLE_PAGE_NAME:
                return getSubjectpage(parameter, model);
            case MAGIC_SUBJECT_PAGE_NAME_E:
            case MAGIC_ARTICLE_PAGE_NAME_E:
                return model.encodeTitleToUrl(getSubjectpage(parameter, model), true);
            case MAGIC_BASE_PAGE_NAME:
                return getBasePageName(parameter, model);
            case MAGIC_BASE_PAGE_NAME_E:
                return model.encodeTitleToUrl(getBasePageName(parameter, model), true);
            case MAGIC_SUB_PAGE_NAME:
                return getSubPageName(parameter, model);
            case MAGIC_SUB_PAGE_NAME_E:
                return model.encodeTitleToUrl(getSubPageName(parameter, model), false);
            case MAGIC_BANG:
                return "|";
            default:
                break;
        }

        return magicWord.toString();
    }

    /**
     * Gets the sub page name of a given non-<tt>null</tt> parameter or the
     * current model's pagename and namespace.
     *
     * @param parameter
     *            the parameter of the magic word
     * @param model
     *            the model being used
     *
     * @return the sub page name (or the pagename if there is no sub-page)
     */
    protected static String getSubPageName(String parameter, IWikiModel model) {
        String pagename = getPagenameHelper(parameter, model);
        if (pagename != null) {
            return Titleparts.getTitleparts(pagename, 0, -1);
        } else {
            return "";
        }
    }

    /**
     * Gets the base page name of a given non-<tt>null</tt> parameter or the
     * current model's pagename and namespace.
     *
     * @param parameter
     *            the parameter of the magic word
     * @param model
     *            the model being used
     *
     * @return the base page name
     */
    protected static String getBasePageName(String parameter, IWikiModel model) {
        String pagename = getPagenameHelper(parameter, model);
        if (pagename != null) {
            // note: titleparts even strips off if there is no subpage - in this
            // case the result is an empty string
            final String basePagename = Titleparts.getTitleparts(pagename, -1, 1);
            if (pagename.length() != 0 && basePagename.length() == 0) {
                return pagename;
            } else {
                return basePagename;
            }
        } else {
            return "";
        }
    }

    /**
     * Gets the full page's name of a given non-<tt>null</tt> parameter or the
     * current model's pagename and namespace.
     *
     * @param parameter
     *            the parameter of the magic word (may be <tt>null</tt>)
     * @param model
     *            the model being used
     *
     * @return the (normalised) name of the page
     */
    protected static String getFullpagename(String parameter, IWikiModel model) {
        String[] fullPage = getPagenameHelper2(parameter, model);
        if (fullPage != null) {
            INamespaceValue namespace = model.getNamespace().getNamespace(fullPage[0]);
            if (namespace == null) {
                return fullPage[1];
            } else {
                return namespace.makeFullPagename(fullPage[1]);
            }
        } else {
            return "";
        }
    }

    private static String fullUrl(IWikiModel model, String parameter, boolean encode) {
        final String name = getFullpagename(parameter, model);
        return model.getWikiBaseURL()
                .replace("${title}", encode ? model.encodeTitleToUrl(name, true) : name)
                .replaceAll("^https?:", "");
    }

    /**
     * Gets the talkpage's name of a given non-<tt>null</tt> parameter or the
     * current model's pagename and namespace.
     *
     * @param parameter
     *            the parameter of the magic word (may be <tt>null</tt>)
     * @param model
     *            the model being used
     *
     * @return the name of the talkpage
     */
    protected static String getSubjectpage(String parameter, IWikiModel model) {
        String[] fullPage = getPagenameHelper2(parameter, model);
        if (fullPage != null) {
            INamespaceValue subjectSpace = model.getNamespace().getContentspace(fullPage[0]);
            if (subjectSpace == null) {
                return fullPage[1];
            } else {
                return subjectSpace.makeFullPagename(fullPage[1]);
            }
        } else {
            return "";
        }
    }

    /**
     * Gets the talkpage's name of a given non-<tt>null</tt> parameter or the
     * current model's pagename and namespace.
     *
     * @param parameter
     *            the parameter of the magic word (may be <tt>null</tt>)
     * @param model
     *            the model being used
     *
     * @return the name of the talkpage
     */
    protected static String getTalkpage(String parameter, IWikiModel model) {
    String[] fullPage = getPagenameHelper2(parameter, model);
        if (fullPage != null) {
            INamespaceValue talkSpace = model.getNamespace().getTalkspace(fullPage[0]);
            if (talkSpace == null) {
                return fullPage[1];
            } else {
                return talkSpace.makeFullPagename(fullPage[1]);
            }
        } else {
            return "";
        }
    }

    /**
     * Gets the talkspace of a given non-<tt>null</tt> parameter
     * or the current model's namespace.
     *
     * @param parameter
     *            the parameter of the magic word (may be <tt>null</tt>)
     * @param model
     *            the model being used
     *
     * @return the talkspace
     */
    protected static String getTalkspace(String parameter, IWikiModel model) {
        INamespaceValue namespace = getNamespaceHelper(parameter, model);
        if (namespace != null) {
            return namespace.getTalkspace().toString();
        } else {
            return "";
        }
    }

    /**
     * Gets the subject/articlespace of a given non-<tt>null</tt> parameter
     * or the current model's namespace.
     *
     * @param parameter
     *            the parameter of the magic word (may be <tt>null</tt>)
     * @param model
     *            the model being used
     *
     * @return the subjectspace
     */
    protected static String getSubjectSpace(String parameter, IWikiModel model) {
        INamespaceValue namespace = getNamespaceHelper(parameter, model);
        if (namespace != null) {
            return namespace.getContentspace().toString();
        } else {
            return "";
        }
    }

    /**
     * Helper to get the namespace of either a given non-<tt>null</tt> parameter
     * or the current model's namespace.
     *
     * @param parameter
     *            the parameter of the magic word (may be <tt>null</tt>)
     * @param model
     *            the model being used
     *
     * @return the extracted namespace or <tt>""</tt> if the parameter was empty
     */
    protected static String getNamespace(String parameter, IWikiModel model) {
        INamespaceValue namespace = getNamespaceHelper(parameter, model);
        if (namespace != null) {
            return namespace.toString();
        } else {
            return "";
        }
    }

    /**
     * Helper to get the namespace of either a given non-<tt>null</tt> parameter
     * or the current model's namespace.
     *
     * @param parameter
     *            the parameter of the magic word (may be <tt>null</tt>)
     * @param model
     *            the model being used
     *
     * @return the extracted namespace or <tt>null</tt> if the parameter was empty
     */
    protected static INamespaceValue getNamespaceHelper(String parameter, IWikiModel model) {
        final INamespace namespaceObj = model.getNamespace();
        if (parameter != null) {
            if (parameter.length() > 0) {
                String[] split = model.splitNsTitle(parameter);
                return namespaceObj.getNamespace(split[0]);
            } else {
                return null;
            }
        } else {
            return namespaceObj.getNamespace(model.getNamespaceName());
        }
    }

    /**
     * Helper to get the pagename (excluding the namespace) of either a given
     * non-<tt>null</tt> parameter or the current model's pagename.
     *
     * @param parameter
     *            the parameter of the magic word (may be <tt>null</tt>)
     * @param model
     *            the model being used
     *
     * @return the extracted pagename or <tt>null</tt> if the parameter was
     *         empty
     */
    protected static String getPagenameHelper(String parameter, IWikiModel model) {
        if (parameter != null) {
            if (parameter.length() > 0) {
                String[] split = model.splitNsTitle(parameter);
                return split[1];
            } else {
                return null;
            }
        }
        return model.getPageName();
    }

    /**
     * Helper to get the pagename and the namespace of either a given non-
     * <tt>null</tt> parameter or the current model's pagename and namespace.
     *
     * @param parameter
     *            the parameter of the magic word (may be <tt>null</tt>)
     * @param model
     *            the model being used
     *
     * @return a 2-element array with the namespace (index 0) and the page title
     *         (index 1) or <tt>null</tt> if the parameter was empty
     */
    protected static String[] getPagenameHelper2(String parameter, IWikiModel model) {
        if (parameter != null) {
            if (parameter.length() > 0) {
                return model.splitNsTitle(parameter);
            } else {
                return null;
            }
        }
        return new String[] {
                model.getNamespace().getNamespace(model.getNamespaceName()).toString(),
                model.getPageName() };
    }
}
