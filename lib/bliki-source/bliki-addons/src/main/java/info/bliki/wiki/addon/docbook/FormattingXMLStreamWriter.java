package info.bliki.wiki.addon.docbook;

import java.util.Stack;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * 
 * Initial source copied from the <a href="https://textile-j.dev.java.net/">Textile-J</a> project
 *
 */
public class FormattingXMLStreamWriter implements XMLStreamWriter {

	private XMLStreamWriter delegate;
	private int indentLevel;
	private Stack<Integer> childCounts = new Stack<Integer>();
	private int childCount;
	
	public FormattingXMLStreamWriter(XMLStreamWriter delegate) {
		this.delegate = delegate;
	}

	public void close() throws XMLStreamException {
		delegate.close();
	}

	public void flush() throws XMLStreamException {
		delegate.flush();
	}

	public NamespaceContext getNamespaceContext() {
		return delegate.getNamespaceContext();
	}

	public String getPrefix(String uri) throws XMLStreamException {
		return delegate.getPrefix(uri);
	}

	public Object getProperty(String name) throws IllegalArgumentException {
		return delegate.getProperty(name);
	}

	public void setDefaultNamespace(String uri) throws XMLStreamException {
		delegate.setDefaultNamespace(uri);
	}

	public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
		delegate.setNamespaceContext(context);
	}

	public void setPrefix(String prefix, String uri) throws XMLStreamException {
		delegate.setPrefix(prefix, uri);
	}

	public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
		if (value == null) {
			value = "";
		}
		delegate.writeAttribute(prefix, namespaceURI, localName, value);
	}

	public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
		if (value == null) {
			value = "";
		}
		delegate.writeAttribute(namespaceURI, localName, value);
	}

	public void writeAttribute(String localName, String value) throws XMLStreamException {
		if (value == null) {
			value = "";
		}
		delegate.writeAttribute(localName, value);
	}

	public void writeCData(String data) throws XMLStreamException {
		delegate.writeCData(data);
	}

	public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
		delegate.writeCharacters(text, start, len);
	}

	public void writeCharacters(String text) throws XMLStreamException {
		if (text == null) { 
			return;
		}
		delegate.writeCharacters(text);
	}

	public void writeComment(String data) throws XMLStreamException {
		if (data == null) {
			data = "";
		}
		delegate.writeComment(data);
	}

	public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
		delegate.writeDefaultNamespace(namespaceURI);
	}

	public void writeDTD(String dtd) throws XMLStreamException {
		delegate.writeDTD(dtd);
	}

	public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		++childCount;
		maybeIndent();
		delegate.writeEmptyElement(prefix, localName, namespaceURI);
	}

	public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
		++childCount;
		maybeIndent();
		delegate.writeEmptyElement(namespaceURI, localName);
	}

	public void writeEmptyElement(String localName) throws XMLStreamException {
		++childCount;
		maybeIndent();
		delegate.writeEmptyElement(localName);
	}


	public void writeEndDocument() throws XMLStreamException {
		delegate.writeEndDocument();
	}

	public void writeEndElement() throws XMLStreamException {
		--indentLevel;
		maybeIndent();
		delegate.writeEndElement();
		childCount = childCounts.pop();
	}

	public void writeEntityRef(String name) throws XMLStreamException {
		delegate.writeEntityRef(name);
	}

	public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
		delegate.writeNamespace(prefix, namespaceURI);
	}

	public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
		delegate.writeProcessingInstruction(target, data);
	}

	public void writeProcessingInstruction(String target) throws XMLStreamException {
		delegate.writeProcessingInstruction(target);
	}

	public void writeStartDocument() throws XMLStreamException {
		delegate.writeStartDocument();
	}

	public void writeStartDocument(String encoding, String version) throws XMLStreamException {
		delegate.writeStartDocument(encoding, version);
	}

	public void writeStartDocument(String version) throws XMLStreamException {
		delegate.writeStartDocument(version);
	}

	public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		++childCount;
		maybeIndent();
		childCounts.push(childCount);
		childCount = 0;
		++indentLevel;
		delegate.writeStartElement(prefix, localName, namespaceURI);
	}

	public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
		++childCount;
		maybeIndent();
		childCounts.push(childCount);
		childCount = 0;
		++indentLevel;
		delegate.writeStartElement(namespaceURI, localName);
	}

	public void writeStartElement(String localName) throws XMLStreamException {
		++childCount;
		maybeIndent();
		childCounts.push(childCount);
		childCount = 0;
		++indentLevel;
		delegate.writeStartElement(localName);
	}

	private void maybeIndent() throws XMLStreamException {
		if (childCount == 0) {
			return;
		}
		StringBuilder buf = new StringBuilder();
		buf.append('\n');
		for (int x = 0;x<indentLevel;++x) {
			buf.append('\t');
		}
		writeCharacters(buf.toString());
	}
	
}