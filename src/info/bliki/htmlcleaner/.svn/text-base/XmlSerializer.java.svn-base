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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Abstract XML serializer - contains common logic for descendants.
 * </p>
 * 
 * Created by: Vladimir Nikic<br/> Date: November, 2006.
 */
public abstract class XmlSerializer {

	protected final String XML_DECLARATION = "<?xml version=\"1.0\"?>";

	protected HtmlCleaner htmlCleaner;

	protected BufferedWriter writer;

	protected XmlSerializer() {
	}

	protected XmlSerializer(Writer writer, HtmlCleaner htmlCleaner) {
		this.writer = new BufferedWriter(writer);
		this.htmlCleaner = htmlCleaner;
	}

	protected void createXml(TagNode tagNode) throws IOException {
		if (!htmlCleaner.isOmitXmlDeclaration()) {
			writer.write(XML_DECLARATION + "\n");
		}

		if (!htmlCleaner.isOmitDoctypeDeclaration()) {
			DoctypeToken doctypeToken = htmlCleaner.getDoctype();
			if (doctypeToken != null) {
				doctypeToken.serialize(this);
			}
		}

		serialize(tagNode);

		writer.flush();
		writer.close();
	}

	protected String escapeXml(String xmlContent) {
		return Utils.escapeXml(xmlContent, htmlCleaner.isAdvancedXmlEscape(), htmlCleaner.isRecognizeUnicodeChars(), htmlCleaner
				.isTranslateSpecialEntities());
	}

	protected boolean dontEscape(TagNode tagNode) {
		String tagName = tagNode.getName();
		return htmlCleaner.isUseCdataForScriptAndStyle() && ("script".equalsIgnoreCase(tagName) || "style".equalsIgnoreCase(tagName));
	}

	protected boolean isScriptOrStyle(TagNode tagNode) {
		String tagName = tagNode.getName();
		return "script".equalsIgnoreCase(tagName) || "style".equalsIgnoreCase(tagName);
	}

	protected void serializeOpenTag(TagNode tagNode, boolean newLine) throws IOException {
		String tagName = tagNode.getName();
		Map<String, String> tagAtttributes = tagNode.getAttributes();
		List tagChildren = tagNode.getChildren();

		writer.write("<" + tagName);
		for (Map.Entry<String, String> currEntry : tagAtttributes.entrySet()) {
			String attName = currEntry.getKey();
			String attValue = currEntry.getValue();
			if (htmlCleaner.isOmitXmlnsAttributes() && "xmlns".equals(attName)) {
				continue;
			}
			writer.write(" " + attName + "=\"" + escapeXml(attValue) + "\"");
		}

		if (tagChildren.size() == 0) {
			writer.write("/>");
			if (newLine) {
				writer.write("\n");
			}
		} else if (dontEscape(tagNode)) {
			writer.write("><![CDATA[");
		} else {
			writer.write(">");
		}
	}

	protected void serializeOpenTag(TagNode tagNode) throws IOException {
		serializeOpenTag(tagNode, true);
	}

	protected void serializeEndTag(TagNode tagNode, boolean newLine) throws IOException {
		String tagName = tagNode.getName();

		if (dontEscape(tagNode)) {
			writer.write("]]>");
		}

		writer.write("</" + tagName + ">");

		if (newLine) {
			writer.write("\n");
		}
	}

	Writer getWriter() {
		return writer;
	}

	protected void serializeEndTag(TagNode tagNode) throws IOException {
		serializeEndTag(tagNode, true);
	}

	protected abstract void serialize(TagNode tagNode) throws IOException;

}