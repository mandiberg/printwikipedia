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

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * <p>
 * Class contains information about single HTML tag.<br/> It also contains
 * rules to for tag balancing. For each tag, list of dependant tags may be
 * defined. In order to more easely describe those rules, several prefixed are
 * introduced.
 * </p>
 * <p>
 * For each tag, list of dependant tags may be specified using following
 * prefixes:
 * <ul>
 * <li>
 * <h3>!</h3>
 * fatal tag - required outer tag - the tag will be ignored during parsing (will
 * be skipped) if this fatal tag is missing. For example, most web browsers
 * ignore elements TD, TR, TBODY if they are not in the context of TABLE tag.
 * </li>
 * <li>
 * <h3>+</h3>
 * required enclosing tag - if there is no such, it is implicitely created. For
 * example if TD is out of TR - open TR is created before. </li>
 * <li>
 * <h3>-</h3>
 * permitted tag - it is not allowed to occure inside - for example FORM cannot
 * be inside other FORM and it will be ignored during cleanup. </li>
 * <li>
 * <h3>#</h3>
 * allowed children tags - for example TR allowes TD and TH. If there are some
 * dependant allowed tags defined then cleaner ignores other tags, treating them
 * as unallowed, unless they are in some other relationship with this tag. </li>
 * <li>
 * <h3>^</h3>
 * higher level tags - for example for TR higher tags are THEAD, TBODY, TFOOT.
 * </li>
 * <li>
 * <h3>&</h3>
 * tags that must be closed and copied - for example, in
 * <code>&lt;a href="#"&gt;&lt;div&gt;....</code> tag A must be closed before
 * DIV but copied again inside DIV. </li>
 * </ul>
 * </p>
 * 
 * <p>
 * Tag TR for instance (table row) may define the following dependancies:
 * <code>!table,+tbody,^thead,^tfoot,#td,#th,tr,caption,colgroup</code>
 * meaning the following: <br>
 * <li>TR must be in context of TABLE, otherwise it will be ignored,</li>
 * <li>TR may can be directly inside TBODY, TFOOT and THEAD, otherwise TBODY
 * will be implicitely created in front of it.</li>
 * <li>TR can contain TD and TD, all other tags and content will be pushed out
 * of current limiting context, in the case of html tables, in front of
 * enclosing TABLE tag.</li>
 * <li>if previous open tag is one of TR, CAPTION or COLGROUP, it will be
 * implicitely closed.</li>
 * </p>
 * 
 * Created by Vladimir Nikic.<br/> Date: November, 2006
 */
public class TagInfo {

	static final int HEAD_AND_BODY = 0;

	static final int HEAD = 1;

	static final int BODY = 2;

	static String CONTENT_ALL = "ALL";

	static String CONTENT_NONE = "NONE";

	static String CONTENT_TEXT = "TEXT";

	private String name;

	private String contentType;

	private Set mustCloseTags = new HashSet();

	private Set higherTags = new HashSet();

	private Set childTags = new HashSet();

	private Set permittedTags = new HashSet();

	private Set copyTags = new HashSet();

	private int belongsTo = BODY;

	private String requiredParent = null;

	private String fatalTag = null;

	private boolean deprecated = false;

	private boolean unique = false;

	private boolean ignorePermitted = false;

	public TagInfo(String name, String contentType, int belongsTo, boolean depricated, boolean unique, boolean ignorePermitted,
			String dependancies) {
		this.name = name;
		this.contentType = contentType;
		this.belongsTo = belongsTo;
		this.deprecated = depricated;
		this.unique = unique;
		this.ignorePermitted = ignorePermitted;

		// defines dependant tags
		if (dependancies != null) {
			StringTokenizer tokenizer = new StringTokenizer(dependancies, ",.;| ");
			while (tokenizer.hasMoreTokens()) {
				String currTag = tokenizer.nextToken().toLowerCase();
				addDependancy(currTag);
			}
		}
	}

	public void addDependancy(String dependantTagName) {
		if (dependantTagName.startsWith("!")) {
			String tagName = dependantTagName.substring(1);
			this.fatalTag = tagName;
			this.higherTags.add(tagName);
		} else if (dependantTagName.startsWith("+")) {
			String tagName = dependantTagName.substring(1);
			this.requiredParent = dependantTagName.substring(1);
			this.higherTags.add(tagName);
		} else if (dependantTagName.startsWith("-")) {
			this.permittedTags.add(dependantTagName.substring(1));
		} else if (dependantTagName.startsWith("#")) {
			this.childTags.add(dependantTagName.substring(1));
		} else if (dependantTagName.startsWith("^")) {
			this.higherTags.add(dependantTagName.substring(1));
		} else if (dependantTagName.startsWith("&")) {
			String tagName = dependantTagName.substring(1);
			this.copyTags.add(tagName);
			this.mustCloseTags.add(tagName);
		} else if (!"".equals(dependantTagName.trim())) {
			this.mustCloseTags.add(dependantTagName);
		}
	}

	// getters and setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Set getMustCloseTags() {
		return mustCloseTags;
	}

	public void setMustCloseTags(Set mustCloseTags) {
		this.mustCloseTags = mustCloseTags;
	}

	public Set getHigherTags() {
		return higherTags;
	}

	public void setHigherTags(Set higherTags) {
		this.higherTags = higherTags;
	}

	public Set getChildTags() {
		return childTags;
	}

	public void setChildTags(Set childTags) {
		this.childTags = childTags;
	}

	public Set getPermittedTags() {
		return permittedTags;
	}

	public void setPermittedTags(Set permittedTags) {
		this.permittedTags = permittedTags;
	}

	public Set getCopyTags() {
		return copyTags;
	}

	public void setCopyTags(Set copyTags) {
		this.copyTags = copyTags;
	}

	public String getRequiredParent() {
		return requiredParent;
	}

	public void setRequiredParent(String requiredParent) {
		this.requiredParent = requiredParent;
	}

	public int getBelongsTo() {
		return belongsTo;
	}

	public void setBelongsTo(int belongsTo) {
		this.belongsTo = belongsTo;
	}

	public String getFatalTag() {
		return fatalTag;
	}

	public void setFatalTag(String fatalTag) {
		this.fatalTag = fatalTag;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isIgnorePermitted() {
		return ignorePermitted;
	}

	public void setIgnorePermitted(boolean ignorePermitted) {
		this.ignorePermitted = ignorePermitted;
	}

	// other functionality

	boolean allowsBody() {
		return !CONTENT_NONE.equals(contentType);
	}

	boolean isHigher(String tagName) {
		return higherTags.contains(tagName);
	}

	boolean isCopy(String tagName) {
		return copyTags.contains(tagName);
	}

	boolean hasCopyTags() {
		return !copyTags.isEmpty();
	}

	boolean hasPermittedTags() {
		return !permittedTags.isEmpty();
	}

	boolean isHeadTag() {
		return belongsTo == HEAD;
	}

	boolean isHeadAndBodyTag() {
		return belongsTo == HEAD || belongsTo == HEAD_AND_BODY;
	}

	boolean isMustCloseTag(TagInfo tagInfo) {
		if (tagInfo != null) {
			return mustCloseTags.contains(tagInfo.getName()) || tagInfo.contentType.equals(CONTENT_TEXT);
		}

		return false;
	}

	boolean allowsItem(BaseToken token) {
		if (contentType != CONTENT_NONE && token instanceof TagToken) {
			TagToken tagToken = (TagToken) token;
			String tagName = tagToken.getName();
			if ("script".equals(tagName)) {
				return true;
			}
		}

		if (contentType == CONTENT_ALL) {
			if (!childTags.isEmpty()) {
				return token instanceof TagToken ? childTags.contains(((TagToken) token).getName()) : false;
			} else if (!permittedTags.isEmpty()) {
				return token instanceof TagToken ? !permittedTags.contains(((TagToken) token).getName()) : true;
			} else {
				return true;
			}
		} else if (contentType == CONTENT_TEXT) {
			return !(token instanceof TagToken);
		}

		return false;
	}

	boolean allowsAnything() {
		return contentType.equals(CONTENT_ALL) && childTags.size() == 0;
	}

}