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

/**
 * <p>
 * HTML tag token - descendants are start (TagNode) and end token (EndTagToken).
 * </p>
 *
 * Created by: Vladimir Nikic<br/>
 * Date: November, 2006.
 */
public abstract class TagToken implements BaseToken, Cloneable {

    protected String name;

    protected String originalSource = "";

    public TagToken() {
    }

    public TagToken(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalSource() {
        return originalSource;
    }

    public void setOriginalSource(String originalSource) {
        this.originalSource = originalSource;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Add an attribute to this tag.
     *
     * @param attName
     *          the attribute name
     * @param attValue
     *          the attribute's value string
     * @param checkXSS
     *          check the attributes for allowed names to avoid cross side
     *          scripting
     * @return
     */
    public abstract boolean addAttribute(String attName, String attValue, boolean checkXSS);

    /**
     * Check, if the attName is allowed.
     *
     * @param attName
     * @return
     */
    public abstract boolean isAllowedAttribute(String attName);

    /**
     * Get the allowed parent tags for this tag
     *
     * @return <code>null</code> if no parent tags are allowed
     */
    abstract public String getParents();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TagToken) {
            return name.equals(obj.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public Object clone() {
        TagToken tt;
        try {
            tt = (TagToken) super.clone();
            tt.name = name;
            tt.originalSource = originalSource;
            return tt;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isReduceTokenStack() {
        return false;
    }
}
