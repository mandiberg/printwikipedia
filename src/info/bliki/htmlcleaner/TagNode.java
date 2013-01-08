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

import info.bliki.wiki.tags.HTMLTag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
 * Created by: Vladimir Nikic<br/> Date: November, 2006.<br/>
 * 
 * Modified by: Axel Kramer<br/>
 */
public class TagNode extends TagToken {
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

	protected static final HashSet<String> ALLOWED_ATTRIBUTES_SET = new HashSet<String>();

	static {
		for (int i = 0; i < ALLOWED_ATTRIBUTES.length; i++) {
			ALLOWED_ATTRIBUTES_SET.add(ALLOWED_ATTRIBUTES[i]);
		}
	}

	public static Set<String> getAllowedAttributes() {
		return ALLOWED_ATTRIBUTES_SET;
	}

	public static boolean removeAllowedAttribute(String key) {
		return ALLOWED_ATTRIBUTES_SET.remove(key);
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

	private TagNode parent = null;

	private Map<String, String> attributes = new TreeMap<String, String>();

	private HashMap<String, Object> objectAttributes = null;

	private List<Object> children = new ArrayList<Object>();

	private List<Object> itemsToMove = null;

	private transient boolean isFormed = false;

	public TagNode() {
	}

	public TagNode(String name) {
		super(name.toLowerCase());
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

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
				checkedAttributes = false;
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

	public void addObjectAttribute(String attName, Object attValue) {
		if (attName != null && attValue != null) {
			if (objectAttributes == null) {
				objectAttributes = new HashMap<String, Object>(4);
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

	public void addChildren(List<? extends Object> children) {
		if (children != null) {
			Iterator<? extends Object> it = children.iterator();
			while (it.hasNext()) {
				Object child = it.next();
				addChild(child);
			}
		}
	}

	public void addItemForMoving(BaseToken item) {
		if (itemsToMove == null) {
			itemsToMove = new ArrayList<Object>();
		}

		itemsToMove.add(item);
	}

	public List<Object> getItemsToMove() {
		return itemsToMove;
	}

	public void setItemsToMove(List<Object> itemsToMove) {
		this.itemsToMove = itemsToMove;
	}

	public boolean isFormed() {
		return isFormed;
	}

	public void setFormed() {
		this.isFormed = true;
	}

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
		tt.children = new ArrayList(this.children);
		tt.attributes = new TreeMap<String, String>(this.attributes);
		if (objectAttributes == null) {
			tt.objectAttributes = null;
		} else {
			tt.objectAttributes = new HashMap<String, Object>(objectAttributes);
		}
		return tt;
	}

	public String getParents() {
		return null;
	}

	/**
	 * Get the pure content text without the tags from this HTMLTag
	 * 
	 * @return
	 */
	public void getBodyString(Appendable buf) throws IOException {
		List<Object> children = getChildren();
		if (children.size() == 1 && children.get(0) instanceof ContentToken) {
			buf.append(((ContentToken) children.get(0)).getContent());
		} else {
			if (children.size() > 0) {
				for (int i = 0; i < children.size(); i++) {
					if (children.get(i) instanceof ContentToken) {
						buf.append(((ContentToken) children.get(i)).getContent());
					} else if (children.get(i) instanceof HTMLTag) {
						((HTMLTag) children.get(i)).getBodyString(buf);
					}
				}
			}
		}
	}

	/**
	 * Get the pure content text without the tags from this HTMLTag
	 * 
	 * @return
	 */
	public String getBodyString() {
		List<Object> children = getChildren();
		if (children.size() == 1 && children.get(0) instanceof ContentToken) {
			return ((ContentToken) children.get(0)).getContent();
		} else {
			if (children.size() > 0) {
				StringBuilder buf = new StringBuilder(children.size() * 16);
				for (int i = 0; i < children.size(); i++) {
					if (children.get(i) instanceof ContentToken) {
						buf.append(((ContentToken) children.get(i)).getContent());
					} else if (children.get(i) instanceof HTMLTag) {
						buf.append(((HTMLTag) children.get(i)).getBodyString());
					}
				}
				return buf.toString();
			}
		}
		return "";
	}
}