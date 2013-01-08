/*  Copyright (c) 2006-2007, Vladimir Nikic
    All rights reserved.

    Redistribution and use of this software in source and binary forms,
    with or without modification, are permitted provided that the following
    conditions are met:

    * Redistributions of source code must retain the above
      copyright notice, this list of conditions and the
      following disclaimer.

    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the
      following disclaimer in the documentation and/or other
      materials provided with the distribution.

    * The name of HtmlCleaner may not be used to endorse or promote
      products derived from this software without specific prior
      written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.

    You can contact Vladimir Nikic by sending e-mail to
    nikic_vladimir@yahoo.com. Please include the word "HtmlCleaner" in the
    subject line.
*/

package info.bliki.htmlcleaner;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * <p>
 * Default HTML tag info provider. Here the basic set of HTML tags is defined, including
 * depricated tags and some Microsoft specific tags. Rules for tag balancing are similar
 * to that used in most web-browsers.
 * </p>
 *
 * Created by: Vladimir Nikic<br/>
 * Date: November, 2006.
 */
public class HtmlTagProvider extends HashMap implements ITagInfoProvider {

    // singleton instance, used if no other TagInfoProvider is specified
    private static HtmlTagProvider _instance;

    /**
     * Returns singleton instance of this class.
     */
    public static synchronized HtmlTagProvider getInstance() {
        if (_instance == null) {
            _instance = new HtmlTagProvider();
        }

        return _instance;
    }

    /**
     * Default constructor - creates tags and rules for balancing.
     */
    public HtmlTagProvider() {
        defineTags();
    }

    /**
     * Shortcut to creating TagInfo instance and storing it to the map.
     * @param name
     * @param contentType
     * @param belongsTo
     * @param dependancies
     */
    protected void addTag(String name, String contentType, int belongsTo, String dependancies) {
        this.put( name.toLowerCase(), new TagInfo(name, contentType, belongsTo, false, false, false, dependancies) );
    }

    /**
     * Definition of all HTML tags together with rules for tag balancing.
     */
    protected void defineTags() {
        // Structure
        addTag("div",       TagInfo.CONTENT_ALL,    TagInfo.BODY, null);
        addTag("span",      TagInfo.CONTENT_ALL,	TagInfo.BODY, null);

        // Meta Information
        addTag("meta",      TagInfo.CONTENT_NONE,	TagInfo.HEAD, null);
        addTag("link",      TagInfo.CONTENT_NONE,   TagInfo.HEAD, null);
        addTag("title",     TagInfo.CONTENT_TEXT,   TagInfo.HEAD, null);
        addTag("style",     TagInfo.CONTENT_ALL,	TagInfo.HEAD, null);
        addTag("bgsound",   TagInfo.CONTENT_NONE, 	TagInfo.HEAD, null);

        // Text
        addTag("h1",        TagInfo.CONTENT_ALL,	TagInfo.BODY, "h1,h2,h3,h4,h5,h6");
        addTag("h2",        TagInfo.CONTENT_ALL,	TagInfo.BODY, "h1,h2,h3,h4,h5,h6");
        addTag("h3",        TagInfo.CONTENT_ALL,	TagInfo.BODY, "h1,h2,h3,h4,h5,h6");
        addTag("h4",        TagInfo.CONTENT_ALL,	TagInfo.BODY, "h1,h2,h3,h4,h5,h6");
        addTag("h5",        TagInfo.CONTENT_ALL,	TagInfo.BODY, "h1,h2,h3,h4,h5,h6");
        addTag("h6",        TagInfo.CONTENT_ALL,	TagInfo.BODY, "h1,h2,h3,h4,h5,h6");
        addTag("p",         TagInfo.CONTENT_ALL,  	TagInfo.BODY, "p");
        addTag("strong",    TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("em",        TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("abbr",      TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("acronym",   TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("address",   TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("bdo",       TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("blockquote",TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("cite",      TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("q",       	TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("code",      TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("ins",       TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("del",       TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("dfn",       TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("kbd",       TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("pre",       TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("samp",      TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("listing",   TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("var",       TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("br",        TagInfo.CONTENT_NONE,	TagInfo.BODY, null);
        addTag("wbr",       TagInfo.CONTENT_NONE,	TagInfo.BODY, null);
        addTag("nobr",      TagInfo.CONTENT_ALL,	TagInfo.BODY, "nobr");
        addTag("xmp",       TagInfo.CONTENT_TEXT,	TagInfo.BODY, null);

        // Links
        addTag("a",         TagInfo.CONTENT_ALL,  	TagInfo.BODY, "a");
        addTag("base",      TagInfo.CONTENT_NONE,	TagInfo.HEAD, null);

        // Images and Objects
        addTag("img",       TagInfo.CONTENT_NONE,	TagInfo.BODY, null);
        addTag("area",      TagInfo.CONTENT_NONE,	TagInfo.BODY, "!map,area");
        addTag("map",       TagInfo.CONTENT_ALL,	TagInfo.BODY, "map");
        addTag("object",    TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("param",     TagInfo.CONTENT_NONE, 	TagInfo.BODY, null);
        addTag("applet",    TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("xml",       TagInfo.CONTENT_ALL,	TagInfo.BODY, null);

        // Lists
        addTag("ul",        TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("ol",        TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("li",        TagInfo.CONTENT_ALL,	TagInfo.BODY, "li");
        addTag("dl",     	TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("dt",     	TagInfo.CONTENT_ALL,	TagInfo.BODY, "dt,dd");
        addTag("dd",     	TagInfo.CONTENT_ALL,	TagInfo.BODY, "dt,dd");
        addTag("menu",      TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("dir",       TagInfo.CONTENT_ALL,   	TagInfo.BODY, null);

        // Tables
        addTag("table", 	TagInfo.CONTENT_ALL,	TagInfo.BODY, "#tr,#tbody,#thead,#tfoot,#colgroup,#caption,#tr,tr,thead,tbody,tfoot,caption,colgroup,table");
        addTag("tr", 		TagInfo.CONTENT_ALL,	TagInfo.BODY, "!table,+tbody,^thead,^tfoot,#td,#th,tr,caption,colgroup");
        addTag("td", 		TagInfo.CONTENT_ALL,	TagInfo.BODY, "!table,+tr,td,th,caption,colgroup");
        addTag("th", 		TagInfo.CONTENT_ALL,	TagInfo.BODY, "!table,+tr,td,th,caption,colgroup");
        addTag("tbody", 	TagInfo.CONTENT_ALL,	TagInfo.BODY, "!table,#tr,td,th,tr,tbody,thead,tfoot,caption,colgroup");
        addTag("thead", 	TagInfo.CONTENT_ALL,	TagInfo.BODY, "!table,#tr,td,th,tr,tbody,thead,tfoot,caption,colgroup");
        addTag("tfoot", 	TagInfo.CONTENT_ALL,	TagInfo.BODY, "!table,#tr,td,th,tr,tbody,thead,tfoot,caption,colgroup");
        addTag("col", 		TagInfo.CONTENT_NONE,	TagInfo.BODY, "!colgroup");
        addTag("colgroup",	TagInfo.CONTENT_ALL,	TagInfo.BODY, "!table,#col,td,th,tr,tbody,thead,tfoot,caption,colgroup");
        addTag("caption",	TagInfo.CONTENT_ALL,	TagInfo.BODY, "!table,td,th,tr,tbody,thead,tfoot,caption,colgroup");

        // Forms
        addTag("form",     	TagInfo.CONTENT_ALL,	TagInfo.BODY, "-form,option,optgroup,textarea,select,fieldset");
        addTag("input",     TagInfo.CONTENT_NONE,	TagInfo.BODY, "select,optgroup,option");
        addTag("textarea",  TagInfo.CONTENT_ALL,	TagInfo.BODY, "select,optgroup,option");
        addTag("select",    TagInfo.CONTENT_ALL,    TagInfo.BODY, "#option,#optgroup,option,optgroup,select");
        addTag("option",    TagInfo.CONTENT_TEXT,	TagInfo.BODY, "!select,option");
        addTag("optgroup",  TagInfo.CONTENT_ALL,	TagInfo.BODY, "!select,#option,optgroup");
        addTag("button",    TagInfo.CONTENT_ALL,	TagInfo.BODY, "select,optgroup,option");
        addTag("label",     TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("fieldset",  TagInfo.CONTENT_ALL,	TagInfo.BODY, null);
        addTag("isindex",   TagInfo.CONTENT_NONE,	TagInfo.BODY, null);

        // Scripting
        addTag("script",    TagInfo.CONTENT_ALL,  	TagInfo.HEAD_AND_BODY, null);
        addTag("noscript",  TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);

        // Presentational
        addTag("b",         TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("i",         TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("u",         TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("tt",        TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("sub",       TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("sup",       TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("big",       TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("small",     TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("strike",    TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("blink",     TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("marquee",   TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("s",     	TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("hr",        TagInfo.CONTENT_NONE,  	TagInfo.BODY, null);
        addTag("font",      TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("basefont",  TagInfo.CONTENT_NONE, 	TagInfo.BODY, null);
        addTag("center",    TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);

        addTag("comment",   TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("server",    TagInfo.CONTENT_ALL,  	TagInfo.BODY, null);
        addTag("iframe",    TagInfo.CONTENT_NONE,  	TagInfo.BODY, null);
        addTag("embed",     TagInfo.CONTENT_NONE,  	TagInfo.BODY, null);

        getTagInfo("title").setUnique(true);
        getTagInfo("form").setIgnorePermitted(true);
        getTagInfo("select").setIgnorePermitted(true);
        getTagInfo("option").setIgnorePermitted(true);
        getTagInfo("optgroup").setIgnorePermitted(true);

        String commonTags = "div,p,address,h1,h2,h3,h4,h5,h6,blockquote,pre,listing,ul,ol,li,dl,menu,dir,table,form,fieldset,isindex,marquee,center,embed,param,hr";

        addDependancy("p", commonTags);
        addDependancy("address", commonTags);
        addDependancy("label", commonTags);
        addDependancy("abbr", commonTags);
        addDependancy("acronym", commonTags);
        addDependancy("dfn", commonTags);
        addDependancy("kbd", commonTags);
        addDependancy("samp", commonTags);
        addDependancy("var", commonTags);
        addDependancy("cite", commonTags);
        addDependancy("code", commonTags);
        addDependancy("param", commonTags);
        addDependancy("xml", commonTags);

        addDependancy("&a", commonTags);
        addDependancy("&bdo", commonTags);
        addDependancy("&strong", commonTags);
        addDependancy("&em", commonTags);
        addDependancy("&q", commonTags);
        addDependancy("&b", commonTags);
        addDependancy("&i", commonTags);
        addDependancy("&u", commonTags);
        addDependancy("&tt", commonTags);
        addDependancy("&sub", commonTags);
        addDependancy("&sup", commonTags);
        addDependancy("&big", commonTags);
        addDependancy("&small", commonTags);
        addDependancy("&strike", commonTags);
        addDependancy("&s", commonTags);
        addDependancy("&font", commonTags);

        getTagInfo("applet").setDeprecated(true);
        getTagInfo("basefont").setDeprecated(true);
        getTagInfo("center").setDeprecated(true);
        getTagInfo("dir").setDeprecated(true);
        getTagInfo("font").setDeprecated(true);
        getTagInfo("isindex").setDeprecated(true);
        getTagInfo("menu").setDeprecated(true);
        getTagInfo("s").setDeprecated(true);
        getTagInfo("strike").setDeprecated(true);
        getTagInfo("u").setDeprecated(true);
    }

    protected void addDependancy(String tagName, String tagList) {
        if (tagList != null) {
            StringTokenizer tokenizer = new StringTokenizer(tagList, ",.");
            while (tokenizer.hasMoreTokens()) {
                TagInfo curr = getTagInfo(tokenizer.nextToken().trim());
                curr.addDependancy(tagName);
            }
        }
    }

    /**
     * Implementation of the interface method.
     * @param tagName
     * @return TagInfo instance from the map, for the specified tag name.
     */
    public TagInfo getTagInfo(String tagName) {
        if (tagName != null) {
            return (TagInfo) get( tagName.toLowerCase() );
        }

        return null;
    }

}