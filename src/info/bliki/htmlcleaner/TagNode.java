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

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.filter.PlainTextConvertable;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * <p>
 * XML node node tag - it is produced during cleaning process when all start and
 * end tokens are removed and replaced by instances of TagNode.
 * </p>
 *
 * Created by: Vladimir Nikic<br/>
 * Date: November, 2006.<br/>
 *
 * Modified by: Axel Kramer<br/>
 */
public class TagNode extends TagToken implements PlainTextConvertable {
    /**
     * Allowed attributes
     *
     * <b>Note:</b> the 'style' attribute isn't allowed by default because of XSS
     * risks; if you need this attribute (or other attributes not listed here) you
     * can add it with the <code>static addAllowedAttribute()</code> method.
     */
    public static final String[] ALLOWED_ATTRIBUTES = { "title", "align", "lang", "dir", "width", "height", "bgcolor", "clear",
            "noshade", "cite", "size", "face", "color", "type", "start", "value", "compact", "summary", "width", "border", "frame",
            "rules", "cellspacing", "cellpadding", "valign", "char", "charoff", "colgroup", "col", "span", "abbr", "axis", "headers",
            "scope", "rowspan", "colspan", "id", "class", "name", "href", "rel", "alt", "src" };

    protected static final HashSet<String> ALLOWED_ATTRIBUTES_SET = new HashSet<>();

    static {
        Collections.addAll(ALLOWED_ATTRIBUTES_SET, ALLOWED_ATTRIBUTES);
    }

    public static Set<String> getAllowedAttributes() {
        return ALLOWED_ATTRIBUTES_SET;
    }

    /**
     * Add an additional allowed attribute name
     *
     * <b>Note:</b> the 'style' attribute isn't allowed by default because of XSS
     * risks; if you need this attribute (or other attributes not listed here) you
     * can add it with this method.
     */
    public static boolean addAllowedAttribute(String key) {
        return ALLOWED_ATTRIBUTES_SET.add(key);
    }

    private TagNode parent;
    private Map<String, String> attributes = new TreeMap<>();
    private HashMap<String, Object> objectAttributes;
    private List<Object> children = new ArrayList<>();
    private List<BaseToken> itemsToMove;
    private transient boolean isFormed;

    public TagNode() {
    }

    public TagNode(String name) {
        super(name.toLowerCase());
    }
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Get a special object for this TagNode which contains original information
     * from the parsed wiki object (for example the ImageFormat or original wiki
     * topic string).
     *
     * @param attName
     *          the attribute name
     * @param attValue
     *          the attribute value
     * @see #addObjectAttribute(String, Object)
     * @see info.bliki.wiki.model.ImageFormat
     */
    public Map<String, Object> getObjectAttributes() {
        return objectAttributes;
    }

    public List<Object> getChildren() {
        return children;
    }

    public TagNode getParent() {
        return parent;
    }

    public void setParent(TagNode parent) {
        this.parent = parent;
    }

    @Override
    public boolean addAttribute(String attName, String attValue, boolean checkXSS) {
        if (attName != null && (!"".equals(attName.trim()) && attValue != null)) {
            boolean checkedAttributes = true;
            String nameLowerCased = attName.toLowerCase();
            String valueLowerCased = attValue.toLowerCase();
            if (checkXSS && (!getAllowedAttributes().contains(nameLowerCased))) {
                if (!isAllowedAttribute(nameLowerCased)) {
                    checkedAttributes = false;
                }
            }
            if (checkedAttributes && valueLowerCased.contains("javascript:")) {
                checkedAttributes = false;
            }
            // attempt to prevent cross-site scripting inside CSS style (this is
            // not complete!)
            // see http://openmya.hacker.jp/hasegawa/security/expression.txt
            if (checkedAttributes && attName.equalsIgnoreCase("style")) {
                if (valueLowerCased.contains("expression")) {
                    checkedAttributes = false;
                } else if (valueLowerCased.contains("url")) {
                    checkedAttributes = false;
                } else if (valueLowerCased.contains("tps")) {
                    checkedAttributes = false;
                }
            }

            if (checkedAttributes) {
                attributes.put(nameLowerCased, attValue);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAllowedAttribute(String attName) {
        return false;
    }

    /**
     * Add a special object to this TagNode which contains original information
     * from the parsed wiki object (for example the ImageFormat or original wiki
     * topic string)
     *
     * @param attName
     *          the attribute name
     * @param attValue
     *          the attribute value
     * @see #getObjectAttributes()
     * @see info.bliki.wiki.model.ImageFormat
     */
    public void addObjectAttribute(String attName, Object attValue) {
        if (attName != null && attValue != null) {
            if (objectAttributes == null) {
                objectAttributes = new HashMap<>(4);
            }
            objectAttributes.put(attName, attValue);
        }
    }

    public void addChild(Object child) {
        children.add(child);
        if (child instanceof TagNode) {
            TagNode childTagNode = (TagNode) child;
            childTagNode.parent = this;
        }
    }

    public void addChildren(List<?> children) {
        if (children != null) {
            for (Object child : children) {
                addChild(child);
            }
        }
    }

    public void addItemForMoving(BaseToken item) {
        if (itemsToMove == null) {
            itemsToMove = new ArrayList<>();
        }

        itemsToMove.add(item);
    }

    public List<BaseToken> getItemsToMove() {
        return itemsToMove;
    }

    public void setItemsToMove(List<BaseToken> itemsToMove) {
        this.itemsToMove = itemsToMove;
    }

    public boolean isFormed() {
        return isFormed;
    }

    public void setFormed() {
        this.isFormed = true;
    }

    @Override
    public void serialize(XmlSerializer xmlSerializer) throws IOException {
        xmlSerializer.serialize(this);
    }

    public TagNode makeCopy() {
        TagNode copy = new TagNode(this.name);
        copy.attributes = this.attributes;
        copy.objectAttributes = this.objectAttributes;
        return copy;
    }

    @Override
    public Object clone() {
        TagNode tt = (TagNode) super.clone();
        tt.parent = this.parent;
        tt.itemsToMove = this.itemsToMove;
        tt.isFormed = this.isFormed;
        tt.children = new ArrayList<>(this.children);
        tt.attributes = new TreeMap<>(this.attributes);
        if (objectAttributes == null) {
            tt.objectAttributes = null;
        } else {
            tt.objectAttributes = new HashMap<>(objectAttributes);
        }
        return tt;
    }

    @Override
    public String getParents() {
        return null;
    }

    @Override
    public void renderPlainText(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
        List<Object> children = getChildren();
        for (Object child : children) {
            if (child instanceof PlainTextConvertable) {
                ((PlainTextConvertable) child).renderPlainText(converter, buf, wikiModel);
            }
        }
    }

    /**
     * Get the pure content text without the tags from this HTMLTag
     */
    public String getBodyString() {
        List<Object> children = getChildren();
        if (children.size() > 0) {
            StringBuilder buf = new StringBuilder(children.size() * 16);
            for (Object child : children) {
                if (child instanceof ContentToken) {
                    buf.append(((ContentToken) child).getContent());
                } else if (child instanceof TagNode) {
                    buf.append(((TagNode) child).getBodyString());
                }
            }
            return buf.toString();
        } else {
            return "";
        }
    }
}
